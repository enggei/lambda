package nextgen.lambda.ui.navigator;

public class NavigatorTreeNode<T> extends javax.swing.tree.DefaultMutableTreeNode {

   final java.util.function.Function<T, String> renderer;
   public javax.swing.Icon icon;
   public String tooltip;
   public Navigator navigator;

   public NavigatorTreeNode(Navigator navigator, T model, java.util.function.Function<T, String> renderer, java.awt.Color iconColor) {
      super(model);
      this.navigator = navigator;
      this.renderer = renderer;
      this.icon = nextgen.lambda.ui.UI.icon(iconColor);
   }

   @Override
   public String toString() {
      return renderer.apply(model());
   }

   @SuppressWarnings("unchecked")
   public T model() {
      return (T) getUserObject();
   }

   @SuppressWarnings("unchecked")
   public <P> nextgen.lambda.ui.navigator.NavigatorTreeNode<P> parent() {
      return (nextgen.lambda.ui.navigator.NavigatorTreeNode<P>) getParent();
   }

   public nextgen.lambda.ui.canvas.Canvas canvas() {
      return navigator.ui().canvas;
   }

   public void refresh() {
      javax.swing.SwingUtilities.invokeLater(() -> navigator.model().nodeStructureChanged(this));
   }

   public java.util.Collection<javax.swing.Action> actions() {
      final java.util.ArrayList<javax.swing.Action> actions = new java.util.ArrayList<>();
      return actions;
   }

   public void removeChild(Object model) {

      int n = getChildCount();

      for (int i = 0; i < n; i++) {
         final NavigatorTreeNode<?> node = (NavigatorTreeNode<?>) getChildAt(i);
         if (node.model().equals(model)) {
            node.removeFromParent();
            navigator.model().nodesWereRemoved(this, new int[]{i}, new Object[]{node});
            return;
         }
      }
   }

   public void insert(NavigatorTreeNode<?> child) {

      int n = getChildCount();

      if (n == 0) {
         add(child);
         navigator.model().nodesWereInserted(this, new int[]{n});
         return;
      }

      for (int i = 0; i < n; i++) {
         final NavigatorTreeNode<?> node = (NavigatorTreeNode<?>) getChildAt(i);
         if (node.getUserObject().toString().compareTo(child.getUserObject().toString()) > 0) {
            insert(child, i);
            navigator.model().nodesWereInserted(this, new int[]{i});
            return;
         }
      }

      add(child);
      navigator.model().nodesWereInserted(this, new int[]{n});
   }
}