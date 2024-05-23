package compiler.instr;

import compiler.ExecutionEnvIntf;
import compiler.InstrIntf;
import compiler.TokenIntf;

import java.io.OutputStreamWriter;
import java.util.Optional;

public class InstrQuestionMarkExpr extends InstrIntf {
    private final InstrIntf m_lhsInstruction, m_rhsInstruction;
    private final InstrIntf m_condition;

    public InstrQuestionMarkExpr(InstrIntf lhsInstruction, InstrIntf rhsInstruction, InstrIntf condition) {
        this.m_lhsInstruction = lhsInstruction;
        this.m_rhsInstruction = rhsInstruction;
        this.m_condition = condition;
    }

    @Override
    public void execute(ExecutionEnvIntf env) throws Exception {
        m_value = m_condition.getValue() > 0 ? m_lhsInstruction.getValue() : m_rhsInstruction.getValue();
    }

    @Override
    public void trace(OutputStreamWriter os) throws Exception {
        os.write(String.format("%%%d = %%%d %s %%%d, %%%d\n", m_id, m_condition.getId(), TokenIntf.Type.QUESTIONMARK, m_lhsInstruction.getId(), m_rhsInstruction.getId()));
    }
}
