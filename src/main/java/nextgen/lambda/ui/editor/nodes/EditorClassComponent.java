package nextgen.lambda.ui.editor.nodes;

public class EditorClassComponent extends javax.swing.JPanel {

   private final Class<?> model;
   private final nextgen.lambda.ui.editor.Editor editor;

   public EditorClassComponent(nextgen.lambda.ui.editor.Editor editor, Class<?> model) {
      super(new java.awt.BorderLayout());
      this.editor = editor;
      this.model = model;
      setFont(nextgen.lambda.ui.UI.font);
      final javax.swing.JTabbedPane tabbedPane = new javax.swing.JTabbedPane();
      tabbedPane.add("Class", new nextgen.lambda.ui.forms.nodes.ClassForm(this, model, editor.ui).build());
      add(tabbedPane, java.awt.BorderLayout.CENTER);
   }

   public Object model() {
      return model;
   }

   public nextgen.lambda.ui.canvas.Canvas canvas() {
      return editor.ui.canvas;
   }
}