package nextgen.lambda.domain.core;

public class Parameter {

	public io.vertx.core.json.JsonObject delegate;

	public Parameter(io.vertx.core.json.JsonObject delegate) {
	    this.delegate = delegate;
	}

	public String name() {
	    return delegate.getString("name");
	}

	public String type() {
	    return delegate.getString("type");
	}
}