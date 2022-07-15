package nextgen.lambda.ui.canvas;

public class CanvasInputListener extends org.piccolo2d.event.PBasicInputEventHandler {

   final Canvas canvas;
   public final CanvasSelectEventsHandler selectEventHandler;

   CanvasInputListener(nextgen.lambda.ui.canvas.Canvas canvas) {
      this.canvas = canvas;
      selectEventHandler = new CanvasSelectEventsHandler(canvas);
   }

   @Override
   public void mouseEntered(org.piccolo2d.event.PInputEvent event) {
      if (!this.equals(event.getInputManager().getKeyboardFocus())) {
         event.getInputManager().setKeyboardFocus(this);
         canvas.requestFocusInWindow();
      }
   }

   @Override
   public void mouseExited(org.piccolo2d.event.PInputEvent event) {
      event.getInputManager().setKeyboardFocus(null);
   }

   @Override
   public void mouseClicked(org.piccolo2d.event.PInputEvent event) {
      canvas.removeInputEventListener(selectEventHandler);
      if (!this.equals(event.getInputManager().getKeyboardFocus())) event.getInputManager().setKeyboardFocus(this);
      if (event.isRightMouseButton())
         nextgen.lambda.ui.UI.showPopup(canvas, event, canvas.actions());
      else
         javax.swing.SwingUtilities.invokeLater(() -> canvas.elements.values().forEach(canvasElement -> {
            canvasElement.unhighlight();
            canvasElement.unselect();
         }));
   }

   @Override
   public void keyPressed(org.piccolo2d.event.PInputEvent event) {
      if (event.isControlDown()) {
         canvas.removeInputEventListener(selectEventHandler);
         canvas.addInputEventListener(selectEventHandler.init(event));
      }

      nextgen.lambda.ACTIONS.runKeyAction(event, canvas.actions());
   }

   @Override
   public void keyReleased(org.piccolo2d.event.PInputEvent event) {
      if (event.getKeyCode() == java.awt.event.KeyEvent.VK_CONTROL) canvas.removeInputEventListener(selectEventHandler.end());
   }

   @Override
   public void mouseMoved(org.piccolo2d.event.PInputEvent event) {
      if (!event.isControlDown()) canvas.removeInputEventListener(selectEventHandler.end());
   }
}