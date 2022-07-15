package nextgen.lambda.modules.templates.java;

public class newInstanceBuilder extends io.vertx.core.json.JsonObject {

	public newInstanceBuilder() {
	    put("_template", "newInstance");
	    put("arguments", new io.vertx.core.json.JsonArray());
	}

	public newInstanceBuilder(io.vertx.core.json.JsonObject model) {
	    super(model.toBuffer());
	}

	public newInstanceBuilder set_type(Object value) {
	    put("type", value);
	    return this;
	}

	public newInstanceBuilder add_arguments(Object value) {
	    getJsonArray("arguments").add(value);
	    return this;
	}
}