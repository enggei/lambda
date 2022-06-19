package nextgen.lambda.domain.core;

public class Enum {

	public io.vertx.core.json.JsonObject delegate;

	public Enum(io.vertx.core.json.JsonObject delegate) {
	    this.delegate = delegate;
	}

	public String name() {
	    return delegate.getString("name");
	}

	public String values() {
	    return delegate.getString("values");
	}
}