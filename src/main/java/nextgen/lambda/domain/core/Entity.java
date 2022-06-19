package nextgen.lambda.domain.core;

public class Entity {

	public io.vertx.core.json.JsonObject delegate;

	public Entity(io.vertx.core.json.JsonObject delegate) {
	    this.delegate = delegate;
	}

	public String name() {
	    return delegate.getString("name");
	}

	public java.util.stream.Stream<Field> fields() {
	    return delegate.getJsonArray("fields", new io.vertx.core.json.JsonArray())
	             .stream()
	             .map(element -> (io.vertx.core.json.JsonObject) element)
	             .map(Field::new);
	}

	public java.util.stream.Stream<Method> methods() {
	    return delegate.getJsonArray("methods", new io.vertx.core.json.JsonArray())
	             .stream()
	             .map(element -> (io.vertx.core.json.JsonObject) element)
	             .map(Method::new);
	}
}