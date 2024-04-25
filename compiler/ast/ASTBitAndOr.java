package compiler.ast;

import compiler.TokenIntf;

import java.io.OutputStreamWriter;

public class ASTBitAndOr extends ASTExprNode {
    public ASTExprNode m_lhs;
    public TokenIntf.Type m_operator;
    public ASTExprNode m_rhs;

    public ASTBitAndOr(ASTExprNode m_lhs, TokenIntf.Type m_operator, ASTExprNode m_rhs) {
        this.m_lhs = m_lhs;
        this.m_operator = m_operator;
        this.m_rhs = m_rhs;
    }

    @Override
    public void print(OutputStreamWriter outStream, String indent) throws Exception {
        this.m_lhs.print(outStream, indent + "  ");
        outStream.write(indent + "OPERATOR " + this.m_operator);
        outStream.write("\n");
        this.m_rhs.print(outStream, indent + "  ");
    }

    @Override
    public int eval() {
        if (this.m_operator == TokenIntf.Type.BITAND) {
            return this.m_lhs.eval() & this.m_rhs.eval();
        } else if (this.m_operator == TokenIntf.Type.BITOR) {
            return this.m_lhs.eval() | this.m_rhs.eval();
        }
        // Ideally throw an error
        return 0;
    }
}
