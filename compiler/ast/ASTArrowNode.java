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
        outStream.write(indent);
        outStream.write("ARROWExpr ");
        outStream.write("\n");
        m_lvalue.print(outStream, indent +" ");
        outStream.write(indent + " ");
        outStream.write("ARROW");
        outStream.write("\n");
        m_rvalue.print(outStream, indent + " ");
    }
}
