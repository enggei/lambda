package nextgen.lambda.modules.templates.java;

public class typeBuilder extends io.vertx.core.json.JsonObject {

	public typeBuilder() {
	    put("_template", "type");
	    put("types", new io.vertx.core.json.JsonArray());
	}

	public typeBuilder(io.vertx.core.json.JsonObject model) {
	    super(model.toBuffer());
	}

	public typeBuilder set_name(Object value) {
	    put("name", value);
	    return this;
	}

	public typeBuilder add_types(Object value) {
	    getJsonArray("types").add(value);
	    return this;
	}
}