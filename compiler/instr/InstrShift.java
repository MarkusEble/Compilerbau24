package compiler.instr;

import compiler.ExecutionEnvIntf;
import compiler.TokenIntf;

import java.io.OutputStreamWriter;

public class InstrShift extends compiler.InstrIntf{
    private compiler.InstrIntf m_lhs;
    private compiler.InstrIntf m_rhs;
    private compiler.TokenIntf.Type m_op;

    public InstrShift(compiler.TokenIntf.Type op, compiler.InstrIntf lhs, compiler.InstrIntf rhs) {
        m_op = op;
        m_lhs = lhs;
        m_rhs = rhs;
    }

    @Override
    public void execute(ExecutionEnvIntf env) throws Exception {
        if (m_op == TokenIntf.Type.SHIFTRIGHT) {
            m_value = m_lhs.getValue() >> m_rhs.getValue();
        } else {
            m_value = m_lhs.getValue() << m_rhs.getValue();
        }
    }

    @Override
    public void trace(OutputStreamWriter os) throws Exception {
        os.write(String.format("%%%d = %s %%%d, %%%d\n", m_id, m_op.toString(), m_lhs.getId(), m_rhs.getId()));     
    }
}
