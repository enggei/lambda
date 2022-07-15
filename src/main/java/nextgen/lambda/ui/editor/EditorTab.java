package nextgen.lambda.ui.editor;

public class EditorTab<M, C extends javax.swing.JComponent> extends javax.swing.JPanel {

   final java.util.UUID uuid = java.util.UUID.randomUUID();
   final nextgen.lambda.ui.editor.Editor editor;
   final String title;
   final C component;
   final M model;

   EditorTab(final Editor editor, String title, M model, C component) {
      super(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));
      this.editor = editor;
      this.title = title;
      this.component = component;
      this.model = model;

      javax.swing.JLabel label = new javax.swing.JLabel(title);
      label.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 5));
      add(label);

      setOpaque(false);

      final nextgen.lambda.ui.editor.EditorTabInputListener inputListener = new nextgen.lambda.ui.editor.EditorTabInputListener(this);
      addMouseListener(inputListener);
      addFocusListener(inputListener);
   }

   java.util.List<javax.swing.Action> actions() {
      return java.util.List.of(
         nextgen.lambda.ACTIONS.newAction("Close", "C", () -> editor.remove(nextgen.lambda.ui.editor.EditorTab.this)),
         nextgen.lambda.ACTIONS.newAction("Retain", "R", () -> new java.util.LinkedHashSet<>(editor.editors.values())
            .stream().filter(other -> !other.equals(nextgen.lambda.ui.editor.EditorTab.this))
            .forEach(editor::remove)),
         nextgen.lambda.ACTIONS.newAction("Close All", "X" ,() -> new java.util.LinkedHashSet<>(editor.editors.values()).forEach(editor::remove)));
   }

   @Override
   public String toString() {
      return String.join(" ", uuid.toString(), title, model.toString().replaceAll("\r?\n"," "));
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      nextgen.lambda.ui.editor.EditorTab<?, ?> that = (nextgen.lambda.ui.editor.EditorTab<?, ?>) o;
      return uuid.equals(that.uuid);
   }

   @Override
   public int hashCode() {
      return uuid.hashCode();
   }
}