package compiler.ast;

import compiler.instr.InstrJmp;

import java.io.OutputStreamWriter;

public class ASTLoopStmtNode extends ASTStmtNode {
    public ASTStmtNode m_stmtNode;
    public ASTLoopStmtNode(ASTStmtNode stmtNode) {
        this.m_stmtNode = stmtNode;
    }

    @Override
    public void print(OutputStreamWriter outStream, String indent) throws Exception {
        outStream.write(indent);
        outStream.write("LOOP");
        outStream.write("\n");
        m_stmtNode.print(outStream, indent + "  ");
        outStream.write(indent + "ENDLOOP\n");
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
        compiler.InstrBlock loopBlock = compileEnv.createBlock("LOOP");
        compiler.InstrBlock exitBlock = compileEnv.createBlock("LOOP_EXIT");

        compiler.InstrIntf jmpLoop = new InstrJmp(loopBlock);

        compileEnv.setCurrentBlock(loopBlock);
        compileEnv.pushLoopStack(exitBlock);
        m_stmtNode.codegen(compileEnv);
        compileEnv.addInstr(jmpLoop);

        compileEnv.popLoopStack();
        compileEnv.setCurrentBlock(exitBlock);
    }

    static class BreakLoopException extends RuntimeException {
        public BreakLoopException() {
            super();
        }
    }
}
