package net.sourceforge.jenesis4java.impl;

/*
 * #%L
 * Jenesis 4 Java Code Generator
 * %%
 * Copyright (C) 2000 - 2015 jenesis4java
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import net.sourceforge.jenesis4java.Block;
import net.sourceforge.jenesis4java.Codeable;
import net.sourceforge.jenesis4java.IVisitor;
import net.sourceforge.jenesis4java.PrimitiveType;
import net.sourceforge.jenesis4java.Type;
import net.sourceforge.jenesis4java.impl.test.CollectingVisitor;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static net.sourceforge.jenesis4java.impl.test.TestHelper.*;
import static org.junit.Assert.*;

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
        lambda.newBodyBlock();
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
    public void testVisitEmptyLambda() throws Exception {
        final List<Codeable> visited = new ArrayList<Codeable>();
        IVisitor visitor = new CollectingVisitor(visited);

        lambda.visit(visitor);

        assertEquals(1, visited.size());
        assertSame("expected null comment", null, visited.get(0));
    }

    @Test
    public void testVisitEmptyLambdaWithTypelessParameter() throws Exception {
        final List<Codeable> visited = new ArrayList<Codeable>();
        IVisitor visitor = new CollectingVisitor(visited);

        lambda.addParameter("a");
        lambda.visit(visitor);

        assertEquals(1, visited.size());
        assertSame("expected null comment", null, visited.get(0));
    }

    @Test
    public void testVisitEmptyLambdaWithTypedParameter() throws Exception {
        final List<Codeable> visited = new ArrayList<>();
        IVisitor visitor = new CollectingVisitor(visited);

        PrimitiveType intType = vm().newType(Type.INT);
        lambda.addParameter(intType, "a");
        lambda.visit(visitor);

        assertEquals(3, visited.size());
        assertSame("expected null lambda comment", null, visited.get(0));
        assertSame("expected null type comment", null, visited.get(1));
        assertSame(intType, visited.get(2));
    }

    @Test
    public void testVisitSimpleLambda() throws Exception {
        final List<Codeable> visited = new ArrayList<Codeable>();
        IVisitor visitor = new CollectingVisitor(visited);

        PrimitiveType intType = vm().newType(Type.INT);
        lambda.addParameter(intType, "a");
        Block block = lambda.newBodyBlock();
        block.newReturn().setExpression(vm().newInt(10));

        lambda.visit(visitor);

        // TODO: this is wrong
        assertEquals(3, visited.size());
        assertSame("expected null lambda comment", null, visited.get(0));
        assertSame("expected null type comment", null, visited.get(1));
        assertSame(intType, visited.get(2));
    }
}
