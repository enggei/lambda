package nextgen.lambda.ui.canvas.nodes;

public class CanvasFutureElement extends CanvasObjectElement<java.util.concurrent.Future<?>> {

   final long startTime = System.currentTimeMillis();

   public CanvasFutureElement(nextgen.lambda.ui.canvas.Canvas canvas, java.util.concurrent.Future<?> model) {
      super(canvas, model, Object::toString, "Future");
      javax.swing.SwingUtilities.invokeLater(() -> {
         while (!model.isDone()) {
            try {
               Thread.sleep(500);
               refreshLater();
            } catch (InterruptedException e) {
               nextgen.lambda.EVENTS.exception(e);
            }
         }

         canvas.closeModels(java.util.Collections.singleton(model));
      });
   }

   @Override
   public java.util.function.Function<java.util.concurrent.Future<?>, String> modelRenderer() {
      return f -> String.join(" ", f.toString(), nextgen.lambda.FORMATS.formatTime(System.currentTimeMillis() - startTime));
   }

   @Override
   public java.util.Collection<javax.swing.Action> actions() {
      final java.util.Collection<javax.swing.Action> actions = super.actions();
      actions.add(nextgen.lambda.ACTIONS.newAction("Cancel", "control C", () -> model().cancel(true)));
      return actions;
   }
}