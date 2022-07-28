package nextgen.lambda.modules.java;

public final class JavaTypes {

   public static JavaTypes get() {return new JavaTypes();}

   final java.util.Set<JavaType> types = new java.util.TreeSet<>();

   public nextgen.lambda.modules.java.JavaTypes lang() {

      final java.util.List<JavaType> types = java.util.List.of(
         from(String.class),
         from(StringBuilder.class),
         from(Integer.class),
         from(Boolean.class),
         from(Float.class),
         from(Short.class),
         from(Object.class),
         from(Double.class),
         from(Byte.class),
         from(java.time.LocalDate.class),
         from(java.time.LocalDateTime.class),
         from(java.time.Period.class)
      );

      this.types.addAll(types);
      return this;
   }

   public nextgen.lambda.modules.java.JavaTypes util() {

      final java.util.List<JavaType> javaTypes = java.util.List.of(
         from(java.util.Set.class, java.util.List.of(java.util.TreeSet.class, java.util.LinkedHashSet.class, java.util.concurrent.ConcurrentSkipListSet.class, java.util.concurrent.CopyOnWriteArraySet.class)),
         from(java.util.List.class, java.util.List.of(java.util.ArrayList.class, java.util.LinkedList.class, java.util.concurrent.CopyOnWriteArrayList.class, java.util.Stack.class, java.util.Vector.class)),
         from(java.util.Map.class, java.util.List.of(java.util.LinkedHashMap.class, java.util.TreeMap.class, java.util.concurrent.ConcurrentHashMap.class)),
         from(java.util.UUID.class),
         from(java.util.Random.class),
         from(java.util.regex.Pattern.class),
         from(java.util.concurrent.atomic.AtomicInteger.class),
         from(java.util.concurrent.atomic.AtomicBoolean.class),
         from(java.util.concurrent.atomic.AtomicReference.class),
         from(java.util.concurrent.atomic.AtomicLong.class),
         from(java.util.concurrent.atomic.AtomicIntegerArray.class),
         from(java.util.concurrent.atomic.AtomicLong.class)
      );
      this.types.addAll(javaTypes);
      return this;
   }

   public nextgen.lambda.modules.java.JavaTypes stream() {

      final java.util.List<JavaType> javaTypes = java.util.List.of(
         from(java.util.stream.Stream.class),
         from(java.util.stream.StreamSupport.class),
         from(java.util.stream.Collectors.class)
      );
      this.types.addAll(javaTypes);
      return this;
   }

   public nextgen.lambda.modules.java.JavaTypes arrays() {

      final java.util.List<JavaType> javaTypes = java.util.List.of(
         from(java.util.Arrays.class)
      );
      this.types.addAll(javaTypes);
      return this;
   }

   public nextgen.lambda.modules.java.JavaTypes sets() {

      final java.util.List<JavaType> javaTypes = java.util.List.of(
         from(java.util.Set.class),
         from(java.util.LinkedHashSet.class),
         from(java.util.TreeSet.class),
         from(java.util.concurrent.ConcurrentSkipListSet.class),
         from(java.util.concurrent.CopyOnWriteArraySet.class)
      );
      this.types.addAll(javaTypes);
      return this;
   }

   public nextgen.lambda.modules.java.JavaTypes lists() {

      final java.util.List<JavaType> javaTypes = java.util.List.of(
         from(java.util.List.class),
         from(java.util.LinkedList.class),
         from(java.util.ArrayList.class),
         from(java.util.PriorityQueue.class),
         from(java.util.ArrayDeque.class),
         from(java.util.concurrent.CopyOnWriteArrayList.class),
         from(java.util.concurrent.ConcurrentLinkedDeque.class),
         from(java.util.concurrent.ConcurrentLinkedQueue.class)
      );
      this.types.addAll(javaTypes);
      return this;
   }

   public nextgen.lambda.modules.java.JavaTypes maps() {

      final java.util.List<JavaType> javaTypes = java.util.List.of(
         from(java.util.Map.class),
         from(java.util.LinkedHashMap.class),
         from(java.util.HashMap.class),
         from(java.util.concurrent.ConcurrentHashMap.class),
         from(java.util.concurrent.ConcurrentSkipListMap.class)
      );
      this.types.addAll(javaTypes);
      return this;
   }

   public nextgen.lambda.modules.java.JavaTypes functional() {

      final java.util.List<JavaType> javaTypes = java.util.List.of(
         from(java.util.function.Supplier.class),
         from(java.util.function.Consumer.class),
         from(java.util.function.BiConsumer.class),
         from(java.util.function.Function.class),
         from(java.util.function.BiFunction.class),
         from(java.util.function.Predicate.class),
         from(java.util.function.BiPredicate.class),
         from(java.util.function.UnaryOperator.class),
         from(java.util.function.BinaryOperator.class)

//         from(java.util.function.DoubleSupplier.class),
//         from(java.util.function.DoubleConsumer.class),
//         from(java.util.function.DoubleFunction.class),
//         from(java.util.function.DoublePredicate.class),
//         from(java.util.function.DoubleToLongFunction.class),
//         from(java.util.function.DoubleToIntFunction.class),
//         from(java.util.function.DoubleUnaryOperator.class),
//         from(java.util.function.DoubleBinaryOperator.class),
//
//         from(java.util.function.ObjDoubleConsumer.class),
//         from(java.util.function.ObjIntConsumer.class),
//         from(java.util.function.ObjLongConsumer.class),
//
//         from(java.util.function.BooleanSupplier.class),
//
//         from(java.util.function.IntSupplier.class),
//         from(java.util.function.IntConsumer.class),
//         from(java.util.function.IntFunction.class),
//         from(java.util.function.IntPredicate.class),
//         from(java.util.function.IntToLongFunction.class),
//         from(java.util.function.IntToDoubleFunction.class),
//         from(java.util.function.IntUnaryOperator.class),
//         from(java.util.function.IntBinaryOperator.class),
//
//         from(java.util.function.LongSupplier.class),
//         from(java.util.function.LongConsumer.class),
//         from(java.util.function.LongFunction.class),
//         from(java.util.function.LongPredicate.class),
//         from(java.util.function.LongToDoubleFunction.class),
//         from(java.util.function.LongToIntFunction.class),
//         from(java.util.function.LongUnaryOperator.class),
//         from(java.util.function.LongBinaryOperator.class)

         );
      this.types.addAll(javaTypes);
      return this;
   }

   public static nextgen.lambda.modules.java.JavaType from(Class<?> aClass) {
      return new nextgen.lambda.modules.java.JavaType(aClass);
   }

   public static nextgen.lambda.modules.java.JavaType from(Class<?> aClass, java.util.List<Class<?>> implementations) {
      final nextgen.lambda.modules.java.JavaType javaType = new nextgen.lambda.modules.java.JavaType(aClass);
      javaType.implementations.addAll(implementations.stream().map(nextgen.lambda.modules.java.JavaType::new).collect(java.util.stream.Collectors.toSet()));
      return javaType;
   }

   public java.util.Set<JavaType> build() {
      return this.types;
   }

   public java.util.Set<Class<?>> classes() {
      return types.stream()
         .map(nextgen.lambda.modules.java.JavaType::asClass)
         .filter(java.util.Optional::isPresent)
         .map(java.util.Optional::get)
         .collect(java.util.stream.Collectors.toSet());
   }
}