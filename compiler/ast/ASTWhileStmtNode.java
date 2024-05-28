package compiler.ast;

import java.io.OutputStreamWriter;

public class ASTWhileStmtNode extends ASTStmtNode {

    ASTStmtNode m_blockStmt;
    ASTExprNode m_expr;

    public ASTWhileStmtNode(ASTStmtNode m_blockStmt, ASTExprNode m_expr) {
        this.m_blockStmt = m_blockStmt;
        this.m_expr = m_expr;
    }

    @Override
    public void print(OutputStreamWriter outStream, String indent) throws Exception {
        outStream.write( indent +"WHILE\n" + indent + "(\n");
        m_expr.print(outStream, indent + "  ");
        outStream.write(indent + ")\n");
        m_blockStmt.print(outStream, indent);
    }

    @Override
    public void execute() {
        while (m_expr.eval() != 0) {
            m_blockStmt.execute();
        }
    }

    @Override
    public void codegen(compiler.CompileEnvIntf env) {
        compiler.InstrBlock whileConditionBlock = env.createBlock("WHILE_CONDITION");
        compiler.InstrBlock whileBlock = env.createBlock("WHILE_BODY");
        compiler.InstrBlock whileExit = env.createBlock("WHILE_EXIT");

        compiler.InstrIntf jmpCondition = new compiler.instr.InstrJmp(whileConditionBlock);
        env.addInstr(jmpCondition);

        env.setCurrentBlock(whileConditionBlock);
        compiler.InstrIntf condition = m_expr.codegen(env);
        compiler.InstrIntf condJmp = new compiler.instr.InstrCondJump(condition, whileBlock, whileExit);
        env.addInstr(condJmp);

        env.setCurrentBlock(whileBlock);
        m_blockStmt.codegen(env);
        compiler.InstrIntf jmpBack = new compiler.instr.InstrJmp(whileConditionBlock);
        env.addInstr(jmpBack);

        env.setCurrentBlock(whileExit);
    }
}
