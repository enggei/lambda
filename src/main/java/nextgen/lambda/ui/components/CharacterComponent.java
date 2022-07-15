package nextgen.lambda.ui.components;

public final class CharacterComponent extends AbstractComponent<javax.swing.JTextField, Character> {

   CharacterComponent(String name) {
      super(name, new javax.swing.JTextField());
   }

   @Override
   public nextgen.lambda.ui.components.Component<javax.swing.JTextField, Character> value(Character model) {
      component().setText(model.toString());
      return this;
   }

   @Override
   public java.util.Optional<Character> value() {
      final String value = component().getText().trim();
      return java.util.Optional.ofNullable(value.length() == 0 ? null : value.charAt(0));
   }
}
