package nextgen.lambda.ui.components;

public final class ShortComponent extends AbstractComponent<javax.swing.JTextField, Short> {

   ShortComponent(String name) {
      super(name, new javax.swing.JTextField(5));
   }

   @Override
   public nextgen.lambda.ui.components.Component<javax.swing.JTextField, Short> value(Short model) {
      component().setText(model.toString());
      return this;
   }

   @Override
   public java.util.Optional<Short> value() {
      try {
         final String value = component().getText().trim();
         return value.length() == 0 ? java.util.Optional.empty() : java.util.Optional.of(Short.parseShort(value));
      } catch (Throwable throwable) {
         return java.util.Optional.empty();
      }

   }
}
