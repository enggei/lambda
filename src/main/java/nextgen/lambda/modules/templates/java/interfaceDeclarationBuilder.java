package nextgen.lambda.modules.templates.java;

public class interfaceDeclarationBuilder extends io.vertx.core.json.JsonObject {

	public interfaceDeclarationBuilder() {
	    put("_template", "interfaceDeclaration");
	    put("annotations", new io.vertx.core.json.JsonArray());
	    put("typeParameters", new io.vertx.core.json.JsonArray());
	    put("members", new io.vertx.core.json.JsonArray());
	}

	public interfaceDeclarationBuilder(io.vertx.core.json.JsonObject model) {
	    super(model.toBuffer());
	}

	public interfaceDeclarationBuilder add_annotations(Object value) {
	    getJsonArray("annotations").add(value);
	    return this;
	}

	public interfaceDeclarationBuilder set_scope(Object value) {
	    put("scope", value);
	    return this;
	}

	public interfaceDeclarationBuilder set_name(Object value) {
	    put("name", value);
	    return this;
	}

	public interfaceDeclarationBuilder add_typeParameters(Object value) {
	    getJsonArray("typeParameters").add(value);
	    return this;
	}

	public interfaceDeclarationBuilder add_members(Object value) {
	    getJsonArray("members").add(value);
	    return this;
	}
}