package compiler.ast;

import compiler.CompileEnvIntf;
import compiler.InstrIntf;
import compiler.instr.InstrPushReturn;

import java.io.OutputStreamWriter;

public class ASTReturnStmtNode extends ASTStmtNode{
    private final compiler.ast.ASTExprNode returnValue;

    public ASTReturnStmtNode(ASTExprNode returnValue) {
        this.returnValue = returnValue;
    }

    @Override
    public void print(OutputStreamWriter outStream, String indent) throws Exception {
        outStream.write(indent + "RETURN\n");
        returnValue.print(outStream, indent + "  ");
    }

    @Override
    public void execute() {
        // not needed
    }

    @Override
    public void codegen(CompileEnvIntf env) {
        InstrIntf ret = returnValue.codegen(env);
        // create return instruction, to push values to stack
        InstrIntf returnInstr = new InstrPushReturn(ret);
        env.addInstr(returnInstr);
    }
}
