package nextgen.lambda.ui.editor.nodes;

public class EditorTextComponentInputListener implements java.awt.event.KeyListener, java.awt.event.MouseListener {

   final nextgen.lambda.ui.editor.nodes.EditorTextComponent<?> editorTextComponent;

   public EditorTextComponentInputListener(nextgen.lambda.ui.editor.nodes.EditorTextComponent<?> editorTextComponent) {
      this.editorTextComponent = editorTextComponent;
   }

   @Override
   public void keyTyped(java.awt.event.KeyEvent keyEvent) {

   }

   @Override
   public void keyPressed(java.awt.event.KeyEvent keyEvent) {

   }

   @Override
   public void keyReleased(java.awt.event.KeyEvent keyEvent) {
      if (keyEvent.isControlDown() && java.awt.event.KeyEvent.VK_CONTROL != keyEvent.getKeyCode())
         nextgen.lambda.ACTIONS.runKeyAction(keyEvent, editorTextComponent.actions());
   }

   @Override
   public void mouseClicked(java.awt.event.MouseEvent mouseEvent) {
      if (javax.swing.SwingUtilities.isRightMouseButton(mouseEvent))
         nextgen.lambda.ui.UI.showPopup(editorTextComponent, mouseEvent, editorTextComponent.actions());
   }

   @Override
   public void mousePressed(java.awt.event.MouseEvent mouseEvent) {

   }

   @Override
   public void mouseReleased(java.awt.event.MouseEvent mouseEvent) {

   }

   @Override
   public void mouseEntered(java.awt.event.MouseEvent mouseEvent) {

   }

   @Override
   public void mouseExited(java.awt.event.MouseEvent mouseEvent) {

   }
}
