package compiler;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.*;

public class ExecutionEnv implements ExecutionEnvIntf {
    private SymbolTable m_symbolTable;
    private Stack<Integer> m_valueStack;
    private Stack<ListIterator<InstrIntf>> m_returnAddrStack;
    private ListIterator<InstrIntf> m_instrIter;
    private OutputStreamWriter m_outStream;
    private FunctionTable m_functionTable;
    private boolean m_trace;

    public ExecutionEnv(FunctionTable functionTable, SymbolTable symbolTable, OutputStreamWriter outStream, boolean trace) throws Exception {
		m_symbolTable = symbolTable;
		m_valueStack = new Stack<Integer>();
		m_returnAddrStack = new Stack<ListIterator<InstrIntf>>();
		m_outStream = outStream;
		m_functionTable = functionTable;
		m_trace = trace;
	}
	
    @Override
    public FunctionTable getFunctionTable() {
        return m_functionTable;
    }
    
	public void push(int value) {
		m_valueStack.push(value);
	}
	
	public int pop() {
        return m_valueStack.pop();
	}
	
	public Symbol getSymbol(String symbolName) {
		return m_symbolTable.getSymbol(symbolName);
	}

	public void pushReturnAddr(ListIterator<InstrIntf> instrIter) { // instrIter == program counter
		m_returnAddrStack.push(instrIter);
	}
	
    public ListIterator<InstrIntf> popReturnAddr() {
        return m_returnAddrStack.pop();
    }
    
    public void execute(ListIterator<InstrIntf> instrIter) throws Exception {
        m_instrIter = instrIter;
        while (m_instrIter.hasNext()) {
            InstrIntf nextInstr = m_instrIter.next();
            if (m_trace) {
                nextInstr.trace(getOutputStream());
                m_outStream.flush();
            }
            nextInstr.execute(this);
        }
    }
	
	public OutputStreamWriter getOutputStream() {
		return m_outStream;
	}

    @Override
    public void setInstrIter(ListIterator<InstrIntf> instrIter) {
        m_instrIter = instrIter;        
    }

    @Override
    public ListIterator<InstrIntf> getInstrIter() {
        return m_instrIter;
    }
}
