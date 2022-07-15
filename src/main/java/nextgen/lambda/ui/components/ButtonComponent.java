package nextgen.lambda.ui.components;

public class ButtonComponent extends AbstractComponent<javax.swing.JButton, Object> {

   private Object value;

   public ButtonComponent(String name, javax.swing.Action action) {
      super(name, new javax.swing.JButton(action));
   }

   @Override
   public nextgen.lambda.ui.components.Component<javax.swing.JButton, Object> value(Object model) {
      this.value = model;
      return this;
   }

   @Override
   public java.util.Optional<Object> value() {
      return java.util.Optional.ofNullable(value);
   }
}