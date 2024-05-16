package compiler.ast;

import java.io.OutputStreamWriter;

public class ASTParantheseNode extends ASTExprNode {

    public ASTExprNode m_expr;

    public ASTParantheseNode(ASTExprNode m_expr) {
        this.m_expr = m_expr;
    }

    @Override
    public int eval() {
        return m_expr.eval();
    }

    public compiler.InstrIntf codegen(compiler.CompileEnvIntf env) {
        return m_expr.codegen(env);
    }

    @Override
    public void print(OutputStreamWriter outStream, String indent) throws Exception {
        outStream.write(indent);
        outStream.write("LPAREN");
        m_expr.print(outStream, indent);
        outStream.write("RPAREN");
        outStream.write("\n");
    }
}
