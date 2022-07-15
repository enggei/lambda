package nextgen.lambda.modules.templates.java;

public class annotationBuilder extends io.vertx.core.json.JsonObject {

	public annotationBuilder() {
	    put("_template", "annotation");
	}

	public annotationBuilder(io.vertx.core.json.JsonObject model) {
	    super(model.toBuffer());
	}

	public annotationBuilder set_name(Object value) {
	    put("name", value);
	    return this;
	}
}