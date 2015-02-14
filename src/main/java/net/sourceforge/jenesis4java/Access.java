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
 * Classes that implement this interface are access controllable according to
 * the Java Language Specification. These include classes, interfaces, fields,
 * methods, and constructors.
 */
public interface Access extends Codeable {

    enum AccessType {
        NONE(null),
        PUBLIC("public"),
        PACKAGE(null),
        PROTECTED("protected"),
        PRIVATE("private");

        static public CodeWriter toCode(AccessType accesstype, CodeWriter out) {
            if (accesstype == null) {
                return PACKAGE.toCode(out);
            } else {
                return accesstype.toCode(out);
            }
        }

        private final String accessString;

        AccessType(String accessString) {
            this.accessString = accessString != null ? accessString + " " : null;
        }

        public CodeWriter toCode(CodeWriter out) {
            if (this.accessString != null) {
                out.write(this.accessString);
            }
            return out;
        }

    }

    /**
     * Constant to indicate public access.
     */
    static final AccessType PUBLIC = AccessType.PUBLIC;

    /**
     * Constant to indicate package access.
     */
    static final AccessType PACKAGE = AccessType.PACKAGE;

    /**
     * Constant to indicate protected access.
     */
    static final AccessType PROTECTED = AccessType.PROTECTED;

    /**
     * Constant to indicate private access.
     */
    static final AccessType PRIVATE = AccessType.PRIVATE;

    /**
     * Gets the access level as one of the constants in this interface.
     */
    AccessType getAccess();

    /**
     * Sets the access level according to the given constant.
     */
    Access setAccess(AccessType level);
}
