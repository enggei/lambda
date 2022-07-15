package nextgen.lambda.modules;

public abstract class AbstractLambdaModule implements nextgen.lambda.modules.LambdaModule {

   private static final java.util.logging.Logger log = nextgen.lambda.LOG.logger(AbstractLambdaModule.class);

   protected nextgen.lambda.CONTEXT context;
   protected nextgen.lambda.ui.UI ui;

   @Override
   public java.util.Collection<javax.swing.Action> objectActions(Object model) {
      final java.util.ArrayList<javax.swing.Action> actions = new java.util.ArrayList<>();
      getClassMethodsFor(model).forEach(method -> actions.add(nextgen.lambda.ACTIONS.newAction(method.getName(), () -> nextgen.lambda.OBJECTS.invokeClassMethod(ui.editor, method, java.util.Collections.singleton(model)))));
      getObjectMethodsFor(model).forEach(method -> actions.add(nextgen.lambda.ACTIONS.newAction(method.getName(), () -> nextgen.lambda.OBJECTS.invokeObjectMethod(ui.editor, this, method, java.util.Collections.singleton(model)))));
      return actions;
   }

   @Override
   public nextgen.lambda.modules.LambdaModule register(nextgen.lambda.CONTEXT context) {
      log.info("register context " + this);
      this.context = context;
      return this;
   }

   @Override
   public nextgen.lambda.modules.LambdaModule register(nextgen.lambda.ui.UI ui) {
      log.info("register ui " + this);
      this.ui = ui;
      return this;
   }

   @Override
   public java.util.Optional<javax.swing.JComponent> editor(Object model) {

      final nextgen.lambda.ui.forms.Form form = nextgen.lambda.ui.forms.Form.newForm()
         .newLeftPrefNone().newLeftPrefNone().newLeftPrefGrow();

      getClassMethodsFor(model)
         .forEach(method -> form.newRow().center().pref().none()
            .label(method.getReturnType().getSimpleName())
            .button(nextgen.lambda.ACTIONS.newAction(method.getName(), () -> nextgen.lambda.OBJECTS.invokeClassMethod(ui.editor, method, java.util.Collections.singleton(model))))
            .label(java.util.Arrays.stream(method.getParameterTypes()).map(Class::getSimpleName).collect(java.util.stream.Collectors.joining(" "))));

      getObjectMethodsFor(model)
         .forEach(method -> form.newRow().center().pref().none()
            .label(method.getReturnType().getSimpleName())
            .button(nextgen.lambda.ACTIONS.newAction(method.getName(), () -> nextgen.lambda.OBJECTS.invokeObjectMethod(ui.editor, this, method, java.util.Collections.singleton(model))))
            .label(java.util.Arrays.stream(method.getParameterTypes()).map(Class::getSimpleName).collect(java.util.stream.Collectors.joining(" "))));

      final java.util.Set<Class<?>> allTypes = ui.canvas.allTypes();

      nextgen.lambda.OBJECTS.objectMethods(this)
         .filter(method -> method.getParameterCount() != 0)
         .filter(method -> nextgen.lambda.OBJECTS.isCompatible(method, allTypes))
         .forEach(method -> form.newRow().center().pref().none()
            .label(method.getReturnType().getSimpleName())
            .button(nextgen.lambda.ACTIONS.newAction(method.getName(), () -> nextgen.lambda.OBJECTS.invokeObjectMethod(ui.editor, this, method, ui.canvas.allModels())))
            .label(java.util.Arrays.stream(method.getParameterTypes()).map(Class::getSimpleName).collect(java.util.stream.Collectors.joining(" "))));

      nextgen.lambda.OBJECTS.classMethods(this)
         .filter(method -> method.getParameterCount() != 0)
         .filter(method -> nextgen.lambda.OBJECTS.isCompatible(method, allTypes))
         .forEach(method -> form.newRow().center().pref().none()
            .label(method.getReturnType().getSimpleName())
            .button(nextgen.lambda.ACTIONS.newAction(method.getName(), () -> nextgen.lambda.OBJECTS.invokeClassMethod(ui.editor, method, ui.canvas.allModels())))
            .label(java.util.Arrays.stream(method.getParameterTypes()).map(Class::getSimpleName).collect(java.util.stream.Collectors.joining(" "))));

      return (form.rowCount() == 0) ? java.util.Optional.empty() : java.util.Optional.of(form.build());
   }

   protected java.util.stream.Stream<java.lang.reflect.Method> getObjectMethodsFor(Object model) {
      return nextgen.lambda.OBJECTS.objectMethods(this)
         .filter(method -> !isObjectMethod(method))
         .filter(method -> method.getParameterCount() == 1)
         .filter(method -> method.getParameters()[0].getType().isAssignableFrom(model.getClass()));
   }

   private boolean isObjectMethod(java.lang.reflect.Method method) {
      return method.getName().equals("hashCode") || method.getName().equals("toString") || (method.getName().equals("equals") && method.getParameterCount() == 1 && method.getParameterTypes()[0].equals(Object.class));
   }

   protected java.util.stream.Stream<java.lang.reflect.Method> getClassMethodsFor(Object model) {
      return nextgen.lambda.OBJECTS.classMethods(this)
         .filter(method -> method.getParameterCount() == 1)
         .filter(method -> method.getParameters()[0].getType().isAssignableFrom(model.getClass()));
   }

   protected java.util.List<javax.swing.Action> actions(java.util.Set<Object> objects) {

      final java.util.List<javax.swing.Action> actions = new java.util.ArrayList<>();
      final java.util.Set<Class<?>> allTypes = objects.stream().map(Object::getClass).collect(java.util.stream.Collectors.toSet());

      nextgen.lambda.OBJECTS.objectMethods(this)
         .filter(method -> nextgen.lambda.OBJECTS.isCompatible(method, allTypes))
         .map(method -> nextgen.lambda.ACTIONS.newAction(this + " " + method.getName(), () -> nextgen.lambda.OBJECTS.invokeObjectMethod(this, method, new java.util.ArrayList<>(objects))))
         .forEach(actions::add);

      nextgen.lambda.OBJECTS.classMethods(this)
         .filter(method -> nextgen.lambda.OBJECTS.isCompatible(method, allTypes))
         .map(method -> nextgen.lambda.ACTIONS.newAction(this + " " + method.getName(), () -> nextgen.lambda.OBJECTS.invokeClassMethod(method, new java.util.ArrayList<>(objects))))
         .forEach(actions::add);

      return actions;
   }

   @Override
   public int compareTo(nextgen.lambda.modules.LambdaModule other) {
      return toString().compareToIgnoreCase(other.toString());
   }

   @Override
   public String toString() {
      return getClass().getSimpleName();
   }

   protected nextgen.lambda.modules.LambdaModuleTreeNode addToNavigator() {
      final nextgen.lambda.modules.LambdaModuleTreeNode treeNode = new nextgen.lambda.modules.LambdaModuleTreeNode(ui.navigator, this);
      ui.navigator.root().add(treeNode);
      return treeNode;
   }

   public void addClassTreeNode(nextgen.lambda.ui.navigator.NavigatorTreeNode<?> treeNode, Class<?> model) {
      treeNode.add(new nextgen.lambda.ui.navigator.nodes.NavigatorClassTreeNode(ui.navigator, model));
   }

   public void addPackageTreeNodeTreeNode(nextgen.lambda.ui.navigator.NavigatorTreeNode<?> treeNode, String name, java.util.Set<Class<?>> members) {
      nextgen.lambda.ui.navigator.NavigatorTreeNode<String> packageTreeNode = new nextgen.lambda.ui.navigator.NavigatorTreeNode<>(ui.navigator, name, s -> s, java.awt.Color.decode("#fb9a99"));
      members.forEach(aClass-> packageTreeNode.add(new nextgen.lambda.ui.navigator.nodes.NavigatorClassTreeNode(ui.navigator, aClass)));
      treeNode.add(packageTreeNode);
   }

   public void addObjectTreeNode(nextgen.lambda.ui.navigator.NavigatorTreeNode<?> treeNode, Object model) {
      treeNode.add(new nextgen.lambda.ui.navigator.nodes.NavigatorObjectTreeNode<>(ui.navigator, model, Object::toString));
   }

   public void addObjectTreeNode(nextgen.lambda.ui.navigator.NavigatorTreeNode<?> treeNode, Object model, java.util.function.Function<Object, String> renderer) {
      treeNode.add(new nextgen.lambda.ui.navigator.nodes.NavigatorObjectTreeNode<>(ui.navigator, model, renderer));
   }
}