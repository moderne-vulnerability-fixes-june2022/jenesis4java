package net.sourceforge.jenesis4java.impl;

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
            return this.type;
        }

        @Override
        public int getDims() {
            return this.dims;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            out.queue(this.comment);
            this.type.toCode(out);
            for (int i = 0; i < this.dims; i++) {
                out.write("[]");
            }
            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            this.type = VisitorUtils.visit(this.type, this, visitor);
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
            return this.pattern;
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
            return this.name;
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
        return this.type == Type.ARRAY;
    }

    public boolean isPrimitive() {
        return this.type != Type.CLASS && this.type != Type.ARRAY;
    }

    @Override
    public CodeWriter toCode(CodeWriter out) {
        out.queue(this.comment);
        boolean foundInImportedTypes = isImportedType(out);
        if (foundInImportedTypes) {
            out.write(this.name.substring(this.name.lastIndexOf('.') + 1));
        } else {
            out.write(this.name);
        }
        return out;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public int type() {
        return this.type;
    }

    @Override
    public void visit(IVisitor visitor) {
        super.visit(visitor);
    }

    private boolean isImportedType(CodeWriter out) {
        if (this.name.indexOf('.') > 0) {
            if (this.name.startsWith(MType.JAVA_LANG_PACKAGE)) {
                return true;
            }
            if (out.getCompilationUnit() != null) {
                for (Import importedType : out.getCompilationUnit().getImports()) {
                    if (importedType.getName().equals(this.name)) {
                        return true;
                    }
                    if (importedType.getName().endsWith(".*")) {
                        String packageName = this.name.substring(0, this.name.lastIndexOf('.') + 1);
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
