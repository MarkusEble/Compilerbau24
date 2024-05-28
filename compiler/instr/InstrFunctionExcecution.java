package compiler.instr;

import compiler.ExecutionEnvIntf;
import compiler.InstrIntf;
import compiler.SymbolTable;
import compiler.SymbolTableIntf;

import java.io.OutputStreamWriter;
import java.util.List;

public class InstrFunctionExcecution extends InstrIntf {
    private final compiler.SymbolTableIntf symbolTable;
    private final List<String> paramList;


    public InstrFunctionExcecution(SymbolTableIntf symbolTable, List<String> paramList) {
        this.symbolTable = symbolTable;
        this.paramList = paramList;
    }

    @Override
    public void execute(ExecutionEnvIntf env) throws Exception {
        paramList.forEach(param -> {
            compiler.Symbol symbol = symbolTable.getSymbol(param);
            symbol.m_number = env.pop();
        });
    }

    @Override
    public void trace(OutputStreamWriter os) throws Exception {
        os.write("FUNCTION: LOAD PARAMS\n");
    }
}
