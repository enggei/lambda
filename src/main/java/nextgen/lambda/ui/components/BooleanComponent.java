package nextgen.lambda.ui.components;

public final class BooleanComponent extends AbstractComponent<javax.swing.JCheckBox, Boolean> {

   BooleanComponent(String name) {
      super(name, new javax.swing.JCheckBox());
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