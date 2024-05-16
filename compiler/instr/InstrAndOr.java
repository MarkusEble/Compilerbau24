package compiler.instr;

import java.io.OutputStreamWriter;

import compiler.ExecutionEnvIntf;
import compiler.TokenIntf.Type;

public class InstrAndOr extends compiler.InstrIntf  {
    private compiler.InstrIntf rhs, lhs; 
    private compiler.TokenIntf.Type op;

    public InstrAndOr(compiler.TokenIntf.Type op, compiler.InstrIntf lhs, compiler.InstrIntf rhs) {
        this.op = op;
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public void execute(ExecutionEnvIntf env) throws Exception {
        if(op == Type.AND){
            m_value = lhs.getValue() == 1 || rhs.getValue() == 1 ? 1 : 0;
        } else if(op == Type.OR){
            m_value = lhs.getValue() == 1 && rhs.getValue() == 1 ? 1 : 0;
        }
    }

    @Override
    public void trace(OutputStreamWriter os) throws Exception {
        os.write(op.toString());
        os.write("\n");
    }
}
