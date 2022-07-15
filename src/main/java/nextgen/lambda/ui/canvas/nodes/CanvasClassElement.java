package nextgen.lambda.ui.canvas.nodes;

import static nextgen.lambda.OBJECTS.*;

public class CanvasClassElement extends nextgen.lambda.ui.canvas.CanvasTextLabelElement<java.lang.Class<?>> {

   public CanvasClassElement(nextgen.lambda.ui.canvas.Canvas canvas, Class<?> model) {
      super(canvas, model, nextgen.lambda.ui.UI.getContrastColor(canvas.getBackground()), Class::getCanonicalName, java.awt.Color.decode("#d8b365"), Class::getSimpleName);
   }

   public CanvasClassElement(nextgen.lambda.ui.canvas.Canvas canvas, Class<?> model, String label) {
      super(canvas, model, nextgen.lambda.ui.UI.getContrastColor(canvas.getBackground()), Class::getCanonicalName, java.awt.Color.decode("#d8b365"), m -> label);
   }

   @Override
   public java.util.Collection<javax.swing.Action> actions() {
      final java.util.Collection<javax.swing.Action> actions = super.actions();

      actions.add(nextgen.lambda.ACTIONS.newAction("Edit", "Space", () -> nextgen.lambda.EVENTS.edit(model(), label())));

      final java.util.Set<Class<?>> canvasTypes = canvas().elements.keySet().stream().map(nextgen.lambda.OBJECTS::classFor).collect(java.util.stream.Collectors.toSet());
      final java.util.Set<Object> objects = new java.util.HashSet<>(canvas().elements.keySet());

      classMethods(model())
         .filter(method -> isCompatible(method, canvasTypes))
         .map(method -> nextgen.lambda.ACTIONS.newAction(method.getName(), () -> invokeClassMethod(canvas(), method, objects)))
         .forEach(actions::add);

      classFields(model())
         .map(field -> nextgen.lambda.ACTIONS.newAction(field.getName(), () -> classFieldValue(field).ifPresent(nextgen.lambda.EVENTS::open)))
         .forEach(actions::add);

      return actions;
   }
}