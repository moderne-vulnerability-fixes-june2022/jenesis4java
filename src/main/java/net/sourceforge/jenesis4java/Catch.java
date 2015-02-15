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

import java.util.List;

/**
 * <code>Statement</code> subinterface for the <code>catch</code> construct in a
 * <code>try</code>.
 */
public interface Catch extends Block {

    /**
     * Gets the formal parameter for this catch clause. if more than one formal
     * parameter has been declared, only the first one is returned.
     */
    FormalParameter getThrowable();

    /**
     * Sets the formal parameter for this catch clause.<br>
     * Overwrites the current formal parameter(s).
     */
    Catch setThrowable(Type type, String name);

    /**
     * Returns an immutable list containing the formal parameters of this catch
     * clause.
     * 
     * @return an immutable list containing the formal parameters for this catch
     *         clause.
     */
    List<FormalParameter> getThrowables();

    /**
     * Adds a formal parameter to this catch clause.
     * 
     * @param type
     * @param name
     * @return this catch clause instance.
     */
    Catch addThrowable(Type type, String name);
}
