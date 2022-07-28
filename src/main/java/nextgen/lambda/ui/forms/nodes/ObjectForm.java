package nextgen.lambda.ui.forms.nodes;

public class ObjectForm extends nextgen.lambda.ui.forms.Form {

   private final Object model;

   public ObjectForm(javax.swing.JComponent owner, Object model, nextgen.lambda.ui.UI ui) {
      super(owner);
      this.model = model;

      newLeftPrefNone()
         .newLeftPrefNone()
         .newLeftPrefGrow();

      nextgen.lambda.OBJECTS.objectFields(model)
         .forEach(field -> {
            final nextgen.lambda.ui.forms.FormRow fieldRow = newCenterPrefNone();
            fieldRow.label(field.getName());
            fieldRow.button(nextgen.lambda.ACTIONS.newAction("Open", () -> nextgen.lambda.EVENTS.open(nextgen.lambda.OBJECTS.objectFieldValue(field, model))));
            fieldRow.button(nextgen.lambda.ACTIONS.newAction("Set", () -> nextgen.lambda.OBJECTS.setObjectField(owner, model, field, ui.canvas.allModels())));
         });

      nextgen.lambda.OBJECTS.objectMethods(model)
         .forEach(method -> newRow().center().pref().none()
            .label(method.getReturnType().getSimpleName())
            .button(nextgen.lambda.ACTIONS.newAction(method.getName(), () -> nextgen.lambda.OBJECTS.invokeObjectMethod(owner, model, method, ui.canvas.allModels())))
            .label(java.util.Arrays.stream(method.getParameterTypes()).map(Class::getSimpleName).collect(java.util.stream.Collectors.joining(" "))));

      newCenterPrefNone().label("debug").button(nextgen.lambda.ACTIONS.newAction("Debug", ()-> nextgen.lambda.EVENTS.open(nextgen.lambda.OBJECTS.debug(model))));
   }
}