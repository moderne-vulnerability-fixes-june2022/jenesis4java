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
 * The <code>Block</code> superinterface. A <code>Block</code> is a structure
 * that holds Statements and typically delimited by braces. <code>Block</code>
 * acts as a factory for all <code>Statement</code> objects.
 */
public interface Block extends Codeable {

    /**
     * Gets the list of statements as an list of <code>Statement</code>.
     */
    List<Statement> getStatements();

    /**
     * insert the expression at the specified index.
     * 
     * @param index
     * @param expression
     */
    void insertStatement(int index, Expression expression);

    /**
     * insert the statement at the specified index.
     * 
     * @param index
     * @param statement
     */
    void insertStatement(int index, Statement statement);

    /**
     * Adds a new <code>Break</code> statement to this block and returns it.
     */
    Break newBreak();

    /**
     * Adds a new <code>Continue</code> statement to this block and returns it.
     */
    Continue newContinue();

    /**
     * Adds a new <code>Let</code> statement to declaration section of this
     * block for the given type and returns it.
     */
    Let newDeclarationLet(Type type);

    /**
     * Adds a new <code>DoWhile</code> statement to this block and returns it.
     */
    DoWhile newDoWhile(Expression predicate);

    /**
     * Adds a new <code>Empty</code> statement to this block and returns it.
     */
    Empty newEmpty();

    /**
     * Adds a new <code>For</code> statement to this block and returns it.
     */
    For newFor();

    /**
     * Adds a new <code>If</code> statement to this block and returns it.
     */
    If newIf(Expression predicate);

    /**
     * Adds a new <code>Let</code> statement to this block for the given type
     * and returns it.
     */
    Let newLet(Type type);

    /**
     * Adds a new <code>LocalBlock</code> statement to this block and returns
     * it.
     */
    LocalBlock newLocalBlock();

    /**
     * Adds a new <code>LocalClass</code> statement to this block and returns
     * it.
     */
    LocalClass newLocalClass(String name);

    /**
     * Adds a new <code>Return</code> statement to this block and returns it.
     */
    Return newReturn();

    /**
     * Adds a new <code>ExpressionStatement</code> statement to this block on
     * the given statement <code>Expression</code> and returns it.
     */
    ExpressionStatement newStmt(Expression expr);

    /**
     * Adds a new <code>Switch</code> statement to this block and returns it.
     */
    Switch newSwitch(Expression integer);

    /**
     * Adds a new <code>Synchronized</code> statement to this block and returns
     * it.
     */
    Synchronized newSynchronized(Expression mutex);

    /**
     * Adds a new <code>Throw</code> statement to this block and returns it.
     */
    Throw newThrow(Expression throwable);

    /**
     * Adds a new <code>Try</code> statement to this block and returns it.
     */
    Try newTry();

    /**
     * Adds a new <code>While</code> statement to this block and returns it.
     */
    While newWhile(Expression predicate);

    /**
     * removes a statement from the statement list.
     */
    void removeStmt(Statement statement);
}
