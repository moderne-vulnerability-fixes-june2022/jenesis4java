package net.sourceforge.jenesis4java.impl;

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
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jenesis4java.CodeWriter;
import net.sourceforge.jenesis4java.Codeable;
import net.sourceforge.jenesis4java.Comment;
import net.sourceforge.jenesis4java.CompilationUnit;

/**
 * Standard <code>CodeWriter</code> implementation.
 */
public class MCodeWriter implements CodeWriter {

    // the actual writer
    private final PrintWriter out;

    private int colNo;

    private int lineNo;

    private int indentNo;

    private boolean isLineNew;

    private boolean hasQueue;

    private final List<Comment> queue;

    private CompilationUnit compilationUnit;

    public MCodeWriter(PrintWriter out) {
        this.out = out;
        colNo = 0;
        lineNo = 1; // start at line 1
        indentNo = 0;
        isLineNew = true;
        queue = new ArrayList<Comment>();
    }

    /**
     * Decrements the tab and calls newLine()
     */
    @Override
    public CodeWriter dedentLine() {
        if (hasQueue) {
            flushQueue();
            hasQueue = false;
        }
        indentNo--;
        newLine();
        return this;
    }

    /**
     * Returns the current number of characters in the current line. It does not
     * take the indent into account. Therefore, only the write methods and the
     * space method increment the column counter.
     */
    @Override
    public int getColumnNumber() {
        return colNo;
    }

    @Override
    public CompilationUnit getCompilationUnit() {
        return compilationUnit;
    }

    /**
     * Returns the current number of indentation levels.
     */
    @Override
    public int getIndentNumber() {
        return indentNo;
    }

    /**
     * Returns the number of lines of the current document.
     */
    @Override
    public int getLineNumber() {
        return lineNo;
    }

    /**
     * Increments the tab and calls newLine()
     */
    @Override
    public CodeWriter indentLine() {
        if (hasQueue) {
            flushQueue();
            hasQueue = false;
        }
        indentNo++;
        newLine();
        return this;
    }

    /**
     * Returns true if no characters have been written since the last call of
     * newLine().
     */
    @Override
    public boolean isLineNew() {
        return isLineNew;
    }

    /**
     * Adds a the newLine string according to
     * <CODE>System.getProperty("line.separator")</CODE> and the line is padded
     * with the n tab characters where n is the number returned by
     * <code>getIndentNumber()</code>.
     */
    @Override
    public CodeWriter newLine() {
        if (hasQueue) {
            flushQueue();
            hasQueue = false;
        }
        out.println();
        writeIndent();
        colNo = 0;
        lineNo++;
        isLineNew = true;
        return this;
    }

    /**
     * This method allows those codeable objects to inject a comment without
     * interrupting the line-by-line code itself. For example, if an expression
     * wants to express a comment, it cannot do it until the end of the line.
     * This method accepts a string argument. Before the newline is called, all
     * comments given to the code writer will be written after newLine has been
     * called.
     * 
     * @return
     */
    @Override
    public MCodeWriter queue(Comment comment) {
        if (comment != null) {
            comment.setText(comment.getText());
            queue.add(comment);
            hasQueue = true;
        }
        return this;
    }

    /**
     * Resets the tab counter to zero and calls the newLine() method.
     */
    @Override
    public CodeWriter resetLine() {
        indentNo = 0;
        newLine();
        return this;
    }

    @Override
    public void setCompilationUnit(CompilationUnit compilationUnit) {
        this.compilationUnit = compilationUnit;
    }

    /**
     * Writes a single space.
     */
    @Override
    public CodeWriter space() {
        out.print(' ');
        colNo++;
        isLineNew = false;
        return this;
    }

    /**
     * Writes a boolean.
     */
    @Override
    public CodeWriter write(boolean b) {
        // print it
        out.print(b);
        // add if 4:'true' or 5:'false'
        colNo += b ? 4 : 5;
        isLineNew = false;
        return this;
    }

    /**
     * Writes a single character.
     */
    @Override
    public CodeWriter write(char c) {
        // print the char
        out.print(c);
        // add one
        colNo++;
        isLineNew = false;
        return this;
    }

    /**
     * Writes an array of characters.
     */
    @Override
    public CodeWriter write(char[] chars) {

        if (chars != null) {
            // print the chars;
            out.print(chars);
            // add
            colNo += chars.length;
            isLineNew = false;
        }
        return this;
    }

    /**
     * Writes an array of characters.
     */
    @Override
    public CodeWriter write(char[] chars, int off, int len) {
        if (chars != null) {
            // print the chars;
            out.write(chars, off, len);
            // add
            colNo += chars.length;
            isLineNew = false;
        }
        return this;
    }

    /**
     * Instead of calling the <code>Object.toString()</code> method, the
     * <code>Object.toCode(public CodeWriter)</code> method is invoked with
     * <code>this</code> as the argument.
     */
    @Override
    public CodeWriter write(Codeable ico) {
        if (ico != null) {
            ico.toCode(this);
        }
        return this;
    }

    /**
     * Iterates the array and sends each element to <code>write(Codeable)</code>
     * .
     */
    @Override
    public CodeWriter write(Codeable[] aico) {
        if (aico != null) {
            for (Codeable element : aico) {
                write(element);
            }
        }
        return this;
    }

    /**
     * Writes a double.
     */
    @Override
    public CodeWriter write(double d) {
        out.print(d);
        colNo += Double.toString(d).length();
        isLineNew = false;
        return this;
    }

    /**
     * Writes a float.
     */
    @Override
    public CodeWriter write(float f) {
        out.print(f);
        colNo += Float.toString(f).length();
        isLineNew = false;
        return this;
    }

    /**
     * Writes an integer.
     */
    @Override
    public CodeWriter write(int i) {
        out.print(i);
        colNo += Integer.toString(i).length();
        isLineNew = false;
        return this;
    }

    @Override
    public CodeWriter write(List<? extends Codeable> codeables) {
        if (codeables != null) {
            for (Codeable element : codeables) {
                write(element);
            }
        }
        return this;
    }

    /**
     * Writes an object.
     */
    @Override
    public CodeWriter write(Object o) {
        if (o != null) {
            if (o instanceof Codeable) {
                ((Codeable) o).toCode(this);
            } else {
                out.print(o);
                colNo += o.toString().length();
                isLineNew = false;
            }
        }
        return this;
    }

    /**
     * Writes a string.
     */
    @Override
    public CodeWriter write(String s) {
        if (s != null) {
            out.print(s);
            colNo += s.length();
            isLineNew = false;
        }

        return this;
    }

    private void flushQueue() {
        while (!queue.isEmpty()) {
            Codeable c = queue.remove(0);
            write(c);
        }
    }

    private void writeIndent() {
        int n = indentNo;
        while (n-- > 0) {
            out.print("    ");
        }
    }

}
