package net.sourceforge.jenesis4java.impl;

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
            this.qualExpression = null;
            this.name = name;
            this.nameExpression = null;
        }

        MAccessor(MVM vm) {
            super(vm);
        }

        MAccessor(MVM vm, Expression qual, String name) {
            super(vm);
            this.qual = null;
            this.qualExpression = qual;
            this.name = name;
        }

        MAccessor(MVM vm, Expression qual, Expression name) {
            super(vm);
            this.qual = null;
            this.qualExpression = qual;
            this.nameExpression = name;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public Expression getQualExpression() {
            return this.qualExpression;
        }

        @Override
        public String getQualifier() {
            return this.qual;
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
            if (this.qual != null) {
                out.write(this.qual).write('.');
            } else if (this.qualExpression != null) {
                out.write(this.qualExpression).write('.');
            }
            if (name != null) {
                out.write(this.name);
            } else if (nameExpression != null) {
                out.write(this.nameExpression).write('.');
            }
            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            this.qualExpression = VisitorUtils.visit(this.qualExpression, this, visitor);
            this.nameExpression = VisitorUtils.visit(this.nameExpression, this, visitor);
        }
    }

    // ===============================================================
    // ARRAY ACCESS EXPRESSION
    // ===============================================================
    static class MArrayAccess extends MAccessor implements ArrayAccess {

        List<Expression> dimensions;

        MArrayAccess(MVM vm) {
            super(vm);
            this.dimensions = new ArrayList<Expression>();
        }

        MArrayAccess(MVM vm, Expression qual, String name) {
            super(vm, qual, name);
            this.dimensions = new ArrayList<Expression>();
        }

        MArrayAccess(MVM vm, String qual, String name) {
            super(vm, qual, name);
            this.dimensions = new ArrayList<Expression>();
        }

        @Override
        public ArrayAccess addDim(Expression expr) {
            this.dimensions.add(expr);
            return this;
        }

        @Override
        public List<Expression> getDims() {
            return ListTypeSelector.select(this.dimensions);
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);

            for (int i = 0; i < this.dimensions.size(); i++) {
                out.write('[').write(this.dimensions.get(i)).write(']');
            }

            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            VisitorUtils.visit(this.dimensions, this, visitor);
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
            return this.o;
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
            if (this.o == null) {
                out.write("{}");
            } else if (this.o instanceof Object[]) {
                Object[] ao = (Object[]) this.o;
                if (ao.length == 0) {
                    out.write("{}");
                } else {
                    writeElements(ao, out);
                }

                // the exceptional case where the primary object
                // is NOT an array, and we simply write the sole
                // element
            } else if (this.o instanceof Expression) {
                out.write("{ ").write(this.o).write(" }");
            }

            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            this.o = VisitorUtils.visit(this.o, this, visitor);
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
            out.queue(this.comment);

            switch (this.type) {
                case Assign.S:
                    out.write(this.l).write(" = ").write(this.r);
                    break;
                case Binary.BAND:
                    out.write(this.l).write(" &= ").write(this.r);
                    break;
                case Binary.BOR:
                    out.write(this.l).write(" |= ").write(this.r);
                    break;
                case Binary.XOR:
                    out.write(this.l).write(" ^= ").write(this.r);
                    break;
                case Binary.LEFT:
                    out.write(this.l).write(" <<= ").write(this.r);
                    break;
                case Binary.RIGHT:
                    out.write(this.l).write(" >>= ").write(this.r);
                    break;
                case Binary.UNSIGNED:
                    out.write(this.l).write(" >>>= ").write(this.r);
                    break;
                case Binary.SUB:
                    out.write(this.l).write(" -= ").write(this.r);
                    break;
                case Binary.MUL:
                    out.write(this.l).write(" *= ").write(this.r);
                    break;
                case Binary.DIV:
                    out.write(this.l).write(" /= ").write(this.r);
                    break;
                case Binary.MOD:
                    out.write(this.l).write(" %= ").write(this.r);
                    break;
                case Binary.ADD:
                case Binary.CAT:
                    out.write(this.l).write(" += ").write(this.r);
                    break;
                default:
                    throw new RuntimeException("Please use a constant from the Assign interface when making new Assignment expressions.");
            }
            return out;
        }

        @Override
        public int type() {
            return this.type;
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
            return this.l;
        }

        @Override
        public Expression getRValue() {
            return this.r;
        }

        @Override
        public Type getType() {
            return this.l.getType();
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

            switch (this.type) {
                case Binary.LAND:
                    out.write(this.l).write(" && ").write(this.r);
                    break;
                case Binary.LOR:
                    out.write(this.l).write(" || ").write(this.r);
                    break;
                case Binary.BAND:
                    out.write(this.l).write(" & ").write(this.r);
                    break;
                case Binary.BOR:
                    out.write(this.l).write(" | ").write(this.r);
                    break;
                case Binary.XOR:
                    out.write(this.l).write(" ^ ").write(this.r);
                    break;
                case Binary.LEFT:
                    out.write(this.l).write(" << ").write(this.r);
                    break;
                case Binary.RIGHT:
                    out.write(this.l).write(" >> ").write(this.r);
                    break;
                case Binary.UNSIGNED:
                    out.write(this.l).write(" >>> ").write(this.r);
                    break;
                case Binary.SUB:
                    out.write(this.l).write(" - ").write(this.r);
                    break;
                case Binary.MUL:
                    out.write(this.l).write(" * ").write(this.r);
                    break;
                case Binary.DIV:
                    out.write(this.l).write(" / ").write(this.r);
                    break;
                case Binary.MOD:
                    out.write(this.l).write(" % ").write(this.r);
                    break;
                case Binary.EQ:
                    out.write(this.l).write(" == ").write(this.r);
                    break;
                case Binary.NE:
                    out.write(this.l).write(" != ").write(this.r);
                    break;
                case Binary.GT:
                    out.write(this.l).write(" > ").write(this.r);
                    break;
                case Binary.GTE:
                    out.write(this.l).write(" >= ").write(this.r);
                    break;
                case Binary.LT:
                    out.write(this.l).write(" < ").write(this.r);
                    break;
                case Binary.LTE:
                    out.write(this.l).write(" <= ").write(this.r);
                    break;
                case Binary.IOF:
                    out.write(this.l).write(" instanceof ").write(this.r);
                    break;
                case Binary.ASSIGN:
                    out.write(this.l).write(" = ").write(this.r);
                    break;
                case Binary.ADD:
                case Binary.CAT:
                    out.write(this.l).write(" + ").write(this.r);
                    break;
                default:
                    throw new RuntimeException("Please use a constant from the Binary interface when making new binary expressions.");
            }
            return out;
        }

        @Override
        public int type() {
            return this.type;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            this.l = VisitorUtils.visit(this.l, this, visitor);
            this.r = VisitorUtils.visit(this.r, this, visitor);
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
            return this.val;
        }

        @Override
        public Type getType() {
            return this.type;
        }

        @Override
        public MCast setExpression(Expression val) {
            this.val = val;
            return this;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);
            if (this.type == null) {
                out.write('(').write(this.val).write(')');
            } else {
                out.write('(').write('(').write(this.type).write(')').write(this.val).write(')');
            }
            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            this.type = VisitorUtils.visit(this.type, this, visitor);
            this.val = VisitorUtils.visit(this.val, this, visitor);
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
            this.indentNo--;
            newLine();
            return this;
        }

        @Override
        public String getCode() {
            return this.buf.toString();
        }

        public int getColumnNumber() {
            return this.colNo;
        }

        @Override
        public int getIndentNumber() {
            return this.indentNo;
        }

        public int getLineNumber() {
            return this.lineNo;
        }

        @Override
        public Type getType() {
            return null;
        }

        @Override
        public Freeform indentLine() {
            this.indentNo++;
            newLine();
            return this;
        }

        @Override
        public boolean isLineNew() {
            return this.isLineNew;
        }

        @Override
        public Freeform newLine() {
            this.out.println();
            writeIndent();
            this.colNo = 0;
            this.lineNo++;
            this.isLineNew = true;
            return this;
        }

        @Override
        public Freeform resetLine() {
            this.indentNo = 0;
            newLine();
            return this;
        }

        @Override
        public MFreeform setCode(String code) {
            init();
            this.buf.write(code);
            return this;
        }

        @Override
        public Freeform space() {
            this.out.print(' ');
            this.colNo++;
            this.isLineNew = false;
            return this;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);
            out.write(this.buf.toString());
            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }

        @Override
        public Freeform write(boolean b) {
            // print it
            this.out.print(b);
            // add if 4:'true' or 5:'false'
            this.colNo += b ? 4 : 5;
            this.isLineNew = false;
            return this;
        }

        @Override
        public Freeform write(char c) {
            // print the char
            this.out.print(c);
            // add one
            this.colNo++;
            this.isLineNew = false;
            return this;
        }

        @Override
        public Freeform write(char[] chars) {

            if (chars != null) {
                // print the chars;
                this.out.print(chars);
                // add
                this.colNo += chars.length;
                this.isLineNew = false;
            }
            return this;
        }

        @Override
        public Freeform write(double d) {
            this.out.print(d);
            this.colNo += Double.toString(d).length();
            this.isLineNew = false;
            return this;
        }

        @Override
        public Freeform write(float f) {
            this.out.print(f);
            this.colNo += Float.toString(f).length();
            this.isLineNew = false;
            return this;
        }

        @Override
        public Freeform write(int i) {
            this.out.print(i);
            this.colNo += Integer.toString(i).length();
            this.isLineNew = false;
            return this;
        }

        @Override
        public Freeform write(Object o) {
            if (o != null) {
                this.out.print(o);
                this.colNo += o.toString().length();
                this.isLineNew = false;
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
                this.out.print(s);
                this.colNo += s.length();
                this.isLineNew = false;
            }

            return this;
        }

        private void writeIndent() {
            int n = this.indentNo;
            while (n-- > 0) {
                this.out.print('\t');
            }
        }

        void init() {
            this.buf = new java.io.StringWriter();
            this.out = new java.io.PrintWriter(this.buf);
        }
    }

    // ===============================================================
    // METHOD INVOCATION EXPRESSION
    // ===============================================================
    static class MInvoke extends MNary implements Invoke {

        MInvoke(MVM vm, Expression qual, String name) {
            super(vm);
            this.qual = null;
            this.qualExpression = qual;
            this.name = name;
        }

        MInvoke(MVM vm, String qual, String name) {
            super(vm);
            this.qual = qual;
            this.qualExpression = null;
            this.name = name;
        }

        @Override
        public Invoke addArg(boolean value) {
            return addArg(value ? this.vm.newTrue() : this.vm.newFalse());
        }

        @Override
        public Invoke addArg(double value) {
            return addArg(this.vm.newDouble(value));
        }

        @Override
        public Invoke addArg(Expression arg) {
            this.ve.add(arg);
            return this;
        }

        @Override
        public Invoke addArg(float value) {
            return addArg(this.vm.newFloat(value));
        }

        @Override
        public Invoke addArg(int value) {
            return addArg(this.vm.newInt(value));
        }

        @Override
        public Invoke addArg(long value) {
            return addArg(this.vm.newLong(value));
        }

        @Override
        public Invoke addArg(String value) {
            return addArg(this.vm.newString(value));
        }

        @Override
        public Invoke addVarriableArg(String variableName) {
            return addArg(this.vm.newVar(variableName));
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);
            out.write('(');
            for (int i = 0; i < this.ve.size(); i++) {
                if (i > 0) {
                    out.write(", ");
                }
                out.write(this.ve.get(i));
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
            this.ve = new ArrayList<Expression>();
        }

        public List<Expression> getArgs() {
            return ListTypeSelector.select(this.ve, Expression.class);
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            VisitorUtils.visit(this.ve, this, visitor);
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
            this.ve = new ArrayList<Expression>();
            this.type = type;
        }

        @Override
        public NewClass addArg(Expression arg) {
            this.ve.add(arg);
            return this;
        }

        @Override
        public List<Expression> getArgs() {
            return ListTypeSelector.select(this.ve, Expression.class);
        }

        @Override
        public String getQualifier() {
            return this.qual;
        }

        @Override
        public Type getType() {
            return this.type;
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
            if (this.comment != null) {
                out.queue(this.comment);
            }

            out.write("new ").write(this.type).write('(');

            for (int i = 0; i < this.ve.size(); i++) {
                if (i > 0) {
                    out.write(", ");
                }
                out.write(this.ve.get(i));
            }
            out.write(')');

            writeBlock(out, this.vm.getStyle("anonymous-class"));

            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            this.type = VisitorUtils.visit(this.type, this, visitor);
            VisitorUtils.visit(this.ve, this, visitor);
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
            this.dimensions = new ArrayList<Expression>(); // dimensions
        }

        @Override
        public NewArray addDim() {
            this.dimensions.add(new MBlank(this.vm));
            return this;
        }

        @Override
        public NewArray addDim(Expression e) {
            this.dimensions.add(e);
            return this;
        }

        @Override
        public List<Expression> getDims() {
            return ListTypeSelector.select(this.dimensions, Expression.class);
        }

        @Override
        public ArrayInitializer getInitializer() {
            return this.ai;
        }

        @Override
        public Type getType() {
            return this.type;
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
            out.write("new ").write(this.type);
            for (Expression dim : this.dimensions) {
                out.write('[').write(dim).write(']');
            }
            out.write(this.ai);
            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            this.type = VisitorUtils.visit(this.type, this, visitor);
            VisitorUtils.visit(this.dimensions, this, visitor);
            this.ai = VisitorUtils.visit(this.ai, this, visitor);
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
            this.ve.add(arg);
            return this;
        }

        @Override
        public Type getType() {
            return this.type;
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

            out.write("new ").write(this.type).write('(');
            for (int i = 0; i < this.ve.size(); i++) {
                if (i > 0) {
                    out.write(", ");
                }
                out.write(this.ve.get(i));
            }
            out.write(')');

            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            this.type = VisitorUtils.visit(this.type, this, visitor);
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
            return this.two.getType();
        }

        @Override
        public Expression getValue1() {
            return this.one;
        }

        @Override
        public Expression getValue2() {
            return this.two;
        }

        @Override
        public Expression getValue3() {
            return this.three;
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
            out.write(this.one).write(" ? ").write(this.two).write(" : ").write(this.three);
            return out;
        }

        @Override
        public int type() {
            return this.type;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            this.one = VisitorUtils.visit(this.one, this, visitor);
            this.two = VisitorUtils.visit(this.two, this, visitor);
            this.three = VisitorUtils.visit(this.three, this, visitor);
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
            return this.type.toString();
        }

        @Override
        public Type getType() {
            return this.type;
        }

        @Override
        public Variable setName(String typeName) {
            this.type = this.vm.newType(typeName);
            return this;
        }

        public MTypeVariable setName(Type type) {
            this.type = type;
            return this;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);
            out.write(this.type);
            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            this.type = VisitorUtils.visit(this.type, this, visitor);
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
            return this.val.getType();
        }

        @Override
        public Expression getValue() {
            return this.val;
        }

        @Override
        public MUnary setValue(Expression val) {
            this.val = val;
            return this;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);

            switch (this.type) {
                case Unary.GROUP:
                    out.write('(').write(this.val).write(')');
                    break;
                case Unary.NOT:
                    out.write('!').write(this.val);
                    break;
                case Unary.BITWISE_NOT:
                    out.write('~').write(this.val);
                    break;
                case Unary.POS:
                    out.write('+').write(this.val);
                    break;
                case Unary.NEG:
                    out.write('-').write(this.val);
                    break;
                case Unary.AI:
                    out.write("++").write(this.val);
                    break;
                case Unary.PI:
                    out.write(this.val).write("++");
                    break;
                case Unary.AD:
                    out.write("--").write(this.val);
                    break;
                case Unary.PD:
                    out.write(this.val).write("--");
                    break;
                default:
                    throw new RuntimeException("Please use a constant from the Unary interface when making new unary expressions.");
            }

            return out;
        }

        @Override
        public int type() {
            return this.type;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            this.val = VisitorUtils.visit(this.val, this, visitor);
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
            return this.name;
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
            out.write(this.name);
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
        out.queue(this.comment);
        return out;
    }

    @Override
    public void visit(IVisitor visitor) {
        super.visit(visitor);
    }
}
