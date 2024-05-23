package compiler.instr;

import java.io.OutputStreamWriter;

import compiler.ExecutionEnvIntf;
import compiler.TokenIntf;

public class InstrBitAndOr extends compiler.InstrIntf {
    private compiler.InstrIntf m_lhs;
    private compiler.InstrIntf m_rhs;
    private compiler.TokenIntf.Type m_op;

    public InstrBitAndOr(compiler.TokenIntf.Type op, compiler.InstrIntf lhs, compiler.InstrIntf rhs) {
        m_op = op;
        m_lhs = lhs;
        m_rhs = rhs;
    }

    @Override
    public void execute(ExecutionEnvIntf env) throws Exception {
        if (m_op == compiler.TokenIntf.Type.BITAND) {
            m_value = m_lhs.getValue() & m_rhs.getValue();
        } else { // must be OR
            m_value = m_lhs.getValue() | m_rhs.getValue();
        }
    }

    @Override
    public void trace(OutputStreamWriter os) throws Exception {
        os.write(String.format("%%%d = %s %%%d, %%%d\n", m_id, m_op.toString(), m_lhs.getId(), m_rhs.getId()));     
    }
}
