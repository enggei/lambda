package nextgen.lambda.domain.actions;

@FunctionalInterface
public interface Task<T>  {
    T run() throws Throwable;
}