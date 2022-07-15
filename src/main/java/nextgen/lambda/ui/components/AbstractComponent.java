package nextgen.lambda.ui.components;

public abstract class AbstractComponent<C extends javax.swing.JComponent, T> implements Component<C, T> {

   final String name;
   final C component;

   public AbstractComponent(String name, C component) {
      this.name = name;
      this.component = component;
   }

   @Override
   public String name() {
      return name;
   }

   @Override
   public C component() {
      return component;
   }
}