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
 * <code>Declaration</code> subinterface for interfaces.
 */
public interface Interface extends TypeDeclaration {

    /**
     * Adds the given string to the list of extends clauses and returns the
     * <code>Interface</code>.
     */
    Interface addExtends(String type);

    /**
     * Gets the list of extends clauses as an list of <code>String</code>.
     */
    List<String> getExtends();

    /**
     * Adds a new int constant to this interface with the given name and value.
     * This is a convenience method.
     */
    Constant newConstant(String name, int value);

    /**
     * Adds a new constant to this interface.
     */
    Constant newConstant(Type type, String name);

    /**
     * Adds a new abstract method signature to this interface.
     */
    AbstractMethod newMethod(Type type, String name);

    /**
     * Removes the given string to the list of extends clauses and returns if
     * the interface was present.
     */
    boolean removeExtends(String className);
}
