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
 * <code>Declaration</code> subinterface for other declaration structures which
 * may belong to a class / interface. These include fields, methods, and inner
 * classes.
 */
public interface Member extends Access, Declaration {

    Member addAnnotation(Annotation annotation);

    Annotation addAnnotation(String text);

    Annotation addAnnotation(String name, String text);

    List<Annotation> getAnnotations();

    /**
     * Gets the identifier for this member.
     */
    String getName();

    /**
     * Accessor method for the isFinal flag.
     */
    boolean isFinal();

    /**
     * Mutator method for the isFinal flag.
     */
    Member isFinal(boolean value);

    /**
     * Accessor method for the isStatic flag.
     */
    boolean isStatic();

    /**
     * Mutator method for the isStatic flag.
     */
    Member isStatic(boolean value);

    /**
     * Sets the identifier for this member.
     */
    Member setName(String identifier);
}
