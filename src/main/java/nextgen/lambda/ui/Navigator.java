package nextgen.lambda.ui;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;

public class Navigator extends UIComponent {

   public Navigator(UI ui) {
      super(ui);
      setBackground(ui().getBackground());
      add(new JScrollPane(new NavigatorTree(this)), BorderLayout.CENTER);
   }

   private class NavigatorTree extends JTree {
      NavigatorTree(Navigator navigator) {
         super();
         setBackground(ui().getBackground());
         setModel(new DefaultTreeModel(new NavigatorRoot(navigator, this)));
         addMouseListener(new NavigatorMouseListener());
         setCellRenderer(new NavigatorTreeNodeRenderer());
      }

      private class NavigatorTreeNode extends DefaultMutableTreeNode {
         Icon icon;
         String tooltip;

         NavigatorTreeNode(Object userObject) {
            super(userObject);
         }

         Collection<Action> actions() {
            return new ArrayList<>();
         }
      }
      private final class NavigatorRoot extends NavigatorTreeNode {
         NavigatorRoot(Navigator navigator, NavigatorTree navigatorTree) {
            super("\u03BB");
            icon = ui().squareIcon(Color.ORANGE);
         }
      }

      private final class NavigatorTreeNodeRenderer extends javax.swing.tree.DefaultTreeCellRenderer {
         @Override
         public java.awt.Component getTreeCellRendererComponent(javax.swing.JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            final NavigatorTreeNode node = (NavigatorTreeNode) value;
            setIcon(node.icon);
            setOpenIcon(node.icon);
            setClosedIcon(node.icon);
            setLeafIcon(node.icon);
            setToolTipText(node.tooltip);
            return super.getTreeCellRendererComponent(tree, node, sel, expanded, leaf, row, hasFocus);
         }
      }

      private final class NavigatorMouseListener extends MouseAdapter {

         @Override
         public void mouseClicked(MouseEvent e) {
            final javax.swing.tree.TreePath selectionPath = getPathForLocation(e.getX(), e.getY());
            if (selectionPath == null) return;
            final Object lastPathComponent = selectionPath.getLastPathComponent();

            if (javax.swing.SwingUtilities.isRightMouseButton(e))
               ui().showPopup(e, () -> ((NavigatorTreeNode) lastPathComponent).actions());
            else
               ui().post(((NavigatorTreeNode) lastPathComponent).getUserObject());
         }
      }
   }
}