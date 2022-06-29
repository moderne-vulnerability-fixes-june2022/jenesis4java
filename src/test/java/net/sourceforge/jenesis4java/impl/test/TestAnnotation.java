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

/**
 * Copyright (C) 2008 Richard van Nieuwenhoven - ritchie [at] gmx [dot] at This
 * program is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any
 * later version. This program is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser
 * General Public License for more details. You should have received a copy of
 * the GNU Lesser General Public License along with this program; if not, write
 * to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston,
 * MA 02111-1307, USA.
 */

import net.sourceforge.jenesis4java.*;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import net.sourceforge.jenesis4java.Access;
import net.sourceforge.jenesis4java.Annotation;
import net.sourceforge.jenesis4java.AnnotationAttribute;
import net.sourceforge.jenesis4java.ClassMethod;
import net.sourceforge.jenesis4java.CompilationUnit;
import net.sourceforge.jenesis4java.PackageClass;
import net.sourceforge.jenesis4java.Type;
import net.sourceforge.jenesis4java.VirtualMachine;

import static org.junit.Assert.assertTrue;

public class TestAnnotation {

    @Test
    public void test3() throws IOException {

        VirtualMachine vm = VirtualMachine.getVirtualMachine();
        File tempDirectory = Files.createTempDirectory("jenesisTmp" + "tmp").toFile();
        // make a new compilation unit
        CompilationUnit unit = vm.newCompilationUnit(tempDirectory.getCanonicalPath());
        PackageClass cls = unit.newClass("HelloWorld");
        cls.setAccess(Access.PUBLIC);

        ClassMethod method = cls.newMethod(vm.newType(Type.VOID), "main");
        method.setAccess(Access.PUBLIC);
        method.isStatic(true);
        method.addParameter(vm.newArray("String", 1), "argv");

        cls.addAnnotation("Simple(annotation=\"test\")");
        cls.addAnnotation("Simple2", "annotation2=\"test2\"");

        Annotation ann3 = cls.addAnnotation("Simple3");

        ann3.addAnntationAttribute("annotation3", vm.newString("test3"));
        ann3.addAnntationAttribute("annotation3a", vm.newString("test3a"));
        cls.addAnnotation("NamedQueries", "");

        for (Annotation annotation : cls.getAnnotations()) {
            if ("NamedQueries".equals(annotation.getName())) {
                AnnotationAttribute valueAttribute = annotation.addAnntationAttribute((String) null);
                valueAttribute.addValueAnnotation(vm.newAnnotation("NamedQuery", "text1"));
                valueAttribute.addValueAnnotation(vm.newAnnotation("NamedQuery", "text2"));
            }
        }

        String unitJavaCode = unit.toString();
        int index = unitJavaCode.indexOf("@Simple");
        assertTrue(unitJavaCode.indexOf("HelloWorld") > index && index > 0);
        index = unitJavaCode.indexOf("@Simple3");
        assertTrue(unitJavaCode.indexOf("HelloWorld") > index && index > 0);
        index = unitJavaCode.indexOf("@NamedQueries");
        assertTrue(unitJavaCode.indexOf("HelloWorld") > index && index > 0);
        index = unitJavaCode.indexOf("@NamedQuery");
        assertTrue(unitJavaCode.indexOf("@NamedQueries") < index && index > 0);
    }
}
