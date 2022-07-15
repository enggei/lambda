package nextgen.lambda.ui.navigator.nodes;

public class NavigatorObjectTreeNode<T> extends nextgen.lambda.ui.navigator.NavigatorTreeNode<T> {

   public NavigatorObjectTreeNode(nextgen.lambda.ui.navigator.Navigator navigator, T model, java.util.function.Function<T, String> renderer) {
      super(navigator, model, renderer, java.awt.Color.decode("#5ab4ac"));
   }

   @Override
   public java.util.Collection<javax.swing.Action> actions() {
      final java.util.Collection<javax.swing.Action> actions = super.actions();
      actions.add(nextgen.lambda.ACTIONS.newAction("Open", "Space", () -> nextgen.lambda.EVENTS.open(model())));
      actions.add(nextgen.lambda.ACTIONS.newAction("Edit", "control Space", () -> nextgen.lambda.EVENTS.edit(model(), toString())));

      final java.util.HashSet<Object> canvasObjects = new java.util.LinkedHashSet<>();
      final java.util.HashSet<Class<?>> canvasTypes = new java.util.LinkedHashSet<>();
      canvas().elements().forEach(canvasElement -> {
         canvasObjects.add(canvasElement.model());
         canvasTypes.add(canvasElement.model().getClass());
      });

      nextgen.lambda.OBJECTS.objectFields(model())
         .map(field -> nextgen.lambda.ACTIONS.newAction(field.getName(), () -> nextgen.lambda.OBJECTS.objectFieldValue(field, model()).ifPresent(nextgen.lambda.EVENTS::open)))
         .forEach(actions::add);

      nextgen.lambda.OBJECTS.objectMethods(model())
         .filter(method -> nextgen.lambda.OBJECTS.isCompatible(method, canvasTypes))
         .map(method -> nextgen.lambda.ACTIONS.newAction(method.getName(), () -> nextgen.lambda.OBJECTS.invokeObjectMethod(canvas(), model(), method, canvasObjects)))
         .forEach(actions::add);

      return actions;
   }
}