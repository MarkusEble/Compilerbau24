package compiler.ast;

import compiler.CompileEnvIntf;
import compiler.TokenIntf;

import java.io.OutputStreamWriter;

public class ASTNumericIfNode extends ASTStmtNode {

    private ASTExprNode expr;
    private ASTStmtNode pos;
    private ASTStmtNode neg;
    private ASTStmtNode zero;

    public ASTNumericIfNode(ASTExprNode expr, ASTStmtNode pos, ASTStmtNode neg, ASTStmtNode zero) {
        this.expr = expr;
        this.pos = pos;
        this.neg = neg;
        this.zero = zero;
    }

    @Override
    public void print(OutputStreamWriter outStream, String indent) throws Exception {
        outStream.write(indent + TokenIntf.Type.NUMERIC_IF.toString() + " " + expr.eval() + "\n");
        outStream.write(indent + "POSITIVE");
        pos.print(outStream, indent + "  ");
        outStream.write(indent + "NEGATIVE");
        neg.print(outStream, indent + "  ");
        outStream.write(indent + "ZERO");
        zero.print(outStream, indent + "  ");
    }

    @Override
    public void execute() {
        if (expr.eval() > 0) {
            pos.execute();
        } else if (expr.eval() < 0) {
            neg.execute();
        } else {
            zero.execute();
        }
    }

    @Override
    public void codegen(CompileEnvIntf env) {
        String leonie = "Hallo Leonie";


        super.codegen(env);
    }
}
