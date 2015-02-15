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
