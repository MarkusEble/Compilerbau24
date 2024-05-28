package compiler.ast;

import compiler.Token;
import compiler.TokenIntf;

import java.io.OutputStreamWriter;

public class ASTMulDivExprNode extends ASTExprNode {

    ASTExprNode m_unaryExprLhs;
    ASTExprNode m_unaryExprRhs;
    Token m_operator;

    public ASTMulDivExprNode(ASTExprNode m_unaryExprLhs, Token operator, ASTExprNode m_unaryExprRhs) {
        this.m_unaryExprLhs = m_unaryExprLhs;
        this.m_unaryExprRhs = m_unaryExprRhs;
        this.m_operator = operator;
    }

    @Override
    public void print(OutputStreamWriter outStream, String indent) throws Exception {
        outStream.write(indent);
        outStream.write("MulDivExpr \n");
        m_unaryExprLhs.print(outStream, indent + "  ");
        outStream.write(indent + "  ");
        outStream.write(m_operator.m_type.name()+ "\n");
        m_unaryExprRhs.print(outStream, indent + "  ");
    }

    @Override
    public int eval() {
        int result = 0;
        if (m_operator.m_type == TokenIntf.Type.MUL) {
            result = m_unaryExprLhs.eval() * m_unaryExprRhs.eval();
        } else {
            result = m_unaryExprLhs.eval() / m_unaryExprRhs.eval();
        }
        return result;
    }

    @Override
    public compiler.InstrIntf codegen(compiler.CompileEnvIntf compileEnv) {
        Integer constResult = constFold();
        if (constResult != null) {
            compiler.InstrIntf resultExpr = new compiler.instr.InstrIntegerLiteral(constResult.toString());
            compileEnv.addInstr(resultExpr);
            return resultExpr;
        }
        compiler.InstrIntf lhsExpr = m_unaryExprLhs.codegen(compileEnv);
        compiler.InstrIntf rhsExpr = m_unaryExprRhs.codegen(compileEnv);
        compiler.InstrIntf resultExpr =  new compiler.instr.InstrMulDiv(m_operator.m_type, lhsExpr, rhsExpr);
        compileEnv.addInstr(resultExpr);
        return resultExpr;
    }

    public Integer constFold() {
        Integer constLhs = m_unaryExprLhs.constFold();
        Integer constRhs = m_unaryExprRhs.constFold();

        if (constLhs != null && constRhs != null) {
            if (m_operator.m_type == TokenIntf.Type.MUL) {
                return Integer.valueOf((int) constLhs * (int) constRhs);
            }
            else {
                return Integer.valueOf((int) constLhs / (int) constRhs);
            }
        } else {
            return null;
        }
    }
}