package nextgen.lambda.modules;

public class LambdaModuleTreeNode extends nextgen.lambda.ui.navigator.NavigatorTreeNode<LambdaModule> {

   public LambdaModuleTreeNode(nextgen.lambda.ui.navigator.Navigator navigator, LambdaModule model) {
      super(navigator, model, Object::toString, java.awt.Color.decode("#5ab4ac"));
   }

   @Override
   public java.util.Collection<javax.swing.Action> actions() {

      final java.util.Collection<javax.swing.Action> actions = super.actions();

      actions.add(nextgen.lambda.ACTIONS.newAction("Edit", "Space", () -> nextgen.lambda.EVENTS.edit(model(), model().toString())));

      final java.util.Set<Object> objects = canvas().allModels();
      final java.util.Set<Class<?>> types = canvas().allTypes();

      nextgen.lambda.OBJECTS.objectFields(model())
         .map(field -> nextgen.lambda.ACTIONS.newAction(field.getName(), () -> nextgen.lambda.OBJECTS.objectFieldValue(field, model()).ifPresent(nextgen.lambda.EVENTS::open)))
         .forEach(actions::add);

      nextgen.lambda.OBJECTS.objectMethods(model())
         .filter(method -> nextgen.lambda.OBJECTS.isCompatible(method, types))
         .map(method -> nextgen.lambda.ACTIONS.newAction(method.getName(), () -> nextgen.lambda.OBJECTS.invokeObjectMethod(canvas(), model(), method, objects)))
         .forEach(actions::add);

      nextgen.lambda.OBJECTS.classFields(model())
         .map(field -> nextgen.lambda.ACTIONS.newAction(field.getName(), () -> nextgen.lambda.OBJECTS.classFieldValue(field).ifPresent(nextgen.lambda.EVENTS::open)))
         .forEach(actions::add);

      nextgen.lambda.OBJECTS.classMethods(model())
         .filter(method -> nextgen.lambda.OBJECTS.isCompatible(method, types))
         .map(method -> nextgen.lambda.ACTIONS.newAction(method.getName(), () -> nextgen.lambda.OBJECTS.invokeClassMethod(navigator, method, objects)))
         .forEach(actions::add);

      return actions;
   }
}
