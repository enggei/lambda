package nextgen.lambda.ui.editor;

public class Editor extends javax.swing.JTabbedPane {

   public nextgen.lambda.ui.UI ui;
   final java.util.Map<Object, EditorTab<?, ?>> editors = new java.util.concurrent.ConcurrentHashMap<>();
   final java.util.Map<javax.swing.JComponent, javax.swing.JScrollPane> scrollers = new java.util.concurrent.ConcurrentHashMap<>();

   public Editor(nextgen.lambda.ui.UI ui) {
      this.ui = ui;
      setFont(nextgen.lambda.ui.UI.font);
      setOpaque(true);
      setBackground(ui.getBackground());
      nextgen.lambda.EVENTS.register(this);
   }

   public <T> void merge(T model, java.util.function.Supplier<EditorTab<T, ?>> supplier) {
      if (editors.containsKey(model)) {
         setSelectedComponent(scrollers.get(editors.get(model).component));
      } else {
         final EditorTab<T, ?> editor = supplier.get();
         editors.put(model, editor);
         scrollers.put(editor.component, new javax.swing.JScrollPane(editor.component));
         add(editor.title, scrollers.get(editors.get(model).component));
         setSelectedComponent(scrollers.get(editors.get(model).component));
         setTabComponentAt(indexOfComponent(scrollers.get(editors.get(model).component)), editor);
      }
   }

   public void remove(nextgen.lambda.ui.editor.EditorTab<?, ?> editorTab) {
      editors.remove(editorTab.model);
      remove(scrollers.remove(editorTab.component));
   }

   public void setSelected(nextgen.lambda.ui.editor.EditorTab<?, ?> editorTab) {
      setSelectedComponent(scrollers.get(editorTab.component));
   }
}