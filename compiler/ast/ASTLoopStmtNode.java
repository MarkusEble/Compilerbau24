package compiler.ast;

import java.io.OutputStreamWriter;

public class ASTLoopStmtNode extends ASTStmtNode {
    public ASTStmtNode m_stmtNode;
    public ASTLoopStmtNode(ASTStmtNode stmtNode) {
        this.m_stmtNode = stmtNode;
    }

    @Override
    public void print(OutputStreamWriter outStream, String indent) throws Exception {
        outStream.write(indent);
        outStream.write("LOOP ");
        outStream.write("\n");
        m_stmtNode.print(outStream, indent+indent);
        outStream.write("ENDLOOP\n");
    }

    @Override
    public void execute() {
        while (true) {
            try {
                m_stmtNode.execute();
            } catch (BreakLoopException e) {
                break;
            }
        }
    }

    @Override
    public void codegen(compiler.CompileEnvIntf compileEnv) {

    }

    static class BreakLoopException extends RuntimeException {
        public BreakLoopException() {
            super();
        }
    }
}
