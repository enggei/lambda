package nextgen.lambda.domain.templates;

public class TemplateGroup {

	public io.vertx.core.json.JsonObject delegate;
	public org.stringtemplate.v4.STGroup stGroup;

	public TemplateGroup(io.vertx.core.json.JsonObject delegate) {
	    this.delegate = delegate;
	    this.stGroup = new org.stringtemplate.v4.STGroupString(name(), toSTG(), '~', '~');
	}

	public String name() {
	    return delegate.getString("name");
	}

	public java.util.stream.Stream<Template> templates() {
	    return delegate.getJsonArray("templates", new io.vertx.core.json.JsonArray())
	             .stream()
	             .map(element -> (io.vertx.core.json.JsonObject) element)
	             .map(Template::new);
	}

	public org.stringtemplate.v4.STGroup stGroup() {
	    return stGroup;
	}

	public String toSTG() {
	    final String stg = "delimiters \"~\", \"~\"" +
	       "\n\ngt() ::= \">\"" +
	       "\n\neot() ::= <<~gt()~~gt()~>>" +
	       "\n\nSTGroupTemplate(DELIMITER,TEMPLATES) ::= <<delimiters \"~DELIMITER~\",\"~DELIMITER~\"" +
	       "\n\n~TEMPLATES:{it|~it~};separator=\"\\n\\n\"~" +
	       "\n\neom() ::= \"}\"" +
	       "\n\ngt() ::= \">\"" +
	       "\n\n>>" +
	       "\n\nSTTemplate(CONTENT,NAME,PARAMS) ::= <<~NAME~(~PARAMS:{it|~it~};separator=\",\"~) ::= <<~CONTENT~ ~eot()~>>";

	    final org.stringtemplate.v4.STGroup templateGroup = new org.stringtemplate.v4.STGroupString(name(), stg, '~', '~');
	    final org.stringtemplate.v4.ST stGroupTemplate = templateGroup.getInstanceOf("STGroupTemplate");
	    stGroupTemplate.add("DELIMITER", "~");

	    templates()
	       .filter(template -> !template.name().equals("eom"))
	       .filter(template -> !template.name().equals("gt"))
	       .forEach(template -> {
	          final org.stringtemplate.v4.ST stTemplate = templateGroup.getInstanceOf("STTemplate");
	          stTemplate.add("NAME", template.name());
	          stTemplate.add("CONTENT", template.content());
	          template.parameters().forEach(parameter -> stTemplate.add("PARAMS", parameter.name()));
	          stGroupTemplate.add("TEMPLATES", stTemplate);
	       });

	    final org.stringtemplate.v4.STGroup stGroup = new org.stringtemplate.v4.STGroupString(name(), stGroupTemplate.render(), '~', '~');
	    stGroup.registerRenderer(Object.class, (o, formatString, locale) -> {

	       final String text = o.toString();
	       if (formatString == null) return text;

	       final int length = text.length();
	       final int lastIndex = text.lastIndexOf(".");

	       switch (formatString) {
	          case "sn":
	          case "simpleName":
	             if (lastIndex == -1) return text;
	             return text.substring(lastIndex + 1);
	          case "cap":
	          case "capitalize":
	             return Character.toUpperCase(text.charAt(0)) + (length > 1 ? text.substring(1) : "");
	          case "tu":
	          case "toUpper":
	             return text.toUpperCase();
	          case "lf":
	          case "lowFirst":
	             return Character.toLowerCase(text.charAt(0)) + (length > 1 ? text.substring(1) : "");
	          case "tl":
	          case "toLower":
	             return text.toLowerCase();
	          default:
	             return o.toString();
	       }
	    });

	    return stGroupTemplate.render();
	}
}