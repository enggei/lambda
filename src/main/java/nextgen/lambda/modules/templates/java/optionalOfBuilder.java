package nextgen.lambda.modules.templates.java;

public class optionalOfBuilder extends io.vertx.core.json.JsonObject {

	public optionalOfBuilder() {
	    put("_template", "optionalOf");
	}

	public optionalOfBuilder(io.vertx.core.json.JsonObject model) {
	    super(model.toBuffer());
	}

	public optionalOfBuilder set_value(Object value) {
	    put("value", value);
	    return this;
	}
}