package nextgen.lambda.modules.templates.java;

public class variableBuilder extends io.vertx.core.json.JsonObject {

	public variableBuilder() {
	    put("_template", "variable");
	}

	public variableBuilder(io.vertx.core.json.JsonObject model) {
	    super(model.toBuffer());
	}

	public variableBuilder set_scope(Object value) {
	    put("scope", value);
	    return this;
	}

	public variableBuilder set_name(Object value) {
	    put("name", value);
	    return this;
	}

	public variableBuilder set_type(Object value) {
	    put("type", value);
	    return this;
	}

	public variableBuilder set_init(Object value) {
	    put("init", value);
	    return this;
	}
}