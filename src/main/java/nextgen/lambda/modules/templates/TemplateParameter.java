package nextgen.lambda.modules.templates;

public class TemplateParameter {

	String uuid;
	String name;
	TemplateParameterCardinality qty;
	String type;

	public TemplateParameter() {
	    this.uuid = java.util.UUID.randomUUID().toString();
	}

	public TemplateParameter(org.neo4j.graphdb.Transaction tx, String uuid) {
	    this(nextgen.lambda.DB.merge(tx, "TemplateParameter", "uuid", uuid));
	}

	public TemplateParameter(io.vertx.core.json.JsonObject delegate) {
	    this.uuid = delegate.getString("uuid", java.util.UUID.randomUUID().toString());
	    this.name = delegate.getString("name", "?");
	    this.qty = TemplateParameterCardinality.valueOf(delegate.getString("qty", "SINGLE"));
	    this.type = delegate.getString("type", "?");
	}

	public TemplateParameter(org.neo4j.graphdb.Node delegate) {
	    this.uuid = (String) delegate.getProperty("uuid", java.util.UUID.randomUUID().toString());
	    this.name = (String) delegate.getProperty("name", "?");
	    this.qty = TemplateParameterCardinality.valueOf((String) delegate.getProperty("qty", "SINGLE"));
	    this.type = (String) delegate.getProperty("type", "?");
	}

	public String uuid() {
	    return uuid;
	}

	@Override
	public boolean equals(Object o) {
	    if (this == o) return true;
	    if (o == null || getClass() != o.getClass()) return false;
	    TemplateParameter that = (TemplateParameter) o;
	    return uuid.equals(that.uuid);
	}

	@Override
	public int hashCode() {
	    return uuid.hashCode();
	}

	public String name() {
	    return name;
	}

	public TemplateParameterCardinality qty() {
	    return qty;
	}

	public String type() {
	    return type;
	}

	public TemplateParameter setName(String name) {
	    this.name = name;
	    return this;
	}

	public TemplateParameter setQty(TemplateParameterCardinality qty) {
	    this.qty = qty;
	    return this;
	}

	public TemplateParameter setType(String type) {
	    this.type = type;
	    return this;
	}

	@Override
	public String toString() {
	    return uuid + " " + name + " " + qty + " " + type;
	}

	public org.neo4j.graphdb.Node save(org.neo4j.graphdb.Transaction tx) {
	    final org.neo4j.graphdb.Node node = nextgen.lambda.DB.merge(tx, "TemplateParameter", "uuid", uuid());
	    nextgen.lambda.DB.set(node, "name", name);
	    nextgen.lambda.DB.set(node, "qty", qty.name());
	    nextgen.lambda.DB.set(node, "type", type);
	    return node;
	}
}