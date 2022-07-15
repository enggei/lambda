package nextgen.lambda.ui;

import static java.util.stream.Collectors.*;
import static java.util.stream.Stream.*;
import static javax.swing.ListSelectionModel.*;

public class UI extends javax.swing.JFrame {

   private static final java.util.logging.Logger log = nextgen.lambda.LOG.logger(UI.class);

   public static java.awt.Font font = new java.awt.Font("Input Mono", java.awt.Font.PLAIN, 12);
   private static final int width = 16;
   private static final int height = 16;
   private static final java.util.Map<String, javax.swing.Icon> iconCache = new java.util.LinkedHashMap<>();


   static String LAST_PATH = System.getProperty("user.home");

   public final nextgen.lambda.ui.navigator.Navigator navigator;
   public final nextgen.lambda.ui.canvas.Canvas canvas;
   public final nextgen.lambda.ui.editor.Editor editor;

   private final nextgen.lambda.CONTEXT context;

   public UI(nextgen.lambda.CONTEXT context) throws java.awt.HeadlessException {
      super("\u03BB");

      this.context = context;
      this.navigator = new nextgen.lambda.ui.navigator.Navigator(this);
      this.canvas = new nextgen.lambda.ui.canvas.Canvas(this);
      this.editor = new nextgen.lambda.ui.editor.Editor(this);

      this.context.modules().forEach(lambdaModule -> lambdaModule.register(this));

      final javax.swing.JSplitPane canvas_editor = new javax.swing.JSplitPane(javax.swing.JSplitPane.HORIZONTAL_SPLIT, canvas, editor);
      canvas_editor.setOneTouchExpandable(true);
      canvas_editor.setResizeWeight(0.5);

      final javax.swing.JSplitPane navigator_canvas_editor = new javax.swing.JSplitPane(javax.swing.JSplitPane.HORIZONTAL_SPLIT, new javax.swing.JScrollPane(navigator), canvas_editor);
      navigator_canvas_editor.setOneTouchExpandable(true);
      navigator_canvas_editor.setResizeWeight(0.5);

      final nextgen.lambda.ui.MenuBar menuBar = new nextgen.lambda.ui.MenuBar(this, canvas_editor, navigator_canvas_editor);
      setJMenuBar(menuBar);

      getContentPane().add(navigator_canvas_editor, java.awt.BorderLayout.CENTER);
   }

   public static java.awt.Color getContrastColor(java.awt.Color color) {
      // Counting the perceptive luminance - human eye favors green color...
      double a = 1 - (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue()) / 255;
      return a < 0.5 ? java.awt.Color.BLACK : java.awt.Color.WHITE;
   }

   public static java.awt.Color[] palette(String palette) {
      return java.util.Arrays.stream(palette.substring(1, palette.length() - 1).replaceAll("'", "").split(",")).map(java.awt.Color::decode).toArray(java.awt.Color[]::new);
   }

   public static java.awt.Color[] resetPalette(String palette, java.util.concurrent.atomic.AtomicInteger pIndex) {
      java.awt.Color[] colors = palette(palette);
      pIndex.set(-1);
      return colors;
   }

   public static void show(javax.swing.JComponent component, String content) {
      javax.swing.JOptionPane.showMessageDialog(component, content.trim(), "Show", javax.swing.JOptionPane.INFORMATION_MESSAGE);
   }

   public nextgen.lambda.CONTEXT context() {
      return context;
   }

   public static void showPopup(javax.swing.JComponent component, org.piccolo2d.event.PInputEvent inputEvent, java.util.Collection<javax.swing.Action> actions) {
      javax.swing.SwingUtilities.invokeLater(() -> getPop(actions).show(component, (int) inputEvent.getCanvasPosition().getX(), (int) inputEvent.getCanvasPosition().getY()));
   }

   public static void showPopup(javax.swing.JComponent component, java.awt.event.MouseEvent mouseEvent, java.util.Collection<javax.swing.Action> actions) {
      javax.swing.SwingUtilities.invokeLater(() -> getPop(actions).show(component, mouseEvent.getX(), mouseEvent.getY()));
   }

   public static javax.swing.JPopupMenu getPop(java.util.Collection<javax.swing.Action> actions) {
      final javax.swing.JPopupMenu pop = new javax.swing.JPopupMenu();

      if (actions.size() > 10) {

         final java.util.Map<String, javax.swing.JMenu> menuMap = new java.util.TreeMap<>();

         for (javax.swing.Action action : actions) {
            final String actionName = action.getValue(javax.swing.Action.NAME).toString().trim();
            final String[] ss = actionName.split("[ |_]");

            log.info(actionName + " (" + ss[0] + ") " + ss.length + " " + java.util.Arrays.toString(ss));

            if (!menuMap.containsKey(ss[0])) {
               menuMap.put(ss[0], new javax.swing.JMenu(ss[0]));
               pop.add(menuMap.get(ss[0]));
            }

            menuMap.get(ss[0]).add(action);
         }
      } else for (javax.swing.Action action : actions) pop.add(action);
      return pop;
   }

   public static javax.swing.JDialog newDialog(javax.swing.JComponent owner, String message) {
      final javax.swing.JDialog dialog = new javax.swing.JDialog(javax.swing.SwingUtilities.windowForComponent(owner), message, java.awt.Dialog.ModalityType.DOCUMENT_MODAL);
      dialog.setDefaultCloseOperation(javax.swing.JDialog.DISPOSE_ON_CLOSE);
      dialog.getRootPane().registerKeyboardAction(e -> dialog.dispose(), javax.swing.KeyStroke.getKeyStroke("Escape"), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
      return dialog;
   }

   public static java.util.Optional<Boolean> show(javax.swing.JComponent owner, String message, javax.swing.JComponent panel) {

      final java.util.concurrent.atomic.AtomicBoolean confirmed = new java.util.concurrent.atomic.AtomicBoolean(false);

      final javax.swing.JDialog dialog = newDialog(owner, message);

      final javax.swing.JButton btnConfirm = new javax.swing.JButton(new javax.swing.AbstractAction("Confirm") {
         @Override
         public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
            confirmed.set(true);
            dialog.dispose();
         }
      });
      dialog.getRootPane().registerKeyboardAction(e -> btnConfirm.doClick(), javax.swing.KeyStroke.getKeyStroke("control Enter"), javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);

      final javax.swing.JButton btnCancel = cancelButton(dialog);
      final javax.swing.JPanel buttonPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
      buttonPanel.add(btnConfirm);
      buttonPanel.add(btnCancel);

      dialog.getRootPane().setDefaultButton(btnConfirm);
      dialog.getContentPane().add(panel, java.awt.BorderLayout.CENTER);
      dialog.getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);
      dialog.setMaximumSize(java.awt.Toolkit.getDefaultToolkit().getScreenSize());
      dialog.setMinimumSize(new java.awt.Dimension(100, 100));
      dialog.pack();
      dialog.setLocationRelativeTo(owner);
      dialog.setVisible(true);

      return confirmed.get() ? java.util.Optional.of(true) : java.util.Optional.empty();
   }

   public static void showDialog(javax.swing.JComponent owner, javax.swing.JPanel build, javax.swing.JDialog dialog, javax.swing.JButton btnConfirm) {
      final javax.swing.JButton btnCancel = cancelButton(dialog);
      final javax.swing.JPanel buttonPanel = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
      buttonPanel.add(btnConfirm);
      buttonPanel.add(btnCancel);

      dialog.getRootPane().setDefaultButton(btnConfirm);
      dialog.getContentPane().add(build, java.awt.BorderLayout.CENTER);
      dialog.getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);
      dialog.setMaximumSize(java.awt.Toolkit.getDefaultToolkit().getScreenSize());
      dialog.setMinimumSize(new java.awt.Dimension(100, 100));
      dialog.pack();
      dialog.setLocationRelativeTo(owner);
      dialog.setVisible(true);
   }

   public static javax.swing.JButton cancelButton(javax.swing.JDialog dialog) {
      return new javax.swing.JButton(new javax.swing.AbstractAction("Cancel") {
         @Override
         public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
            dialog.dispose();
         }
      });
   }

   public static java.util.Optional<Boolean> confirm(javax.swing.JComponent owner, String message) {
      final int i = javax.swing.JOptionPane.showConfirmDialog(owner, message.trim(), "Confirm", javax.swing.JOptionPane.OK_CANCEL_OPTION);
      return i == javax.swing.JOptionPane.OK_OPTION ? java.util.Optional.of(true) : java.util.Optional.empty();
   }

   public static nextgen.lambda.ui.UI start(nextgen.lambda.CONTEXT context) {

      try {
         javax.swing.UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarculaLaf());
      } catch (javax.swing.UnsupportedLookAndFeelException throwable) {
         nextgen.lambda.LOG.onException(log, throwable);
      }

      final nextgen.lambda.ui.UI ui = new nextgen.lambda.ui.UI(context);

      return ui;
   }

   void showCloseDialog(String message, javax.swing.JPanel panel) {
      javax.swing.JOptionPane.showMessageDialog(this, panel, message.trim(), javax.swing.JOptionPane.INFORMATION_MESSAGE);
   }

   public void showUI(Runnable onClose) {

      final java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
      final java.awt.Dimension preferredSize = new java.awt.Dimension(1200, 800);
      setMinimumSize(new java.awt.Dimension(800, 640));
      setPreferredSize(preferredSize);
      setMaximumSize(screenSize);
      setSize(preferredSize);
      setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);

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

   public static java.util.Optional<String> singleInput(javax.swing.JComponent parent, String message) {
      return singleInput(parent, message, fromClipboard().orElse(""));
   }

   public static java.util.Optional<String> singleInput(javax.swing.JComponent parent, String message, String initial) {
      final String s = javax.swing.JOptionPane.showInputDialog(parent, message, initial);
      if (s == null || s.trim().length() == 0)
         return java.util.Optional.empty();
      return java.util.Optional.of(s.trim());
   }

   public static void toClipboard(String s) {
      java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new java.awt.datatransfer.StringSelection(s), (cp, contents) -> System.out.println(contents.toString()));
   }

   public static java.util.Optional<String> fromClipboard() {
      final java.awt.datatransfer.Transferable contents = java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
      if ((contents != null) && contents.isDataFlavorSupported(java.awt.datatransfer.DataFlavor.stringFlavor)) {
         try {
            final String data = (String) contents.getTransferData(java.awt.datatransfer.DataFlavor.stringFlavor);
            return data.trim().length() == 0 ? java.util.Optional.empty() : java.util.Optional.of(data);
         } catch (Exception e) {
            return java.util.Optional.empty();
         }
      }
      return java.util.Optional.empty();
   }

   public static void requestFocus(javax.swing.JComponent component) {
      component.addAncestorListener(new javax.swing.event.AncestorListener() {
         @Override
         public void ancestorAdded(javax.swing.event.AncestorEvent ancestorEvent) {
            component.requestFocusInWindow();
         }

         @Override
         public void ancestorRemoved(javax.swing.event.AncestorEvent ancestorEvent) {

         }

         @Override
         public void ancestorMoved(javax.swing.event.AncestorEvent ancestorEvent) {

         }
      });
      component.addFocusListener(new java.awt.event.FocusAdapter() {
         private boolean isFirstTime = true;

         @Override
         public void focusLost(java.awt.event.FocusEvent e) {
            if (isFirstTime) {
               component.requestFocusInWindow();
               isFirstTime = false;
            }
         }
      });
   }

   public static java.util.Optional<java.nio.file.Path> openDir(javax.swing.JComponent owner, java.nio.file.Path path) {

      final javax.swing.JFileChooser chooser = new javax.swing.JFileChooser();
      chooser.setCurrentDirectory(path.toFile());
      chooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
      int i = chooser.showOpenDialog(owner);
      if (i == javax.swing.JOptionPane.OK_OPTION) {
         final java.io.File selectedFile = chooser.getSelectedFile();
         LAST_PATH = selectedFile.getParent();
         return java.util.Optional.of(java.nio.file.Path.of(selectedFile.getAbsolutePath()));
      }
      return java.util.Optional.empty();
   }

   public static java.util.Optional<java.nio.file.Path> openFile(javax.swing.JComponent owner, java.nio.file.Path path) {
      final javax.swing.JFileChooser chooser = new javax.swing.JFileChooser();
      chooser.setCurrentDirectory(path.toFile());
      chooser.setFileSelectionMode(javax.swing.JFileChooser.FILES_ONLY);
      int i = chooser.showOpenDialog(owner);
      if (i == javax.swing.JOptionPane.OK_OPTION) {
         final java.io.File selectedFile = chooser.getSelectedFile();
         LAST_PATH = selectedFile.getParent();
         return java.util.Optional.of(java.nio.file.Path.of(selectedFile.getAbsolutePath()));
      }
      return java.util.Optional.empty();
   }

   public static <T> java.util.Optional<T> selectOrNew(javax.swing.JComponent owner, String message, java.util.Collection<T> choices, java.util.function.Function<String, T> creator) {
      return selectOrNew(owner, message, new java.util.ArrayList<>(choices), null, creator);
   }

   public static <T> java.util.Optional<T> selectOrNew(javax.swing.JComponent owner, String message, java.util.List<T> choices, T defaultValue, java.util.function.Function<String, T> creator) {

      if (choices.isEmpty()) return singleInput(owner, message).map(creator);
      if (choices.size() == 1) return singleInput(owner, message, choices.get(0).toString()).map(creator);

      return selectForm(owner, message, choices, defaultValue, SINGLE_SELECTION, creator)
         .findFirst();
   }

   public static <T> java.util.Optional<T> singleSelect(javax.swing.JComponent owner, String message, java.util.List<T> choices) {
      return singleSelect(owner, message, choices, null);
   }

   public static <T> java.util.Optional<T> singleSelect(javax.swing.JComponent owner, String message, java.util.List<T> choices, T defaultValue) {
      return selectForm(owner, message, choices, defaultValue, SINGLE_SELECTION, null)
         .findFirst();
   }

   public static <T> java.util.Optional<java.util.List<T>> multipleSelect(javax.swing.JComponent owner, String message, java.util.Collection<T> choices) {
      return java.util.Optional.of(selectForm(owner, message, new java.util.ArrayList<>(choices), null, MULTIPLE_INTERVAL_SELECTION, null)
         .collect(toList()));
   }

   public static <T> java.util.stream.Stream<T> selectForm(javax.swing.JComponent owner, String message, java.util.List<T> choices, T defaultValue, int selectionMode, java.util.function.Function<String, T> creator) {

      if (choices.isEmpty()) return empty();
      if (choices.size() == 1) return choices.stream();

      final java.util.concurrent.atomic.AtomicInteger selIndex = new java.util.concurrent.atomic.AtomicInteger(defaultValue == null ? -1 : choices.indexOf(defaultValue));
      final java.util.List<T> all = new java.util.ArrayList<>(choices);
      final javax.swing.DefaultListModel<T> model = new javax.swing.DefaultListModel<>();
      model.addAll(all);

      final javax.swing.JTextField textField = new javax.swing.JTextField(selIndex.get() == -1 ? "" : model.get(selIndex.get()).toString());
      final javax.swing.JList<T> input = new javax.swing.JList<>(model);

      textField.addKeyListener(new java.awt.event.KeyAdapter() {
         @Override
         public void keyPressed(java.awt.event.KeyEvent e) {
            javax.swing.SwingUtilities.invokeLater(() -> {

               if (e.getKeyCode() == java.awt.event.KeyEvent.VK_DOWN) {
                  selIndex.incrementAndGet();
                  update();

               } else if (e.getKeyCode() == java.awt.event.KeyEvent.VK_UP) {
                  selIndex.decrementAndGet();
                  update();

               } else if (e.getKeyCode() >= java.awt.event.KeyEvent.VK_A && e.getKeyCode() <= java.awt.event.KeyEvent.VK_Z) {

                  nextgen.lambda.ACTIONS.run(() -> {
                     final String s = textField.getText().trim().toLowerCase();

                     final java.util.concurrent.atomic.AtomicReference<T> exact = new java.util.concurrent.atomic.AtomicReference<>();
                     final java.util.List<T> filtered = all.parallelStream()
                        .filter(t -> t.toString().toLowerCase().contains(s))
                        .peek(t -> {
                           if (exact.get() == null && textField.getText().equals(t.toString())) exact.set(t);
                        })
                        .collect(toList());

                     model.removeAllElements();
                     model.addAll(filtered);

                     if (exact.get() != null) input.setSelectedValue(exact.get(), true);
                     else if (filtered.size() == 1 && creator == null) input.setSelectedValue(filtered.get(0), true);
                  });
               }
            });
         }

         private void update() {
            if (selIndex.get() >= model.size()) selIndex.set(model.size() - 1);
            if (selIndex.get() < 0) selIndex.set(0);

            if (model.size() > selIndex.get()) {
               input.setSelectedIndex(selIndex.get());
               input.ensureIndexIsVisible(selIndex.get());
            }
         }
      });

      input.setSelectionMode(selectionMode);

      if (selIndex.get() != -1) {
         textField.selectAll();
         input.setSelectedIndex(selIndex.get());
         input.ensureIndexIsVisible(selIndex.get());
      }

      requestFocus(textField);

      final nextgen.lambda.ui.forms.Form builder = nextgen.lambda.ui.forms.Form.newForm()
         .newColumn().left().pref().grow()
         .newColumn().right().pref().none()
         .newColumn().right().pref().none()
         .newRow().fill().pref().none().add(textField, 3, 1)
         .newRow().fill().pref().grow().addScrollable(input, 3, 1)
         .builder();

      if (selectionMode != SINGLE_SELECTION) {
         if (model.size() > 0 && model.size() <= 2) input.setSelectionInterval(0, model.size() - 1);
         builder.newRow()
            .button(new javax.swing.AbstractAction("Select all") {
               @Override
               public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
                  input.clearSelection();
                  input.setSelectionInterval(0, model.size() - 1);
               }
            })
            .button(new javax.swing.AbstractAction("Unselect all") {
               @Override
               public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
                  input.clearSelection();
               }
            });
      }

      final java.util.concurrent.atomic.AtomicReference<java.util.stream.Stream<T>> selected = new java.util.concurrent.atomic.AtomicReference<>(empty());
      builder.show(owner, message, form -> {
         switch (selectionMode) {
            case SINGLE_SELECTION -> {
               final T selectedValue = input.getSelectedIndex() == -1 ? null : model.get(input.getSelectedIndex());
               selected.set(creator == null ? ofNullable(selectedValue) : of(selectedValue != null ? selectedValue : creator.apply(textField.getText().trim())));
            }

            case MULTIPLE_INTERVAL_SELECTION, SINGLE_INTERVAL_SELECTION -> selected.set(input.getSelectedValuesList().stream());
         }
      });

      return selected.get();
   }


   public static javax.swing.Icon icon(java.awt.Color color) {
      final String key = String.join("_", color.toString(), "iconOnly");
      if (iconCache.containsKey(key)) return iconCache.get(key);

      final java.awt.image.BufferedImage bim = new java.awt.image.BufferedImage(width, height, java.awt.image.BufferedImage.TYPE_INT_ARGB);
      final java.awt.Graphics2D g2 = bim.createGraphics();
      g2.setPaint(color);
      g2.fillRect(0, 0, width, height);
      g2.dispose();

      final javax.swing.ImageIcon icon = new javax.swing.ImageIcon(bim);
      iconCache.put(key, icon);
      return icon;
   }
}