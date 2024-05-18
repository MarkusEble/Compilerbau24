package compiler.instr;

import compiler.ExecutionEnvIntf;
import compiler.TokenIntf;

import java.io.OutputStreamWriter;

public class InstrArrow extends compiler.InstrIntf{
    private compiler.InstrIntf m_lhs;
    private compiler.InstrIntf m_rhs;

    public InstrArrow(compiler.InstrIntf lhs, compiler.InstrIntf rhs) {
        m_lhs = lhs;
        m_rhs = rhs;
    }

    @Override
    public void execute(ExecutionEnvIntf env) throws Exception {
        int lresult = m_lhs.getValue();
        int rresult = m_rhs.getValue();
        if (lresult>=rresult){
            m_value = lresult-rresult;
        }else{
            m_value = rresult+lresult;
        }
    }

    @Override
    public void trace(OutputStreamWriter os) throws Exception {
        os.write(String.format("%%%d = %s %%%d, %%%d\n", m_id, TokenIntf.Type.ARROW.toString(), m_lhs.getId(), m_rhs.getId()));     
    }
}
