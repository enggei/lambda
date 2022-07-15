package nextgen.lambda.ui.forms;

public class FormRow {
   private final nextgen.lambda.ui.forms.Form builder;
   private String alignment = "FILL";
   private String size = "PREF";
   private String behaviour = "NONE";
   private final nextgen.lambda.ui.forms.Form form;

   private final RowSpan defaultSpans = new RowSpan();
   private final RowAlignment defaultAlignments = new RowAlignment();

   final java.util.Map<Integer, javax.swing.JComponent> columns = new java.util.TreeMap<>();
   private final java.util.Map<Integer, RowSpan> spans = new java.util.TreeMap<>();
   private final java.util.Map<Integer, RowAlignment> alignments = new java.util.TreeMap<>();
   private final java.util.Map<String, nextgen.lambda.ui.components.Component<?, ?>> components = new java.util.LinkedHashMap<>();

   public FormRow(nextgen.lambda.ui.forms.Form form, nextgen.lambda.ui.forms.Form builder) {
      this.form = form;
      this.builder = builder;
   }

   public String spec() {
      return alignment + ":" + size + ":" + behaviour;
   }

   public nextgen.lambda.ui.forms.FormRow top() {
      this.alignment = "FILL";
      return this;
   }

   public nextgen.lambda.ui.forms.FormRow center() {
      this.alignment = "CENTER";
      return this;
   }

   public nextgen.lambda.ui.forms.FormRow bottom() {
      this.alignment = "BOTTOM";
      return this;
   }

   public nextgen.lambda.ui.forms.FormRow fill() {
      this.alignment = "FILL";
      return this;
   }

   public nextgen.lambda.ui.forms.FormRow pref() {
      this.size = "PREF";
      return this;
   }

   public nextgen.lambda.ui.forms.FormRow none() {
      this.behaviour = "NONE";
      return this;
   }

   public nextgen.lambda.ui.forms.FormRow grow() {
      this.behaviour = "GROW";
      return this;
   }

   public nextgen.lambda.ui.forms.FormRow add(javax.swing.JComponent component) {
      return add(component, 1, 1);
   }

   public nextgen.lambda.ui.forms.FormRow add(javax.swing.JComponent component, int colSpan, int rowSpan) {
      return add(component, colSpan, rowSpan, "FILL", "FILL");
   }

   public nextgen.lambda.ui.forms.FormRow add(javax.swing.JComponent component, int colSpan, int rowSpan, String colAlign, String rowAlign) {
      return add(columns.size(), component, colSpan, rowSpan, colAlign, rowAlign);
   }

   public nextgen.lambda.ui.forms.FormRow add(int col, javax.swing.JComponent component, int colSpan, int rowSpan, String colAlign, String rowAlign) {
      columns.put(col, component);
      spans.put(col, new RowSpan(colSpan, rowSpan));
      alignments.put(col, new RowAlignment(colAlign, rowAlign));
      return this;
   }

   public nextgen.lambda.ui.forms.FormRow add(int col, javax.swing.JComponent component, int colSpan, int rowSpan) {
      columns.put(col, component);
      spans.put(col, new RowSpan(colSpan, rowSpan));
      return this;
   }

   public nextgen.lambda.ui.forms.FormRow add(int col, javax.swing.JComponent component) {
      columns.put(col, component);
      return this;
   }

   public nextgen.lambda.ui.forms.FormRow addScrollable(javax.swing.JComponent component) {
      return addScrollable(component, 1, 1);
   }

   public nextgen.lambda.ui.forms.FormRow addScrollable(javax.swing.JComponent component, int colSpan, int rowSpan) {
      final javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(component);
      final java.awt.Dimension preferredSize = new java.awt.Dimension(800, 600);
      scrollPane.setMaximumSize(new java.awt.Dimension(800, 600));
      scrollPane.setMinimumSize(preferredSize);
      scrollPane.setPreferredSize(preferredSize);
      scrollPane.setSize(preferredSize);
      return add(scrollPane, colSpan, rowSpan, "FILL", "FILL");
   }

   public nextgen.lambda.ui.forms.FormRow newRow() {
      return this.builder.newRow();
   }

   public RowSpan spans(int column) {
      return this.spans.getOrDefault(column, defaultSpans);
   }
   public RowAlignment alignments(Integer column) {
      return this.alignments.getOrDefault(column, defaultAlignments);
   }

   public nextgen.lambda.ui.forms.Form builder() {
      return this.builder;
   }

   @SuppressWarnings("unchecked")
   public <T extends javax.swing.JComponent> T get(int column) {
      return (T) columns.get(column);
   }

   public nextgen.lambda.ui.forms.FormRow label(String label) {
      return add(new javax.swing.JLabel(label), 1, 1, "FILL", "FILL");
   }

   public nextgen.lambda.ui.forms.FormRow button(String name, javax.swing.Action action) {
      final nextgen.lambda.ui.components.ButtonComponent component = addButtonComponent(name, action);
      return add(columns.size(), component.component());
   }

   public nextgen.lambda.ui.forms.FormRow comboBox(String name, java.util.Collection<?> choices) {
      final nextgen.lambda.ui.components.ComboBoxComponent component = addComboBoxComponent(name, choices);
      return add(columns.size(), component.component());
   }

   public nextgen.lambda.ui.forms.FormRow textField(String name) {
      final nextgen.lambda.ui.components.StringComponent component = addStringComponent(name);
      return add(columns.size(), component.component());
   }

   public nextgen.lambda.ui.forms.FormRow textField(String name, String value) {
      final nextgen.lambda.ui.components.StringComponent component = addStringComponent(name);
      component.value(value);
      return add(columns.size(), component.component());
   }

   public nextgen.lambda.ui.forms.FormRow textArea(String name) {
      final nextgen.lambda.ui.components.TextComponent component = addTextComponent(name);
      return add(columns.size(), component.component());
   }

   public nextgen.lambda.ui.forms.FormRow textArea(String name, String value) {
      final nextgen.lambda.ui.components.TextComponent component = addTextComponent(name);
      component.value(value);
      return add(columns.size(), component.component());
   }

   public nextgen.lambda.ui.forms.FormRow intField(String name) {
      final nextgen.lambda.ui.components.IntegerComponent component = addIntegerComponent(name);
      return add(columns.size(), component.component());
   }

   public nextgen.lambda.ui.forms.FormRow intField(String name, int value) {
      final nextgen.lambda.ui.components.IntegerComponent component = addIntegerComponent(name);
      component.value(value);
      return add(columns.size(), component.component());
   }

   public <C extends javax.swing.JComponent, T> nextgen.lambda.ui.components.Component<C, T> add(nextgen.lambda.ui.components.Component<C, T> component) {
      this.components.put(component.name(), component);
      add(columns.size(), component.component());
      return component;
   }

   private nextgen.lambda.ui.components.ComboBoxComponent addComboBoxComponent(String name, java.util.Collection<?> choices) {
      return (nextgen.lambda.ui.components.ComboBoxComponent) add(new nextgen.lambda.ui.components.ComboBoxComponent(name, choices));
   }

   private nextgen.lambda.ui.components.ButtonComponent addButtonComponent(String name, javax.swing.Action action) {
      return (nextgen.lambda.ui.components.ButtonComponent) add(new nextgen.lambda.ui.components.ButtonComponent(name, action));
   }

   private nextgen.lambda.ui.components.StringComponent addStringComponent(String name) {
      return (nextgen.lambda.ui.components.StringComponent) add(new nextgen.lambda.ui.components.StringComponent(name));
   }

   private nextgen.lambda.ui.components.TextComponent addTextComponent(String name) {
      return (nextgen.lambda.ui.components.TextComponent) add(new nextgen.lambda.ui.components.TextComponent(name));
   }

   private nextgen.lambda.ui.components.IntegerComponent addIntegerComponent(String name) {
      return (nextgen.lambda.ui.components.IntegerComponent) add(new nextgen.lambda.ui.components.IntegerComponent(name));
   }

   public nextgen.lambda.ui.forms.FormRow button(javax.swing.Action action) {
      return add(new javax.swing.JButton(action), 1, 1, "FILL", "FILL");
   }

   public nextgen.lambda.ui.forms.FormRow toggleButton(String buttonGroup, String name, Runnable action) {
      final javax.swing.JToggleButton button = new javax.swing.JToggleButton(nextgen.lambda.ACTIONS.newAction(name, action));
      form.buttonGroups.putIfAbsent(buttonGroup, new javax.swing.ButtonGroup());
      form.buttonGroups.get(buttonGroup).add(button);
      return add(button, 1, 1, "FILL", "FILL");
   }

   public java.util.Optional<nextgen.lambda.ui.components.ComboBoxComponent> getComboBox(String name) {
      try {
         return java.util.Optional.ofNullable((nextgen.lambda.ui.components.ComboBoxComponent) components.get(name));
      } catch (Throwable throwable) {
         return java.util.Optional.empty();
      }
   }

   public java.util.Optional<nextgen.lambda.ui.components.ButtonComponent> getButton(String name) {
      try {
         return java.util.Optional.ofNullable((nextgen.lambda.ui.components.ButtonComponent) components.get(name));
      } catch (Throwable throwable) {
         return java.util.Optional.empty();
      }
   }


   public java.util.Optional<nextgen.lambda.ui.components.StringComponent> getTextField(String name) {
      try {
         return java.util.Optional.ofNullable((nextgen.lambda.ui.components.StringComponent) components.get(name));
      } catch (Throwable throwable) {
         return java.util.Optional.empty();
      }
   }


   public java.util.Optional<nextgen.lambda.ui.components.TextComponent> getTextArea(String name) {
      try {
         return java.util.Optional.ofNullable((nextgen.lambda.ui.components.TextComponent) components.get(name));
      } catch (Throwable throwable) {
         return java.util.Optional.empty();
      }
   }


   public java.util.Optional<nextgen.lambda.ui.components.IntegerComponent> getIntField(String name) {
      try {
         return java.util.Optional.ofNullable((nextgen.lambda.ui.components.IntegerComponent) components.get(name));
      } catch (Throwable throwable) {
         return java.util.Optional.empty();
      }
   }
}