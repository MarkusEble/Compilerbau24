package compiler.ast;

import compiler.InstrIntf;
import compiler.instr.InstrCondJump;
import compiler.instr.InstrJmp;

import java.io.OutputStreamWriter;

public class ASTIfStmtNode extends ASTStmtNode{

    public ASTExprNode cond;
    public ASTStmtNode trueBlock;
    public ASTStmtNode restChild;

    public ASTIfStmtNode(ASTExprNode condition, ASTStmtNode trueBlock, ASTStmtNode elseBlock){
        this.cond = condition;
        this.trueBlock = trueBlock;
        this.restChild = elseBlock;
    }

    @Override
    public void print(OutputStreamWriter outStream, String indent) throws Exception {
        outStream.write(indent + "IF\n");
        cond.print(outStream, indent);
        outStream.write("\n");
        trueBlock.print(outStream, indent);
        outStream.write("\n");
        restChild.print(outStream, indent);
    }

    @Override
    public void execute() {
        if(cond.eval() != 0){
            trueBlock.execute();
        }
        else if (restChild != null){
            restChild.execute();
        }
    }

    @Override
    public void codegen(compiler.CompileEnvIntf env) {
        compiler.InstrBlock conditionBlock = env.createBlock("conditionBlock");
        compiler.InstrBlock trueBlockInstr = env.createBlock("codeTrueBlock");
        compiler.InstrBlock falseBlockInstr = env.createBlock("codeFalseBlock");
        compiler.InstrBlock blockExit = env.createBlock("ifExit");

        InstrIntf jump = new InstrJmp(conditionBlock);
        env.addInstr(jump);

        env.setCurrentBlock(conditionBlock);
        InstrIntf conditionInstr = cond.codegen(env);

        env.setCurrentBlock(trueBlockInstr);
        trueBlock.codegen(env);
        env.addInstr(new InstrJmp(blockExit));

        if (restChild == null) {
            env.setCurrentBlock(conditionBlock);
            InstrIntf jumpCondBlock = new InstrCondJump(conditionInstr, trueBlockInstr, blockExit);
            env.addInstr(jumpCondBlock);
        } else {
            env.setCurrentBlock(conditionBlock);
            InstrIntf jumpCondBlock = new InstrCondJump(conditionInstr, trueBlockInstr, falseBlockInstr);
            env.addInstr(jumpCondBlock);

            env.setCurrentBlock(falseBlockInstr);
            restChild.codegen(env);
            env.addInstr(new InstrJmp(blockExit));
        }
        env.setCurrentBlock(blockExit);
    }
}
