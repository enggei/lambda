package nextgen.lambda.domain.core;

public class Interface {

	public io.vertx.core.json.JsonObject delegate;

	public Interface(io.vertx.core.json.JsonObject delegate) {
	    this.delegate = delegate;
	}

	public String name() {
	    return delegate.getString("name");
	}

	public java.util.stream.Stream<Method> members() {
	    return delegate.getJsonArray("members", new io.vertx.core.json.JsonArray())
	             .stream()
	             .map(element -> (io.vertx.core.json.JsonObject) element)
	             .map(Method::new);
	}
}