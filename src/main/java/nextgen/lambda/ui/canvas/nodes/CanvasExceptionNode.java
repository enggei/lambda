package nextgen.lambda.ui.canvas.nodes;

public class CanvasExceptionNode extends CanvasObjectElement<Throwable> {

   public CanvasExceptionNode(nextgen.lambda.ui.canvas.Canvas canvas, Throwable throwable) {
      super(canvas, throwable, m -> fullStack(m), m -> String.join(" ", "Exception", m.toString()));
   }

   private static String fullStack(Throwable throwable) {

      final StringBuilder stack = new StringBuilder();
      addToStack(throwable, stack);

      Throwable throwableCause = throwable.getCause();
      while (throwableCause != null) {
         addToStack(throwable, stack);
         throwableCause = throwableCause.getCause();
      }
      return stack.toString().trim();
   }

   private static void addToStack(Throwable throwable, StringBuilder stack) {
      for (StackTraceElement stackTraceElement : throwable.getStackTrace())
         stack
            .append(String.join(" ",
               stackTraceElement.getClassName(),
               stackTraceElement.getMethodName(),
               Integer.toString(stackTraceElement.getLineNumber())))
            .append("\n");
   }
}