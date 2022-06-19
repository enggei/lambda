package nextgen.lambda.domain.ui;

public class Canvas extends org.piccolo2d.PCanvas {

	public UI ui;
	public java.util.Map<Object,CanvasElement> elements;
	public SelectEventsHandler selectEventHandler;
	public CanvasZoomHandler canvasZoomHandler;
	public CanvasEventHandler canvasEventHandler;
	public java.awt.Color highlightedColor;
	public java.awt.Color selectedColor;

	public Canvas(UI ui) {
	    this.ui = ui;
	    setBackground(java.awt.Color.DARK_GRAY);
	}
}