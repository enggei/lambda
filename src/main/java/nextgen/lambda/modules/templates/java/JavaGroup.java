package nextgen.lambda.modules.templates.java;

public class JavaGroup {

	org.stringtemplate.v4.STGroup stGroup;

	public JavaGroup(org.stringtemplate.v4.STGroup stGroup) {
	    this.stGroup = stGroup;
	}

	public JavaGroup(java.nio.file.Path path) {
	    this.stGroup = new org.stringtemplate.v4.STGroupFile(path.toString(), '~', '~');
	}

	public static enumDeclarationBuilder new_enumDeclarationBuilder() {
	    return new enumDeclarationBuilder();
	}

	public String render_enumDeclaration(io.vertx.core.json.JsonObject model) {
	    final org.stringtemplate.v4.ST st = stGroup.getInstanceOf("enumDeclaration");
	    model.getJsonArray("annotations", new io.vertx.core.json.JsonArray()).stream().map(this::render).forEach(element -> st.add("annotations", element));
	    st.add("scope", render(model.getValue("scope", null)));
	    st.add("name", render(model.getValue("name", null)));
	    model.getJsonArray("values", new io.vertx.core.json.JsonArray()).stream().map(this::render).forEach(element -> st.add("values", element));
	    return st.render();
	}

	public static classConstructorBuilder new_classConstructorBuilder() {
	    return new classConstructorBuilder();
	}

	public String render_classConstructor(io.vertx.core.json.JsonObject model) {
	    final org.stringtemplate.v4.ST st = stGroup.getInstanceOf("classConstructor");
	    st.add("scope", render(model.getValue("scope", null)));
	    st.add("name", render(model.getValue("name", null)));
	    model.getJsonArray("parameters", new io.vertx.core.json.JsonArray()).stream().map(this::render).forEach(element -> st.add("parameters", element));
	    model.getJsonArray("throws", new io.vertx.core.json.JsonArray()).stream().map(this::render).forEach(element -> st.add("throws", element));
	    model.getJsonArray("statements", new io.vertx.core.json.JsonArray()).stream().map(this::render).forEach(element -> st.add("statements", element));
	    return st.render();
	}

	public static statementBuilder new_statementBuilder() {
	    return new statementBuilder();
	}

	public String render_statement(io.vertx.core.json.JsonObject model) {
	    final org.stringtemplate.v4.ST st = stGroup.getInstanceOf("statement");
	    st.add("expression", render(model.getValue("expression", null)));
	    return st.render();
	}

	public static methodCallBuilder new_methodCallBuilder() {
	    return new methodCallBuilder();
	}

	public String render_methodCall(io.vertx.core.json.JsonObject model) {
	    final org.stringtemplate.v4.ST st = stGroup.getInstanceOf("methodCall");
	    st.add("scope", render(model.getValue("scope", null)));
	    st.add("name", render(model.getValue("name", null)));
	    model.getJsonArray("arguments", new io.vertx.core.json.JsonArray()).stream().map(this::render).forEach(element -> st.add("arguments", element));
	    return st.render();
	}

	public static classFieldBuilder new_classFieldBuilder() {
	    return new classFieldBuilder();
	}

	public String render_classField(io.vertx.core.json.JsonObject model) {
	    final org.stringtemplate.v4.ST st = stGroup.getInstanceOf("classField");
	    st.add("scope", render(model.getValue("scope", null)));
	    st.add("name", render(model.getValue("name", null)));
	    st.add("type", render(model.getValue("type", null)));
	    st.add("init", render(model.getValue("init", null)));
	    return st.render();
	}

	public static parameterBuilder new_parameterBuilder() {
	    return new parameterBuilder();
	}

	public String render_parameter(io.vertx.core.json.JsonObject model) {
	    final org.stringtemplate.v4.ST st = stGroup.getInstanceOf("parameter");
	    st.add("type", render(model.getValue("type", null)));
	    st.add("name", render(model.getValue("name", null)));
	    return st.render();
	}

	public static typeBuilder new_typeBuilder() {
	    return new typeBuilder();
	}

	public String render_type(io.vertx.core.json.JsonObject model) {
	    final org.stringtemplate.v4.ST st = stGroup.getInstanceOf("type");
	    st.add("name", render(model.getValue("name", null)));
	    model.getJsonArray("types", new io.vertx.core.json.JsonArray()).stream().map(this::render).forEach(element -> st.add("types", element));
	    return st.render();
	}

	public static optionalOfBuilder new_optionalOfBuilder() {
	    return new optionalOfBuilder();
	}

	public String render_optionalOf(io.vertx.core.json.JsonObject model) {
	    final org.stringtemplate.v4.ST st = stGroup.getInstanceOf("optionalOf");
	    st.add("value", render(model.getValue("value", null)));
	    return st.render();
	}

	public static lambdaBuilder new_lambdaBuilder() {
	    return new lambdaBuilder();
	}

	public String render_lambda(io.vertx.core.json.JsonObject model) {
	    final org.stringtemplate.v4.ST st = stGroup.getInstanceOf("lambda");
	    model.getJsonArray("arguments", new io.vertx.core.json.JsonArray()).stream().map(this::render).forEach(element -> st.add("arguments", element));
	    model.getJsonArray("statements", new io.vertx.core.json.JsonArray()).stream().map(this::render).forEach(element -> st.add("statements", element));
	    return st.render();
	}

	public static variableBuilder new_variableBuilder() {
	    return new variableBuilder();
	}

	public String render_variable(io.vertx.core.json.JsonObject model) {
	    final org.stringtemplate.v4.ST st = stGroup.getInstanceOf("variable");
	    st.add("scope", render(model.getValue("scope", null)));
	    st.add("name", render(model.getValue("name", null)));
	    st.add("type", render(model.getValue("type", null)));
	    st.add("init", render(model.getValue("init", null)));
	    return st.render();
	}

	public static compilationUnitBuilder new_compilationUnitBuilder() {
	    return new compilationUnitBuilder();
	}

	public String render_compilationUnit(io.vertx.core.json.JsonObject model) {
	    final org.stringtemplate.v4.ST st = stGroup.getInstanceOf("compilationUnit");
	    st.add("packageName", render(model.getValue("packageName", null)));
	    model.getJsonArray("imports", new io.vertx.core.json.JsonArray()).stream().map(this::render).forEach(element -> st.add("imports", element));
	    model.getJsonArray("members", new io.vertx.core.json.JsonArray()).stream().map(this::render).forEach(element -> st.add("members", element));
	    return st.render();
	}

	public static interfaceDeclarationBuilder new_interfaceDeclarationBuilder() {
	    return new interfaceDeclarationBuilder();
	}

	public String render_interfaceDeclaration(io.vertx.core.json.JsonObject model) {
	    final org.stringtemplate.v4.ST st = stGroup.getInstanceOf("interfaceDeclaration");
	    model.getJsonArray("annotations", new io.vertx.core.json.JsonArray()).stream().map(this::render).forEach(element -> st.add("annotations", element));
	    st.add("scope", render(model.getValue("scope", null)));
	    st.add("name", render(model.getValue("name", null)));
	    model.getJsonArray("typeParameters", new io.vertx.core.json.JsonArray()).stream().map(this::render).forEach(element -> st.add("typeParameters", element));
	    model.getJsonArray("members", new io.vertx.core.json.JsonArray()).stream().map(this::render).forEach(element -> st.add("members", element));
	    return st.render();
	}

	public static classDeclarationBuilder new_classDeclarationBuilder() {
	    return new classDeclarationBuilder();
	}

	public String render_classDeclaration(io.vertx.core.json.JsonObject model) {
	    final org.stringtemplate.v4.ST st = stGroup.getInstanceOf("classDeclaration");
	    model.getJsonArray("annotations", new io.vertx.core.json.JsonArray()).stream().map(this::render).forEach(element -> st.add("annotations", element));
	    st.add("scope", render(model.getValue("scope", null)));
	    st.add("name", render(model.getValue("name", null)));
	    model.getJsonArray("typeParameters", new io.vertx.core.json.JsonArray()).stream().map(this::render).forEach(element -> st.add("typeParameters", element));
	    st.add("extending", render(model.getValue("extending", null)));
	    model.getJsonArray("implement", new io.vertx.core.json.JsonArray()).stream().map(this::render).forEach(element -> st.add("implement", element));
	    model.getJsonArray("fields", new io.vertx.core.json.JsonArray()).stream().map(this::render).forEach(element -> st.add("fields", element));
	    model.getJsonArray("constructors", new io.vertx.core.json.JsonArray()).stream().map(this::render).forEach(element -> st.add("constructors", element));
	    model.getJsonArray("methods", new io.vertx.core.json.JsonArray()).stream().map(this::render).forEach(element -> st.add("methods", element));
	    model.getJsonArray("nested", new io.vertx.core.json.JsonArray()).stream().map(this::render).forEach(element -> st.add("nested", element));
	    return st.render();
	}

	public static interfaceMethodBuilder new_interfaceMethodBuilder() {
	    return new interfaceMethodBuilder();
	}

	public String render_interfaceMethod(io.vertx.core.json.JsonObject model) {
	    final org.stringtemplate.v4.ST st = stGroup.getInstanceOf("interfaceMethod");
	    st.add("scope", render(model.getValue("scope", null)));
	    st.add("name", render(model.getValue("name", null)));
	    st.add("type", render(model.getValue("type", null)));
	    model.getJsonArray("annotations", new io.vertx.core.json.JsonArray()).stream().map(this::render).forEach(element -> st.add("annotations", element));
	    model.getJsonArray("parameters", new io.vertx.core.json.JsonArray()).stream().map(this::render).forEach(element -> st.add("parameters", element));
	    model.getJsonArray("throws", new io.vertx.core.json.JsonArray()).stream().map(this::render).forEach(element -> st.add("throws", element));
	    return st.render();
	}

	public static annotationBuilder new_annotationBuilder() {
	    return new annotationBuilder();
	}

	public String render_annotation(io.vertx.core.json.JsonObject model) {
	    final org.stringtemplate.v4.ST st = stGroup.getInstanceOf("annotation");
	    st.add("name", render(model.getValue("name", null)));
	    return st.render();
	}

	public static returnExpressionBuilder new_returnExpressionBuilder() {
	    return new returnExpressionBuilder();
	}

	public String render_returnExpression(io.vertx.core.json.JsonObject model) {
	    final org.stringtemplate.v4.ST st = stGroup.getInstanceOf("returnExpression");
	    st.add("expression", render(model.getValue("expression", null)));
	    return st.render();
	}

	public static optionalBuilder new_optionalBuilder() {
	    return new optionalBuilder();
	}

	public String render_optional(io.vertx.core.json.JsonObject model) {
	    final org.stringtemplate.v4.ST st = stGroup.getInstanceOf("optional");
	    st.add("type", render(model.getValue("type", null)));
	    return st.render();
	}

	public static collectionOfBuilder new_collectionOfBuilder() {
	    return new collectionOfBuilder();
	}

	public String render_collectionOf(io.vertx.core.json.JsonObject model) {
	    final org.stringtemplate.v4.ST st = stGroup.getInstanceOf("collectionOf");
	    st.add("type", render(model.getValue("type", null)));
	    return st.render();
	}

	public static importDeclarationBuilder new_importDeclarationBuilder() {
	    return new importDeclarationBuilder();
	}

	public String render_importDeclaration(io.vertx.core.json.JsonObject model) {
	    final org.stringtemplate.v4.ST st = stGroup.getInstanceOf("importDeclaration");
	    st.add("name", render(model.getValue("name", null)));
	    st.add("isStatic", render(model.getValue("isStatic", null)));
	    st.add("isAsterisk", render(model.getValue("isAsterisk", null)));
	    return st.render();
	}

	public static newInstanceBuilder new_newInstanceBuilder() {
	    return new newInstanceBuilder();
	}

	public String render_newInstance(io.vertx.core.json.JsonObject model) {
	    final org.stringtemplate.v4.ST st = stGroup.getInstanceOf("newInstance");
	    st.add("type", render(model.getValue("type", null)));
	    model.getJsonArray("arguments", new io.vertx.core.json.JsonArray()).stream().map(this::render).forEach(element -> st.add("arguments", element));
	    return st.render();
	}

	public static classMethodBuilder new_classMethodBuilder() {
	    return new classMethodBuilder();
	}

	public String render_classMethod(io.vertx.core.json.JsonObject model) {
	    final org.stringtemplate.v4.ST st = stGroup.getInstanceOf("classMethod");
	    st.add("scope", render(model.getValue("scope", null)));
	    st.add("name", render(model.getValue("name", null)));
	    st.add("type", render(model.getValue("type", null)));
	    model.getJsonArray("annotations", new io.vertx.core.json.JsonArray()).stream().map(this::render).forEach(element -> st.add("annotations", element));
	    model.getJsonArray("parameters", new io.vertx.core.json.JsonArray()).stream().map(this::render).forEach(element -> st.add("parameters", element));
	    model.getJsonArray("throws", new io.vertx.core.json.JsonArray()).stream().map(this::render).forEach(element -> st.add("throws", element));
	    model.getJsonArray("statements", new io.vertx.core.json.JsonArray()).stream().map(this::render).forEach(element -> st.add("statements", element));
	    return st.render();
	}

	public static blockBuilder new_blockBuilder() {
	    return new blockBuilder();
	}

	public String render_block(io.vertx.core.json.JsonObject model) {
	    final org.stringtemplate.v4.ST st = stGroup.getInstanceOf("block");
	    model.getJsonArray("statements", new io.vertx.core.json.JsonArray()).stream().map(this::render).forEach(element -> st.add("statements", element));
	    return st.render();
	}

	public String render(Object model) {
	    if (model == null) return null;
	    if (model instanceof String) return model.toString();
	    if (model instanceof io.vertx.core.json.JsonObject) {
	    io.vertx.core.json.JsonObject jo = (io.vertx.core.json.JsonObject) model;
	    switch (jo.getString("_template", "_empty")) {
	    	case "enumDeclaration": return render_enumDeclaration(jo);
	    	case "classConstructor": return render_classConstructor(jo);
	    	case "statement": return render_statement(jo);
	    	case "methodCall": return render_methodCall(jo);
	    	case "classField": return render_classField(jo);
	    	case "parameter": return render_parameter(jo);
	    	case "type": return render_type(jo);
	    	case "optionalOf": return render_optionalOf(jo);
	    	case "lambda": return render_lambda(jo);
	    	case "variable": return render_variable(jo);
	    	case "compilationUnit": return render_compilationUnit(jo);
	    	case "interfaceDeclaration": return render_interfaceDeclaration(jo);
	    	case "classDeclaration": return render_classDeclaration(jo);
	    	case "interfaceMethod": return render_interfaceMethod(jo);
	    	case "annotation": return render_annotation(jo);
	    	case "returnExpression": return render_returnExpression(jo);
	    	case "optional": return render_optional(jo);
	    	case "collectionOf": return render_collectionOf(jo);
	    	case "importDeclaration": return render_importDeclaration(jo);
	    	case "newInstance": return render_newInstance(jo);
	    	case "classMethod": return render_classMethod(jo);
	    	case "block": return render_block(jo);
	    }
	    }
	    return model.toString();
	}
}