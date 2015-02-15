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

import java.util.List;

import net.sourceforge.jenesis4java.Codeable;
import net.sourceforge.jenesis4java.IVisitor;

public class VisitorUtils {

    public static <T extends Codeable> void visit(List<T> vs, Codeable parent, IVisitor visitor) {
        visitList(vs, parent, visitor);
    }

    private static <T extends Codeable> void visitList(List<T> vs, Codeable parent, IVisitor visitor) {
        for (int index = 0; vs != null && index < vs.size(); index++) {
            T element = visit(vs.get(index), parent, visitor);
            vs.set(index, element);
        }
    }

    public static Object visit(Object o, Codeable parent, IVisitor visitor) {
        if (o != null) {
            if (o.getClass().isArray()) {
                visitArray((Codeable[]) o, parent, visitor);
            } else if (o instanceof List) {
                visitList((List<? extends Codeable>) o, parent, visitor);
            } else {
                o = visitSingle((Codeable) o, parent, visitor);
            }
        }
        return o;
    }

    public static <T extends Codeable> T visit(T codeable, Codeable parent, IVisitor visitor) {
        return visitSingle(codeable, parent, visitor);
    }

    private static <T extends Codeable> T visitSingle(T current, Codeable parent, IVisitor visitor) {
        return (T) visitor.visitReplace(current, parent);
    }

    public static <T extends Codeable> void visit(T[] vs, Codeable parent, IVisitor visitor) {
        visitArray(vs, parent, visitor);
    }

    private static <T extends Codeable> void visitArray(T[] vs, Codeable parent, IVisitor visitor) {
        for (int index = 0; vs != null && index < vs.length; index++) {
            T element = visitSingle(vs[index], parent, visitor);
            vs[index] = element;
        }
    }
}
