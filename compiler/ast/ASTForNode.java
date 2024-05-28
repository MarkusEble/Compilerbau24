package compiler.ast;

import compiler.CompileEnvIntf;
import compiler.InstrIntf;
import compiler.instr.InstrComp;
import compiler.instr.InstrCondJump;
import compiler.instr.InstrJmp;

import java.io.OutputStreamWriter;

public class ASTForNode extends ASTStmtNode {

    private ASTStmtNode m_stmt_assign;
    private ASTExprNode m_stmt_condition;
    private ASTStmtNode m_stmt_action;

    private ASTStmtNode m_block;

    public ASTForNode(
            ASTStmtNode m_stmt_assign,
            ASTExprNode m_stmt_condition,
            ASTStmtNode m_stmt_action,
            ASTStmtNode m_block
    ) {
        this.m_stmt_assign = m_stmt_assign;
        this.m_stmt_condition = m_stmt_condition;
        this.m_stmt_action = m_stmt_action;
        this.m_block = m_block;
    }

    @Override
    public void print(OutputStreamWriter outStream, String indent) throws Exception {
        outStream.write(indent);
        outStream.write("FOR");
        indent += "  ";
        m_stmt_assign.print(outStream, indent);
        m_stmt_condition.print(outStream, indent);
        m_stmt_action.print(outStream, indent);
        m_block.print(outStream, indent);
    }

    @Override
    public void execute() {
        // Execute the before action
        m_stmt_assign.execute();

        while(m_stmt_condition.eval() == 1) {
            m_stmt_action.execute();
        }
    }

    @Override
    public void codegen(CompileEnvIntf env) {
        compiler.InstrBlock beforeFor = env.createBlock("BEFORE_FOR");
        compiler.InstrBlock forHeader = env.createBlock("FOR_HEAD");
        compiler.InstrBlock forBody = env.createBlock("FOR_BODY");
        compiler.InstrBlock forExit = env.createBlock("FOR_EXIT");

        // Execute the first statement first and only once
        env.addInstr(new InstrJmp(beforeFor));

        env.setCurrentBlock(beforeFor);
        m_stmt_assign.codegen(env);
        env.addInstr(new InstrJmp(forHeader));

        env.setCurrentBlock(forHeader);
        InstrIntf expr = m_stmt_condition.codegen(env);
        InstrIntf condJump = new InstrCondJump(expr, forBody, forExit);
        env.addInstr(condJump);

        env.setCurrentBlock(forBody);
        m_block.codegen(env);
        env.addInstr(new InstrJmp(forHeader));

        env.setCurrentBlock(forExit);
        // Ende?
    }
}
