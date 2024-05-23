package compiler.ast;

import java.io.OutputStreamWriter;

public class ASTDashNode extends ASTExprNode {
    public ASTExprNode lhs, rhs;
    public ASTDashNode(ASTExprNode lhs, ASTExprNode rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public void print(OutputStreamWriter outStream, String indent) throws Exception {
        outStream.write(indent);
        lhs.print(outStream, indent+indent);
        outStream.write("DASH ");
        outStream.write("\n");
        rhs.print(outStream, indent+indent);
    }

    @Override
    public int eval() {
        return (int) Math.pow(lhs.eval(), rhs.eval());
    }

    @Override
    public compiler.InstrIntf codegen(compiler.CompileEnvIntf compileEnv) {
        Integer constResult = this.constFold();
        if (constResult != null) {
            compiler.InstrIntf literalInstr = new compiler.instr.InstrIntegerLiteral(constResult.toString());
            compileEnv.addInstr(literalInstr);
            return literalInstr;
        }

        compiler.InstrIntf lhsExpr = lhs.codegen(compileEnv);
        compiler.InstrIntf rhsExpr = rhs.codegen(compileEnv);
        compiler.InstrIntf resultExpr =  new compiler.instr.InstrDash(lhsExpr, rhsExpr);
        compileEnv.addInstr(resultExpr);
        return resultExpr;
    }

    @Override
    public Integer constFold() {
        Integer lhsConstFold = lhs.constFold();
        Integer rhsConstFold = rhs.constFold();
        if (lhsConstFold != null && rhsConstFold != null) {
            return (int) Math.pow(lhsConstFold, rhsConstFold);
        }
        return null;
    }
}
