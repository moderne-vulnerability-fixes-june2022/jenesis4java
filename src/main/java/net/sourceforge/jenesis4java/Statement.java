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
 * The <code>Statement</code> superinterface. All statements can be optionally
 * labeled.
 */
public interface Statement extends Codeable {

    /**
     * Comments the statement.
     */
    Comment comment(String text);

    /**
     * Gets the label for the statement.
     */
    String getLabel();

    /**
     * Returns <code>true</code> if the <code>Statement</code> is a statement
     * that completes abruptly (as defined in The Java language specification,
     * 2nd Edition,Chapter 14.1 - see <a href=
     * "http://java.sun.com/docs/books/jls/second_edition/html/statements.doc.html#5894"
     * >Normal and Abrupt Completion of Statements</a>).
     * 
     * @return <code>true</code> if the <code>Statement</code> is a statement
     *         that completes abruptly
     */
    boolean isAbruptCompletionStatement();

    /**
     * Sets the label for the statement.
     */
    Statement setLabel(String label);
}
