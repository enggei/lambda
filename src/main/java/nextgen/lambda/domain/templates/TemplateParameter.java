package nextgen.lambda.domain.templates;

public class TemplateParameter {

	public io.vertx.core.json.JsonObject delegate;

	public TemplateParameter(io.vertx.core.json.JsonObject delegate) {
	    this.delegate = delegate;
	}

	public String name() {
	    return delegate.getString("name");
	}

	public String qty() {
	    return delegate.getString("qty");
	}
}