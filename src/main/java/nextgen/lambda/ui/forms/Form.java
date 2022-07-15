package nextgen.lambda.ui.forms;

import static nextgen.lambda.ui.UI.*;

public class Form {

   private static final java.util.logging.Logger log = nextgen.lambda.LOG.logger(Form.class);

   private final java.util.Map<Integer, FormColumn> cols = new java.util.TreeMap<>();
   private final java.util.Map<Integer, FormRow> rows = new java.util.TreeMap<>();
   private final javax.swing.border.Border border = javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5);
   final java.util.Map<String, javax.swing.ButtonGroup> buttonGroups = new java.util.LinkedHashMap<>();


   protected javax.swing.JComponent owner;
   protected javax.swing.JPanel panel;

   public static nextgen.lambda.ui.forms.Form newForm() {
      return new nextgen.lambda.ui.forms.Form();
   }

   public static nextgen.lambda.ui.forms.Form newForm(javax.swing.JComponent owner) {
      return new nextgen.lambda.ui.forms.Form(owner);
   }

   protected Form() {
   }

   protected Form(javax.swing.JComponent owner) {
      this.owner = owner;
   }

   public int rowCount() {
      return rows.size();
   }

   public void show(javax.swing.JComponent owner, String message, java.util.function.Consumer<Form> onConfirm) {
      final javax.swing.JPanel build = build();
      build.setBorder(border);
      nextgen.lambda.ui.UI.show(owner, message, build).ifPresent(confirm -> onConfirm.accept(this));
   }

   public <T> void edit(Form owner, String message, java.util.function.Function<Form, java.util.Optional<T>> formPredicate, java.util.function.Consumer<T> onConfirm) {
      edit(owner.panel, message, formPredicate, onConfirm);
   }

   public <T> void edit(String message, java.util.function.Function<Form, java.util.Optional<T>> formPredicate, java.util.function.Consumer<T> onConfirm) {
      edit(owner, message, formPredicate, onConfirm);
   }

   public <T> void edit(javax.swing.JComponent owner, String message, java.util.function.Function<Form, java.util.Optional<T>> formPredicate, java.util.function.Consumer<T> onConfirm) {

      final javax.swing.JPanel build = build();
      build.setBorder(border);

      final javax.swing.JDialog dialog = newDialog(build, message);
      final javax.swing.JButton btnConfirm = new javax.swing.JButton(new javax.swing.AbstractAction("Confirm") {
         @Override
         public void actionPerformed(java.awt.event.ActionEvent actionEvent) {

            final java.util.Optional<T> apply = validate(formPredicate);
            if (apply.isEmpty()) {
               build.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.RED));
               return;
            }
            dialog.dispose();

            apply.ifPresent(onConfirm);
         }
      });
      dialog.getRootPane().registerKeyboardAction(e -> btnConfirm.doClick(), javax.swing.KeyStroke.getKeyStroke("control Enter"), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);

      showDialog(owner, build, dialog, btnConfirm);
   }

   public <T> java.util.Optional<T> validate(java.util.function.Function<nextgen.lambda.ui.forms.Form, java.util.Optional<T>> formPredicate) {
      return formPredicate.apply(nextgen.lambda.ui.forms.Form.this);
   }

   public FormRow newRow() {
      return newRow(this.rows.size());
   }

   public FormRow newRow(int index) {
      final FormRow row = new FormRow(this, this);
      this.rows.put(index, row);
      return row;
   }

   public FormColumn newColumn() {
      return newColumn(this.cols.size());
   }

   public FormColumn newColumn(int index) {
      final FormColumn col = new FormColumn(this);
      this.cols.put(index, col);
      return col;
   }

   public javax.swing.JPanel build() {

      if (this.panel != null) return this.panel;

      this.panel = new javax.swing.JPanel(new com.jgoodies.forms.layout.FormLayout(
         colSpec(),
         rowSpec()));
      panel.setBorder(border);
      panel.setFont(nextgen.lambda.ui.UI.font);

      final com.jgoodies.forms.layout.CellConstraints cc = new com.jgoodies.forms.layout.CellConstraints();
      rows.forEach((r, row) -> row.columns.forEach((c, component) -> {
         final RowSpan spans = row.spans(c);
         final RowAlignment alignments = row.alignments(c);
         final com.jgoodies.forms.layout.CellConstraints xywh = cc.xywh(c + 1, r + 1, spans.colSpan, spans.rowSpan, alignments.toString());

         try {
            panel.add(component, xywh);
         } catch (Throwable throwable) {
            log.severe(c + " " + component.getClass().getName());
            log.severe(spans + " " + alignments + " " + xywh);
            log.severe(throwable.getMessage());
            nextgen.lambda.EVENTS.exception(throwable);
         }
      }));
      return panel;
   }

   public nextgen.lambda.ui.forms.Form newLeftPrefNone() {
      return newColumn().left().pref().none().builder();
   }

   public nextgen.lambda.ui.forms.Form newLeftPrefGrow() {
      return newColumn().left().pref().grow().builder();
   }

   public nextgen.lambda.ui.forms.FormRow newCenterPrefNone() {
      return newRow().center().pref().none();
   }

   private String rowSpec() {
      final StringBuilder spec = new StringBuilder();
      for (int i = 0; i < rows.size(); i++) spec.append(i > 0 ? ", " : "").append(rows.get(i).spec());
      return spec.toString();
   }

   private String colSpec() {
      final StringBuilder spec = new StringBuilder();
      for (int i = 0; i < cols.size(); i++) spec.append(i > 0 ? ", " : "").append(cols.get(i).spec());
      return spec.toString();
   }

   public java.util.stream.Stream<FormRow> rows() {
      return this.rows.values().stream();
   }

   public nextgen.lambda.ui.forms.FormRow row(int index) {
      return rows.get(index);
   }
}