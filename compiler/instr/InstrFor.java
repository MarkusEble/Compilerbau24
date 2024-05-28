package compiler.instr;

import compiler.ExecutionEnvIntf;
import compiler.ast.ASTBlockStmtNode;
import compiler.ast.ASTStmtNode;

import java.io.OutputStreamWriter;
import java.util.List;

public class InstrFor extends compiler.InstrIntf {

    List<ASTStmtNode> m_stmts;
    ASTBlockStmtNode m_block;

    public InstrFor(List<ASTStmtNode> stmts, ASTBlockStmtNode block) {
        m_stmts = stmts;
        m_block = block;
    }

    @Override
    public void execute(ExecutionEnvIntf env) throws Exception {

    }

    @Override
    public void trace(OutputStreamWriter os) throws Exception {

    }

}
