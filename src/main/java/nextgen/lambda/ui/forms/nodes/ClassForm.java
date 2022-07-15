package nextgen.lambda.ui.forms.nodes;

public class ClassForm extends nextgen.lambda.ui.forms.Form {

   private final javax.swing.JComponent owner;
   private final Object model;

   public ClassForm(javax.swing.JComponent owner, Object model, nextgen.lambda.ui.UI ui) {
      this.owner = owner;
      this.model = model;

      newLeftPrefNone().newLeftPrefNone().newLeftPrefGrow();

      nextgen.lambda.OBJECTS.classFields(model)
         .forEach(field -> newRow().top().pref().none()
            .label(field.getName())
            .button(nextgen.lambda.ACTIONS.newAction("Open", () -> nextgen.lambda.EVENTS.open(nextgen.lambda.OBJECTS.objectFieldValue(field, model)))));

      nextgen.lambda.OBJECTS.classMethods(model)
         .forEach(method -> newRow().center().pref().none()
            .label(method.getReturnType().getSimpleName())
            .button(nextgen.lambda.ACTIONS.newAction(method.getName(), () -> nextgen.lambda.OBJECTS.invokeClassMethod(owner, method, ui.canvas.allModels())))
            .label(java.util.Arrays.stream(method.getParameterTypes()).map(Class::getSimpleName).collect(java.util.stream.Collectors.joining(" "))));
   }
}
