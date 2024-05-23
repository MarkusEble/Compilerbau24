package compiler.instr;

import compiler.ExecutionEnvIntf;

import java.io.OutputStreamWriter;

public class InstNumericIf extends compiler.InstrIntf{
    private compiler.InstrIntf m_pos;
    private compiler.InstrIntf m_neg;
    private compiler.InstrIntf m_zero;
    private compiler.InstrIntf m_expr;

    @Override
    public void execute(ExecutionEnvIntf env) throws Exception {
        if (m_expr.getValue() > 0) {
            m_pos.execute(env);
        } else if (m_expr.getValue() < 0) {
            m_neg.execute(env);
        } else {
            m_zero.execute(env);
        }
    }

    @Override
    public void trace(OutputStreamWriter os) throws Exception {
        os.write("NUMERIC IF\n");
        
    }
}
