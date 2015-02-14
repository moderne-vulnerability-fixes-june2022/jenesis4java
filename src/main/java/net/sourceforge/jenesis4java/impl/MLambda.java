package net.sourceforge.jenesis4java.impl;

import net.sourceforge.jenesis4java.*;
import net.sourceforge.jenesis4java.impl.MDeclaration.MFormalParameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the {@code Lambda} interface.
 */
public class MLambda extends MVM.MCodeable implements Lambda {

    private static abstract class Parameter {

        private static class TypedParameter extends Parameter {

            private final MFormalParameter parameter;

            public TypedParameter(MFormalParameter parameter) {
                this.parameter = parameter;
            }

            public CodeWriter toCode(CodeWriter out) {
                return parameter.toCode(out);
            }
        }

        private static class ParameterName extends Parameter {

            private final String parameterName;

            public ParameterName(String parameterName) {
                this.parameterName = parameterName;
            }

            public CodeWriter toCode(CodeWriter out) {
                return out.write(parameterName);
            }
        }

        public static Parameter createParameter(String parameterName) {
            return new ParameterName(parameterName);
        }

        public static Parameter createParameter(MVM vm, Type type, String name) {
            return new TypedParameter(new MFormalParameter(vm, type, name));
        }

        public abstract CodeWriter toCode(CodeWriter out);
    }

    private class ParameterHolder {

        // NOTE: initially null (i.e. undefined)
        private Boolean typelessParameters;

        private List<Parameter> parameterList = new ArrayList<Parameter>();

        public void add(Type type, String name) {
            if (typelessParameters != null && typelessParameters) {
                throw new RuntimeException("Lambda parameters with and without type declaration cannot be mixed");
            }
            typelessParameters = false;
            this.parameterList.add(Parameter.createParameter(MLambda.this.vm, type, name));
        }

        public void add(String name) {
            if (typelessParameters != null && !typelessParameters) {
                throw new RuntimeException("Lambda parameters with and without type declaration cannot be mixed");
            }
            typelessParameters = true;
            this.parameterList.add(Parameter.createParameter(name));
        }

        public void toCode(CodeWriter out) {
            if (parameterList.size() == 1 && typelessParameters) {
                // No parentheses around single parameter name
                parameterList.get(0).toCode(out);
            } else {
                codeParameters(out);
            }
        }

        private void codeParameters(CodeWriter out) {
            boolean first = true;
            out.write("(");
            for (Parameter parameter : parameterList) {
                if (!first) {
                    out.write(", ");
                } else {
                    first = false;
                }
                parameter.toCode(out);
            }
            out.write(")");
        }

        public void visit(IVisitor visitor) {
        }
    }

    private ParameterHolder parameters = new ParameterHolder();

    private Expression body;

    private MStatement.BlockStatement bodyBlock;

    public MLambda(MVM vm) {
        super(vm);
    }

    @Override
    public Lambda addParameter(Class<?> type, String name) {
        addParameter(this.vm.newType(type), name);
        return this;
    }

    @Override
    public Lambda addParameter(Type type, String name) {
        parameters.add(type, name);
        return this;
    }

    @Override
    public Lambda addParameter(String name) {
        parameters.add(name);
        return this;
    }

    @Override
    public Lambda setBody(Expression body) {
        this.body = body;
        return this;
    }

    @Override
    public Block newBodyBlock() {
        this.bodyBlock = new MStatement.BlockStatement(this.vm);
        return this.bodyBlock;
    }

    @Override
    public CodeWriter toCode(CodeWriter out) {
        parameters.toCode(out);
        out.write(" -> ");
        if (body != null) {
            body.toCode(out);
        } else if (bodyBlock != null) {
            bodyBlock.writeBlock(out, this.vm.getStyle("lambda-block"));
        } else {
            // create an empty block and write it
            MStatement.BlockStatement block = new MStatement.BlockStatement(this.vm);
            block.newEmpty();
            block.writeBlock(out, this.vm.getStyle("lambda-block"));
        }
        return out.write(';');
    }

    @Override
    public void visit(IVisitor visitor) {
        super.visit(visitor);
        parameters.visit(visitor);
        // TODO
    }
}
