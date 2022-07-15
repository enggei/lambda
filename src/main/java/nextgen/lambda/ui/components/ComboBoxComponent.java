package nextgen.lambda.ui.components;

public class ComboBoxComponent extends AbstractComponent<javax.swing.JComboBox<Object>, Object> {

   public ComboBoxComponent(String name, java.util.Collection<?> choices) {
      super(name, new javax.swing.JComboBox<>(new javax.swing.DefaultComboBoxModel<>(new java.util.Vector<>(choices))));
   }

   @Override
   public nextgen.lambda.ui.components.Component<javax.swing.JComboBox<Object>, Object> value(Object model) {
      component().setSelectedItem(model);
      return this;
   }

   @Override
   public java.util.Optional<Object> value() {
      return java.util.Optional.ofNullable(component().getSelectedItem());
   }
}