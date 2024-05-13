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

}
