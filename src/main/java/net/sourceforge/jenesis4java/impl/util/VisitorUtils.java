package net.sourceforge.jenesis4java.impl.util;

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
