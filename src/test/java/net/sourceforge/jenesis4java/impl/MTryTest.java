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

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static net.sourceforge.jenesis4java.impl.test.TestHelper.*;
import static org.junit.Assert.assertEquals;

public class MTryTest {

    private Try aTry;

    private static VirtualMachine vm() {
        return MVM.getVirtualMachine();
    }

    @Before
    public void setUp() {
        String sourcePath = "net.sourceforge.jenesis4java.test";
        CompilationUnit cu = vm().newCompilationUnit(sourcePath);
        PackageClass cls = cu.newClass("HelloWorld");
        ClassMethod test = cls.newMethod("test");
        aTry = test.newTry();
    }

    // Note: toString uses toCode internally...
    @Test
    public void toCodeStandardTy() {
        Catch aCatch = aTry.newCatch(vm().newType(IOException.class), "e");
        Finally aFinally = aTry.getFinally();
        assertEquals("" + //
                "try {" + LS + //
                "}" + LS + //
                "catch (java.io.IOException e) {" + LS + //
                "}" + LS + //
                "finally {" + LS + //
                "}", aTry.toString());
    }

    @Test
    public void toCodeTryWithResources() {
        TryResource tryResource = aTry.newResource(getFOSType(), "fo", vm().newClass(getFOSType()).addArg(vm().newVar("file")));
        tryResource.isFinal(true);
        aTry.newResource(getFOSType(), "fo2", vm().newClass(getFOSType()).addArg(vm().newVar("file2")));
        TryResource tryResource3 = aTry.newResource(getFOSType(), "fo3", vm().newClass(getFOSType()).addArg(vm().newVar("file3")));
        tryResource3.isFinal(true);
        aTry.newCatch(vm().newType(IOException.class), "e");
        aTry.getFinally();
        assertEquals("" + //
                "try(final java.io.FileOutputStream fo = new java.io.FileOutputStream(file);" + LS + //
                "    java.io.FileOutputStream fo2 = new java.io.FileOutputStream(file2);" + LS + //
                "    final java.io.FileOutputStream fo3 = new java.io.FileOutputStream(file3)) {" + LS + //
                "}" + LS + //
                "catch (java.io.IOException e) {" + LS + //
                "}" + LS + //
                "finally {" + LS + //
                "}", aTry.toString());
    }

    private ClassType getFOSType() {
        return vm().newType(FileOutputStream.class);
    }

    @Test
    public void testVisit() throws Exception {
        final List<Codeable> visited = new ArrayList<Codeable>();
        IVisitor visitor = new CollectingVisitor(visited);
        Catch aCatch = aTry.newCatch(vm().newType(IOException.class), "e");
        Finally aFinally = aTry.getFinally();

        aTry.visit(visitor);

        assertEquals(3, visited.size());
    }

    @Test
    public void testVisitWithResources() throws Exception {
        final List<Codeable> visited = new ArrayList<Codeable>();
        IVisitor visitor = new CollectingVisitor(visited);
        aTry.newResource(getFOSType(), "fo", vm().newClass(getFOSType()).addArg(vm().newVar("file")));
        aTry.newResource(getFOSType(), "fo2", vm().newClass(getFOSType()).addArg(vm().newVar("file2")));
        Catch aCatch = aTry.newCatch(vm().newType(IOException.class), "e");
        Finally aFinally = aTry.getFinally();

        aTry.visit(visitor);

        // 1 for try comment + 2 for resources + 1 for catch + 1 for finally
        assertEquals(5, visited.size());
    }
}
