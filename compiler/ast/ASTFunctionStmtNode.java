package compiler.ast;

import java.io.OutputStreamWriter;
import java.util.List;

public class ASTFunctionStmtNode extends ASTStmtNode{
    private final String functionName;
    private final List<String> paramList;
    private final List<ASTStmtNode> statementList;
    private final compiler.FunctionTable functionTable;

    public ASTFunctionStmtNode(String functionName, List<String> paramList, List<ASTStmtNode> statementList, compiler.FunctionTable functionTable) {
        this.functionName = functionName;
        this.paramList = paramList;
        this.statementList = statementList;
        this.functionTable = functionTable;
    }

    @Override
    public void print(OutputStreamWriter outStream, String indent) throws Exception {
        outStream.write(indent + "FUNCTION ");
        outStream.write(functionName + "LPAREN ");
        for (String p : paramList) {
            outStream.write(p + " ");
        }
        outStream.write("LPAREN LBRACE\n");
        for (ASTStmtNode stmt : statementList) {
            stmt.print(outStream,indent + "  ");
        }
        outStream.write(indent + "LBRACE");
    }

    @Override
    public void execute() {
        functionTable.createFunction(functionName,paramList);
    }
}
