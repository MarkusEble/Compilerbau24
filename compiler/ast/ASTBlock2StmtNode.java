package compiler.ast;

import java.io.OutputStreamWriter;
import java.util.List;

public class ASTBlock2StmtNode extends ASTStmtNode{

    List<ASTStmtNode> stmts;


    public ASTBlock2StmtNode(List<ASTStmtNode> stmts) {
        this.stmts = stmts;
    }
    @Override
    public void print(OutputStreamWriter outStream, String indent) throws Exception {
        outStream.write(indent + "BLOCK2\n" + indent + "{\n");
        for (ASTStmtNode stmt : stmts) {
            stmt.print(outStream, indent + "  ");
        }
        outStream.write(indent + "}\n");
    }

    @Override
    public void execute() {
        for (ASTStmtNode stmt : stmts) {
            stmt.execute();
        }
    }

    @Override
    public void codegen(compiler.CompileEnvIntf env) {
        compiler.InstrBlock block = env.createBlock("BLOCK2");
        compiler.InstrBlock blockExit = env.createBlock("BLOCK2_EXIT");
        compiler.InstrIntf jmpBlock = new compiler.instr.InstrJmp(block);
        env.addInstr(jmpBlock);
        env.setCurrentBlock(block);


        for (ASTStmtNode stmt : stmts) {
            stmt.codegen(env);
        }

        compiler.InstrIntf jmpExit = new compiler.instr.InstrJmp(blockExit);
        env.addInstr(jmpExit);
        env.setCurrentBlock(blockExit);
    }
}
