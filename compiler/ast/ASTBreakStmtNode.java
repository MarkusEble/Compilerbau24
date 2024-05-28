package compiler.ast;

import compiler.instr.InstrJmp;

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
        compiler.InstrBlock exitBlock = compileEnv.peekLoopStack();
        compiler.InstrIntf jmpExit = new InstrJmp(exitBlock);
        compileEnv.addInstr(jmpExit);
    }
}
