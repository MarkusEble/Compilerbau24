package compiler.instr;

import java.io.OutputStreamWriter;

import compiler.ExecutionEnvIntf;

public class InstrJmp extends compiler.InstrIntf {
    compiler.InstrBlock m_target;

    public InstrJmp(compiler.InstrBlock target) {
        m_target = target;
    }

    @Override
    public void execute(ExecutionEnvIntf env) throws Exception {
        env.setInstrIter(m_target.getIterator());
    }

    @Override
    public void trace(OutputStreamWriter os) throws Exception {
        os.write("JUMP ");
        os.write(m_target.getName());
        os.write("\n");
    }

}
