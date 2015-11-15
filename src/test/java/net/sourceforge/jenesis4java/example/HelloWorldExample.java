package net.sourceforge.jenesis4java.example;

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
 * Copyright (C) 2008 Richard van Nieuwenhoven - ritchie [at] gmx [dot] at
 * Copyright (C) 2000, 2001 Paul Cody Johnston - pcj@inxar.org This program is
 * free software; you can redistribute it and/or modify it under the terms of
 * the GNU Lesser General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details. You should have received a copy of the GNU Lesser General
 * Public License along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 */
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import net.sourceforge.jenesis4java.Access;
import net.sourceforge.jenesis4java.ClassMethod;
import net.sourceforge.jenesis4java.Comment;
import net.sourceforge.jenesis4java.CompilationUnit;
import net.sourceforge.jenesis4java.Invoke;
import net.sourceforge.jenesis4java.PackageClass;
import net.sourceforge.jenesis4java.Type;
import net.sourceforge.jenesis4java.VirtualMachine;

/**
 * Example which generates a class which prints "Hello World!" to standard
 * output.
 */
class HelloWorldExample {

    public static void main(String[] argv) throws Exception {
        long start = System.currentTimeMillis(), time;

        File temp = new File("target/testgenerate");
        boolean resMkdirs = temp.mkdirs();

        if (!resMkdirs) {
            System.out.println("Error creating temp directories!");
            return;
        }

        // Get the VirtualMachine implementation.
        VirtualMachine vm = VirtualMachine.getVirtualMachine();

        // Instantiate a new CompilationUnit. The argument to the
        // compilation unit is the "codebase" or directory where the
        // compilation unit should be written.
        //
        // Make a new compilation unit rooted to the given sourcepath.
        CompilationUnit unit = vm.newCompilationUnit(temp.getCanonicalPath());

        // Set the package namespace.
        unit.setNamespace("com.example.jenesis");

        // Add an import statement for fun.
        unit.addImport("java.io.Serializable");

        // Comment the package with a javadoc (DocumentationComment).
        unit.setComment(Comment.D, "Auto-Generated using the Jenesis Syntax API");

        // Make a new class.
        PackageClass cls = unit.newClass("HelloWorld");
        // Make it a public class.
        cls.setAccess(Access.PUBLIC);
        // Extend Object just for fun.
        cls.setExtends("Object");
        // Implement serializable just for fun.
        cls.addImplements("Serializable");
        // Comment the class with a javadoc (DocumentationComment).
        unit.setComment(Comment.D, "The HelloWorld example class.");

        // Make a new Method in the Class having type VOID and name "main".
        ClassMethod method = cls.newMethod(vm.newType(Type.VOID), "main");
        // Make it a public method.
        method.setAccess(Access.PUBLIC);
        // Make it a static method
        method.isStatic(true);
        // Add the "String[] argv" formal parameter.
        method.addParameter(vm.newArray("String", 1), "argv");

        // Create a new Method Invocation expression.
        Invoke println = vm.newInvoke("System.out", "println");
        // Add the Hello World string literal as the sole argument.
        println.addArg(vm.newString("Hello World!"));
        // Add this expression to the method in a statement.
        method.newStmt(println);

        time = System.currentTimeMillis() - start;
        System.out.println("constructed in " + time + " ms");

        // Encode the file, compile it, and load the class
        unit.encode();

        Process exec = Runtime.getRuntime().exec(new String[]{
            "javac",
            "-d",
            "target/classes",
            temp.getCanonicalPath() + "/com/example/jenesis/HelloWorld.java"
        });
        exec.waitFor();
        if (exec.exitValue() != 0) {
            String streambuffer1 = readStream(exec.getErrorStream());
            String streambuffer2 = readStream(exec.getInputStream());

            throw new RuntimeException(streambuffer1 + "\n" + streambuffer2);
        }
        Class<?> hello = Thread.currentThread().getContextClassLoader().loadClass("com.example.jenesis.HelloWorld");

        time = System.currentTimeMillis() - start;
        System.out.println("loaded in " + time + " ms");
        // Get the main method
        java.lang.reflect.Method main = hello.getMethod("main", new Class[]{
            String[].class
        });

        // Invoke it
        main.invoke(hello.newInstance(), new Object[]{
            new String[]{}
        });

        time = System.currentTimeMillis() - start;
        System.out.println("done in " + time + " ms");
    }

    private static String readStream(InputStream error) throws IOException {
        ByteArrayOutputStream errorBytes = new ByteArrayOutputStream();
        int count;
        byte[] buffer = new byte[4096];
        while ((count = error.read(buffer)) >= 0) {
            errorBytes.write(buffer, 0, count);
        }
        return new String(buffer);
    }
}
