package compiler.instr;

import compiler.ExecutionEnvIntf;
import compiler.InstrIntf;

import java.io.OutputStreamWriter;
import java.util.ListIterator;

/**
 * Instruction to set execution to previous iterator
 * push value to stack,
 * to be used with _InstPopReturn_
 */
public class InstrPushReturn extends InstrIntf {
    private final compiler.InstrIntf returnValue;
    public InstrPushReturn(InstrIntf returnValue) {
        this.returnValue = returnValue;
    }

    @Override
    public void execute(ExecutionEnvIntf env) throws Exception {
        // set execution to previous iterator
        // push value to stack, to be popped by InstPopReturn
        ListIterator<InstrIntf> retAddress = env.popReturnAddr();
        env.setInstrIter(retAddress);
        env.push(returnValue.getValue());
    }

    @Override
    public void trace(OutputStreamWriter os) throws Exception {
        os.write("PUSH RETURN\n");
    }
}
