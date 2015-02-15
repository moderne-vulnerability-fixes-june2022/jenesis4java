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
 * <code>Expression</code> subinterface for expressions which have left and
 * right operands.
 */
public interface Binary extends Expression {

    /**
     * Binary function type for logical and: {@code (a && b)}.
     */
    static final int LAND = 1;

    /**
     * Binary function type for logical or: {@code (a || b)}.
     */
    static final int LOR = 2;

    /**
     * Binary function type for bitwise and: {@code (a & b)}.
     */
    static final int BAND = 3;

    /**
     * Binary function type for bitwise or: {@code (a | b)}.
     */
    static final int BOR = 4;

    /**
     * Binary function type for bitwise xor: {@code (a ^ b)}.
     */
    static final int XOR = 5;

    /**
     * Binary function type for bitwise left shift: {@code (a << b)}.
     */
    static final int LEFT = 6;

    /**
     * Binary function type for bitwise right shift: {@code (a >> b)}.
     */
    static final int RIGHT = 7;

    /**
     * Binary function type for bitwise unsigned right shift: {@code (a >>> b)}.
     */
    static final int UNSIGNED = 8;

    /**
     * Binary function type for arithmetic addition: <code>(a +
     * b)</code>.
     */
    static final int ADD = 9;

    /**
     * Binary function type for arithmetix subtraction: <code>(a -
     * b)</code>.
     */
    static final int SUB = 10;

    /**
     * Binary function type for arithmetic multiplication: <code>(a *
     * b)</code>.
     */
    static final int MUL = 11;

    /**
     * Binary function type for arithmetic division: <code>(a /
     * b)</code>.
     */
    static final int DIV = 12;

    /**
     * Binary function type for arithmetic modulus: <code>(a %
     * b)</code>.
     */
    static final int MOD = 13;

    /**
     * Binary function type for predicate equals: <code>(a ==
     * b)</code>.
     */
    static final int EQ = 14;

    /**
     * Binary function type for predicate not equals: {@code (a != b)}.
     */
    static final int NE = 15;

    /**
     * Binary function type for predicate greater than: {@code (a > b)}.
     */
    static final int GT = 16;

    /**
     * Binary function type for compund predicate greater than or equal:
     * {@code (a => b)}.
     */
    static final int GTE = 17;

    /**
     * Binary function type for predicate less than: {@code (a < b)}.
     */
    static final int LT = 18;

    /**
     * Binary function type for compound predicate less than or equal:
     * {@code (a <= b)}.
     */
    static final int LTE = 19;

    /**
     * Binary function type for class equality: <code>(a instanceof
     * b)</code>.
     */
    static final int IOF = 20;

    /**
     * Binary function type for string concatenation: <code>(a +
     * b)</code>.
     */
    static final int CAT = 21;

    /**
     * Binary function type for arithmetic addition: <code>(a =
     * b)</code>.
     */
    static final int ASSIGN = 22;

    /**
     * Getter method for the left side.
     */
    Expression getLValue();

    /**
     * Getter method for the right side.
     */
    Expression getRValue();

    /**
     * Setter method for the left side.
     */
    Binary setLValue(Expression e);

    /**
     * Setter method for the right side.
     */
    Binary setRValue(Expression e);

    /**
     * Returns the type of this binary function as one of the constants in this
     * interface.
     */
    int type();
}
