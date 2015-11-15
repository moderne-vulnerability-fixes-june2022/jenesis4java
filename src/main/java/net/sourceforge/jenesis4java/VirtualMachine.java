package net.sourceforge.jenesis4java;

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
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The <code>VirtualMachine</code> acts as a factory method for
 * <code>CompilationUnit</code>, <code>Type</code>, <code>Literal</code>, and
 * <code>Expression</code> objects. Most <code>Statement</code> factory methods
 * are located on <code>Block</code>; most <code>Declaration</code> factory
 * methods are located on the objects that contain them.
 */
public abstract class VirtualMachine {

    private static final Logger LOGGER = Logger.getLogger(VirtualMachine.class.getName());

    private static final String JENESIS_PROPERTY_PREFIX = "jenesis.";

    /**
     * The default <code>VirtualMachine</code> implementation.
     */
    private static VirtualMachine virtualMachine;

    /**
     * Returns the default <code>VirtualMachine</code> implementation previously
     * set using the static <code>setVirtualMachine(VirtualMachine)</code>
     * method OR returns the reference implementation (
     * <code>com.inxar.jenesis.MVM</code>. <br>
     * <br>
     * The settings for the coding style are read from the file specified by the
     * system property "jenesis.blockstyle-properties-filename" (a real file or
     * available in the classpath). If there was non specified, a fall back to
     * /META-INF/blockstyle.properties will be used.<br>
     * <br>
     * All properties may be overwritten by specifying them in the system
     * properties with a "jenesis." prefix.<br>
     * For example you can specify to use the
     * net.sourceforge.jenesis4java.jaloppy.JenesisJalopyEncoder encoder by
     * setting the property "jenesis.encoder" and "jenesis.encoder.jalopyconfig"
     */
    public static VirtualMachine getVirtualMachine() {
        if (VirtualMachine.virtualMachine == null) {

            InputStream in = null;
            BufferedInputStream bufferedStream = null;
            try {
                String filename = System.getProperty("jenesis.blockstyle-properties-filename");
                if (filename != null) {
                    if (new File(filename).exists()) {
                        in = new FileInputStream(filename);
                    } else {
                        in = Thread.currentThread().getContextClassLoader().getResourceAsStream(filename);
                    }
                } else {
                    filename = "blockstyle.properties";
                    in = net.sourceforge.jenesis4java.VirtualMachine.class.getClassLoader().getResourceAsStream(filename);
                }

                if (in == null) {
                    throw new RuntimeException("Cannot instantiate VirtualMachine: " + "Could not find blockstyle.properties file in system classpath "
                            + "or from -Djenesis.blockstyle-properties-filename!");
                }

                Properties p = new Properties();
                bufferedStream = new BufferedInputStream(in);
                p.load(bufferedStream);
                bufferedStream.close();
                in.close();
                in = null;

                Properties systemproperties = System.getProperties();
                for (String systemProperty : systemproperties.stringPropertyNames()) {
                    if (systemProperty.startsWith(VirtualMachine.JENESIS_PROPERTY_PREFIX)) {
                        p.put(systemProperty.substring(VirtualMachine.JENESIS_PROPERTY_PREFIX.length()), systemproperties.get(systemProperty));
                    }
                }
                VirtualMachine.virtualMachine = new net.sourceforge.jenesis4java.impl.MVM(p);

            } catch (IOException ioex) {
                RuntimeException rex = new RuntimeException("Could not load VirtualMachine blockstyles: " + ioex.getMessage());
                ioex.printStackTrace();
                throw rex;
            } finally {
                Exception exception = null;
                if (bufferedStream != null) {
                    try {
                        bufferedStream.close();
                    } catch (Exception ex) {
                        exception = ex;
                    }
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception ex) {
                        exception = ex;
                    }
                }
                if (exception != null) {
                    LOGGER.log(Level.SEVERE, "Exception during InputStreamReader.close(). ", exception);
                }
            }
        }

        return VirtualMachine.virtualMachine;
    }

    /**
     * Sets the default <code>VirtualMachine</code> implementation.
     */
    public static VirtualMachine setVirtualMachine(VirtualMachine vm) {
        VirtualMachine.virtualMachine = vm;
        return vm;
    }

    /**
     * Removes all cached compilationUnits.
     */
    public abstract VirtualMachine clear();

    /**
     * Writes out the source code to all enclosed <code>CompilationUnit</code>
     * instances.
     */
    public abstract VirtualMachine encode();

    /**
     * @return the current active encoder.
     */
    public abstract CompilationUnitEncoder getEncoder();

    public abstract FieldAccess newAccess(Class<?> qualifierClass, Object object);

    /**
     * <code>Expression</code> factory method which returns a new field access
     * with the given qualifier and name.
     */
    public abstract FieldAccess newAccess(Expression qualifier, String name);

    /**
     * <code>Expression</code> factory method which returns a new field access
     * with the given qualifier and name.
     */
    public abstract FieldAccess newAccess(String name);

    /**
     * <code>Expression</code> factory method which returns a new field access
     * with the given qualifier and name.
     */
    public abstract FieldAccess newAccess(String qualifier, String name);

    public abstract Annotation newAnnotation(Class<?> annotationClass);

    public abstract Annotation newAnnotation(String text);

    public abstract Annotation newAnnotation(String name, String text);

    /**
     * <code>Expression</code> factory method which returns a new anonymous
     * class instance creation expression for the given type.
     */
    public abstract NewAnonymousClass newAnon(Type type);

    public abstract NewAnonymousClass newAnonymousClass(String className, String... genericTypes);

    /**
     * <code>Type</code> factory method which returns an array of primitive type
     * with the given constant and dimensions.
     */
    public abstract ArrayType newArray(int type, int dims);

    /**
     * <code>Type</code> factory method which returns an array of class type
     * with the given name and dimensions.
     */
    public abstract ArrayType newArray(String type, int dims);

    /**
     * <code>Expression</code> factory method which returns a new array instance
     * creation expression for the given type.
     */
    public abstract NewArray newArray(Type type);

    /**
     * <code>Expression</code> factory method which returns a new array access
     * expression with the given qualifier and name.
     */
    public abstract ArrayAccess newArrayAccess(Expression qual, String name);

    /**
     * <code>Expression</code> factory method which returns a new array access
     * expression with the given qualifier and name.
     */
    public abstract ArrayAccess newArrayAccess(String name);

    /**
     * <code>Expression</code> factory method which returns a new array access
     * expression with the given qualifier and name.
     */
    public abstract ArrayAccess newArrayAccess(String qual, String name);

    /**
     * <code>Expression</code> factory method which returns a new array
     * initializer with the given arguments.
     */
    public abstract ArrayInitializer newArrayInit(Expression[] expressions);

    /**
     * <code>Expression</code> factory method which returns a new array
     * initializer with the given arguments.
     */
    public abstract ArrayInitializer newArrayInit(Object o);

    /**
     * <code>Expression</code> factory method which returns a new assignment of
     * the given type.
     */
    public abstract Assign newAssign(int type, Variable left, Expression right);

    /**
     * <code>Expression</code> factory method which returns a new assignment of
     * the SIMPLE type. This is a convenience * method.
     */
    public abstract Assign newAssign(Variable left, Expression right);

    /**
     * <code>Expression</code> factory method which returns a new unary function
     * of the given type.
     */
    public abstract Binary newBinary(int type, Expression left, Expression right);

    /**
     * <code>Expression</code> factory method which returns the blank
     * expression.
     */
    public abstract Blank newBlank();

    /**
     * <code>Literal</code> factory method which returns a new
     * <code>boolean</code> literal.
     */
    public abstract BooleanLiteral newBoolean(boolean value);

    /**
     * <code>Expression</code> factory method which returns a new expression
     * that wraps brackets around the expression.
     * 
     * @param value
     * @return this
     */
    public abstract Cast newBrackets(Expression value);

    /**
     * <code>Literal</code> factory method which returns a new <code>byte</code>
     * literal.
     */
    public abstract ByteLiteral newByte(byte val);

    /**
     * <code>Type</code> factory method which returns a calendar type with the
     * given pattern as representation storage.
     */
    public abstract CalendarClassType newCalendarType(String pattern);

    /**
     * <code>Expression</code> factory method which returns a new cast function
     * having the given type and value.
     */
    public abstract Cast newCast(Type type, Expression value);

    /**
     * <code>Literal</code> factory method which returns a new <code>char</code>
     * literal.
     */
    public abstract CharLiteral newChar(char val);

    /**
     * <code>Expression</code> factory method which returns a new class instance
     * creation expression for the given type.
     */
    public abstract NewClass newClass(Type type);

    /**
     * <code>Literal</code> factory method which returns a new
     * <code>Class</code> literal.
     */
    public abstract ClassLiteral newClassLiteral(ClassType type);

    /**
     * <code>Literal</code> factory method which returns a new
     * <code>Class</code> literal.
     */
    public abstract ClassLiteral newClassLiteral(String className);

    /**
     * Creates a new <code>CompilationUnit</code> in this <code>Machine</code>
     * and returns it.
     */
    public abstract CompilationUnit newCompilationUnit(String sourcepath);

    /**
     * Creates a new <code>CompilationUnit</code> in this <code>Machine</code>
     * sets the namespace and returns it.
     */
    public abstract CompilationUnit newCompilationUnit(String sourceDirectory, String packageName);

    /**
     * <code>Literal</code> factory method which returns a new
     * <code>double</code> literal.
     */
    public abstract DoubleLiteral newDouble(double val);

    /**
     * <code>Literal</code> factory method which returns the boolean
     * <code>false</code> literal.
     */
    public abstract False newFalse();

    /**
     * <code>Literal</code> factory method which returns a new
     * <code>float</code> literal.
     */
    public abstract FloatLiteral newFloat(float val);

    /**
     * <code>Expression</code> factory method which returns a new freeform
     * expression with the given code.
     */
    public abstract Freeform newFree(String code);

    /**
     * <code>Literal</code> factory method which returns a new <code>int</code>
     * literal.
     */
    public abstract IntLiteral newInt(int val);

    public abstract Invoke newInvoke(Class<?> qualifyingClass, String name);

    /**
     * <code>Expression</code> factory method which returns a new method
     * invocation with the given qualifier and name.
     */
    public abstract Invoke newInvoke(Expression qualifier, String name);

    /**
     * <code>Expression</code> factory method which returns a new method
     * invocation with the given qualifier and name.
     */
    public abstract Invoke newInvoke(String name);

    /**
     * <code>Expression</code> factory method which returns a new method
     * invocation with the given qualifier and name.
     */
    public abstract Invoke newInvoke(String qualifier, String name);

    /**
     * <code>Literal</code> factory method which returns a new <code>long</code>
     * literal.
     */
    public abstract LongLiteral newLong(long val);

    /**
     * <code>Literal</code> factory method which returns the <code>null</code>
     * literal.
     */
    public abstract Null newNull();

    /**
     * <code>Literal</code> factory method which returns a new octal
     * <code>char</code> literal.
     */
    public abstract OctalLiteral newOctal(char val);

    /**
     * <code>Literal</code> factory method which returns a new scientific
     * floating point <code>char</code> literal.
     */
    public abstract ScientificLiteral newScientific(int precision, int scale, int exponent);

    /**
     * <code>Literal</code> factory method which returns a new
     * <code>short</code> literal.
     */
    public abstract ShortLiteral newShort(short val);

    public abstract ClassType newSimpleType(Class<?> type);

    /**
     * <code>Literal</code> factory method which returns a new
     * <code>String</code> literal.
     */
    public abstract StringLiteral newString(String val);

    /**
     * <code>Expression</code> factory method which returns a new unary function
     * of the given type.
     */
    public abstract Ternary newTernary(int type, Expression one, Expression two, Expression three);

    /**
     * <code>Literal</code> factory method which returns the boolean
     * <code>true</code> literal.
     */
    public abstract True newTrue();

    /**
     * <code>Type</code> factory method which returns a class type named with
     * the given <code>Class</code>.
     */
    public abstract ClassType newType(Class<?> clazz);

    /**
     * <code>Type</code> factory method which returns a primitive type named by
     * the given <code>int</code> as allowed by the constants listed in the
     * <code>Type</code> interface.
     */
    public abstract PrimitiveType newType(int type);

    /**
     * <code>Type</code> factory method which returns a class type named with
     * the given <code>String</code>.
     */
    public abstract ClassType newType(String name);

    /**
     * <code>Expression</code> factory method which returns a new unary function
     * of the given type.
     */
    public abstract Unary newUnary(int type, Expression value);

    /**
     * <code>Literal</code> factory method which returns a new unicode
     * <code>char</code> literal.
     */
    public abstract UnicodeLiteral newUnicode(char val);

    /**
     * <code>Expression</code> factory method which returns a new variable with
     * the given name.
     */
    public abstract Variable newVar(String name);

    /**
     * <code>Expression</code> factory method which returns a new variable with
     * the given name.
     */
    public abstract Variable newVar(Type type);

    /**
     * @return type representing:
     */
    public abstract Type type_boolean();

    /**
     * @return type representing:
     */
    public abstract Type type_byte();

    /**
     * @return type representing:
     */
    public abstract Type type_char();

    /**
     * @return type representing:
     */
    public abstract Type type_double();

    /**
     * @return type representing:
     */
    public abstract Type type_float();

    /**
     * @return type representing:
     */
    public abstract Type type_int();

    /**
     * @return type representing:
     */
    public abstract Type type_long();

    /**
     * @return type representing:
     */
    public abstract Type type_short();

    /**
     * @return type representing:
     */
    public abstract Type type_void();

    /**
     * @return type representing:
     */
    public abstract ClassType typeBigDecimal();

    /**
     * @return type representing:
     */
    public abstract ClassType typeBoolean();

    /**
     * @return type representing:
     */
    public abstract ClassType typeCalendar();

    /**
     * @return type representing:
     */
    public abstract ClassType typeInteger();

    /**
     * @return type representing:
     */
    public abstract ClassType typeLong();

    /**
     * @return type representing:
     */
    public abstract ClassType typeString();

}
