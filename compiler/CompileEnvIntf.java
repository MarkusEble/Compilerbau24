package compiler;

import java.io.OutputStream;
import java.io.OutputStreamWriter;

public interface CompileEnvIntf {
	/**
	 * generate AST and instruction program from source code
	 */
    public void compile() throws Exception;

	/**
	 * dump generated AST
	 */
    public void dumpAst(OutputStream outStream) throws Exception;


	/**
	 * dump generated program
	 */
	public void dump(OutputStream outStream) throws Exception;

    /**
	 * execute generated program
	 */
	public void execute(OutputStreamWriter outStream) throws Exception;

		/**
	 *  add instruction at end of current block
	 */
	public void addInstr(InstrIntf instr);
	
	/**
	 *  create new instruction block
	 */
	public InstrBlock createBlock();
	public InstrBlock createBlock(String name);
	
	/**
	 *  set current instruction block
	 */
	public void setCurrentBlock(InstrBlock instrBlock);
	
	/**
	 *  get current instruction block
	 */
	public InstrBlock getCurrentBlock();

	/**
	 *  get symbol table
	 */
	public SymbolTable getSymbolTable();

	/**
	 *  get function table
	 */
	public FunctionTable getFunctionTable();

	void pushLoopStack(InstrBlock instrBlock);

	InstrBlock peekLoopStack();

	InstrBlock popLoopStack();

	Symbol createUniqueSymbol(String prefix);

}
