package nextgen.lambda;

public class Lambda {

   public static void main(String[] args) throws java.io.IOException {

      final java.nio.file.Path dbPath = java.nio.file.Path.of(args.length < 1 ? "db" : args[1]);

      final org.neo4j.graphdb.GraphDatabaseService db = nextgen.lambda.DB.start(dbPath);
      final nextgen.lambda.CONTEXT context = new nextgen.lambda.CONTEXT(db);

      nextgen.lambda.ui.UI.start(context).showUI(() -> System.exit(0));
   }

//	public static java.util.function.Consumer<org.stringtemplate.v4.ST> write(String mainSrc, org.stringtemplate.v4.STGroup stGroup, io.vertx.core.json.JsonObject packagesElement, io.vertx.core.json.JsonObject model) {
//		return st -> {
//			try {
//				final String packageName = packagesElement.getString("name");
//				final String packagePath = String.join(java.io.File.separator, mainSrc, packagesElement.getString("name").replaceAll("\\.", java.io.File.separator));
//				final String className = model.getString("name");
//				final String javaFile = String.join(java.io.File.separator, packagePath, String.join(".", className, "java"));
//
//				final org.stringtemplate.v4.ST compilationUnit = stGroup.getInstanceOf("compilationUnit");
//				compilationUnit.add("packageName", packageName);
//				compilationUnit.add("members", st);
//
//				java.nio.file.Files.createDirectories(java.nio.file.Path.of(packagePath));
//				java.nio.file.Files.writeString(java.nio.file.Path.of(javaFile), compilationUnit.render());
//				System.out.println("w " + javaFile);
//			} catch (java.io.IOException e) {
//				throw new RuntimeException(e);
//			}
//		};
//	}

//	public static java.util.Optional<org.stringtemplate.v4.ST> mapClassDeclaration(org.stringtemplate.v4.STGroup stGroup, io.vertx.core.json.JsonObject model, io.vertx.core.json.JsonObject map) {
//	    final org.stringtemplate.v4.ST template = stGroup.getInstanceOf(map.getString("templateName"));
//	    streamJsonObjects("ONE", map).forEach(one -> {
//	        try {
//	            final io.vertx.core.json.JsonObject mapper = one.getJsonObject("mapper");
//	            if (mapper != null)
//	                mapClassDeclaration(stGroup, one, mapper).ifPresent(mapped -> template.add(one.getString("template"), mapped));
//	            else {
//	                final Object value = model.getValue(one.getString("model"));
//	                if (value != null && value.toString().trim().length() > 0)
//	                    template.add(one.getString("template"), value);
//	            }
//	        } catch (Throwable throwable) {
//	            System.err.println("ERROR with " + template.getName());
//	            System.err.println("\t" + throwable.getMessage());
//	        }
//	    });
//	    streamJsonObjects("MANY", map).forEach(many -> {
//	        try {
//	            final io.vertx.core.json.JsonObject mapper = many.getJsonObject("mapper");
//	            if (mapper != null)
//	                streamJsonObjects(many.getString("model"), model).forEach(manyElement -> mapClassDeclaration(stGroup, manyElement, mapper).ifPresent(mapped -> template.add(many.getString("template"), mapped)));
//	            else
//	                streamObjects(many.getString("model"), model).forEach(manyElement -> template.add(many.getString("template"), manyElement));
//	        } catch (Throwable throwable) {
//	            System.err.println("ERROR with " + template.getName());
//	            System.err.println("\t" + throwable.getMessage());
//	        }
//	    });
//	    return java.util.Optional.of(template);
//	}

//	public static java.util.stream.Stream<java.nio.file.Path> files(String path, String postfix) throws java.io.IOException {
//	    return java.nio.file.Files.walk(java.nio.file.Paths.get(path)).filter(java.nio.file.Files::isRegularFile).filter(file -> file.toString().endsWith(postfix));
//	}
//
//	private static void write(io.vertx.core.file.FileSystem fileSystem, org.stringtemplate.v4.ST compilationUnit, String file) {
//	    fileSystem.writeFile(file, io.vertx.core.buffer.Buffer.buffer(compilationUnit.render()), ar2 -> {
//	        if (ar2.failed())
//	            throw new RuntimeException(ar2.cause());
//	        System.out.println(file);
//	    });
//	}
//
//	static io.vertx.core.json.JsonObject decorate(io.vertx.core.json.JsonObject model) {
//	    if (model.getString("scope") == null)
//	        model.put("scope", "public");
//	    streamJsonObjects("fields", model).forEach(field -> {
//	        if (field.getString("scope") == null)
//	            field.put("scope", "public");
//	    });
//	    streamJsonObjects("methods", model).forEach(method -> {
//	        if (method.getString("scope") == null)
//	            method.put("scope", "public");
//	    });
//	    streamJsonObjects("constructors", model).forEach(method -> {
//	        if (method.getString("scope") == null)
//	            method.put("scope", "public");
//	        if (method.getString("name") == null)
//	            method.put("name", model.getString("name"));
//	    });
//	    streamJsonObjects("nested", model).forEach(nextgen.lambda.Lambda::decorate);
//	    return model;
//	}
//
//	private static void parse(String path, io.vertx.core.file.FileSystem fileSystem, io.vertx.core.json.JsonObject lambda) {
//	    final io.vertx.core.json.JsonArray packages = lambda.getJsonArray("packages");
//	    final io.vertx.core.json.JsonObject aPackage = new io.vertx.core.json.JsonObject();
//	    aPackage.put("name", path.substring(path.indexOf("java/") + 5).replaceAll(java.io.File.separator, "."));
//	    aPackage.put("entities", new io.vertx.core.json.JsonArray());
//	    aPackage.put("interfaces", new io.vertx.core.json.JsonArray());
//	    aPackage.put("enums", new io.vertx.core.json.JsonArray());
//	}
//
//	public static io.vertx.core.json.JsonObject mapEnumDeclaration(com.github.javaparser.ast.body.EnumDeclaration declaration) {
//	    final io.vertx.core.json.JsonObject model = new io.vertx.core.json.JsonObject();
//	    model.put("name", declaration.getName().asString());
//	    model.put("scope", declaration.getModifiers().stream().map(modifier -> modifier.getKeyword().asString()).collect(java.util.stream.Collectors.joining(" ")));
//	    model.put("values", new io.vertx.core.json.JsonArray());
//	    declaration.getEntries().forEach(element -> model.getJsonArray("values").add(element.getNameAsString()));
//	    return model;
//	}
//
//	public static io.vertx.core.json.JsonObject mapClassDeclaration(com.github.javaparser.ast.body.ClassOrInterfaceDeclaration declaration) {
//	    final io.vertx.core.json.JsonObject model = new io.vertx.core.json.JsonObject();
//	    model.put("name", declaration.getName().asString());
//	    model.put("scope", declaration.getModifiers().stream().map(element -> element.getKeyword().asString()).collect(java.util.stream.Collectors.joining(" ")));
//	    model.put("extends", declaration.getExtendedTypes().stream().map(com.github.javaparser.ast.type.ClassOrInterfaceType::asString).collect(java.util.stream.Collectors.joining(", ")));
//	    model.put("fields", new io.vertx.core.json.JsonArray());
//	    model.put("methods", new io.vertx.core.json.JsonArray());
//	    model.put("constructors", new io.vertx.core.json.JsonArray());
//	    model.put("nested", new io.vertx.core.json.JsonArray());
//	    model.put("annotations", new io.vertx.core.json.JsonArray());
//	    model.put("typeParameters", new io.vertx.core.json.JsonArray());
//	    declaration.getTypeParameters().forEach(element -> model.getJsonArray("typeParameters").add(element.getName().asString()));
//	    declaration.getAnnotations().stream().map(nextgen.lambda.Lambda::mapAnnotation).forEach(element -> model.getJsonArray("annotations").add(element));
//	    declaration.getFields().forEach(fieldDeclaration -> fieldDeclaration.getVariables().forEach(variableDeclaration -> model.getJsonArray("fields").add(mapFieldDeclaration(variableDeclaration))));
//	    declaration.getMethods().forEach(methodDeclaration -> model.getJsonArray("methods").add(mapMethodDeclaration(methodDeclaration)));
//	    declaration.getConstructors().forEach(constructorDeclaration -> model.getJsonArray("constructors").add(mapConstructorDeclaration(constructorDeclaration)));
//	    // typeDeclaration.getMembers().stream().filter(BodyDeclaration::isClassOrInterfaceDeclaration).map(BodyDeclaration::asClassOrInterfaceDeclaration).forEach(classOrInterfaceDeclaration -> entity.getJsonArray("nested").add(mapEntity(classOrInterfaceDeclaration)));
//	    return model;
//	}
//
//	private static io.vertx.core.json.JsonObject mapAnnotation(com.github.javaparser.ast.expr.AnnotationExpr element) {
//	    return new io.vertx.core.json.JsonObject().put("name", element.getName().asString());
//	}
//
//	private static io.vertx.core.json.JsonObject mapConstructorDeclaration(com.github.javaparser.ast.body.ConstructorDeclaration declaration) {
//	    final io.vertx.core.json.JsonObject model = new io.vertx.core.json.JsonObject();
//	    model.put("name", declaration.getNameAsString());
//	    model.put("scope", declaration.getModifiers().stream().map(modifier -> modifier.getKeyword().asString()).collect(java.util.stream.Collectors.joining(" ")));
//	    model.put("parameters", new io.vertx.core.json.JsonArray());
//	    model.put("statements", new io.vertx.core.json.JsonArray());
//	    model.put("annotations", new io.vertx.core.json.JsonArray());
//	    model.put("throws", new io.vertx.core.json.JsonArray());
//	    declaration.getThrownExceptions().stream().map(com.github.javaparser.ast.Node::toString).forEach(thrown -> model.getJsonArray("throws").add(thrown));
//	    declaration.getAnnotations().stream().map(nextgen.lambda.Lambda::mapAnnotation).forEach(element -> model.getJsonArray("annotations").add(element));
//	    declaration.getParameters().forEach(parameterDeclaration -> model.getJsonArray("parameters").add(mapParameter(parameterDeclaration)));
//	    declaration.getBody().getStatements().forEach(statement -> model.getJsonArray("statements").add(statement.toString()));
//	    return model;
//	}
//
//	private static io.vertx.core.json.JsonObject mapMethodDeclaration(com.github.javaparser.ast.body.MethodDeclaration declaration) {
//	    final io.vertx.core.json.JsonObject model = new io.vertx.core.json.JsonObject();
//	    model.put("type", declaration.getTypeAsString());
//	    model.put("name", declaration.getNameAsString());
//	    model.put("scope", declaration.getModifiers().stream().map(modifier -> modifier.getKeyword().asString()).collect(java.util.stream.Collectors.joining(" ")));
//	    model.put("parameters", new io.vertx.core.json.JsonArray());
//	    model.put("statements", new io.vertx.core.json.JsonArray());
//	    model.put("annotations", new io.vertx.core.json.JsonArray());
//	    model.put("throws", new io.vertx.core.json.JsonArray());
//	    declaration.getThrownExceptions().stream().map(com.github.javaparser.ast.Node::toString).forEach(thrown -> model.getJsonArray("throws").add(thrown));
//	    declaration.getAnnotations().stream().map(nextgen.lambda.Lambda::mapAnnotation).forEach(element -> model.getJsonArray("annotations").add(element));
//	    declaration.getParameters().forEach(parameterDeclaration -> {
//	        final io.vertx.core.json.JsonObject parameter = mapParameter(parameterDeclaration);
//	        model.getJsonArray("parameters").add(parameter);
//	    });
//	    declaration.getBody().ifPresent(blockStmt -> blockStmt.getStatements().forEach(statement -> model.getJsonArray("statements").add(statement.toString())));
//	    return model;
//	}
//
//	private static io.vertx.core.json.JsonObject mapParameter(com.github.javaparser.ast.body.Parameter declaration) {
//	    final io.vertx.core.json.JsonObject parameter = new io.vertx.core.json.JsonObject();
//	    parameter.put("name", declaration.getNameAsString());
//	    parameter.put("type", declaration.getTypeAsString());
//	    return parameter;
//	}
//
//	public static io.vertx.core.json.JsonObject mapFieldDeclaration(com.github.javaparser.ast.body.VariableDeclarator declaration) {
//	    final io.vertx.core.json.JsonObject model = new io.vertx.core.json.JsonObject();
//	    model.put("name", declaration.getNameAsString());
//	    model.put("type", declaration.getTypeAsString());
//	    return model;
//	}


}