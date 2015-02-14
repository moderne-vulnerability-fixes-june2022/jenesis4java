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
 * <code>Expression</code> subinterface for freeform expressions. The freeform
 * expression is a very general container expression for (hopefully) exceptional
 * instances in which it the api is too difficult or cumbersome and one needs to
 * inject a source code directly. This structure simply copies the text given in
 * setCode. No semicolon is appended.
 */
public interface Freeform extends Expression {

    /**
     * Decrements the tab and calls newLine()
     */
    Freeform dedentLine();

    /**
     * Gets the code for this expression.
     */
    String getCode();

    /**
     * Returns the current number of indentation levels.
     */
    int getIndentNumber();

    /**
     * Increments the tab and calls newLine()
     */
    Freeform indentLine();

    /**
     * Returns true if no characters have been written since the last call of
     * newLine().
     */
    boolean isLineNew();

    /**
     * Adds a the newLine string according to
     * <CODE>System.getProperty("line.separator")</CODE> and the line is padded
     * with the n tab characters where n is the number returned by
     * <code>getIndentNumber()</code>.
     */
    Freeform newLine();

    /**
     * Resets the tab counter to zero and calls the newLine() method.
     */
    Freeform resetLine();

    /**
     * Sets the code for this expression.
     */
    Freeform setCode(String code);

    /**
     * Writes a single space.
     */
    Freeform space();

    /**
     * Writes a boolean.
     */
    Freeform write(boolean b);

    /**
     * Writes a single character.
     */
    Freeform write(char c);

    /**
     * Writes an array of characters.
     */
    Freeform write(char[] chars);

    /**
     * Writes a double.
     */
    Freeform write(double d);

    /**
     * Writes a float.
     */
    Freeform write(float f);

    /**
     * Writes an integer.
     */
    Freeform write(int i);

    /**
     * Writes an object.
     */
    Freeform write(Object o);

    /**
     * Writes each element of the given object array.
     */
    Freeform write(Object[] ao);

    /**
     * Writes a string.
     */
    Freeform write(String s);
}
