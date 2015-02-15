package net.sourceforge.jenesis4java.impl.test;

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
