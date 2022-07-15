package nextgen.lambda;

public class FORMATS {

   public static final long SECONDms = 1000;
   public static final long MINUTEms = SECONDms * 60L;
   public static final long HOURms = MINUTEms * 60L;
   public static final long DAYms = HOURms * 24L;

   public static String formatTime(long runningTime) {

      final boolean negative = runningTime < 0;

      runningTime = Math.abs(runningTime);

      final StringBuilder t = new StringBuilder();
      final long d = runningTime / DAYms;
      if (d > 0L) {
         t.append(" ").append(d).append(" day").append(d == 1 ? "" : "s");
         runningTime %= DAYms;
      }
      final long h = runningTime / HOURms;
      if (h > 0L) {
         t.append(" ").append(h).append(" hour").append(h == 1 ? "" : "s");
         runningTime %= HOURms;
      }
      final long m = runningTime / MINUTEms;
      if (m > 0L) {
         t.append(" ").append(m).append(" minute").append(m == 1 ? "" : "s");
         runningTime %= MINUTEms;
      }
      final long s = runningTime / SECONDms;
      if (s > 0L) {
         t.append(" ").append(s).append(" second").append(s == 1 ? "" : "s");
         runningTime %= SECONDms;
      }

      final String x = t.toString().trim();
      return (negative ? "-" : "") + (x.length() == 0 ? (runningTime + " ms") : x);
   }
}