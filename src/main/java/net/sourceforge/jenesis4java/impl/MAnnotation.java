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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.jenesis4java.Annotation;
import net.sourceforge.jenesis4java.AnnotationAttribute;
import net.sourceforge.jenesis4java.CodeWriter;
import net.sourceforge.jenesis4java.Expression;
import net.sourceforge.jenesis4java.IVisitor;
import net.sourceforge.jenesis4java.impl.MVM.MCodeable;
import net.sourceforge.jenesis4java.impl.util.VisitorUtils;

public class MAnnotation extends MCodeable implements Annotation {

    static class MAnnotationAttribute extends MCodeable implements AnnotationAttribute {

        String name;

        Expression[] values;

        boolean isArray;

        Annotation[] valueAnnotations;

        MAnnotationAttribute(MVM vm) {
            super(vm);
        }

        @Override
        public MAnnotationAttribute addValue(Expression value) {
            Expression[] oldValue = this.values;
            if (oldValue == null) {
                oldValue = new Expression[0];
            }
            this.values = new Expression[oldValue.length + 1];
            System.arraycopy(oldValue, 0, this.values, 0, oldValue.length);
            this.values[oldValue.length] = value;
            return this;
        }

        @Override
        public MAnnotationAttribute addValueAnnotation(Annotation valueAnnotation) {
            Annotation[] oldValue = this.valueAnnotations;
            if (oldValue == null) {
                oldValue = new Annotation[0];
            }
            this.valueAnnotations = new Annotation[oldValue.length + 1];
            System.arraycopy(oldValue, 0, this.valueAnnotations, 0, oldValue.length);
            this.valueAnnotations[oldValue.length] = valueAnnotation;
            return this;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public Expression getValue() {
            if (this.values != null && this.values.length > 0) {
                return this.values[0];
            } else {
                return null;
            }
        }

        @Override
        public Annotation getValueAnnotation() {
            if (this.valueAnnotations != null && this.valueAnnotations.length > 0) {
                return this.valueAnnotations[0];
            } else {
                return null;
            }
        }

        @Override
        public Annotation[] getValueAnnotations() {
            return this.valueAnnotations;
        }

        @Override
        public Expression[] getValues() {
            return this.values;
        }

        @Override
        public boolean isArray() {
            return this.isArray || this.values != null && this.values.length > 1 || this.valueAnnotations != null && this.valueAnnotations.length > 1;
        }

        @Override
        public MAnnotationAttribute setArray(boolean isArray) {
            this.isArray = isArray;
            return this;
        }

        @Override
        public MAnnotationAttribute setName(String name) {
            this.name = name;
            return this;
        }

        @Override
        public MAnnotationAttribute setValue(Expression value) {
            this.values = new Expression[]{
                value
            };
            return this;
        }

        @Override
        public MAnnotationAttribute setValueAnnotation(Annotation valueAnnotation) {
            this.valueAnnotations = new Annotation[]{
                valueAnnotation
            };
            return this;
        }

        @Override
        public MAnnotationAttribute setValueAnnotations(Annotation[] valueAnnotations) {
            this.valueAnnotations = valueAnnotations;
            return this;
        }

        @Override
        public MAnnotationAttribute setValues(Expression[] values) {
            this.values = values;
            return this;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            if (!isArray() && this.valueAnnotations != null && this.valueAnnotations.length == 1) {
                out.indentLine();
            }
            if (this.name != null && this.name.trim().length() > 0) {
                out.write(this.name);
                out.write(" = ");
            }
            if (isArray()) {
                out.write('{');
                out.indentLine();
            }
            if (this.values != null) {
                for (int index = 0; index < this.values.length; index++) {
                    if (index > 0) {
                        out.write(", ");
                    }
                    out.write(this.values[index]);
                }
            } else if (this.valueAnnotations != null) {
                for (int index = 0; index < this.valueAnnotations.length; index++) {
                    if (index > 0) {
                        out.write(", ");
                        out.newLine();
                    }
                    out.write(this.valueAnnotations[index]);
                }
            }
            if (isArray()) {
                out.dedentLine();
                out.write('}');
            } else if (this.valueAnnotations != null && this.valueAnnotations.length == 1) {
                out.dedentLine();
            }
            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            VisitorUtils.visit(this.values, this, visitor);
            VisitorUtils.visit(this.valueAnnotations, this, visitor);
        }
    }

    String name;

    String text;

    List<AnnotationAttribute> annotationAttributes = new ArrayList<AnnotationAttribute>();

    MAnnotation(MVM vm, String text) {
        super(vm);
        this.text = text;
    }

    MAnnotation(MVM vm, String name, String text) {
        super(vm);
        this.name = name;
        this.text = text;
    }

    @Override
    public MAnnotation addAnntationAttribute(AnnotationAttribute annotation) {
        this.annotationAttributes.add(annotation);
        return this;
    }

    @Override
    public AnnotationAttribute addAnntationAttribute(String name) {
        MAnnotationAttribute attribute = new MAnnotationAttribute(this.vm);
        attribute.setName(name);
        addAnntationAttribute(attribute);
        return attribute;
    }

    @Override
    public AnnotationAttribute addAnntationAttribute(String name, Expression value) {
        AnnotationAttribute attribute = addAnntationAttribute(name);
        attribute.setValue(value);
        return attribute;
    }

    @Override
    public AnnotationAttribute getAnnotationAttribute(String name) {
        for (Object element : this.annotationAttributes) {
            AnnotationAttribute annotationAttribute = (AnnotationAttribute) element;
            if (annotationAttribute.getName().equals(name)) {
                return annotationAttribute;
            }
        }
        return null;
    }

    @Override
    public List<AnnotationAttribute> getAnnotationAttributes() {
        return this.annotationAttributes;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public MAnnotation setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public MAnnotation setText(String text) {
        this.text = text;
        return this;
    }

    @Override
    public CodeWriter toCode(CodeWriter out) {
        super.toCode(out);
        if (this.annotationAttributes.size() == 0 && (this.name == null || "".equals(this.name))) {
            out.write("@" + this.text);
        } else if (this.annotationAttributes.size() == 0) {
            out.write("@" + this.name + "(" + this.text + ")");
        } else {
            out.write("@");
            if (this.name != null && this.name.trim().length() > 0) {
                out.write(this.name);
            } else if (this.text != null && this.text.trim().length() > 0) {
                out.write(this.text);
            }
            out.write("(");
            for (Iterator<AnnotationAttribute> iterator = this.annotationAttributes.iterator(); iterator.hasNext();) {
                out.write(iterator.next());
                if (iterator.hasNext()) {
                    out.write(", ");
                }
            }
            out.write(")");
        }
        return out;
    }

    @Override
    public void visit(IVisitor visitor) {
        super.visit(visitor);
        VisitorUtils.visit(this.annotationAttributes,this, visitor);
    }
}
