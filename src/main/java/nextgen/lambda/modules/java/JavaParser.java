package nextgen.lambda.modules.java;

public class JavaParser {

   private static final java.util.logging.Logger log = nextgen.lambda.LOG.logger(JavaParser.class);
   private static final com.github.javaparser.JavaParser parser = new com.github.javaparser.JavaParser();

   public static void parseAndOpen(java.nio.file.Path model) {
      parse(model).ifPresent(nextgen.lambda.modules.java.JavaParser::open);
   }

   public static java.util.Optional<com.github.javaparser.ParseResult<com.github.javaparser.ast.CompilationUnit>> parse(java.nio.file.Path model) {
      log.info("parse " + model);
      try {
         return java.util.Optional.of(parser.parse(model));
      } catch (java.io.IOException e) {
         nextgen.lambda.EVENTS.exception(e);
         return java.util.Optional.empty();
      }
   }

   public static void parse(String model) {
      log.info("parse " + model);
      open(parser.parse(model));
   }

   private static void open(com.github.javaparser.ParseResult<com.github.javaparser.ast.CompilationUnit> result) {
      result.getResult().ifPresent(nextgen.lambda.EVENTS::open);
      final java.util.List<com.github.javaparser.Problem> problems = result.getProblems();
      if (!problems.isEmpty()) nextgen.lambda.EVENTS.open(problems);
   }
}