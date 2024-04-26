package compiler.ast;

import java.io.OutputStreamWriter;

public class ASTAndOrExpr extends ASTExprNode {
    ASTExprNode lhs, rhs;
    boolean isOr;

    public ASTAndOrExpr(boolean or, ASTExprNode left, ASTExprNode right) {
        this.lhs = left;
        this.isOr = or;
        this.rhs = right;
    }

    @Override
    public void print(OutputStreamWriter outStream, String indent) throws Exception {
        outStream.write("AndOr\n");
        outStream.write(indent);
        this.lhs.print(outStream, indent + indent);
        outStream.write("\n");
        outStream.write(indent);
        outStream.write(isOr ? "OR" : "AND");
        outStream.write("\n");
        outStream.write(indent);
        this.rhs.print(outStream, indent + indent);
        outStream.write("\n");
    }

    @Override
    public int eval() {
        if (isOr) {
            return lhs.eval() == 1 || rhs.eval() == 1 ? 1 : 0;
        } else {
            return lhs.eval() == 1 && rhs.eval() == 1 ? 1 : 0;
        }
    }

}
