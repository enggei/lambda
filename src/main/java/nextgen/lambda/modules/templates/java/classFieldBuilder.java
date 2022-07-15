package nextgen.lambda.modules.templates.java;

public class classFieldBuilder extends io.vertx.core.json.JsonObject {

	public classFieldBuilder() {
	    put("_template", "classField");
	}

	public classFieldBuilder(io.vertx.core.json.JsonObject model) {
	    super(model.toBuffer());
	}

	public classFieldBuilder set_scope(Object value) {
	    put("scope", value);
	    return this;
	}

	public classFieldBuilder set_name(Object value) {
	    put("name", value);
	    return this;
	}

	public classFieldBuilder set_type(Object value) {
	    put("type", value);
	    return this;
	}

	public classFieldBuilder set_init(Object value) {
	    put("init", value);
	    return this;
	}
}