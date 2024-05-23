package compiler.ast;

import compiler.CompileEnvIntf;
import compiler.InstrIntf;
import compiler.instr.InstrQuestionMarkExpr;

import java.io.OutputStreamWriter;
import java.util.Optional;

public class ASTQuestionMarkExprNode extends ASTExprNode {
    private final ASTExprNode m_condition;
    private final ASTExprNode m_result1;
    private final ASTExprNode m_result2;

    public ASTQuestionMarkExprNode(ASTExprNode condition, ASTExprNode result1, ASTExprNode result2) {
        m_condition = condition;
        m_result1 = result1;
        m_result2 = result2;
    }

    @Override
    public int eval() {
        return m_condition.eval() > 0 ? m_result1.eval() : m_result2.eval();
    }

    @Override
    public void print(OutputStreamWriter outStream, String indent) throws Exception {
        outStream.write(indent);
        outStream.write("QUESTION_MARK_EXPR");
        outStream.write("\n");
        m_condition.print(outStream, indent + "  ");
        outStream.write(indent + "  ");
        outStream.write("QUESTION_MARK");
        outStream.write("\n");
        m_result1.print(outStream, indent + "  ");
        outStream.write(indent + "  ");
        outStream.write("DOUBLE_COLON");
        outStream.write("\n");
        m_result2.print(outStream, indent + "  ");
    }

    @Override
    public InstrIntf codegen(CompileEnvIntf compileEnvIntf) {
        InstrIntf result1 = m_result1.codegen(compileEnvIntf);
        InstrIntf result2 = m_result2.codegen(compileEnvIntf);

        InstrIntf condition = m_condition.codegen(compileEnvIntf);
        InstrIntf questionMarkExpr = new InstrQuestionMarkExpr(result1, result2, condition);
        compileEnvIntf.addInstr(questionMarkExpr);
        return questionMarkExpr;
    }
}
