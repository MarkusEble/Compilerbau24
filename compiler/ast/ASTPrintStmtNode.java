package compiler.ast;

import java.io.OutputStreamWriter;

public class ASTPrintStmtNode extends ASTStmtNode {

    private ASTExprNode expressionNode;

    public ASTPrintStmtNode(ASTExprNode exprNode) {
        expressionNode = exprNode;
    }

    @Override
    public void execute() {
        System.out.println(expressionNode.eval());
    }

    @Override
    public void print(OutputStreamWriter outStream, String indent) throws Exception {
        outStream.write(indent);
        outStream.write("PrintStmt \n");
        outStream.write(indent + "  ");
        outStream.write("PRINT \n");
        expressionNode.print(outStream, indent + "  ");
        outStream.write(indent + "  ");
        outStream.write("SEMICOLON \n");
    }

}