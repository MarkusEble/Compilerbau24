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
        os.write("LITERAL ");
        os.write(Integer.toString(m_value));
        os.write("\n");
    }

}
