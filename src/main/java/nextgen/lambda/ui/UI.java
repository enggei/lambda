package nextgen.lambda.ui;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.piccolo2d.event.PInputEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.function.Supplier;

public class UI extends JFrame {

   public static void main(String[] args) {

      try {
         UIManager.setLookAndFeel(new com.formdev.flatlaf.FlatDarculaLaf());
      } catch (UnsupportedLookAndFeelException ignore) {
      }

      new UI().showUI();
   }

   Font FONT = new Font("Monospaced", Font.PLAIN, 12);
   private final Map<Color, ImageIcon> iconCache = new LinkedHashMap<>();

   public UI() throws HeadlessException {
      super("\u03BB");

      final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      final Dimension preferredSize = new Dimension((int) (screenSize.width * .75d), (int) (screenSize.height * .75d));
      final Dimension minimumSize = new Dimension(100, 100);

      setMinimumSize(minimumSize);
      setPreferredSize(preferredSize);
      setMaximumSize(screenSize);

      add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
         new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
            new Navigator(this),
            new Canvas(this)),
         new Editor(this)), BorderLayout.CENTER);

      EventBus.getDefault().register(this);
   }

   @Subscribe
   public void onEvent(Object event) {
      System.out.println(event);
   }

   void showPopup(MouseEvent e, Supplier<Collection<Action>> actions) {
      SwingUtilities.invokeLater(() -> getPop(actions.get()).show(e.getComponent(), e.getX(), e.getY()));
   }

   void showPopup(Canvas canvas, PInputEvent e, Supplier<Collection<Action>> actions) {
      SwingUtilities.invokeLater(() -> {
         final Point2D canvasPosition = e.getCanvasPosition();
         SwingUtilities.invokeLater(() -> getPop(actions.get()).show(canvas, (int) canvasPosition.getX(), (int) canvasPosition.getY()));
      });
   }

   JPopupMenu getPop(Collection<Action> actions) {
      final JPopupMenu pop = new JPopupMenu();
      for (Action action : actions) pop.add(action);
      return pop;
   }

   Icon squareIcon(Color color) {
      if (iconCache.containsKey(color)) return iconCache.get(color);

      final BufferedImage bim = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
      final Graphics2D g2 = bim.createGraphics();
      g2.setPaint(color);
      g2.fillRect(0, 0, 16, 16);
      g2.dispose();

      final ImageIcon icon = new ImageIcon(bim);
      iconCache.put(color, icon);
      return icon;
   }

   void post(Object object) {
      EventBus.getDefault().post(object);
   }

   private void showUI() {
      SwingUtilities.invokeLater(() -> {
         pack();
         setLocationByPlatform(true);
         setVisible(true);
      });
   }

   public void runKeyAction(PInputEvent event, Collection<Action> actions) {

      final String keyText = String.join(" ",
         event.isControlDown() ? "control" : "",
         KeyEvent.getKeyText(event.getKeyCode())
      );

      actions.stream()
         .filter(action -> keyText.equals(action.getValue(Action.ACTION_COMMAND_KEY)))
         .findFirst()
         .ifPresent(action -> action.actionPerformed(null));
   }
}