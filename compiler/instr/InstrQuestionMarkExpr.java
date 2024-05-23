package compiler.instr;

import compiler.ExecutionEnvIntf;
import compiler.InstrIntf;
import compiler.TokenIntf;

import java.io.OutputStreamWriter;
import java.util.Optional;

public class InstrQuestionMarkExpr extends InstrIntf{
    private final Optional<InstrIntf> m_lhsInstruction, m_rhsInstruction;
    private final InstrIntf m_condition;

    public InstrQuestionMarkExpr(Optional<InstrIntf> lhsInstruction, Optional<InstrIntf> rhsInstruction, InstrIntf condition) {
        this.m_lhsInstruction = lhsInstruction;
        this.m_rhsInstruction = rhsInstruction;
        this.m_condition = condition;
    }

    @Override
    public void execute(ExecutionEnvIntf env) throws Exception {
        if (m_lhsInstruction.isPresent() && m_rhsInstruction.isPresent()) {
            m_value = m_condition.getValue() > 0 ? m_lhsInstruction.get().getValue() : m_rhsInstruction.get().getValue();
        } else {
            m_value = m_condition.getValue();
        }
    }

    @Override
    public void trace(OutputStreamWriter os) throws Exception {
        os.write(String.format("%%%d = %%%d %s %%%d, %%%d\n", m_id, m_condition.getId(), TokenIntf.Type.QUESTIONMARK.toString(), m_lhsInstruction.get().getId(), m_rhsInstruction.get().getId()));     
    }
}
