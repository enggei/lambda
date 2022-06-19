package nextgen.lambda.domain.ui;

public class Canvas extends org.piccolo2d.PCanvas {

	public UI ui;
	public java.util.Map<Object, CanvasElement> elements = new java.util.concurrent.ConcurrentHashMap<>();
	public SelectEventsHandler selectEventHandler = new SelectEventsHandler();
	public CanvasZoomHandler canvasZoomHandler = new CanvasZoomHandler();
	public CanvasEventHandler canvasEventHandler = new CanvasEventHandler();
	public java.awt.Color highlightedColor = java.awt.Color.decode("#fdbf6f");
	public java.awt.Color selectedColor = java.awt.Color.decode("#ff7f00");

	public Canvas(UI ui) {
	    this.ui = ui;
	    setBackground(java.awt.Color.DARK_GRAY);
	}
}