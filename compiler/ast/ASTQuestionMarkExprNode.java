package compiler.ast;

import compiler.CompileEnvIntf;
import compiler.InstrIntf;
import compiler.instr.InstrQuestionMarkExpr;

import java.io.OutputStreamWriter;
import java.util.Optional;

public class ASTQuestionMarkExprNode extends ASTExprNode {
    private final ASTExprNode m_condition;
    private Optional<ASTExprNode> m_result1 = Optional.empty();
    private Optional<ASTExprNode> m_result2 = Optional.empty();

    public ASTQuestionMarkExprNode(ASTExprNode condition, ASTExprNode result1, ASTExprNode result2) {
        m_condition = condition;
        m_result1 = Optional.of(result1);
        m_result2 = Optional.of(result2);
    }

    public ASTQuestionMarkExprNode(ASTExprNode condition) {
        m_condition = condition;
    }

    @Override
    public int eval() {
        if (m_result1.isPresent() && m_result2.isPresent()) {
            return m_condition.eval() > 0 ? m_result1.get().eval() : m_result2.get().eval();
        } else {
            return m_condition.eval();
        }
    }

    @Override
    public void print(OutputStreamWriter outStream, String indent) throws Exception {
        if (m_result1.isPresent() && m_result2.isPresent()) {
            outStream.write(indent);
            outStream.write("QUESTION_MARK_EXPR");
            outStream.write("\n");
            m_condition.print(outStream, indent + "  ");
            outStream.write(indent + "  ");
            outStream.write("QUESTION_MARK");
            outStream.write("\n");
            m_result1.get().print(outStream, indent + "  ");
            outStream.write(indent + "  ");
            outStream.write("DOUBLE_COLON");
            outStream.write("\n");
            m_result2.get().print(outStream, indent + "  ");
        } else {
            m_condition.print(outStream, indent);
        }
        outStream.flush();
    }

    @Override
    public InstrIntf codegen(CompileEnvIntf compileEnvIntf){
        Optional<InstrIntf> result1;
        Optional<InstrIntf> result2;
        if (m_result1.isPresent() && m_result2.isPresent()){
            result1 = Optional.of(m_result1.get().codegen(compileEnvIntf));
            result2 = Optional.of(m_result2.get().codegen(compileEnvIntf));
        } else {
            result1 = Optional.empty();
            result2 = Optional.empty();
        }
        InstrIntf condition = m_condition.codegen(compileEnvIntf);
        InstrIntf questionMarkExpr = new InstrQuestionMarkExpr(result1, result2, condition);
        compileEnvIntf.addInstr(questionMarkExpr);
        return  questionMarkExpr;
    }
}
