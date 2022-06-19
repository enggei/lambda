package nextgen.lambda;

import com.github.javaparser.*;
import com.github.javaparser.ast.body.*;
import io.vertx.core.file.FileSystem;
import io.vertx.core.json.*;
import org.stringtemplate.v4.*;

import java.io.*;
import java.util.stream.*;

import static java.io.File.*;

public class Lambda {


//   public static void main(String[] args) {
//
//      final VertxOptions options = new VertxOptions();
//
//      final Vertx vertx = Vertx.vertx(options);
//      final FileSystem fileSystem = vertx.fileSystem();
//      final EventBus eventBus = vertx.eventBus();
//
//      final Project project = new Project("Lambda", ".");
//      final String modelPath = join(separator, project.srcMainResources(), "lambda.json");
//      final String templatePath = join(separator, project.srcMainResources(), "Java.json");
//
//      final nextgen.lambda.domain.core.Model model = new nextgen.lambda.domain.core.Model(modelPath);
//      final org.stringtemplate.v4.STGroup stGroup = new org.stringtemplate.v4.STGroupFile(join(separator, project.srcMainResources(), "Java.stg"));
//      stgToJson(fileSystem, stGroup);
//
//      eventBus.consumer("lambda", write(fileSystem, project, stGroup));
//
//      try {
//         javax.swing.UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarculaLaf());
//      } catch (javax.swing.UnsupportedLookAndFeelException ignore) {
//      }
//
//      final UI ui = new UI();
//      ui.setTitle("\u03BB");
//      ui.navigator = new Navigator(vertx);
//      ui.canvas = new Canvas(ui);
//      ui.editor = new Editor(ui);
//
//      fileSystem.readFile(modelPath, ar -> {
//         if (ar.failed()) throw new RuntimeException(ar.cause());
//         model.delegate = new JsonObject(ar.result());
//
//         eventBus.publish("lambda", model.delegate);
//         ui.navigator.root.add(new nextgen.lambda.domain.core.navigator.ModelNavigatorTreeNode(ui.navigator, model));
//
//         parse(modelPath, fileSystem, model.delegate);
//      });
//
//      fileSystem.readFile(templatePath, ar -> {
//         if (ar.failed()) throw new RuntimeException(ar.cause());
//         final nextgen.lambda.domain.templates.TemplateGroup templateGroup = new nextgen.lambda.domain.templates.TemplateGroup(new io.vertx.core.json.JsonObject(ar.result()));
//         ui.navigator.root.add(new nextgen.lambda.domain.templates.navigator.TemplateGroupNavigatorTreeNode(ui.navigator, templateGroup));
//      });
//
//      ui.showUI(() -> System.exit(0));
//   }

   private static void stgToJson(io.vertx.core.file.FileSystem fileSystem, org.stringtemplate.v4.STGroup stGroup) {
      final io.vertx.core.json.JsonObject tmp = new io.vertx.core.json.JsonObject();
      tmp.put("name", stGroup.getName());

      tmp.put("templates", new io.vertx.core.json.JsonArray());
      stGroup.getTemplateNames()
         .stream().filter(templateName -> !templateName.contains("_sub"))
         .forEach(templateName -> {
            final io.vertx.core.json.JsonObject template = new io.vertx.core.json.JsonObject();
            template.put("name", templateName.substring(1));

            final org.stringtemplate.v4.compiler.CompiledST compiledST = stGroup.lookupTemplate(templateName);
            template.put("content", compiledST.template);

            final io.vertx.core.json.JsonArray parameters = new io.vertx.core.json.JsonArray();
            java.util.Optional.ofNullable(compiledST.formalArguments).ifPresent(arguments -> {
               arguments.forEach((pName, argument) -> {
                  final io.vertx.core.json.JsonObject parameter = new io.vertx.core.json.JsonObject();
                  parameter.put("name", pName);
                  parameter.put("qty", compiledST.template.contains("~" + pName + ":{") ? "MANY" : "ONE");
                  parameters.add(parameter);
               });
            });
            if (!parameters.isEmpty()) template.put("parameters", parameters);

            tmp.getJsonArray("templates").add(template);
         });

      fileSystem.writeFile("/home/goe/github/lambda/src/main/resources/" + stGroup.getName() + ".json", tmp.toBuffer(), ar2 -> {
         if (ar2.failed()) throw new RuntimeException(ar2.cause());
         System.out.println("/home/goe/github/lambda/src/main/resources/" + stGroup.getName() + ".json");
      });
   }


   public static java.util.Optional<String> singleInput(javax.swing.JComponent parent, String message, String initial) {
      final String s = javax.swing.JOptionPane.showInputDialog(parent, message, initial);
      if (s == null || s.trim().length() == 0) return java.util.Optional.empty();
      return java.util.Optional.of(s.trim());
   }

   public static void toClipboard(String s) {
      java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new java.awt.datatransfer.StringSelection(s), (cp, contents) -> System.out.println(contents.toString()));
   }

   public static java.util.Optional<String> fromClipboard() {
      final java.awt.datatransfer.Transferable contents = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
      if ((contents != null) && contents.isDataFlavorSupported(java.awt.datatransfer.DataFlavor.stringFlavor)) {
         try {
            final String data = (String) contents.getTransferData(java.awt.datatransfer.DataFlavor.stringFlavor);
            return data.trim().length() == 0 ? java.util.Optional.empty() : java.util.Optional.of(data);
         } catch (Exception e) {
            return java.util.Optional.empty();
         }
      }
      return java.util.Optional.empty();
   }


//   private static Handler<Message<Object>> write(FileSystem fileSystem, Project project, STGroup stGroup) {
//      return message -> streamJsonObjects("packages", (JsonObject) message.body())
//         .forEach(aPackage -> {
//
//            streamJsonObjects("entities", aPackage).map(Lambda::decorate).forEach(entity -> {
//               writeDomainEntity(fileSystem, project, stGroup, aPackage, entity);
//               writeDomainTreeNodes(fileSystem, project, stGroup, aPackage, entity);
//            });
//
//            streamJsonObjects("enums", aPackage).map(Lambda::decorate).forEach(entity -> {
//               writeDomainEnum(fileSystem, project, stGroup, aPackage, entity);
//            });
//
//            streamJsonObjects("interfaces", aPackage).map(Lambda::decorate).forEach(entity -> {
//               writeDomainInterface(fileSystem, project, stGroup, aPackage, entity);
//            });
//         });
//   }

//   private static void writeDomainEnum(io.vertx.core.file.FileSystem fileSystem, nextgen.lambda.domain.maven.Project project, org.stringtemplate.v4.STGroup stGroup, io.vertx.core.json.JsonObject aPackage, io.vertx.core.json.JsonObject model) {
//
//      final String packageName = aPackage.getString("name");
//      final String enumName = model.getString("name");
//
//      final ST enumDeclaration = stGroup.getInstanceOf("enumDeclaration");
//      enumDeclaration.add("name", model.getString("name"));
//      enumDeclaration.add("scope", model.getString("scope", null));
//      streamObjects("values", model).forEach(element -> enumDeclaration.add("values", element));
//
//      final org.stringtemplate.v4.ST compilationUnit = stGroup.getInstanceOf("compilationUnit");
//      compilationUnit.add("packageName", packageName);
//      compilationUnit.add("members", enumDeclaration);
//
//      fileSystem.mkdirs(join(separator, project.srcMainJava(), packageName.replaceAll("\\.", separator)), ar -> {
//         if (ar.failed()) throw new RuntimeException(ar.cause());
//         write(fileSystem, compilationUnit, getFile(project, packageName, enumName));
//      });
//   }

//   private static void writeDomainInterface(io.vertx.core.file.FileSystem fileSystem, nextgen.lambda.domain.maven.Project project, org.stringtemplate.v4.STGroup stGroup, io.vertx.core.json.JsonObject aPackage, io.vertx.core.json.JsonObject model) {
//
//      final String packageName = aPackage.getString("name");
//      final String enumName = model.getString("name");
//
//      final ST interfaceDeclaration = stGroup.getInstanceOf("interfaceDeclaration");
//      interfaceDeclaration.add("name", model.getString("name"));
//      interfaceDeclaration.add("scope", model.getString("scope", null));
//      streamJsonObjects("annotations", model).forEach(child -> interfaceDeclaration.add("annotations", annotation(stGroup, child)));
//      streamObjects("typeParameters", model).forEach(element -> interfaceDeclaration.add("typeParameters", element));
//      streamJsonObjects("members", model).forEach(element -> {
//         final ST interfaceMethod = stGroup.getInstanceOf("interfaceMethod");
//         interfaceMethod.add("name", element.getString("name"));
//         interfaceMethod.add("type", element.getString("type"));
//         interfaceMethod.add("scope", element.getString("scope"));
//         streamJsonObjects("annotations", element).forEach(child -> interfaceMethod.add("annotations", annotation(stGroup, child)));
//         streamJsonObjects("parameters", element).forEach(child -> interfaceMethod.add("parameters", parameter(stGroup, child)));
//         streamObjects("throws", element).forEach(child -> interfaceMethod.add("throws", child));
//         interfaceDeclaration.add("members", interfaceMethod);
//      });
//
//      final org.stringtemplate.v4.ST compilationUnit = stGroup.getInstanceOf("compilationUnit");
//      compilationUnit.add("packageName", packageName);
//      compilationUnit.add("members", interfaceDeclaration);
//
//      fileSystem.mkdirs(join(separator, project.srcMainJava(), packageName.replaceAll("\\.", separator)), ar -> {
//         if (ar.failed()) throw new RuntimeException(ar.cause());
//         write(fileSystem, compilationUnit, getFile(project, packageName, enumName));
//      });
//   }

//   private static void writeDomainEntity(io.vertx.core.file.FileSystem fileSystem, nextgen.lambda.domain.maven.Project project, org.stringtemplate.v4.STGroup stGroup, io.vertx.core.json.JsonObject aPackage, io.vertx.core.json.JsonObject model) {
//
//      final String packageName = aPackage.getString("name");
//      final String className = model.getString("name");
//
//      final org.stringtemplate.v4.ST compilationUnit = stGroup.getInstanceOf("compilationUnit");
//      compilationUnit.add("packageName", packageName);
////      compilationUnit.add("members", classDeclaration);
//      fileSystem.mkdirs(join(separator, project.srcMainJava(), packageName.replaceAll("\\.", separator)), ar -> {
//         if (ar.failed()) throw new RuntimeException(ar.cause());
//         write(fileSystem, compilationUnit, getFile(project, packageName, className));
//      });
//   }

   public static java.util.Optional<org.stringtemplate.v4.ST> mapClassDeclaration(STGroup stGroup, io.vertx.core.json.JsonObject model, io.vertx.core.json.JsonObject map) {

      final org.stringtemplate.v4.ST template = stGroup.getInstanceOf(map.getString("templateName"));

      streamJsonObjects("ONE", map)
         .forEach(one -> {
            try {
               final JsonObject mapper = one.getJsonObject("mapper");
               if (mapper != null) mapClassDeclaration(stGroup, one, mapper).ifPresent(mapped -> template.add(one.getString("template"), mapped));
               else {
                  final Object value = model.getValue(one.getString("model"));
                  if (value != null && value.toString().trim().length() > 0) template.add(one.getString("template"), value);
               }
            } catch (Throwable throwable) {
               System.err.println("ERROR with " + template.getName());
               System.err.println("\t" + throwable.getMessage());
            }
         });

      streamJsonObjects("MANY", map)
         .forEach(many -> {
            try {
               final io.vertx.core.json.JsonObject mapper = many.getJsonObject("mapper");
               if (mapper != null) streamJsonObjects(many.getString("model"), model).forEach(manyElement -> mapClassDeclaration(stGroup, manyElement, mapper).ifPresent(mapped -> template.add(many.getString("template"), mapped)));
               else streamObjects(many.getString("model"), model).forEach(manyElement -> template.add(many.getString("template"), manyElement));
            } catch (Throwable throwable) {
               System.err.println("ERROR with " + template.getName());
               System.err.println("\t" + throwable.getMessage());
            }
         });

      return java.util.Optional.of(template);
   }

   public static java.util.stream.Stream<java.nio.file.Path> files(String path, String postfix) throws java.io.IOException {
      return java.nio.file.Files.walk(java.nio.file.Paths.get(path))
         .filter(java.nio.file.Files::isRegularFile)
         .filter(file -> file.toString().endsWith(postfix));
   }

   static class Mapper {

      io.vertx.core.json.JsonObject delegate = new io.vertx.core.json.JsonObject();

      Mapper(String templateName) {
         delegate.put("templateName", templateName);
         delegate.put("ONE", new io.vertx.core.json.JsonArray());
         delegate.put("MANY", new io.vertx.core.json.JsonArray());
      }

      Mapper addOne(String template, String model) {
         delegate.getJsonArray("ONE").add(new io.vertx.core.json.JsonObject().put("template", template).put("model", model));
         return this;
      }

      Mapper addMany(String template, String model) {
         delegate.getJsonArray("MANY").add(new io.vertx.core.json.JsonObject().put("template", template).put("model", model));
         return this;
      }

      Mapper addMany(String template, String model, Mapper mapper) {
         delegate.getJsonArray("MANY").add(new io.vertx.core.json.JsonObject().put("template", template).put("model", model).put("mapper", mapper.delegate));
         return this;
      }
   }

//   private static void writeDomainTreeNodes(io.vertx.core.file.FileSystem fileSystem, nextgen.lambda.domain.maven.Project project, org.stringtemplate.v4.STGroup stGroup, io.vertx.core.json.JsonObject aPackage, io.vertx.core.json.JsonObject model) {
//
//      final String packageName = aPackage.getString("name") + ".navigator";
//      final String className = model.getString("name") + "NavigatorTreeNode";
//      final String modelType = join(".", aPackage.getString("name"), model.getString("name"));
//
//      final ST classDeclaration = stGroup.getInstanceOf("classDeclaration");
//      classDeclaration.add("name", className);
//      classDeclaration.add("extending", "nextgen.lambda.domain.ui.NavigatorTreeNode");
//      classDeclaration.add("scope", "public");
//
//      final org.stringtemplate.v4.ST classConstructor = stGroup.getInstanceOf("classConstructor");
//      classConstructor.add("name", className);
//      classConstructor.add("scope", "public");
//      classConstructor.add("parameters", stGroup.getInstanceOf("parameter").add("name", "navigator").add("type", "nextgen.lambda.domain.ui.Navigator"));
//      classConstructor.add("parameters", stGroup.getInstanceOf("parameter").add("name", "model").add("type", modelType));
//      classConstructor.add("statements", "super(navigator, model);");
//      streamJsonObjects("methods", model)
//         .filter(element -> element.getString("type", "void").startsWith("java.util.stream"))
//         .forEach(element -> classConstructor.add("statements", "model." + element.getString("name") + "().forEach(element-> add(new " + element.getString("elementType") + "NavigatorTreeNode(navigator, element)));"));
//
//      final org.stringtemplate.v4.ST getModel = stGroup.getInstanceOf("classMethod");
//      getModel.add("name", "model");
//      getModel.add("type", modelType);
//      getModel.add("statements", "return (" + modelType + ") getUserObject();");
//      classDeclaration.add("methods", getModel);
//
//      streamJsonObjects("methods", model)
//         .filter(element -> element.getString("name").startsWith("name"))
//         .findFirst()
//         .ifPresent(element -> {
//            final org.stringtemplate.v4.ST toString = stGroup.getInstanceOf("classMethod");
//            toString.add("name", "toString");
//            toString.add("scope", "public");
//            toString.add("annotations", annotation(stGroup, new io.vertx.core.json.JsonObject().put("name", "Override")));
//            toString.add("type", "String");
//            toString.add("statements", "return model().name();");
//            classDeclaration.add("methods", toString);
//         });
//      classDeclaration.add("constructors", classConstructor);
//
//      final org.stringtemplate.v4.ST actions = stGroup.getInstanceOf("classMethod");
//      actions.add("name", "actions");
//      actions.add("scope", "public");
//      actions.add("annotations", annotation(stGroup, new io.vertx.core.json.JsonObject().put("name", "Override")));
//      actions.add("type", "java.util.Collection<nextgen.lambda.domain.ui.Action>");
//      actions.add("statements", "final java.util.ArrayList<nextgen.lambda.domain.ui.Action> actions = new java.util.ArrayList<>();");
//      actions.add("statements", "actions.add(new nextgen.lambda.domain.ui.Action(\"model\", () -> {\n" +
//         "         System.out.println(model());\n" +
//         "         return null;\n" +
//         "      }));");
//      actions.add("statements", "return actions;");
//      classDeclaration.add("methods", actions);
//
//      final org.stringtemplate.v4.ST compilationUnit = stGroup.getInstanceOf("compilationUnit");
//      compilationUnit.add("packageName", packageName);
//      compilationUnit.add("members", classDeclaration);
//
//      fileSystem.mkdirs(join(separator, project.srcMainJava(), packageName.replaceAll("\\.", separator)), ar -> {
//         if (ar.failed()) throw new RuntimeException(ar.cause());
//         write(fileSystem, compilationUnit, getFile(project, packageName, className));
//      });
//   }

   private static void write(io.vertx.core.file.FileSystem fileSystem, org.stringtemplate.v4.ST compilationUnit, String file) {
      fileSystem.writeFile(file, io.vertx.core.buffer.Buffer.buffer(compilationUnit.render()), ar2 -> {
         if (ar2.failed()) throw new RuntimeException(ar2.cause());
         System.out.println(file);
      });
   }

//   private static String getFile(nextgen.lambda.domain.maven.Project project, String packageName, String className) {
//      return join(separator, project.srcMainJava(), packageName.replaceAll("\\.", separator), join(".", className, "java"));
//   }

   static JsonObject decorate(JsonObject model) {
      if (model.getString("scope") == null) model.put("scope", "public");

      streamJsonObjects("fields", model).forEach(field -> {
         if (field.getString("scope") == null) field.put("scope", "public");

      });

      streamJsonObjects("methods", model).forEach(method -> {
         if (method.getString("scope") == null) method.put("scope", "public");
      });

      streamJsonObjects("constructors", model).forEach(method -> {
         if (method.getString("scope") == null) method.put("scope", "public");
         if (method.getString("name") == null) method.put("name", model.getString("name"));
      });

      streamJsonObjects("nested", model).forEach(nextgen.lambda.Lambda::decorate);

      return model;
   }

   private static void parse(String path, FileSystem fileSystem, JsonObject lambda) {

      final JsonArray packages = lambda.getJsonArray("packages");

      final JsonObject aPackage = new JsonObject();
      aPackage.put("name", path.substring(path.indexOf("java/") + 5).replaceAll(separator, "."));
      aPackage.put("entities", new JsonArray());
      aPackage.put("interfaces", new JsonArray());
      aPackage.put("enums", new JsonArray());

      fileSystem.readDir(path, ar -> {
         if (ar.failed()) throw new RuntimeException(ar.cause());
         ar.result().forEach(file -> {
            if (file.endsWith(".java")) {

               try {
                  StaticJavaParser.parse(java.nio.file.Path.of(file))
                     .getTypes()
                     .forEach(typeDeclaration -> {

                        typeDeclaration.ifClassOrInterfaceDeclaration(classOrInterfaceDeclaration -> {
                           if (classOrInterfaceDeclaration.isInterface()) {
                              aPackage.getJsonArray("interfaces").add(mapClassDeclaration(classOrInterfaceDeclaration));
                           } else {
                              aPackage.getJsonArray("entities").add(mapClassDeclaration(classOrInterfaceDeclaration));
                           }
                        });

                        typeDeclaration.ifEnumDeclaration(enumDeclaration -> {
                           aPackage.getJsonArray("enums").add(mapEnumDeclaration(enumDeclaration));
                        });
                     });

               } catch (IOException e) {
                  throw new RuntimeException(e);
               }

            } else if (!file.contains(".")) {
               parse(file, fileSystem, lambda);
            }
         });
      });
   }

   public static JsonObject mapEnumDeclaration(EnumDeclaration declaration) {
      final JsonObject model = new JsonObject();
      model.put("name", declaration.getName().asString());
      model.put("scope", declaration.getModifiers().stream().map(modifier -> modifier.getKeyword().asString()).collect(java.util.stream.Collectors.joining(" ")));
      model.put("values", new JsonArray());
      declaration.getEntries().forEach(element -> model.getJsonArray("values").add(element.getNameAsString()));
      return model;
   }

   public static JsonObject mapClassDeclaration(ClassOrInterfaceDeclaration declaration) {
      final JsonObject model = new JsonObject();
      model.put("name", declaration.getName().asString());
      model.put("scope", declaration.getModifiers().stream().map(element -> element.getKeyword().asString()).collect(java.util.stream.Collectors.joining(" ")));
      model.put("extends", declaration.getExtendedTypes().stream().map(com.github.javaparser.ast.type.ClassOrInterfaceType::asString).collect(java.util.stream.Collectors.joining(", ")));

      model.put("fields", new JsonArray());
      model.put("methods", new JsonArray());
      model.put("constructors", new JsonArray());
      model.put("nested", new JsonArray());
      model.put("annotations", new io.vertx.core.json.JsonArray());
      model.put("typeParameters", new io.vertx.core.json.JsonArray());

      declaration.getTypeParameters().forEach(element -> model.getJsonArray("typeParameters").add(element.getName().asString()));
      declaration.getAnnotations().stream().map(nextgen.lambda.Lambda::mapAnnotation).forEach(element -> model.getJsonArray("annotations").add(element));

      declaration.getFields()
         .forEach(fieldDeclaration -> fieldDeclaration.getVariables()
            .forEach(variableDeclaration -> model.getJsonArray("fields")
               .add(mapFieldDeclaration(variableDeclaration))));

      declaration.getMethods()
         .forEach(methodDeclaration -> {
            model.getJsonArray("methods").add(mapMethodDeclaration(methodDeclaration));
         });

      declaration.getConstructors().forEach(constructorDeclaration -> model.getJsonArray("constructors").add(mapConstructorDeclaration(constructorDeclaration)));

      //typeDeclaration.getMembers().stream().filter(BodyDeclaration::isClassOrInterfaceDeclaration).map(BodyDeclaration::asClassOrInterfaceDeclaration).forEach(classOrInterfaceDeclaration -> entity.getJsonArray("nested").add(mapEntity(classOrInterfaceDeclaration)));

      return model;
   }

   private static io.vertx.core.json.JsonObject mapAnnotation(com.github.javaparser.ast.expr.AnnotationExpr element) {
      return new io.vertx.core.json.JsonObject().put("name", element.getName().asString());
   }

   private static io.vertx.core.json.JsonObject mapConstructorDeclaration(com.github.javaparser.ast.body.ConstructorDeclaration declaration) {
      final io.vertx.core.json.JsonObject model = new io.vertx.core.json.JsonObject();
      model.put("name", declaration.getNameAsString());
      model.put("scope", declaration.getModifiers().stream().map(modifier -> modifier.getKeyword().asString()).collect(java.util.stream.Collectors.joining(" ")));
      model.put("parameters", new io.vertx.core.json.JsonArray());
      model.put("statements", new io.vertx.core.json.JsonArray());
      model.put("annotations", new io.vertx.core.json.JsonArray());

      declaration.getAnnotations().stream().map(nextgen.lambda.Lambda::mapAnnotation).forEach(element -> model.getJsonArray("annotations").add(element));

      declaration.getParameters()
         .forEach(parameterDeclaration -> model.getJsonArray("parameters")
            .add(mapParameter(parameterDeclaration)));

      declaration.getBody().getStatements()
         .forEach(statement -> model.getJsonArray("statements")
            .add(statement.toString()));

      return model;
   }

   private static io.vertx.core.json.JsonObject mapMethodDeclaration(com.github.javaparser.ast.body.MethodDeclaration declaration) {
      final io.vertx.core.json.JsonObject model = new io.vertx.core.json.JsonObject();
      model.put("type", declaration.getTypeAsString());
      model.put("name", declaration.getNameAsString());
      model.put("scope", declaration.getModifiers().stream().map(modifier -> modifier.getKeyword().asString()).collect(java.util.stream.Collectors.joining(" ")));
      model.put("parameters", new io.vertx.core.json.JsonArray());
      model.put("statements", new io.vertx.core.json.JsonArray());
      model.put("annotations", new io.vertx.core.json.JsonArray());

      declaration.getAnnotations().stream().map(nextgen.lambda.Lambda::mapAnnotation).forEach(element -> model.getJsonArray("annotations").add(element));

      declaration.getParameters().forEach(parameterDeclaration -> {
         final io.vertx.core.json.JsonObject parameter = mapParameter(parameterDeclaration);
         model.getJsonArray("parameters").add(parameter);
      });

      declaration.getBody()
         .ifPresent(blockStmt -> blockStmt.getStatements()
            .forEach(statement -> model.getJsonArray("statements").add(statement.toString())));
      return model;
   }

   private static io.vertx.core.json.JsonObject mapParameter(com.github.javaparser.ast.body.Parameter declaration) {
      final io.vertx.core.json.JsonObject parameter = new io.vertx.core.json.JsonObject();
      parameter.put("name", declaration.getNameAsString());
      parameter.put("type", declaration.getTypeAsString());
      return parameter;
   }

   public static io.vertx.core.json.JsonObject mapFieldDeclaration(com.github.javaparser.ast.body.VariableDeclarator declaration) {
      final io.vertx.core.json.JsonObject model = new io.vertx.core.json.JsonObject();
      model.put("name", declaration.getNameAsString());
      model.put("type", declaration.getTypeAsString());
      return model;
   }

   public static Stream<Object> streamObjects(String name, JsonObject model) {
      return model.getJsonArray(name, new JsonArray()).stream();
   }

   public static Stream<JsonObject> streamJsonObjects(String name, JsonObject model) {
      return model.getJsonArray(name, new JsonArray()).stream().map(o -> {
         try
         {
            return (JsonObject) o;
         }catch (Throwable throwable) {
            throw new RuntimeException(throwable);
         }
      });
   }
}