package nextgen.lambda;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.*;

import static java.util.stream.Collectors.*;


public class OBJECTS {

   private static final java.util.logging.Logger log = nextgen.lambda.LOG.logger(OBJECTS.class);

   public static Stream<Field> objectFields(Object object) {
      return Arrays.stream(classFor(object).getDeclaredFields()).filter(field -> !Modifier.isStatic(field.getModifiers())).filter(field -> Modifier.isPublic(field.getModifiers()));
   }

   public static Optional<Object> objectFieldValue(java.lang.reflect.Field field, Object object) {
      try {
         if (!field.trySetAccessible()) return Optional.empty();
         return Optional.ofNullable(field.get(object));
      } catch (Throwable throwable) {
         EVENTS.exception(throwable);
         return Optional.empty();
      }
   }

   public static Stream<Method> objectMethods(Object object) {
      return java.util.Arrays.stream(classFor(object).getDeclaredMethods())
         .filter(method -> !Modifier.isAbstract(method.getModifiers()))
         .filter(method -> Modifier.isPublic(method.getModifiers()))
         .filter(method -> !Modifier.isStatic(method.getModifiers()))
         .sorted(methodComparator());
   }


   public static void invokeConstructor(javax.swing.JComponent owner, java.lang.reflect.Constructor<?> constructor, java.util.Set<Object> objects) {

      if (constructor.getParameterCount() == 0) {
         nextgen.lambda.OBJECTS.construct(constructor, java.util.Collections.emptyList()).ifPresent(nextgen.lambda.EVENTS::open);
         return;
      }

      final java.lang.reflect.Parameter[] parameters = constructor.getParameters();
      final java.util.Map<java.lang.reflect.Parameter, Object> actualArguments = new java.util.LinkedHashMap<>(parameters.length);

      final java.util.Set<Object> usedObjects = new java.util.LinkedHashSet<>();
      final java.util.Set<Object> applicableObjects = new java.util.LinkedHashSet<>();
      final java.util.concurrent.atomic.AtomicBoolean hasPrimitive = new java.util.concurrent.atomic.AtomicBoolean(false);
      for (java.lang.reflect.Parameter parameter : parameters) {

         if (isPrimitive(parameter)) hasPrimitive.set(true);

         for (Object object : objects) {
            final boolean assignable = parameter.getType().isAssignableFrom(classFor(object));
            if (assignable) applicableObjects.add(object);
            if (!usedObjects.contains(object) && assignable) {
               actualArguments.put(parameter, object);
               usedObjects.add(object);
            }
         }
      }

//      if (usedObjects.size() == parameters.length) {
//         nextgen.lambda.OBJECTS.construct(constructor, actualArguments.keySet().stream().map(actualArguments::get).collect(toList())).ifPresent(nextgen.lambda.EVENTS::open);
//         return;
//      }

      final nextgen.lambda.ui.forms.Form form = nextgen.lambda.ui.forms.Form.newForm().newColumn().left().pref().grow().builder();

      final nextgen.lambda.ui.forms.FormRow headerRow = form.newRow().center().pref().none().label("Values");

      final java.util.Map<java.lang.reflect.Parameter, nextgen.lambda.ui.forms.FormColumn> parameterColumns = new java.util.LinkedHashMap<>();
      for (java.lang.reflect.Parameter parameter : parameters) {
         final Class<?> parameterType = parameter.getType();
         parameterColumns.putIfAbsent(parameter, form.newColumn().left().pref().none());
         headerRow.label(parameterType.getSimpleName());
      }

      final java.util.Map<Object, nextgen.lambda.ui.forms.FormRow> objectRows = new java.util.LinkedHashMap<>();
      for (Object object : applicableObjects) {
         objectRows.putIfAbsent(object, form.newRow().center().pref().none().label(object.getClass().getCanonicalName()));
         parameterColumns.forEach((parameter, column) -> {
            if (parameter.getType().isAssignableFrom(classFor(object))) objectRows.get(object).toggleButton(parameter.getName(), "Set", () -> actualArguments.put(parameter, object));
            else objectRows.get(object).label("");
         });
      }

      form.show(owner, "Construct " + constructor.getName(), confirmed -> nextgen.lambda.OBJECTS.construct(constructor, new java.util.ArrayList<>(actualArguments.values())).ifPresent(nextgen.lambda.EVENTS::open));
   }

   public static Optional<Object> invokeObjectMethod(Object model, Method method, List<Object> arguments) {

      try {

         if (!method.canAccess(model)) if (!method.trySetAccessible()) return Optional.empty();

         final java.lang.reflect.Parameter[] parameters = method.getParameters();
         if (parameters.length == 0) return Optional.ofNullable(method.invoke(model));

         final Object[] actualArguments = new Object[parameters.length];
         for (int i = 0; i < parameters.length; i++) {
            java.lang.reflect.Parameter parameter = parameters[i];
            final boolean isVarargs = parameter.isVarArgs();
            if (isVarargs) {
               actualArguments[i] = arguments.subList(i, arguments.size()).toArray();
            } else {
               actualArguments[i] = arguments.get(i);
            }
         }

         final nextgen.lambda.modules.templates.java.methodCallBuilder methodCall = nextgen.lambda.modules.templates.java.JavaGroup.new_methodCallBuilder()
            .set_scope(model.getClass().getCanonicalName())
            .set_name(method.getName());

         for (Object actualArgument : actualArguments) {
            methodCall.add_arguments(actualArgument.getClass().getCanonicalName());
         }

         log.info(nextgen.lambda.modules.java.Java.getJavaGroup().render(methodCall));


         return Optional.ofNullable(arguments.isEmpty() ? method.invoke(model) : method.invoke(model, actualArguments)).flatMap(invoke -> Optional.ofNullable(invoke.equals(model) ? null : invoke));

      } catch (Throwable throwable) {
         EVENTS.exception(throwable);
         log.severe(method.getName() + " " + model);
         return Optional.empty();
      }
   }

   public static <T> void invokeObjectMethod(javax.swing.JComponent owner, T model, java.lang.reflect.Method method, java.util.Collection<Object> objects) {

      if (method.getParameterCount() == 0) {
         nextgen.lambda.OBJECTS.invokeObjectMethod(model, method, java.util.Collections.emptyList()).ifPresent(nextgen.lambda.EVENTS::open);
         return;
      }

      methodParameterForm(owner, model, method, objects);
   }

   public static <T> void methodParameterForm(javax.swing.JComponent owner, T model, java.lang.reflect.Method method, java.util.Collection<Object> objects) {

      final java.lang.reflect.Parameter[] parameters = method.getParameters();
      final java.util.Map<java.lang.reflect.Parameter, Object> actualArguments = new java.util.LinkedHashMap<>(parameters.length);
      final java.util.Set<Object> usedObjects = new java.util.LinkedHashSet<>();
      final java.util.Set<Object> applicableObjects = new java.util.LinkedHashSet<>();
      for (java.lang.reflect.Parameter parameter : parameters) {
         for (Object object : objects) {
            if (object.equals(model)) continue;
            final boolean assignable = parameter.getType().isAssignableFrom(classFor(object));
            if (assignable) applicableObjects.add(object);
            if (!usedObjects.contains(object) && assignable) {
               actualArguments.put(parameter, object);
               usedObjects.add(object);
            }
         }
      }

      if (method.getParameterCount() == 1 && !isPrimitive(parameters[0]) && !usedObjects.isEmpty() && usedObjects.size() == objects.size()) {
         nextgen.lambda.OBJECTS.invokeObjectMethod(model, method, List.of(usedObjects.iterator().next())).ifPresent(nextgen.lambda.EVENTS::open);
         return;
      }

      final java.util.Map<java.lang.reflect.Parameter, java.util.function.Supplier<Object>> userInputParameterMap = new java.util.LinkedHashMap<>();
      final nextgen.lambda.ui.forms.Form form = nextgen.lambda.ui.forms.Form.newForm().newLeftPrefGrow();
      final nextgen.lambda.ui.forms.FormRow headerRow = form.newCenterPrefNone().label("parameters");
      final nextgen.lambda.ui.forms.FormRow userInputRow = form.newCenterPrefNone().label("Input");

      final java.util.Map<java.lang.reflect.Parameter, nextgen.lambda.ui.forms.FormColumn> parameterColumns = new java.util.LinkedHashMap<>();
      for (java.lang.reflect.Parameter parameter : parameters) {
         final Class<?> parameterType = parameter.getType();
         final String simpleName = parameterType.getSimpleName();
         parameterColumns.put(parameter, form.newColumn().left().pref().none());
         headerRow.label(simpleName);
         nextgen.lambda.ui.components.Component.component(parameterType, simpleName).ifPresent(component -> {
            userInputRow.add(component);
            userInputParameterMap.put(parameter, () -> component.value().orElse(null));
         });
      }

      final java.util.Map<Object, nextgen.lambda.ui.forms.FormRow> objectRows = new java.util.LinkedHashMap<>();
      for (Object object : applicableObjects) {
         objectRows.putIfAbsent(object, form.newRow().center().pref().none().label(object.getClass().getCanonicalName()));
         parameterColumns.forEach((parameter, column) -> {
            if (parameter.getType().isAssignableFrom(classFor(object)))
               objectRows.get(object).toggleButton(parameter.getName(), "Set", () -> actualArguments.put(parameter, object));
            else
               objectRows.get(object).label("");
         });
      }

      final java.util.function.Consumer<nextgen.lambda.ui.forms.Form> onConfirm = confirmed -> {
         for (java.lang.reflect.Parameter parameter : parameters) {
            if (userInputParameterMap.containsKey(parameter)) {
               try {
                  final Object v = userInputParameterMap.getOrDefault(parameter, () -> null).get();
                  if (v != null) actualArguments.put(parameter, v);
               } catch (Throwable throwable) {
                  nextgen.lambda.EVENTS.exception(throwable);
               }
            }
         }

         if (model == null || Modifier.isStatic(method.getModifiers())) invokeClassMethod(method, new java.util.ArrayList<>(actualArguments.values())).ifPresent(nextgen.lambda.EVENTS::open);
         else nextgen.lambda.OBJECTS.invokeObjectMethod(model, method, new java.util.ArrayList<>(actualArguments.values())).ifPresent(nextgen.lambda.EVENTS::open);
      };

      form.show(owner, "Invoke " + method.getName(), onConfirm);
   }

   public static <T> void setObjectField(javax.swing.JComponent owner, T model, java.lang.reflect.Field field, java.util.Set<Object> objects) {

      final java.util.Set<Object> usedObjects = new java.util.LinkedHashSet<>();
      final java.util.Set<Object> applicableObjects = new java.util.LinkedHashSet<>();
      for (Object object : objects) {
         if (object.equals(model)) continue;
         final boolean assignable = field.getType().isAssignableFrom(classFor(object));
         if (assignable) applicableObjects.add(object);
         if (!usedObjects.contains(object) && assignable) usedObjects.add(object);
      }

      if (applicableObjects.size() == 1) {
         setObjectField(model, field, usedObjects);
         return;
      }

      nextgen.lambda.ui.UI.singleSelect(owner, "set", new java.util.ArrayList<>(applicableObjects), null)
         .ifPresent(selected -> setObjectField(model, field, selected));
   }

   private static <T> void setObjectField(T model, java.lang.reflect.Field field, Object value) {
      try {
         field.set(model, value);
      } catch (Throwable e) {
         nextgen.lambda.EVENTS.exception(e);
      }
   }

   public static java.util.Optional<Object> construct(java.lang.reflect.Constructor<?> constructor) {
      return construct(constructor, Collections.emptyList());
   }

   public static java.util.Optional<Object> construct(java.lang.reflect.Constructor<?> constructor, List<Object> arguments) {
      try {

         final java.lang.reflect.Parameter[] parameters = constructor.getParameters();
         if (parameters.length == 0) return Optional.of(constructor.newInstance());

         final Object[] actualArguments = new Object[parameters.length];
         for (int i = 0; i < parameters.length; i++) {
            java.lang.reflect.Parameter parameter = parameters[i];
            if (parameter.isVarArgs()) {
               actualArguments[i] = arguments.subList(i, arguments.size()).toArray();
            } else {
               actualArguments[i] = arguments.get(i);
            }
         }

         return Optional.of(constructor.newInstance(actualArguments));
      } catch (Throwable throwable) {
         EVENTS.exception(throwable);
         return Optional.empty();
      }
   }

   public static void forEach(Iterable<Object> model, java.util.function.Consumer<Object> consumer) {
      model.forEach(consumer);
   }

   @SuppressWarnings("unchecked")
   public static Optional<Iterable<Object>> asIterable(Object model) {
      final Class<?> aClass = classFor(model);
      if (Iterable.class.isAssignableFrom(aClass)) return Optional.of((Iterable<Object>) model);
      else if (Iterator.class.isAssignableFrom(aClass)) return Optional.of(() -> (Iterator<Object>) model);
      else if (aClass.isArray()) return Optional.of(() -> Arrays.stream((Object[]) model).iterator());
      else if (Stream.class.isAssignableFrom(aClass)) return Optional.of(() -> ((Stream<Object>) model).iterator());
      return Optional.empty();
   }

   static Comparator<Constructor<?>> constructorComparator() {
      return (m1, m2) -> {

         int compare = m1.getParameterCount() - m2.getParameterCount();
         if (compare != 0) return compare;

         final String t1 = Arrays.stream(m1.getParameterTypes()).map(Class::getCanonicalName).collect(joining());
         final String t2 = Arrays.stream(m2.getParameterTypes()).map(Class::getCanonicalName).collect(joining());
         return t1.compareTo(t2);
      };
   }

   static Comparator<Method> methodComparator() {
      return (m1, m2) -> {

         final int p1 = m1.getParameterCount();
         final int p2 = m2.getParameterCount();
         int compare = p1 - p2;
         if (compare != 0) return compare;

         final String r1 = m1.getReturnType().getCanonicalName();
         final String r2 = m2.getReturnType().getCanonicalName();
         compare = r1.compareTo(r2);
         if (compare != 0) return compare;

         return m1.getName().compareTo(m2.getName());
      };
   }

   static Comparator<Field> fieldComparator() {
      return Comparator.comparing(Field::getName);
   }

   public static String signature(java.lang.reflect.Constructor<?> constructor) {
      return String.join(" ", constructor.getName(), Integer.toString(constructor.getParameterCount()), java.util.Arrays.stream(constructor.getParameters()).map(Parameter::toString).sorted().collect(java.util.stream.Collectors.joining(" ")));
   }

   public static String signature(java.lang.reflect.Method method) {
      return String.join(" ", method.getName(), method.getReturnType().getSimpleName(), Integer.toString(method.getParameterCount()), java.util.Arrays.stream(method.getParameters()).map(Parameter::toString).sorted().collect(java.util.stream.Collectors.joining(" ")));
   }

   public static String debug(Object model) {
      final StringBuilder out = new StringBuilder();
      out.append(model.getClass().getCanonicalName());
      objectFields(model).forEach(field -> objectFieldValue(field, model).ifPresent(fieldValue -> out.append("\n\t").append(fieldValue.toString().replaceAll("\r?\n", " "))));
      return out.toString();
   }

   public static boolean isPrimitive(Class<?> aClass) {
      return isPrimitive(aClass.getCanonicalName());
   }

   public static java.util.stream.Stream<java.lang.reflect.Field> classFields(Object object) {
      return java.util.Arrays.stream(classFor(object).getDeclaredFields()).filter(field -> java.lang.reflect.Modifier.isStatic(field.getModifiers())).filter(field -> java.lang.reflect.Modifier.isPublic(field.getModifiers())).sorted(fieldComparator());
   }

   public static java.util.Optional<Object> classFieldValue(java.lang.reflect.Field field) {
      try {
         if (!field.trySetAccessible()) return java.util.Optional.empty();
         return java.util.Optional.ofNullable(field.get(null));
      } catch (Throwable throwable) {
         EVENTS.exception(throwable);
         return java.util.Optional.empty();
      }
   }

   public static java.util.stream.Stream<java.lang.reflect.Constructor<?>> classConstructors(Class<?> aClass) {
      return java.util.Arrays.stream(classFor(aClass).getDeclaredConstructors())
         .filter(method -> java.lang.reflect.Modifier.isPublic(method.getModifiers()))
         .filter(method -> !java.lang.reflect.Modifier.isAbstract(method.getModifiers()))
         .sorted(constructorComparator());
   }

   public static java.util.stream.Stream<java.lang.reflect.Method> classMethods(Object object) {
      return java.util.Arrays.stream(classFor(object)
            .getDeclaredMethods())
         .filter(method -> !java.lang.reflect.Modifier.isAbstract(method.getModifiers()))
         .filter(method -> java.lang.reflect.Modifier.isPublic(method.getModifiers()))
         .filter(method -> java.lang.reflect.Modifier.isStatic(method.getModifiers()))
         .sorted(methodComparator());
   }

   public static void invokeClassMethod(javax.swing.JComponent owner, java.lang.reflect.Method method, java.util.Collection<Object> objects) {

      if (method.getParameterCount() == 0) {
         invokeClassMethod(method, java.util.Collections.emptyList()).ifPresent(EVENTS::open);
         return;
      }

      methodParameterForm(owner, null, method, objects);
   }

   public static java.util.Optional<Object> invokeClassMethod(java.lang.reflect.Method method, java.util.List<Object> arguments) {
      try {
         if (!method.trySetAccessible()) return java.util.Optional.empty();

         final java.lang.reflect.Parameter[] parameters = method.getParameters();
         if (parameters.length == 0) return java.util.Optional.ofNullable(method.invoke(null));

         final Object[] actualArguments = new Object[parameters.length];
         for (int i = 0; i < parameters.length; i++) {
            java.lang.reflect.Parameter parameter = parameters[i];
            final boolean isVarargs = parameter.isVarArgs();
            if (isVarargs) {
               actualArguments[i] = arguments.subList(i, arguments.size()).toArray();
            } else {
               actualArguments[i] = arguments.get(i);
            }
         }

         return java.util.Optional.ofNullable(arguments.isEmpty() ? method.invoke(null) : method.invoke(null, actualArguments));

      } catch (Throwable throwable) {
         EVENTS.exception(throwable);
         return java.util.Optional.empty();
      }
   }

   public static Class<?> classFor(Object object) {
      return (object instanceof Class<?>) ? (Class<?>) object : object.getClass();
   }

   @SuppressWarnings("unchecked")
   public static java.util.Optional<Class<?>> classFor(String canonicalName) {
      if (!canonicalName.contains(".")) return java.util.Optional.empty();
      try {
         return java.util.Optional.of(Class.forName(canonicalName));
      } catch (Throwable throwable) {
         //nextgen.lambda.EVENTS.exception(throwable);
         return java.util.Optional.empty();
      }
   }

   public static java.util.Optional<Class<?>> asClass(String className) {
      try {
         return java.util.Optional.of(Class.forName(className));
      } catch (Throwable throwable) {
         EVENTS.exception(throwable);
         return java.util.Optional.empty();
      }
   }

   public static java.util.Optional<Object> newInstance(Class<?> aClass) {
      try {
         return java.util.Optional.of(aClass.getDeclaredConstructor().newInstance());
      } catch (Throwable throwable) {
         EVENTS.exception(throwable);
         return java.util.Optional.empty();
      }
   }

   public static java.util.Optional<Class<?>> newClassExtending(javax.swing.JComponent owner, Class<?> aClass) {

      final nextgen.lambda.ui.forms.Form form = nextgen.lambda.ui.forms.Form.newForm()
         .newColumn().left().pref().none()
         .newColumn().left().pref().grow()
         .builder();

      form.newRow().center().pref().none().label("scope").textField("scope", java.lang.reflect.Modifier.toString(aClass.getModifiers()));
      form.newRow().center().pref().none().label("name").textField("name", aClass.getSimpleName() + "_");
      form.newRow().center().pref().none().label("extends").textField("extends", aClass.getCanonicalName());

      final java.util.Map<String, nextgen.lambda.ui.components.MethodDeclarationComponent> implementationComponents = new java.util.TreeMap<>();
      final javax.swing.JTabbedPane tabbedPane = new javax.swing.JTabbedPane();
      overrideMethods(aClass).forEach(method -> {
         final String signature = signature(method);
         implementationComponents.put(signature, new nextgen.lambda.ui.components.MethodDeclarationComponent(method));
         tabbedPane.add(signature, new javax.swing.JScrollPane(implementationComponents.get(signature).build()));
      });
      form.newRow().center().pref().grow().add(tabbedPane);

      final java.util.concurrent.atomic.AtomicReference<Class<?>> compiled = new java.util.concurrent.atomic.AtomicReference<>();
      form.edit(owner,
         aClass.getCanonicalName(),
         f -> {
            final nextgen.lambda.modules.templates.java.classDeclarationBuilder implementation = nextgen.lambda.modules.templates.java.JavaGroup.new_classDeclarationBuilder();
            f.row(0).getTextField("scope").flatMap(nextgen.lambda.ui.components.StringComponent::value).ifPresent(implementation::set_scope);
            f.row(1).getTextField("name").flatMap(nextgen.lambda.ui.components.StringComponent::value).ifPresent(implementation::set_name);
            f.row(2).getTextField("extends").flatMap(nextgen.lambda.ui.components.StringComponent::value).ifPresent(implementation::set_extending);
            implementationComponents.values().forEach(editor -> editor.model().ifPresent(implementation::add_methods));
            return nextgen.lambda.modules.java.JavaCompiler.compileSource(aClass.getPackageName() + ".compiled", implementation);
         },
         compiled::set);

      return java.util.Optional.ofNullable(compiled.get());
   }

   public static boolean isExtendable(Class<?> aClass) {
      return java.lang.reflect.Modifier.isFinal(aClass.getModifiers());
   }

   public static boolean isPrimitive(String name) {

      final boolean isPrim = (name.contains(".Object") || name.equals("Object")) ||
         name.contains(".String") ||
         name.contains(".Short") ||
         name.contains(".Integer") ||
         name.contains(".Long") ||
         name.contains(".Float") ||
         name.contains(".Character") ||
         name.contains(".Double") ||
         name.contains(".Boolean");

      nextgen.lambda.OBJECTS.log.info((isPrim ? "PRIMITIVE " : "TYPE ") + name);
      return isPrim;
   }

   public static boolean isPrimitive(java.lang.reflect.Parameter parameter) {
      return isPrimitive(parameter.getType());
   }

   public static java.util.stream.Stream<java.lang.reflect.Method> interfaceMethod(Class<?> anInterface) {
      return java.util.Arrays.stream(anInterface.getMethods())
         .filter(method -> java.lang.reflect.Modifier.isInterface(method.getModifiers()))
         .sorted(methodComparator());
   }

   public static java.util.stream.Stream<java.lang.reflect.Method> overrideMethods(Class<?> anInterface) {
      return java.util.Arrays.stream(anInterface.getMethods())
         .filter(method -> !java.lang.reflect.Modifier.isFinal(method.getModifiers()))
         .filter(method -> !java.lang.reflect.Modifier.isPrivate(method.getModifiers()))
         .filter(method -> java.lang.reflect.Modifier.isProtected(method.getModifiers()) || java.lang.reflect.Modifier.isPublic(method.getModifiers()))
         .sorted(methodComparator());
   }

   public static boolean isCompatible(java.lang.reflect.Method method, java.util.Set<Class<?>> types) {
      if (method.getParameterCount() == 0) return true;

      final java.util.Set<Class<?>> parameterTypes = java.util.Arrays.stream(method.getParameterTypes()).collect(toSet());
      if (types.containsAll(parameterTypes)) return true;

      return parameterTypes.stream().allMatch(parameterType -> types.stream().anyMatch(parameterType::isAssignableFrom));
   }

   public static boolean isCompatible(java.lang.reflect.Constructor<?> constructor, java.util.Set<Class<?>> types) {
      if (constructor.getParameterCount() == 0) return true;

      final java.util.Set<Class<?>> parameterTypes = java.util.Arrays.stream(constructor.getParameterTypes()).collect(toSet());
      if (types.containsAll(parameterTypes)) return true;

      return parameterTypes.stream().allMatch(parameterType -> types.stream().anyMatch(parameterType::isAssignableFrom));
   }

   public static java.util.Optional<Class<?>> newInterfaceImplementation(javax.swing.JComponent owner, Class<?> anInterface) {

      final nextgen.lambda.modules.templates.java.classDeclarationBuilder implementation = nextgen.lambda.modules.templates.java.JavaGroup.new_classDeclarationBuilder()
         .set_scope("public")
         .set_name(String.join("", anInterface.getSimpleName(), "Test"))
         .add_implement(anInterface.getCanonicalName());

      final nextgen.lambda.ui.forms.Form form = nextgen.lambda.ui.forms.Form.newForm().newColumn().left().pref().grow().builder();

      final java.util.Map<String, nextgen.lambda.ui.components.MethodDeclarationComponent> implementationComponents = new java.util.TreeMap<>();
      interfaceMethod(anInterface).forEach(method -> {
         final String signature = signature(method);
         implementationComponents.put(signature, new nextgen.lambda.ui.components.MethodDeclarationComponent(method));
         form.newRow().center().pref().grow().add(implementationComponents.get(signature).build());
      });

      final java.util.concurrent.atomic.AtomicReference<Class<?>> compiled = new java.util.concurrent.atomic.AtomicReference<>();
      form.edit(owner,
         anInterface.getSimpleName(),
         f -> {

            interfaceMethod(anInterface)
               .forEach(method -> implementation.add_methods(implementationComponents.get(signature(method)).model()));

            return nextgen.lambda.modules.java.JavaCompiler.compileSource(anInterface.getPackageName() + ".compiled", implementation);
         },
         compiled::set);

      return java.util.Optional.ofNullable(compiled.get());
   }
}