package nextgen.lambda.domain.maven;

public class Project {

	public String name;
	public String root;

	public Project(String name, String root) {
	    this.name = name;
	    this.root = root;
	}

	public String name() {
	    return this.name;
	}

	public String root() {
	    return this.root;
	}

	public String srcMain() {
	    return root + java.io.File.separator + "src/main";
	}

	public String srcMainJava() {
	    return srcMain() + java.io.File.separator + "java";
	}

	public String srcMainResources() {
	    return srcMain() + java.io.File.separator + "resources";
	}
}