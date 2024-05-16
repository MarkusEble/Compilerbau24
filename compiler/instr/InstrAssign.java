package compiler.instr;

import java.io.OutputStreamWriter;

import compiler.ExecutionEnvIntf;

public class InstrAssign extends compiler.InstrIntf {
    compiler.Symbol m_lhs;
    compiler.InstrIntf m_rhs;

    public InstrAssign(compiler.Symbol lhs, compiler.InstrIntf rhs) {
        m_lhs = lhs;
        m_rhs = rhs;
    }

    @Override
    public void execute(ExecutionEnvIntf env) throws Exception {
        m_lhs.m_number = m_rhs.getValue();
    }

    @Override
    public void trace(OutputStreamWriter os) throws Exception {
        os.write("ASSIGN ");
        os.write(m_lhs.m_name);
        os.write("\n");
    }

}
