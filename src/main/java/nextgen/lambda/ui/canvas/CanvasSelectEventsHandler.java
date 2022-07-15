package nextgen.lambda.ui.canvas;

import static javax.swing.SwingUtilities.invokeLater;

public class CanvasSelectEventsHandler extends org.piccolo2d.event.PBasicInputEventHandler {

   final Canvas canvas;
   private org.piccolo2d.nodes.PPath selectionRectangle;
   private boolean isDragging = true;
   private double startX;
   private double startY;
   private final org.piccolo2d.PLayer selectionLayer;

   public CanvasSelectEventsHandler(nextgen.lambda.ui.canvas.Canvas canvas) {
      this.canvas = canvas;
      selectionLayer = canvas.getLayer();
   }

   org.piccolo2d.event.PInputEventListener init(org.piccolo2d.event.PInputEvent event) {
      isDragging = event.isControlDown();
      if (selectionRectangle != null) selectionLayer.removeChild(selectionRectangle);
      startX = canvas.getCamera().localToView(event.getCanvasPosition()).getX();
      startY = canvas.getCamera().localToView(event.getCanvasPosition()).getY();
      selectionRectangle = org.piccolo2d.nodes.PPath.createRectangle(startX, startY, 1, 1);
      selectionRectangle.setTransparency(.5f);
      selectionLayer.addChild(selectionRectangle);
      return this;
   }

   @Override
   public void mouseMoved(org.piccolo2d.event.PInputEvent event) {
      if (isDragging) {
         final double eventX = canvas.getCamera().localToView(event.getCanvasPosition()).getX();
         final double eventY = canvas.getCamera().localToView(event.getCanvasPosition()).getY();
         final boolean left = eventX < startX;
         selectionRectangle.setX(left ? eventX : startX);
         selectionRectangle.setWidth(left ? (startX - eventX) : (eventX - startX));
         final boolean top = eventY < startY;
         selectionRectangle.setY(top ? eventY : startY);
         selectionRectangle.setHeight(top ? (startY - eventY) : (eventY - startY));
         invokeLater(() -> {
            final org.piccolo2d.util.PBounds fullBounds = selectionRectangle.getFullBounds();
            invokeLater(() -> {
               canvas.elements.values().forEach(CanvasElement::unselect);
               canvas.elements.values().stream().filter(n -> fullBounds.contains(n.getFullBounds())).forEach(CanvasElement::select);
            });
         });
      }
   }

   org.piccolo2d.event.PInputEventListener end() {
      isDragging = false;
      if (selectionRectangle != null) selectionLayer.removeChild(selectionRectangle);
      return this;
   }
}