package nextgen.lambda;

public class CONTEXT {

   private static final java.util.logging.Logger log = nextgen.lambda.LOG.logger(CONTEXT.class);

   private final java.util.Set<nextgen.lambda.modules.LambdaModule> modules = new java.util.TreeSet<>();
   private final org.neo4j.graphdb.GraphDatabaseService db;

   public CONTEXT(org.neo4j.graphdb.GraphDatabaseService db) {
      this.db = db;

      final String modulePackage = "nextgen.lambda.modules";
      nextgen.lambda.IO
         .directoriesIn(nextgen.lambda.IO.mainJava, modulePackage)
         .map(module -> module.toFile().getName())
         .sorted()
         .forEach(name -> {
            final String className = String.join(".", modulePackage, name, (Character.toUpperCase(name.charAt(0)) + name.substring(1)));
            nextgen.lambda.OBJECTS
               .asClass(className)
               .flatMap(nextgen.lambda.OBJECTS::newInstance)
               .map(object -> (nextgen.lambda.modules.LambdaModule) object)
               .ifPresent(module -> this.modules.add(module.register(this)));
         });
   }

   public org.neo4j.graphdb.GraphDatabaseService db() {
      return db;
   }

   public java.util.stream.Stream<nextgen.lambda.modules.LambdaModule> modules() {
      return modules.stream();
   }
}