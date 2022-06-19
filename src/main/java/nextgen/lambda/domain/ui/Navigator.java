package nextgen.lambda.domain.ui;

public class Navigator extends javax.swing.JTree {

	public NavigatorTreeNode root;

	public Navigator(io.vertx.core.Vertx model) {
	    setModel(new javax.swing.tree.DefaultTreeModel(root = new NavigatorTreeNode(this, model)));
	    addMouseListener(new java.awt.event.MouseAdapter() {
	       @Override
	       public void mouseClicked(java.awt.event.MouseEvent e) {

	          final javax.swing.tree.TreePath selectionPath = getPathForLocation(e.getX(), e.getY());
	          if (selectionPath == null) return;

	          final nextgen.lambda.domain.ui.NavigatorTreeNode selected = (nextgen.lambda.domain.ui.NavigatorTreeNode) selectionPath.getLastPathComponent();

	          if (javax.swing.SwingUtilities.isRightMouseButton(e)) {
	             final javax.swing.JPopupMenu popupMenu = new javax.swing.JPopupMenu();
	             selected.actions().forEach(popupMenu::add);
	             if (popupMenu.getComponentCount() != 0) javax.swing.SwingUtilities.invokeLater(() -> popupMenu.show(nextgen.lambda.domain.ui.Navigator.this, e.getX(), e.getY()));
	          }
	       }
	    });
	}
}