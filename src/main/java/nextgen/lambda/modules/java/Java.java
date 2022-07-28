package nextgen.lambda.modules.java;

import static nextgen.lambda.IO.*;

public class Java extends nextgen.lambda.modules.AbstractLambdaModule {


   private static final org.stringtemplate.v4.STGroup JAVAGROUP = nextgen.lambda.modules.templates.Templates.toSTGroup(java.nio.file.Path.of(mainResources.toString(), "Java.stg"));

   @Override
   public nextgen.lambda.modules.LambdaModule register(nextgen.lambda.ui.UI ui) {
      super.register(ui);

      final nextgen.lambda.modules.LambdaModuleTreeNode treeNode = addToNavigator();

      addClassTreeNode(treeNode, nextgen.lambda.modules.templates.java.JavaGroup.class);
      addClassTreeNode(treeNode, nextgen.lambda.modules.java.JavaParser.class);
      addClassTreeNode(treeNode, nextgen.lambda.modules.java.JavaCompiler.class);
      addClassTreeNode(treeNode, nextgen.lambda.modules.java.JavaTypes.class);
      addClassTreeNode(treeNode, nextgen.lambda.modules.java.JavaMainSrc.class);

      addPackageTreeNodeTreeNode(treeNode, "lang", nextgen.lambda.modules.java.JavaTypes.get().lang().classes());
      addPackageTreeNodeTreeNode(treeNode, "stream", nextgen.lambda.modules.java.JavaTypes.get().stream().classes());
      addPackageTreeNodeTreeNode(treeNode, "util", nextgen.lambda.modules.java.JavaTypes.get().util().classes());
      addPackageTreeNodeTreeNode(treeNode, "functional", nextgen.lambda.modules.java.JavaTypes.get().functional().classes());

      addClassTreeNode(treeNode, nextgen.lambda.modules.templates.java.classDeclarationBuilder.class);
      addClassTreeNode(treeNode, nextgen.lambda.modules.templates.java.interfaceDeclarationBuilder.class);
      addClassTreeNode(treeNode, nextgen.lambda.modules.templates.java.enumDeclarationBuilder.class);
      addClassTreeNode(treeNode, nextgen.lambda.modules.templates.java.blockBuilder.class);

      return this;
   }

   public static nextgen.lambda.modules.templates.java.JavaGroup getJavaGroup() {
      return new nextgen.lambda.modules.templates.java.JavaGroup(JAVAGROUP);
   }

   public void newPojo() {
      new nextgen.lambda.modules.java.nodes.PojoForm(ui.canvas).edit(nextgen.lambda.EVENTS::open);
   }

   public void newBuilder() {
      new nextgen.lambda.modules.java.nodes.BuilderForm(ui.canvas).edit(nextgen.lambda.EVENTS::open);
   }
}