package nextgen.lambda.modules.templates;

public class Template {

	String uuid;
	String name;
	String content;
	java.util.Set<TemplateParameter> parameters = new java.util.TreeSet<>(java.util.Comparator.comparing(TemplateParameter::name));

	public Template() {
	    this.uuid = java.util.UUID.randomUUID().toString();
	}

	public Template(org.neo4j.graphdb.Transaction tx, String uuid) {
	    this(nextgen.lambda.DB.merge(tx, "Template", "uuid", uuid));
	}

	public Template(io.vertx.core.json.JsonObject delegate) {
	    this.uuid = delegate.getString("uuid", java.util.UUID.randomUUID().toString());
	    this.name = delegate.getString("name", "?");
	    this.content = delegate.getString("content", "?");
	    this.parameters.addAll(delegate.getJsonArray("parameters", new io.vertx.core.json.JsonArray()).stream().map(o -> (io.vertx.core.json.JsonObject) o).map(TemplateParameter::new).collect(java.util.stream.Collectors.toList()));
	}

	public Template(org.neo4j.graphdb.Node delegate) {
	    this.uuid = (String) delegate.getProperty("uuid", java.util.UUID.randomUUID().toString());
	    this.name = (String) delegate.getProperty("name", "?");
	    this.content = (String) delegate.getProperty("content", "?");
	    this.parameters.addAll(nextgen.lambda.DB.outgoing(delegate, "parameters", "TemplateParameter").map(TemplateParameter::new).collect(java.util.stream.Collectors.toList()));
	}

	public String uuid() {
	    return uuid;
	}

	@Override
	public boolean equals(Object o) {
	    if (this == o) return true;
	    if (o == null || getClass() != o.getClass()) return false;
	    Template that = (Template) o;
	    return uuid.equals(that.uuid);
	}

	@Override
	public int hashCode() {
	    return uuid.hashCode();
	}

	public String name() {
	    return name;
	}

	public String content() {
	    return content;
	}

	public Template setName(String name) {
	    this.name = name;
	    return this;
	}

	public Template setContent(String content) {
	    this.content = content;
	    return this;
	}

	public java.util.stream.Stream<TemplateParameter> parameters() {
	    return parameters.stream();
	}

	public Template addParameter(TemplateParameter template) {
	    parameters.add(template);
	    return this;
	}

	public Template removeParameter(String uuid) {
	    parameters.stream().filter(template -> template.uuid().equals(uuid)).findFirst().ifPresent(template -> parameters.remove(template));
	    return this;
	}

	public Template clearParameters() {
	    parameters.clear();
	    return this;
	}

	@Override
	public String toString() {
	    return uuid + " " + name + " " + parameters.size();
	}

	public org.neo4j.graphdb.Node save(org.neo4j.graphdb.Transaction tx) {
	    final org.neo4j.graphdb.Node node = nextgen.lambda.DB.merge(tx, "Template", "uuid", uuid);
	    nextgen.lambda.DB.set(node, "name", name);
	    nextgen.lambda.DB.set(node, "content", content);
	    nextgen.lambda.DB.out(node,"parameters","TemplateParameter").forEach(nextgen.lambda.DB::deleteTree);
	    parameters.stream().map(other -> other.save(tx)).forEach(other -> nextgen.lambda.DB.relate(node, other, "parameters"));
	    return node;
	}
}