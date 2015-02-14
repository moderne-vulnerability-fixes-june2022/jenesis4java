package net.sourceforge.jenesis4java.impl;

import net.sourceforge.jenesis4java.Block;
import net.sourceforge.jenesis4java.Codeable;
import net.sourceforge.jenesis4java.IVisitor;
import net.sourceforge.jenesis4java.Type;
import net.sourceforge.jenesis4java.impl.test.CollectingVisitor;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static net.sourceforge.jenesis4java.impl.test.TestHelper.*;
import static org.junit.Assert.assertEquals;

public class MLambdaTest {

    private MLambda lambda;

    @Before
    public void setup() {
        lambda = new MLambda(vm());
    }

    @Test
    public void testAddOneTypeParameter() throws Exception {
        lambda.setBody(vm().newInvoke("System.out", "println").addArg("Hallo"));
        lambda.addParameter(vm().newType(Type.INT), "a");
        assertEquals("(int a) -> System.out.println(\"Hallo\");", toCodeAsString(lambda));
    }

    @Test
    public void testAddOneClassParameter() throws Exception {
        lambda.setBody(vm().newInvoke("System.out", "println").addArg("Hallo"));
        lambda.addParameter(int.class, "a");
        assertEquals("(int a) -> System.out.println(\"Hallo\");", toCodeAsString(lambda));
    }

    @Test
    public void testAddTwoTypeParameter() throws Exception {
        lambda.setBody(vm().newInvoke("System.out", "println").addArg("Hallo"));
        lambda.addParameter(vm().newType(Type.INT), "a").addParameter(vm().newType(Type.LONG), "b");
        assertEquals("(int a, long b) -> System.out.println(\"Hallo\");", toCodeAsString(lambda));
    }

    @Test
    public void testAddOneParameterByName() throws Exception {
        lambda.setBody(vm().newInvoke("System.out", "println").addArg("Hallo")).addParameter("a");
        assertEquals("a -> System.out.println(\"Hallo\");", toCodeAsString(lambda));
    }

    @Test
    public void testAddTwoParametersByName() throws Exception {
        lambda.setBody(vm().newInvoke("System.out", "println").addArg("Hallo"));
        lambda.addParameter("a").addParameter("b");
        assertEquals("(a, b) -> System.out.println(\"Hallo\");", toCodeAsString(lambda));
    }

    @Test(expected = RuntimeException.class)
    public void testMixParametersTypes() throws Exception {
        lambda.addParameter("a").addParameter(int.class, "b");
    }

    @Test(expected = RuntimeException.class)
    public void testMixParametersTypes2() throws Exception {
        lambda.addParameter(int.class, "a").addParameter("b");
    }

    @Test
    public void testSetBodyExpr1() throws Exception {
        lambda.setBody(vm().newInvoke("System.out", "println").addArg("Hallo"));
        assertEquals("() -> System.out.println(\"Hallo\");", toCodeAsString(lambda));
    }

    @Test
    public void testSetBodyExpr2() throws Exception {
        lambda.setBody(vm().newInvoke("System.out", "println").addArg("Hello"));
        assertEquals("() -> System.out.println(\"Hello\");", toCodeAsString(lambda));
    }

    @Test
    public void testEmptyBodyBlock() throws Exception {
        Block block = lambda.newBodyBlock();
        assertEquals("() -> {" + LS + //
                "};", toCodeAsString(lambda));
    }

    @Test
    public void testSimpleBodyBlock() throws Exception {
        Block block = lambda.newBodyBlock();
        block.newReturn().setExpression(vm().newInt(10));
        assertEquals("() -> {" + LS + //
                "    return 10;" + LS + //
                "};", toCodeAsString(lambda));
    }

    @Test
    public void testVisit() throws Exception {
        final List<Codeable> visited = new ArrayList<Codeable>();
        IVisitor visitor = new CollectingVisitor(visited);

        lambda.visit(visitor);
    }
}
