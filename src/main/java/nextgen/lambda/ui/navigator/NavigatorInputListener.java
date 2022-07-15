package nextgen.lambda.ui.navigator;

public class NavigatorInputListener implements java.awt.event.KeyListener, java.awt.event.MouseListener {

   private final nextgen.lambda.ui.navigator.Navigator navigator;

   public NavigatorInputListener(nextgen.lambda.ui.navigator.Navigator navigator) {
      this.navigator = navigator;
   }

   @Override
   public void keyReleased(java.awt.event.KeyEvent e) {
      final javax.swing.tree.TreePath selectionPath = navigator.getSelectionPath();
      if (selectionPath == null) return;
      final NavigatorTreeNode<?> treeNode = (NavigatorTreeNode<?>) selectionPath.getLastPathComponent();
      nextgen.lambda.ACTIONS.runKeyAction(e, treeNode.actions());
   }

   @Override
   public void mouseClicked(java.awt.event.MouseEvent e) {

      final javax.swing.tree.TreePath selectionPath = navigator.getPathForLocation(e.getX(), e.getY());
      if (selectionPath == null) return;

      final nextgen.lambda.ui.navigator.NavigatorTreeNode<?> selected = (nextgen.lambda.ui.navigator.NavigatorTreeNode<?>) selectionPath.getLastPathComponent();

      if (javax.swing.SwingUtilities.isRightMouseButton(e)) {
         nextgen.lambda.ui.UI.showPopup(navigator, e, selected.actions());
      }
   }

   @Override
   public void keyTyped(java.awt.event.KeyEvent keyEvent) {

   }

   @Override
   public void keyPressed(java.awt.event.KeyEvent keyEvent) {

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
