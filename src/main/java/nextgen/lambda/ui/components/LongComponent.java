package nextgen.lambda.ui.components;

public final class LongComponent extends AbstractComponent<javax.swing.JTextField, Long> {

   LongComponent(String name) {
      super(name, new javax.swing.JTextField(15));
   }

   @Override
   public nextgen.lambda.ui.components.Component<javax.swing.JTextField, Long> value(Long model) {
      component().setText(model.toString());
      return this;
   }

   @Override
   public java.util.Optional<Long> value() {
      try {
         final String value = component().getText().trim();
         return value.length() == 0 ? java.util.Optional.empty() : java.util.Optional.of(Long.parseLong(value));
      } catch (Throwable throwable) {
         return java.util.Optional.empty();
      }

   }
}
