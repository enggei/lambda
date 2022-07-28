package nextgen.lambda.modules.java;

public class JavaMainSrc {

   private static final java.util.logging.Logger log = nextgen.lambda.LOG.logger(JavaMainSrc.class);

   public static JavaMainSrc lambda = new nextgen.lambda.modules.java.JavaMainSrc(java.nio.file.Path.of("/home/goe/github/lambda/src/main/java"));
   public static JavaMainSrc real_analytics = new nextgen.lambda.modules.java.JavaMainSrc(java.nio.file.Path.of("/home/goe/projects/real_analytics/src/main/java"));

   private java.nio.file.Path path;

   public JavaMainSrc(java.nio.file.Path path) {
      this.path = path;
   }

   public void classes() {
      nextgen.lambda.IO.visitDirectory(path, file -> {
         if (file.toFile().isFile() && file.getFileName().toString().endsWith("java"))
            JavaParser.parse(file)
               .ifPresent(compilationUnitParseResult -> {
                  compilationUnitParseResult.getProblems().forEach(problem -> log.warning(problem.getLocation().map(com.github.javaparser.TokenRange::toString).stream().collect(java.util.stream.Collectors.joining("\n\t"))));
                  compilationUnitParseResult.getResult().ifPresent(compilationUnit -> {
                     log.info(compilationUnit.getPrimaryTypeName().orElse(file + " ?"));
                  });
               });
      });
   }
}