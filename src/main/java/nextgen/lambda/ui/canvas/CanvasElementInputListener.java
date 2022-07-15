package nextgen.lambda.ui.canvas;

import static javax.swing.SwingUtilities.invokeLater;

public class CanvasElementInputListener extends org.piccolo2d.event.PDragSequenceEventHandler {

   final CanvasElement<?> canvasElement;
   CanvasElementInputListener(nextgen.lambda.ui.canvas.CanvasElement<?> canvasElement) {
      this.canvasElement = canvasElement;
      getEventFilter().setMarksAcceptedEventsAsHandled(true);
   }

   @Override
   protected void startDrag(org.piccolo2d.event.PInputEvent event) {
      super.startDrag(event);
   }

   @Override
   protected void drag(org.piccolo2d.event.PInputEvent event) {
      super.drag(event);
      canvasElement.translate(event.getDelta().width, event.getDelta().height);
   }

   @Override
   protected void endDrag(org.piccolo2d.event.PInputEvent event) {
      super.endDrag(event);
   }

   @Override
   protected boolean shouldStartDragInteraction(org.piccolo2d.event.PInputEvent event) {
      return super.shouldStartDragInteraction(event);
   }

   @Override
   public void mouseEntered(org.piccolo2d.event.PInputEvent event) {
      if (!event.isControlDown()) event.getInputManager().setKeyboardFocus(this);
      canvasElement.highlight();
   }

   @Override
   public void mouseExited(org.piccolo2d.event.PInputEvent event) {
      canvasElement.unhighlight();
      if (!event.isControlDown())
         event.getInputManager().setKeyboardFocus(canvasElement.canvas().canvasInputListener);
   }

   @Override
   public void mouseClicked(org.piccolo2d.event.PInputEvent event) {
      if (event.isRightMouseButton())
         nextgen.lambda.ui.UI.showPopup(canvasElement.canvas(), event, canvasElement.actions());
      else if (event.isLeftMouseButton())
         invokeLater(() -> {
            if (canvasElement.selected()) canvasElement.unselect();
            else canvasElement.select();
         });
   }

   @Override
   public void keyReleased(org.piccolo2d.event.PInputEvent event) {
      nextgen.lambda.ACTIONS.runKeyAction(event, canvasElement.actions());
   }
}