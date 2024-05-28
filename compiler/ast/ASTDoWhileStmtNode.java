package compiler.ast;

import java.io.OutputStreamWriter;

public class ASTDoWhileStmtNode extends ASTStmtNode {

    ASTStmtNode m_blockStmt;
    ASTExprNode m_expr;

    public ASTDoWhileStmtNode(ASTStmtNode m_blockStmt, ASTExprNode m_expr) {
        this.m_blockStmt = m_blockStmt;
        this.m_expr = m_expr;
    }

    @Override
    public void print(OutputStreamWriter outStream, String indent) throws Exception {
        outStream.write(indent + "DO\n");
        m_blockStmt.print(outStream, indent + "  ");
        outStream.write(indent + "WHILE\n" + indent + "(\n");
        m_expr.print(outStream, indent + "  ");
        outStream.write(indent + ")\n");
    }

    @Override
    public void execute() {
        do {
            m_blockStmt.execute();
        } while (m_expr.eval() != 0);
    }

    @Override
    public void codegen(compiler.CompileEnvIntf env) {
        compiler.InstrBlock doWhileBlock = env.createBlock("DO_WHILE");
        compiler.InstrBlock doWhileExit = env.createBlock("DO_WHILE_EXIT");

        compiler.InstrIntf jmpDo = new compiler.instr.InstrJmp(doWhileBlock);
        env.addInstr(jmpDo);
        env.setCurrentBlock(doWhileBlock);
        m_blockStmt.codegen(env);
        compiler.InstrIntf condition = m_expr.codegen(env);
        compiler.InstrIntf condJmp = new compiler.instr.InstrCondJump(condition, doWhileBlock, doWhileExit);
        env.addInstr(condJmp);

        env.setCurrentBlock(doWhileBlock);
    }
}
