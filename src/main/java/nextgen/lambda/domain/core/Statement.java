package nextgen.lambda.domain.core;

public class Statement {

	public io.vertx.core.json.JsonObject delegate;

	public Statement(io.vertx.core.json.JsonObject delegate) {
	    this.delegate = delegate;
	}

	public String expression() {
	    return delegate.getString("expression");
	}

	public String name() {
	    return delegate.getString("name");
	}
}