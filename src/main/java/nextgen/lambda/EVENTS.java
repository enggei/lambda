package nextgen.lambda;

public class EVENTS {

   static final java.util.logging.Logger log = LOG.logger(EVENTS.class);


   public enum TYPES {
      OPEN,
      CLOSE,
      EDIT,
      FUTURE,
      EXCEPTION;

   }

   public static void register(nextgen.lambda.ui.editor.Editor editor) {
      org.greenrobot.eventbus.EventBus.getDefault().register(new nextgen.lambda.ui.editor.EditorEventsHandler(editor));
   }

   public static void register(nextgen.lambda.ui.canvas.Canvas canvas) {
      org.greenrobot.eventbus.EventBus.getDefault().register(new nextgen.lambda.ui.canvas.CanvasEventsHandler<>(canvas));
   }

   public static void exception(Throwable throwable) {
      LOG.onException(log, throwable);
      org.greenrobot.eventbus.EventBus.getDefault().post(new EVENTS.Event(nextgen.lambda.EVENTS.TYPES.EXCEPTION, throwable, "Throwable"));
   }

   public static void edit(Object model, String label) {
      org.greenrobot.eventbus.EventBus.getDefault().post(new EVENTS.Event(nextgen.lambda.EVENTS.TYPES.EDIT, model, label));
   }

   public static void open(Object model) {
      org.greenrobot.eventbus.EventBus.getDefault().post(new EVENTS.Event(TYPES.OPEN, model, java.util.Optional.ofNullable(model.getClass().getCanonicalName()).orElse(model.getClass().getName())));
   }

   public static void open(Object model, String label) {
      org.greenrobot.eventbus.EventBus.getDefault().post(new EVENTS.Event(TYPES.OPEN, model, label));
   }

   public static void run(java.util.concurrent.Future<?> model) {
      org.greenrobot.eventbus.EventBus.getDefault().post(new EVENTS.Event(nextgen.lambda.EVENTS.TYPES.FUTURE, model, "Future"));
   }

   public static void close(Object model) {
      org.greenrobot.eventbus.EventBus.getDefault().post(new EVENTS.Event(TYPES.CLOSE, model, "Close"));
   }

   public static class Event implements Comparable<nextgen.lambda.EVENTS.Event> {

      public final TYPES type;
      private final String label;
      final long timestamp = System.currentTimeMillis();
      final java.util.UUID uuid = java.util.UUID.randomUUID();
      public final Object model;

      protected Event(nextgen.lambda.EVENTS.TYPES type, Object model, String label) {
         this.type = type;
         this.label = label;
         this.model = model;
      }

      @Override
      public boolean equals(Object o) {
         if (this == o) return true;
         if (o == null || getClass() != o.getClass()) return false;
         nextgen.lambda.EVENTS.Event that = (nextgen.lambda.EVENTS.Event) o;
         return uuid.equals(that.uuid);
      }

      @Override
      public int hashCode() {
         return uuid.hashCode();
      }

      @Override
      public int compareTo(nextgen.lambda.EVENTS.Event other) {
         int timestampCompare = Long.compare(timestamp, other.timestamp);
         if (timestampCompare != 0) return timestampCompare;
         return uuid.compareTo(other.uuid);
      }

      @Override
      public String toString() {
         return String.join(" ", type.name(), Long.toString(timestamp), uuid.toString());
      }

      public String label() {
         return this.label;
      }
   }
}