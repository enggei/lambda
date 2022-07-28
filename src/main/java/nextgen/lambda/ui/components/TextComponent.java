package nextgen.lambda.ui.components;

public class TextComponent extends AbstractComponent<javax.swing.JTextArea, String> {

   public TextComponent(String name) {
      super(name, new javax.swing.JTextArea(10, 30));
      component().setFont(new java.awt.Font(java.awt.Font.MONOSPACED, java.awt.Font.PLAIN, 12));
   }

   @Override
   public nextgen.lambda.ui.components.Component<javax.swing.JTextArea, String> value(String model) {
      component().setText(model);
      return this;
   }

   @Override
   public java.util.Optional<String> value() {
      final String value = component().getText().trim();
      return java.util.Optional.ofNullable(value.length() == 0 ? null : value);
   }
}