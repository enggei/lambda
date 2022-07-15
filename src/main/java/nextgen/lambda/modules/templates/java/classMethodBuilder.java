package nextgen.lambda.modules.templates.java;

public class classMethodBuilder extends io.vertx.core.json.JsonObject {

	public classMethodBuilder() {
	    put("_template", "classMethod");
	    put("annotations", new io.vertx.core.json.JsonArray());
	    put("parameters", new io.vertx.core.json.JsonArray());
	    put("throws", new io.vertx.core.json.JsonArray());
	    put("statements", new io.vertx.core.json.JsonArray());
	}

	public classMethodBuilder(io.vertx.core.json.JsonObject model) {
	    super(model.toBuffer());
	}

	public classMethodBuilder set_scope(Object value) {
	    put("scope", value);
	    return this;
	}

	public classMethodBuilder set_name(Object value) {
	    put("name", value);
	    return this;
	}

	public classMethodBuilder set_type(Object value) {
	    put("type", value);
	    return this;
	}

	public classMethodBuilder add_annotations(Object value) {
	    getJsonArray("annotations").add(value);
	    return this;
	}

	public classMethodBuilder add_parameters(Object value) {
	    getJsonArray("parameters").add(value);
	    return this;
	}

	public classMethodBuilder add_throws(Object value) {
	    getJsonArray("throws").add(value);
	    return this;
	}

	public classMethodBuilder add_statements(Object value) {
	    getJsonArray("statements").add(value);
	    return this;
	}
}