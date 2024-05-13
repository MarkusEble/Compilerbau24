package compiler.ast;

import compiler.Token;
import compiler.TokenIntf;

import java.io.OutputStreamWriter;

public class ASTPlusMinusExprNode extends ASTExprNode {

    ASTExprNode m_lhs;
    ASTExprNode m_rhs;
    Token m_operator;

    public ASTPlusMinusExprNode(Token operator, ASTExprNode lhs, ASTExprNode rhs) {
        m_operator = operator;
        m_lhs = lhs;
        m_rhs = rhs;
    }

    @Override
    public void print(OutputStreamWriter outStream, String indent) throws Exception {
        outStream.write(indent);
        outStream.write("PlusMinusExpr \n");
        m_lhs.print(outStream, indent + "  ");
        outStream.write(indent + "  ");
        outStream.write(m_operator.m_type.name()+ "\n");
        m_rhs.print(outStream, indent + "  ");
    }

    @Override
    public int eval() {
        int result = 0;
        if (m_operator.m_type == TokenIntf.Type.PLUS) {
            result = m_lhs.eval() + m_rhs.eval();
        } else {
            result = m_lhs.eval() - m_rhs.eval();
        }
        return result;
    }

    @Override
    public compiler.InstrIntf codegen(compiler.CompileEnvIntf compileEnv) {
        compiler.InstrIntf lhsExpr = m_lhs.codegen(compileEnv);
        compiler.InstrIntf rhsExpr = m_rhs.codegen(compileEnv);
        compiler.InstrIntf resultExpr =  new compiler.instr.InstrPlusMinus(m_operator.m_type, lhsExpr, rhsExpr);
        compileEnv.addInstr(resultExpr);
        return resultExpr;
    }
}