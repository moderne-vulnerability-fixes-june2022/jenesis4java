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
import net.sourceforge.jenesis4java.BooleanLiteral;
import net.sourceforge.jenesis4java.ByteLiteral;
import net.sourceforge.jenesis4java.CharLiteral;
import net.sourceforge.jenesis4java.ClassLiteral;
import net.sourceforge.jenesis4java.ClassType;
import net.sourceforge.jenesis4java.CodeWriter;
import net.sourceforge.jenesis4java.Comment;
import net.sourceforge.jenesis4java.DoubleLiteral;
import net.sourceforge.jenesis4java.False;
import net.sourceforge.jenesis4java.FloatLiteral;
import net.sourceforge.jenesis4java.IVisitor;
import net.sourceforge.jenesis4java.IntLiteral;
import net.sourceforge.jenesis4java.Literal;
import net.sourceforge.jenesis4java.LongLiteral;
import net.sourceforge.jenesis4java.Null;
import net.sourceforge.jenesis4java.OctalLiteral;
import net.sourceforge.jenesis4java.ScientificLiteral;
import net.sourceforge.jenesis4java.ShortLiteral;
import net.sourceforge.jenesis4java.StringLiteral;
import net.sourceforge.jenesis4java.True;
import net.sourceforge.jenesis4java.Type;
import net.sourceforge.jenesis4java.UnicodeLiteral;
import net.sourceforge.jenesis4java.impl.util.VisitorUtils;

/**
 * Standard <code>Literal</code> implementations.
 */
public abstract class MLiteral extends MVM.MCodeable implements Literal {

    // ===============================================================
    // BOOLEAN LITERAL
    // ===============================================================
    static class MBooleanLiteral extends MLiteral implements BooleanLiteral {

        MBooleanLiteral(MVM vm, boolean val) {
            super(vm, vm.BOOLEAN, Boolean.valueOf(val), val ? "true" : "false");
        }

        @Override
        public boolean toBoolean() {
            return ((Boolean) val).booleanValue();
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    // ===============================================================
    // BYTE LITERAL
    // ===============================================================
    static class MByteLiteral extends MLiteral implements ByteLiteral {

        MByteLiteral(MVM vm, byte val) {
            super(vm, vm.BYTE, Byte.valueOf(val), "(byte)" + val);
        }

        @Override
        public byte toByte() {
            return ((Byte) val).byteValue();
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    // ===============================================================
    // CHAR LITERAL
    // ===============================================================
    static class MCharLiteral extends MLiteral implements CharLiteral {

        private static String escapedValue(char val) {
            StringBuffer b = new StringBuffer();
            b.append("'");
            escape(val, b);
            b.append("'");
            return b.toString();
        }

        static StringBuffer escape(char c, StringBuffer b) {
            // Check if the char is in the ACSII range.
            if (c >= 0x20 && c < 0x7f) {
                // The char is ASCII, but we still may need to
                // escape it.
                if (c == '"' || c == '\'' || c == '\\') {
                    b.append("\\");
                }
                b.append(c);

            } else {
                // Need special escapes for these chars.
                switch (c) {
                    case '\b':
                        b.append("\\b");
                        break;
                    case '\t':
                        b.append("\\t");
                        break;
                    case '\n':
                        b.append("\\n");
                        break;
                    case '\f':
                        b.append("\\f");
                        break;
                    case '\r':
                        b.append("\\r");
                        break;
                    default: {
                        String charString = Integer.toHexString(c);
                        b.append("\\u");
                        b.append("0000".substring(charString.length()));
                        b.append(charString);
                    }
                }
            }
            return b;
        }

        MCharLiteral(MVM vm, char val) {
            super(vm, vm.CHAR, Character.valueOf(val), escapedValue(val));
        }

        @Override
        public char toChar() {
            return ((Character) val).charValue();
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }

    }

    // ===============================================================
    // CLASS LITERAL
    // ===============================================================
    static class MClassLiteral extends MLiteral implements ClassLiteral {

        MClassLiteral(MVM vm, ClassType val) {
            super(vm, val, val, val.toString() + ".class");
        }

        @Override
        public Class<?> toClass() {
            return null;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    // ===============================================================
    // DOUBLE LITERAL
    // ===============================================================
    static class MDoubleLiteral extends MLiteral implements DoubleLiteral {

        MDoubleLiteral(MVM vm, double val) {
            super(vm, vm.DOUBLE, new Double(val), Double.toString(val) + "D");
        }

        @Override
        public double toDouble() {
            return ((Double) val).doubleValue();
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    // ===============================================================
    // FALSE LITERAL
    // ===============================================================
    static class MFalse extends MBooleanLiteral implements False {

        MFalse(MVM vm) {
            super(vm, false);
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    // ===============================================================
    // FLOAT LITERAL
    // ===============================================================
    static class MFloatLiteral extends MLiteral implements FloatLiteral {

        MFloatLiteral(MVM vm, float val) {
            super(vm, vm.FLOAT, new Float(val), Float.toString(val) + "F");
        }

        @Override
        public float toFloat() {
            return ((Float) val).floatValue();
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    // ===============================================================
    // INT LITERAL
    // ===============================================================
    static class MIntLiteral extends MLiteral implements IntLiteral {

        MIntLiteral(MVM vm, int val) {
            super(vm, vm.INT, Integer.valueOf(val), Integer.toString(val));
        }

        @Override
        public int toInt() {
            return ((Integer) val).intValue();
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    // ===============================================================
    // LONG LITERAL
    // ===============================================================
    static class MLongLiteral extends MLiteral implements LongLiteral {

        MLongLiteral(MVM vm, long val) {
            super(vm, vm.LONG, Long.valueOf(val), Long.toString(val) + 'L');
        }

        @Override
        public long toLong() {
            return ((Long) val).longValue();
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    // ===============================================================
    // NULL LITERAL
    // ===============================================================
    static class MNull extends MLiteral implements Null {

        MNull(MVM vm) {
            super(vm, vm.NULL, null, "null");
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    // ===============================================================
    // OCTAL LITERAL
    // ===============================================================
    static class MOctalLiteral extends MCharLiteral implements OctalLiteral {

        MOctalLiteral(MVM vm, char val) {
            super(vm, val);
            label = "'\\" + Integer.toOctalString(val) + "'";
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    // ===============================================================
    // SCIENTIFIC LITERAL
    // ===============================================================
    static class MScientificLiteral extends MLiteral implements ScientificLiteral {

        final int precision;

        final int scale;

        final int exponent;

        MScientificLiteral(MVM vm, int precision, int scale, int exponent) {
            super(vm, vm.DOUBLE, null, "" + precision + "." + scale + "e" + exponent);
            this.precision = precision;
            this.scale = scale;
            this.exponent = exponent;
        }

        @Override
        public int getExponent() {
            return exponent;
        }

        @Override
        public int getPrecision() {
            return precision;
        }

        @Override
        public int getScale() {
            return scale;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    // ===============================================================
    // SHORT LITERAL
    // ===============================================================
    static class MShortLiteral extends MLiteral implements ShortLiteral {

        MShortLiteral(MVM vm, short val) {
            super(vm, vm.SHORT, Short.valueOf(val), "(short)" + val);
        }

        @Override
        public short toShort() {
            return ((Short) val).shortValue();
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    // ===============================================================
    // STRING LITERAL
    // ===============================================================
    static class MStringLiteral extends MLiteral implements StringLiteral {

        private static void escape(String s, StringBuffer b) {
            // Cache the length of the String to escape.
            int len = s.length();
            // Iterate over the length of the string.
            for (int i = 0; i < len; i++) {
                MCharLiteral.escape(s.charAt(i), b);
            }
        }

        private static String escapeValue(String val) {
            StringBuffer b = new StringBuffer();
            b.append("\"");
            escape(val, b);
            b.append("\"");
            return b.toString();
        }

        MStringLiteral(MVM vm, String val) {
            super(vm, vm.STRING, val, escapeValue(val));
        }

        @Override
        public String toString() {
            return (String) val;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    // ===============================================================
    // TRUE LITERAL
    // ===============================================================
    static class MTrue extends MBooleanLiteral implements True {

        MTrue(MVM vm) {
            super(vm, true);
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    // ===============================================================
    // UNICODE LITERAL
    // ===============================================================
    static class MUnicodeLiteral extends MCharLiteral implements UnicodeLiteral {

        MUnicodeLiteral(MVM vm, char val) {
            super(vm, val);
            label = "'\\u" + Integer.toHexString(val) + "'";
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    private Comment comment;

    private Type type;

    final Object val;

    String label;

    MLiteral(MVM vm, Type type, Object val, String label) {
        super(vm);
        this.type = type;
        this.val = val;
        this.label = label;
    }

    @Override
    public Comment getComment() {
        return comment;
    }

    @Override
    public Type getType() {
        return type;
    }

    public MLiteral setComment(Comment comment) {
        this.comment = comment;
        return this;
    }

    @Override
    public CodeWriter toCode(CodeWriter out) {
        out.queue(comment);
        out.write(label);
        return out;
    }

    @Override
    public Object toObject() {
        return val;
    }

    @Override
    public void visit(IVisitor visitor) {
        super.visit(visitor);
        type = VisitorUtils.visit(type, this, visitor);
        comment = VisitorUtils.visit(comment, this, visitor);
    }
}
