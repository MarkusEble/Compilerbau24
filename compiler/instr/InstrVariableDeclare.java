package compiler.instr;

import compiler.ExecutionEnvIntf;
import compiler.Symbol;
import java.io.OutputStreamWriter;

public class InstrVariableDeclare extends compiler.InstrIntf {

    Symbol symbol;

    public InstrVariableDeclare(Symbol symbol) {
        this.symbol = symbol;
    }

    @Override
    public void execute(ExecutionEnvIntf env) throws Exception {
    }

    @Override
    public void trace(OutputStreamWriter os) throws Exception {
        os.write("DECLARE " + this.symbol.m_name);
        os.write("\n");
    }
}
