package nextgen.lambda.modules.java;

public class JavaCompiler {

   private static final java.util.logging.Logger log = nextgen.lambda.LOG.logger(JavaCompiler.class);
   private static final net.openhft.compiler.CachedCompiler compiler = new net.openhft.compiler.CachedCompiler(null, null);

   public static java.util.Optional<Class<?>> compileSource(java.nio.file.Path javaFile) {

      final java.util.Optional<com.github.javaparser.ParseResult<com.github.javaparser.ast.CompilationUnit>> result = JavaParser.parse(javaFile);
      if (result.isEmpty()) return java.util.Optional.empty();

      final com.github.javaparser.ParseResult<com.github.javaparser.ast.CompilationUnit> parseResult = result.get();


      final String packageName = parseResult.getResult().flatMap(com.github.javaparser.ast.CompilationUnit::getPackageDeclaration).map(packageDeclaration -> packageDeclaration.getName().asString()).orElse(null);
      final java.util.Optional<com.github.javaparser.ast.body.TypeDeclaration<?>> declaration = parseResult.getResult().flatMap(com.github.javaparser.ast.CompilationUnit::getPrimaryType);
      if (packageName == null || declaration.isEmpty()) return java.util.Optional.empty();
      if (!declaration.get().isClassOrInterfaceDeclaration()) return java.util.Optional.empty();

      return compileSource(packageName, declaration.get().asClassOrInterfaceDeclaration());
   }

   private static java.util.Optional<Class<?>> compileSource(String packageName, com.github.javaparser.ast.body.ClassOrInterfaceDeclaration classOrInterfaceDeclaration) {

      final String className = String.join(".", packageName, classOrInterfaceDeclaration.getNameAsString());
      final java.io.StringWriter errors = new java.io.StringWriter();
      try {
         final Class<?> aClass = compiler.loadFromJava(Thread.currentThread().getContextClassLoader(), className, classOrInterfaceDeclaration.toString());
         return java.util.Optional.of(aClass);
      } catch (Throwable throwable) {
         log.severe(errors.toString());
         nextgen.lambda.EVENTS.open(errors);
         nextgen.lambda.EVENTS.exception(throwable);
         return java.util.Optional.empty();
      }
   }

   public static java.util.Optional<Class<?>> compileSource(String packageName, nextgen.lambda.modules.templates.java.classDeclarationBuilder classDeclarationBuilder) {
      log.info("compiling " + classDeclarationBuilder.getString("name"));

      final String className = classDeclarationBuilder.getString("name");
      final String compileClassName = className + "_" + System.currentTimeMillis();
      final String compileCanonicalName = packageName + "." + compileClassName;

      final String sourceCode = Java.getJavaGroup()
         .render(nextgen.lambda.modules.templates.java.JavaGroup.new_compilationUnitBuilder()
            .set_packageName(packageName)
            .add_members(classDeclarationBuilder.set_name(compileClassName)));

      final String compileCode = sourceCode // rename className to timestamp-className. matches ' className[ |(|<]'
         .replaceAll("\\s" + className + "\\s", " " + compileClassName + " ")
         .replaceAll("\\s" + className + "<", " " + compileClassName + "<")
         .replaceAll("\\s" + className + "[(]", " " + compileClassName + "(")
         .replaceAll("[(]" + className + "[)]", "(" + compileClassName + ")");

      final java.io.StringWriter errors = new java.io.StringWriter();
      try {
         final Class<?> aClass = compiler.loadFromJava(Thread.currentThread().getContextClassLoader(), compileCanonicalName, compileCode, new java.io.PrintWriter(errors));
         classDeclarationBuilder.set_name(className);
         return java.util.Optional.of(aClass);
      } catch (Throwable throwable) {
         classDeclarationBuilder.set_name(className);
         log.severe(errors.toString());
         nextgen.lambda.EVENTS.open(errors);
         nextgen.lambda.EVENTS.open(classDeclarationBuilder);
         nextgen.lambda.EVENTS.open(compileCode);
         nextgen.lambda.EVENTS.exception(throwable);
         return java.util.Optional.empty();
      }
   }
}