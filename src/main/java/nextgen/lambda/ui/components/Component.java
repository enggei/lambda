package nextgen.lambda.ui.components;

import java.time.*;

public interface Component<C extends javax.swing.JComponent, T> {

   String name();

   C component();

   Component<C, T> value(T model);

   java.util.Optional<T> value();

   @SuppressWarnings("unchecked")
   static <T> java.util.Optional<Component<?, T>> component(T object, String name) {

      final Class<?> aClass = nextgen.lambda.OBJECTS.classFor(object);
      final String simpleName = aClass.getSimpleName();
      if (aClass.isAssignableFrom(String.class)) {
         final boolean singleLine = !object.toString().contains("\n");
         if (singleLine)
            return java.util.Optional.of((Component<?, T>) new StringComponent(name));
         else
            return java.util.Optional.of((Component<?, T>) new TextComponent(name));
      } else if ("int".equals(simpleName)) {
         return java.util.Optional.of((Component<?, T>) new nextgen.lambda.ui.components.IntegerComponent(name));
      } else if ("long".equals(simpleName)) {
         return java.util.Optional.of((Component<?, T>) new nextgen.lambda.ui.components.LongComponent(name));
      } else if ("float".equals(simpleName)) {
         return java.util.Optional.of((Component<?, T>) new nextgen.lambda.ui.components.FloatComponent(name));
      } else if ("short".equals(simpleName)) {
         return java.util.Optional.of((Component<?, T>) new nextgen.lambda.ui.components.ShortComponent(name));
      } else if ("char".equals(simpleName)) {
         return java.util.Optional.of((Component<?, T>) new nextgen.lambda.ui.components.CharacterComponent(name));
      } else if ("bool".equals(simpleName)) {
         return java.util.Optional.of((Component<?, T>) new nextgen.lambda.ui.components.BooleanComponent(name));
      } else if (LocalDate.class.equals(aClass)) {
         return java.util.Optional.of((Component<?, T>) new DateComponent(name));
      }
      return java.util.Optional.empty();
   }
}