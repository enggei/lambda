package nextgen.lambda.ui.components;

public class MethodDeclarationComponent extends nextgen.lambda.ui.forms.Form {

   private final java.lang.reflect.Method method;
   private final java.util.Map<java.lang.reflect.Parameter, javax.swing.JTextField> parameterNames = new java.util.LinkedHashMap<>();
   private final javax.swing.JTextArea textArea = new javax.swing.JTextArea(10, 50);

   public MethodDeclarationComponent(java.lang.reflect.Method method) {
      this.method = method;

      newColumn().left().pref().grow();
      newRow().center().pref().grow().label(method.getName());

      java.util.Arrays.stream(method.getParameters()).forEach(parameter -> {
         parameterNames.put(parameter, new javax.swing.JTextField(getDefaultName(parameter)));
         newRow().center().pref().none()
            .add(nextgen.lambda.ui.forms.Form.newForm()
               .newLeftPrefGrow()
               .newLeftPrefGrow()
               .newCenterPrefNone().label(parameter.getType().getSimpleName()).add(parameterNames.get(parameter))
               .builder()
               .build());
      });

      newRow().center().pref().grow().addScrollable(textArea);
   }

   private String getDefaultName(java.lang.reflect.Parameter parameter) {
      final String s = parameter.getType().getSimpleName();
      return Character.toLowerCase(s.charAt(0)) + s.substring(1);
   }

   public java.util.Optional<nextgen.lambda.modules.templates.java.classMethodBuilder> model() {

      final nextgen.lambda.modules.templates.java.classMethodBuilder overridden = nextgen.lambda.modules.templates.java.JavaGroup.new_classMethodBuilder()
         .add_annotations("@Override")
         .set_name(method.getName())
         .set_scope(java.lang.reflect.Modifier.toString(method.getModifiers()).replaceAll("abstract", "").trim())
         .set_type(method.getReturnType().getCanonicalName());

      for (int i = 0; i < method.getParameters().length; i++) {
         java.lang.reflect.Parameter parameter = method.getParameters()[i];
         overridden.add_parameters(nextgen.lambda.modules.templates.java.JavaGroup.new_parameterBuilder()
            .set_type(parameter.getType().getCanonicalName())
            .set_name(parameterNames.get(parameter).getText().trim()));
      }

      final String[] lines = textArea.getText().trim().split("[\r?\n]");
      for (String line : lines) overridden.add_statements(line.trim());

      return java.util.Optional.of(overridden);
   }
}
