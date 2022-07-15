package nextgen.lambda.modules.templates.java;

public class classDeclarationBuilder extends io.vertx.core.json.JsonObject {

	public classDeclarationBuilder() {
	    put("_template", "classDeclaration");
	    put("annotations", new io.vertx.core.json.JsonArray());
	    put("typeParameters", new io.vertx.core.json.JsonArray());
	    put("implement", new io.vertx.core.json.JsonArray());
	    put("fields", new io.vertx.core.json.JsonArray());
	    put("constructors", new io.vertx.core.json.JsonArray());
	    put("methods", new io.vertx.core.json.JsonArray());
	    put("nested", new io.vertx.core.json.JsonArray());
	}

	public classDeclarationBuilder(io.vertx.core.json.JsonObject model) {
	    super(model.toBuffer());
	}

	public classDeclarationBuilder add_annotations(Object value) {
	    getJsonArray("annotations").add(value);
	    return this;
	}

	public classDeclarationBuilder set_scope(Object value) {
	    put("scope", value);
	    return this;
	}

	public classDeclarationBuilder set_name(Object value) {
	    put("name", value);
	    return this;
	}

	public classDeclarationBuilder add_typeParameters(Object value) {
	    getJsonArray("typeParameters").add(value);
	    return this;
	}

	public classDeclarationBuilder set_extending(Object value) {
	    put("extending", value);
	    return this;
	}

	public classDeclarationBuilder add_implement(Object value) {
	    getJsonArray("implement").add(value);
	    return this;
	}

	public classDeclarationBuilder add_fields(Object value) {
	    getJsonArray("fields").add(value);
	    return this;
	}

	public classDeclarationBuilder add_constructors(Object value) {
	    getJsonArray("constructors").add(value);
	    return this;
	}

	public classDeclarationBuilder add_methods(Object value) {
	    getJsonArray("methods").add(value);
	    return this;
	}

	public classDeclarationBuilder add_nested(Object value) {
	    getJsonArray("nested").add(value);
	    return this;
	}
}