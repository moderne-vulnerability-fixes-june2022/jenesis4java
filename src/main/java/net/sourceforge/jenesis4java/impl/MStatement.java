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
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jenesis4java.Assign;
import net.sourceforge.jenesis4java.Block;
import net.sourceforge.jenesis4java.Break;
import net.sourceforge.jenesis4java.Case;
import net.sourceforge.jenesis4java.CodeWriter;
import net.sourceforge.jenesis4java.Codeable;
import net.sourceforge.jenesis4java.Comment;
import net.sourceforge.jenesis4java.ConditionalStatement;
import net.sourceforge.jenesis4java.Continue;
import net.sourceforge.jenesis4java.Default;
import net.sourceforge.jenesis4java.DoWhile;
import net.sourceforge.jenesis4java.Else;
import net.sourceforge.jenesis4java.ElseIf;
import net.sourceforge.jenesis4java.Empty;
import net.sourceforge.jenesis4java.Expression;
import net.sourceforge.jenesis4java.ExpressionStatement;
import net.sourceforge.jenesis4java.Finally;
import net.sourceforge.jenesis4java.For;
import net.sourceforge.jenesis4java.IVisitor;
import net.sourceforge.jenesis4java.If;
import net.sourceforge.jenesis4java.Let;
import net.sourceforge.jenesis4java.LocalBlock;
import net.sourceforge.jenesis4java.LocalClass;
import net.sourceforge.jenesis4java.Return;
import net.sourceforge.jenesis4java.Statement;
import net.sourceforge.jenesis4java.Switch;
import net.sourceforge.jenesis4java.Synchronized;
import net.sourceforge.jenesis4java.Throw;
import net.sourceforge.jenesis4java.Try;
import net.sourceforge.jenesis4java.Type;
import net.sourceforge.jenesis4java.While;
import net.sourceforge.jenesis4java.impl.util.BlockStyle;
import net.sourceforge.jenesis4java.impl.util.BlockUtil;
import net.sourceforge.jenesis4java.impl.util.ListTypeSelector;
import net.sourceforge.jenesis4java.impl.util.VisitorUtils;

/**
 * Standard <code>Statement</code> implementations.
 */
abstract class MStatement extends MVM.MCodeable implements Statement {

    // ===============================================================
    // BLOCK STATEMENT
    // ===============================================================
    static class BlockStatement extends MStatement implements Block {

        List<Statement> vs;

        BlockStatement(MVM vm) {
            super(vm);
            vs = new ArrayList<Statement>();
        }

        @Override
        public List<Statement> getStatements() {
            return ListTypeSelector.select(vs);
        }

        @Override
        public void insertStatement(int index, Expression expression) {
            ExpressionStatement expressionStatement = new MStatement.MExpressionStatement((MVM) vm(), expression);
            vs.add(index, expressionStatement);
        }

        @Override
        public void insertStatement(int index, Statement statement) {
            vs.add(index, statement);

        }

        @Override
        public boolean isBlockWithAbruptCompletion() {
            return BlockUtil.isBlockWithAbruptCompletion(getStatements());
        }

        @Override
        public Break newBreak() {
            Break x = new MBreak(vm);
            vs.add(x);
            return x;
        }

        @Override
        public Continue newContinue() {
            Continue x = new MContinue(vm);
            vs.add(x);
            return x;
        }

        @Override
        public Let newDeclarationLet(Type type) {
            Let x = new MStatement.MLet(vm, type);
            int index = 0;
            while (index < vs.size() && vs.get(index) instanceof Let) {
                index++;
            }
            vs.add(index, x);
            return x;
        }

        @Override
        public DoWhile newDoWhile(Expression predicate) {
            DoWhile x = new MDoWhile(vm, predicate);
            vs.add(x);
            return x;
        }

        @Override
        public Empty newEmpty() {
            Empty x = new MEmpty(vm);
            vs.add(x);
            return x;
        }

        @Override
        public For newFor() {
            For x = new MFor(vm);
            vs.add(x);
            return x;
        }

        @Override
        public If newIf(Expression predicate) {
            If x = new MIf(vm, predicate);
            vs.add(x);
            return x;
        }

        @Override
        public Let newLet(Type type) {
            Let x = new MLet(vm, type);
            vs.add(x);
            return x;
        }

        @Override
        public LocalBlock newLocalBlock() {
            LocalBlock x = new MLocalBlock(vm);
            vs.add(x);
            return x;
        }

        @Override
        public LocalClass newLocalClass(String name) {
            LocalClass x = new MDeclaration.MLocalClass(vm, name);
            vs.add(x);
            return x;
        }

        @Override
        public Return newReturn() {
            Return x = new MReturn(vm);
            vs.add(x);
            return x;
        }

        @Override
        public ExpressionStatement newStmt(Expression expr) {
            ExpressionStatement x = new MExpressionStatement(vm, expr);
            vs.add(x);
            return x;
        }

        @Override
        public Switch newSwitch(Expression integer) {
            Switch x = new MSwitch(vm, integer);
            vs.add(x);
            return x;
        }

        @Override
        public Synchronized newSynchronized(Expression mutex) {
            Synchronized x = new MSynchronized(vm, mutex);
            vs.add(x);
            return x;
        }

        @Override
        public Throw newThrow(Expression throwable) {
            Throw x = new MThrow(vm, throwable);
            vs.add(x);
            return x;
        }

        @Override
        public Try newTry() {
            Try x = new MTry(vm);
            vs.add(x);
            return x;
        }

        @Override
        public While newWhile(Expression predicate) {
            While x = new MWhile(vm, predicate);
            vs.add(x);
            return x;
        }

        @Override
        public void removeStmt(Statement statement) {
            if (vs != null) {
                vs.remove(statement);
            }
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            VisitorUtils.visit(vs, this, visitor);
        }

        void writeBlock(CodeWriter out, BlockStyle style) {
            // write it
            style.toCode(out, getStatements());
        }
    }

    // ===============================================================
    // GOTO STATMENTS
    // ===============================================================
    abstract static class GotoStatement extends MStatement {

        final String kwd;

        String target;

        GotoStatement(MVM vm, String kwd) {
            super(vm);
            this.kwd = kwd;
        }

        public String getGoto() {
            return target;
        }

        public GotoStatement setGoto(String target) {
            this.target = target;
            return this;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);

            out.write(kwd);

            if (target != null) {
                out.space().write(target);
            }

            return out.write(';');
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    // ===============================================================
    // BREAK STATEMENT
    // ===============================================================
    static class MBreak extends GotoStatement implements Break {

        MBreak(MVM vm) {
            super(vm, "break");
        }

        @Override
        public MBreak setGoto(String target) {
            super.setGoto(target);
            return this;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    // ===============================================================
    // CASE STATEMENT
    // ===============================================================
    static class MCase extends MConditionalStatement implements Case {

        MCase(MVM vm, Expression expr) {
            super(vm);
            setPredicate(expr);
        }

        @Override
        public Expression getConstant() {
            return getPredicate();
        }

        @Override
        public MCase setConstant(Expression expr) {
            setPredicate(expr);
            return this;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);
            out.write("case ").write(e).write(':');

            out.indentLine();
            out.write(getStatements());
            out.dedentLine();
            // writeBlock(out, this.vm.getStyle("case"));
            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    // ===============================================================
    // CONDITIONAL STATEMENT
    // ===============================================================
    abstract static class MConditionalStatement extends BlockStatement implements ConditionalStatement {

        Expression e;

        public MConditionalStatement(MVM vm) {
            super(vm);
        }

        public MConditionalStatement(MVM vm, Expression e) {
            super(vm);
            this.e = e;
        }

        @Override
        public Expression getPredicate() {
            return e;
        }

        @Override
        public MConditionalStatement setPredicate(Expression e) {
            this.e = e;
            return this;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            e = VisitorUtils.visit(e, this, visitor);
        }
    }

    // ===============================================================
    // CONTINUE STATEMENT
    // ===============================================================
    static class MContinue extends GotoStatement implements Continue {

        MContinue(MVM vm) {
            super(vm, "continue");
        }

        @Override
        public MContinue setGoto(String target) {
            super.setGoto(target);
            return this;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    // ===============================================================
    // DEFAULT STATEMENT
    // ===============================================================
    static class MDefault extends BlockStatement implements Default {

        MDefault(MVM vm) {
            super(vm);
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);
            out.write("default:");
            writeBlock(out, vm.getStyle("default"));
            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    // ===============================================================
    // DO WHILE STATEMENT
    // ===============================================================
    static class MDoWhile extends MConditionalStatement implements DoWhile {

        public MDoWhile(MVM vm, Expression e) {
            super(vm, e);
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);
            out.write("do");
            writeBlock(out, vm.getStyle("do"));
            out.write("while (").write(e).write(");");
            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    // ===============================================================
    // ELSE STATEMENT
    // ===============================================================
    static class MElse extends BlockStatement implements Else {

        MElse(MVM vm) {
            super(vm);
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);
            out.write("else");
            writeBlock(out, vm.getStyle("else"));
            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    // ===============================================================
    // ELSE IF STATEMENT
    // ===============================================================
    static class MElseIf extends MConditionalStatement implements ElseIf {

        MElseIf(MVM vm, Expression e) {
            super(vm, e);
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);
            out.write("else if (").write(e).write(')');
            writeBlock(out, vm.getStyle("else-if"));
            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    // ===============================================================
    // EMPTY STATEMENT
    // ===============================================================
    static class MEmpty extends MStatement implements Empty {

        MEmpty(MVM vm) {
            super(vm);
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);
            out.write(';');
            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    // ===============================================================
    // EXPRESSIONABLE STATEMENT
    // ===============================================================
    abstract static class MExpressionableStatement extends MStatement {

        Expression e;

        MExpressionableStatement(MVM vm) {
            super(vm);
        }

        public Expression getExpression() {
            return e;
        }

        public MExpressionableStatement setExpression(Expression e) {
            this.e = e;
            return this;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            e = VisitorUtils.visit(e, this, visitor);
        }
    }

    // ===============================================================
    // EXPRESSION STATEMENT
    // ===============================================================
    static class MExpressionStatement extends MExpressionableStatement implements ExpressionStatement {

        Expression e;

        MExpressionStatement(MVM vm) {
            super(vm);
        }

        MExpressionStatement(MVM vm, Expression e) {
            super(vm);
            this.e = e;
        }

        @Override
        public Expression getExpression() {
            return e;
        }

        @Override
        public MExpressionStatement setExpression(Expression e) {
            this.e = e;
            return this;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);

            out.write(e).write(';');

            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            e = VisitorUtils.visit(e, this, visitor);
        }
    }

    // ===============================================================
    // CATCH STATEMENT
    // ===============================================================
    static class MFinally extends BlockStatement implements Finally {

        MFinally(MVM vm) {
            super(vm);
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);
            out.write("finally");
            writeBlock(out, vm.getStyle("finally"));
            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    // ===============================================================
    // FOR STATEMENT
    // ===============================================================
    static class MFor extends MConditionalStatement implements For {

        List<Codeable> vi;

        List<Codeable> vu;

        public MFor(MVM vm) {
            super(vm);
            vi = new ArrayList<Codeable>();
            vu = new ArrayList<Codeable>();
        }

        @Override
        public MFor addInit(Expression e) {
            vi.add(e);
            return this;
        }

        @Override
        public MFor addUpdate(Expression e) {
            vu.add(e);
            return this;
        }

        @Override
        public List<Codeable> getInits() {
            return ListTypeSelector.select(vi);
        }

        @Override
        public List<Codeable> getUpdates() {
            return ListTypeSelector.select(vu);
        }

        @Override
        public Let setInit(Type type) {
            Let let = new MForInitLet(vm, type);
            vi.add(let);
            return let;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);
            out.write("for (");
            write(vi, out);
            out.write(';');
            if (e != null) {
                out.space().write(e);
            }
            out.write(';');
            if (vu.size() > 0) {
                out.space();
                write(vu, out);
            }
            out.write(')');
            writeBlock(out, vm.getStyle("for"));
            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            VisitorUtils.visit(vi, this, visitor);
            VisitorUtils.visit(vu, this, visitor);
        }

        private void write(List<Codeable> v, CodeWriter out) {
            for (int i = 0; i < v.size(); i++) {
                if (i > 0) {
                    out.write(", ");
                }
                out.write(v.get(i));
            }
        }
    }

    // ===============================================================
    // LET STATEMENT
    // ===============================================================
    static class MForInitLet extends MLet {

        MForInitLet(MVM vm, Type type) {
            super(vm, type);
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            // queue up the comment
            out.queue(comment);

            if (isFinal) {
                out.write("final").space();
            }

            out.write(type).space();

            for (int i = 0; i < v.size(); i++) {
                if (i > 0) {
                    out.write(',').space();
                }
                out.write(v.get(i));
            }

            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    // ===============================================================
    // IF STATEMENT
    // ===============================================================
    static class MIf extends MConditionalStatement implements If {

        List<ElseIf> vcei;

        Else _else;

        MIf(MVM vm, Expression e) {
            super(vm, e);
            vcei = new ArrayList<ElseIf>();
        }

        @Override
        public Else getElse() {
            if (_else == null) {
                _else = new MElse(vm);
            }
            return _else;
        }

        @Override
        public List<ElseIf> getElseIfs() {
            return ListTypeSelector.select(vcei);
        }

        @Override
        public ElseIf newElseIf(Expression e) {
            ElseIf ei = new MElseIf(vm, e);
            vcei.add(ei);
            return ei;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);
            out.write("if (").write(e).write(')');
            writeBlock(out, vm.getStyle("if"));
            codeElseIfs(vcei, out);
            if (_else != null && _else.getStatements().size() > 0) {
                out.write(_else);
            }
            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            VisitorUtils.visit(vcei, this, visitor);
            _else = VisitorUtils.visit(_else, this, visitor);
        }

        private CodeWriter codeElseIfs(List<ElseIf> v, CodeWriter out) {
            for (ElseIf elseIf : v) {
                out.write(elseIf);
            }
            return out;
        }
    }

    // ===============================================================
    // LET STATEMENT
    // ===============================================================
    static class MLet extends MExpressionableStatement implements Let {

        Type type;

        boolean isFinal;

        List<Assign> v;

        MLet(MVM vm, Type type) {
            super(vm);
            this.type = type;
            v = new ArrayList<Assign>();
        }

        @Override
        public MLet addAssign(Assign assign) {
            v.add(assign);
            return this;
        }

        @Override
        public MLet addAssign(String name, Expression expr) {
            v.add(new MExpression.MAssign(vm, Assign.S, new MExpression.MVariable(vm, name), expr));
            return this;
        }

        @Override
        public List<Assign> getAssigns() {
            return ListTypeSelector.select(v);
        }

        @Override
        public Type getType() {
            return type;
        }

        @Override
        public boolean isFinal() {
            return isFinal;
        }

        @Override
        public MLet isFinal(boolean value) {
            isFinal = value;
            return this;
        }

        @Override
        public MLet setType(Type type) {
            this.type = type;
            return this;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);

            if (isFinal) {
                out.write("final").space();
            }

            out.write(type).space();

            for (int i = 0; i < v.size(); i++) {
                if (i > 0) {
                    out.write(',').space();
                }
                out.write(v.get(i));
            }

            out.write(';');

            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            type = VisitorUtils.visit(type, this, visitor);
            VisitorUtils.visit(v, this, visitor);
        }
    }

    // ===============================================================
    // LOCAL BLOCK STATEMENT
    // ===============================================================
    static class MLocalBlock extends BlockStatement implements LocalBlock {

        public MLocalBlock(MVM vm) {
            super(vm);
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);
            writeBlock(out, vm.getStyle("local-class"));
            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    // ===============================================================
    // RETURN STATEMENT
    // ===============================================================
    static class MReturn extends MExpressionableStatement implements Return {

        MReturn(MVM vm) {
            super(vm);
        }

        @Override
        public MReturn setExpression(Expression e) {
            super.setExpression(e);
            return this;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);

            out.write("return");

            if (e != null) {
                out.space().write(e);
            }

            out.write(';');

            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    // ===============================================================
    // SWITCH STATEMENT
    // ===============================================================
    static class MSwitch extends MConditionalStatement implements Switch {

        boolean triggered;

        Default d;

        MSwitch(MVM vm, Expression e) {
            super(vm, e);
        }

        @Override
        public List<Case> getCases() {
            return ListTypeSelector.select(vs, Case.class);
        }

        @Override
        public Default getDefault() {
            if (d == null) {
                d = new MDefault(vm);
            }
            return d;
        }

        @Override
        public Case newCase(Expression constant) {
            Case c = new MCase(vm, constant);
            vs.add(c);
            return c;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            if (!triggered) {
                // add in the default if its not null
                if (d != null) {
                    vs.add(d);
                }
                triggered = true;
            }

            super.toCode(out);
            out.write("switch (").write(e).write(')');
            writeBlock(out, vm.getStyle("switch"));
            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            d = VisitorUtils.visit(d, this, visitor);
        }
    }

    // ===============================================================
    // SYNCHRONIZED STATEMENT
    // ===============================================================
    static class MSynchronized extends BlockStatement implements Synchronized {

        Expression mutex;

        MSynchronized(MVM vm, Expression mutex) {
            super(vm);
            this.mutex = mutex;
        }

        @Override
        public Expression getMutex() {
            return mutex;
        }

        @Override
        public MSynchronized setMutex(Expression mutex) {
            this.mutex = mutex;
            return this;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);
            out.write("synchronized ").write('(').write(mutex).write(')');
            writeBlock(out, vm.getStyle("synchronized"));
            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            mutex = VisitorUtils.visit(mutex, this, visitor);
        }
    }

    // ===============================================================
    // THROW STATEMENT
    // ===============================================================
    static class MThrow extends MStatement implements Throw {

        Expression e;

        MThrow(MVM vm, Expression e) {
            super(vm);
            this.e = e;
        }

        @Override
        public Expression getThrowable() {
            return e;
        }

        @Override
        public MThrow setThrowable(Expression e) {
            this.e = e;
            return this;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);

            out.write("throw ").write(e).write(';');

            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            e = VisitorUtils.visit(e, this, visitor);
        }
    }

    // ===============================================================
    // WHILE STATEMENT
    // ===============================================================
    static class MWhile extends MConditionalStatement implements While {

        public MWhile(MVM vm, Expression e) {
            super(vm, e);
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);
            out.write("while (").write(e).write(')');
            writeBlock(out, vm.getStyle("while"));
            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    private String label;

    MStatement(MVM vm) {
        super(vm);
    }

    @Override
    public Comment comment(String text) {
        Comment sl = new MComment.MSingleLineComment(vm, text);
        comment = sl;
        return sl;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public boolean isAbruptCompletionStatement() {
        return this instanceof Return || //
                this instanceof Break || //
                this instanceof Throw || //
                this instanceof Continue;
    }

    @Override
    public MStatement setLabel(String label) {
        this.label = label;
        return this;
    }

    @Override
    public CodeWriter toCode(CodeWriter out) {
        // write the comment
        out.write(comment);

        // make a new line if this one is not new
        if (!out.isLineNew()) {
            out.newLine();
        }

        // and the label, if there is one.
        if (label != null) {
            out.write(label).write(':').space();
        }

        return out;
    }

    @Override
    public void visit(IVisitor visitor) {
        super.visit(visitor);
    }
}
