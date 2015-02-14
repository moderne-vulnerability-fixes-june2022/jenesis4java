package net.sourceforge.jenesis4java.impl;

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
