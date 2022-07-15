package nextgen.lambda.modules.templates.java;

public class enumDeclarationBuilder extends io.vertx.core.json.JsonObject {

   public enumDeclarationBuilder() {
      put("_template", "enumDeclaration");
      put("annotations", new io.vertx.core.json.JsonArray());
      put("values", new io.vertx.core.json.JsonArray());
      put("fields", new io.vertx.core.json.JsonArray());
      put("methods", new io.vertx.core.json.JsonArray());
   }

   public enumDeclarationBuilder(io.vertx.core.json.JsonObject model) {
      super(model.toBuffer());
   }

   public enumDeclarationBuilder add_annotations(Object value) {
      getJsonArray("annotations").add(value);
      return this;
   }

   public enumDeclarationBuilder add_fields(classFieldBuilder value) {
      getJsonArray("fields").add(value);
      return this;
   }

   public enumDeclarationBuilder add_methods(classMethodBuilder value) {
      getJsonArray("methods").add(value);
      return this;
   }

   public enumDeclarationBuilder set_scope(Object value) {
      put("scope", value);
      return this;
   }

   public enumDeclarationBuilder set_name(Object value) {
      put("name", value);
      return this;
   }

   public enumDeclarationBuilder add_values(Object value) {
      getJsonArray("values").add(value);
      return this;
   }
}