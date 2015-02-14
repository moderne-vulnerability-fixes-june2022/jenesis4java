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
 * <code>Statement</code> subinterface for local variable declarations. Although
 * of course 'let' is not a keyword, the concept implied by the let keyword in
 * other languages encapsulates the idea here. Multiple variables of the same
 * type can be initialized in the same <code>Let</code>.
 */
public interface Let extends Statement {

    /**
     * Adds a new assignment to this variable declaration.
     */
    Let addAssign(Assign assign);

    /**
     * Adds a new assignment to this variable declaration.
     */
    Let addAssign(String name, Expression expr);

    /**
     * Gets the list of comma separated assignments as an list of
     * <code>Assign</code>.
     */
    List<Assign> getAssigns();

    /**
     * Gets the type for this declaration.
     */
    Type getType();

    /**
     * Getter method for the isFinal flag.
     */
    boolean isFinal();

    /**
     * Setter method for the isFinal flag.
     */
    Let isFinal(boolean value);

    /**
     * Sets the type for this declaration.
     */
    Let setType(Type expr);
}
