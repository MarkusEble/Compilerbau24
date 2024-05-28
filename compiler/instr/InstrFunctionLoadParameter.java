package compiler.instr;

import compiler.ExecutionEnvIntf;
import compiler.InstrIntf;
import compiler.SymbolTableIntf;

import java.io.OutputStreamWriter;
import java.util.List;

public class InstrFunctionLoadParameter extends InstrIntf {
    private final compiler.SymbolTableIntf symbolTable;
    private final List<String> paramList;


    public InstrFunctionLoadParameter(SymbolTableIntf symbolTable, List<String> paramList) {
        this.symbolTable = symbolTable;
        this.paramList = paramList;
    }

    @Override
    public void execute(ExecutionEnvIntf env) throws Exception {
        // load parameter values into symbol table
        paramList.forEach(param -> {
            compiler.Symbol symbol = symbolTable.getSymbol(param);
            symbol.m_number = env.pop();
        });
    }

    @Override
    public void trace(OutputStreamWriter os) throws Exception {
        os.write("LOAD PARAMS\n");
    }
}
