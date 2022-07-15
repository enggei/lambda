package nextgen.lambda.ui.canvas.nodes;

import static nextgen.lambda.OBJECTS.*;

public class CanvasObjectElement<T> extends nextgen.lambda.ui.canvas.CanvasTextLabelElement<T> {

   public CanvasObjectElement(nextgen.lambda.ui.canvas.Canvas canvas, T model, String label) {
      super(canvas, model, nextgen.lambda.ui.UI.getContrastColor(canvas.getBackground()), Object::toString, java.awt.Color.decode("#5ab4ac"), m -> label);
   }

   public CanvasObjectElement(nextgen.lambda.ui.canvas.Canvas canvas, T model, java.util.function.Function<T, String> renderer, java.util.function.Function<T, String> labelRenderer) {
      super(canvas, model, nextgen.lambda.ui.UI.getContrastColor(canvas.getBackground()), renderer, java.awt.Color.decode("#5ab4ac"), labelRenderer);
   }

   public CanvasObjectElement(nextgen.lambda.ui.canvas.Canvas canvas, T model, java.util.function.Function<T, String> renderer, String label) {
      super(canvas, model, nextgen.lambda.ui.UI.getContrastColor(canvas.getBackground()), renderer, java.awt.Color.decode("#5ab4ac"), m -> label);
   }

   @Override
   public java.util.Collection<javax.swing.Action> actions() {
      final java.util.Collection<javax.swing.Action> actions = super.actions();

      nextgen.lambda.OBJECTS.asIterable(model()).ifPresent(iterable -> actions.add(nextgen.lambda.ACTIONS.newAction("Open all", () -> nextgen.lambda.OBJECTS.forEach(iterable, nextgen.lambda.EVENTS::open))));

      actions.add(nextgen.lambda.ACTIONS.newAction("Edit", "Space", () -> nextgen.lambda.EVENTS.edit(model(), label())));

      final java.util.Set<Class<?>> canvasTypes = canvas().elements.keySet().stream().map(nextgen.lambda.OBJECTS::classFor).collect(java.util.stream.Collectors.toSet());
      final java.util.Set<Object> objects = new java.util.HashSet<>(canvas().elements.keySet());

      nextgen.lambda.OBJECTS.objectMethods(model())
         .filter(method -> isCompatible(method, canvasTypes))
         .map(method -> nextgen.lambda.ACTIONS.newAction(method.getName(), () -> invokeObjectMethod(canvas(), model(), method, objects)))
         .forEach(actions::add);

      nextgen.lambda.OBJECTS.objectFields(model())
         .map(field -> nextgen.lambda.ACTIONS.newAction(field.getName(), () -> nextgen.lambda.EVENTS.open(nextgen.lambda.OBJECTS.objectFieldValue(field, model()))))
         .forEach(actions::add);

      return actions;
   }
}