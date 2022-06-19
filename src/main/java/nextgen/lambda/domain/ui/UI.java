package nextgen.lambda.domain.ui;

public class UI extends javax.swing.JFrame {

	public Navigator navigator;
	public Canvas canvas;
	public Editor editor;

	public void showUI(Runnable onClose) {
	    final java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	    final java.awt.Dimension preferredSize = new java.awt.Dimension(800, 768);
	    setMinimumSize(preferredSize);
	    setPreferredSize(preferredSize);
	    setMaximumSize(screenSize);
	    setSize(preferredSize);
	    final javax.swing.JPanel contentPanel = new javax.swing.JPanel(new java.awt.BorderLayout());
	    contentPanel.add(navigator, java.awt.BorderLayout.WEST);
	    contentPanel.add(canvas, java.awt.BorderLayout.CENTER);
	    contentPanel.add(editor, java.awt.BorderLayout.EAST);
	    getContentPane().add(contentPanel, java.awt.BorderLayout.CENTER);
	    addWindowListener(new java.awt.event.WindowAdapter() {

	        @Override
	        public void windowClosed(java.awt.event.WindowEvent e) {
	            onClose.run();
	        }
	    });
	    javax.swing.SwingUtilities.invokeLater(() -> {
	        pack();
	        setLocationByPlatform(true);
	        setVisible(true);
	    });
	}
}