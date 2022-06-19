package nextgen.lambda.ui;

import javax.swing.JPanel;
import java.awt.BorderLayout;

public class UIComponent extends JPanel {
   final UI ui;
   UIComponent(UI ui) {
      super(new BorderLayout(), true);
      this.ui = ui;
      setSize(ui.getPreferredSize());
      setMinimumSize(ui.getMinimumSize());
      setPreferredSize(ui.getPreferredSize());
      setMaximumSize(ui.getMaximumSize());
   }

   UI ui() {
      return ui;
   }
}