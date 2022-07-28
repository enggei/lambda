package nextgen.lambda;

public class LOG {

   static {
      try {
         System.out.println("Logging configuration");

//         java.io.PrintStream errStream = System.err;
//         System.setErr(new java.io.PrintStream("error.log"));

         java.util.logging.LogManager.getLogManager().readConfiguration(LOG.class.getClassLoader().getResourceAsStream("logging.properties"));
      } catch (java.io.IOException e) {
         System.err.println("Logging configuration error " + e.getMessage());
         e.printStackTrace();
      }
   }

   public static java.util.logging.Logger logger(Class<?> owner) {
      final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(owner.getSimpleName());
      //logger.setUseParentHandlers(false);
      return logger;
   }

   public static void onException(java.util.logging.Logger log, Throwable throwable) {

      log.severe(throwable.getMessage());
      logStackTrace(log, throwable);

      Throwable throwableCause = throwable.getCause();
      while (throwableCause != null) {
         log.severe(throwableCause.getMessage());
         logStackTrace(log, throwableCause);
         throwableCause = throwableCause.getCause();
      }
   }

   public static void logStackTrace(java.util.logging.Logger log, Throwable throwable) {
      for (StackTraceElement stackTraceElement : throwable.getStackTrace()) log.severe(stackTraceElement.toString());
   }
}