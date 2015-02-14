package net.sourceforge.jenesis4java.impl.test;

import java.io.File;
import java.io.IOException;

import net.sourceforge.jenesis4java.CompilationUnit;
import net.sourceforge.jenesis4java.PackageClass;
import net.sourceforge.jenesis4java.impl.MVM;

import org.junit.Assert;
import org.junit.Test;

public class TestGenerics {

    @Test
    public void testGenerics() throws IOException {

        CompilationUnit unit = MVM.getVirtualMachine().newCompilationUnit("/tmp");
        unit.setNamespace("at.xx");
        PackageClass clazz = unit.newClass("Service<Request extends IMessage, Response extends IMessage>");
        unit.encode();
        Assert.assertTrue(new File("/tmp/at/xx/Service.java").exists());
    }
}
