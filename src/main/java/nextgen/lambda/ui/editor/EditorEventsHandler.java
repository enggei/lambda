package nextgen.lambda.ui.editor;

public class EditorEventsHandler {

   private final nextgen.lambda.ui.editor.Editor editor;

   public EditorEventsHandler(nextgen.lambda.ui.editor.Editor editor) {
      this.editor = editor;
   }

   @org.greenrobot.eventbus.Subscribe
   public void onEvent(nextgen.lambda.EVENTS.Event event) {
      switch (event.type) {
         case EDIT -> {
            if (event.model instanceof Class<?>)
               editor.merge(event.model, () -> new nextgen.lambda.ui.editor.EditorTab<>(editor, event.label(), event.model, new nextgen.lambda.ui.editor.nodes.EditorClassComponent(editor, (Class<?>) event.model)));
            else
               editor.merge(event.model, () -> new nextgen.lambda.ui.editor.EditorTab<>(editor, event.label(), event.model, new nextgen.lambda.ui.editor.nodes.EditorObjectComponent<>(editor, event.model)));
         }
      }
   }
}