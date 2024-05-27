package compiler.instr;

import java.io.OutputStreamWriter;

import compiler.ExecutionEnvIntf;

public class InstrCondJump extends compiler.InstrIntf {
    compiler.InstrIntf m_cond;
    compiler.InstrBlock m_trueBlock;
    compiler.InstrBlock m_falseBlock;

    public InstrCondJump(compiler.InstrIntf m_cond, compiler.InstrBlock m_trueBlock, compiler.InstrBlock m_falseBlock){
        this.m_cond = m_cond;
        this.m_trueBlock = m_trueBlock;
        this.m_falseBlock = m_falseBlock;
    }

    @Override
    public void execute(ExecutionEnvIntf env) throws Exception {
        int cond = m_cond.getValue();
        if (cond != 0) {
            env.setInstrIter(m_trueBlock.getIterator());
        } else {
            env.setInstrIter(m_falseBlock.getIterator());
        }
    }

    @Override
    public void trace(OutputStreamWriter os) throws Exception {
        os.write("JUMPCOND ");
        os.write(m_trueBlock.getName());
        os.write(", ");
        os.write(m_falseBlock.getName());
        os.write("\n");
    }

}
