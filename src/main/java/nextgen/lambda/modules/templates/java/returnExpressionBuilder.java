package nextgen.lambda.modules.templates.java;

public class returnExpressionBuilder extends io.vertx.core.json.JsonObject {

	public returnExpressionBuilder() {
	    put("_template", "returnExpression");
	}

	public returnExpressionBuilder(io.vertx.core.json.JsonObject model) {
	    super(model.toBuffer());
	}

	public returnExpressionBuilder set_expression(Object value) {
	    put("expression", value);
	    return this;
	}
}