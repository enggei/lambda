package nextgen.lambda.ui.canvas;

public class CanvasEventsHandler<T extends nextgen.lambda.ui.canvas.nodes.CanvasObjectElement<?>> {

   private final nextgen.lambda.ui.canvas.Canvas canvas;

   public CanvasEventsHandler(nextgen.lambda.ui.canvas.Canvas canvas) {
      this.canvas = canvas;
   }

   @org.greenrobot.eventbus.Subscribe
   public void onEvent(nextgen.lambda.EVENTS.Event event) {
      switch (event.type) {
         case OPEN -> {
            if (event.model instanceof Class<?>)
               canvas.mergeAndCenter(event.model, () -> new nextgen.lambda.ui.canvas.nodes.CanvasClassElement(canvas, (Class<?>) event.model, event.label()));
            else
               canvas.mergeAndCenter(event.model, () -> new nextgen.lambda.ui.canvas.nodes.CanvasObjectElement<>(canvas, event.model, event.label()));
         }
         case EXCEPTION -> canvas.mergeAndCenter(event.model, () -> new nextgen.lambda.ui.canvas.nodes.CanvasThrowableElement(canvas, (Throwable) event.model));
         case FUTURE -> canvas.merge(event.model, () -> new nextgen.lambda.ui.canvas.nodes.CanvasFutureElement(canvas, (java.util.concurrent.Future<?>) event.model));
         case CLOSE -> canvas.closeModels(java.util.Collections.singleton(event.model));
      }
   }
}