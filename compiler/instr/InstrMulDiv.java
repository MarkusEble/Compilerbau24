package compiler.instr;

import compiler.ExecutionEnvIntf;
import compiler.TokenIntf;

import java.io.OutputStreamWriter;

public class InstrMulDiv extends compiler.InstrIntf {
    private compiler.InstrIntf m_lhsExpr;
    private compiler.InstrIntf m_rhsExpr;
    private compiler.TokenIntf.Type m_op;

    public InstrMulDiv(compiler.TokenIntf.Type op, compiler.InstrIntf lhsExpr, compiler.InstrIntf rhsExpr) {
        this.m_lhsExpr = lhsExpr;
        this.m_rhsExpr = rhsExpr;
        this.m_op = op;
    }

    @Override
    public void execute(ExecutionEnvIntf env) throws Exception {
        if (m_op == TokenIntf.Type.MUL) {
            m_value = m_lhsExpr.getValue() * m_rhsExpr.getValue();
        } else {
            m_value = m_lhsExpr.getValue() / m_rhsExpr.getValue();
        }
    }

    @Override
    public void trace(OutputStreamWriter os) throws Exception {
        os.write(String.format("%%%d = %s %%%d, %%%d\n", m_id, m_op.toString(), m_lhsExpr.getId(), m_rhsExpr.getId()));     
    }
}