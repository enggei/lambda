package nextgen.lambda.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Editor extends UIComponent {
   public Editor(UI ui) {
      super(ui);
      setBackground(ui().getBackground());
      add(new EditorPanel(this), BorderLayout.CENTER);
   }

   private class EditorPanel extends JTabbedPane {

      final Editor editor;
      final Map<Object, EditorComponent<?, ?>> editors = new ConcurrentHashMap<>();

      EditorPanel(Editor editor) {
         super();
         setOpaque(true);
         setBackground(ui().getBackground());
         this.editor = editor;
      }

      abstract class EditorComponent<T, C extends JComponent> extends JPanel {

         final EditorPanel editorPanel;
         final String title;
         final C component;
         final T model;

         EditorComponent(final EditorPanel editorPanel, String title, C component, T model) {
            super(new FlowLayout(FlowLayout.LEFT, 0, 0));
            this.editorPanel = editorPanel;
            this.title = title;
            this.component = component;
            this.model = model;

            javax.swing.JLabel label = new javax.swing.JLabel(title);
            label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
            add(label);

            setOpaque(false);

            addMouseListener(new MouseAdapter() {
               @Override
               public void mouseClicked(MouseEvent event) {
                  if (SwingUtilities.isRightMouseButton(event))
                     editorPanel.editor.ui.showPopup(event, this::actions);
                  else
                     SwingUtilities.invokeLater(() -> editorPanel.setSelectedComponent(component));
               }

               java.util.List<Action> actions() {
                  return java.util.List.of(
                     close(),
                     closeOthers(),
                     closeAll()
                  );
               }
            });
         }

         Action close() {
            return new AbstractAction("Close") {
               @Override
               public void actionPerformed(ActionEvent actionEvent) {
                  javax.swing.SwingUtilities.invokeLater(() -> {
                     editors.remove(model);
                     editor.remove(component);
                  });
               }
            };
         }

         Action closeOthers() {
            return new AbstractAction("Close others") {
               @Override
               public void actionPerformed(ActionEvent actionEvent) {
                  new java.util.LinkedHashSet<>(editors.values())
                     .forEach(other -> {
                        if (other.equals(EditorComponent.this)) return;
                        editors.remove(model);
                        editor.remove(component);
                     });
               }
            };
         }

         Action closeAll() {
            return new AbstractAction("Close all") {
               @Override
               public void actionPerformed(ActionEvent actionEvent) {
                  new java.util.LinkedHashSet<>(editors.values())
                     .forEach(other -> {
                        editors.remove(model);
                        editor.remove(component);
                     });
               }
            };
         }
      }
   }
}