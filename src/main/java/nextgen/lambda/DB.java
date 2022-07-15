package nextgen.lambda;

import com.github.benmanes.caffeine.cache.*;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.schema.*;
import org.neo4j.values.storable.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import java.util.function.*;
import java.util.logging.*;
import java.util.stream.*;

import static com.github.benmanes.caffeine.cache.Caffeine.*;
import static java.lang.Runtime.*;
import static java.nio.file.Files.*;
import static java.util.Collections.*;
import static java.util.Optional.*;
import static java.util.stream.Collectors.*;
import static org.neo4j.graphdb.Direction.*;
import static org.neo4j.values.storable.CoordinateReferenceSystem.*;
import static org.neo4j.values.storable.Values.*;

public class DB {

   final static Logger log = LOG.logger(DB.class);

   final static Map<String, IndexDefinition> indices = new LinkedHashMap<>();
   final static Cache<String, Long> cache = newBuilder()
      .maximumSize(10000)
      .recordStats()
      .expireAfterWrite(5, TimeUnit.MINUTES)
      .build();

   private static final Set<String> merges = new LinkedHashSet<>();

   public static Optional<GraphDatabaseService> start(String path) {

      try {
         return java.util.Optional.ofNullable(start(java.nio.file.Path.of(path)));
      } catch (java.io.IOException e) {
         EVENTS.exception(e);
         return Optional.empty();
      }
   }

   public static GraphDatabaseService start(java.nio.file.Path dbPath) throws java.io.IOException {
      if (!exists(dbPath)) createDirectories(dbPath);

      final org.neo4j.dbms.api.DatabaseManagementService managementService = new org.neo4j.dbms.api.DatabaseManagementServiceBuilder(dbPath).build();
      getRuntime().addShutdownHook(new Thread(managementService::shutdown));

      return managementService.database(org.neo4j.configuration.GraphDatabaseSettings.DEFAULT_DATABASE_NAME);
   }

   public static org.neo4j.graphdb.schema.IndexDefinition addIndex(org.neo4j.graphdb.Transaction tx, String name, String label, String property) {
      return tx.schema()
         .indexFor(Label.label(label))
         .on(property)
         .withName(name)
         .create();
   }

   public static void addIndex(GraphDatabaseService db, String name, String label, String property) {
      if (indices.containsKey(name)) return;

      tx(db, tx -> {
         log.info("add index " + name);
         indices.put(name, addIndex(tx, name, label, property));
      });
   }

   public static void tx(GraphDatabaseService db, java.util.function.Consumer<Transaction> consumer) {
      try (Transaction tx = db.beginTx()) {
         consumer.accept(tx);
         tx.commit();
      } catch (Throwable e) {
         log.severe(e.getMessage());
      }
   }

   public static java.util.Map<String, Object> params(Object... params) {
      if (params.length % 2 != 0) throw new IllegalArgumentException("query params must be in key-value pars : " + Arrays.toString(params));
      java.util.Map<String, Object> map = new java.util.LinkedHashMap<>();
      for (int i = 0; i < params.length; i += 2)
         map.put(params[i].toString(), params[i + 1]);
      return map;
   }

   public static void query(Transaction tx, String query, BiConsumer<Integer, Map<String, Object>> forEach) {
      query(tx, query, emptyMap(), forEach);
   }

   public static Stream<Node> queryStream(Transaction tx, String query, java.util.Map<String, Object> params) {
      final java.util.stream.Stream.Builder<Node> stream = java.util.stream.Stream.builder();
      org.neo4j.graphdb.Result result = tx.execute(query, params);
      while (result.hasNext()) stream.add((org.neo4j.graphdb.Node) result.next().get("n"));
      result.close();
      return stream.build();
   }

   public static Stream<Node> querySingle(Transaction tx, String query, java.util.Map<String, Object> params) {
      final java.util.stream.Stream.Builder<Node> stream = java.util.stream.Stream.builder();
      org.neo4j.graphdb.Result result = tx.execute(query, params);
      if (result.hasNext()) stream.add((org.neo4j.graphdb.Node) result.next().get("n"));
      result.close();
      return stream.build();
   }

   public static void query(Transaction tx, String query, java.util.Map<String, Object> params, BiConsumer<Integer, Map<String, Object>> forEach) {
      final AtomicInteger row = new AtomicInteger(-1);
      org.neo4j.graphdb.Result result = tx.execute(query, params);
      while (result.hasNext())
         forEach.accept(row.incrementAndGet(), result.next());
      result.close();
   }

   public static Map<String, Object> queryParams(Object... kv) {
      final Map<String, Object> map = new TreeMap<>();
      for (int i = 0; i < kv.length; i += 2)
         map.put(kv[i].toString(), kv[i + 1]);
      return map;
   }

   public static java.util.Optional<Node> find(Transaction tx, String label, String property, Object value) {

      final String key = String.join(".", label, property, value.toString());

      final Long nodeId = cache.get(key, k -> {
         final Node node = tx.findNode(Label.label(label), property, value);
         return node == null ? null : node.getId();
      });

      return ofNullable(nodeId == null ? null : tx.getNodeById(nodeId));
   }


   public static String uuidOf(Node node) {
      return getString(node, "uuid");
   }

   public static Node merge(Transaction tx, String label, String property, Object value, java.util.function.Function<Node, Node> f) {
      final String key = "merge " + label + " " + property;
      if (!merges.contains(key)) {
         merges.add(key);
         log.info(key);
      }

      return f.apply(find(tx, label, property, value).orElseGet(() -> create(tx, label, property, value)));
   }

   public static Node merge(Transaction tx, String label, String property, Object value) {
      final String key = "merge " + label + " " + property;
      if (!merges.contains(key)) {
         merges.add(key);
         log.info(key);
      }
      return find(tx, label, property, value).orElseGet(() -> create(tx, label, property, value));
   }

   public static org.neo4j.graphdb.Node mergeOutgoing(org.neo4j.graphdb.Transaction tx, org.neo4j.graphdb.Node src, String relation, String dstLabel, String property, Object value) {
      return mergeOutgoing(src, relation, dstLabel, n -> n.hasProperty(property) && n.getProperty(property).equals(value), () -> create(tx, dstLabel, property, value));
   }

   public static org.neo4j.graphdb.Node mergeOutgoing(org.neo4j.graphdb.Node src, String relation, String dstLabel, Predicate<Node> predicate, Supplier<Node> dst) {
      return outgoing(src, relation, dstLabel)
         .filter(predicate)
         .findFirst()
         .orElseGet(() -> {
            final org.neo4j.graphdb.Node node = dst.get();
            relate(src, node, relation);
            return node;
         });
   }

   public static Node create(Transaction tx, String label, String property, Object value) {
      Node node = create(tx, label);
      if (value != null) {
         node.setProperty(property, value);
         cache.put(String.join(".", label, property, value.toString()), node.getId());
      }
      return node;
   }

   public static org.neo4j.graphdb.Node create(org.neo4j.graphdb.Transaction tx, String label) {
      final org.neo4j.graphdb.Node node = tx.createNode(org.neo4j.graphdb.Label.label(label));
      node.setProperty("uuid", java.util.UUID.randomUUID().toString());
      node.setProperty("_created", System.currentTimeMillis());
      return node;
   }

   public static Relationship relate(Node src, Node dst, String type) {
      final org.neo4j.graphdb.RelationshipType relationshipType = org.neo4j.graphdb.RelationshipType.withName(type);
      final java.util.concurrent.atomic.AtomicLong ordinal = new java.util.concurrent.atomic.AtomicLong(0L);
      for (Relationship relationship : src.getRelationships(org.neo4j.graphdb.Direction.OUTGOING, relationshipType)) {
         if (relationship.getEndNode().equals(dst)) return relationship;
         ordinal.incrementAndGet();
      }
      final Relationship relationship = src.createRelationshipTo(dst, relationshipType);
      relationship.setProperty("uuid", java.util.UUID.randomUUID().toString());
      relationship.setProperty("_created", System.currentTimeMillis());
      relationship.setProperty("_ordinal", ordinal.get());
      return relationship;
   }

   public static Node set(Node node, Object... properties) {
      for (int i = 0; i < properties.length; i += 2) {
         final String name = properties[i].toString();
         final Object value = properties[i + 1];
         if (value != null) node.setProperty(name, value);
      }
      return node;
   }

   public static String label(org.neo4j.graphdb.Node node) {
      return java.util.stream.StreamSupport.stream(node.getLabels().spliterator(), false).map(Label::name).collect(joining());
   }

   public static Stream<Node> all(Transaction tx) {
      return java.util.stream.StreamSupport.stream(tx.getAllNodes().stream().spliterator(), false);
   }

   public static Stream<Node> all(Transaction tx, String label) {
      return java.util.stream.StreamSupport.stream(tx.findNodes(Label.label(label)).stream().spliterator(), false);
   }

   public static Stream<Node> findAll(Transaction tx, String label, String property, Object value) {
      return all(tx, label)
         .filter(node -> node.hasProperty(property) && node.getProperty(property).equals(value));
   }

   public static String debug(Node node) {
      final StringBuilder out = new StringBuilder();

      in(node).forEach(r -> out.append("\n").append(label(r.getStartNode())).append(" ").append(r.getType().name()));

      out.append("\n\t").append(label(node)).append(" ").append(node.getId());
      node.getAllProperties().forEach((key, value) -> out.append("\n\t\t").append(key).append(" = ").append(value));
      out(node).forEach(r -> out.append("\n\t\t\t").append(r.getType().name()).append(" ").append(label(r.getEndNode())).append(" ").append(r.getEndNode().getId()));
      return out.toString();
   }

   public static String getString(Node node, String property) {
      return node.hasProperty(property) ? (String) node.getProperty(property) : "";
   }

   public static String getString(Node node, String property, String defaultValue) {
      return node.hasProperty(property) ? (String) node.getProperty(property) : defaultValue;
   }

   public static Number getNumber(Node node, String property) {
      return node.hasProperty(property) ? (Number) node.getProperty(property) : 0L;
   }


   public static boolean getBoolean(org.neo4j.graphdb.Node node, String property, boolean defaultValue) {
      return node.hasProperty(property) ? (Boolean) node.getProperty(property) : defaultValue;
   }

   public static java.time.LocalDate getDate(Node node, String property) {
      return node.hasProperty(property) ? (java.time.LocalDate) node.getProperty(property) : java.time.LocalDate.now();
   }

   public static java.time.LocalDateTime getDateTime(Node node, String property) {
      return node.hasProperty(property) ? (java.time.LocalDateTime) node.getProperty(property) : java.time.LocalDateTime.of(0, 0, 0, 0, 0, 0);
   }

   public static java.time.Duration getDuration(Node node, String property) {
      return node.hasProperty(property) ? (java.time.Duration) node.getProperty(property) : java.time.Duration.ZERO;
   }

   public static org.neo4j.values.storable.PointValue getCoordinate(org.neo4j.graphdb.Node node, String property) {
      return node.hasProperty(property) ? (org.neo4j.values.storable.PointValue) node.getProperty(property) : pointValue(get(WGS84), 0.0f, 0.0f);
   }

   public static PointValue asCoordinate(double lat, double lng) {
      return Values.pointValue(CoordinateReferenceSystem.get(CoordinateReferenceSystem.WGS84), lat, lng);
   }

   public static byte[] getBytes(Node node, String property) {
      return node.hasProperty(property) ? (byte[]) node.getProperty(property) : new byte[0];
   }

   public static Stream<Relationship> in(Node dst) {
      return java.util.stream.StreamSupport.stream(dst.getRelationships(INCOMING).spliterator(), false);
   }

   public static Stream<Relationship> out(Node src) {
      return java.util.stream.StreamSupport.stream(src.getRelationships(OUTGOING).spliterator(), false);
   }

   public static Stream<Relationship> in(Node dst, String type) {
      return in(dst).filter(r -> r.getType().name().equals(type));
   }

   public static Stream<Relationship> out(Node src, String type) {
      return out(src).filter(r -> r.getType().name().equals(type));
   }

   public static Stream<Relationship> out(Node src, String type, String dstLabel) {
      return out(src)
         .filter(r -> r.getType().name().equals(type))
         .filter(r -> r.getEndNode().hasLabel(Label.label(dstLabel)));
   }

   public static Stream<Node> incoming(Node dst) {
      return in(dst).map(Relationship::getStartNode);
   }

   public static Stream<Node> outgoing(Node src) {
      return out(src).map(Relationship::getEndNode);
   }

   public static Stream<Node> incoming(Node dst, String type) {
      return in(dst).filter(r -> r.getType().name().equals(type)).map(Relationship::getStartNode);
   }

   public static Stream<Node> outgoing(Node src, String type) {
      return out(src).filter(r -> r.getType().name().equals(type)).map(Relationship::getEndNode);
   }

   public static Stream<Node> incoming(Node dst, String type, String srcLabel) {
      return incoming(dst, type).filter(n -> n.hasLabel(Label.label(srcLabel)));
   }

   public static Stream<Node> outgoing(Node src, String type, String dstLabel) {
      return outgoing(src, type).filter(n -> n.hasLabel(Label.label(dstLabel)));
   }

   public static Stream<Node> outgoing(Node src, String type, String dstLabel, String property, Object value) {
      return outgoing(src, type, dstLabel)
         .filter(node -> node.hasProperty(property) && node.getProperty(property).equals(value));
   }

   public static Optional<Node> firstOutgoing(Node src, String type, String dstLabel) {
      return outgoing(src, type, dstLabel).findFirst();
   }

   public static Optional<Node> firstIncoming(Node dst, String type, String srcLabel) {
      return incoming(dst, type, srcLabel).findFirst();
   }


   public static com.github.benmanes.caffeine.cache.Cache<String, Long> cache() {
      return cache;
   }

   public static void deleteTree(org.neo4j.graphdb.Relationship relationship) {
      log.info("deleting relation " + relationship.getId() + " " + relationship.getType());
      final org.neo4j.graphdb.Node endNode = relationship.getEndNode();
      relationship.delete();

      final java.util.Optional<org.neo4j.graphdb.Relationship> incoming = in(endNode).findAny();
      if (incoming.isPresent()) return;

      out(endNode).forEach(nextgen.lambda.DB::deleteTree);

      log.info("deleting node " + endNode.getId() + " " + label(endNode));
      endNode.delete();
   }

   public static void deleteTree(org.neo4j.graphdb.Transaction tx, String label, String uuid) {
      Optional.ofNullable(tx.findNode(Label.label(label), "uuid", uuid)).ifPresent(node -> {
         final java.util.Optional<org.neo4j.graphdb.Relationship> incoming = in(node).findAny();
         if (incoming.isPresent()) return;
         out(node).forEach(nextgen.lambda.DB::deleteTree);
         node.delete();
      });
   }

   public static Set<String> labels(org.neo4j.graphdb.GraphDatabaseService db) {
      final Set<String> set = new java.util.TreeSet<>();
      tx(db, tx -> set.addAll(java.util.stream.StreamSupport.stream(tx.getAllLabelsInUse().spliterator(), false).map(Label::name).collect(toSet())));
      return set;
   }

   public static org.neo4j.graphdb.Node create(org.neo4j.graphdb.Transaction tx, java.util.Set<String> labels) {
      if (labels.isEmpty()) return create(tx, "UNLABELLED");
      final java.util.Iterator<String> iterator = labels.iterator();
      final org.neo4j.graphdb.Node node = create(tx, iterator.next());
      while (iterator.hasNext()) node.addLabel(Label.label(iterator.next()));
      return node;
   }
}