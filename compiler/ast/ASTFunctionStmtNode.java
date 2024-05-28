package compiler.ast;

import compiler.CompileEnvIntf;
import compiler.InstrIntf;
import compiler.instr.InstrFunctionExcecution;

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
        outStream.write(functionName + " LPAREN ");
        for (String p : paramList) {
            outStream.write(p + " ");
        }
        outStream.write("RPAREN LBRACE\n");
        statementList.print(outStream, indent + "  ");
        outStream.write(indent + "RBRACE\n");
    }

    @Override
    public void execute() {
        statementList.execute();
    }

    @Override
    public void codegen(CompileEnvIntf env) {
        compiler.InstrBlock currentBlock = env.getCurrentBlock();
        compiler.InstrBlock functionBlock = env.createBlock("FUNCTION");
        functionTable.createFunction(functionName, functionBlock, paramList);

        compiler.InstrIntf functionExecution = new InstrFunctionExcecution(symbolTable, paramList);

        env.setCurrentBlock(functionBlock);
        env.addInstr(functionExecution);
        statementList.codegen(env);

        env.setCurrentBlock(currentBlock);
    }
}
