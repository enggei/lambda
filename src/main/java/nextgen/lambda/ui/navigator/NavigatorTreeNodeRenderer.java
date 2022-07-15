package nextgen.lambda.ui.navigator;

public class NavigatorTreeNodeRenderer extends javax.swing.tree.DefaultTreeCellRenderer {
   @Override
   public java.awt.Component getTreeCellRendererComponent(javax.swing.JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
      final NavigatorTreeNode<?> node = (nextgen.lambda.ui.navigator.NavigatorTreeNode<?>) value;
      setIcon(node.icon);
      setOpenIcon(node.icon);
      setClosedIcon(node.icon);
      setLeafIcon(node.icon);
      setToolTipText(node.tooltip);
      return super.getTreeCellRendererComponent(tree, node, sel, expanded, leaf, row, hasFocus);
   }
}