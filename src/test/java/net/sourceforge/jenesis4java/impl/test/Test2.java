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
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;
import net.sourceforge.jenesis4java.AbstractMethod;
import net.sourceforge.jenesis4java.Access;
import net.sourceforge.jenesis4java.Binary;
import net.sourceforge.jenesis4java.ClassDeclaration;
import net.sourceforge.jenesis4java.ClassField;
import net.sourceforge.jenesis4java.ClassMethod;
import net.sourceforge.jenesis4java.ClassType;
import net.sourceforge.jenesis4java.Codeable;
import net.sourceforge.jenesis4java.CompilationUnit;
import net.sourceforge.jenesis4java.Constant;
import net.sourceforge.jenesis4java.Constructor;
import net.sourceforge.jenesis4java.IVisitor;
import net.sourceforge.jenesis4java.Import;
import net.sourceforge.jenesis4java.Interface;
import net.sourceforge.jenesis4java.Namespace;
import net.sourceforge.jenesis4java.PackageClass;
import net.sourceforge.jenesis4java.StaticInitializer;
import net.sourceforge.jenesis4java.Type;
import net.sourceforge.jenesis4java.VirtualMachine;

import org.junit.*;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class Test2 {

    private final class OrganizeImports implements IVisitor {

        Set<String> imports = new HashSet<String>();

        @Override
        public Codeable visitReplace(Codeable current, Codeable parent) {
            if (current instanceof Import) {
                imports.add(((Import) current).getName());
            }
            if (current instanceof ClassDeclaration) {
                ClassDeclaration classDecl = (ClassDeclaration) current;
                classDecl.setExtends(importResolvedClassName(classDecl.getExtends()));
                List<String> classNames = new ArrayList<String>(classDecl.getImplements());
                for (String className : classNames) {
                    String newClassName = importResolvedClassName(className);
                    if (!newClassName.equals(className)) {
                        classDecl.removeImplements(className);
                        classDecl.addImplements(newClassName);
                    }
                }
            }
            if (current instanceof Interface) {
                Interface classDecl = (Interface) current;
                List<String> classNames = new ArrayList<String>(classDecl.getExtends());
                for (String className : classNames) {
                    String newClassName = importResolvedClassName(className);
                    if (!newClassName.equals(className)) {
                        classDecl.removeExtends(className);
                        classDecl.addExtends(newClassName);
                    }
                }

            }

            if (current instanceof ClassType) {
                String className = ((ClassType) current).getName();
                String newClassName = importResolvedClassName(className);
                if (!newClassName.equals(className)) {
                    return vm.newType(newClassName);
                }
            }
            return current;
        }

        private String importResolvedClassName(String className) {
            String newClassName = className;
            if (className.indexOf('.') > 0 && imports.contains(className)) {
                newClassName = className.substring(className.lastIndexOf('.') + 1);
            }
            return newClassName;
        }
    }

    private VirtualMachine vm = VirtualMachine.getVirtualMachine();

    @Test
    public void test2() throws IOException {
        File tempDirectory = Files.createTempDirectory("jenesisTmp" + "tmp").toFile();
        // make a new compilation unit
        CompilationUnit unit = this.vm.newCompilationUnit(tempDirectory.getCanonicalPath());

        Namespace namespace = unit.setNamespace("com.inxar.jenesis.test");
        namespace.getName();

        // test interface
        Interface ifc = unit.newInterface("MyInterface");
        ifc.setAccess(Access.PUBLIC);
        ifc.isFinal(true);
        ifc.addExtends("Ifc1");
        ifc.addExtends("Ifc2");
        ifc.addExtends("Ifc3");

        // test constant
        Constant constant1 = ifc.newConstant(this.vm.newType(Type.BYTE), "BYTE_01");
        constant1.setExpression(this.vm.newByte((byte) 22));
        Constant constant2 = ifc.newConstant("INT_02", 23);
        constant2.getName();
        // test abstract methods
        AbstractMethod am1 = ifc.newMethod(this.vm.newType("String"), "toString");
        am1.getName();
        AbstractMethod am2 = ifc.newMethod(this.vm.newType("Class"), "forName");
        am2.addParameter(this.vm.newType("String"), "packageName");
        am2.addParameter(this.vm.newType("String"), "className");

        // test class
        PackageClass cls = unit.newClass("MyClass");
        cls.setAccess(Access.PROTECTED);
        cls.isFinal(true);
        cls.isAbstract(true);
        cls.isStatic(true);
        cls.setExtends("Object");
        cls.addImplements("java.util.Iterator");
        cls.addImplements("java.util.Enumeration");

        // test constructor
        Constructor con1 = cls.newConstructor();
        con1.setAccess(Access.PRIVATE);
        con1.addParameter(this.vm.newType(Type.BOOLEAN), "isEnabled");

        Constructor con2 = cls.newConstructor();
        con2.setAccess(Access.PROTECTED);
        con2.addParameter(this.vm.newType(Type.BOOLEAN), "isEnabled");

        // test static init
        StaticInitializer si = cls.newStaticInitializer();
        si.getComment();
        // test class field
        ClassField cf1 = cls.newField(this.vm.newType(Type.FLOAT), "price");
        cf1.setAccess(Access.PUBLIC);
        cf1.isStatic(true);
        cf1.isFinal(true);
        cf1.isTransient(true);
        cf1.isVolatile(true);
        cf1.setExpression(this.vm.newBinary(Binary.ADD, this.vm.newInt(10), this.vm.newFloat(12.3F)));

        if (cls.isMethodAlreadyDefined(this.vm.newType(Type.VOID), "compute")) {
            System.out.println("already defined");
        } else {
            System.out.println("not defined");

        }

        // test class method
        ClassMethod cm1 = cls.newMethod(this.vm.newType(Type.VOID), "compute");
        cm1.setAccess(Access.PUBLIC);
        cm1.isNative(true);
        cm1.isAbstract(true);
        cm1.isFinal(true);
        cm1.isStatic(true);
        cm1.addThrows("Exception");
        cm1.addThrows("Throwable");

        if (cls.isMethodAlreadyDefined(this.vm.newType(Type.VOID), "compute")) {
            System.out.println("already defined");
        } else {
            System.out.println("not defined");

        }

        unit.visit(new OrganizeImports());
        assertTrue(cls.getImplements().indexOf("java.util.Iterator") >= 0);

        cls.addImport("java.util.Iterator");

        unit.visit(new OrganizeImports());
        assertTrue(cls.getImplements().indexOf("java.util.Iterator") < 0);
        assertTrue(cls.getImplements().indexOf("Iterator") >= 0);

    }

    @Test
    public void testNewAnonymousClass() {
        assertTrue(vm.newAnonymousClass("test", "gen1", "gen2").toString().startsWith("new test<gen1, gen2>() {"));
        assertTrue(vm.newAnonymousClass("test").toString().startsWith("new test() {"));
    }
}
