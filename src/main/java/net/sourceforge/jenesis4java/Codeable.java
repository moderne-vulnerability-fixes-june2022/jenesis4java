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
 * <code>Codeable</code> is a core interface which nearly all other interfaces
 * extend. Objects that implement this interface are expected to know how to
 * render themselves as source code to a <code>CodeWriter</code>. Any
 * <code>Codeable</code> is be commentable as well.
 */
public interface Codeable {

    /**
     * @return this casted to the parameter class.
     */
    <T> T cast(Class<T> clazz);

    /**
     * Gets the comment for this <code>Codeable</code> object.
     */
    Comment getComment();

    boolean isBlockWithAbruptCompletion();

    /**
     * Sets the comment to the given type and text.
     */
    Codeable setComment(int type, String text);

    /**
     * Renders the codeable object to the given writer and returns it.
     */
    CodeWriter toCode(CodeWriter out);

    /**
     * walk through the code tree and allow every element to be exchanged.
     * 
     * @param visitor
     */
    void visit(IVisitor visitor);

    /**
     * Returns the virtual machine to which this <code>Codeable</code> object
     * belongs.
     */
    VirtualMachine vm();
}
