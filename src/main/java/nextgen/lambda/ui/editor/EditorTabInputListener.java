package nextgen.lambda.ui.editor;

public class EditorTabInputListener implements java.awt.event.KeyListener, java.awt.event.MouseListener, java.awt.event.FocusListener {

   private static final java.util.logging.Logger log = nextgen.lambda.LOG.logger(EditorTabInputListener.class);
   private final nextgen.lambda.ui.editor.EditorTab<?, ?> component;

   public EditorTabInputListener(EditorTab<?, ?> component) {
      this.component = component;
   }

   @Override
   public void keyTyped(java.awt.event.KeyEvent keyEvent) {

   }

   @Override
   public void keyPressed(java.awt.event.KeyEvent keyEvent) {
      log.info("keyPressed");
   }

   @Override
   public void keyReleased(java.awt.event.KeyEvent event) {
      nextgen.lambda.ACTIONS.runKeyAction(event, component.actions());
   }

   @Override
   public void mouseClicked(java.awt.event.MouseEvent event) {
      if (javax.swing.SwingUtilities.isRightMouseButton(event))
         nextgen.lambda.ui.UI.showPopup(component, event, component.actions());
      else
         javax.swing.SwingUtilities.invokeLater(() -> component.editor.setSelected(component));
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

   @Override
   public void focusGained(java.awt.event.FocusEvent focusEvent) {
      log.info("focusGained");
   }

   @Override
   public void focusLost(java.awt.event.FocusEvent focusEvent) {
      log.info("focusLost");
   }
}