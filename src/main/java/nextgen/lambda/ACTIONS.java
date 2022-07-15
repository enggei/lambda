package nextgen.lambda;

public class ACTIONS {

   private static final java.util.logging.Logger log = LOG.logger(ACTIONS.class);

   static final java.util.concurrent.ExecutorService EXECUTOR = java.util.concurrent.Executors.newFixedThreadPool(20);

//   public static nextgen.lambda.ACTIONS.ExecutorAction newAction(String name, Runnable action) {
//      return new nextgen.lambda.ACTIONS.ExecutorAction(name, action);
//   }
//
//   public static nextgen.lambda.ACTIONS.ExecutorAction newAction(String name, String keycode, Runnable action) {
//      return new nextgen.lambda.ACTIONS.ExecutorAction(name, keycode, action);
//   }

   public static nextgen.lambda.ACTIONS.UIAction newAction(String name, Runnable action) {
      return new ACTIONS.UIAction(name, action);
   }

   public static nextgen.lambda.ACTIONS.UIAction newAction(String name, String keycode, Runnable action) {
      return new ACTIONS.UIAction(name, keycode, action);
   }

   public static void runKeyAction(org.piccolo2d.event.PInputEvent inputEvent, java.util.Collection<javax.swing.Action> actions) {
      runAction(inputEvent.isControlDown(), inputEvent.getKeyCode(), actions);
   }

   public static void runKeyAction(java.awt.event.KeyEvent keyEvent, java.util.Collection<javax.swing.Action> actions) {
      runAction(keyEvent.isControlDown(), keyEvent.getKeyCode(), actions);
   }

   public static void runAction(boolean isControlDown, int keyCode, java.util.Collection<javax.swing.Action> actions) {
      final String keyText = getKeyText(isControlDown, keyCode);
      actions.stream()
         .filter(action -> keyText.equals(action.getValue(javax.swing.Action.ACTION_COMMAND_KEY)))
         .findFirst()
         .ifPresent(action -> action.actionPerformed(null));
   }

   public static String getKeyText(boolean isControlDown, int keyCode) {
      final String keyText = String.join(" ", isControlDown ? "control" : "", java.awt.event.KeyEvent.getKeyText(keyCode)).trim();
      log.info("key " + keyText);
      return keyText;
   }

   public static void run(Runnable runnable) {
      EXECUTOR.submit(runnable);
   }

//   public static final class ExecutorAction extends javax.swing.AbstractAction {
//
//      final Runnable delegate;
//
//      public ExecutorAction(String name, Runnable delegate) {
//         super(name);
//         this.delegate = delegate;
//      }
//
//      public ExecutorAction(String name, String keycode, Runnable delegate) {
//         super(name);
//         this.delegate = delegate;
//         putValue(javax.swing.Action.ACTION_COMMAND_KEY, keycode);
//      }
//
//      @Override
//      public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
//         javax.swing.SwingUtilities.invokeLater(() -> {
//            log.info(getValue(javax.swing.Action.NAME) + " " + delegate.toString());
//            EVENTS.run(EXECUTOR.submit(delegate));
//         });
//      }
//   }

   public static final class UIAction extends javax.swing.AbstractAction {

      final Runnable delegate;

      public UIAction(String name, Runnable delegate) {
         super(name);
         this.delegate = delegate;
      }

      public UIAction(String name, String keycode, Runnable delegate) {
         super(name);
         this.delegate = delegate;
         putValue(javax.swing.Action.ACTION_COMMAND_KEY, keycode);
      }

      @Override
      public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
         javax.swing.SwingUtilities.invokeLater(() -> {
            log.info(getValue(javax.swing.Action.NAME) + " " + delegate.toString());
            delegate.run();
         });
      }
   }
}
