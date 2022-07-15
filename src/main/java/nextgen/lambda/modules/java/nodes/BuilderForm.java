package nextgen.lambda.modules.java.nodes;

import static java.util.Optional.*;
import static nextgen.lambda.modules.java.Java.*;

public class BuilderForm extends nextgen.lambda.ui.forms.Form {


   private final java.util.function.Function<nextgen.lambda.ui.forms.Form, java.util.Optional<String>> predicate;

   public BuilderForm(javax.swing.JComponent owner) {
      super(owner);

      final java.util.Set<nextgen.lambda.modules.java.JavaType> javaTypes = nextgen.lambda.modules.java.JavaTypes.get()
         .lang()
         .util()
         .build();

      newLeftPrefNone();
      newLeftPrefGrow();

      newCenterPrefNone().label("name").textField("name");
      newCenterPrefNone().label("scope").textField("scope", "public");

      final java.util.List<String> fieldDeclarations = new java.util.ArrayList<>();
      newCenterPrefNone().label("fields").button(nextgen.lambda.ACTIONS.newAction("add", () -> {

         final nextgen.lambda.ui.forms.Form fieldForm = nextgen.lambda.ui.forms.Form.newForm()
            .newLeftPrefNone()
            .newLeftPrefNone()
            .newLeftPrefNone()
            .newLeftPrefNone()
            .newLeftPrefNone();

         for (final nextgen.lambda.modules.java.JavaType type : javaTypes) {
            final nextgen.lambda.ui.forms.FormRow fieldRow = fieldForm.newCenterPrefNone().textField("type", type.canonicalName()).textField("name");
            fieldRow.button("init", nextgen.lambda.ACTIONS.newAction("init", () -> nextgen.lambda.ui.UI.selectOrNew(owner, "init", type.constructors(), s -> s)
               .ifPresent(selected -> fieldRow.getButton("init").ifPresent(component -> component.value(selected)))
            ));

            type.one().ifPresent(one -> fieldRow
               .button("ONE", nextgen.lambda.ACTIONS.newAction("ONE", () -> nextgen.lambda.ui.UI.selectOrNew(owner, "type", javaTypes, nextgen.lambda.modules.java.JavaType::new)
                  .ifPresent(selected -> fieldRow.getButton("ONE").ifPresent(component -> component.value(selected)))
               )));

            type.two().ifPresent(two -> fieldRow
               .button("TWO", nextgen.lambda.ACTIONS.newAction("TWO", () -> nextgen.lambda.ui.UI.selectOrNew(owner, "type", javaTypes, nextgen.lambda.modules.java.JavaType::new)
                  .ifPresent(selected -> fieldRow.getButton("TWO").ifPresent(component -> component.value(selected)))
               )));
            type.three().ifPresent(two -> fieldRow
               .button("THREE", nextgen.lambda.ACTIONS.newAction("THREE", () -> nextgen.lambda.ui.UI.selectOrNew(owner, "type", javaTypes, nextgen.lambda.modules.java.JavaType::new)
                  .ifPresent(selected -> fieldRow.getButton("THREE").ifPresent(component -> component.value(selected)))
               )));
         }

         java.util.function.Function<nextgen.lambda.ui.forms.Form, java.util.Optional<java.util.Set<String>>> fieldFormPredicate = f -> {

            final java.util.Set<String> declarations = new java.util.TreeSet<>();

            fieldForm.rows().forEach(row -> {
               row.getTextField("name").flatMap(nextgen.lambda.ui.components.StringComponent::value).ifPresent(names -> {
                  final String[] ss = names.split("[ ,]");
                  for (String item : ss) {
                     String s = item.trim();
                     if (s.length() == 0) continue;

                     final nextgen.lambda.modules.templates.java.classFieldBuilder field = nextgen.lambda.modules.templates.java.JavaGroup.new_classFieldBuilder()
                        .set_scope("public")
                        .set_name(s);

                     final nextgen.lambda.modules.templates.java.classMethodBuilder setter = nextgen.lambda.modules.templates.java.JavaGroup.new_classMethodBuilder();
                     row(0).getTextField("name").flatMap(nextgen.lambda.ui.components.StringComponent::value).ifPresent(setter::set_type);
                     setter.set_name(s);
                     setter.set_scope("public");

                     final nextgen.lambda.modules.templates.java.classMethodBuilder getter = nextgen.lambda.modules.templates.java.JavaGroup.new_classMethodBuilder();
                     getter.set_name(s);
                     getter.set_scope("public");


                     row
                        .getTextField("type")
                        .flatMap(nextgen.lambda.ui.components.StringComponent::value)
                        .ifPresent(value -> {
                           row.getButton("init").flatMap(nextgen.lambda.ui.components.ButtonComponent::value).map(Object::toString).ifPresent(field::set_init);
                           final String one = row.getButton("ONE").flatMap(nextgen.lambda.ui.components.ButtonComponent::value).map(Object::toString).orElse(null);
                           final String two = row.getButton("TWO").flatMap(nextgen.lambda.ui.components.ButtonComponent::value).map(t -> " ," + t).orElse("");
                           final String three = row.getButton("THREE").flatMap(nextgen.lambda.ui.components.ButtonComponent::value).map(t -> " ," + t).orElse("");
                           final boolean isGeneric = one != null;
                           final String fieldType = value + (isGeneric ? ("<" + (one + two + three) + ">") : "");

                           field.set_type(fieldType);

                           if (!isGeneric) {
                              setter.add_parameters(nextgen.lambda.modules.templates.java.JavaGroup.new_parameterBuilder().set_type(fieldType).set_name(s));
                              setter.add_statements("this." + s + " = " + s + ";");
                           } else {

                              setter.add_parameters(nextgen.lambda.modules.templates.java.JavaGroup.new_parameterBuilder()
                                 .set_type(one)
                                 .set_name("one"));

                              if (two.length() == 0) {
                                 setter.add_statements("this." + s + ".add(one);");
                              } else {
                                 setter.add_parameters(nextgen.lambda.modules.templates.java.JavaGroup.new_parameterBuilder()
                                    .set_type(one)
                                    .set_name("two"));
                                 setter.add_statements("this." + s + ".put(one,two);");
                              }
                           }
                           setter.add_statements("return this;");

                           getter.set_type(fieldType);
                           getter.add_statements("return " + s + ";");
                        });

                     declarations.add(getJavaGroup().render_classField(field));
                     declarations.add(getJavaGroup().render_classMethod(setter));
                     declarations.add(getJavaGroup().render_classMethod(getter));
                  }
               });
            });

            return of(declarations);
         };

         fieldForm.edit(this.panel, "fields", fieldFormPredicate, fieldDeclarations::addAll);
      }));

      this.predicate = f -> {

         final nextgen.lambda.modules.templates.java.classDeclarationBuilder builder = nextgen.lambda.modules.templates.java.JavaGroup.new_classDeclarationBuilder();
         row(0).getTextField("name").flatMap(nextgen.lambda.ui.components.StringComponent::value).ifPresent(builder::set_name);
         row(1).getTextField("scope").flatMap(nextgen.lambda.ui.components.StringComponent::value).ifPresent(builder::set_scope);

         fieldDeclarations.forEach(builder::add_fields);

         return of(builder)
            .flatMap(classDeclarationBuilder -> of(getJavaGroup()
               .render_classDeclaration(classDeclarationBuilder)));
      };

      newCenterPrefNone().label("generate").button("generate", nextgen.lambda.ACTIONS.newAction("generate", () -> predicate.apply(this).ifPresent(content -> {
         nextgen.lambda.ui.UI.toClipboard(content);
         nextgen.lambda.ui.UI.show(this.panel, content);
      })));
   }

   public void edit(java.util.function.Consumer<String> onConfirm) {
      edit(owner, "Pojo", predicate, onConfirm);
   }
}