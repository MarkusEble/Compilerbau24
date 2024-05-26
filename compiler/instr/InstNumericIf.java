package compiler.instr;

import compiler.ExecutionEnvIntf;
import compiler.InstrBlock;
import compiler.InstrIntf;
import compiler.TokenIntf;

import java.io.OutputStreamWriter;

public class InstNumericIf extends compiler.InstrIntf{
    private compiler.InstrBlock m_pos;
    private compiler.InstrBlock m_neg;
    private compiler.InstrBlock m_zero;
    private compiler.InstrIntf m_expr;

    public InstNumericIf(InstrBlock m_pos, InstrBlock m_neg, InstrBlock m_zero, InstrIntf m_expr) {
        this.m_pos = m_pos;
        this.m_neg = m_neg;
        this.m_zero = m_zero;
        this.m_expr = m_expr;
    }

    @Override
    public void execute(ExecutionEnvIntf env) throws Exception {
        if (m_expr.getValue() > 0) {
            env.setInstrIter(m_pos.getIterator());
//            m_pos.execute(env);
        } else if (m_expr.getValue() < 0) {
            env.setInstrIter(m_neg.getIterator());
//            m_neg.execute(env);
        } else {
            env.setInstrIter(m_zero.getIterator());
//            m_zero.execute(env);
        }
    }

    @Override
    public void trace(OutputStreamWriter os) throws Exception {
        os.write(String.format("%%%d = %s %%%d\n", m_id, TokenIntf.Type.NUMERIC_IF.toString(), m_expr.getId()));
    }
}
