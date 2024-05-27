package compiler.ast;

import java.io.OutputStreamWriter;

public class ASTSpaceshipExprNode extends ASTExprNode {
    public ASTExprNode m_rvalue;
    public ASTExprNode m_lvalue;

    public ASTSpaceshipExprNode(ASTExprNode lCompareExpr, ASTExprNode rCompareExpr) {
        m_rvalue = rCompareExpr;
        m_lvalue = lCompareExpr;
    }

    @Override
    public int eval() {
        //lhs und rhs operator
        int lvalue = m_lvalue.eval();
        int rvalue = m_rvalue.eval();
        if(lvalue < rvalue) {
            //-1, when lhs<rhs
            return -1;
        }else if(lvalue > rvalue){
            //1, when lhs>rhs
            return 1;
        }
        //0, when lhs==rhs
        return 0;
    }

    @Override
    public void print(OutputStreamWriter outStream, String indent) throws Exception {
        outStream.write(indent);
        outStream.write("SPACESHIPExpr ");
        outStream.write("\n");
        m_lvalue.print(outStream, indent +" ");
        outStream.write(indent + " ");
        outStream.write("SPACESHIP");
        outStream.write("\n");
        m_rvalue.print(outStream, indent + " ");
    }

    /*@Override
    public compiler.InstrIntf codegen(compiler.CompileEnvIntf compileEnv) {

    }*/

}
