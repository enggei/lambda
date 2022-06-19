package nextgen.lambda.domain.ui;

public class Action extends javax.swing.AbstractAction {

	public nextgen.lambda.domain.actions.Task task;

	public Action(String name, nextgen.lambda.domain.actions.Task task) {
	    super(name);
	    this.task = task;
	}

	@Override
	public void actionPerformed(java.awt.event.ActionEvent event) {
	    try {
	    	task.run();
	    } catch (Throwable e) {
	    	throw new RuntimeException(e);
	    }
	}
}