package nextgen.lambda.ui.canvas;

public class CanvasTextLabelElement<T> extends CanvasElement<T> {

   final org.piccolo2d.nodes.PArea labelComponent;
   final org.piccolo2d.nodes.PText labelTextComponent = new org.piccolo2d.nodes.PText();
   final org.piccolo2d.nodes.PText textComponent = new org.piccolo2d.nodes.PText();

   public CanvasTextLabelElement(nextgen.lambda.ui.canvas.Canvas canvas, T model, java.awt.Color color, java.util.function.Function<T, String> renderer, java.awt.Color labelColor, java.util.function.Function<T, String> labelRenderer) {
      super(canvas, model);

      addAttribute("model.color", color);
      addAttribute("model.renderer", renderer);
      addAttribute("label.color", labelColor);
      addAttribute("label.renderer", labelRenderer);

      labelTextComponent.setFont(nextgen.lambda.ui.UI.font);
      labelTextComponent.setTextPaint(nextgen.lambda.ui.UI.getContrastColor(labelColor));
      labelTextComponent.setText(labelRenderer.apply(model));

      labelComponent = new org.piccolo2d.nodes.PArea(labelTextComponent.getFullBoundsReference());
      labelComponent.setStrokePaint(labelColor);
      labelComponent.setPaint(labelColor);
      labelComponent.addChild(labelTextComponent);
      addChild(labelComponent);

      textComponent.setFont(nextgen.lambda.ui.UI.font);
      textComponent.setText(renderer.apply(model));
      textComponent.setTextPaint(currentPaint(modelColor()));
      addChild(textComponent);
   }

   @Override
   public void refreshLater() {

      labelTextComponent.setTextPaint(nextgen.lambda.ui.UI.getContrastColor(labelColor()));
      labelTextComponent.setText(labelRenderer().apply(model()));

      labelComponent.setPaint(labelColor());
      labelComponent.setStrokePaint(labelColor());
      labelComponent.setWidth(labelTextComponent.getFullBoundsReference().getWidth());
      labelComponent.setHeight(labelTextComponent.getFullBoundsReference().getHeight());

      textComponent.setText(modelRenderer().apply(model()));
      textComponent.setTextPaint(currentPaint(modelColor()));

      labelTextComponent.invalidatePaint();
      labelComponent.invalidatePaint();
      textComponent.invalidatePaint();

      super.refreshLater();
   }

   @SuppressWarnings("unchecked")
   public java.util.function.Function<T, String> labelRenderer() {
      return (java.util.function.Function<T, String>) getAttribute("label.renderer");
   }

   public java.awt.Color labelColor() {
      return (java.awt.Color) getAttribute("label.color");
   }

   public String label() {return labelRenderer().apply(model());}

   @SuppressWarnings("unchecked")
   public java.util.function.Function<T, String> modelRenderer() {
      return (java.util.function.Function<T, String>) getAttribute("model.renderer");
   }

   public java.awt.Color modelColor() {
      return (java.awt.Color) getAttribute("model.color");
   }

   public String renderModel() {
      return modelRenderer().apply(model());
   }

   @Override
   public java.util.Collection<javax.swing.Action> actions() {
      final java.util.Collection<javax.swing.Action> actions = super.actions();
      actions.add(nextgen.lambda.ACTIONS.newAction("To Clipboard", "control C", () -> nextgen.lambda.ui.UI.toClipboard(modelRenderer().apply(model()))));
      return actions;
   }

   @Override
   protected void layoutChildren() {
      labelComponent.setOffset(labelComponent.getX(), 0);
      textComponent.setOffset(labelComponent.getX(), labelComponent.getFullBoundsReference().getHeight());
   }

   @Override
   public boolean intersects(java.awt.geom.Rectangle2D localBounds) {
      if (labelComponent.intersects(localBounds)) return true;
      if (textComponent.intersects(localBounds)) return true;
      return super.intersects(localBounds);
   }
}