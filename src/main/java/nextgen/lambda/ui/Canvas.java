package nextgen.lambda.ui;

import org.piccolo2d.*;
import org.piccolo2d.event.*;
import org.piccolo2d.nodes.PPath;
import org.piccolo2d.util.PBounds;

import javax.swing.Action;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.awt.Color.*;
import static javax.swing.SwingUtilities.invokeLater;

public class Canvas extends UIComponent {
   public Canvas(UI ui) {
      super(ui);
      setBackground(ui().getBackground());
      add(new ContextCanvas(), BorderLayout.CENTER);
   }

   private class ContextCanvas extends PCanvas {

      final Map<Object, CanvasElement> nodes = new ConcurrentHashMap<>();
      final SelectEventsHandler selectEventHandler = new SelectEventsHandler();
      final CanvasZoomHandler canvasZoomHandler = new CanvasZoomHandler();
      final CanvasEventHandler canvasEventHandler = new CanvasEventHandler();
      final Color highlightedColor = decode("#fdbf6f");
      final Color selectedColor = decode("#ff7f00");

      ContextCanvas() {
         setBackground(ui().getBackground());
         setMinimumSize(ui().getPreferredSize());
      }

      Collection<Action> actions() {
         return new ArrayList<>();
      }

      class CanvasElement extends PNode {

         final ContextCanvas canvas;
         final CanvasElementEventHandler canvasElementEventHandler = new CanvasElementEventHandler();

         CanvasElement(ContextCanvas canvas, Object model, String color) {
            this.canvas = canvas;
            addAttribute("model", model);
            addAttribute("model.color", decode(color));
            addAttribute("highlighted.color", canvas.highlightedColor);
            addAttribute("selected.color", canvas.selectedColor);
            addInputEventListener(canvasElementEventHandler);
            addInputEventListener(canvas.canvasZoomHandler);
         }

         Collection<Action> actions() {
            return new ArrayList<>();
         }

         boolean selected() {
            return (boolean) getAttribute("selected", false);
         }

         protected Color currentPaint() {
            final Boolean highlight = (Boolean) getAttribute("highlighted", Boolean.FALSE);
            final Boolean selected = (Boolean) getAttribute("selected", Boolean.FALSE);
            return highlight ? highlightColor() : selected ? selectedColor() : modelColor();
         }

         Color highlightColor() {
            return (Color) getAttribute("highlighted.color");
         }

         Color selectedColor() {
            return (Color) getAttribute("selected.color");
         }

         Color modelColor() {
            return (Color) getAttribute("model.color");
         }

         void unselect() {
            addAttribute("selected", false);
            refreshLater();
         }

         void select() {
            addAttribute("selected", true);
            refreshLater();
         }

         void unhighlight() {
            addAttribute("highlighted", false);
            refreshLater();
         }

         void highlight() {
            addAttribute("highlighted", true);
            refreshLater();
         }

         void refreshLater() {
            invokeLater(() -> {
               invalidateFullBounds();
               invalidatePaint();
               repaint();
            });
         }

         private final class CanvasElementEventHandler extends PDragSequenceEventHandler {

            private CanvasElementEventHandler() {
               getEventFilter().setMarksAcceptedEventsAsHandled(true);
            }

            @Override
            protected void startDrag(PInputEvent event) {
               super.startDrag(event);
            }

            @Override
            protected void drag(PInputEvent event) {
               super.drag(event);
               translate(event.getDelta().width, event.getDelta().height);
            }

            @Override
            protected void endDrag(PInputEvent event) {
               super.endDrag(event);
            }

            @Override
            protected boolean shouldStartDragInteraction(PInputEvent event) {
               return super.shouldStartDragInteraction(event);
            }

            @Override
            public void mouseEntered(PInputEvent event) {
               if (!event.isControlDown()) event.getInputManager().setKeyboardFocus(this);
               highlight();
            }

            @Override
            public void mouseExited(PInputEvent event) {
               unhighlight();
               if (!event.isControlDown())
                  event.getInputManager().setKeyboardFocus(canvas.canvasEventHandler);
            }

            @Override
            public void mouseClicked(PInputEvent event) {
               if (event.isRightMouseButton())
                  ui().showPopup(Canvas.this, event, CanvasElement.this::actions);
               else if (event.isLeftMouseButton())
                  invokeLater(() -> {
                     if (selected()) unselect();
                     else select();
                  });
            }

            @Override
            public void keyPressed(PInputEvent event) {
               ui().runKeyAction(event, actions());
            }
         }
      }

      private final class SelectEventsHandler extends PBasicInputEventHandler {

         private PPath selectionRectangle;
         private boolean isDragging = true;
         private double startX;
         private double startY;
         private final PLayer selectionLayer = getLayer();

         PInputEventListener init(PInputEvent event) {
            isDragging = event.isControlDown();
            if (selectionRectangle != null) selectionLayer.removeChild(selectionRectangle);
            startX = getCamera().localToView(event.getCanvasPosition()).getX();
            startY = getCamera().localToView(event.getCanvasPosition()).getY();
            selectionRectangle = PPath.createRectangle(startX, startY, 1, 1);
            selectionRectangle.setTransparency(.5f);
            selectionLayer.addChild(selectionRectangle);
            return this;
         }

         @Override
         public void mouseMoved(PInputEvent event) {
            if (isDragging) {
               final double eventX = getCamera().localToView(event.getCanvasPosition()).getX();
               final double eventY = getCamera().localToView(event.getCanvasPosition()).getY();
               final boolean left = eventX < startX;
               selectionRectangle.setX(left ? eventX : startX);
               selectionRectangle.setWidth(left ? (startX - eventX) : (eventX - startX));
               final boolean top = eventY < startY;
               selectionRectangle.setY(top ? eventY : startY);
               selectionRectangle.setHeight(top ? (startY - eventY) : (eventY - startY));
               invokeLater(() -> {
                  final PBounds fullBounds = selectionRectangle.getFullBounds();
                  invokeLater(() -> {
                     nodes.values().forEach(CanvasElement::unselect);
                     nodes.values().stream().filter(n -> fullBounds.contains(n.getFullBounds())).forEach(CanvasElement::select);
                  });
               });
            }
         }

         PInputEventListener end() {
            isDragging = false;
            if (selectionRectangle != null) selectionLayer.removeChild(selectionRectangle);
            return this;
         }
      }

      private final class CanvasEventHandler extends PBasicInputEventHandler {

         @Override
         public void mouseEntered(PInputEvent event) {
            if (!this.equals(event.getInputManager().getKeyboardFocus())) {
               event.getInputManager().setKeyboardFocus(this);
               requestFocusInWindow();
            }
         }

         @Override
         public void mouseExited(PInputEvent event) {
            event.getInputManager().setKeyboardFocus(null);
         }

         @Override
         public void mouseClicked(PInputEvent event) {
            removeInputEventListener(selectEventHandler);
            if (!this.equals(event.getInputManager().getKeyboardFocus())) event.getInputManager().setKeyboardFocus(this);
            if (event.isRightMouseButton())
               ui().showPopup(Canvas.this, event, ContextCanvas.this::actions);
            else
               invokeLater(() -> nodes.values().forEach(canvasElement -> {
                  canvasElement.unhighlight();
                  canvasElement.unselect();
               }));
         }

         @Override
         public void keyPressed(PInputEvent event) {
            if (event.isControlDown()) {
               removeInputEventListener(selectEventHandler);
               addInputEventListener(selectEventHandler.init(event));
            }

            ui().runKeyAction(event, actions());
         }

         @Override
         public void keyReleased(PInputEvent event) {
            if (event.getKeyCode() == KeyEvent.VK_CONTROL) removeInputEventListener(selectEventHandler.end());
         }

         @Override
         public void mouseMoved(PInputEvent event) {
            if (!event.isControlDown()) removeInputEventListener(selectEventHandler.end());
         }
      }

      private final class CanvasZoomHandler extends PBasicInputEventHandler {

         CanvasZoomHandler() {
            super();
            final PInputEventFilter eventFilter = new PInputEventFilter();
            eventFilter.rejectAllEventTypes();
            eventFilter.setAcceptsMouseWheelRotated(true);
            setEventFilter(eventFilter);
         }

         @Override
         public void mouseWheelRotated(final PInputEvent event) {
            scale(event.getWheelRotation(), event.getPosition(), event.getCamera());
         }

         private void scale(int wheelRotation, Point2D viewAboutPoint, PCamera camera) {
            double maxZoomScale = 2.0d;
            double minZomScale = 0.025d;
            if ((camera.getViewScale() < minZomScale && wheelRotation < 0) || (camera.getViewScale() > maxZoomScale && wheelRotation > 0)) return;
            double scaleFactor = 0.05d;
            final double scale = 1.0d + wheelRotation * scaleFactor;
            camera.scaleViewAboutPoint(scale, viewAboutPoint.getX(), viewAboutPoint.getY());
         }
      }
   }


}