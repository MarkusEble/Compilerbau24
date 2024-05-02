package compiler.instr;

import java.io.OutputStreamWriter;

import compiler.ExecutionEnvIntf;

public class InstrPlusMinus extends compiler.InstrIntf {
    private compiler.InstrIntf m_lhs;
    private compiler.InstrIntf m_rhs;
    private compiler.TokenIntf.Type m_op;

    public InstrPlusMinus(compiler.TokenIntf.Type op, compiler.InstrIntf lhs, compiler.InstrIntf rhs) {
        m_op = op;
        m_lhs = lhs;
        m_rhs = rhs;
    }

    @Override
    public void execute(ExecutionEnvIntf env) throws Exception {
        if (m_op == compiler.TokenIntf.Type.PLUS) {
            m_value = m_lhs.getValue() + m_rhs.getValue();
        } else {
            m_value = m_lhs.getValue() - m_lhs.getValue();
        }
    }

    @Override
    public void trace(OutputStreamWriter os) throws Exception { 
        os.write(m_op.toString());
        os.write("\n");
    }

}
