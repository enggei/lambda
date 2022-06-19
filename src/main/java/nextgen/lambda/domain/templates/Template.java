package nextgen.lambda.domain.templates;

public class Template {

	public io.vertx.core.json.JsonObject delegate;

	public Template(io.vertx.core.json.JsonObject delegate) {
	    this.delegate = delegate;
	}

	public String name() {
	    return delegate.getString("name");
	}

	public String content() {
	    return delegate.getString("content");
	}

	public java.util.stream.Stream<TemplateParameter> parameters() {
	    return delegate.getJsonArray("parameters", new io.vertx.core.json.JsonArray())
	             .stream()
	             .map(element -> (io.vertx.core.json.JsonObject) element)
	             .map(TemplateParameter::new);
	}
}