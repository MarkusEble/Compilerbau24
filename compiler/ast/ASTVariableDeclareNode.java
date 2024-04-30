package compiler.ast;

import compiler.SymbolTableIntf;

import java.io.OutputStreamWriter;

public class ASTVariableDeclareNode extends ASTStmtNode{
    String symbol;
    SymbolTableIntf symbolTable;

    public ASTVariableDeclareNode(String symbol, SymbolTableIntf symbolTable) {
        this.symbol = symbol;
        this.symbolTable = symbolTable;
    }

    @Override
    public void execute() {
        this.symbolTable.createSymbol(this.symbol);
    }

    @Override
    public void print(OutputStreamWriter outStream, String indent) throws Exception {
        outStream.write(indent + "DECLARE " + this.symbol);
        outStream.write("\n");
    }
}
