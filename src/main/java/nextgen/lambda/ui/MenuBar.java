package nextgen.lambda.ui;

public class MenuBar extends javax.swing.JMenuBar {

   public MenuBar(nextgen.lambda.ui.UI ui, javax.swing.JSplitPane canvas_editor, javax.swing.JSplitPane navigator_canvas_editor) {
      add(new javax.swing.JButton(nextgen.lambda.ACTIONS.newAction("Default", () -> {
         navigator_canvas_editor.setDividerLocation(0.1);
         canvas_editor.setDividerLocation(0.5);
      })));
      add(new javax.swing.JButton(nextgen.lambda.ACTIONS.newAction("Navigator", () -> {
         navigator_canvas_editor.setDividerLocation(0.5);
         canvas_editor.setDividerLocation(0.1);
      })));
      add(new javax.swing.JButton(nextgen.lambda.ACTIONS.newAction("Canvas", () -> {
         navigator_canvas_editor.setDividerLocation(0.1);
         canvas_editor.setDividerLocation(0.9);
      })));
      add(new javax.swing.JButton(nextgen.lambda.ACTIONS.newAction("Editor", () -> {
         navigator_canvas_editor.setDividerLocation(0.1);
         canvas_editor.setDividerLocation(0.1);
      })));
   }
}