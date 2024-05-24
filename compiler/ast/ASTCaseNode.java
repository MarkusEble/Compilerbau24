package compiler.ast;

import compiler.CompileEnvIntf;
import java.io.OutputStreamWriter;
import java.util.List;

public class ASTCaseNode extends ASTStmtNode {

    private int m_number;
    private List<ASTStmtNode> m_stmtList;

    public ASTCaseNode(int m_number, List<ASTStmtNode> m_stmtList) {
        this.m_number = m_number;
        this.m_stmtList = m_stmtList;
    }

    @Override
    public void print(OutputStreamWriter outStream, String indent) throws Exception {
        outStream.write(indent + "CASE " + m_number + "\n");
        for (ASTStmtNode stmt : m_stmtList) {
            stmt.print(outStream, indent + "  ");
        }
    }

    @Override
    public void execute() {
        for (ASTStmtNode stmt : m_stmtList) {
            stmt.execute();
        }
    }

    @Override
    public void codegen(CompileEnvIntf env) {
        for (ASTStmtNode stmt : m_stmtList) {
            stmt.codegen(env);
        }
    }

    public int getNumber(){
        return m_number;
    }

}
