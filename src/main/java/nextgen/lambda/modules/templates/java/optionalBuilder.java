package nextgen.lambda.modules.templates.java;

public class optionalBuilder extends io.vertx.core.json.JsonObject {

	public optionalBuilder() {
	    put("_template", "optional");
	}

	public optionalBuilder(io.vertx.core.json.JsonObject model) {
	    super(model.toBuffer());
	}

	public optionalBuilder set_type(Object value) {
	    put("type", value);
	    return this;
	}
}