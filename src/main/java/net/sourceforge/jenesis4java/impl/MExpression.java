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

/**
 * Copyright (C) 2008, 2010 Richard van Nieuwenhoven - ritchie [at] gmx [dot] at
 * Copyright (C) 2000, 2001 Paul Cody Johnston - pcj@inxar.org <br>
 * This file is part of Jenesis4java. Jenesis4java is free software: you can
 * redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.<br>
 * Jenesis4java is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.<br>
 * You should have received a copy of the GNU Lesser General Public License
 * along with Jenesis4java. If not, see <http://www.gnu.org/licenses/>.
 */
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jenesis4java.Accessor;
import net.sourceforge.jenesis4java.ArrayAccess;
import net.sourceforge.jenesis4java.ArrayInitializer;
import net.sourceforge.jenesis4java.Assign;
import net.sourceforge.jenesis4java.Binary;
import net.sourceforge.jenesis4java.Blank;
import net.sourceforge.jenesis4java.Cast;
import net.sourceforge.jenesis4java.CodeWriter;
import net.sourceforge.jenesis4java.Expression;
import net.sourceforge.jenesis4java.FieldAccess;
import net.sourceforge.jenesis4java.Freeform;
import net.sourceforge.jenesis4java.IVisitor;
import net.sourceforge.jenesis4java.Invoke;
import net.sourceforge.jenesis4java.NewAnonymousClass;
import net.sourceforge.jenesis4java.NewArray;
import net.sourceforge.jenesis4java.NewClass;
import net.sourceforge.jenesis4java.Ternary;
import net.sourceforge.jenesis4java.Type;
import net.sourceforge.jenesis4java.Unary;
import net.sourceforge.jenesis4java.Variable;
import net.sourceforge.jenesis4java.impl.util.ListTypeSelector;
import net.sourceforge.jenesis4java.impl.util.VisitorUtils;

/**
 * Standard <code>Expression</code> implementations.
 */
public abstract class MExpression extends MVM.MCodeable implements Expression {

    // ===============================================================
    // ACCESSOR EXPRESSION
    // ===============================================================
    static class MAccessor extends MExpression implements Accessor {

        String name;

        String qual;

        Expression qualExpression;

        Expression nameExpression;

        public MAccessor(MVM vm, String qual, String name) {
            super(vm);
            this.qual = qual;
            qualExpression = null;
            this.name = name;
            nameExpression = null;
        }

        MAccessor(MVM vm) {
            super(vm);
        }

        MAccessor(MVM vm, Expression qual, String name) {
            super(vm);
            this.qual = null;
            qualExpression = qual;
            this.name = name;
        }

        MAccessor(MVM vm, Expression qual, Expression name) {
            super(vm);
            this.qual = null;
            qualExpression = qual;
            nameExpression = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Expression getQualExpression() {
            return qualExpression;
        }

        @Override
        public String getQualifier() {
            return qual;
        }

        @Override
        public Type getType() {
            return null;
        }

        @Override
        public MAccessor setName(String name) {
            this.name = name;
            return this;
        }

        @Override
        public MAccessor setQualifier(String qual) {
            this.qual = qual;
            return this;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);
            if (qual != null) {
                out.write(qual).write('.');
            } else if (qualExpression != null) {
                out.write(qualExpression).write('.');
            }
            if (name != null) {
                out.write(name);
            } else if (nameExpression != null) {
                out.write(nameExpression).write('.');
            }
            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            qualExpression = VisitorUtils.visit(qualExpression, this, visitor);
            nameExpression = VisitorUtils.visit(nameExpression, this, visitor);
        }
    }

    // ===============================================================
    // ARRAY ACCESS EXPRESSION
    // ===============================================================
    static class MArrayAccess extends MAccessor implements ArrayAccess {

        List<Expression> dimensions;

        MArrayAccess(MVM vm) {
            super(vm);
            dimensions = new ArrayList<Expression>();
        }

        MArrayAccess(MVM vm, Expression qual, String name) {
            super(vm, qual, name);
            dimensions = new ArrayList<Expression>();
        }

        MArrayAccess(MVM vm, String qual, String name) {
            super(vm, qual, name);
            dimensions = new ArrayList<Expression>();
        }

        @Override
        public ArrayAccess addDim(Expression expr) {
            dimensions.add(expr);
            return this;
        }

        @Override
        public List<Expression> getDims() {
            return ListTypeSelector.select(dimensions);
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);

            for (int i = 0; i < dimensions.size(); i++) {
                out.write('[').write(dimensions.get(i)).write(']');
            }

            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            VisitorUtils.visit(dimensions, this, visitor);
        }
    }

    // ===============================================================
    // ARRAY INITIALIZATION EXPRESSION
    // ===============================================================
    static class MArrayInitializer extends MExpression implements ArrayInitializer {

        Object o;

        MArrayInitializer(MVM vm, Object o) {
            super(vm);
            this.o = o;
        }

        @Override
        public Object getArgs() {
            return o;
        }

        @Override
        public Type getType() {
            return null;
        }

        @Override
        public MArrayInitializer setArgs(Object o) {
            this.o = o;
            return this;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);

            // normal case
            if (o == null) {
                out.write("{}");
            } else if (o instanceof Object[]) {
                Object[] ao = (Object[]) o;
                if (ao.length == 0) {
                    out.write("{}");
                } else {
                    writeElements(ao, out);
                }

                // the exceptional case where the primary object
                // is NOT an array, and we simply write the sole
                // element
            } else if (o instanceof Expression) {
                out.write("{ ").write(o).write(" }");
            }

            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            o = VisitorUtils.visit(o, this, visitor);
        }

        void writeElement(Object o, CodeWriter out, boolean isNotFirst) {
            if (o instanceof Object[]) {
                if (isNotFirst) {
                    out.write(", ");
                }
                writeElements((Object[]) o, out);
            } else if (o instanceof Expression) {
                if (isNotFirst) {
                    out.write(", ");
                }
                out.write(o);
            }
        }

        void writeElements(Object[] ao, CodeWriter out) {
            out.write('{');

            // write each element, sending true if this
            // is not the first element we are writing
            for (int i = 0; i < ao.length; i++) {
                writeElement(ao[i], out, i > 0);
            }

            out.write('}');
        }
    }

    // ===============================================================
    // ASSIGNMENT EXPRESSION
    // ===============================================================
    static class MAssign extends MBinary implements Assign {

        MAssign(MVM vm, int type) {
            super(vm, type);
        }

        MAssign(MVM vm, int type, Variable l, Expression r) {
            super(vm, type, l, r);
        }

        @Override
        public Variable getVariable() {
            return (Variable) super.getLValue();
        }

        @Override
        public MAssign setVariable(Variable v) {
            super.setLValue(v);
            return this;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            out.queue(comment);

            switch (type) {
                case Assign.S:
                    out.write(l).write(" = ").write(r);
                    break;
                case Binary.BAND:
                    out.write(l).write(" &= ").write(r);
                    break;
                case Binary.BOR:
                    out.write(l).write(" |= ").write(r);
                    break;
                case Binary.XOR:
                    out.write(l).write(" ^= ").write(r);
                    break;
                case Binary.LEFT:
                    out.write(l).write(" <<= ").write(r);
                    break;
                case Binary.RIGHT:
                    out.write(l).write(" >>= ").write(r);
                    break;
                case Binary.UNSIGNED:
                    out.write(l).write(" >>>= ").write(r);
                    break;
                case Binary.SUB:
                    out.write(l).write(" -= ").write(r);
                    break;
                case Binary.MUL:
                    out.write(l).write(" *= ").write(r);
                    break;
                case Binary.DIV:
                    out.write(l).write(" /= ").write(r);
                    break;
                case Binary.MOD:
                    out.write(l).write(" %= ").write(r);
                    break;
                case Binary.ADD:
                case Binary.CAT:
                    out.write(l).write(" += ").write(r);
                    break;
                default:
                    throw new RuntimeException("Please use a constant from the Assign interface when making new Assignment expressions.");
            }
            return out;
        }

        @Override
        public int type() {
            return type;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    // ===============================================================
    // BINARY EXPRESSION
    // ===============================================================
    static class MBinary extends MExpression implements Binary {

        int type;

        Expression l;

        Expression r;

        MBinary(MVM vm, int type) {
            super(vm);
            this.type = type;
        }

        MBinary(MVM vm, int type, Expression l, Expression r) {
            super(vm);
            this.type = type;
            this.l = l;
            this.r = r;
        }

        @Override
        public Expression getLValue() {
            return l;
        }

        @Override
        public Expression getRValue() {
            return r;
        }

        @Override
        public Type getType() {
            return l.getType();
        }

        @Override
        public MBinary setLValue(Expression l) {
            this.l = l;
            return this;
        }

        @Override
        public MBinary setRValue(Expression r) {
            this.r = r;
            return this;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);

            switch (type) {
                case Binary.LAND:
                    out.write(l).write(" && ").write(r);
                    break;
                case Binary.LOR:
                    out.write(l).write(" || ").write(r);
                    break;
                case Binary.BAND:
                    out.write(l).write(" & ").write(r);
                    break;
                case Binary.BOR:
                    out.write(l).write(" | ").write(r);
                    break;
                case Binary.XOR:
                    out.write(l).write(" ^ ").write(r);
                    break;
                case Binary.LEFT:
                    out.write(l).write(" << ").write(r);
                    break;
                case Binary.RIGHT:
                    out.write(l).write(" >> ").write(r);
                    break;
                case Binary.UNSIGNED:
                    out.write(l).write(" >>> ").write(r);
                    break;
                case Binary.SUB:
                    out.write(l).write(" - ").write(r);
                    break;
                case Binary.MUL:
                    out.write(l).write(" * ").write(r);
                    break;
                case Binary.DIV:
                    out.write(l).write(" / ").write(r);
                    break;
                case Binary.MOD:
                    out.write(l).write(" % ").write(r);
                    break;
                case Binary.EQ:
                    out.write(l).write(" == ").write(r);
                    break;
                case Binary.NE:
                    out.write(l).write(" != ").write(r);
                    break;
                case Binary.GT:
                    out.write(l).write(" > ").write(r);
                    break;
                case Binary.GTE:
                    out.write(l).write(" >= ").write(r);
                    break;
                case Binary.LT:
                    out.write(l).write(" < ").write(r);
                    break;
                case Binary.LTE:
                    out.write(l).write(" <= ").write(r);
                    break;
                case Binary.IOF:
                    out.write(l).write(" instanceof ").write(r);
                    break;
                case Binary.ASSIGN:
                    out.write(l).write(" = ").write(r);
                    break;
                case Binary.ADD:
                case Binary.CAT:
                    out.write(l).write(" + ").write(r);
                    break;
                default:
                    throw new RuntimeException("Please use a constant from the Binary interface when making new binary expressions.");
            }
            return out;
        }

        @Override
        public int type() {
            return type;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            l = VisitorUtils.visit(l, this, visitor);
            r = VisitorUtils.visit(r, this, visitor);
        }
    }

    // ===============================================================
    // BLANK EXPRESSION
    // ===============================================================
    static class MBlank extends MExpression implements Blank {

        MBlank(MVM vm) {
            super(vm);
        }

        @Override
        public Type getType() {
            return null;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    // ===============================================================
    // CAST EXPRESSION
    // ===============================================================
    static class MCast extends MExpression implements Cast {

        Type type;

        Expression val;

        MCast(MVM vm, Type type, Expression val) {
            super(vm);
            this.type = type;
            this.val = val;
        }

        @Override
        public Expression getExpression() {
            return val;
        }

        @Override
        public Type getType() {
            return type;
        }

        @Override
        public MCast setExpression(Expression val) {
            this.val = val;
            return this;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);
            if (type == null) {
                out.write('(').write(val).write(')');
            } else {
                out.write('(').write('(').write(type).write(')').write(val).write(')');
            }
            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            type = VisitorUtils.visit(type, this, visitor);
            val = VisitorUtils.visit(val, this, visitor);
        }
    }

    // ===============================================================
    // FIELD ACCESS EXPRESSION
    // ===============================================================
    static class MFieldAccess extends MAccessor implements FieldAccess {

        MFieldAccess(MVM vm) {
            super(vm);
        }

        MFieldAccess(MVM vm, Expression qual, String name) {
            super(vm, qual, name);
        }

        MFieldAccess(MVM vm, String qual, String name) {
            super(vm, qual, name);
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    // ===============================================================
    // FREEFORM EXPRESSION
    // ===============================================================
    static class MFreeform extends MExpression implements Freeform {

        // the actual writer
        java.io.StringWriter buf;

        java.io.PrintWriter out;

        int colNo;

        int lineNo;

        int indentNo;

        boolean isLineNew;

        MFreeform(MVM vm) {
            super(vm);
            init();
        }

        MFreeform(MVM vm, String code) {
            this(vm);
            write(code);
        }

        @Override
        public Freeform dedentLine() {
            indentNo--;
            newLine();
            return this;
        }

        @Override
        public String getCode() {
            return buf.toString();
        }

        public int getColumnNumber() {
            return colNo;
        }

        @Override
        public int getIndentNumber() {
            return indentNo;
        }

        public int getLineNumber() {
            return lineNo;
        }

        @Override
        public Type getType() {
            return null;
        }

        @Override
        public Freeform indentLine() {
            indentNo++;
            newLine();
            return this;
        }

        @Override
        public boolean isLineNew() {
            return isLineNew;
        }

        @Override
        public Freeform newLine() {
            out.println();
            writeIndent();
            colNo = 0;
            lineNo++;
            isLineNew = true;
            return this;
        }

        @Override
        public Freeform resetLine() {
            indentNo = 0;
            newLine();
            return this;
        }

        @Override
        public MFreeform setCode(String code) {
            init();
            buf.write(code);
            return this;
        }

        @Override
        public Freeform space() {
            out.print(' ');
            colNo++;
            isLineNew = false;
            return this;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);
            out.write(buf.toString());
            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }

        @Override
        public Freeform write(boolean b) {
            // print it
            out.print(b);
            // add if 4:'true' or 5:'false'
            colNo += b ? 4 : 5;
            isLineNew = false;
            return this;
        }

        @Override
        public Freeform write(char c) {
            // print the char
            out.print(c);
            // add one
            colNo++;
            isLineNew = false;
            return this;
        }

        @Override
        public Freeform write(char[] chars) {

            if (chars != null) {
                // print the chars;
                out.print(chars);
                // add
                colNo += chars.length;
                isLineNew = false;
            }
            return this;
        }

        @Override
        public Freeform write(double d) {
            out.print(d);
            colNo += Double.toString(d).length();
            isLineNew = false;
            return this;
        }

        @Override
        public Freeform write(float f) {
            out.print(f);
            colNo += Float.toString(f).length();
            isLineNew = false;
            return this;
        }

        @Override
        public Freeform write(int i) {
            out.print(i);
            colNo += Integer.toString(i).length();
            isLineNew = false;
            return this;
        }

        @Override
        public Freeform write(Object o) {
            if (o != null) {
                out.print(o);
                colNo += o.toString().length();
                isLineNew = false;
            }
            return this;
        }

        @Override
        public Freeform write(Object[] ao) {
            if (ao != null) {
                for (Object element : ao) {
                    write(element);
                }
            }
            return this;
        }

        @Override
        public Freeform write(String s) {
            if (s != null) {
                out.print(s);
                colNo += s.length();
                isLineNew = false;
            }

            return this;
        }

        private void writeIndent() {
            int n = indentNo;
            while (n-- > 0) {
                out.print('\t');
            }
        }

        void init() {
            buf = new java.io.StringWriter();
            out = new java.io.PrintWriter(buf);
        }
    }

    // ===============================================================
    // METHOD INVOCATION EXPRESSION
    // ===============================================================
    static class MInvoke extends MNary implements Invoke {

        MInvoke(MVM vm, Expression qual, String name) {
            super(vm);
            this.qual = null;
            qualExpression = qual;
            this.name = name;
        }

        MInvoke(MVM vm, String qual, String name) {
            super(vm);
            this.qual = qual;
            qualExpression = null;
            this.name = name;
        }

        @Override
        public Invoke addArg(boolean value) {
            return addArg(value ? vm.newTrue() : vm.newFalse());
        }

        @Override
        public Invoke addArg(double value) {
            return addArg(vm.newDouble(value));
        }

        @Override
        public Invoke addArg(Expression arg) {
            ve.add(arg);
            return this;
        }

        @Override
        public Invoke addArg(float value) {
            return addArg(vm.newFloat(value));
        }

        @Override
        public Invoke addArg(int value) {
            return addArg(vm.newInt(value));
        }

        @Override
        public Invoke addArg(long value) {
            return addArg(vm.newLong(value));
        }

        @Override
        public Invoke addArg(String value) {
            return addArg(vm.newString(value));
        }

        @Override
        public Invoke addVarriableArg(String variableName) {
            return addArg(vm.newVar(variableName));
        }

        @Override
        public void removeArg(int index) {
            ve.remove(index);
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);
            out.write('(');
            for (int i = 0; i < ve.size(); i++) {
                if (i > 0) {
                    out.write(", ");
                }
                out.write(ve.get(i));
            }
            out.write(')');

            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    // ===============================================================
    // NARY EXPRESSION
    // ===============================================================
    abstract static class MNary extends MAccessor {

        List<Expression> ve;

        MNary(MVM vm) {
            super(vm);
            ve = new ArrayList<Expression>();
        }

        public List<Expression> getArgs() {
            return ListTypeSelector.select(ve, Expression.class);
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            VisitorUtils.visit(ve, this, visitor);
        }
    }

    // ===============================================================
    // ANONYMOUS CLASS CREATION EXPRESSION
    // ===============================================================
    static class MNewAnonymousClass extends MDeclaration.MClassDeclaration implements NewAnonymousClass {

        Type type;

        List<Expression> ve;

        String qual;

        MNewAnonymousClass(MVM vm, Type type) {
            super(vm, null);
            ve = new ArrayList<Expression>();
            this.type = type;
        }

        @Override
        public NewClass addArg(Expression arg) {
            ve.add(arg);
            return this;
        }

        @Override
        public List<Expression> getArgs() {
            return ListTypeSelector.select(ve, Expression.class);
        }

        @Override
        public String getQualifier() {
            return qual;
        }

        @Override
        public Type getType() {
            return type;
        }

        @Override
        public MNewAnonymousClass setQualifier(String qual) {
            this.qual = qual;
            return this;
        }

        @Override
        public MNewAnonymousClass setType(Type type) {
            this.type = type;
            return this;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            if (comment != null) {
                out.queue(comment);
            }

            out.write("new ").write(type).write('(');

            for (int i = 0; i < ve.size(); i++) {
                if (i > 0) {
                    out.write(", ");
                }
                out.write(ve.get(i));
            }
            out.write(')');

            writeBlock(out, vm.getStyle("anonymous-class"));

            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            type = VisitorUtils.visit(type, this, visitor);
            VisitorUtils.visit(ve, this, visitor);
        }
    }

    // ===============================================================
    // ARRAY INSTANCE CREATION EXPRESSION
    // ===============================================================
    static class MNewArray extends MNary implements NewArray {

        Type type;

        List<Expression> dimensions;

        ArrayInitializer ai;

        MNewArray(MVM vm, Type type) {
            super(vm);
            this.type = type;
            dimensions = new ArrayList<Expression>(); // dimensions
        }

        @Override
        public NewArray addDim() {
            dimensions.add(new MBlank(vm));
            return this;
        }

        @Override
        public NewArray addDim(Expression e) {
            dimensions.add(e);
            return this;
        }

        @Override
        public List<Expression> getDims() {
            return ListTypeSelector.select(dimensions, Expression.class);
        }

        @Override
        public ArrayInitializer getInitializer() {
            return ai;
        }

        @Override
        public Type getType() {
            return type;
        }

        @Override
        public MNewArray setInitializer(ArrayInitializer ai) {
            this.ai = ai;
            return this;
        }

        @Override
        public MNewArray setType(Type type) {
            this.type = type;
            return this;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            out.write("new ").write(type);
            for (Expression dim : dimensions) {
                out.write('[').write(dim).write(']');
            }
            out.write(ai);
            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            type = VisitorUtils.visit(type, this, visitor);
            VisitorUtils.visit(dimensions, this, visitor);
            ai = VisitorUtils.visit(ai, this, visitor);
        }
    }

    // ===============================================================
    // CLASS INSTANCE CREATION EXPRESSION
    // ===============================================================
    static class MNewClass extends MNary implements NewClass {

        Type type;

        MNewClass(MVM vm, Type type) {
            super(vm);
            this.type = type;
        }

        @Override
        public NewClass addArg(Expression arg) {
            ve.add(arg);
            return this;
        }

        @Override
        public Type getType() {
            return type;
        }

        @Override
        public MNewClass setQualifier(String qual) {
            super.setQualifier(qual);
            return this;
        }

        @Override
        public MNewClass setType(Type type) {
            this.type = type;
            return this;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);

            out.write("new ").write(type).write('(');
            for (int i = 0; i < ve.size(); i++) {
                if (i > 0) {
                    out.write(", ");
                }
                out.write(ve.get(i));
            }
            out.write(')');

            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            type = VisitorUtils.visit(type, this, visitor);
        }
    }

    // ===============================================================
    // TERNARY EXPRESSION
    // ===============================================================
    static class MTernary extends MExpression implements Ternary {

        int type;

        Expression one;

        Expression two;

        Expression three;

        MTernary(MVM vm, int type) {
            super(vm);
            this.type = type;
        }

        MTernary(MVM vm, int type, Expression one, Expression two, Expression three) {
            super(vm);
            this.type = type;
            this.one = one;
            this.two = two;
            this.three = three;
        }

        @Override
        public Type getType() {
            return two.getType();
        }

        @Override
        public Expression getValue1() {
            return one;
        }

        @Override
        public Expression getValue2() {
            return two;
        }

        @Override
        public Expression getValue3() {
            return three;
        }

        @Override
        public MTernary setValue1(Expression one) {
            this.one = one;
            return this;
        }

        @Override
        public MTernary setValue2(Expression two) {
            this.two = two;
            return this;
        }

        @Override
        public MTernary setValue3(Expression three) {
            this.three = three;
            return this;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);
            out.write(one).write(" ? ").write(two).write(" : ").write(three);
            return out;
        }

        @Override
        public int type() {
            return type;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            one = VisitorUtils.visit(one, this, visitor);
            two = VisitorUtils.visit(two, this, visitor);
            three = VisitorUtils.visit(three, this, visitor);
        }
    }

    // ===============================================================
    // VARIABLE EXPRESSION
    // ===============================================================
    static class MTypeVariable extends MExpression implements Variable {

        Type type;

        MTypeVariable(MVM vm) {
            super(vm);
        }

        MTypeVariable(MVM vm, Type type) {
            super(vm);
            this.type = type;
        }

        @Override
        public String getName() {
            return type.toString();
        }

        @Override
        public Type getType() {
            return type;
        }

        @Override
        public Variable setName(String typeName) {
            type = vm.newType(typeName);
            return this;
        }

        public MTypeVariable setName(Type type) {
            this.type = type;
            return this;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);
            out.write(type);
            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            type = VisitorUtils.visit(type, this, visitor);
        }
    }

    // ===============================================================
    // UNARY EXPRESSION
    // ===============================================================
    static class MUnary extends MExpression implements Unary {

        int type;

        Expression val;

        MUnary(MVM vm, int type) {
            super(vm);
            this.type = type;
        }

        MUnary(MVM vm, int type, Expression val) {
            super(vm);
            this.type = type;
            this.val = val;
        }

        @Override
        public Type getType() {
            return val.getType();
        }

        @Override
        public Expression getValue() {
            return val;
        }

        @Override
        public MUnary setValue(Expression val) {
            this.val = val;
            return this;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);

            switch (type) {
                case Unary.GROUP:
                    out.write('(').write(val).write(')');
                    break;
                case Unary.NOT:
                    out.write('!').write(val);
                    break;
                case Unary.BITWISE_NOT:
                    out.write('~').write(val);
                    break;
                case Unary.POS:
                    out.write('+').write(val);
                    break;
                case Unary.NEG:
                    out.write('-').write(val);
                    break;
                case Unary.AI:
                    out.write("++").write(val);
                    break;
                case Unary.PI:
                    out.write(val).write("++");
                    break;
                case Unary.AD:
                    out.write("--").write(val);
                    break;
                case Unary.PD:
                    out.write(val).write("--");
                    break;
                default:
                    throw new RuntimeException("Please use a constant from the Unary interface when making new unary expressions.");
            }

            return out;
        }

        @Override
        public int type() {
            return type;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            val = VisitorUtils.visit(val, this, visitor);
        }
    }

    // ===============================================================
    // VARIABLE EXPRESSION
    // ===============================================================
    static class MVariable extends MExpression implements Variable {

        String name;

        MVariable(MVM vm) {
            super(vm);
        }

        MVariable(MVM vm, String name) {
            super(vm);
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public Type getType() {
            return null;
        }

        @Override
        public MVariable setName(String name) {
            this.name = name;
            return this;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);
            out.write(name);
            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    MExpression(MVM vm) {
        super(vm);
    }

    @Override
    public CodeWriter toCode(CodeWriter out) {
        out.queue(comment);
        return out;
    }

    @Override
    public void visit(IVisitor visitor) {
        super.visit(visitor);
    }
}
