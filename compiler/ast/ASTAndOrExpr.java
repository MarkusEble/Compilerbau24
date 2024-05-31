package compiler.ast;

import java.io.OutputStreamWriter;

import compiler.CompileEnvIntf;
import compiler.InstrIntf;
import compiler.Token;
import compiler.TokenIntf.Type;
import compiler.instr.InstrCondJump;

public class ASTAndOrExpr extends ASTExprNode {
    ASTExprNode lhs, rhs;
    Token token;
    private static int symbolCount = 0;
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
        Integer constFold = this.constFold();
        int counter = symbolCount++;
        String symName = "$AND_OR" + (counter);
        compiler.Symbol returnVal = env.getSymbolTable().createSymbol(symName);
        if (constFold != null) {
            env.addInstr(new compiler.instr.InstrAssign(returnVal, new compiler.instr.InstrIntegerLiteral(constFold.toString())));
        } else {
            compiler.InstrIntf lhsExpr = lhs.codegen(env);
            compiler.InstrIntf rhsExpr = rhs.codegen(env);
            compiler.InstrBlock retFalse = env.createBlock("RET_FALSE");
            compiler.InstrBlock retTrue = env.createBlock("RET_TRUE");
            compiler.InstrBlock cal = env.createBlock("CAL");
            retTrue.addInstr(new compiler.instr.InstrAssign(returnVal, new compiler.instr.InstrIntegerLiteral("1")));
            retFalse.addInstr(new compiler.instr.InstrAssign(returnVal, new compiler.instr.InstrIntegerLiteral("0")));
            cal.addInstr(new compiler.instr.InstrAndOr(token.m_type, lhsExpr, rhsExpr));
            if(token.m_type == Type.OR){
                compiler.InstrIntf cmp = new compiler.instr.InstrCondJump(lhsExpr, retTrue,cal);
                env.addInstr(cmp);
            } else {
                compiler.InstrIntf cmp = new compiler.instr.InstrCondJump(new compiler.instr.InstrUnary(Type.NOT, lhsExpr), retTrue,cal);
                env.addInstr(cmp);
            }
        }
        return new compiler.instr.InstrVariableExpr(symName);
    }

    public Integer constFold() {
        // NULL, wenn nicht konstant, sonst den wert
        Integer lhsConstFold = lhs.constFold();
        Integer rhsConstFold = rhs.constFold();

        if (token.m_type == Type.OR) {
            if ((lhsConstFold != null && lhsConstFold == 1) || (rhsConstFold != null && rhsConstFold == 1)) {
                return 1;
            } else if (lhsConstFold != null && rhsConstFold != null && lhsConstFold == 0 && rhsConstFold == 0) {
                return 0;
            }
        } else if (token.m_type == Type.AND) {
            if (lhsConstFold != null && rhsConstFold != null && lhsConstFold == 1 && rhsConstFold == 1) {
                return 1;
            } else if ((lhsConstFold != null && lhsConstFold == 0) || (rhsConstFold != null && rhsConstFold == 0)) {
                return 0;
            }
        }

        return null;
    }
}
