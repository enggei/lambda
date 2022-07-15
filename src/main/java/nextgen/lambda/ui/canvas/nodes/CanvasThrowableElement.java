package nextgen.lambda.ui.canvas.nodes;

public class CanvasThrowableElement extends CanvasObjectElement<Throwable> {

   public CanvasThrowableElement(nextgen.lambda.ui.canvas.Canvas canvas, Throwable throwable) {
      super(canvas, throwable, m -> java.util.Arrays.stream(m.getStackTrace()).map(stackTraceElement -> String.join(" ", stackTraceElement.getClassName(), stackTraceElement.getMethodName(), Integer.toString(stackTraceElement.getLineNumber()))).collect(java.util.stream.Collectors.joining("\n")), Throwable::getMessage);
   }
}