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
 * <code>Expression</code> subinterface for expressions which have a three
 * operands. The only ternary function in Java is the ternary if-else construct.
 */
public interface Ternary extends Expression {

    /**
     * Ternary function type for if-else: <code>(a ? b : c)</code>.
     */
    static final int IF = 1;

    /**
     * Accessor method for the first operand.
     */
    Expression getValue1();

    /**
     * Accessor method for the second operand.
     */
    Expression getValue2();

    /**
     * Accessor method for the third operand.
     */
    Expression getValue3();

    /**
     * Mutator method for the first operand.
     */
    Ternary setValue1(Expression e);

    /**
     * Mutator method for the second operand.
     */
    Ternary setValue2(Expression e);

    /**
     * Mutator method for the third operand.
     */
    Ternary setValue3(Expression e);

    /**
     * Returns the type of this binary function as one of the constants in this
     * interface.
     */
    int type();
}
