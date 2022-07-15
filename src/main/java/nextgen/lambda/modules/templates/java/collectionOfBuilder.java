package nextgen.lambda.modules.templates.java;

public class collectionOfBuilder extends io.vertx.core.json.JsonObject {

	public collectionOfBuilder() {
	    put("_template", "collectionOf");
	}

	public collectionOfBuilder(io.vertx.core.json.JsonObject model) {
	    super(model.toBuffer());
	}

	public collectionOfBuilder set_type(Object value) {
	    put("type", value);
	    return this;
	}
}