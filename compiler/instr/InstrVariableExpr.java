package compiler.instr;

import compiler.ExecutionEnvIntf;

import java.io.OutputStreamWriter;

public class InstrVariableExpr extends compiler.InstrIntf {

    private String m_variable;

    public InstrVariableExpr(String m_variable) {
        this.m_variable = m_variable;
    }

    @Override
    public void execute(ExecutionEnvIntf env) throws Exception {
        m_value = env.getSymbol(m_variable).m_number;
    }

    @Override
    public void trace(OutputStreamWriter os) throws Exception {
        os.write("VARIABLE ");
        os.write(m_variable);
        os.write("\n");
    }
}
