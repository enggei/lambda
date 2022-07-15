package nextgen.lambda.modules.templates.java;

public class classConstructorBuilder extends io.vertx.core.json.JsonObject {

	public classConstructorBuilder() {
	    put("_template", "classConstructor");
	    put("parameters", new io.vertx.core.json.JsonArray());
	    put("throws", new io.vertx.core.json.JsonArray());
	    put("statements", new io.vertx.core.json.JsonArray());
	}

	public classConstructorBuilder(io.vertx.core.json.JsonObject model) {
	    super(model.toBuffer());
	}

	public classConstructorBuilder set_scope(Object value) {
	    put("scope", value);
	    return this;
	}

	public classConstructorBuilder set_name(Object value) {
	    put("name", value);
	    return this;
	}

	public classConstructorBuilder add_parameters(Object value) {
	    getJsonArray("parameters").add(value);
	    return this;
	}

	public classConstructorBuilder add_throws(Object value) {
	    getJsonArray("throws").add(value);
	    return this;
	}

	public classConstructorBuilder add_statements(Object value) {
	    getJsonArray("statements").add(value);
	    return this;
	}
}