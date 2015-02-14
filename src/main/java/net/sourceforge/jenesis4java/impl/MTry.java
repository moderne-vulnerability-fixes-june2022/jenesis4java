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
import net.sourceforge.jenesis4java.*;
import net.sourceforge.jenesis4java.impl.util.ListTypeSelector;
import net.sourceforge.jenesis4java.impl.util.VisitorUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Allows to create try statements (the standard as well as the so-called
 * try-with-resources variant).
 */
class MTry extends MStatement.BlockStatement implements Try {
    public static class MTryResource extends MVM.MCodeable implements TryResource {
        private Type type;

        private boolean isFinal;

        private Assign assign;

        public MTryResource(MVM vm, Type type, String name, Expression expr) {
            super(vm);
            this.type = type;
            this.assign = new MExpression.MAssign(this.vm, Assign.S, new MExpression.MVariable(this.vm, name), expr);
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);
            if (this.isFinal) {
                out.write("final").space();
            }
            out.write(this.type).space();
            out.write(assign);
            return out;
        }

        @Override
        public boolean isFinal() {
            return this.isFinal;
        }

        @Override
        public TryResource isFinal(boolean value) {
            this.isFinal = value;
            return this;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            this.type = VisitorUtils.visit(this.type, this, visitor);
            this.assign = VisitorUtils.visit(this.assign, this, visitor);
        }
    }

    private List<Catch> vcc;

    private List<MTryResource> resources;

    private Finally finallyClause;

    MTry(MVM vm) {
        super(vm);
        this.vcc = new ArrayList<Catch>();
    }

    @Override
    public List<Catch> getCatches() {
        return ListTypeSelector.select(this.vcc);
    }

    @Override
    public Finally getFinally() {
        if (this.finallyClause == null) {
            this.finallyClause = new MFinally(this.vm);
        }
        return this.finallyClause;
    }

    @Override
    public Catch newCatch(Type type, String name) {
        Catch cc = new MCatch(this.vm, type, name);
        this.vcc.add(cc);
        return cc;
    }

    @Override
    public TryResource newResource(Type type, String name, Expression expr) {
        if (resources == null) {
            resources = new ArrayList<MTryResource>(1);
        }
        MTryResource resource = new MTryResource(this.vm, type, name, expr);
        resources.add(resource);
        return resource;
    }

    @Override
    public CodeWriter toCode(CodeWriter out) {
        super.toCode(out);
        out.write("try");
        writeResources(out);
        writeBlock(out, this.vm.getStyle("try"));
        writeCatches(this.vcc, out);
        out.write(this.finallyClause);
        return out;
    }

    private void writeResources(CodeWriter out) {
        if (resources == null) {
            return;
        }
        int columnNumber = out.write("(").getColumnNumber();
        boolean isFirst = true;
        for (MTryResource resource : resources) {
            if (isFirst) {
                isFirst = false;
            } else {
                out.write(";").newLine();
                for (int i = 0; i < columnNumber; i++) {
                    out.space();
                }
            }
            resource.toCode(out);
        }
        out.write(")");
    }

    @Override
    public void visit(IVisitor visitor) {
        super.visit(visitor);
        if(this.resources != null) {
            VisitorUtils.visit(this.resources, this, visitor);
        }
        VisitorUtils.visit(this.vcc, this, visitor);
        this.finallyClause = VisitorUtils.visit(this.finallyClause, this, visitor);
    }

    // utility method
    private CodeWriter writeCatches(List<Catch> v, CodeWriter out) {
        for (Catch catch1 : v) {
            out.write(catch1);
        }
        return out;
    }
}
