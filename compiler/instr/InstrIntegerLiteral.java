package compiler.instr;

import java.io.OutputStreamWriter;

import compiler.ExecutionEnvIntf;

public class InstrIntegerLiteral extends compiler.InstrIntf {
    public InstrIntegerLiteral(String value) {
        m_value = Integer.parseInt(value);
    }

    @Override
    public void execute(ExecutionEnvIntf env) throws Exception {
    }

    @Override
    public void trace(OutputStreamWriter os) throws Exception {
        os.write(String.format("%%%d = LITERAL %d\n", m_id, m_value));
    }

}
