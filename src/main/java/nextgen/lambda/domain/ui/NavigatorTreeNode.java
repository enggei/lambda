package nextgen.lambda.domain.ui;

public class NavigatorTreeNode extends javax.swing.tree.DefaultMutableTreeNode {

	public javax.swing.Icon icon;
	public String tooltip;
	public Navigator navigator;

	public NavigatorTreeNode(Navigator navigator, Object model) {
	    super(model);
	    this.navigator = navigator;
	}

	public java.util.Collection<Action> actions() {
	    return java.util.Collections.emptyList();
	}

	public javax.swing.tree.DefaultTreeModel treeModel() {
	    return (javax.swing.tree.DefaultTreeModel) navigator.getModel();
	}

	public void addModel(Object model) {
	    final NavigatorTreeNode child = new NavigatorTreeNode(navigator, model);

	    int n = getChildCount();

	    if (n == 0) {
	    	add(child);
	    	treeModel().nodesWereInserted(this, new int[]{n});
	    	return;
	    }

	    for (int i = 0; i < n; i++) {
	    	final NavigatorTreeNode node = (NavigatorTreeNode) getChildAt(i);
	    	if (node.getUserObject().toString().compareTo(child.getUserObject().toString()) > 0) {
	    		insert(child, i);
	    		treeModel().nodesWereInserted(this, new int[]{i});
	    		return;
	    	}
	    }

	    add(child);
	    treeModel().nodesWereInserted(this, new int[]{n});
	}
}