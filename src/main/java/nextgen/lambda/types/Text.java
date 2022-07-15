package nextgen.lambda.types;

public class Text {

   private final String content;

   public Text(String content) {
      this.content = content;
   }

   @Override
   public String toString() {
      return content;
   }

   public Text splitLines(java.util.function.Consumer<String> lineConsumer) {
      java.util.Arrays.stream(content.trim().split("\r?\n")).map(String::trim).filter(line -> line.length() != 0).forEach(lineConsumer);
      return this;
   }

   public TextTokens tokenize() {

      final TextTokens textTokens = new nextgen.lambda.types.Text.TextTokens();

      final java.util.concurrent.atomic.AtomicInteger index = new java.util.concurrent.atomic.AtomicInteger(-1);
      final java.util.concurrent.atomic.AtomicInteger lineNo = new java.util.concurrent.atomic.AtomicInteger(-1);
      java.util.Arrays.stream(content.trim().split("\r?\n"))
         .map(String::trim)
         .filter(line -> line.length() != 0)
         .forEach(line -> {
            lineNo.incrementAndGet();
            final java.util.concurrent.atomic.AtomicInteger column = new java.util.concurrent.atomic.AtomicInteger(-1);
            java.util.Arrays.stream(line.split(" ")).map(String::trim).filter(token -> token.length() != 0).forEach(token -> {
               index.incrementAndGet();
               textTokens.token(token, index.get(), lineNo.get(), column.get());
               column.addAndGet(token.length());
            });
         });

      return textTokens;
   }

   public static final class TextTokens {

      private final java.util.Map<String, java.util.Set<nextgen.lambda.types.Text.TokenPosition>> tokens = new java.util.TreeMap<>();

      public void token(String token, int index, int line, int column) {
         if (!tokens.containsKey(token)) tokens.put(token, new java.util.TreeSet<>());
         tokens.get(token).add(new nextgen.lambda.types.Text.TokenPosition(index, line, column));
      }

      @Override
      public String toString() {
         final StringBuilder out = new StringBuilder();
         tokens.keySet().forEach(t -> out.append("\n").append(t).append(" : ").append(tokens.get(t)));
         return out.toString().trim();
      }
   }

   public static final class TokenPosition implements Comparable<TokenPosition> {

      final int index;
      final int line;
      final int column;

      public TokenPosition(int index, int line, int column) {
         this.index = index;
         this.line = line;
         this.column = column;
      }

      @Override
      public String toString() {
         return line + " " + column + " (" + index + ")";
      }

      @Override
      public int compareTo(nextgen.lambda.types.Text.TokenPosition tokenPosition) {
         return index - tokenPosition.index;
      }
   }
}