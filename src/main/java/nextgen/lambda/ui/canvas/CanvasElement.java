package nextgen.lambda.ui.canvas;

public class CanvasElement<T> extends org.piccolo2d.PNode {

   final CanvasElementInputListener canvasElementInputListener;

   public CanvasElement(Canvas canvas, T model) {
      canvasElementInputListener = new CanvasElementInputListener(this);
      addAttribute("canvas", canvas);
      addAttribute("model", model);
      addAttribute("highlighted.color", canvas.highlightedColor);
      addAttribute("selected.color", canvas.selectedColor);
      addInputEventListener(canvasElementInputListener);
      addInputEventListener(canvas.canvasZoomHandler);
   }

   public nextgen.lambda.ui.canvas.Canvas canvas() {
      return (Canvas) getAttribute("canvas");
   }

   @SuppressWarnings("unchecked")
   public T model() {
      return (T) getAttribute("model");
   }

   public java.util.Collection<javax.swing.Action> actions() {
      final java.util.ArrayList<javax.swing.Action> actions = new java.util.ArrayList<>();
      canvas().ui.context().modules().forEach(lambdaModule -> actions.addAll(lambdaModule.objectActions(model())));

      actions.add(nextgen.lambda.ACTIONS.newAction("Close", "C", () -> canvas().closeModels(java.util.Collections.singleton(model()))));
      actions.add(nextgen.lambda.ACTIONS.newAction("Retain", "control R", () -> canvas().closeModels(canvas().elements.values().stream().filter(canvasElement -> !canvasElement.equals(CanvasElement.this)).map(nextgen.lambda.ui.canvas.CanvasElement::model).collect(java.util.stream.Collectors.toSet()))));

      return actions;
   }

   public java.awt.Color highlightColor() {
      return (java.awt.Color) getAttribute("highlighted.color");
   }

   public java.awt.Color selectedColor() {
      return (java.awt.Color) getAttribute("selected.color");
   }

   public boolean selected() {
      return (boolean) getAttribute("selected", false);
   }

   public void unselect() {
      addAttribute("selected", false);
      refreshLater();
   }

   public void select() {
      addAttribute("selected", true);
      refreshLater();
   }

   public void unhighlight() {
      addAttribute("highlighted", false);
      refreshLater();
   }

   public CanvasElement<?> highlight() {
      addAttribute("highlighted", true);
      refreshLater();
      return this;
   }

   public java.awt.Color currentPaint(java.awt.Color modelColor) {
      final Boolean highlight = (Boolean) getAttribute("highlighted", Boolean.FALSE);
      final Boolean selected = (Boolean) getAttribute("selected", Boolean.FALSE);
      return highlight ? highlightColor() : selected ? selectedColor() : modelColor;
   }

   public void refreshLater() {
      invalidateFullBounds();
      invalidatePaint();
      repaint();
   }
}