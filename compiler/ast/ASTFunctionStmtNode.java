package compiler.ast;

import compiler.CompileEnvIntf;
import compiler.instr.InstrFunctionLoadParameter;

import java.io.OutputStreamWriter;
import java.util.List;

public class ASTFunctionStmtNode extends ASTStmtNode {
    private final String functionName;
    private final List<String> paramList;
    private final ASTStmtNode statementList;
    private final compiler.FunctionTableIntf functionTable;
    private final compiler.SymbolTableIntf symbolTable;

    public ASTFunctionStmtNode(
            String functionName,
            List<String> paramList,
            ASTStmtNode statementList,
            compiler.FunctionTableIntf functionTable,
            compiler.SymbolTableIntf symbolTable
    ) {
        this.functionName = functionName;
        this.paramList = paramList;
        this.statementList = statementList;
        this.functionTable = functionTable;
        this.symbolTable = symbolTable;
    }

    @Override
    public void print(OutputStreamWriter outStream, String indent) throws Exception {
        outStream.write(indent + "FUNCTION ");
        outStream.write(functionName + " ( ");
        for (String p : paramList) {
            outStream.write(p + " ");
        }
        outStream.write(") {\n");
        statementList.print(outStream, indent + "  ");
        outStream.write(indent + "}\n");
    }

    @Override
    public void execute() {
        // execute statements in function
        statementList.execute();
    }

    @Override
    public void codegen(CompileEnvIntf env) {
        // add instructions of a function to new code block
        // behaves neutral current Block
        compiler.InstrBlock currentBlock = env.getCurrentBlock();

        // create and register new code block
        compiler.InstrBlock functionBlock = env.createBlock("FUNCTION");
        functionTable.createFunction(functionName, functionBlock, paramList);

        env.setCurrentBlock(functionBlock);
        // load parameter instruction
        compiler.InstrIntf functionExecution = new InstrFunctionLoadParameter(symbolTable, paramList);
        env.addInstr(functionExecution);

        statementList.codegen(env);

        env.setCurrentBlock(currentBlock);
    }
}
