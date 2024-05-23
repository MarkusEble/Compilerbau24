package compiler.ast;

public abstract class ASTExprNode extends ASTNode {
    public abstract int eval();
    public compiler.InstrIntf codegen(compiler.CompileEnvIntf env) { return null; }
    public Integer consFold() { return null; }
}
