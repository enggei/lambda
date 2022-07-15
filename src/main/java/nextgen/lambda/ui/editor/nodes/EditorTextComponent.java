package nextgen.lambda.ui.editor.nodes;

import static nextgen.lambda.ui.UI.*;

public class EditorTextComponent<T> extends javax.swing.JTextArea {

   private final T model;
   final java.util.function.Function<T, String> renderer;

   private final java.util.Set<javax.swing.Action> defaultActions = new java.util.LinkedHashSet<>();

   public EditorTextComponent(T model, java.util.function.Function<T, String> renderer) {
      super(renderer.apply(model));

      this.model = model;
      this.renderer = renderer;

      setFont(nextgen.lambda.ui.UI.font);

      final nextgen.lambda.ui.editor.nodes.EditorTextComponentInputListener inputListener = new nextgen.lambda.ui.editor.nodes.EditorTextComponentInputListener(this);
      addMouseListener(inputListener);
      addKeyListener(inputListener);
   }

   public T model() {
      return this.model;
   }

   public void refresh() {
      javax.swing.SwingUtilities.invokeLater(() -> setText(renderer.apply(model)));
   }

   public java.util.Collection<javax.swing.Action> actions() {
      final java.util.ArrayList<javax.swing.Action> actions = new java.util.ArrayList<>(defaultActions);
      actions.add(nextgen.lambda.ACTIONS.newAction("Menu", "control SPACE", () -> javax.swing.SwingUtilities.invokeLater(() -> getPop(actions()).show(EditorTextComponent.this, (int) cursorLocation().getX(), (int) cursorLocation().getY()))));
      return actions;
   }

   public String getWordAtCaret() {
      try {
         int caretPosition = getCaretPosition();
         int start = javax.swing.text.Utilities.getWordStart(this, caretPosition);
         int end = javax.swing.text.Utilities.getWordEnd(this, caretPosition);
         return getText(start, end - start);
      } catch (javax.swing.text.BadLocationException e) {
         nextgen.lambda.EVENTS.exception(e);
      }

      return null;
   }

   public void insertAtCaret(String text) {
      insert(text, getCaretPosition());
   }

   public java.awt.geom.Rectangle2D cursorLocation() {
      try {
         return modelToView2D(getCaretPosition());
      } catch (javax.swing.text.BadLocationException e) {
         nextgen.lambda.EVENTS.exception(e);
         return getVisibleRect().getBounds2D();
      }
   }

   public nextgen.lambda.ui.editor.nodes.EditorTextComponent<T> addAction(javax.swing.Action action) {
      this.defaultActions.add(action);
      return this;
   }
}