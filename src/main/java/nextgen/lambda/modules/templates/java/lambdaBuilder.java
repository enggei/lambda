package nextgen.lambda.modules.templates.java;

public class lambdaBuilder extends io.vertx.core.json.JsonObject {

	public lambdaBuilder() {
	    put("_template", "lambda");
	    put("arguments", new io.vertx.core.json.JsonArray());
	    put("statements", new io.vertx.core.json.JsonArray());
	}

	public lambdaBuilder(io.vertx.core.json.JsonObject model) {
	    super(model.toBuffer());
	}

	public lambdaBuilder add_arguments(Object value) {
	    getJsonArray("arguments").add(value);
	    return this;
	}

	public lambdaBuilder add_statements(Object value) {
	    getJsonArray("statements").add(value);
	    return this;
	}
}