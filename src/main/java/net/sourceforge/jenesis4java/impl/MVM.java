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
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import net.sourceforge.jenesis4java.Annotation;
import net.sourceforge.jenesis4java.ArrayAccess;
import net.sourceforge.jenesis4java.ArrayInitializer;
import net.sourceforge.jenesis4java.ArrayType;
import net.sourceforge.jenesis4java.Assign;
import net.sourceforge.jenesis4java.Binary;
import net.sourceforge.jenesis4java.Blank;
import net.sourceforge.jenesis4java.BooleanLiteral;
import net.sourceforge.jenesis4java.ByteLiteral;
import net.sourceforge.jenesis4java.CalendarClassType;
import net.sourceforge.jenesis4java.Cast;
import net.sourceforge.jenesis4java.CharLiteral;
import net.sourceforge.jenesis4java.ClassLiteral;
import net.sourceforge.jenesis4java.ClassType;
import net.sourceforge.jenesis4java.CodeWriter;
import net.sourceforge.jenesis4java.Codeable;
import net.sourceforge.jenesis4java.Comment;
import net.sourceforge.jenesis4java.CompilationUnit;
import net.sourceforge.jenesis4java.CompilationUnitEncoder;
import net.sourceforge.jenesis4java.DoubleLiteral;
import net.sourceforge.jenesis4java.Expression;
import net.sourceforge.jenesis4java.False;
import net.sourceforge.jenesis4java.FieldAccess;
import net.sourceforge.jenesis4java.FloatLiteral;
import net.sourceforge.jenesis4java.Freeform;
import net.sourceforge.jenesis4java.IVisitor;
import net.sourceforge.jenesis4java.IntLiteral;
import net.sourceforge.jenesis4java.Invoke;
import net.sourceforge.jenesis4java.LongLiteral;
import net.sourceforge.jenesis4java.NewAnonymousClass;
import net.sourceforge.jenesis4java.NewArray;
import net.sourceforge.jenesis4java.NewClass;
import net.sourceforge.jenesis4java.Null;
import net.sourceforge.jenesis4java.OctalLiteral;
import net.sourceforge.jenesis4java.PrimitiveType;
import net.sourceforge.jenesis4java.ScientificLiteral;
import net.sourceforge.jenesis4java.ShortLiteral;
import net.sourceforge.jenesis4java.StringLiteral;
import net.sourceforge.jenesis4java.Ternary;
import net.sourceforge.jenesis4java.True;
import net.sourceforge.jenesis4java.Type;
import net.sourceforge.jenesis4java.Unary;
import net.sourceforge.jenesis4java.UnicodeLiteral;
import net.sourceforge.jenesis4java.Variable;
import net.sourceforge.jenesis4java.VirtualMachine;
import net.sourceforge.jenesis4java.impl.util.BlockStyle;
import net.sourceforge.jenesis4java.impl.util.VisitorUtils;

/**
 * Standard <code>VirtualMachine</code> implementation.
 */
public class MVM extends net.sourceforge.jenesis4java.VirtualMachine {

    /* =============================================================== */
    /* CODEABLE SUPERPARENT */
    /* =============================================================== */
    abstract static class MCodeable implements Codeable {

        Comment comment;

        MVM vm;

        MCodeable(MVM vm) {
            this.vm = vm;
        }

        @Override
        public <T> T cast(Class<T> clazz) {
            return clazz.cast(this);
        }

        @Override
        public Comment getComment() {
            return this.comment;
        }

        @Override
        public boolean isBlockWithAbruptCompletion() {
            return false;
        }

        @Override
        public MCodeable setComment(int type, String text) {
            switch (type) {
                case Comment.S:
                    this.comment = new MComment.MSingleLineComment(this.vm, text);
                    break;
                case Comment.M:
                    this.comment = new MComment.MMultipleLineComment(this.vm, text);
                    break;
                case Comment.Mbe:
                    this.comment = new MComment.MMultipleLineComment(this.vm, text, true);
                    break;
                case Comment.D:
                    this.comment = new MComment.MDocumentationComment(this.vm, text);
                    break;
            }
            return this;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            if (this.comment != null) {
                out.write(this.comment);
            }
            return out;
        }

        @Override
        public String toString() {
            StringWriter sout = new StringWriter();
            PrintWriter pout = new PrintWriter(sout);
            CodeWriter cout = new MCodeWriter(pout);
            toCode(cout);
            return sout.toString();
        }

        @Override
        public void visit(IVisitor visitor) {
            this.comment = VisitorUtils.visit(this.comment, this, visitor);
        }

        @Override
        public VirtualMachine vm() {
            return this.vm;
        }
    }

    PrimitiveType VOID;

    PrimitiveType NULL;

    PrimitiveType BOOLEAN;

    PrimitiveType BYTE;

    PrimitiveType SHORT;

    PrimitiveType INT;

    PrimitiveType CHAR;

    PrimitiveType LONG;

    PrimitiveType FLOAT;

    PrimitiveType DOUBLE;

    ClassType STRING;

    ClassType BIGDECIMAL_TYPE;

    ClassType BOOLEAN_TYPE;

    ClassType CALENDAR_TYPE;

    ClassType INTEGER_TYPE;

    ClassType LONG_TYPE;

    ClassType STRING_TYPE;

    Null _NULL;

    True TRUE;

    False FALSE;

    MStyle.MStyleMap styles;

    List<CompilationUnit> units;

    CompilationUnitEncoder encoder;

    public MVM(java.util.Properties styleprops) {
        init(styleprops);
    }

    public MVM(String styleprops) throws IOException {
        BufferedInputStream in = null;
        Properties p = null;

        try {

            // make the stream
            in = new BufferedInputStream(new FileInputStream(styleprops));
            // fill the props
            p = new Properties();
            p.load(in);
            // close stream
            in.close();

        } catch (IOException ioex) {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
            throw ioex;
        }

        init(p);
    }

    @Override
    public VirtualMachine clear() {
        this.units.clear();
        return this;
    }

    @Override
    public MVM encode() throws java.io.IOException {
        for (CompilationUnit c : this.units) {
            c.encode();
        }
        return this;
    }

    @Override
    public CompilationUnitEncoder getEncoder() {
        return this.encoder;
    }

    @Override
    public FieldAccess newAccess(Class<?> qualifierClass, Object object) {
        return newAccess(qualifierClass.getSimpleName(), object.toString());
    }

    @Override
    public FieldAccess newAccess(Expression qual, String name) {
        return new MExpression.MFieldAccess(this, qual, name);
    }

    @Override
    public FieldAccess newAccess(String name) {
        return new MExpression.MFieldAccess(this, (String) null, name);
    }

    @Override
    public FieldAccess newAccess(String qual, String name) {
        return new MExpression.MFieldAccess(this, qual, name);
    }

    @Override
    public Annotation newAnnotation(Class<?> annotationClass) {
        return newAnnotation(annotationClass.getSimpleName());
    }

    @Override
    public Annotation newAnnotation(String text) {
        return new MAnnotation(this, text);
    }

    @Override
    public Annotation newAnnotation(String name, String text) {
        return new MAnnotation(this, name, text);
    }

    @Override
    public NewAnonymousClass newAnon(Type type) {
        return new MExpression.MNewAnonymousClass(this, type);
    }

    @Override
    public NewAnonymousClass newAnonymousClass(String className, String... genericTypes) {
        return newAnon(newType(className + createTypeParameters(genericTypes)));
    }

    @Override
    public ArrayType newArray(int type, int name) {
        return new MType.MArrayType(this, newType(type), name);
    }

    @Override
    public ArrayType newArray(String type, int dims) {
        return new MType.MArrayType(this, newType(type), dims);
    }

    @Override
    public NewArray newArray(Type type) {
        return new MExpression.MNewArray(this, type);
    }

    public ArrayType newArray(Type type, int dims) {
        return new MType.MArrayType(this, type, dims);
    }

    @Override
    public ArrayAccess newArrayAccess(Expression qual, String name) {
        return new MExpression.MArrayAccess(this, qual, name);
    }

    @Override
    public ArrayAccess newArrayAccess(String name) {
        return new MExpression.MArrayAccess(this, (String) null, name);
    }

    @Override
    public ArrayAccess newArrayAccess(String qual, String name) {
        return new MExpression.MArrayAccess(this, qual, name);
    }

    @Override
    public ArrayInitializer newArrayInit(Expression[] expressions) {
        return new MExpression.MArrayInitializer(this, expressions);
    }

    @Override
    public ArrayInitializer newArrayInit(Object o) {
        return new MExpression.MArrayInitializer(this, o);
    }

    @Override
    public Assign newAssign(int type, Variable l, Expression r) {
        return new MExpression.MAssign(this, type, l, r);
    }

    @Override
    public Assign newAssign(Variable l, Expression r) {
        return new MExpression.MAssign(this, Assign.S, l, r);
    }

    @Override
    public Binary newBinary(int type, Expression l, Expression r) {
        return new MExpression.MBinary(this, type, l, r);
    }

    @Override
    public Blank newBlank() {
        return new MExpression.MBlank(this);
    }

    @Override
    public BooleanLiteral newBoolean(boolean value) {
        return value ? newTrue() : newFalse();
    }

    @Override
    public Cast newBrackets(Expression expression) {
        return newCast(null, expression);
    }

    @Override
    public ByteLiteral newByte(byte val) {
        return new MLiteral.MByteLiteral(this, val);
    }

    @Override
    public CalendarClassType newCalendarType(String pattern) {
        return new MType.MCalendarClassType(this, pattern);
    }

    @Override
    public Cast newCast(Type type, Expression val) {
        return new MExpression.MCast(this, type, val);
    }

    @Override
    public CharLiteral newChar(char val) {
        return new MLiteral.MCharLiteral(this, val);
    }

    @Override
    public NewClass newClass(Type type) {
        return new MExpression.MNewClass(this, type);
    }

    @Override
    public ClassLiteral newClassLiteral(ClassType val) {
        return new MLiteral.MClassLiteral(this, val);
    }

    @Override
    public ClassLiteral newClassLiteral(String val) {
        return new MLiteral.MClassLiteral(this, newType(val));
    }

    @Override
    public CompilationUnit newCompilationUnit(String path) {
        CompilationUnit x = new MDeclaration.MCompilationUnit(this, path);
        this.units.add(x);
        return x;
    }

    @Override
    public CompilationUnit newCompilationUnit(String sourceDirectory, String packageName) {
        CompilationUnit unit = newCompilationUnit(sourceDirectory);
        unit.setNamespace(packageName);
        return unit;
    }

    @Override
    public DoubleLiteral newDouble(double val) {
        return new MLiteral.MDoubleLiteral(this, val);
    }

    @Override
    public False newFalse() {
        return this.FALSE;
    }

    @Override
    public FloatLiteral newFloat(float val) {
        return new MLiteral.MFloatLiteral(this, val);
    }

    @Override
    public Freeform newFree(String code) {
        return new MExpression.MFreeform(this, code);
    }

    @Override
    public IntLiteral newInt(int val) {
        return new MLiteral.MIntLiteral(this, val);
    }

    @Override
    public Invoke newInvoke(Class<?> qualifyingClass, String name) {
        return newInvoke(qualifyingClass.getSimpleName(), name);
    }

    @Override
    public Invoke newInvoke(Expression qual, String name) {
        return new MExpression.MInvoke(this, qual, name);
    }

    @Override
    public Invoke newInvoke(String name) {
        return newInvoke((String) null, name);
    }

    @Override
    public Invoke newInvoke(String qual, String name) {
        return new MExpression.MInvoke(this, qual, name);
    }

    @Override
    public LongLiteral newLong(long val) {
        return new MLiteral.MLongLiteral(this, val);
    }

    @Override
    public Null newNull() {
        return this._NULL;
    }

    @Override
    public OctalLiteral newOctal(char val) {
        return new MLiteral.MOctalLiteral(this, val);
    }

    @Override
    public ScientificLiteral newScientific(int precision, int scale, int exponent) {
        return new MLiteral.MScientificLiteral(this, precision, scale, exponent);
    }

    @Override
    public ShortLiteral newShort(short val) {
        return new MLiteral.MShortLiteral(this, val);
    }

    @Override
    public ClassType newSimpleType(Class<?> type) {
        return newType(type.getSimpleName());
    }

    @Override
    public StringLiteral newString(String val) {
        return new MLiteral.MStringLiteral(this, val);
    }

    @Override
    public Ternary newTernary(int type, Expression one, Expression two, Expression three) {
        return new MExpression.MTernary(this, type, one, two, three);
    }

    @Override
    public True newTrue() {
        return this.TRUE;
    }

    @Override
    public ClassType newType(Class<?> clazz) {
        return newType(clazz.getName());
    }

    @Override
    public PrimitiveType newType(int type) {
        switch (type) {
            case Type.VOID:
                return this.VOID;
            case Type.NULL:
                return this.NULL;
            case Type.BOOLEAN:
                return this.BOOLEAN;
            case Type.BYTE:
                return this.BYTE;
            case Type.SHORT:
                return this.SHORT;
            case Type.INT:
                return this.INT;
            case Type.LONG:
                return this.LONG;
            case Type.FLOAT:
                return this.FLOAT;
            case Type.DOUBLE:
                return this.DOUBLE;
            case Type.CHAR:
                return this.CHAR;
        }
        return null;
    }

    @Override
    public ClassType newType(String name) {
        return new MType.MClassType(this, name);
    }

    @Override
    public Unary newUnary(int type, Expression val) {
        return new MExpression.MUnary(this, type, val);
    }

    @Override
    public UnicodeLiteral newUnicode(char val) {
        return new MLiteral.MUnicodeLiteral(this, val);
    }

    @Override
    public Variable newVar(String name) {
        return new MExpression.MVariable(this, name);
    }

    @Override
    public Variable newVar(Type type) {
        return new MExpression.MTypeVariable(this, type);
    }

    @Override
    public Type type_boolean() {
        return this.BOOLEAN;
    }

    @Override
    public Type type_byte() {
        return this.BYTE;
    }

    @Override
    public Type type_char() {
        return this.CHAR;
    }

    @Override
    public Type type_double() {
        return this.DOUBLE;
    }

    @Override
    public Type type_float() {
        return this.FLOAT;
    }

    @Override
    public Type type_int() {
        return this.INT;
    }

    @Override
    public Type type_long() {
        return this.LONG;
    }

    @Override
    public Type type_short() {
        return this.SHORT;
    }

    @Override
    public Type type_void() {
        return this.VOID;
    }

    @Override
    public ClassType typeBigDecimal() {
        return this.BIGDECIMAL_TYPE;
    }

    @Override
    public ClassType typeBoolean() {
        return this.BOOLEAN_TYPE;
    }

    @Override
    public ClassType typeCalendar() {
        return this.CALENDAR_TYPE;
    }

    @Override
    public ClassType typeInteger() {
        return this.INTEGER_TYPE;
    }

    @Override
    public ClassType typeLong() {
        return this.LONG_TYPE;
    }

    @Override
    public ClassType typeString() {
        return this.STRING_TYPE;
    }

    private String createTypeParameters(String[] genericTypes) {
        if (genericTypes == null || genericTypes.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder("<");
        boolean first = true;
        for (String type : genericTypes) {
            if (!first) {
                builder.append(", ");
            } else {
                first = false;
            }
            builder.append(type);
        }
        return builder.append(">").toString();
    }

    BlockStyle getStyle(String name) {
        return this.styles.get(name);
    }

    void init(java.util.Properties styleprops) {
        initTypes();
        this.styles = new MStyle.MStyleMap(styleprops);
        this.units = new ArrayList<CompilationUnit>();
        try {
            this.encoder = (CompilationUnitEncoder) Class.forName(styleprops.getProperty("encoder", BasicCompilationUnitEncoder.class.getName())).newInstance();
        } catch (Exception e) {
            throw new RuntimeException("encoder class not found!", e);
        }
    }

    void initTypes() {
        this.VOID = new MType.MPrimitiveType(this, Type.VOID, "void");
        this.NULL = new MType.MPrimitiveType(this, Type.NULL, "null");
        this.BOOLEAN = new MType.MPrimitiveType(this, Type.BOOLEAN, "boolean");
        this.BYTE = new MType.MPrimitiveType(this, Type.BYTE, "byte");
        this.SHORT = new MType.MPrimitiveType(this, Type.SHORT, "short");
        this.INT = new MType.MPrimitiveType(this, Type.INT, "int");
        this.CHAR = new MType.MPrimitiveType(this, Type.CHAR, "char");
        this.LONG = new MType.MPrimitiveType(this, Type.LONG, "long");
        this.FLOAT = new MType.MPrimitiveType(this, Type.FLOAT, "float");
        this.DOUBLE = new MType.MPrimitiveType(this, Type.DOUBLE, "double");
        this.STRING = new MType.MClassType(this, "String");
        this._NULL = new MLiteral.MNull(this);
        this.TRUE = new MLiteral.MTrue(this);
        this.FALSE = new MLiteral.MFalse(this);

        this.BIGDECIMAL_TYPE = new MType.MClassType(this, java.math.BigDecimal.class.getName());
        this.BOOLEAN_TYPE = new MType.MClassType(this, Boolean.class.getName());
        this.CALENDAR_TYPE = new MType.MClassType(this, java.util.Calendar.class.getName());
        this.INTEGER_TYPE = new MType.MClassType(this, Integer.class.getName());
        this.LONG_TYPE = new MType.MClassType(this, Long.class.getName());
        this.STRING_TYPE = new MType.MClassType(this, String.class.getName());
    }
}
