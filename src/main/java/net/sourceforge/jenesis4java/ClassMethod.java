package net.sourceforge.jenesis4java;

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
 * <code>Declaration</code> subinterface for a method which may occur in a class
 * and is a <code>Block</code>.
 */
public interface ClassMethod extends Method, Block {

    /**
     * Setter method for the <code>isNative</code> flag.
     */
    boolean isNative();

    /**
     * Setter method for the <code>isNative</code> flag.
     */
    ClassMethod isNative(boolean value);

    /**
     * Getter method for the <code>isSynchronized</code> flag.
     */
    boolean isSynchronized();

    /**
     * Setter method for the <code>isSynchronized</code> flag.
     */
    ClassMethod isSynchronized(boolean value);
}
