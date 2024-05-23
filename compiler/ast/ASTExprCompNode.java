package compiler.ast;

import compiler.CompileEnvIntf;
import compiler.InstrIntf;
import compiler.Token;
import compiler.instr.InstrIntegerLiteral;

import java.io.OutputStreamWriter;

public class ASTExprCompNode extends ASTExprNode{

    public ASTExprNode lhs, rhs;
    public Token token;

    public ASTExprCompNode(ASTExprNode lhs, Token token, ASTExprNode rhs){
        this.lhs = lhs;
        this.token = token;
        this.rhs = rhs;
    }
    @Override
    public int eval() {
        if(token.m_type == Token.Type.GREATER){
            return lhs.eval() > rhs.eval() ? 1 : 0;
        } else if(token.m_type == Token.Type.LESS){
            return lhs.eval() < rhs.eval() ? 1 : 0;
        } else {
            return lhs.eval() == rhs.eval() ? 1 : 0;
        }
    }

    @Override
    public void print(OutputStreamWriter outStream, String indent) throws Exception {
        outStream.write(indent);
        outStream.write("COMPARE\n");
        lhs.print(outStream, indent);
        outStream.write(indent);
        outStream.write(token.m_value);
        outStream.write("\n");
        rhs.print(outStream, indent);
    }

    @Override
    public InstrIntf codegen(CompileEnvIntf compileEnv) {
        Integer foldValue = this.constFold();

        if (foldValue != null) {
            compiler.InstrIntf resultExpr = new compiler.instr.InstrIntegerLiteral(foldValue.toString());
            compileEnv.addInstr(resultExpr);
            return resultExpr;
        }

        InstrIntf lhsInstr = lhs.codegen(compileEnv);
        InstrIntf rhsInstr = rhs.codegen(compileEnv);
        InstrIntf resultExpr = new compiler.instr.InstrComp(lhsInstr, rhsInstr, token);
        compileEnv.addInstr(resultExpr);
        return resultExpr;
    }

    @Override
    public Integer constFold() {
        Integer lhsValue = lhs.constFold();
        Integer rhsValue = rhs.constFold();

        if (lhsValue != null && rhsValue != null) {
            if(token.m_type == Token.Type.GREATER){
                return lhsValue > rhsValue ? 1 : 0;
            } else if(token.m_type == Token.Type.LESS){
                return lhsValue < rhsValue ? 1 : 0;
            } else {
                return lhsValue == rhsValue ? 1 : 0;
            }
        }

        return null;
    }
}
