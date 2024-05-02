package compiler;

import java.io.OutputStreamWriter;

public abstract class InstrIntf {
    protected int m_value = 0;
	/**
	 * execute this instruction
	 */
	abstract public void execute(ExecutionEnvIntf env);
	/**
	 * trace this instruction
	 */
	abstract public void trace(OutputStreamWriter os) throws Exception;
	/**
	 * return the result value of the instruction
	 */
	int getValue() {
	    return m_value;
	}

}
