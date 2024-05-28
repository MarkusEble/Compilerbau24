package compiler.instr;

import compiler.ExecutionEnvIntf;
import compiler.InstrIntf;

import java.io.OutputStreamWriter;

/**
 * get values from Stack,
 * to be used with _PushReturn_
 */
public class InstrPopReturn extends InstrIntf {
    @Override
    public void execute(ExecutionEnvIntf env) throws Exception {
        m_value = env.pop();
    }

    @Override
    public void trace(OutputStreamWriter os) throws Exception {
        os.write("POP RETURN\n");
    }
}
