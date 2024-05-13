package compiler.ast;

import java.io.OutputStreamWriter;
import java.util.List;

public class ASTBlockStmtNode extends ASTStmtNode{

    List<ASTStmtNode> stmts;


    public ASTBlockStmtNode(List<ASTStmtNode> stmts) {
        this.stmts = stmts;
    }
    @Override
    public void print(OutputStreamWriter outStream, String indent) throws Exception {
        outStream.write(indent + "BLOCK\n{\n");
        for (ASTStmtNode stmt : stmts) {
            stmt.print(outStream, indent);
        }
        outStream.write("\n}");
    }

    @Override
    public void execute() {
        for (ASTStmtNode stmt : stmts) {
            stmt.execute();
        }
    }

    @Override
    public void codegen(compiler.CompileEnvIntf env) {
        for (ASTStmtNode stmt : stmts) {
            stmt.codegen(env);
        }
    }
}
