package net.sourceforge.jenesis4java;

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
 * <code>Declaration</code> subinterface for compilation units. A compilation
 * unit is the source code atom in a typical filesystem. It consists of three
 * parts, each of which is optional:
 * <UL>
 * <LI>A package declaration (�7.4), giving the fully qualified name (�6.7) of
 * the package to which the compilation unit belongs
 * <LI>Iimport declarations (�7.5) that allow types from other packages to be
 * referred to using their simple names
 * <LI>Type declarations (�7.6) of class and interface types
 * </UL>
 */
public interface CompilationUnit extends Codeable {

    /**
     * Adds the given import declaration to the compilation unit.
     */
    Import addImport(Class<?> clazz);

    /**
     * Adds the given import declaration to the compilation unit.
     */
    Import addImport(String name);

    /**
     * Generates the source code file.
     */
    CompilationUnit encode() throws java.io.IOException;

    /**
     * Returns the filesystem location where the compilation unit should be
     * written.
     */
    String getCodebase();

    /**
     * Gets the list of imports as an list of <code>ImportDeclaration</code>
     */
    List<Import> getImports();

    /**
     * Returns the package name as a <code>Package</code>.
     */
    Namespace getNamespace();

    PackageClass getPackageClass(String name);

    /**
     * Gets the top level class or interface.
     */
    TypeDeclaration getTopLevelType();

    /**
     * Returns a list of types members as an list of
     * <code>TypeDeclaration</code>.
     */
    List<TypeDeclaration> getTypes();

    /**
     * Adds a new class to this compilation unit.
     */
    PackageClass newClass(String name);

    /**
     * Adds a new interface to this compilation unit.
     */
    Interface newInterface(String name);

    /**
     * Adds a new public class to this compilation unit.
     */
    PackageClass newPublicClass(String name);

    /**
     * Adds a new interface to this compilation unit.
     */
    Interface newPublicInterface(String name);

    /**
     * Sets the package name and returns the corresponding package declaration.
     */
    Namespace setNamespace(String name);
}
