package nextgen.lambda.modules.templates.java;

public class parameterBuilder extends io.vertx.core.json.JsonObject {

	public parameterBuilder() {
	    put("_template", "parameter");
	}

	public parameterBuilder(io.vertx.core.json.JsonObject model) {
	    super(model.toBuffer());
	}

	public parameterBuilder set_type(Object value) {
	    put("type", value);
	    return this;
	}

	public parameterBuilder set_name(Object value) {
	    put("name", value);
	    return this;
	}
}