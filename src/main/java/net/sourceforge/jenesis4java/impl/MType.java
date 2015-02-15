package net.sourceforge.jenesis4java.impl;

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
import java.util.Calendar;

import net.sourceforge.jenesis4java.ArrayType;
import net.sourceforge.jenesis4java.CalendarClassType;
import net.sourceforge.jenesis4java.ClassType;
import net.sourceforge.jenesis4java.CodeWriter;
import net.sourceforge.jenesis4java.IVisitor;
import net.sourceforge.jenesis4java.Import;
import net.sourceforge.jenesis4java.PrimitiveType;
import net.sourceforge.jenesis4java.Type;
import net.sourceforge.jenesis4java.impl.util.VisitorUtils;

/**
 * Standard <code>Type</code> implementations.
 */
public abstract class MType extends MVM.MCodeable {

    /* =============================================================== */
    /* ARRAY TYPE */
    /* =============================================================== */
    static class MArrayType extends MType implements ArrayType {

        Type type;

        int dims;

        public MArrayType(MVM vm, Type type, int dims) {
            super(vm, Type.ARRAY, type.toString());
            this.type = type;
            this.dims = dims;
        }

        @Override
        public Type getComponentType() {
            return type;
        }

        @Override
        public int getDims() {
            return dims;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            out.queue(comment);
            type.toCode(out);
            for (int i = 0; i < dims; i++) {
                out.write("[]");
            }
            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            type = VisitorUtils.visit(type, this, visitor);
        }
    }

    static class MCalendarClassType extends MType implements ClassType, CalendarClassType {

        private final String pattern;

        public MCalendarClassType(MVM vm, String pattern) {
            super(vm, Type.CLASS, Calendar.class.getName());
            this.pattern = pattern;
        }

        @Override
        public String getName() {
            return Calendar.class.getName();
        }

        @Override
        public String getPattern() {
            return pattern;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    /* =============================================================== */
    /* CLASS TYPE */
    /* =============================================================== */
    static class MClassType extends MType implements ClassType {

        MClassType(MVM vm, String name) {
            super(vm, Type.CLASS, name);
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    /* =============================================================== */
    /* PRIMITIVE TYPE */
    /* =============================================================== */
    static class MPrimitiveType extends MType implements PrimitiveType {

        MPrimitiveType(MVM vm, int type, String name) {
            super(vm, type, name);
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    private static final String JAVA_LANG_PACKAGE = "java.lang.";

    int type;

    String name;

    public MType(MVM vm, int type, String name) {
        super(vm);
        this.type = type;
        this.name = name;
    }

    public boolean isArray() {
        return type == Type.ARRAY;
    }

    public boolean isPrimitive() {
        return type != Type.CLASS && type != Type.ARRAY;
    }

    @Override
    public CodeWriter toCode(CodeWriter out) {
        out.queue(comment);
        boolean foundInImportedTypes = isImportedType(out);
        if (foundInImportedTypes) {
            out.write(name.substring(name.lastIndexOf('.') + 1));
        } else {
            out.write(name);
        }
        return out;
    }

    @Override
    public String toString() {
        return name;
    }

    public int type() {
        return type;
    }

    @Override
    public void visit(IVisitor visitor) {
        super.visit(visitor);
    }

    private boolean isImportedType(CodeWriter out) {
        if (name.indexOf('.') > 0) {
            if (name.startsWith(MType.JAVA_LANG_PACKAGE)) {
                return true;
            }
            if (out.getCompilationUnit() != null) {
                for (Import importedType : out.getCompilationUnit().getImports()) {
                    if (importedType.getName().equals(name)) {
                        return true;
                    }
                    if (importedType.getName().endsWith(".*")) {
                        String packageName = name.substring(0, name.lastIndexOf('.') + 1);
                        if ((packageName + "*").equals(importedType.getName())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
