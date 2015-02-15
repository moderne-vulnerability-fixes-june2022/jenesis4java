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

import java.util.List;

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
/**
 * <code>Declaration</code> subinterface for the class declaration heirarchy.
 */
public interface ClassDeclaration extends TypeDeclaration {

    /**
     * Adds the given string to the list of implements clauses.
     */
    ClassDeclaration addImplements(String type);

    /**
     * Adds the given import declaration to the compilation unit.
     */
    Import addImport(Class<?> clazz);

    /**
     * Adds the given import declaration to the compilation unit.
     */
    Import addImport(String name);

    /**
     * Gets the list of constructors as an list of <code>Constructor</code>.
     */
    List<Constructor> getConstructors();

    /**
     * Gets the extends clause.
     */
    String getExtends();

    /**
     * Gets the list of fields as an list of <code>ClassField</code>.
     */
    List<ClassField> getFields();

    /**
     * Gets the list of implementation clauses as an list of <code>String</code>
     * .
     */
    List<String> getImplements();

    /**
     * removes the interface of the list of implementation clauses and returns
     * if the interface was present. .
     */
    boolean removeImplements(String interfaceName);

    /**
     * Gets the list of inner classes as an list of <code>InnerClass</code>.
     */
    List<InnerClass> getInnerClasses();

    /**
     * Gets the list of method as an list of <code>ClassMethod</code>.
     */
    List<ClassMethod> getMethods();

    /**
     * Returns a list containing all <code>ClassMethod</code>s of the underlying
     * <code>ClassDeclaration</code> with the specified name.
     * 
     * @param methodName
     * @return all <code>ClassMethod</code>s with the specified name.
     */
    List<ClassMethod> getMethods(String methodName);

    /**
     * Gets the list of static initializers as an list of
     * <code>StaticInitializer</code>.
     */
    List<StaticInitializer> getStaticInitializers();

    /**
     * Getter method for the isAbstract flag.
     */
    boolean isAbstract();

    /**
     * Setter method for the isAbstract flag.
     */
    ClassDeclaration isAbstract(boolean value);

    /**
     * Creates a new static initialization block in this class
     */

    boolean isMethodAlreadyDefined(Type type, String name);

    /**
     * Creates a new constructor in this class
     */
    Constructor newConstructor();

    /**
     * Creates a new field in this class with the given type and name.
     */
    ClassField newField(Class<?> type, String name);

    /**
     * Creates a new field in this class with the given type and name.
     */
    ClassField newField(Type type, String name);

    /**
     * Creates a new inner class in this class.
     */
    InnerClass newInnerClass(String name);

    /**
     * Creates a new inner class in this class.
     */
    InnerInterface newInnerInterface(String name);

    /**
     * Creates a new void method in this class with the given type and name.
     */
    ClassMethod newMethod(String name);

    /**
     * Creates a new method in this class with the given type and name.
     */
    ClassMethod newMethod(Type type, String name);

    StaticInitializer newStaticInitializer();

    /**
     * Sets the extends clause to the given string. A class can extend only one
     * type.
     */
    ClassDeclaration setExtends(String type);
}
