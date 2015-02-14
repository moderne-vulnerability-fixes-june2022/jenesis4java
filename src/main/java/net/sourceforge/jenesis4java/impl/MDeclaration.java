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
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.jenesis4java.AbstractMethod;
import net.sourceforge.jenesis4java.Access;
import net.sourceforge.jenesis4java.Access.AccessType;
import net.sourceforge.jenesis4java.Annotation;
import net.sourceforge.jenesis4java.Break;
import net.sourceforge.jenesis4java.ClassDeclaration;
import net.sourceforge.jenesis4java.ClassField;
import net.sourceforge.jenesis4java.ClassMethod;
import net.sourceforge.jenesis4java.ClassType;
import net.sourceforge.jenesis4java.CodeWriter;
import net.sourceforge.jenesis4java.Codeable;
import net.sourceforge.jenesis4java.Comment;
import net.sourceforge.jenesis4java.CompilationUnit;
import net.sourceforge.jenesis4java.Constant;
import net.sourceforge.jenesis4java.Constructor;
import net.sourceforge.jenesis4java.Continue;
import net.sourceforge.jenesis4java.Declaration;
import net.sourceforge.jenesis4java.DoWhile;
import net.sourceforge.jenesis4java.DocumentationComment;
import net.sourceforge.jenesis4java.Empty;
import net.sourceforge.jenesis4java.Expression;
import net.sourceforge.jenesis4java.ExpressionStatement;
import net.sourceforge.jenesis4java.Field;
import net.sourceforge.jenesis4java.For;
import net.sourceforge.jenesis4java.FormalParameter;
import net.sourceforge.jenesis4java.IVisitor;
import net.sourceforge.jenesis4java.If;
import net.sourceforge.jenesis4java.Import;
import net.sourceforge.jenesis4java.InitializationDeclaration;
import net.sourceforge.jenesis4java.InnerClass;
import net.sourceforge.jenesis4java.InnerInterface;
import net.sourceforge.jenesis4java.Interface;
import net.sourceforge.jenesis4java.Let;
import net.sourceforge.jenesis4java.LocalBlock;
import net.sourceforge.jenesis4java.LocalClass;
import net.sourceforge.jenesis4java.Member;
import net.sourceforge.jenesis4java.Method;
import net.sourceforge.jenesis4java.Namespace;
import net.sourceforge.jenesis4java.PackageClass;
import net.sourceforge.jenesis4java.Return;
import net.sourceforge.jenesis4java.Statement;
import net.sourceforge.jenesis4java.StaticInitializer;
import net.sourceforge.jenesis4java.Switch;
import net.sourceforge.jenesis4java.Synchronized;
import net.sourceforge.jenesis4java.Throw;
import net.sourceforge.jenesis4java.Try;
import net.sourceforge.jenesis4java.Type;
import net.sourceforge.jenesis4java.TypeDeclaration;
import net.sourceforge.jenesis4java.While;
import net.sourceforge.jenesis4java.impl.util.BlockStyle;
import net.sourceforge.jenesis4java.impl.util.BlockUtil;
import net.sourceforge.jenesis4java.impl.util.ListTypeSelector;
import net.sourceforge.jenesis4java.impl.util.MemberComparator;
import net.sourceforge.jenesis4java.impl.util.VisitorUtils;

/**
 * Standard <code>Declaration</code> implementations.
 */
abstract class MDeclaration extends MVM.MCodeable {

    // ===============================================================
    // BLOCK DECLARATION
    // ===============================================================
    static class BlockDeclaration extends MMember {

        List<Statement> vs;

        BlockDeclaration(MVM vm) {
            super(vm);
            this.vs = new ArrayList<Statement>();
        }

        @Override
        public List<Statement> codeableList() {
            return getStatements();
        }

        public List<Statement> getStatements() {
            return ListTypeSelector.select(this.vs, Statement.class);
        }

        public void insertStatement(int index, Expression expression) {
            ExpressionStatement expressionStatement = new MStatement.MExpressionStatement((MVM) vm(), expression);
            this.vs.add(index, expressionStatement);
        }

        public void insertStatement(int index, Statement statement) {
            this.vs.add(index, statement);

        }

        @Override
        public boolean isBlockWithAbruptCompletion() {
            return BlockUtil.isBlockWithAbruptCompletion(getStatements());
        }

        public Break newBreak() {
            Break x = new MStatement.MBreak(this.vm);
            this.vs.add(x);
            return x;
        }

        public Continue newContinue() {
            Continue x = new MStatement.MContinue(this.vm);
            this.vs.add(x);
            return x;
        }

        public Let newDeclarationLet(Type type) {
            Let x = new MStatement.MLet(this.vm, type);
            int index = 0;
            for (; index < this.vs.size() && this.vs.get(index) instanceof Let; index++) {
                ;
            }
            this.vs.add(index, x);
            return x;
        }

        public DoWhile newDoWhile(Expression e) {
            DoWhile x = new MStatement.MDoWhile(this.vm, e);
            this.vs.add(x);
            return x;
        }

        public Empty newEmpty() {
            Empty x = new MStatement.MEmpty(this.vm);
            this.vs.add(x);
            return x;
        }

        public For newFor() {
            For x = new MStatement.MFor(this.vm);
            this.vs.add(x);
            return x;
        }

        public If newIf(Expression e) {
            If x = new MStatement.MIf(this.vm, e);
            this.vs.add(x);
            return x;
        }

        public Let newLet(Type type) {
            Let x = new MStatement.MLet(this.vm, type);
            this.vs.add(x);
            return x;
        }

        public LocalBlock newLocalBlock() {
            LocalBlock x = new MStatement.MLocalBlock(this.vm);
            this.vs.add(x);
            return x;
        }

        public LocalClass newLocalClass(String name) {
            LocalClass x = new MLocalClass(this.vm, name);
            this.vs.add(x);
            return x;
        }

        public Return newReturn() {
            Return x = new MStatement.MReturn(this.vm);
            this.vs.add(x);
            return x;
        }

        public ExpressionStatement newStmt(Expression expr) {
            ExpressionStatement x = new MStatement.MExpressionStatement(this.vm, expr);
            this.vs.add(x);
            return x;
        }

        public Switch newSwitch(Expression e) {
            Switch x = new MStatement.MSwitch(this.vm, e);
            this.vs.add(x);
            return x;
        }

        public Synchronized newSynchronized(Expression e) {
            Synchronized x = new MStatement.MSynchronized(this.vm, e);
            this.vs.add(x);
            return x;
        }

        public Throw newThrow(Expression e) {
            Throw x = new MStatement.MThrow(this.vm, e);
            this.vs.add(x);
            return x;
        }

        public Try newTry() {
            Try x = new MTry(this.vm);
            this.vs.add(x);
            return x;
        }

        public While newWhile(Expression e) {
            While x = new MStatement.MWhile(this.vm, e);
            this.vs.add(x);
            return x;
        }

        public void removeStmt(Statement statement) {
            this.vs.remove(statement);
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            VisitorUtils.visit(this.vs, this, visitor);
        }
    }

    // ===============================================================
    // ABSTRACT METHOD DECLARATION
    // ===============================================================
    static class MAbstractMethod extends MMethod implements AbstractMethod {

        MAbstractMethod(MVM vm, Type type, String name) {
            super(vm, type, name);
            this.isAbstract = true;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);

            AccessType.toCode(this.access, out);

            writeTypeParametersAndThrow(this.name, this.type, this.parameters, this.throwz, out);

            out.write(';');

            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }

    }

    // ===============================================================
    // CLASS DECLARATION
    // ===============================================================
    static class MClassDeclaration extends MTypeDeclaration implements ClassDeclaration {

        String ex;

        boolean isAbstract;

        List<String> implementations;

        MClassDeclaration(MVM vm, MCompilationUnit unit) {
            super(vm, unit);
            this.implementations = new ArrayList<String>();
        }

        @Override
        public MClassDeclaration addImplements(String type) {
            this.implementations.add(type);
            return this;
        }

        @Override
        public Import addImport(Class<?> clazz) {
            return addImport(clazz.getName());
        }

        @Override
        public Import addImport(String name) {
            return getUnit().addImport(name);
        }

        @Override
        public ClassType asClassType() {
            return vm().newType(getUnit().getNamespace().getName() + "." + getName());
        }

        @Override
        public List<Constructor> getConstructors() {
            return ListTypeSelector.select(this.members, Constructor.class);
        }

        @Override
        public String getExtends() {
            return this.ex;
        }

        @Override
        public List<ClassField> getFields() {
            return ListTypeSelector.select(this.members, ClassField.class);
        }

        @Override
        public List<String> getImplements() {
            return ListTypeSelector.select(this.implementations);
        }

        public List<InitializationDeclaration> getInitializations() {
            return ListTypeSelector.select(this.members, InitializationDeclaration.class);
        }

        @Override
        public List<InnerClass> getInnerClasses() {
            return ListTypeSelector.select(this.members, InnerClass.class);
        }

        @Override
        public List<Member> getMembers() {
            return ListTypeSelector.select(this.members, Member.class);
        }

        @Override
        public List<ClassMethod> getMethods() {
            return ListTypeSelector.select(this.members, ClassMethod.class);
        }

        @Override
        public List<ClassMethod> getMethods(String methodName) {
            List<ClassMethod> list = new ArrayList<ClassMethod>();
            for (ClassMethod method : getMethods()) {
                if (method.getName().equals(methodName)) {
                    list.add(method);
                }
            }
            return list;
        }

        @Override
        public List<StaticInitializer> getStaticInitializers() {
            return ListTypeSelector.select(this.members, StaticInitializer.class);
        }

        @Override
        public boolean isAbstract() {
            return this.isAbstract;
        }

        @Override
        public MClassDeclaration isAbstract(boolean value) {
            this.isAbstract = value;
            return this;
        }

        @Override
        public boolean isMethodAlreadyDefined(Type type, String name) {
            for (ClassMethod cm : getMethods()) {
                if (cm.getName().equals(name) && cm.getType().equals(type)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public Constructor newConstructor() {
            Constructor x = new MConstructor(this.vm, this.name);
            this.members.add(x);
            return x;
        }

        @Override
        public ClassField newField(Class<?> type, String name) {
            if (!type.getName().startsWith("java.lang.")) {
                Class<?> typeToImport = type;
                if (typeToImport.isArray()) {
                    typeToImport = typeToImport.getComponentType();
                }
                if (typeToImport != null) {
                    addImport(typeToImport);
                }
            }
            return newField(this.vm.newType(type.getSimpleName()), name);
        }

        @Override
        public ClassField newField(Type type, String name) {
            ClassField x = new MClassField(this.vm, type, name);
            this.members.add(x);
            return x;
        }

        @Override
        public InnerClass newInnerClass(String name) {
            InnerClass x = new MInnerClass(this.vm, this.unit, this, name);
            this.members.add(x);
            return x;
        }

        @Override
        public InnerInterface newInnerInterface(String name) {
            MInnerInterface x = new MInnerInterface(this.vm, this.unit, this, name);
            this.members.add(x);
            return x;
        }

        @Override
        public ClassMethod newMethod(String name) {
            return newMethod(vm().newType(Type.VOID), name);
        }

        @Override
        public ClassMethod newMethod(Type type, String name) {
            ClassMethod x = new MClassMethod(this.vm, type, name);
            this.members.add(x);
            return x;
        }

        @Override
        public StaticInitializer newStaticInitializer() {
            StaticInitializer x = new MStaticInitializer(this.vm);
            this.members.add(x);
            return x;
        }

        @Override
        public MClassDeclaration setExtends(String ex) {
            this.ex = ex;
            return this;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);

            AccessType.toCode(this.access, out);

            if (this.isAbstract) {
                out.write("abstract ");
            }
            if (this.isStatic) {
                out.write("static ");
            }
            if (this.isFinal) {
                out.write("final ");
            }

            // write the class and name
            out.write("class ").write(this.name);

            // write the exnteds clause if ther is one
            if (this.ex != null) {
                out.newLine().write("extends ").write(this.ex);
            }

            // write the implements clause if there are any
            if (this.implementations.size() > 0) {
                out.newLine().write("implements ");
                for (int i = 0; i < this.implementations.size(); i++) {
                    if (i > 0) {
                        out.write(", ");
                    }
                    out.write(this.implementations.get(i));
                }
            }
            writeBlock(out, this.vm.getStyle("class"));
            return out;
        }

        @Override
        public boolean removeImplements(String interfaceName) {
            return this.implementations.remove(interfaceName);
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }

    }

    // ===============================================================
    // CLASS FIELD DECLARATION
    // ===============================================================
    static class MClassField extends MField implements ClassField {

        // the transient flag
        boolean isTransient;

        // the volatile flag
        boolean isVolatile;

        public MClassField(MVM vm, Type type, String name) {
            super(vm, type, name);
        }

        @Override
        public boolean isTransient() {
            return this.isTransient;
        }

        @Override
        public MClassField isTransient(boolean isTransient) {
            this.isTransient = isTransient;
            return this;
        }

        @Override
        public boolean isVolatile() {
            return this.isVolatile;
        }

        @Override
        public MClassField isVolatile(boolean isVolatile) {
            this.isVolatile = isVolatile;
            return this;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);
            AccessType.toCode(this.access, out);

            if (this.isTransient) {
                out.write("transient ");
            }
            if (this.isVolatile) {
                out.write("volatile ");
            }
            if (this.isStatic) {
                out.write("static ");
            }
            if (this.isFinal) {
                out.write("final ");
            }

            // write the class and name
            out.write(this.type).space().write(this.name);

            if (this.expr != null) {
                out.write(" = ").write(this.expr);
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
    // CLASS METHOD DECLARATION
    // ===============================================================
    static class MClassMethod extends MMethod implements ClassMethod {

        boolean isNative;

        boolean isSynchronized;

        MClassMethod(MVM vm, Type type, String name) {
            super(vm, type, name);
        }

        @Override
        public boolean isNative() {
            return this.isNative;
        }

        @Override
        public MClassMethod isNative(boolean isNative) {
            this.isNative = isNative;
            return this;
        }

        @Override
        public boolean isSynchronized() {
            return this.isSynchronized;
        }

        @Override
        public MClassMethod isSynchronized(boolean isSynchronized) {
            this.isSynchronized = isSynchronized;
            return this;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);
            AccessType.toCode(this.access, out);

            if (this.isAbstract) {
                out.write("abstract ");
            }
            if (this.isNative) {
                out.write("native ");
            }
            if (this.isSynchronized) {
                out.write("synchronized ");
            }
            if (this.isStatic) {
                out.write("static ");
            }
            if (this.isFinal) {
                out.write("final ");
            }

            writeTypeParametersAndThrow(this.name, this.type, this.parameters, this.throwz, out);
            if (this.isAbstract) {
                out.write(";");
            } else {
                writeBlock(out, this.vm.getStyle("method"));
            }

            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }

    }

    // ===============================================================
    // COMPILATION UNIT DECLARATION
    // ===============================================================
    static class MCompilationUnit extends MDeclaration implements CompilationUnit {

        boolean isEncoded; // flag to track if we have encoded yet.

        List<TypeDeclaration> types;

        List<MImport> imports;

        String codebase;

        Namespace namespace;

        MCompilationUnit(MVM vm, String codebase) {
            super(vm);
            this.codebase = codebase;
            this.types = new ArrayList<TypeDeclaration>();
            this.imports = new ArrayList<MImport>();
        }

        @Override
        public Import addImport(Class<?> clazz) {
            return addImport(clazz.getName());
        }

        @Override
        public Import addImport(String name) {
            if (name == null || name.trim().isEmpty()) {
                return null;
            }
            for (MImport im : this.imports) {
                if (im.isClassnamePartOfImport(name)) {
                    return im;
                }
                if (im.isClassNameAlreadyUsed(name)) {
                    System.err.println("ERROR: import conflict:\n" + im + "already defined and trying to add\n" + name);
                    return null;
                }
            }
            MImport x = new MImport(this.vm, name);
            this.imports.add(x);
            return x;
        }

        @Override
        public MCompilationUnit encode() throws java.io.IOException {
            // have we already called this method?
            if (this.isEncoded) {
                return this;
            }
            this.vm.getEncoder().encode(this);
            this.isEncoded = true;
            return this;
        }

        @Override
        public String getCodebase() {
            return this.codebase;
        }

        public String getFileName() {
            // make the fileName of the target file as the concatenation
            // of the sourcepath, the package name, and the className,
            // and the ending dot java
            StringBuffer file = null;

            // was a path specified?
            if (this.codebase != null && this.codebase.length() > 0) {
                // make the buffer
                file = new StringBuffer(this.codebase);
                // does the codebase end with a slash?
                if (!this.codebase.endsWith(Character.toString(File.separatorChar))) {
                    // make it so...
                    file.append(File.separatorChar);
                }
            } else {
                file = new StringBuffer();
            }

            // append to the filepath if exists
            if (this.namespace != null) {
                // fetch the package name
                String pkg = this.namespace.getName();
                if (pkg != null) {
                    // append the package name
                    file.append(pkg.replace('.', java.io.File.separatorChar));
                }
            }

            // add a separator, the top level name, and the extention
            String topLevelTypeName = getTopLevelType().getName();
            int indexOfGeneric = topLevelTypeName.indexOf('<');
            if (indexOfGeneric > 0) {
                topLevelTypeName = topLevelTypeName.substring(0, indexOfGeneric).trim();
            }
            file.append(java.io.File.separatorChar).append(topLevelTypeName).append(".java");

            // done
            return file.toString();
        }

        @Override
        public List<Import> getImports() {
            return ListTypeSelector.generalize(this.imports, Import.class);
        }

        @Override
        public Namespace getNamespace() {
            if (this.namespace == null) {
                this.namespace = new MNamespace(this.vm, null);
            }
            return this.namespace;
        }

        @Override
        public PackageClass getPackageClass(String name) {
            if (name == null) {
                return null;
            }
            for (PackageClass pc : ListTypeSelector.select(getTypes(), PackageClass.class)) {
                if (pc.getName().equals(name)) {
                    return pc;
                }
            }
            return null;
        }

        @Override
        public TypeDeclaration getTopLevelType() {
            return this.types.isEmpty() ? null : (TypeDeclaration) this.types.get(0);
        }

        @Override
        public List<TypeDeclaration> getTypes() {
            return ListTypeSelector.select(this.types, TypeDeclaration.class);
        }

        @Override
        public PackageClass newClass(String name) {
            PackageClass x = new MPackageClass(this.vm, this);
            x.setName(name);
            this.types.add(x);
            return x;
        }

        @Override
        public Interface newInterface(String name) {
            Interface x = new MInterface(this.vm, this);
            x.setName(name);
            this.types.add(x);
            return x;
        }

        @Override
        public PackageClass newPublicClass(String name) {
            return newClass(name)//
                    .setAccess(AccessType.PUBLIC)//
                    .cast(PackageClass.class);
        }

        @Override
        public Interface newPublicInterface(String name) {
            return newInterface(name)//
                    .setAccess(AccessType.PUBLIC)//
                    .cast(Interface.class);
        }

        @Override
        public Namespace setNamespace(String name) {
            this.namespace = new MNamespace(this.vm, name);
            return this.namespace;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            out.setCompilationUnit(this);
            if (this.comment != null) {
                out.write(this.comment).newLine();
            }

            if (this.namespace != null) {
                out.write(this.namespace).newLine();
            }

            if (getImports().size() > 0) {
                out.write(getImports());
            }

            out.write(getTypes());

            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            this.namespace = VisitorUtils.visit(this.namespace, this, visitor);
            VisitorUtils.visit(this.imports, this, visitor);
            VisitorUtils.visit(this.types, this, visitor);
        }

    }

    // ===============================================================
    // CONSTANT DECLARATION
    // ===============================================================
    static class MConstant extends MField implements Constant {

        public MConstant(MVM vm, Type type, String name) {
            super(vm, type, name);
            this.isStatic = true;
            this.isFinal = true;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);

            // write the class and name
            out.write("static final ").write(this.type).space().write(this.name);

            if (this.expr != null) {
                out.write(" = ").write(this.expr);
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
    // CONSTRUCTOR DECLARATION
    // ===============================================================
    static class MConstructor extends MMethod implements Constructor {

        MConstructor(MVM vm, String name) {
            super(vm, null, name);
        }

        @Override
        public MConstructor addThrows(String name) {
            super.addThrows(name);
            return this;
        }

        @Override
        public boolean isAbstract() {
            return false;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);
            AccessType.toCode(this.access, out);

            writeTypeParametersAndThrow(this.name, null, this.parameters, this.throwz, out);

            writeBlock(out, this.vm.getStyle("constructor"));

            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    // ===============================================================
    // FIELD DECLARATION
    // ===============================================================
    abstract static class MField extends MMember implements Field {

        // the type
        Type type;

        // the init expr
        Expression expr;

        public MField(MVM vm, Type type, String name) {
            super(vm);
            this.type = type;
            this.name = name;
        }

        @Override
        public Expression getExpression() {
            return this.expr;
        }

        @Override
        public Type getType() {
            return this.type;
        }

        @Override
        public MField setExpression(Expression expr) {
            this.expr = expr;
            return this;
        }

        @Override
        public MField setType(Type type) {
            this.type = type;
            return this;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            // We write fields in a special manner. If a field has a
            // javadoc comment, separate it from the stuff above with
            // a newline. If it does not have a comment or the
            // comment is not a javadoc, dont add another line.

            if (!out.isLineNew()) {
                out.newLine();
            }

            if (this.comment != null && this.comment.type() == Comment.D) {
                out.newLine().write(this.comment);
            }

            if (this.annotations.size() > 0) {
                out.newLine();
                for (Annotation annotation : getAnnotations()) {
                    out.write(annotation);
                    out.newLine();
                }
            }

            if (!out.isLineNew()) {
                out.newLine();
            }

            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            this.type = VisitorUtils.visit(this.type, this, visitor);
            this.expr = VisitorUtils.visit(this.expr, this, visitor);
        }

        // never called
        @Override
        List<Codeable> codeableList() {
            return null;
        }
    }

    // ===============================================================
    // FORMAL PARAMETER DECLARATION
    // ===============================================================
    static class MFormalParameter extends MDeclaration implements FormalParameter {

        Type type;

        String name;

        boolean isFinal;

        MFormalParameter(MVM vm, Type type, String name) {
            super(vm);
            this.type = type;
            this.name = name;
        }

        @Override
        public String getName() {
            return this.name;
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
        public MFormalParameter isFinal(boolean value) {
            this.isFinal = value;
            return this;
        }

        @Override
        public MFormalParameter setName(String name) {
            this.name = name;
            return this;
        }

        @Override
        public MFormalParameter setType(Type type) {
            this.type = type;
            return this;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            out.queue(this.comment);
            if (this.isFinal) {
                out.write("final ");
            }
            out.write(this.type).space().write(this.name);
            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            this.type = VisitorUtils.visit(this.type, this, visitor);
        }
    }

    // ===============================================================
    // IMPORT DECLARATION
    // ===============================================================
    static class MImport extends MDeclaration implements Import {

        String name;

        MImport(MVM vm, String name) {
            super(vm);
            this.name = name;
        }

        @Override
        public String getName() {
            return this.name;
        }

        public boolean isClassnamePartOfImport(String fullclassname) {
            if (isSingle()) {
                return getName().equals(fullclassname);
            } else {
                String packagename = getName().substring(0, getName().lastIndexOf("."));
                return packagename.equals(fullclassname.substring(0, fullclassname.lastIndexOf(".")));
            }
        }

        public boolean isClassNameAlreadyUsed(String fullclassname) {
            if (isSingle()) {
                String baseClassName = getName().substring(getName().lastIndexOf("."));
                return baseClassName.equals(fullclassname.substring(fullclassname.lastIndexOf(".")));
            } else {
                return false;
            }
        }

        @Override
        public boolean isSingle() {
            return !this.name.endsWith("*");
        }

        @Override
        public MImport setName(String name) {
            this.name = name;
            return this;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            if (!out.isLineNew()) {
                out.newLine();
            }
            super.toCode(out);
            out.write("import ").write(this.name).write(';').newLine();
            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    // ===============================================================
    // INNER CLASS DECLARATION
    // ===============================================================
    static class MInnerClass extends MClassDeclaration implements InnerClass {

        MClassDeclaration parent;

        MInnerClass(MVM vm, MCompilationUnit unit, MClassDeclaration parent, String name) {
            super(vm, unit);
            this.parent = parent;
            this.name = name;
        }

        @Override
        public ClassDeclaration getParentClass() {
            return this.parent;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            // skip parent that would be an endless loop
        }
    }

    // ===============================================================
    // INNER CLASS DECLARATION
    // ===============================================================
    static class MInnerInterface extends MInterface implements InnerInterface {

        MClassDeclaration parent;

        MInnerInterface(MVM vm, MCompilationUnit unit, MClassDeclaration parent, String name) {
            super(vm, unit);
            this.parent = parent;
            this.name = name;
        }

        @Override
        public ClassDeclaration getParentClass() {
            return this.parent;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            // skip parent that would be an endless loop
        }
    }

    // ===============================================================
    // INTERFACE DECLARATION
    // ===============================================================
    static class MInterface extends MTypeDeclaration implements Interface {

        List<String> extendz;

        MInterface(MVM vm, MCompilationUnit unit) {
            super(vm, unit);
            this.extendz = new ArrayList<String>();
        }

        @Override
        public Interface addExtends(String ex) {
            this.extendz.add(ex);
            return this;
        }

        @Override
        public ClassType asClassType() {
            return vm().newType(getUnit().getNamespace().getName() + "." + getName());
        }

        @Override
        public List<String> getExtends() {
            return ListTypeSelector.select(this.extendz);
        }

        @Override
        public Constant newConstant(String name, int val) {
            Constant x = new MConstant(this.vm, this.vm.INT, name);
            x.setExpression(new MLiteral.MIntLiteral(this.vm, val));
            this.members.add(x);
            return x;
        }

        @Override
        public Constant newConstant(Type type, String name) {
            Constant x = new MConstant(this.vm, type, name);
            this.members.add(x);
            return x;
        }

        @Override
        public AbstractMethod newMethod(Type type, String name) {
            AbstractMethod x = new MAbstractMethod(this.vm, type, name);
            this.members.add(x);
            return x;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);

            AccessType.toCode(this.access, out);

            if (this.isFinal) {
                out.write("final ");
            }
            out.write("interface ").write(this.name);

            if (this.extendz.size() > 0) {
                out.newLine().write("extends ");

                for (int i = 0; i < this.extendz.size(); i++) {
                    if (i > 0) {
                        out.write(", ");
                    }
                    out.write(this.extendz.get(i));
                }
            }

            writeBlock(out, this.vm.getStyle("interface"));
            return out;
        }

        @Override
        public boolean removeExtends(String className) {
            return this.extendz.remove(className);
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    // ===============================================================
    // LOCAL CLASS DECLARATION
    // ===============================================================
    static class MLocalClass extends MClassDeclaration implements LocalClass {

        String label;

        MLocalClass(MVM vm, String name) {
            super(vm, null); /*
                              * * *| NOTE: we are passing null and thus cannot
                              * load() a local class! |* *
                              */
            this.name = name;
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
            return false;
        }

        @Override
        public MLocalClass setLabel(String label) {
            this.label = label;
            return this;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);

            out.write(";");

            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            // skip parent that would be an endless loop
        }
    }

    // ===============================================================
    // MEMBER DECLARATION
    // ===============================================================
    abstract static class MMember extends MDeclaration implements Member {

        String name;

        AccessType access;

        boolean isFinal;

        boolean isStatic;

        List<Annotation> annotations;

        MMember(MVM vm) {
            super(vm);
            this.access = Access.PACKAGE;
            this.annotations = new ArrayList<Annotation>();
        }

        @Override
        public MMember addAnnotation(Annotation annotation) {
            this.annotations.add(annotation);
            return this;
        }

        @Override
        public Annotation addAnnotation(String text) {
            Annotation x = new MAnnotation(this.vm, text);
            this.annotations.add(x);
            return x;
        }

        @Override
        public Annotation addAnnotation(String name, String text) {
            Annotation x = new MAnnotation(this.vm, name, text);
            this.annotations.add(x);
            return x;
        }

        @Override
        public AccessType getAccess() {
            return this.access;
        }

        @Override
        public List<Annotation> getAnnotations() {
            return ListTypeSelector.select(this.annotations, Annotation.class);
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public boolean isFinal() {
            return this.isFinal;
        }

        @Override
        public MMember isFinal(boolean value) {
            this.isFinal = value;
            return this;
        }

        @Override
        public boolean isStatic() {
            return this.isStatic;
        }

        @Override
        public MMember isStatic(boolean value) {
            this.isStatic = value;
            return this;
        }

        @Override
        public MMember setAccess(AccessType access) {
            this.access = access;
            return this;
        }

        @Override
        public MMember setName(String name) {
            this.name = name;
            return this;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            if (!out.isLineNew()) {
                out.newLine();
            }

            out.newLine();

            if (this.comment != null) {
                out.write(this.comment);
            }
            if (this.annotations.size() > 0) {
                out.newLine();
                for (Annotation annotation : getAnnotations()) {
                    out.write(annotation);
                    out.newLine();
                }
            }
            if (!out.isLineNew()) {
                out.newLine();
            }

            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            VisitorUtils.visit(this.annotations, this, visitor);
        }

        abstract List<? extends Codeable> codeableList();

        void writeBlock(CodeWriter out, BlockStyle style) {
            style.toCode(out, codeableList());
        }
    }

    // ===============================================================
    // METHOD DECLARATION
    // ===============================================================
    abstract static class MMethod extends BlockDeclaration implements Method {

        boolean isAbstract;

        List<FormalParameter> parameters;

        List<String> throwz;

        Type type;

        MMethod(MVM vm, Type type, String name) {
            super(vm);
            this.type = type;
            this.name = name;
            this.parameters = new ArrayList<FormalParameter>();
            this.throwz = new ArrayList<String>();
        }

        @Override
        public FormalParameter addParameter(Class type, String name) {
            return addParameter(this.vm.newType(type), name);
        }

        @Override
        public FormalParameter addParameter(Type type, String name) {
            FormalParameter x = new MFormalParameter(this.vm, type, name);
            this.parameters.add(x);
            return x;
        }

        @Override
        public MMethod addThrows(String name) {
            this.throwz.add(name);
            return this;
        }

        @Override
        public List<FormalParameter> getParameters() {
            return ListTypeSelector.select(this.parameters, FormalParameter.class);
        }

        @Override
        public List<String> getThrows() {
            return ListTypeSelector.select(this.throwz, String.class);
        }

        @Override
        public Type getType() {
            return this.type;
        }

        @Override
        public boolean isAbstract() {
            return this.isAbstract;
        }

        @Override
        public MMethod isAbstract(boolean isAbstract) {
            this.isAbstract = isAbstract;
            return this;
        }

        @Override
        public MMethod setType(Type type) {
            this.type = type;
            return this;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            VisitorUtils.visit(this.parameters, this, visitor);
            this.type = VisitorUtils.visit(this.type, this, visitor);
        }
    }

    // ===============================================================
    // PACKAGE DECLARATION
    // ===============================================================
    static class MNamespace extends MDeclaration implements Namespace {

        String name;

        MNamespace(MVM vm, String name) {
            super(vm);
            this.name = name;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public MNamespace setName(String name) {
            this.name = name;
            return this;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            if (!out.isLineNew()) {
                out.newLine();
            }
            super.toCode(out);
            out.write("package ").write(this.name).write(';').newLine();
            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    // ===============================================================
    // PACKAGE CLASS DECLARATION
    // ===============================================================
    static class MPackageClass extends MClassDeclaration implements PackageClass {

        MPackageClass(MVM vm, MCompilationUnit unit) {
            super(vm, unit);
        }

        @Override
        public MPackageClass addSerializable() {
            getUnit().addImport(Serializable.class.getName());

            addImplements(Serializable.class.getSimpleName());

            ClassField serialVersionUID = newField(this.vm.newType(Type.LONG), "serialVersionUID");
            serialVersionUID.setExpression(this.vm.newLong(1L))//
                    .isFinal(true)//
                    .isStatic(true)//
                    .setAccess(Access.PRIVATE);
            return this;

        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    // ===============================================================
    // STATIC INITIALIZATION DECLARATION
    // ===============================================================
    static class MStaticInitializer extends BlockDeclaration implements StaticInitializer {

        MStaticInitializer(MVM vm) {
            super(vm);
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);

            // write the class and name
            out.write("static");
            writeBlock(out, this.vm.getStyle("static-initializer"));
            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    // ===============================================================
    // MEMBER DECLARATION
    // ===============================================================
    abstract static class MTypeDeclaration extends MMember implements TypeDeclaration {

        MCompilationUnit unit;

        List<Declaration> members;

        static Comparator<Member> comparator;

        MTypeDeclaration(MVM vm, MCompilationUnit unit) {
            super(vm);
            this.unit = unit;
            this.members = new ArrayList<Declaration>();
        }

        @Override
        public List<Member> getMembers() {
            return ListTypeSelector.select(this.members, Member.class);
        }

        @Override
        public CompilationUnit getUnit() {
            return this.unit;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
            VisitorUtils.visit(this.members, this, visitor);
        }

        @SuppressWarnings("unchecked")
        protected Comparator<Member> getComparator() {
            if (MTypeDeclaration.comparator == null) {
                String className = System.getProperty("jenesis.member-comparator-classname");
                if (className != null) {
                    try {
                        MTypeDeclaration.comparator = Comparator.class.cast(Class.forName(className).newInstance());
                    } catch (Exception ex) {
                        System.out.println("Unable to load comparator: " + className);
                        ex.printStackTrace();
                    }
                }
                if (MTypeDeclaration.comparator == null) {
                    MTypeDeclaration.comparator = new MemberComparator();
                }
            }
            return MTypeDeclaration.comparator;
        }

        @Override
        List<Member> codeableList() {
            return sort(getMembers());
        }

        /**
         * Note: this method does not take inner classes or fancy class names as
         * of yet.
         */
        String getClassfile() {
            // make the fully qualified class name
            StringBuffer className = new StringBuffer();

            // append to the filepath if exists (should, but
            // not required).
            if (this.unit.namespace != null) {
                // fetch the package name
                String pkg = this.unit.namespace.getName();
                // make sure the package name exists
                if (pkg != null) {
                    // append the package name
                    className.append(pkg).append('.');
                }
            }

            // add the name
            className.append(getName());

            // done
            return className.toString();
        }

        String getClassfileName() {
            // make the fileName of the target file as the concatenation
            // of the sourcepath, the package name, and the className,
            // and the ending dot class
            StringBuffer file = null;

            // was a path specified?
            if (this.unit.codebase != null && this.unit.codebase.length() > 0) {
                // make the buffer
                file = new StringBuffer(this.unit.codebase);
                // does the codebase end with a slash?
                if (!this.unit.codebase.endsWith(Character.toString(File.separatorChar))) {
                    // make it so...
                    file.append(File.separatorChar);
                }
            } else {
                file = new StringBuffer();
            }

            // append to the filepath if exists
            if (this.unit.namespace != null) {
                // fetch the package name
                String pkg = this.unit.namespace.getName();
                // make sure the package name exists
                if (pkg != null) {
                    // append the package name
                    file.append(pkg.replace('.', java.io.File.separatorChar));
                }
            }

            // add a separator, the name, and the extention
            file.append(java.io.File.separatorChar).append(getName()).append(".class");

            // done
            return file.toString();
        }

        List<Member> sort(List<Member> list) {
            List<Member> result = new ArrayList<Member>(list);
            Collections.sort(result, getComparator());
            return result;
        }

        @Override
        public boolean removeMember(Member member) {
            return members.remove(member);
        }
    }

    private static void writeTypeParametersAndThrow(String name, Type type, List<FormalParameter> parameters, List<String> throwz, CodeWriter out) {
        // write the class and name
        if (type != null) {
            out.write(type).space();
        }
        out.write(name).write('(');

        for (int i = 0; i < parameters.size(); i++) {
            if (i > 0) {
                out.write(", ");
            }
            out.write(parameters.get(i));
        }

        out.write(')');

        if (throwz.size() > 0) {
            out.newLine().write("throws ");
            for (int i = 0; i < throwz.size(); i++) {
                if (i > 0) {
                    out.write(", ");
                }
                out.write(throwz.get(i));
            }
        }
    }

    MDeclaration(MVM vm) {
        super(vm);
    }

    public DocumentationComment javadoc(String text) {
        this.comment = new MComment.MDocumentationComment(this.vm, text);
        return (DocumentationComment) this.comment;
    }

    @Override
    public void visit(IVisitor visitor) {
        super.visit(visitor);
    }
}
