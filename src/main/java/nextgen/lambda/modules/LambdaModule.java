package nextgen.lambda.modules;

public interface LambdaModule extends Comparable<LambdaModule> {

   LambdaModule register(nextgen.lambda.CONTEXT context);

   LambdaModule register(nextgen.lambda.ui.UI ui);

   java.util.Collection<javax.swing.Action> objectActions(Object model);

   java.util.Optional<javax.swing.JComponent> editor(Object model);
}