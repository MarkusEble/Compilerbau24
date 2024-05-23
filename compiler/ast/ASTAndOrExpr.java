package compiler.ast;

import java.io.OutputStreamWriter;

import compiler.CompileEnvIntf;
import compiler.InstrIntf;
import compiler.Token;
import compiler.TokenIntf.Type;

public class ASTAndOrExpr extends ASTExprNode {
    ASTExprNode lhs, rhs;
    Token token;

    public ASTAndOrExpr(Token or, ASTExprNode left, ASTExprNode right) {
        this.lhs = left;
        this.token = or;
        this.rhs = right;
    }

    @Override
    public void print(OutputStreamWriter outStream, String indent) throws Exception {
        outStream.write("AndOr\n");
        outStream.write(indent);
        this.lhs.print(outStream, indent + indent);
        outStream.write("\n");
        outStream.write(indent);
        outStream.write(token.toString());
        outStream.write("\n");
        outStream.write(indent);
        this.rhs.print(outStream, indent + indent);
        outStream.write("\n");
    }

    @Override
    public int eval() {
        if (token.m_type == Type.OR) {
            return lhs.eval() == 1 || rhs.eval() == 1 ? 1 : 0;
        } else {
            return lhs.eval() == 1 && rhs.eval() == 1 ? 1 : 0;
        }
    }
    @Override
    public InstrIntf codegen(CompileEnvIntf env) {
        compiler.InstrIntf lhsExpr = lhs.codegen(env);
        compiler.InstrIntf rhsExpr = rhs.codegen(env);
        compiler.InstrIntf resultExpr = new compiler.instr.InstrAndOr(token.m_type, lhsExpr, rhsExpr);
        env.addInstr(resultExpr);
        return resultExpr;
    }
}
