package nextgen.lambda.modules.templates.java;

public class importDeclarationBuilder extends io.vertx.core.json.JsonObject {

	public importDeclarationBuilder() {
	    put("_template", "importDeclaration");
	}

	public importDeclarationBuilder(io.vertx.core.json.JsonObject model) {
	    super(model.toBuffer());
	}

	public importDeclarationBuilder set_name(Object value) {
	    put("name", value);
	    return this;
	}

	public importDeclarationBuilder set_isStatic(Object value) {
	    put("isStatic", value);
	    return this;
	}

	public importDeclarationBuilder set_isAsterisk(Object value) {
	    put("isAsterisk", value);
	    return this;
	}
}