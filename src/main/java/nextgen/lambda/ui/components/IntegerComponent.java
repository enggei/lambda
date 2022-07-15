package nextgen.lambda.ui.components;

public final class IntegerComponent extends AbstractComponent<javax.swing.JTextField, Integer> {

   public IntegerComponent(String name) {
      super(name, new javax.swing.JTextField(10));
   }

   @Override
   public nextgen.lambda.ui.components.Component<javax.swing.JTextField, Integer> value(Integer model) {
      component().setText(model.toString());
      return this;
   }

   @Override
   public java.util.Optional<Integer> value() {
      try {
         final String value = component().getText().trim();
         return value.length() == 0 ? java.util.Optional.empty() : java.util.Optional.of(Integer.parseInt(value));
      } catch (Throwable throwable) {
         return java.util.Optional.empty();
      }

   }
}
