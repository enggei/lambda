package nextgen.lambda.modules.java;

public class JavaType implements Comparable<JavaType> {

   final String canonicalName;
   JavaType ONE;
   JavaType TWO;
   JavaType THREE;
   Class<?> aClass;

   final java.util.Set<JavaType> implementations = new java.util.TreeSet<>();

   boolean isArray = false;

   public static void main(String[] args) {
      nextgen.lambda.modules.java.JavaTypes.get().util().types.forEach(javaType -> {
         System.out.println(javaType + "\n\t" + String.join("\n\t", javaType.constructors()).trim());
      });
   }

   public JavaType(String type) {
      this.canonicalName = type;
      if (this.canonicalName.length() == 1) return;
      nextgen.lambda.OBJECTS.classFor(canonicalName).ifPresent(aClass1 -> {
         this.aClass = aClass1;
         java.util.Arrays.stream(aClass1.getTypeParameters()).forEach(this::addType);
      });
   }

   public JavaType(java.lang.reflect.Type type) {
      this(type.getTypeName());

      if (type instanceof java.lang.reflect.ParameterizedType) {
         java.lang.reflect.ParameterizedType parameterizedType = (java.lang.reflect.ParameterizedType) type;
         java.util.Arrays.stream(parameterizedType.getActualTypeArguments()).forEach(this::addType);
      } else if (type instanceof java.lang.reflect.TypeVariable) {
         final java.lang.reflect.TypeVariable<?> typeVariable = (java.lang.reflect.TypeVariable<?>) type;
         java.util.Arrays.stream(typeVariable.getBounds()).forEach(this::addType);
      }
   }

   public java.util.Set<String> constructors() {
      final java.util.Set<String> set = new java.util.TreeSet<>();
      java.util.Optional.ofNullable(aClass)
         .ifPresent(theClass -> {
            nextgen.lambda.OBJECTS.classConstructors(theClass)
               .forEach(constructor -> {
                  final boolean isGeneric = theClass.getTypeParameters().length != 0;
                  set.add(String.join(" ", "new", String.join("", constructor.getName(), (isGeneric ? "<>" : ""), "()")));
               });

            nextgen.lambda.OBJECTS.classMethods(theClass)
               .filter(method -> theClass.isAssignableFrom(method.getReturnType()))
               .filter(method -> method.getParameterCount() == 0)
               .forEach(method -> {
                  final boolean isGeneric = theClass.getTypeParameters().length != 0;
                  set.add(String.join(".", theClass.getCanonicalName(), String.join("", method.getName(), (isGeneric ? "<>" : ""), "()")));
               });
         });

      implementations.forEach(javaType -> set.addAll(javaType.constructors()));

      return set;
   }

   public java.util.Optional<nextgen.lambda.modules.java.JavaType> one() {
      return java.util.Optional.ofNullable(ONE);
   }

   public java.util.Optional<nextgen.lambda.modules.java.JavaType> two() {
      return java.util.Optional.ofNullable(TWO);
   }

   public java.util.Optional<nextgen.lambda.modules.java.JavaType> three() {
      return java.util.Optional.ofNullable(THREE);
   }

   public java.util.Optional<Class<?>> asClass() {
      return java.util.Optional.ofNullable(aClass);
   }

   public String canonicalName() {
      return canonicalName;
   }

   private void addType(java.lang.reflect.Type typeVariable) {
      if (ONE == null) ONE = new nextgen.lambda.modules.java.JavaType(typeVariable);
      else if (TWO == null) TWO = new nextgen.lambda.modules.java.JavaType(typeVariable);
      else if (THREE == null) THREE = new nextgen.lambda.modules.java.JavaType(typeVariable);
   }

   @Override
   public String toString() {
      if (this.canonicalName.length() == 1) return canonicalName;
      final boolean isGeneric = ONE != null;
      final String two = TWO == null ? "" : (" ," + TWO);
      final String three = THREE == null ? "" : (" ," + THREE);
      final String types = ONE + two + three;
      return canonicalName + (isGeneric ? ("<" + types + ">") : "") + (isArray ? "[]" : "");
   }

   @Override
   public int compareTo(nextgen.lambda.modules.java.JavaType javaType) {
      return toString().compareTo(javaType.toString());
   }
}