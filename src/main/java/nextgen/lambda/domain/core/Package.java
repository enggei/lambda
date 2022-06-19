package nextgen.lambda.domain.core;

public class Package {

	public io.vertx.core.json.JsonObject delegate;

	public Package(io.vertx.core.json.JsonObject delegate) {
	    this.delegate = delegate;
	}

	public String name() {
	    return delegate.getString("name");
	}

	public java.util.stream.Stream<Entity> entities() {
	    return delegate.getJsonArray("entities", new io.vertx.core.json.JsonArray())
	             .stream()
	             .map(element -> (io.vertx.core.json.JsonObject) element)
	             .map(Entity::new);
	}

	public java.util.stream.Stream<Enum> enums() {
	    return delegate.getJsonArray("enums", new io.vertx.core.json.JsonArray())
	             .stream()
	             .map(element -> (io.vertx.core.json.JsonObject) element)
	             .map(Enum::new);
	}

	public java.util.stream.Stream<Interface> interfaces() {
	    return delegate.getJsonArray("interfaces", new io.vertx.core.json.JsonArray())
	             .stream()
	             .map(element -> (io.vertx.core.json.JsonObject) element)
	             .map(Interface::new);
	}
}