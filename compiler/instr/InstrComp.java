package compiler.instr;

import compiler.ExecutionEnvIntf;
import compiler.Token;
import compiler.TokenIntf;

import java.io.OutputStreamWriter;

public class InstrComp extends compiler.InstrIntf{
    private compiler.InstrIntf lhsInstr, rhsInstr;
    private Token.Type type;

    public InstrComp(compiler.InstrIntf lhsInstr, compiler.InstrIntf rhsInstr, Token.Type type){
        this.lhsInstr = lhsInstr;
        this.rhsInstr = rhsInstr;
        this.type = type;
    }

    @Override
    public void execute(ExecutionEnvIntf env) throws Exception {
        if(type == Token.Type.GREATER){
            m_value = lhsInstr.getValue() > rhsInstr.getValue() ? 1 : 0;
        } else if(type == Token.Type.LESS){
            m_value = lhsInstr.getValue() < rhsInstr.getValue() ? 1 : 0;
        } else {
            m_value = lhsInstr.getValue() == rhsInstr.getValue() ? 1 : 0;
        }
    }

    @Override
    public void trace(OutputStreamWriter os) throws Exception {
        os.write(String.format("%%%d = %s %%%d, %%%d\n", m_id, type.toString(), lhsInstr.getId(), rhsInstr.getId()));
    }
}
