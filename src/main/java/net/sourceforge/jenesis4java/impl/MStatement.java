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
import java.util.List;

import net.sourceforge.jenesis4java.Assign;
import net.sourceforge.jenesis4java.Block;
import net.sourceforge.jenesis4java.Break;
import net.sourceforge.jenesis4java.Case;
import net.sourceforge.jenesis4java.Catch;
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
import net.sourceforge.jenesis4java.FormalParameter;
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
public abstract class MStatement extends MVM.MCodeable implements Statement {

    // ===============================================================
    // BLOCK STATEMENT
    // ===============================================================
    static class BlockStatement extends MStatement implements Block {

        List<Statement> vs;

        BlockStatement(MVM vm) {
            super(vm);
            this.vs = new ArrayList<Statement>();
        }

        @Override
        public List<Statement> getStatements() {
            return ListTypeSelector.select(this.vs);
        }

        @Override
        public void insertStatement(int index, Expression expression) {
            ExpressionStatement expressionStatement = new MStatement.MExpressionStatement((MVM) vm(), expression);
            this.vs.add(index, expressionStatement);
        }

        @Override
        public void insertStatement(int index, Statement statement) {
            this.vs.add(index, statement);

        }

        @Override
        public boolean isBlockWithAbruptCompletion() {
            return BlockUtil.isBlockWithAbruptCompletion(getStatements());
        }

        @Override
        public Break newBreak() {
            Break x = new MBreak(this.vm);
            this.vs.add(x);
            return x;
        }

        @Override
        public Continue newContinue() {
            Continue x = new MContinue(this.vm);
            this.vs.add(x);
            return x;
        }

        @Override
        public Let newDeclarationLet(Type type) {
            Let x = new MStatement.MLet(this.vm, type);
            int index = 0;
            for (; index < this.vs.size() && this.vs.get(index) instanceof Let; index++) {
                ;
            }
            this.vs.add(index, x);
            return x;
        }

        @Override
        public DoWhile newDoWhile(Expression predicate) {
            DoWhile x = new MDoWhile(this.vm, predicate);
            this.vs.add(x);
            return x;
        }

        @Override
        public Empty newEmpty() {
            Empty x = new MEmpty(this.vm);
            this.vs.add(x);
            return x;
        }

        @Override
        public For newFor() {
            For x = new MFor(this.vm);
            this.vs.add(x);
            return x;
        }

        @Override
        public If newIf(Expression predicate) {
            If x = new MIf(this.vm, predicate);
            this.vs.add(x);
            return x;
        }

        @Override
        public Let newLet(Type type) {
            Let x = new MLet(this.vm, type);
            this.vs.add(x);
            return x;
        }

        @Override
        public LocalBlock newLocalBlock() {
            LocalBlock x = new MLocalBlock(this.vm);
            this.vs.add(x);
            return x;
        }

        @Override
        public LocalClass newLocalClass(String name) {
            LocalClass x = new MDeclaration.MLocalClass(this.vm, name);
            this.vs.add(x);
            return x;
        }

        @Override
        public Return newReturn() {
            Return x = new MReturn(this.vm);
            this.vs.add(x);
            return x;
        }

        @Override
        public ExpressionStatement newStmt(Expression expr) {
            ExpressionStatement x = new MExpressionStatement(this.vm, expr);
            this.vs.add(x);
            return x;
        }

        @Override
        public Switch newSwitch(Expression integer) {
            Switch x = new MSwitch(this.vm, integer);
            this.vs.add(x);
            return x;
        }

        @Override
        public Synchronized newSynchronized(Expression mutex) {
            Synchronized x = new MSynchronized(this.vm, mutex);
            this.vs.add(x);
            return x;
        }

        @Override
        public Throw newThrow(Expression throwable) {
            Throw x = new MThrow(this.vm, throwable);
            this.vs.add(x);
            return x;
        }

        @Override
        public Try newTry() {
            Try x = new MTry(this.vm);
            this.vs.add(x);
            return x;
        }

        @Override
        public While newWhile(Expression predicate) {
            While x = new MWhile(this.vm, predicate);
            this.vs.add(x);
            return x;
        }

        @Override
        public void removeStmt(Statement statement) {
            if (this.vs != null) {
                this.vs.remove(statement);
            }
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            VisitorUtils.visit(this.vs, this, visitor);
        }

        protected void writeBlock(CodeWriter out, BlockStyle style) {
            // write it
            style.toCode(out, getStatements());
        }
    }

    // ===============================================================
    // GOTO STATMENTS
    // ===============================================================
    abstract static class GotoStatement extends MStatement {

        String kwd;

        String target;

        GotoStatement(MVM vm, String kwd) {
            super(vm);
            this.kwd = kwd;
        }

        public String getGoto() {
            return this.target;
        }

        public GotoStatement setGoto(String target) {
            this.target = target;
            return this;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);

            out.write(this.kwd);

            if (this.target != null) {
                out.space().write(this.target);
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
            out.write("case ").write(this.e).write(':');

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
            return this.e;
        }

        @Override
        public MConditionalStatement setPredicate(Expression e) {
            this.e = e;
            return this;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            this.e = VisitorUtils.visit(this.e, this, visitor);
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
            writeBlock(out, this.vm.getStyle("default"));
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
            writeBlock(out, this.vm.getStyle("do"));
            out.write("while (").write(this.e).write(");");
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
            writeBlock(out, this.vm.getStyle("else"));
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
            out.write("else if (").write(this.e).write(')');
            writeBlock(out, this.vm.getStyle("else-if"));
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
            return this.e;
        }

        public MExpressionableStatement setExpression(Expression e) {
            this.e = e;
            return this;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            this.e = VisitorUtils.visit(this.e, this, visitor);
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
            return this.e;
        }

        @Override
        public MExpressionStatement setExpression(Expression e) {
            this.e = e;
            return this;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);

            out.write(this.e).write(';');

            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            this.e = VisitorUtils.visit(this.e, this, visitor);
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
            writeBlock(out, this.vm.getStyle("finally"));
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
            this.vi = new ArrayList<Codeable>();
            this.vu = new ArrayList<Codeable>();
        }

        @Override
        public MFor addInit(Expression e) {
            this.vi.add(e);
            return this;
        }

        @Override
        public MFor addUpdate(Expression e) {
            this.vu.add(e);
            return this;
        }

        @Override
        public List<Codeable> getInits() {
            return ListTypeSelector.select(this.vi);
        }

        @Override
        public List<Codeable> getUpdates() {
            return ListTypeSelector.select(this.vu);
        }

        @Override
        public Let setInit(Type type) {
            Let let = new MForInitLet(this.vm, type);
            this.vi.add(let);
            return let;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);
            out.write("for (");
            write(this.vi, out);
            out.write(';');
            if (this.e != null) {
                out.space().write(this.e);
            }
            out.write(';');
            if (this.vu.size() > 0) {
                out.space();
                write(this.vu, out);
            }
            out.write(')');
            writeBlock(out, this.vm.getStyle("for"));
            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            VisitorUtils.visit(this.vi, this, visitor);
            VisitorUtils.visit(this.vu, this, visitor);
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
            out.queue(this.comment);

            if (this.isFinal) {
                out.write("final").space();
            }

            out.write(this.type).space();

            for (int i = 0; i < this.v.size(); i++) {
                if (i > 0) {
                    out.write(',').space();
                }
                out.write(this.v.get(i));
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
            this.vcei = new ArrayList<ElseIf>();
        }

        @Override
        public Else getElse() {
            if (this._else == null) {
                this._else = new MElse(this.vm);
            }
            return this._else;
        }

        @Override
        public List<ElseIf> getElseIfs() {
            return ListTypeSelector.select(this.vcei);
        }

        @Override
        public ElseIf newElseIf(Expression e) {
            ElseIf ei = new MElseIf(this.vm, e);
            this.vcei.add(ei);
            return ei;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);
            out.write("if (").write(this.e).write(')');
            writeBlock(out, this.vm.getStyle("if"));
            codeElseIfs(this.vcei, out);
            if (this._else != null && this._else.getStatements().size() > 0) {
                out.write(this._else);
            }
            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            VisitorUtils.visit(this.vcei, this, visitor);
            this._else = VisitorUtils.visit(this._else, this, visitor);
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
            this.v = new ArrayList<Assign>();
        }

        @Override
        public MLet addAssign(Assign assign) {
            this.v.add(assign);
            return this;
        }

        @Override
        public MLet addAssign(String name, Expression expr) {
            this.v.add(new MExpression.MAssign(this.vm, Assign.S, new MExpression.MVariable(this.vm, name), expr));
            return this;
        }

        @Override
        public List<Assign> getAssigns() {
            return ListTypeSelector.select(this.v);
        }

        @Override
        public Type getType() {
            return this.type;
        }

        @Override
        public boolean isFinal() {
            return this.isFinal;
        }

        @Override
        public MLet isFinal(boolean value) {
            this.isFinal = value;
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

            if (this.isFinal) {
                out.write("final").space();
            }

            out.write(this.type).space();

            for (int i = 0; i < this.v.size(); i++) {
                if (i > 0) {
                    out.write(',').space();
                }
                out.write(this.v.get(i));
            }

            out.write(';');

            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            this.type = VisitorUtils.visit(this.type, this, visitor);
            VisitorUtils.visit(this.v, this, visitor);
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
            writeBlock(out, this.vm.getStyle("local-class"));
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

            if (this.e != null) {
                out.space().write(this.e);
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
            return ListTypeSelector.select(this.vs, Case.class);
        }

        @Override
        public Default getDefault() {
            if (this.d == null) {
                this.d = new MDefault(this.vm);
            }
            return this.d;
        }

        @Override
        public Case newCase(Expression constant) {
            Case c = new MCase(this.vm, constant);
            this.vs.add(c);
            return c;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            if (!this.triggered) {
                // add in the default if its not null
                if (this.d != null) {
                    this.vs.add(this.d);
                }
                this.triggered = true;
            }

            super.toCode(out);
            out.write("switch (").write(this.e).write(')');
            writeBlock(out, this.vm.getStyle("switch"));
            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            this.d = VisitorUtils.visit(this.d, this, visitor);
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
            return this.mutex;
        }

        @Override
        public MSynchronized setMutex(Expression mutex) {
            this.mutex = mutex;
            return this;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);
            out.write("synchronized ").write('(').write(this.mutex).write(')');
            writeBlock(out, this.vm.getStyle("synchronized"));
            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            this.mutex = VisitorUtils.visit(this.mutex, this, visitor);
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
            return this.e;
        }

        @Override
        public MThrow setThrowable(Expression e) {
            this.e = e;
            return this;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);

            out.write("throw ").write(this.e).write(';');

            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            this.e = VisitorUtils.visit(this.e, this, visitor);
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
            out.write("while (").write(this.e).write(')');
            writeBlock(out, this.vm.getStyle("while"));
            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    String label;

    public MStatement(MVM vm) {
        super(vm);
    }

    @Override
    public Comment comment(String text) {
        Comment sl = new MComment.MSingleLineComment(this.vm, text);
        this.comment = sl;
        return sl;
    }

    @Override
    public String getLabel() {
        return this.label;
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
        out.write(this.comment);

        // make a new line if this one is not new
        if (!out.isLineNew()) {
            out.newLine();
        }

        // and the label, if there is one.
        if (this.label != null) {
            out.write(this.label).write(':').space();
        }

        return out;
    }

    @Override
    public void visit(IVisitor visitor) {
        super.visit(visitor);
    }
}
