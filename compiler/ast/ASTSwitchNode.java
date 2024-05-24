package compiler.ast;

import compiler.CompileEnvIntf;
import compiler.InstrBlock;
import compiler.InstrIntf;
import compiler.Token;
import compiler.TokenIntf.Type;
import compiler.instr.InstrComp;
import compiler.instr.InstrCondJump;
import compiler.instr.InstrIntegerLiteral;
import compiler.instr.InstrJmp;
import java.io.OutputStreamWriter;
import java.util.List;

public class ASTSwitchNode extends ASTStmtNode{

    private ASTExprNode m_expr;
    private List<ASTCaseNode> m_caseList;

    public ASTSwitchNode(ASTExprNode m_expr, List<ASTCaseNode> m_caseList) {
        this.m_expr = m_expr;
        this.m_caseList = m_caseList;
    }

    @Override
    public void print(OutputStreamWriter outStream, String indent) throws Exception {
        outStream.write(indent + "SWITCH");
        outStream.write("\n");
        m_expr.print(outStream, indent + "  ");
        for(ASTCaseNode node : m_caseList){
            node.print(outStream, indent + "  ");
        }
    }

    @Override
    public void execute() {
        int value = m_expr.eval();
        for(ASTCaseNode node : m_caseList){
            if(node.getNumber() == value){
                node.execute();
                return;
            }
        }
    }

    @Override
    public void codegen(CompileEnvIntf env) {
        InstrBlock block = env.createBlock("SWITCH");
        env.addInstr(new InstrJmp(block));
        env.setCurrentBlock(block);

        // Exit block
        InstrBlock blockExit = env.createBlock("SWITCH_EXIT");

        InstrIntf expr = m_expr.codegen(env);
        //would be better if InstrComp would take Token.Type instead of Token...
        Token token = new Token();
        token.m_type = Type.EQUAL;

        for(ASTCaseNode caseNode : m_caseList){
            InstrIntf compare = new InstrComp(
                expr,
                new InstrIntegerLiteral(String.valueOf(caseNode.getNumber())),
                token
            );

            InstrBlock blockCode = env.createBlock("CASE_" + caseNode.getNumber());
            InstrBlock blockHelper = env.createBlock("CASE_HELPER_" + caseNode.getNumber());

            // Add condJump to active block
            InstrIntf condJump = new InstrCondJump(compare, blockCode, blockHelper);
            env.addInstr(condJump);

            // Generate code of code block
            env.setCurrentBlock(blockCode);
            caseNode.codegen(env);
            env.addInstr(new InstrJmp(blockExit));

            // Activate helper block for jump to be added later on
            env.setCurrentBlock(blockHelper);
        }
        // Exit the last helper block
        env.addInstr(new InstrJmp(blockExit));

        env.setCurrentBlock(blockExit);

        /*
        CONDJUMP expr, 1_1Block, 1_2Block

        1_1Block:
            //was hier passiert -> codegen

        1_2Block:
            CONDJUMP expr, 2_1BLock, 2_2Block

        2_1Block:
            //was hier passiert -> codegen

        2_2Block:
            JMP EXIT
         */
    }
}
