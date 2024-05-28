package compiler.instr;

import compiler.ExecutionEnvIntf;
import compiler.InstrIntf;

import java.io.OutputStreamWriter;
import java.util.ListIterator;

public class InstrPushReturn extends InstrIntf {
    private final compiler.InstrIntf returnValue;
    public InstrPushReturn(InstrIntf returnValue) {
        this.returnValue = returnValue;
    }

    @Override
    public void execute(ExecutionEnvIntf env) throws Exception {
        ListIterator<InstrIntf> retAddress = env.popReturnAddr();
        env.setInstrIter(retAddress);
        env.push(returnValue.getValue());
    }

    @Override
    public void trace(OutputStreamWriter os) throws Exception {
        os.write("PUSH RETURN\n");
    }
}
