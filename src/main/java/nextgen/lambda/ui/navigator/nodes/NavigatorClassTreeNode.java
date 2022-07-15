package nextgen.lambda.ui.navigator.nodes;

public class NavigatorClassTreeNode extends nextgen.lambda.ui.navigator.NavigatorTreeNode<java.lang.Class<?>> {

   public NavigatorClassTreeNode(nextgen.lambda.ui.navigator.Navigator navigator, Class<?> model) {
      super(navigator, model, Class::getSimpleName, java.awt.Color.decode("#d8b365"));
   }

   @Override
   public java.util.Collection<javax.swing.Action> actions() {
      final java.util.Collection<javax.swing.Action> actions = super.actions();
      actions.add(nextgen.lambda.ACTIONS.newAction("Open", "control Space", () -> nextgen.lambda.EVENTS.open(model())));
      actions.add(nextgen.lambda.ACTIONS.newAction("Edit", "Space", () -> nextgen.lambda.EVENTS.edit(model(), toString())));

      final java.util.HashSet<Object> canvasObjects = new java.util.LinkedHashSet<>();
      final java.util.HashSet<Class<?>> canvasTypes = new java.util.LinkedHashSet<>();
      canvas().elements().forEach(canvasElement -> {
         canvasObjects.add(canvasElement.model());
         canvasTypes.add(canvasElement.model().getClass());
      });

      nextgen.lambda.OBJECTS.classConstructors(model()).filter(constructor -> nextgen.lambda.OBJECTS.isCompatible(constructor, canvasTypes)).forEach(constructor -> actions.add(new nextgen.lambda.ACTIONS.UIAction("Construct", "control N", () -> nextgen.lambda.OBJECTS.invokeConstructor(navigator, constructor, canvasObjects))));
      nextgen.lambda.OBJECTS.classFields(model()).map(field -> nextgen.lambda.ACTIONS.newAction(field.getName(), () -> nextgen.lambda.OBJECTS.classFieldValue(field).ifPresent(nextgen.lambda.EVENTS::open))).forEach(actions::add);
      nextgen.lambda.OBJECTS.classMethods(model()).filter(method -> nextgen.lambda.OBJECTS.isCompatible(method, canvasTypes)).map(method -> nextgen.lambda.ACTIONS.newAction(method.getName(), () -> nextgen.lambda.OBJECTS.invokeClassMethod(navigator, method, canvasObjects))).forEach(actions::add);
      if (nextgen.lambda.OBJECTS.isExtendable(model())) actions.add(new nextgen.lambda.ACTIONS.UIAction("Extend", "control X", () -> nextgen.lambda.OBJECTS.newClassExtending(navigator, model()).ifPresent(nextgen.lambda.EVENTS::open)));
      return actions;
   }
}