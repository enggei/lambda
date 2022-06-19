package nextgen.lambda.domain.core;

public class Method {

	public io.vertx.core.json.JsonObject delegate;

	public Method(io.vertx.core.json.JsonObject delegate) {
	    this.delegate = delegate;
	}

	public String name() {
	    return delegate.getString("name");
	}

	public String type() {
	    return delegate.getString("type");
	}

	public java.util.stream.Stream<Parameter> parameters() {
	    return delegate.getJsonArray("packages", new io.vertx.core.json.JsonArray())
	             .stream()
	             .map(element -> (io.vertx.core.json.JsonObject) element)
	             .map(Parameter::new);
	}

	public java.util.stream.Stream<Statement> statements() {
	    return delegate.getJsonArray("packages", new io.vertx.core.json.JsonArray())
	             .stream()
	             .map(element -> (io.vertx.core.json.JsonObject) element)
	             .map(Statement::new);
	}
}