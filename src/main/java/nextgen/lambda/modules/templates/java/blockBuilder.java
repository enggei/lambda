package nextgen.lambda.modules.templates.java;

public class blockBuilder extends io.vertx.core.json.JsonObject {

	public blockBuilder() {
	    put("_template", "block");
	    put("statements", new io.vertx.core.json.JsonArray());
	}

	public blockBuilder(io.vertx.core.json.JsonObject model) {
	    super(model.toBuffer());
	}

	public blockBuilder add_statements(Object value) {
	    getJsonArray("statements").add(value);
	    return this;
	}
}