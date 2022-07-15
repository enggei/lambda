package nextgen.lambda.modules.templates;

public class TemplateGroup {

	String uuid;
	String name;
	java.util.Set<Template> templates = new java.util.TreeSet<>(java.util.Comparator.comparing(Template::name));

	public TemplateGroup() {
	    this.uuid = java.util.UUID.randomUUID().toString();
	}

	public TemplateGroup(org.neo4j.graphdb.Transaction tx, String uuid) {
	    this(nextgen.lambda.DB.merge(tx, "TemplateGroup", "uuid", uuid));
	}

	public TemplateGroup(io.vertx.core.json.JsonObject delegate) {
	    this.uuid = delegate.getString("uuid", java.util.UUID.randomUUID().toString());
	    this.name = delegate.getString("name", "?");
	    this.templates.addAll(delegate.getJsonArray("templates", new io.vertx.core.json.JsonArray()).stream().map(o -> (io.vertx.core.json.JsonObject) o).map(Template::new).collect(java.util.stream.Collectors.toList()));
	}

	public TemplateGroup(org.neo4j.graphdb.Node delegate) {
	    this.uuid = (String) delegate.getProperty("uuid", java.util.UUID.randomUUID().toString());
	    this.name = (String) delegate.getProperty("name", "?");
	    this.templates.addAll(nextgen.lambda.DB.outgoing(delegate, "templates", "Template").map(Template::new).collect(java.util.stream.Collectors.toList()));
	}

	public String uuid() {
	    return uuid;
	}

	@Override
	public boolean equals(Object o) {
	    if (this == o) return true;
	    if (o == null || getClass() != o.getClass()) return false;
	    TemplateGroup that = (TemplateGroup) o;
	    return uuid.equals(that.uuid);
	}

	@Override
	public int hashCode() {
	    return uuid.hashCode();
	}

	public String name() {
	    return name;
	}

	public TemplateGroup setName(String name) {
	    this.name = name;
	    return this;
	}

	public java.util.stream.Stream<Template> templates() {
	    return templates.stream().filter(template -> !(template.name().equals("eot") || template.name().equals("gt") || template.name().equals("eom")));
	}

	public TemplateGroup addTemplate(Template template) {
	    templates.add(template);
	    return this;
	}

	public TemplateGroup removeTemplate(String uuid) {
	    templates.stream().filter(template -> template.uuid().equals(uuid)).findFirst().ifPresent(template -> templates.remove(template));
	    return this;
	}

	@Override
	public String toString() {
	    return uuid + " " + name + " " + templates.size();
	}

	public org.neo4j.graphdb.Node save(org.neo4j.graphdb.Transaction tx) {
	    final org.neo4j.graphdb.Node node = nextgen.lambda.DB.merge(tx, "TemplateGroup", "uuid", uuid);
	    nextgen.lambda.DB.set(node, "name", name);
	    nextgen.lambda.DB.out(node, "templates", "Template").forEach(nextgen.lambda.DB::deleteTree);
	    templates.stream().map(other -> other.save(tx)).forEach(other -> nextgen.lambda.DB.relate(node, other, "templates"));
	    return node;
	}
}