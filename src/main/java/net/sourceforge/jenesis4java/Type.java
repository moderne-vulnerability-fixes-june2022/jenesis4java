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
 * The <code>Type</code> superinterface.
 */
public interface Type extends Codeable {

    /**
     * Constant for the <code>void</code> type.
     */
    static final int VOID = -1;

    /**
     * Constant for the <code>null</code> type.
     */
    static final int NULL = 0;

    /**
     * Constant for the <code>boolean</code> type.
     */
    static final int BOOLEAN = 1;

    /**
     * Constant for the <code>byte</code> type.
     */
    static final int BYTE = 2;

    /**
     * Constant for the <code>short</code> type.
     */
    static final int SHORT = 3;

    /**
     * Constant for the <code>int</code> type.
     */
    static final int INT = 4;

    /**
     * Constant for the <code>long</code> type.
     */
    static final int LONG = 5;

    /**
     * Constant for the <code>float</code> type.
     */
    static final int FLOAT = 6;

    /**
     * Constant for the <code>double</code> type.
     */
    static final int DOUBLE = 7;

    /**
     * Constant for the <code>char</code> type.
     */
    static final int CHAR = 8;

    /**
     * Constant for the <code>class</code> type.
     */
    static final int CLASS = 9;

    /**
     * Constant for the <code>array</code> type.
     */
    static final int ARRAY = 10;

    /**
     * Returns <code>true</code> if this is an array, <code>false</code>
     * otherwise.
     */
    boolean isArray();

    /**
     * Returns <code>true</code> if this is one of the primitive types,
     * <code>false</code> otherwise.
     */
    boolean isPrimitive();

    /**
     * Returns the integer type as one of the constants in the type interface.
     */
    int type();
}
