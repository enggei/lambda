package nextgen.lambda;

import java.util.*;
import java.util.function.*;

import static java.lang.String.*;

public class LambdaTests {

   private static final java.util.logging.Logger log = nextgen.lambda.LOG.logger(LambdaTests.class);

   @org.junit.Test
   public void testObjects() {

      final LambdaMatrix matrix = LambdaMatrix.init()
         .supplier("Hello", () -> "Hello")
         .supplier("World", () -> "World")
         .consumer("Println", o -> System.out.println(o.toString()))
         .function("ToUpper", o -> o.toString().toUpperCase())
         .biFunction("Concat", (one,two) -> String.join(" ", one.toString(), two.toString()))
         .function("ToLower", o -> o.toString().toLowerCase());

      matrix
         .apply_2("Hello", "World", "Concat")
         .ifPresent(System.out::println);


   }

   private static class LambdaMatrix {

      public static LambdaMatrix init() {
         return new LambdaMatrix();
      }

      protected final Map<String, Supplier<Object>> supplierMap = new TreeMap<>();
      protected final Map<String, Consumer<Object>> consumerMap = new TreeMap<>();

      protected final Map<String, Function<Object, Object>> functionMap = new TreeMap<>();
      protected final Map<String, BiFunction<Object, Object, Object>> biFunctionMap = new TreeMap<>();

      Optional<Supplier<Object>> supplier(String name) {
         return Optional.ofNullable(supplierMap.get(name));
      }

      Optional<Consumer<Object>> consumer(String name) {
         return Optional.ofNullable(consumerMap.get(name));
      }

      Optional<Function<Object, Object>> function(String name) {
         return Optional.ofNullable(functionMap.get(name));
      }

      Optional<BiFunction<Object, Object, Object>> biFunction(String name) {
         return Optional.ofNullable(biFunctionMap.get(name));
      }

      LambdaMatrix supplier(String name, Supplier<Object> supplier) {
         this.supplierMap.put(name, supplier);
         return this;
      }

      LambdaMatrix consumer(String name, Consumer<Object> consumer) {
         this.consumerMap.put(name, consumer);
         return this;
      }

      LambdaMatrix function(String name, Function<Object, Object> function) {
         this.functionMap.put(name, function);
         return this;
      }

      LambdaMatrix biFunction(String name, BiFunction<Object, Object, Object> function) {
         this.biFunctionMap.put(name, function);
         return this;
      }

      public Optional<Object> apply(String supplier, String function) {
         final Optional<Supplier<Object>> optionalSupplier = supplier(supplier);
         final Optional<Function<Object, Object>> optionalFunction = function(function);
         if (optionalSupplier.isPresent() && optionalFunction.isPresent())
            return Optional.ofNullable(optionalFunction.get().apply(optionalSupplier.get().get()));
         return Optional.empty();
      }

      public Optional<Object> apply_2(String supplier, String supplier2, String biFunction) {
         final Optional<Supplier<Object>> optionalSupplier = supplier(supplier);
         final Optional<Supplier<Object>> optionalSupplier2 = supplier(supplier2);
         final Optional<BiFunction<Object, Object, Object>> optionalFunction = biFunction(biFunction);
         if (optionalSupplier.isPresent() && optionalSupplier2.isPresent() && optionalFunction.isPresent())
            return Optional.ofNullable(optionalFunction.get().apply(optionalSupplier.get().get(), optionalSupplier2.get().get()));
         return Optional.empty();
      }
   }

   private void log(Class<?> aClass) {
      log.info("class         " + aClass);

      log.info("constructors  ");
      java.util.Arrays.stream(aClass.getDeclaredConstructors())
         .map(this::log)
         .sorted()
         .forEach(log::info);

      log.info("class fields  ");
      java.util.Arrays.stream(aClass.getDeclaredFields())
         .map(this::log)
         .sorted()
         .forEach(log::info);

      log.info("class methods  ");
      java.util.Arrays.stream(aClass.getDeclaredMethods())
         .map(this::log)
         .sorted()
         .forEach(log::info);
   }

   private String log(java.lang.reflect.Constructor<?> model) {
      return join(",",
         "C",
         java.lang.reflect.Modifier.toString(model.getModifiers()),
         join(",",
            Integer.toString(model.getParameterCount()),
            java.util.Arrays.stream(model.getParameterTypes()).map(Class::getSimpleName).sorted().collect(java.util.stream.Collectors.joining(" "))));
   }

   private String log(java.lang.reflect.Field model) {
      return join(",",
         "F",
         java.lang.reflect.Modifier.toString(model.getModifiers()),
         model.getType().getSimpleName(),
         model.getName()
      );
   }

   private String log(java.lang.reflect.Method model) {
      return join(",",
         "M",
         java.lang.reflect.Modifier.toString(model.getModifiers()),
         model.getReturnType().getSimpleName(),
         model.getName(),
         Integer.toString(model.getParameterCount()),
         java.util.Arrays.stream(model.getParameterTypes())
            .map(Class::getSimpleName)
            .sorted()
            .collect(java.util.stream.Collectors.joining(" ")));
   }

}