package nextgen.lambda.ui.components;

public final class StringComponent extends AbstractComponent<javax.swing.JTextField, String> {

   public StringComponent(String name) {
      super(name, new javax.swing.JTextField(15));
   }

   @Override
   public nextgen.lambda.ui.components.Component<javax.swing.JTextField, String> value(String model) {
      component().setText(model);
      return this;
   }

   @Override
   public java.util.Optional<String> value() {
      final String value = component().getText().trim();
      return java.util.Optional.ofNullable(value.length() == 0 ? null : value);
   }
}