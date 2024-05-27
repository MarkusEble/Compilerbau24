package compiler.instr;

import compiler.ExecutionEnvIntf;
import compiler.TokenIntf;

import java.io.OutputStreamWriter;

public class InstrUnary extends compiler.InstrIntf{
    private final compiler.TokenIntf.Type m_type;
    private final compiler.InstrIntf m_child;
    public InstrUnary(compiler.TokenIntf.Type type, compiler.InstrIntf child) {
        this.m_child = child;
        this.m_type = type;
    }

    @Override
    public void execute(ExecutionEnvIntf env) throws Exception {
        int result = m_child.getValue();
        if(m_type == compiler.TokenIntf.Type.NOT){
            m_value = result > 0 ? 0 : 1;
        } else if (m_type == TokenIntf.Type.MINUS) {
            m_value =   - result;
        }
    }

    @Override
    public void trace(OutputStreamWriter os) throws Exception {
        os.write(String.format("%%%d = %s %%%d\n", m_id, m_type.toString(), m_child.getId()));
    }
}
