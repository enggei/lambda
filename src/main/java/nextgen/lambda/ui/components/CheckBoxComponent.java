package nextgen.lambda.ui.components;

public class CheckBoxComponent extends AbstractComponent<javax.swing.JCheckBox, Boolean> {

   public CheckBoxComponent(String name, boolean selected) {
      super(name, new javax.swing.JCheckBox(name, selected));
   }

   public CheckBoxComponent(String name, String text, boolean selected) {
      super(name, new javax.swing.JCheckBox(text, selected));
   }

   @Override
   public nextgen.lambda.ui.components.Component<javax.swing.JCheckBox, Boolean> value(Boolean model) {
      component().setSelected(model);
      return this;
   }

   @Override
   public java.util.Optional<Boolean> value() {
      return java.util.Optional.of(component().isSelected());
   }
}