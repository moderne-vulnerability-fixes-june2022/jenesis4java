package net.sourceforge.jenesis4java.impl;

import junit.framework.TestCase;
import net.sourceforge.jenesis4java.CompilationUnit;
import net.sourceforge.jenesis4java.Import;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.List;

public class MCompilationUnitTest {

    private CompilationUnit cu;

    @Before
    public void setUp() {
        String sourcePath = "net.sourceforge.jenesis4java.test";
        cu = MVM.getVirtualMachine().newCompilationUnit(sourcePath);
    }

    @Test
    public void testAddImportClassDuplicate() throws Exception {
        Import imp1 = cu.addImport(List.class);
        Import imp2 = cu.addImport(List.class);
        assertEquals(imp1, imp2);
    }

    @Test
    public void testAddImportClassConflict() throws Exception {
        Import imp1 = cu.addImport(List.class);
        Import imp2 = cu.addImport(java.awt.List.class);
        assertNull("Expected import to be ignored as", imp2);
    }
}