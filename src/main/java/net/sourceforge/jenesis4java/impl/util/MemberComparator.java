package net.sourceforge.jenesis4java.impl.util;

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

import java.io.Serializable;

import net.sourceforge.jenesis4java.ClassDeclaration;
import net.sourceforge.jenesis4java.Constructor;
import net.sourceforge.jenesis4java.Field;
import net.sourceforge.jenesis4java.Interface;
import net.sourceforge.jenesis4java.Member;
import net.sourceforge.jenesis4java.Method;
import net.sourceforge.jenesis4java.StaticInitializer;

/**
 * Comparator which is used to sort <code>Members</code> in a class.
 */
public class MemberComparator implements java.util.Comparator<Member>, Serializable {

    private static final long serialVersionUID = 2419912166943712377L;

    private static final int CONSTANT = 3;

    private static final int INTERFACE = 4;

    private static final int CONSTRUCTOR = 5;

    private static final int METHOD = 6;

    private static final int FIELD = 7;

    private static final int CLASS = 8;

    private static final int STATIC_INIT = 9;

    private static final int OTHER = 10;

    private static final int EQUAL = 0;

    private static final int LESS = -1;

    private static final int GREATER = 1;

    public MemberComparator() {
    }

    @Override
    public int compare(Member o1, Member o2) {
        // assign an integer for the compared types
        int i1 = assignInt(o1);
        int i2 = assignInt(o2);

        // are they equal?...
        if (i1 == i2) {

            // if so, we will fork to the appropriate type
            // identity comparison method
            switch (i1) {
                case CLASS:
                    return compareClasses((ClassDeclaration) o1, (ClassDeclaration) o2);
                case METHOD:
                    return compareMethods((Method) o1, (Method) o2);
                case FIELD:
                    return compareFields((Field) o1, (Field) o2);
                case CONSTRUCTOR:
                    return compareConstructors((Constructor) o1, (Constructor) o2);
                case INTERFACE:
                    return compareInterfaces((Interface) o1, (Interface) o2);
                default:
                    return MemberComparator.EQUAL;
            }

            // the types are not the same,
            // and therefore we can compare directly
        } else {
            return i1 < i2 ? MemberComparator.LESS : MemberComparator.GREATER;
        }
    }

    private int assignInt(Object o) {
        if (o instanceof Interface) {
            return MemberComparator.INTERFACE;
        } else if (o instanceof Constructor) {
            return MemberComparator.CONSTRUCTOR;
        } else if (o instanceof Method) {
            return MemberComparator.METHOD;
        } else if (o instanceof Field) {
            // Special case. See if it's static and final (a
            // constant).
            Field f = (Field) o;
            return f.isStatic() && f.isFinal() ? MemberComparator.CONSTANT : MemberComparator.FIELD;
        } else if (o instanceof ClassDeclaration) {
            return MemberComparator.CLASS;
        } else if (o instanceof StaticInitializer) {
            return MemberComparator.STATIC_INIT;
        } else {
            return MemberComparator.OTHER;
        }
    }

    /**
     * utility method to compare boolean state. If they are different, the first
     * boolean arg (o1) is deemed GREATER if it is the true one.
     */
    private int compareBooleans(boolean b1, boolean b2) {
        // if they are different, return the comparison, otw equal
        if (b1 ^ b2) {
            return b1 ? MemberComparator.GREATER : MemberComparator.LESS;
        } else {
            return MemberComparator.EQUAL;
        }
    }

    private int compareClasses(ClassDeclaration o1, ClassDeclaration o2) {
        int x = MemberComparator.EQUAL;

        // compare abstract state
        x = compareBooleans(o1.isAbstract(), o2.isAbstract());
        if (x != MemberComparator.EQUAL) {
            return x;
        }

        return compareMembers(o1, o2);
    }

    private int compareConstructors(Constructor o1, Constructor o2) {
        int x = MemberComparator.EQUAL;

        // compare access levels
        x = compareInts(o1.getAccess().ordinal(), o2.getAccess().ordinal());
        if (x != MemberComparator.EQUAL) {
            return x;
        }

        // ctors have same acces, same # of ctors. We could continue
        // and compare the arguments, but naw...
        return x;
    }

    private int compareFields(Field o1, Field o2) {
        return compareMembers(o1, o2);
    }

    private int compareInterfaces(Interface o1, Interface o2) {
        int x = MemberComparator.EQUAL;

        // compare name state
        x = compareStrings(o1.getName(), o2.getName());
        if (x != MemberComparator.EQUAL) {
            return x;
        }

        // pretty damn close enough
        return x;
    }

    /**
     * utility method to compare integer state. If they are different, the they
     * are compared with GREATER deemed to the larger number.
     */
    private int compareInts(int i1, int i2) {
        if (i1 == i2) {
            return MemberComparator.EQUAL;
        } else {
            return i1 < i2 ? MemberComparator.LESS : MemberComparator.GREATER;
        }
    }

    private int compareMembers(Member o1, Member o2) {
        int x = MemberComparator.EQUAL;

        // compare access levels
        x = compareInts(o1.getAccess().ordinal(), o2.getAccess().ordinal());
        if (x != MemberComparator.EQUAL) {
            return x;
        }

        // compare static state
        x = compareBooleans(o1.isStatic(), o2.isStatic());
        if (x != MemberComparator.EQUAL) {
            return x;
        }

        // compare final state
        x = compareBooleans(o1.isFinal(), o2.isFinal());
        if (x != MemberComparator.EQUAL) {
            return x;
        }

        // compare name state
        x = compareStrings(o1.getName(), o2.getName());
        if (x != MemberComparator.EQUAL) {
            return x;
        }

        // pretty damn close enough
        return x;
    }

    private int compareMethods(Method o1, Method o2) {
        return compareMembers(o1, o2);
    }

    /**
     * utility method to compare strings. uses jdk1.2's compareTo method. 'does
     * s1 compared to s2', not the other way around.
     */
    private int compareStrings(String s1, String s2) {
        // compare strings
        return s1.compareTo(s2);
    }
}
