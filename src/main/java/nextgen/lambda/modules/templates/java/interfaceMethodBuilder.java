package nextgen.lambda.modules.templates.java;

public class interfaceMethodBuilder extends io.vertx.core.json.JsonObject {

	public interfaceMethodBuilder() {
	    put("_template", "interfaceMethod");
	    put("annotations", new io.vertx.core.json.JsonArray());
	    put("parameters", new io.vertx.core.json.JsonArray());
	    put("throws", new io.vertx.core.json.JsonArray());
	}

	public interfaceMethodBuilder(io.vertx.core.json.JsonObject model) {
	    super(model.toBuffer());
	}

	public interfaceMethodBuilder set_scope(Object value) {
	    put("scope", value);
	    return this;
	}

	public interfaceMethodBuilder set_name(Object value) {
	    put("name", value);
	    return this;
	}

	public interfaceMethodBuilder set_type(Object value) {
	    put("type", value);
	    return this;
	}

	public interfaceMethodBuilder add_annotations(Object value) {
	    getJsonArray("annotations").add(value);
	    return this;
	}

	public interfaceMethodBuilder add_parameters(Object value) {
	    getJsonArray("parameters").add(value);
	    return this;
	}

	public interfaceMethodBuilder add_throws(Object value) {
	    getJsonArray("throws").add(value);
	    return this;
	}
}