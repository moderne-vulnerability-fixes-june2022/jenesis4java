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

import net.sourceforge.jenesis4java.*;
import net.sourceforge.jenesis4java.impl.test.CollectingVisitor;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static net.sourceforge.jenesis4java.impl.test.TestHelper.*;

import static org.junit.Assert.*;

public class MCatchTest {

    private Catch aCatch;

    @Before
    public void setUp() {
        String sourcePath = "net.sourceforge.jenesis4java.test";
        CompilationUnit cu = vm().newCompilationUnit(sourcePath);
        PackageClass cls = cu.newClass("HelloWorld");
        ClassMethod test = cls.newMethod("test");
        Try aTry = test.newTry();
        aCatch = aTry.newCatch(vm().newType(IOException.class), "e1");
    }

    @Test
    public void testGetThrowable() throws Exception {
        FormalParameter parameter = aCatch.getThrowable();

        assertEquals("e1", parameter.getName());
        Type type = parameter.getType();
        assertTrue(type instanceof ClassType);
        ClassType classType = (ClassType) type;
        assertEquals(IOException.class.getName(), classType.getName());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetThrowablesUnmodifiable() throws Exception {
        List<FormalParameter> parameters = aCatch.getThrowables();
        parameters.add(null);
    }

    @Test
    public void testSetThrowable() throws Exception {
        aCatch.setThrowable(vm().newType(SQLException.class), "e");
        FormalParameter parameter = aCatch.getThrowable();

        assertEquals("e", parameter.getName());
        Type type = parameter.getType();
        assertTrue(type instanceof ClassType);
        ClassType classType = (ClassType) type;
        assertEquals(SQLException.class.getName(), classType.getName());
    }

    @Test
    public void testAddThrowable() throws Exception {
        assertSame(aCatch, aCatch.addThrowable(vm().newType(SQLException.class), "e2"));

        List<FormalParameter> parameters = aCatch.getThrowables();
        assertEquals(2, parameters.size());

        FormalParameter parameter = parameters.get(0);
        assertEquals("e1", parameter.getName());
        Type type = parameter.getType();
        assertTrue(type instanceof ClassType);
        ClassType classType = (ClassType) type;
        assertEquals(IOException.class.getName(), classType.getName());

        parameter = parameters.get(1);
        assertEquals("e2", parameter.getName());
        type = parameter.getType();
        assertTrue(type instanceof ClassType);
        classType = (ClassType) type;
        assertEquals(SQLException.class.getName(), classType.getName());
    }

    @Test
    public void testVisitSimple() throws Exception {
        aCatch.setComment(Comment.S, "a comment");
        final List<Codeable> visited = new ArrayList<Codeable>();
        IVisitor visitor = new CollectingVisitor(visited);
        aCatch.visit(visitor);

        assertEquals(2, visited.size());
    }

    @Test
    public void testVisitMultiple() throws Exception {
        aCatch.setComment(Comment.S, "a comment");
        aCatch.addThrowable(vm().newType(SQLException.class), "e2");
        final List<Codeable> visited = new ArrayList<Codeable>();
        IVisitor visitor = new CollectingVisitor(visited);
        aCatch.visit(visitor);

        assertEquals(3, visited.size());
    }

    // Note: toString uses toCode internally...
    @Test
    public void toCodeSimple() {
        assertTrue(aCatch.toString().startsWith("catch (java.io.IOException e1) {"));
    }

    @Test
    public void toCodeMultiple() {
        aCatch.addThrowable(vm().newType(SQLException.class), "e2");
        assertTrue(aCatch.toString().startsWith("catch (java.io.IOException e1 | java.sql.SQLException e2) {"));
    }
}
