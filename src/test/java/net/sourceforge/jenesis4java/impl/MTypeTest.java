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
import net.sourceforge.jenesis4java.impl.test.TestHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.concurrent.FutureTask;

import static net.sourceforge.jenesis4java.impl.test.TestHelper.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MTypeTest {

    private CompilationUnit cu;

    @Before
    public void setUp() {
        String sourcePath = "net.sourceforge.jenesis4java.test";
        cu = vm().newCompilationUnit(sourcePath);
    }

    @Test
    public void typeWithJavaLangToCode() throws Exception {
        ClassType classType = vm().newType(Object.class);
        Assert.assertEquals("Object", TestHelper.toCodeAsString(classType));
    }

    @Test
    public void typeWithImportToCode() throws Exception {
        Import imp1 = cu.addImport(FutureTask.class.getPackage().getName() + ".*");
        PackageClass testClass = cu.newClass("Test");

        ClassType classType = vm().newType(FutureTask.class);
        testClass.newField(classType, "t");
        Assert.assertEquals("import java.util.concurrent.*;" + LS + //
                "" + LS + //
                "class Test" + LS + //
                "{" + LS + //
                "    FutureTask t;" + LS + //
                "}", TestHelper.toCodeAsString(cu));
    }
}
