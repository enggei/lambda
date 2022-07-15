package nextgen.lambda.ui.navigator;

public class Navigator extends javax.swing.JTree {

   private static final java.util.logging.Logger log = nextgen.lambda.LOG.logger(Navigator.class);
   private final nextgen.lambda.ui.UI ui;

   public Navigator(nextgen.lambda.ui.UI ui) {
      super();
      this.ui = ui;

      setModel(new javax.swing.tree.DefaultTreeModel(new nextgen.lambda.ui.navigator.NavigatorTreeNode<>(this, "Lambda", m -> "\u03BB", java.awt.Color.decode("#a6611a"))));
      setFont(nextgen.lambda.ui.UI.font);
      setBackground(ui.getBackground());

      javax.swing.InputMap inputMap = getInputMap(javax.swing.JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
      javax.swing.KeyStroke[] keyStrokes = inputMap.allKeys();
      for (javax.swing.KeyStroke keyStroke : keyStrokes) log.info("\tnavigator " + keyStroke + " " + inputMap.get(keyStroke));

      final NavigatorInputListener inputListener = new NavigatorInputListener(this);
      addMouseListener(inputListener);
      addKeyListener(inputListener);
      setCellRenderer(new NavigatorTreeNodeRenderer());
   }

   public javax.swing.tree.DefaultTreeModel model() {
      return (javax.swing.tree.DefaultTreeModel) getModel();
   }

   public nextgen.lambda.ui.navigator.NavigatorTreeNode<?> root() {
      return (NavigatorTreeNode<?>) model().getRoot();
   }

   public nextgen.lambda.ui.UI ui() {
      return ui;
   }
}