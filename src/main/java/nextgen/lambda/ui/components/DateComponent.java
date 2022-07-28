package nextgen.lambda.ui.components;

import nextgen.lambda.*;
import nextgen.lambda.ui.*;

import javax.swing.*;
import java.awt.event.*;
import java.time.*;
import java.time.format.*;
import java.util.*;

public final class DateComponent extends AbstractComponent<javax.swing.JTextField, LocalDate> {

   static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MM yyyy");

   public DateComponent(String name) {
      super(name, new javax.swing.JTextField(formatter.format(LocalDate.now()), 15));

      component.addMouseListener(new MouseAdapter() {
         @Override
         public void mouseClicked(MouseEvent e) {
            UI.showPopup(component, e, actions());
         }
      });

   }

   private Collection<Action> actions() {
      final ArrayList<Action> actions = new ArrayList<>();
      actions.add(ACTIONS.newAction("now", () -> component().setText(formatter.format(LocalDate.now()))));
      actions.add(ACTIONS.newAction("yesterday", () -> component().setText(formatter.format(LocalDate.now().minusDays(1L)))));
      actions.add(ACTIONS.newAction("a week ago", () -> component().setText(formatter.format(LocalDate.now().minusWeeks(1l)))));
      actions.add(ACTIONS.newAction("a month ago", () -> component().setText(formatter.format(LocalDate.now().minusMonths(1L)))));
      actions.add(ACTIONS.newAction("a year ago", () -> component().setText(formatter.format(LocalDate.now().minusYears(1L)))));
      return actions;
   }

   @Override
   public Component<javax.swing.JTextField, LocalDate> value(LocalDate model) {
      component().setText(formatter.format(model));
      return this;
   }

   @Override
   public java.util.Optional<LocalDate> value() {
      final String value = component().getText().trim();
      return java.util.Optional.ofNullable(value.length() == 0 ? null : LocalDate.parse(value, formatter));
   }
}