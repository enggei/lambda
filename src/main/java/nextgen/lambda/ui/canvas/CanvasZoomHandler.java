package nextgen.lambda.ui.canvas;

public class CanvasZoomHandler extends org.piccolo2d.event.PBasicInputEventHandler {

   static final private double maxZoomScale = 2.0d;
   static final private double minZomScale = 0.025d;
   static final private double scaleFactor = 0.05d;

   CanvasZoomHandler() {
      super();
      final org.piccolo2d.event.PInputEventFilter eventFilter = new org.piccolo2d.event.PInputEventFilter();
      eventFilter.rejectAllEventTypes();
      eventFilter.setAcceptsMouseWheelRotated(true);
      setEventFilter(eventFilter);
   }

   @Override
   public void mouseWheelRotated(final org.piccolo2d.event.PInputEvent event) {
      scale(event.getWheelRotation(), event.getPosition(), event.getCamera());
   }

   private void scale(int wheelRotation, java.awt.geom.Point2D viewAboutPoint, org.piccolo2d.PCamera camera) {
      if ((camera.getViewScale() < minZomScale && wheelRotation < 0) || (camera.getViewScale() > maxZoomScale && wheelRotation > 0)) return;
      final double scale = 1.0d + wheelRotation * scaleFactor;
      camera.scaleViewAboutPoint(scale, viewAboutPoint.getX(), viewAboutPoint.getY());
   }
}