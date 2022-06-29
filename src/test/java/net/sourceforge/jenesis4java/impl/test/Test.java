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
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import net.sourceforge.jenesis4java.AbstractMethod;
import net.sourceforge.jenesis4java.Access;
import net.sourceforge.jenesis4java.ArrayInitializer;
import net.sourceforge.jenesis4java.Assign;
import net.sourceforge.jenesis4java.Binary;
import net.sourceforge.jenesis4java.Break;
import net.sourceforge.jenesis4java.Catch;
import net.sourceforge.jenesis4java.ClassField;
import net.sourceforge.jenesis4java.ClassMethod;
import net.sourceforge.jenesis4java.Codeable;
import net.sourceforge.jenesis4java.Comment;
import net.sourceforge.jenesis4java.CompilationUnit;
import net.sourceforge.jenesis4java.Constant;
import net.sourceforge.jenesis4java.Constructor;
import net.sourceforge.jenesis4java.Continue;
import net.sourceforge.jenesis4java.DoWhile;
import net.sourceforge.jenesis4java.DocumentationComment;
import net.sourceforge.jenesis4java.Else;
import net.sourceforge.jenesis4java.ElseIf;
import net.sourceforge.jenesis4java.Empty;
import net.sourceforge.jenesis4java.Expression;
import net.sourceforge.jenesis4java.ExpressionStatement;
import net.sourceforge.jenesis4java.Finally;
import net.sourceforge.jenesis4java.For;
import net.sourceforge.jenesis4java.Freeform;
import net.sourceforge.jenesis4java.If;
import net.sourceforge.jenesis4java.InnerClass;
import net.sourceforge.jenesis4java.Interface;
import net.sourceforge.jenesis4java.Let;
import net.sourceforge.jenesis4java.LocalBlock;
import net.sourceforge.jenesis4java.LocalClass;
import net.sourceforge.jenesis4java.Member;
import net.sourceforge.jenesis4java.Namespace;
import net.sourceforge.jenesis4java.NewArray;
import net.sourceforge.jenesis4java.PackageClass;
import net.sourceforge.jenesis4java.Return;
import net.sourceforge.jenesis4java.StaticInitializer;
import net.sourceforge.jenesis4java.Switch;
import net.sourceforge.jenesis4java.Synchronized;
import net.sourceforge.jenesis4java.Ternary;
import net.sourceforge.jenesis4java.Throw;
import net.sourceforge.jenesis4java.Try;
import net.sourceforge.jenesis4java.Type;
import net.sourceforge.jenesis4java.Unary;
import net.sourceforge.jenesis4java.Variable;
import net.sourceforge.jenesis4java.VirtualMachine;
import net.sourceforge.jenesis4java.While;
import net.sourceforge.jenesis4java.impl.BasicCompilationUnitEncoder;
import net.sourceforge.jenesis4java.jaloppy.JenesisJalopyEncoder;
import org.junit.Before;

public class Test {

    VirtualMachine vm;

    @org.junit.Test
    public void test1() throws IOException {
        this.vm = VirtualMachine.getVirtualMachine();
        File tempDirectory = Files.createTempDirectory("jenesisTmp" + "tmp").toFile();
        // make a new compilation unit
        CompilationUnit unit = this.vm.newCompilationUnit(tempDirectory.getCanonicalPath());

        // test documentation comment
        DocumentationComment doc = (DocumentationComment) doc(unit, Comment.D);
        doc.setAuthor("ipsem lorem dolor ipsem lorem dolor ipsem lorem dolor ipsem lorem dolor ipsem lorem dolor ");
        doc.setDate("ipsem lorem dolor");
        doc.setDeprecated("ipsem lorem dolor");
        doc.setException("ipsem lorem dolor");
        doc.setReturn("ipsem lorem dolor");
        doc.setSerial("ipsem lorem dolor");
        doc.setSerialData("ipsem lorem dolor");
        doc.setSerialField("ipsem lorem dolor");
        doc.setSince("ipsem lorem dolor");
        doc.setThrows("ipsem lorem dolor");
        doc.setVersion("ipsem lorem dolor");
        doc.addParam("ipsem lorem dolor");
        doc.addParam("ipsem lorem dolor");
        doc.addSee("ipsem lorem dolor");
        doc.addSee("ipsem lorem dolor");

        // test package decl
        Namespace namespace = unit.setNamespace("com.inxar.jenesis.test");
        doc(namespace, Comment.S);

        // test import decl
        doc(unit.addImport("java.io.*"), Comment.S);
        doc(unit.addImport("java.util.Hashtable.*"), Comment.S);
        unit.addImport("java.util.Vector");
        unit.addImport("com.inxar.jenesis.test.*");

        // test interface
        Interface ifc = unit.newInterface("MyInterface");
        ifc.setAccess(Access.PUBLIC);
        ifc.isFinal(true);
        ifc.addExtends("Ifc1");
        ifc.addExtends("Ifc2");
        ifc.addExtends("Ifc3");
        doc(ifc, Comment.M);

        // test constant
        Constant constant1 = ifc.newConstant(this.vm.newType(Type.BYTE), "BYTE_01");
        constant1.setExpression(this.vm.newByte((byte) 22));
        doc(constant1, Comment.D);
        Constant constant2 = ifc.newConstant("INT_02", 23);
        constant2.setComment(Comment.D, "Niks besonderes");

        // test abstract methods
        AbstractMethod am1 = ifc.newMethod(this.vm.newType(String.class.getName()), "toString");
        doc(am1, Comment.D);
        AbstractMethod am2 = ifc.newMethod(this.vm.newType("Class"), "forName");
        am2.addParameter(this.vm.newType(String.class.getName()), "packageName");
        am2.addParameter(this.vm.newType(String.class.getName()), "className");

        // test class
        PackageClass cls = unit.newClass("MyClass");
        cls.setAccess(Access.PROTECTED);
        cls.isFinal(true);
        cls.isAbstract(true);
        cls.isStatic(true);
        cls.setExtends("Object");
        cls.addImplements("java.util.Iterator");
        cls.addImplements("java.util.Enumeration");
        doc(cls, Comment.M);

        // test constructor
        Constructor con1 = cls.newConstructor();
        con1.setAccess(Access.PRIVATE);
        con1.addParameter(this.vm.newType(Type.BOOLEAN), "isEnabled");
        doc(con1, Comment.D);

        Constructor con2 = cls.newConstructor();
        con2.setAccess(Access.PROTECTED);
        con2.addParameter(this.vm.newType(Type.BOOLEAN), "isEnabled");

        // test static init
        StaticInitializer si = cls.newStaticInitializer();
        doc(si, Comment.M);

        // test class field
        ClassField cf1 = cls.newField(this.vm.newType(Type.FLOAT), "price");
        cf1.setAccess(Access.PUBLIC);
        cf1.isStatic(true);
        cf1.isFinal(true);
        cf1.isTransient(true);
        cf1.isVolatile(true);
        cf1.setExpression(this.vm.newBinary(Binary.ADD, this.vm.newInt(10), this.vm.newFloat(12.3F)));
        doc(cf1, Comment.S);

        // test class method
        ClassMethod cm1 = cls.newMethod(this.vm.newType(Type.VOID), "compute");
        cm1.setAccess(Access.PUBLIC);
        cm1.isNative(true);
        cm1.isAbstract(false);
        cm1.isFinal(true);
        cm1.isStatic(true);
        cm1.addThrows("Exception");
        cm1.addThrows("Throwable");
        doc(cm1, Comment.D);

        // test simple statements
        Break break1 = cm1.newBreak();
        break1.setLabel("forth");
        break1.setGoto("away");
        doc(break1, Comment.S);
        Continue continue1 = cm1.newContinue();
        continue1.setLabel("away");
        continue1.setGoto("forth");
        doc(continue1, Comment.S);
        Return return1 = cm1.newReturn();
        doc(return1, Comment.S);
        Empty empty1 = cm1.newEmpty();
        doc(empty1, Comment.S);
        Throw throw1 = cm1.newThrow(this.vm.newClass(this.vm.newType("Exception")));
        throw1.getLabel();
        doc(empty1, Comment.S);

        // test let
        Let let1 = cm1.newLet(this.vm.newType("java.util.Vector"));
        let1.addAssign("v", this.vm.newNull());
        let1.addAssign("w", this.vm.newNull());
        let1.addAssign("x", this.vm.newNull());
        doc(let1, Comment.M);

        // test local block
        LocalBlock local1 = cm1.newLocalBlock();
        local1.newBreak();
        doc(local1, Comment.M);

        // test if
        If if1 = cm1.newIf(this.vm.newTrue());
        if1.newBreak();
        doc(if1, Comment.M);

        // test else if
        ElseIf elseIf1 = if1.newElseIf(this.vm.newFalse());
        elseIf1.newBreak();
        doc(elseIf1, Comment.M);

        // test else
        Else else1 = if1.getElse();
        else1.newBreak();
        doc(else1, Comment.M);

        // test while
        While while1 = cm1.newWhile(this.vm.newTrue());
        while1.newContinue().setLabel("on");
        doc(while1, Comment.M);

        // test do while
        DoWhile do1 = cm1.newDoWhile(this.vm.newTrue());
        do1.newBreak();
        doc(do1, Comment.M);

        // test switch
        Switch switch1 = cm1.newSwitch(this.vm.newVar("input"));
        switch1.newCase(this.vm.newInt(1)).newBreak();
        switch1.newCase(this.vm.newInt(2)).newBreak();
        switch1.newCase(this.vm.newInt(3)).newBreak();
        switch1.getDefault().newBreak();
        doc(switch1, Comment.S);

        // test synchronized
        Synchronized sync1 = cm1.newSynchronized(this.vm.newVar("this"));
        sync1.newBreak();
        doc(sync1, Comment.M);

        // test for
        For for1 = cm1.newFor();
        Let let2 = for1.setInit(this.vm.newType(Type.INT));
        doc(let2, Comment.S);
        let2.addAssign("i", this.vm.newInt(0));
        for1.setPredicate(this.vm.newTrue());
        for1.addUpdate(this.vm.newUnary(Unary.PI, this.vm.newVar("i")));
        for1.newBreak();

        // test for
        For for2 = cm1.newFor();
        for2.setPredicate(this.vm.newTrue());

        // test try
        Try try1 = cm1.newTry();
        try1.newBreak();
        Catch catch1 = try1.newCatch(this.vm.newType("IOException"), "ioex");
        catch1.newBreak();
        Catch catch2 = try1.newCatch(this.vm.newType("Exception"), "ex");
        catch2.newBreak();
        doc(catch2, Comment.M);
        Finally fin1 = try1.getFinally();
        fin1.newBreak();
        doc(fin1, Comment.M);
        doc(try1, Comment.M);

        // test local class
        LocalClass lc1 = cm1.newLocalClass("MyLocal");
        doc(lc1, Comment.D);

        // test free statement
        Freeform free1 = this.vm.newFree("int i = 1*4 | 3*34");
        ExpressionStatement stmt1 = cm1.newStmt(free1);
        doc(stmt1, Comment.M);

        // test all expressions
        Variable a = this.vm.newVar("a");
        Variable b = this.vm.newVar("b");
        Variable c = this.vm.newVar("c");

        // unary expressions
        cm1.newStmt(this.vm.newUnary(Unary.GROUP, a));
        cm1.newStmt(this.vm.newUnary(Unary.NOT, a));
        cm1.newStmt(this.vm.newUnary(Unary.BITWISE_NOT, a));
        cm1.newStmt(this.vm.newUnary(Unary.NEG, a));
        cm1.newStmt(this.vm.newUnary(Unary.POS, a));
        cm1.newStmt(this.vm.newUnary(Unary.AI, a));
        cm1.newStmt(this.vm.newUnary(Unary.PI, a));
        cm1.newStmt(this.vm.newUnary(Unary.AD, a));
        cm1.newStmt(this.vm.newUnary(Unary.PD, a));

        // binary expressions
        cm1.newStmt(this.vm.newBinary(Binary.LAND, a, b));
        cm1.newStmt(this.vm.newBinary(Binary.LOR, a, b));
        cm1.newStmt(this.vm.newBinary(Binary.BAND, a, b));
        cm1.newStmt(this.vm.newBinary(Binary.BOR, a, b));
        cm1.newStmt(this.vm.newBinary(Binary.XOR, a, b));
        cm1.newStmt(this.vm.newBinary(Binary.LEFT, a, b));
        cm1.newStmt(this.vm.newBinary(Binary.RIGHT, a, b));
        cm1.newStmt(this.vm.newBinary(Binary.UNSIGNED, a, b));
        cm1.newStmt(this.vm.newBinary(Binary.ADD, a, b));
        cm1.newStmt(this.vm.newBinary(Binary.SUB, a, b));
        cm1.newStmt(this.vm.newBinary(Binary.MUL, a, b));
        cm1.newStmt(this.vm.newBinary(Binary.DIV, a, b));
        cm1.newStmt(this.vm.newBinary(Binary.MOD, a, b));
        cm1.newStmt(this.vm.newBinary(Binary.EQ, a, b));
        cm1.newStmt(this.vm.newBinary(Binary.NE, a, b));
        cm1.newStmt(this.vm.newBinary(Binary.GT, a, b));
        cm1.newStmt(this.vm.newBinary(Binary.GTE, a, b));
        cm1.newStmt(this.vm.newBinary(Binary.LT, a, b));
        cm1.newStmt(this.vm.newBinary(Binary.LTE, a, b));
        cm1.newStmt(this.vm.newBinary(Binary.IOF, a, b));
        cm1.newStmt(this.vm.newBinary(Binary.CAT, a, b));

        // ternary expressions
        cm1.newStmt(this.vm.newTernary(Ternary.IF, a, b, c));

        // assignment expressions
        cm1.newStmt(this.vm.newAssign(Assign.S, a, b));
        cm1.newStmt(this.vm.newAssign(Binary.BAND, a, b));
        cm1.newStmt(this.vm.newAssign(Binary.BOR, a, b));
        cm1.newStmt(this.vm.newAssign(Binary.XOR, a, b));
        cm1.newStmt(this.vm.newAssign(Binary.LEFT, a, b));
        cm1.newStmt(this.vm.newAssign(Binary.RIGHT, a, b));
        cm1.newStmt(this.vm.newAssign(Binary.UNSIGNED, a, b));
        cm1.newStmt(this.vm.newAssign(Binary.ADD, a, b));
        cm1.newStmt(this.vm.newAssign(Binary.SUB, a, b));
        cm1.newStmt(this.vm.newAssign(Binary.MUL, a, b));
        cm1.newStmt(this.vm.newAssign(Binary.DIV, a, b));
        cm1.newStmt(this.vm.newAssign(Binary.MOD, a, b));
        cm1.newStmt(this.vm.newAssign(Binary.CAT, a, b));

        // array initializer expression
        Expression[] e = new Expression[3];
        e[0] = this.vm.newInt(1);
        e[1] = this.vm.newInt(2);
        e[2] = this.vm.newInt(3);
        ArrayInitializer ai1 = this.vm.newArrayInit(e);
        doc(ai1, Comment.M);

        cm1.newLet(vm.newType("int[]")).addAssign(this.vm.newAssign(vm.newVar("abc"), ai1));

        // special expresssions
        cm1.newStmt(this.vm.newAssign(this.vm.newAccess("this", "a"), vm.newInt(4)));
        cm1.newStmt(this.vm.newAssign(this.vm.newArrayAccess("this", "c").addDim(this.vm.newInt(3)).addDim(this.vm.newInt(3)).addDim(this.vm.newInt(3)), vm.newInt(4)));
        cm1.newStmt(this.vm.newAnon(this.vm.newType("MyInterface")));
        NewArray na1 = this.vm.newArray(this.vm.newType("MyInterface")).addDim(this.vm.newInt(1)).addDim(this.vm.newInt(1)).addDim(null);
        na1.setInitializer(ai1);
        cm1.newStmt(na1);
        cm1.newStmt(this.vm.newInvoke("this", "execute").addArg(this.vm.newChar('a')).addArg(this.vm.newScientific(1, 2, 4)).addArg(this.vm.newChar('a')));

        // test inner class
        InnerClass inner1 = cls.newInnerClass("MyInner");
        inner1.getName();
        // print it
        // System.out.println(unit);

        // test the standard iterator
        for (Member member : cls.getMembers()) {
            member.getAccess();
        }

        // test the type iterator
        for (Constructor constructor : cls.getConstructors()) {
            constructor.getAccess();
        }
        // write it

        unit.encode();

    }

    @org.junit.Test
    public void test2() throws IOException {
        System.setProperty("jenesis.encoder", JenesisJalopyEncoder.class.getName());
        this.vm = VirtualMachine.getVirtualMachine();
        test1();
    }

    @org.junit.Test
    public void test3() throws IOException {
        System.setProperty("jenesis.encoder", BasicCompilationUnitEncoder.class.getName());
        this.vm = VirtualMachine.getVirtualMachine();
        test1();
    }

    @Before
    public void setUp() {
        VirtualMachine.setVirtualMachine(null);
    }

    Comment doc(Codeable x, int type) {
        x.setComment(type, "Carpe Noctem.");
        return x.getComment();
    }
}
