package compiler.instr;

import compiler.ExecutionEnvIntf;

import java.io.OutputStreamWriter;

public class InstrUnaryExpr extends compiler.InstrIntf {
    private compiler.InstrIntf m_lhs;
    private compiler.InstrIntf m_rhs;
    private compiler.TokenIntf.Type m_op;
    public InstrUnaryExpr(compiler.TokenIntf.Type op, compiler.InstrIntf child) {
        m_op = op;
        m_lhs = child;
    }

    @Override
    public void execute(ExecutionEnvIntf env) throws Exception {
    }

    @Override
    public void trace(OutputStreamWriter os) throws Exception {
        os.write(m_op.toString());
    }

}