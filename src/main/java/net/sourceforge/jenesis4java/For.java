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
 * <code>Statement</code> subinterface for the <code>for</code> construct. It
 * has an declaration statement, a conditional statement, and an update
 * statement (all of which are optional).
 */
public interface For extends ConditionalStatement {

    /**
     * Adds this statement expression to the comma separated list of
     * initialization expressions.
     */
    For addInit(Expression expr);

    /**
     * Adds this declaration statement to the list of updates.
     */
    For addUpdate(Expression update);

    /**
     * Gets the comma separated list of initialzers from this for statement as
     * an List of <code>Expression</code> OR a single <code>Let</code> object
     * (depending on how it was defined). One need check the RTTI to be sure.
     */
    List<Codeable> getInits();

    /**
     * Gets the comma separated list of updates from this for statement as an
     * List of <code>Expression</code>.
     */
    List<Codeable> getUpdates();

    /**
     * Sets this declaration statement to a <code>Let</code> statement with the
     * given <code>Type</code>. If any initialization expressions have
     * previously been added through the <code>addInit(Expression)</code>
     * method, they will all be overwritten. This is because the Java Language
     * Specification allows the initialization section of the for statement to
     * be a <code>Let</code> expression.
     */
    Let setInit(Type type);
}
