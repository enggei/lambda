package nextgen.lambda.modules.templates.java;

public class statementBuilder extends io.vertx.core.json.JsonObject {

	public statementBuilder() {
	    put("_template", "statement");
	}

	public statementBuilder(io.vertx.core.json.JsonObject model) {
	    super(model.toBuffer());
	}

	public statementBuilder set_expression(Object value) {
	    put("expression", value);
	    return this;
	}
}