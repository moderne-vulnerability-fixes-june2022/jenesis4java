package net.sourceforge.jenesis4java.example;

import net.sourceforge.jenesis4java.Access;
import net.sourceforge.jenesis4java.ClassField;
import net.sourceforge.jenesis4java.ClassMethod;
import net.sourceforge.jenesis4java.CompilationUnit;
import net.sourceforge.jenesis4java.PackageClass;
import net.sourceforge.jenesis4java.Type;
import net.sourceforge.jenesis4java.Variable;
import net.sourceforge.jenesis4java.VirtualMachine;

/**
 * The <code>SimpleGenerator</code> class creates <code>getXXX()</code>,
 * <code>setXXX()</code>, and member fields using the <code>addMember()</code>
 * method.
 */
public class SimpleGenerator {

    private final VirtualMachine vm;

    private CompilationUnit unit;

    private PackageClass cls;

    /**
     * Create a new <code>SimpleGenerator</code>.
     */
    public SimpleGenerator() {
        this.vm = VirtualMachine.getVirtualMachine();
    }

    public static void main(String[] argv) throws Exception {
        VirtualMachine vm = VirtualMachine.getVirtualMachine();
        SimpleGenerator gen = new SimpleGenerator();
        gen.initialize("/tmp", "com.mycom.myproject", "Test");
        gen.addMember(vm.newType("String"), "name");
        gen.addMember(vm.newArray(Type.INT, 1), "array1");
        gen.addMember(vm.newArray(Type.INT, 2), "array2");
        gen.generate();
    }

    /**
     * Intermediate steps: Add a member to the class such that a member field,
     * getter method, and setter method are defined for the member having the
     * given name and type.
     */
    public void addMember(Type type, String name) {
        // This "Variable" is defined for later use in statements.
        Variable thisvar = this.vm.newVar("this." + name);
        Variable var = this.vm.newVar(name);

        // PART 1: Make the member field.
        ClassField field = this.cls.newField(type, name);
        field.setAccess(Access.PRIVATE);

        // PART 2: Make the getXXX() method.
        ClassMethod m1 = this.cls.newMethod(type, "get" + capitalize(name));
        m1.setAccess(Access.PUBLIC);
        m1.newReturn().setExpression(thisvar);

        // PART 3: Make the setXXX(type) method.
        ClassMethod m2 = this.cls.newMethod(type, "set" + capitalize(name));
        m2.setAccess(Access.PUBLIC);
        m2.addParameter(type, name);
        m2.newStmt(this.vm.newAssign(thisvar, var));
    }

    /**
     * Final step: Generate the class. This calls
     * <code>CompilationUnit.encode()</code>
     */
    public void generate() throws Exception {
        this.unit.encode();
    }

    /**
     * Step 1: Internally setup the <code>CompilationUnit</code> and
     * <code>PackageClass</code> to be generated using the given
     * <code>codebase</code>, <code>packageName</code>, and
     * <code>className</code>.
     * 
     * @param codebase
     *            - where the generated class should be written (for example
     *            /tmp).
     * @param packageName
     *            - the name of the package for the generated class (for example
     *            com.example.util).
     * @param className
     *            - the name of the generated class.
     */
    public void initialize(String codebase, String packageName, String className) throws Exception {
        this.unit = this.vm.newCompilationUnit(codebase);
        this.unit.setNamespace(packageName);

        this.cls = this.unit.newClass(className);
        this.cls.setAccess(Access.PUBLIC);
    }

    private String capitalize(String s) {
        char[] chars = s.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }
}
