package nextgen.lambda.ui.components;

public final class FloatComponent extends AbstractComponent<javax.swing.JTextField, Float> {

   FloatComponent(String name) {
      super(name, new javax.swing.JTextField());
   }

   @Override
   public nextgen.lambda.ui.components.Component<javax.swing.JTextField, Float> value(Float model) {
      component().setText(model.toString());
      return this;
   }

   @Override
   public java.util.Optional<Float> value() {
      try {
         final String value = component().getText().trim();
         return value.length() == 0 ? java.util.Optional.empty() : java.util.Optional.of(Float.parseFloat(value));
      } catch (Throwable throwable) {
         return java.util.Optional.empty();
      }
   }
}