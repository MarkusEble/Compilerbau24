package compiler.ast;

import compiler.*;
import compiler.instr.InstrFunctionCall;
import compiler.instr.InstrPopReturn;

import java.io.OutputStreamWriter;
import java.util.List;
import java.util.ListIterator;

public class ASTExprFunctionCallNode extends ASTExprNode{
    private final List<ASTExprNode> paramList;
    private final String functionName;


    public ASTExprFunctionCallNode(String functionName, List<ASTExprNode> paramList) {
        this.paramList = paramList;
        this.functionName = functionName;
    }

    @Override
    public int eval() {
        // Eval not possible
        return 0;
    }

    @Override
    public void print(OutputStreamWriter outStream, String indent) throws Exception {
        outStream.write(indent);
        outStream.write("CALL ");
        outStream.write(functionName + "\n");
    }

    @Override
    public InstrIntf codegen(CompileEnvIntf env) {
        List<InstrIntf>parameterValues = paramList.stream().map(node -> node.codegen(env)).toList();

        compiler.InstrBlock functionBlock = env.getFunctionTable().getFunction(functionName).m_body;

        InstrIntf functionInstr = new InstrFunctionCall(parameterValues,functionBlock);
        env.addInstr(functionInstr);
        InstrIntf returnInstr = new InstrPopReturn();
        env.addInstr(returnInstr);
        return returnInstr;
    }
}
