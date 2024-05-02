package compiler.ast;

public abstract class ASTStmtNode extends ASTNode {
    public abstract void execute();
    public void codegen(compiler.CompileEnvIntf env) {}
}
