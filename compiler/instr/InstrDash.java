package compiler.instr;

import compiler.ExecutionEnvIntf;

import java.io.OutputStreamWriter;

public class InstrDash extends compiler.InstrIntf {
    private compiler.InstrIntf m_lhs;
    private compiler.InstrIntf m_rhs;

    public InstrDash(compiler.InstrIntf lhs, compiler.InstrIntf rhs) {
        m_lhs = lhs;
        m_rhs = rhs;
    }

    @Override
    public void execute(ExecutionEnvIntf env) throws Exception {
        m_value = (int) Math.pow(m_lhs.getValue(), m_rhs.getValue());
    }

    @Override
    public void trace(OutputStreamWriter os) throws Exception {
        os.write("DASH");
        os.write("\n");
    }

}
