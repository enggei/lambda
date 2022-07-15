package nextgen.lambda.ui.canvas;

import static javax.swing.SwingUtilities.*;

public class Canvas extends org.piccolo2d.PCanvas {

   private static final java.util.logging.Logger log = nextgen.lambda.LOG.logger(Canvas.class);

   public nextgen.lambda.ui.UI ui;
   public CanvasInputListener canvasInputListener;
   public java.util.Map<Object, CanvasElement<?>> elements = new java.util.concurrent.ConcurrentHashMap<>();
   public CanvasZoomHandler canvasZoomHandler = new CanvasZoomHandler();
   public java.awt.Color highlightedColor = java.awt.Color.decode("#fdbf6f");
   public java.awt.Color selectedColor = java.awt.Color.decode("#ff7f00");

   public Canvas(nextgen.lambda.ui.UI ui) {
      this.ui = ui;
      setFont(nextgen.lambda.ui.UI.font);
      setBackground(ui.getBackground());
      canvasInputListener = new CanvasInputListener(this);
      removeInputEventListener(getZoomEventHandler());
      addInputEventListener(canvasZoomHandler);
      addInputEventListener(canvasInputListener);

      nextgen.lambda.EVENTS.register(this);
   }

   public CanvasElement<?> merge(Object model, java.util.function.Supplier<CanvasElement<?>> supplier) {

      if (elements.containsKey(model)) {
         return elements.get(model).highlight();
      } else {
         final CanvasElement<?> canvasElement = supplier.get();
         elements.put(model, canvasElement);
         getLayer().addChild(canvasElement);
         refreshLater();
         return canvasElement;
      }
   }

   public CanvasElement<?> mergeAndCenter(Object model, java.util.function.Supplier<CanvasElement<?>> supplier) {
      return center(merge(model, supplier));
   }

   public CanvasElement<?> center(CanvasElement<?> canvasElement) {
      javax.swing.SwingUtilities.invokeLater(() -> {
         final org.piccolo2d.util.PBounds bounds = canvasElement.getGlobalFullBounds();
         getCamera().animateViewToCenterBounds(new java.awt.geom.Rectangle2D.Double(bounds.getX(), bounds.getY(), 10, 10), false, 500);
      });
      return canvasElement;
   }

   public java.util.Collection<javax.swing.Action> actions() {
      final java.util.ArrayList<javax.swing.Action> actions = new java.util.ArrayList<>();

      actions.add(nextgen.lambda.ACTIONS.newAction("From Clipboard", "control V", () -> nextgen.lambda.ui.UI.fromClipboard().ifPresent(clipboard -> nextgen.lambda.EVENTS.open(new nextgen.lambda.types.Text(clipboard)))));

      actions.add(nextgen.lambda.ACTIONS.newAction("Selected to Stream", () -> nextgen.lambda.EVENTS.open(selectedModels())));
      actions.add(nextgen.lambda.ACTIONS.newAction("Selected to Set", () -> nextgen.lambda.EVENTS.open(selectedModels().collect(java.util.stream.Collectors.toSet()))));
      actions.add(nextgen.lambda.ACTIONS.newAction("Selected to List", () -> nextgen.lambda.EVENTS.open(selectedModels().collect(java.util.stream.Collectors.toList()))));
      actions.add(nextgen.lambda.ACTIONS.newAction("Selected to Objects[]", () -> nextgen.lambda.EVENTS.open(selectedModels().toArray())));

      actions.add(nextgen.lambda.ACTIONS.newAction("Select All", "control A", () -> elements().forEach(nextgen.lambda.ui.canvas.CanvasElement::select)));
      actions.add(nextgen.lambda.ACTIONS.newAction("Close Selected", "control C", () -> closeModels(elements().stream().filter(nextgen.lambda.ui.canvas.CanvasElement::selected).map(nextgen.lambda.ui.canvas.CanvasElement::model).collect(java.util.stream.Collectors.toSet()))));
      actions.add(nextgen.lambda.ACTIONS.newAction("Retain Selected", "control R", () -> closeModels(elements().stream().filter(canvasElement -> !canvasElement.selected()).map(nextgen.lambda.ui.canvas.CanvasElement::model).collect(java.util.stream.Collectors.toSet()))));

      actions.add(nextgen.lambda.ACTIONS.newAction("Layout Vertically", "1", () -> {

         final java.awt.geom.Point2D startPosition = mousePosition();
         final java.awt.geom.Point2D currentPosition = new java.awt.Point((int) startPosition.getX(), (int) startPosition.getY());
         elements()
            .stream().filter(nextgen.lambda.ui.canvas.CanvasElement::selected)
            .forEach(n -> {
               n.setOffset(currentPosition.getX(), currentPosition.getY());
               currentPosition.setLocation(currentPosition.getX(), currentPosition.getY() + n.getFullBounds().getHeight());
            });

      }));
      actions.add(nextgen.lambda.ACTIONS.newAction("Layout Horizontally", "2", () -> {

         final java.awt.geom.Point2D startPosition = mousePosition();
         final java.awt.geom.Point2D currentPosition = new java.awt.Point((int) startPosition.getX(), (int) startPosition.getY());
         elements()
            .stream().filter(nextgen.lambda.ui.canvas.CanvasElement::selected)
            .forEach(n -> {
               n.setOffset(currentPosition.getX(), currentPosition.getY());
               currentPosition.setLocation(currentPosition.getX() + n.getFullBounds().getWidth(), currentPosition.getY());
            });

      }));
      return actions;
   }

   public java.util.stream.Stream<Object> selectedModels() {
      return elements.entrySet().stream().filter(entry -> entry.getValue().selected()).map(java.util.Map.Entry::getKey);
   }

   public java.util.Collection<nextgen.lambda.ui.canvas.CanvasElement<?>> elements() {
      return elements.values();
   }

   public java.awt.Point mousePosition() {
      final java.awt.Point mousePosition = getMousePosition();
      if (mousePosition == null) return centerPosition();
      final java.awt.geom.Point2D localToView = getCamera().localToView(mousePosition);
      return new java.awt.Point((int) localToView.getX(), (int) localToView.getY());
   }

   public java.awt.Point centerPosition() {
      final java.awt.geom.Point2D center2D = getCamera().getViewBounds().getCenter2D();
      return new java.awt.Point((int) center2D.getX(), (int) center2D.getY());
   }

   void refreshLater() {
      invokeLater(() -> {
         invalidate();
         repaint();
      });
   }

   public void closeModels(java.util.Set<Object> models) {
      log.info("close " + models.size() + " " + models.stream().map(Object::toString).collect(java.util.stream.Collectors.joining(" ")));
      new java.util.ArrayList<>(elements.entrySet()).stream()
         .filter(entry -> models.contains(entry.getKey()))
         .forEach(entry -> {
            elements.remove(entry.getKey());
            getLayer().removeChild(entry.getValue());
         });

      refreshLater();
   }

   public java.util.Set<Object> allModels() {
      return elements().stream().map(nextgen.lambda.ui.canvas.CanvasElement::model).collect(java.util.stream.Collectors.toSet());
   }

   public java.util.Set<Class<?>> allTypes() {return allModels().stream().map(Object::getClass).collect(java.util.stream.Collectors.toSet());}
}