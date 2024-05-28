package compiler.ast;

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

    }
}
