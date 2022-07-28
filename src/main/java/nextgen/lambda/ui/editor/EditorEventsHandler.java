package nextgen.lambda.ui.editor;

public class EditorEventsHandler {

   private final nextgen.lambda.ui.editor.Editor editor;

   public EditorEventsHandler(nextgen.lambda.ui.editor.Editor editor) {
      this.editor = editor;
   }

   @org.greenrobot.eventbus.Subscribe
   public void onEvent(nextgen.lambda.EVENTS.Event event) {
      switch (event.type) {
         case CLOSE -> editor.remove(event.model);
         case EDIT -> editor.merge(event.model, getEditorTabSupplier(event, new nextgen.lambda.ui.editor.nodes.EditorObjectComponent<>(editor, event.model)));
         case FUTURE -> editor.merge(event.model, getEditorTabSupplier(event, new nextgen.lambda.ui.editor.nodes.EditorFutureComponent(editor, (Throwable) event.model)));
         case EXCEPTION -> editor.merge(event.model, getEditorTabSupplier(event, new nextgen.lambda.ui.editor.nodes.EditorExceptionComponent(editor, (Throwable) event.model)));
      }
   }

   private java.util.function.Supplier<nextgen.lambda.ui.editor.EditorTab<Object, ?>> getEditorTabSupplier(nextgen.lambda.EVENTS.Event event, javax.swing.JComponent component) {
      return () -> new nextgen.lambda.ui.editor.EditorTab<>(editor, event.label(), event.model, component);
   }
}