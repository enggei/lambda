package nextgen.lambda.ui.editor.nodes;

public class EditorFutureComponent extends javax.swing.JPanel {

   private final Throwable model;
   private final nextgen.lambda.ui.editor.Editor editor;

   public EditorFutureComponent(nextgen.lambda.ui.editor.Editor editor, Throwable model) {
      super(new java.awt.BorderLayout());
      this.model = model;
      this.editor = editor;
      setFont(nextgen.lambda.ui.UI.font);

      final javax.swing.JTabbedPane tabbedPane = new javax.swing.JTabbedPane();
      tabbedPane.add("Object", new nextgen.lambda.ui.forms.nodes.ObjectForm(this, model, editor.ui).build());
      tabbedPane.add("Class", new nextgen.lambda.ui.forms.nodes.ClassForm(this, model, editor.ui).build());
      canvas().ui.context().modules().forEach(lambdaModule -> lambdaModule.editor(model).ifPresent(component -> tabbedPane.add(lambdaModule.toString(), component)));
      add(tabbedPane, java.awt.BorderLayout.CENTER);
   }

   public Throwable model() {
      return model;
   }

   public nextgen.lambda.ui.canvas.Canvas canvas() {
      return editor.ui.canvas;
   }
}