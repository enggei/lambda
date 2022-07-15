package nextgen.lambda.modules.templates.java;

public class compilationUnitBuilder extends io.vertx.core.json.JsonObject {

	public compilationUnitBuilder() {
	    put("_template", "compilationUnit");
	    put("imports", new io.vertx.core.json.JsonArray());
	    put("members", new io.vertx.core.json.JsonArray());
	}

	public compilationUnitBuilder(io.vertx.core.json.JsonObject model) {
	    super(model.toBuffer());
	}

	public compilationUnitBuilder set_packageName(Object value) {
	    put("packageName", value);
	    return this;
	}

	public compilationUnitBuilder add_imports(Object value) {
	    getJsonArray("imports").add(value);
	    return this;
	}

	public compilationUnitBuilder add_members(Object value) {
	    getJsonArray("members").add(value);
	    return this;
	}
}