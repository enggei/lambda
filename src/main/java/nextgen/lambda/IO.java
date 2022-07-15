package nextgen.lambda;

import static java.nio.file.Files.*;
import static nextgen.lambda.LOG.*;

public final class IO {
   static final java.util.logging.Logger log = logger(IO.class);

   public static final java.nio.file.Path srcMain = java.nio.file.Path.of("src/main");
   public static final java.nio.file.Path mainJava = java.nio.file.Path.of(srcMain.toString(), "java");
   public static final java.nio.file.Path mainResources = java.nio.file.Path.of(srcMain.toString(), "resources");

   public static final java.nio.file.Path userHome = java.nio.file.Path.of(System.getProperty("user.home"));

   public static java.nio.file.Path create(String root, String dir, String filename) throws java.io.IOException {
      final java.nio.file.Path file = java.nio.file.Paths.get(root, dir, filename);
      if (!exists(file.getParent())) {
         log.info("create dir " + file.getParent().toString());
         createDirectories(file.getParent());
      }
      if (!exists(file)) {
         log.info("create file " + file.getParent().toString());
         createFile(file);
      }
      return file;
   }

   public static java.util.stream.Stream<String> toLines(java.nio.file.Path file) {
      log.info(file.toString() + " (Lines)");
      try {
         return readAllLines(file).stream();
      } catch (Throwable throwable) {
         onException(log, throwable);
         return java.util.stream.Stream.empty();
      }
   }

   public static void deleteDirectory(java.nio.file.Path directory) {
      if (java.nio.file.Files.exists(directory)) walk(directory, new java.nio.file.SimpleFileVisitor<>() {
         @Override
         public java.nio.file.FileVisitResult visitFile(java.nio.file.Path file, java.nio.file.attribute.BasicFileAttributes attrs) throws java.io.IOException {
            log.warning("deleting " + file);
            java.nio.file.Files.delete(file);
            return java.nio.file.FileVisitResult.CONTINUE;
         }

         @Override
         public java.nio.file.FileVisitResult postVisitDirectory(java.nio.file.Path dir, java.io.IOException throwable) throws java.io.IOException {
            log.warning("deleting " + dir);
            java.nio.file.Files.delete(dir);
            return java.nio.file.FileVisitResult.CONTINUE;
         }
      });
   }

   public static void copyDirectory(java.nio.file.Path src, java.nio.file.Path target, java.nio.file.CopyOption... options) {
      if (java.nio.file.Files.exists(src)) walk(src, new java.nio.file.SimpleFileVisitor<>() {

         @Override
         public java.nio.file.FileVisitResult preVisitDirectory(java.nio.file.Path dir, java.nio.file.attribute.BasicFileAttributes attrs) throws java.io.IOException {
            final java.nio.file.Path dst = target.resolve(src.relativize(dir));
            log.warning("copying " + dir + " " + dst);
            java.nio.file.Files.createDirectories(dst);
            return java.nio.file.FileVisitResult.CONTINUE;
         }

         @Override
         public java.nio.file.FileVisitResult visitFile(java.nio.file.Path file, java.nio.file.attribute.BasicFileAttributes attrs) throws java.io.IOException {
            final java.nio.file.Path dst = target.resolve(src.relativize(file));
            log.warning("copying " + file + " " + dst);
            java.nio.file.Files.copy(file, dst, options);
            return java.nio.file.FileVisitResult.CONTINUE;
         }
      });
   }

   public static void visitDirectory(java.nio.file.Path path, java.util.function.Consumer<java.nio.file.Path> consumer) {
      if (java.nio.file.Files.exists(path)) walk(path, new java.nio.file.SimpleFileVisitor<>() {

         @Override
         public java.nio.file.FileVisitResult preVisitDirectory(java.nio.file.Path dir, java.nio.file.attribute.BasicFileAttributes attrs) throws java.io.IOException {
            consumer.accept(dir);
            return java.nio.file.FileVisitResult.CONTINUE;
         }

         @Override
         public java.nio.file.FileVisitResult visitFile(java.nio.file.Path file, java.nio.file.attribute.BasicFileAttributes attrs) throws java.io.IOException {
            consumer.accept(file);
            return java.nio.file.FileVisitResult.CONTINUE;
         }
      });
   }

   public static java.util.stream.Stream<java.nio.file.Path> listFilesIn(java.nio.file.Path path) {
      final java.util.TreeSet<java.nio.file.Path> paths = new java.util.TreeSet<>();
      if (java.nio.file.Files.exists(path)) {
         walk(path, new java.nio.file.SimpleFileVisitor<>() {

            @Override
            public java.nio.file.FileVisitResult preVisitDirectory(java.nio.file.Path dir, java.nio.file.attribute.BasicFileAttributes attrs) throws java.io.IOException {
               paths.add(dir);
               return java.nio.file.FileVisitResult.CONTINUE;
            }

            @Override
            public java.nio.file.FileVisitResult visitFile(java.nio.file.Path file, java.nio.file.attribute.BasicFileAttributes attrs) throws java.io.IOException {
               paths.add(file);
               return java.nio.file.FileVisitResult.CONTINUE;
            }
         });
      }
      return paths.stream();
   }

   public static void walk(java.nio.file.Path src, java.nio.file.SimpleFileVisitor<java.nio.file.Path> visitor) {
      try {
         java.nio.file.Files.walkFileTree(src, visitor);
      } catch (java.io.IOException e) {
         EVENTS.exception(e);
      }
   }

   public static io.vertx.core.json.JsonObject toJsonObject(java.nio.file.Path file) {
      log.info(file.toString() + " (JsonObject)");
      try {
         return new io.vertx.core.json.JsonObject(io.vertx.core.buffer.Buffer.buffer(readAllBytes(file)));
      } catch (Throwable throwable) {
         onException(log, throwable);
         return new io.vertx.core.json.JsonObject();
      }
   }

   public static io.vertx.core.json.JsonArray toJsonArray(java.nio.file.Path file) {
      log.info(file.toString() + " (JsonArray)");
      try {
         return new io.vertx.core.json.JsonArray(io.vertx.core.buffer.Buffer.buffer(readAllBytes(file)));
      } catch (java.io.IOException throwable) {
         onException(log, throwable);
         return new io.vertx.core.json.JsonArray();
      }
   }

   public static java.util.stream.Stream<java.nio.file.Path> directoriesIn(java.nio.file.Path directory) {
      log.info(directory.toString());
      if (!exists(directory)) return pathDoesNotExists(directory);
      try {
         return list(directory).filter(path -> path.toFile().isDirectory()).peek(path -> log.info(path.toString()));
      } catch (java.io.IOException e) {
         EVENTS.exception(e);
         return java.util.stream.Stream.empty();
      }
   }

   public static java.util.stream.Stream<java.nio.file.Path> directoriesIn(java.nio.file.Path directory, String packageName) {
      return directoriesIn(java.nio.file.Path.of(directory.toString(), packageName.replaceAll("\\.", java.io.File.separator)));
   }

   public static java.util.stream.Stream<java.nio.file.Path> javaFilesIn(java.nio.file.Path directory) {
      return listFiles(directory, ".java");
   }

   public static java.util.stream.Stream<java.nio.file.Path> javaFilesIn(java.nio.file.Path directory, String packageName) {
      final java.nio.file.Path packageDir = java.nio.file.Path.of(directory.toString(), packageName.replaceAll("\\.", java.io.File.separator));
      return listFiles(packageDir, ".java");
   }

   public static java.util.stream.Stream<java.nio.file.Path> listFiles(java.nio.file.Path packageDir, String endsWith) {
      log.info(packageDir.toString());
      if (!exists(packageDir)) return pathDoesNotExists(packageDir);
      try {
         return list(packageDir).filter(path -> path.toString().endsWith(endsWith)).peek(path -> log.info(path.toString()));
      } catch (java.io.IOException e) {
         nextgen.lambda.EVENTS.exception(e);
         return java.util.stream.Stream.empty();
      }
   }

   public static java.util.stream.Stream<java.nio.file.Path> jsonFilesIn(java.nio.file.Path directory) throws java.io.IOException {
      return listFiles(directory, ".json");
   }

   public static java.util.stream.Stream<java.nio.file.Path> htmlFilesIn(java.nio.file.Path directory) throws java.io.IOException {
      return listFiles(directory, ".html");
   }

   public static java.util.stream.Stream<java.nio.file.Path> csvFilesIn(java.nio.file.Path directory) throws java.io.IOException {
      return listFiles(directory, ".csv");
   }

   public static java.util.stream.Stream<java.nio.file.Path> stgFilesIn(java.nio.file.Path directory) throws java.io.IOException {
      return listFiles(directory, ".stg");
   }

   public static java.util.stream.Stream<java.nio.file.Path> pathDoesNotExists(java.nio.file.Path directory) {
      log.warning("MISSING path " + directory);
      return java.util.stream.Stream.empty();
   }

   public static String stripHttp(String url) {
      String s = url.replaceFirst("http(s)?://(www\\.)?", "").trim();
      return s.endsWith("/") ? s.substring(0, s.length() - 1) : s;
   }

   public static void writeFile(String content, java.nio.file.Path file) {
      try {
         log.info(String.join(" ", "write file", file.toString()));

         final java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(file.toFile()));
         writer.write(content);
         writer.close();

      } catch (Throwable throwable) {
         EVENTS.exception(throwable);
      }
   }

   public static void writeJavaFile(java.nio.file.Path mainSrc, String packageName, String className, nextgen.lambda.modules.templates.java.compilationUnitBuilder compilationUnit) {
      final String packagePath = String.join(java.io.File.separator, mainSrc.toString(), packageName.replaceAll("\\.", java.io.File.separator));
      nextgen.lambda.IO.writeJavaFile(packagePath, className, nextgen.lambda.modules.java.Java.getJavaGroup().render(compilationUnit));
   }

   public static void writeJavaFile(java.nio.file.Path mainSrc, String packageName, String className, nextgen.lambda.modules.templates.java.classDeclarationBuilder classDeclarationBuilder) {
      writeJavaFile(mainSrc, packageName, className, nextgen.lambda.modules.templates.java.JavaGroup.new_compilationUnitBuilder().set_packageName(packageName).add_members(nextgen.lambda.modules.java.Java.getJavaGroup().render(classDeclarationBuilder)));
   }

   public static void writeJavaFile(String packageName, String className, nextgen.lambda.modules.templates.java.classDeclarationBuilder classDeclarationBuilder) {
      writeJavaFile(nextgen.lambda.IO.mainJava, packageName, className, nextgen.lambda.modules.templates.java.JavaGroup.new_compilationUnitBuilder().set_packageName(packageName).add_members(nextgen.lambda.modules.java.Java.getJavaGroup().render(classDeclarationBuilder)));
   }

   public static void writeJavaFile(String packagePath, String className, String compilationUnit) {
      final String javaFile = String.join(java.io.File.separator, packagePath, String.join(".", className, "java"));
      log.info("writeJavaFile " + javaFile);
      createDirectories(java.nio.file.Path.of(packagePath));
      write(compilationUnit, javaFile);
   }

   public static void write(String content, String javaFile) {
      try {
         java.nio.file.Files.writeString(java.nio.file.Path.of(javaFile), content);
      } catch (java.io.IOException throwable) {
         nextgen.lambda.EVENTS.exception(throwable);
      }
   }

   public static void write(io.vertx.core.json.JsonObject content, String javaFile) {
      try {
         java.nio.file.Files.writeString(java.nio.file.Path.of(javaFile), content.encodePrettily());
      } catch (java.io.IOException throwable) {
         nextgen.lambda.EVENTS.exception(throwable);
      }
   }

   public static void write(io.vertx.core.json.JsonArray content, String javaFile) {
      try {
         java.nio.file.Files.writeString(java.nio.file.Path.of(javaFile), content.encodePrettily());
      } catch (java.io.IOException throwable) {
         nextgen.lambda.EVENTS.exception(throwable);
      }
   }

   public static java.util.stream.Stream<io.vertx.core.json.JsonObject> streamJsonObjects(String name, io.vertx.core.json.JsonObject model) {
      return model.getJsonArray(name, new io.vertx.core.json.JsonArray()).stream().map(o -> (io.vertx.core.json.JsonObject) o);
   }

   public static void createDirectories(java.nio.file.Path path) {
      try {
         java.nio.file.Files.createDirectories(path);
      } catch (java.io.IOException throwable) {
         EVENTS.exception(throwable);
      }
   }

   public static java.util.Optional<String> read(java.nio.file.Path file) {
      try {
         return java.util.Optional.of(java.nio.file.Files.readString(file));
      } catch (java.io.IOException throwable) {
         EVENTS.exception(throwable);
         return java.util.Optional.empty();
      }
   }


}