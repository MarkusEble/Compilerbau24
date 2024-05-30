package compiler.instr;

import compiler.ExecutionEnvIntf;
import compiler.InstrBlock;
import compiler.InstrIntf;

import java.io.OutputStreamWriter;
import java.util.List;

public class InstrFunctionCall extends InstrIntf {
    private final List<InstrIntf> parameters;
    private final compiler.InstrBlock functionBlock;

    public InstrFunctionCall(List<InstrIntf> parameters, InstrBlock functionBlock) {
        this.parameters = parameters;
        this.functionBlock = functionBlock;
    }

    @Override
    public void execute(ExecutionEnvIntf env) throws Exception {
        // push return address and parameters to stack
        env.pushReturnAddr(env.getInstrIter());
        parameters.forEach(param -> env.push(param.getValue()));
        // change instructions to function
        env.setInstrIter(functionBlock.getIterator());
    }

    @Override
    public void trace(OutputStreamWriter os) throws Exception {
        os.write("CALL FUNCTION: " + functionBlock.getName() + "\n");
    }
}
