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
 * The <code>CodeWriter</code> is the object to which code is rendered. All
 * methods that add content to the <code>CodeWriter</code> should return the
 * same object passed to them though the <code>toCode()</code> method. This
 * supports a coding style analogous to the <code>StringBuffer</code> class.
 */
public interface CodeWriter {

    /**
     * Decrements the tab and calls newLine()
     */
    CodeWriter dedentLine();

    /**
     * Returns the current number of characters in the current line. It does not
     * take the indent into account. Therefore, only the write methods and the
     * space method increment the column counter.
     */
    int getColumnNumber();

    /**
     * gets the context compilation unit.
     */
    CompilationUnit getCompilationUnit();

    /**
     * Returns the current number of indentation levels.
     */
    int getIndentNumber();

    /**
     * Returns the number of lines of the current document.
     */
    int getLineNumber();

    /**
     * Increments the tab and calls newLine()
     */
    CodeWriter indentLine();

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
    CodeWriter newLine();

    /**
     * This method allows those codeable objects to inject a comment without
     * interrupting the line-by-line code itself. For example, if an expression
     * wants to express a comment, it cannot do it until the end of the line.
     * Before the newline is called, all comments given to the code writer will
     * be written.
     */
    CodeWriter queue(Comment comment);

    /**
     * Resets the tab counter to zero and calls the newLine() method.
     */
    CodeWriter resetLine();

    /**
     * sets the context compilation unit.
     * 
     * @param mCompilationUnit
     */
    void setCompilationUnit(CompilationUnit mCompilationUnit);

    /**
     * Writes a single space.
     */
    CodeWriter space();

    /**
     * Writes a boolean.
     */
    CodeWriter write(boolean b);

    /**
     * Writes a single character.
     */
    CodeWriter write(char c);

    /**
     * Writes an array of characters.
     */
    CodeWriter write(char[] chars);

    /**
     * Writes an array of characters starting at the offset with total length
     * len.
     */
    CodeWriter write(char[] chars, int off, int len);

    /**
     * Instead of calling the <code>Object.toString()</code> method, the
     * <code>Object.toCode(CodeWriter)</code> method is invoked with
     * <code>this</code> as the argument.
     */
    CodeWriter write(Codeable codeable);

    /**
     * Iterates the array and sends each element to <code>write(Codeable)</code>
     * .
     */
    CodeWriter write(Codeable[] codeables);

    /**
     * Writes a double.
     */
    CodeWriter write(double d);

    /**
     * Writes a float.
     */
    CodeWriter write(float f);

    /**
     * Writes an integer.
     */
    CodeWriter write(int i);

    /**
     * Iterates the array and sends each element to <code>write(Codeable)</code>
     * .
     */
    CodeWriter write(List<? extends Codeable> codeables);

    /**
     * Writes an object.
     */
    CodeWriter write(Object o);

    /**
     * Writes a string.
     */
    CodeWriter write(String s);

}
