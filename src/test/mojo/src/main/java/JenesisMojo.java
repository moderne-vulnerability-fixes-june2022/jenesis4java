import java.io.File;
import java.io.IOException;

import net.sourceforge.jenesis4java.Access;
import net.sourceforge.jenesis4java.ClassMethod;
import net.sourceforge.jenesis4java.Comment;
import net.sourceforge.jenesis4java.CompilationUnit;
import net.sourceforge.jenesis4java.Invoke;
import net.sourceforge.jenesis4java.PackageClass;
import net.sourceforge.jenesis4java.Type;
import net.sourceforge.jenesis4java.VirtualMachine;
import net.sourceforge.jenesis4java.jaloppy.JenesisJalopyEncoder;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

/**
 * @goal jenesis4java
 * @phase generate-sources
 * @description generate the java source code
 */
public class JenesisMojo extends AbstractMojo {

    /**
     * @parameter expression="${project}"
     * @required
     */
    protected MavenProject project;

    /**
     * @parameter expression=
     *            "${project.build.directory}/generated-sources/jenesis4java"
     * @required
     */
    protected File outputJavaDirectory;

    /**
     * @parameter
     */
    protected String title;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        if (this.project != null) {
            this.project.addCompileSourceRoot(this.outputJavaDirectory.getAbsolutePath());
        }
        if (!this.outputJavaDirectory.mkdirs()) {
            getLog().error("Could not create source directory!");
        } else {
            try {
                generateJavaCode();
            } catch (IOException e) {
                throw new MojoExecutionException("Could not generate Java source code!", e);
            }
        }
    }

    private void generateJavaCode() throws IOException {
        System.setProperty("jenesis.encoder", JenesisJalopyEncoder.class.getName());

        // Get the VirtualMachine implementation.
        VirtualMachine vm = VirtualMachine.getVirtualMachine();

        // Instantiate a new CompilationUnit. The argument to the
        // compilation unit is the "codebase" or directory where the
        // compilation unit should be written.
        //
        // Make a new compilation unit rooted to the given sourcepath.
        CompilationUnit unit = vm.newCompilationUnit(this.outputJavaDirectory.getAbsolutePath());

        // Set the package namespace.
        unit.setNamespace("net.sourceforge.jenesis4java.example");

        // Add an import statement for fun.
        unit.addImport("java.io.Serializable");

        // Comment the package with a javadoc (DocumentationComment).
        unit.setComment(Comment.D, "Auto-Generated using the Jenesis Syntax API");

        // Make a new class.
        PackageClass cls = unit.newClass("HelloWorld");
        // Make it a public class.
        cls.setAccess(Access.PUBLIC);
        // Extend Object just for fun.
        cls.setExtends("Object");
        // Implement serializable just for fun.
        cls.addImplements("Serializable");
        // Comment the class with a javadoc (DocumentationComment).
        unit.setComment(Comment.D, "The HelloWorld example class.");

        // Make a new Method in the Class having type VOID and name "main".
        ClassMethod method = cls.newMethod(vm.newType(Type.VOID), "main");
        // Make it a public method.
        method.setAccess(Access.PUBLIC);
        // Make it a static method
        method.isStatic(true);
        // Add the "String[] argv" formal parameter.
        method.addParameter(vm.newArray("String", 1), "argv");

        // Create a new Method Invocation expression.
        Invoke println = vm.newInvoke("System.out", "println");

        if (this.title != null) {
            // Add the Moja parameter string literal as the sole argument.
            println.addArg(vm.newString(this.title));
        } else {
            // Add the Hello World string literal as the sole argument.
            println.addArg(vm.newString("Hello World!"));
        }
        // Add this expression to the method in a statement.
        method.newStmt(println);

        // Write the java file.
        unit.encode();
    }
}
