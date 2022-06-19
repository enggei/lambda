package nextgen.lambda.domain.core;

public class Model {

	public String path;
	public io.vertx.core.json.JsonObject delegate;

	public Model(String path) {
	    this.path = path;
	}

	public java.util.stream.Stream<Package> packages() {
	    return delegate.getJsonArray("packages", new io.vertx.core.json.JsonArray())
	             .stream()
	             .map(element -> (io.vertx.core.json.JsonObject) element)
	             .map(Package::new);
	}
}