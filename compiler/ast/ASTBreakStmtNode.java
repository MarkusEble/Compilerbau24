package compiler.ast;

import java.io.OutputStreamWriter;

public class ASTBreakStmtNode extends ASTStmtNode {

    public ASTBreakStmtNode() {
    }

    @Override
    public void print(OutputStreamWriter outStream, String indent) throws Exception {
        outStream.write(indent);
        outStream.write("BREAK");
        outStream.write("\n");
    }

    @Override
    public void execute() {
        throw new ASTLoopStmtNode.BreakLoopException();
    }

    @Override
    public void codegen(compiler.CompileEnvIntf compileEnv) {

    }
}
