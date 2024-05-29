package compiler.ast;

import compiler.*;
import compiler.instr.InstrFunctionCall;
import compiler.instr.InstrPopReturn;

import java.io.OutputStreamWriter;
import java.util.List;

public class ASTExprFunctionCallNode extends ASTExprNode {
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
        // Generate parameters
        List<InstrIntf> parameterValues = paramList.stream().map(node -> node.codegen(env)).toList();

        // Get instructions of function
        compiler.FunctionInfo function = env.getFunctionTable().getFunction(functionName);
        compiler.InstrBlock functionBlock = function.m_body;

        // check parameters // no type check
        List<String> requiredParameters = function.varNames;
        if (requiredParameters.size() != parameterValues.size()) {

            throw new Error(String.format("""
                            Amount of provided Parameters differs!
                            Calling the function %s[%s]
                            provided %d, expected %d
                            """,
                    function.m_name,
                    String.join(", ", requiredParameters),
                    parameterValues.size(),
                    requiredParameters.size()
            ));
        }

        // Create instructions for function and return call
        InstrIntf functionInstr = new InstrFunctionCall(parameterValues, functionBlock);
        InstrIntf returnInstr = new InstrPopReturn();
        env.addInstr(functionInstr);
        env.addInstr(returnInstr);
        return returnInstr;
    }
}
