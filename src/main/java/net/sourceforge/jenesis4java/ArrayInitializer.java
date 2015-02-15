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
 * <code>Expression</code> subinterface for array initializers. The array
 * initialization expression has a few interesting features. Notice, the
 * argument is <code>Object</code>. The contract is that the <code>Object</code>
 * must be of runtime-type <code>Expression</code> or, more likely,
 * <code>Object[]</code>. If the type is <code>Object[]</code>, then each
 * element of that array will recursively be introspected, checking again to see
 * if the type is <code>Expression</code> or <code>Object[]</code>. In this
 * manner, one can arbitratily nest expressions. This is the only way I know how
 * to accomplish this behavior (ie not knowing the dimensionality of an argument
 * at compile-time).
 * <P>
 * For example, say we wanted to make the array:
 * 
 * <PRE>
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * int[][] aai = {
 *     {
 *         1,
 *         2
 *     },
 *     {
 *         3,
 *         4
 *     }
 * };
 * </PRE>
 * 
 * One could do this by:
 * 
 * <PRE>
 * Expression[] a1 = new Expression[2];
 * a1[0] = new IntLiteralImpl(1);
 * a1[1] = new IntLiteralImpl(2);
 * Expression[] a2 = new Expression[2];
 * a2[0] = new IntLiteralImpl(3);
 * a2[1] = new IntLiteralImpl(4);
 * Object[] a3 = new Object[2];
 * a3[0] = a1;
 * a3[1] = a2;
 * ArrayDeclarationStatement asd = new ArrayDeclarationStatementImpl(); // fictional
 * // implementation
 * asd.setInitialization(a3);
 * </PRE>
 */
public interface ArrayInitializer extends Expression {

    /**
     * Gets the array initialization expressions.
     */
    Object getArgs();

    /**
     * Sets the array initialization expressions as an arbitrarily nested array
     * of <code>Expression[]</code> <i>or</i> as a single
     * <code>Expression</code>.
     */
    ArrayInitializer setArgs(Object o);
}
