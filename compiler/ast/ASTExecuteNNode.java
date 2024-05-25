package compiler.ast;

import compiler.CompileEnvIntf;
import compiler.InstrIntf;
import compiler.Token;
import compiler.instr.InstrIntegerLiteral;

import java.io.OutputStreamWriter;


public class ASTExecuteNNode extends ASTStmtNode{

    public ASTExprNode times;
    public ASTStmtNode block;

    public ASTExecuteNNode(ASTExprNode times, ASTStmtNode block){
        this.times = times;
        this.block = block;
    }

    @Override
    public void print(OutputStreamWriter outStream, String indent) throws Exception {
        outStream.write(indent);
        outStream.write("EXECUTE \n");
        times.print(outStream, indent + "  ");
        outStream.write("TIMES:");
        block.print(outStream, indent + "  ");
        outStream.write("SEMICOLON \n");
    }

    @Override
    public void execute() {
        int n = times.eval();
        for(int i = 0; i < n; i++){
            block.execute();
        }
    }

    @Override
    public void codegen(compiler.CompileEnvIntf env) {
        compiler.InstrIntf n = times.codegen(env);
        
        compiler.InstrBlock execN = env.createBlock("EXECUTE_N");
        compiler.InstrBlock execNExit = env.createBlock("EXECUTE_N_EXIT");
        compiler.InstrIntf jmpBlock = new compiler.instr.InstrJmp(execN);
        env.addInstr(jmpBlock);
        env.setCurrentBlock(execN);

        //Jump into block n times 
        for(int i = 0; i < n.getValue(); i++){
            block.codegen(env);
        }

        compiler.InstrIntf jmpExit = new compiler.instr.InstrJmp(execNExit);
        env.addInstr(jmpExit);
        env.setCurrentBlock(execNExit);
    }
}

