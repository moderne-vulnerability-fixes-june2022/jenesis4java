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
import net.sourceforge.jenesis4java.impl.util.VisitorUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// ===============================================================
// CATCH STATEMENT
// ===============================================================
class MCatch extends MStatement.BlockStatement implements Catch {

    private List<FormalParameter> formalParameters;

    MCatch(MVM vm, Type type, String name) {
        super(vm);
        this.formalParameters = new ArrayList<FormalParameter>(1);
        formalParameters.add(createFormalParameter(type, name));
    }

    @Override
    public FormalParameter getThrowable() {
        return this.formalParameters.get(0);
    }

    @Override
    public MCatch setThrowable(Type type, String name) {
        this.formalParameters.clear();
        formalParameters.add(createFormalParameter(type, name));
        return this;
    }

    @Override
    public List<FormalParameter> getThrowables() {
        return Collections.unmodifiableList(formalParameters);
    }

    @Override
    public Catch addThrowable(Type type, String name) {
        formalParameters.add(createFormalParameter(type, name));
        return this;
    }

    @Override
    public CodeWriter toCode(CodeWriter out) {
        super.toCode(out);
        out.write("catch (");
        boolean first = true;
        for (FormalParameter fp : this.formalParameters) {
            if (first) {
                first = false;
            } else {
                out.write(" | ");
            }
            out.write(fp);
        }
        out.write(')');
        writeBlock(out, this.vm.getStyle("catch"));
        return out;
    }

    @Override
    public void visit(IVisitor visitor) {
        super.visit(visitor);
        for (FormalParameter fp : this.formalParameters) {
            fp = VisitorUtils.visit(fp, this, visitor);
        }
    }

    private MDeclaration.MFormalParameter createFormalParameter(Type type, String name) {
        return new MDeclaration.MFormalParameter(this.vm, type, name);
    }
}
