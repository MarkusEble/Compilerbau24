package compiler.ast;

import compiler.Token;
import compiler.TokenIntf;

import java.io.OutputStreamWriter;

public class ASTArrowNode extends ASTExprNode {
    public ASTExprNode m_rvalue;
    public ASTExprNode m_lvalue;

    public ASTArrowNode(ASTExprNode lParenExpr, ASTExprNode rParenExpr) {
        m_rvalue = rParenExpr;
        m_lvalue = lParenExpr;
    }
    @Override
    public int eval() {
        int lresult = m_lvalue.eval();
        int rresult = m_rvalue.eval();
        if (lresult>=rresult){
            return lresult-rresult;
        }else{
            return rresult+lresult;
        }
    }

    @Override
    public void print(OutputStreamWriter outStream, String indent) throws Exception {
        outStream.write(indent);outStream.write("ARROWExpr ");
        outStream.write("\n");
        m_lvalue.print(outStream, indent +" ");
        outStream.write(indent + " ");
        outStream.write("ARROW");
        outStream.write("\n");
        m_rvalue.print(outStream, indent + " ");
    }
    @Override
    public compiler.InstrIntf codegen(compiler.CompileEnvIntf compileEnv) {
        if(this.constFold() != null){
            compiler.InstrIntf resultExpr = new compiler.instr.InstrIntegerLiteral(this.constFold().toString());
            compileEnv.addInstr(resultExpr);
            return resultExpr;
        }
        compiler.InstrIntf rhsExpr = m_rvalue.codegen(compileEnv);
        compiler.InstrIntf lhsExpr = m_lvalue.codegen(compileEnv);
        compiler.InstrIntf resultExpr =  new compiler.instr.InstrArrow(lhsExpr, rhsExpr);
        compileEnv.addInstr(resultExpr);
        return resultExpr;
    }

    @Override
    public Integer constFold() {
        Integer const_lresult = m_lvalue.constFold();
        Integer const_rresult = m_rvalue.constFold();
        if (const_lresult != null && const_rresult != null) {
            if(const_lresult >= const_rresult) {
                return const_lresult - const_rresult;
            } else {
                return const_lresult + const_rresult;
            }
        }
        return null;
    }
}
