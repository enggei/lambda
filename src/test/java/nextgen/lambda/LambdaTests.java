package nextgen.lambda;

import static java.io.File.*;
import static java.lang.String.*;

public class LambdaTests {

   private static final String mainResources = "src/main/resources";
   private static final String mainSrc = "src/main/java";

   public static void main(String[] args) throws java.io.IOException {

      String version = args.length == 0 ? "lambda" : args[0];

      final io.vertx.core.json.JsonObject model = coreModel(version);
      final org.stringtemplate.v4.STGroup stGroup = new org.stringtemplate.v4.STGroupFile(join(separator, mainResources, join(".", "Java", "stg")), '~', '~');
      final io.vertx.core.json.JsonObject mapper = coreMapper();

      Lambda.streamJsonObjects("packages", model)
         .forEach(packagesElement -> nextgen.lambda.Lambda.streamJsonObjects("entities", packagesElement)
            .map(nextgen.lambda.Lambda::decorate)
            .forEach(entitiesElement -> nextgen.lambda.Lambda.mapClassDeclaration(stGroup, entitiesElement, mapper)
               .ifPresent(write(stGroup, packagesElement, entitiesElement))));

      final io.vertx.core.json.JsonObject newModel = new io.vertx.core.json.JsonObject();
      newModel.put("packages", new io.vertx.core.json.JsonArray());

      nextgen.lambda.Lambda.files(mainSrc, "java")
        // .filter(file -> file.toString().contains("/domain/"))
         .peek(file -> System.out.println("parse " + file))
         .filter(parse(newModel))
         .findFirst()
         .ifPresentOrElse(path -> {
               try {
                  System.err.println(path + " has errors \n" + java.nio.file.Files.readString(path));
               } catch (java.io.IOException e) {
                  throw new RuntimeException(e);
               }
            },
            () -> {

               try {
                  final String newModelName = "lambda_new";
                  final java.nio.file.Path newModelFile = java.nio.file.Path.of(join(separator, mainResources, join(".", newModelName, "json")));

                  java.nio.file.Files.createDirectories(newModelFile.getParent());
                  java.nio.file.Files.writeString(newModelFile, newModel.encodePrettily());
                  System.out.println("w " + newModelFile);

//                  LambdaTests.main(new String[]{
//                     newModelName
//                  });

               } catch (java.io.IOException e) {
                  throw new RuntimeException(e);
               }
            });
   }

   private static java.util.function.Predicate<java.nio.file.Path> parse(io.vertx.core.json.JsonObject model) {
      return path -> {
         try {

            String packageName = path.toString().substring(mainSrc.length() + 1, path.toString().lastIndexOf(separator)).replaceAll(separator, ".").trim();

            final io.vertx.core.json.JsonObject aPackage = nextgen.lambda.Lambda.streamJsonObjects("packages", model)
               .filter(element -> element.getString("name").equals(packageName))
               .findFirst()
               .orElseGet(() -> {
                  System.out.println("package " + packageName);
                  final io.vertx.core.json.JsonObject newPackage = new io.vertx.core.json.JsonObject();
                  newPackage.put("name", packageName);
                  newPackage.put("entities", new io.vertx.core.json.JsonArray());
                  newPackage.put("interfaces", new io.vertx.core.json.JsonArray());
                  newPackage.put("enums", new io.vertx.core.json.JsonArray());
                  model.getJsonArray("packages").add(newPackage);
                  return newPackage;
               });

            com.github.javaparser.StaticJavaParser.parse(path).getTypes()
               .forEach(typeDeclaration -> {

                  typeDeclaration.ifClassOrInterfaceDeclaration(classOrInterfaceDeclaration -> {
                     if (classOrInterfaceDeclaration.isInterface())
                        aPackage.getJsonArray("interfaces").add(Lambda.mapClassDeclaration(classOrInterfaceDeclaration));
                     else
                        aPackage.getJsonArray("entities").add(Lambda.mapClassDeclaration(classOrInterfaceDeclaration));
                  });

                  typeDeclaration.ifEnumDeclaration(enumDeclaration -> aPackage.getJsonArray("enums").add(Lambda.mapEnumDeclaration(enumDeclaration)));
               });

            return false;
         } catch (java.io.IOException e) {
            return true;
         }
      };
   }

   private static java.util.function.Consumer<org.stringtemplate.v4.ST> write(org.stringtemplate.v4.STGroup stGroup, io.vertx.core.json.JsonObject packagesElement, io.vertx.core.json.JsonObject model) {
      return st -> {
         try {
            final String packageName = packagesElement.getString("name");
            final String packagePath = join(separator, mainSrc, packagesElement.getString("name").replaceAll("\\.", separator));
            final String className = model.getString("name");
            final String javaFile = join(separator, packagePath, join(".", className, "java"));

            final org.stringtemplate.v4.ST compilationUnit = stGroup.getInstanceOf("compilationUnit");
            compilationUnit.add("packageName", packageName);
            compilationUnit.add("members", st);

            java.nio.file.Files.createDirectories(java.nio.file.Path.of(packagePath));
            java.nio.file.Files.writeString(java.nio.file.Path.of(javaFile), compilationUnit.render());
            System.out.println("w " + javaFile);
         } catch (java.io.IOException e) {
            throw new RuntimeException(e);
         }
      };
   }

   private static io.vertx.core.json.JsonObject coreModel(String version) throws java.io.IOException {
      return new io.vertx.core.json.JsonObject(java.nio.file.Files.readString(java.nio.file.Path.of(join(separator, mainResources, join(".", version, "json")))));
   }

   private static io.vertx.core.json.JsonObject coreMapper() throws java.io.IOException {
      return new io.vertx.core.json.JsonObject(java.nio.file.Files.readString(java.nio.file.Path.of(join(separator, mainResources, "core_mapper.json"))));
   }
}