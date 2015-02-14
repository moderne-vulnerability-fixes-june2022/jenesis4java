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
 * <code>Declaration</code> subinterface for methods including abstract and
 * class methods.
 */
public interface Method extends Member {

    /**
     * Adds a new <code>FormalParameter</code> to this method signature with the
     * given type and name and returns the <code>Method</code>.
     */
    FormalParameter addParameter(Class type, String name);

    /**
     * Adds a new <code>FormalParameter</code> to this method signature with the
     * given type and name and returns the <code>Method</code>.
     */
    FormalParameter addParameter(Type type, String name);

    /**
     * Adds this string to the list of throws and returns the
     * <code>Method</code>.
     */
    Method addThrows(String type);

    /**
     * Gets the list of formal parameter declarations as an list of
     * <code>FormalParameter</code>.
     */
    List<FormalParameter> getParameters();

    /**
     * Gets the list of throws clauses as an list of <code>String</code>.
     */
    List<String> getThrows();

    /**
     * Gets the (return) type of this method.
     */
    Type getType();

    /**
     * Accessor method for the isAbstract flag.
     */
    boolean isAbstract();

    /**
     * Mutator method for the isAbstract flag.
     */
    Method isAbstract(boolean value);

    /**
     * Sets the (return) type of this method.
     */
    Method setType(Type type);
}
