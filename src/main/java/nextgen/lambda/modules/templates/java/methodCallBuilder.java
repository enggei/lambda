package nextgen.lambda.modules.templates.java;

public class methodCallBuilder extends io.vertx.core.json.JsonObject {

	public methodCallBuilder() {
	    put("_template", "methodCall");
	    put("arguments", new io.vertx.core.json.JsonArray());
	}

	public methodCallBuilder(io.vertx.core.json.JsonObject model) {
	    super(model.toBuffer());
	}

	public methodCallBuilder set_scope(Object value) {
	    put("scope", value);
	    return this;
	}

	public methodCallBuilder set_name(Object value) {
	    put("name", value);
	    return this;
	}

	public methodCallBuilder add_arguments(Object value) {
	    getJsonArray("arguments").add(value);
	    return this;
	}
}