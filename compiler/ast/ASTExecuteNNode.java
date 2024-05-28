package compiler.ast;

import compiler.TokenIntf;

import java.io.OutputStreamWriter;


public class ASTExecuteNNode extends ASTStmtNode{

    public ASTExprNode times;
    public ASTStmtNode block;

    private static int symbolCount = 0;

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
        compiler.InstrBlock execNInit = env.createBlock("EXECUTE_N_INIT");
        compiler.InstrBlock execNHead = env.createBlock("EXECUTE_N_HEAD");
        compiler.InstrBlock execNBody = env.createBlock("EXECUTE_N_BODY");
        compiler.InstrBlock execNExit = env.createBlock("EXECUTE_N_EXIT");
        //Jump to InitBlock
        compiler.InstrIntf jmpInitBlock = new compiler.instr.InstrJmp(execNInit);
        env.addInstr(jmpInitBlock);
        env.setCurrentBlock(execNInit);
        
        //Init block creats counter variable and assigns value of given expression to it
        compiler.Symbol counterSymbol = env.getSymbolTable().createSymbol("$counter" + symbolCount);
        symbolCount++;
        compiler.InstrIntf nInstr = times.codegen(env);
        compiler.InstrIntf counterInstr = new compiler.instr.InstrVariableExpr(counterSymbol.m_name);
        compiler.InstrIntf assignCounter = new compiler.instr.InstrAssign(counterSymbol, nInstr);
        env.addInstr(counterInstr);
        env.addInstr(assignCounter);
        //Jump to HeadBlock
        compiler.InstrIntf jmpHeadBlock = new compiler.instr.InstrJmp(execNHead);
        env.addInstr(jmpHeadBlock);
        env.setCurrentBlock(execNHead);

        //HeadBlock checks, whether counter is 0 -> If not: jump to Body, If: jump to exit
        compiler.InstrIntf loadCounterInstr = new compiler.instr.InstrVariableExpr(counterSymbol.m_name);
        compiler.InstrIntf comparisionExpr =new compiler.instr.InstrIntegerLiteral("0");
        env.addInstr(loadCounterInstr);
        env.addInstr(comparisionExpr);
        compiler.InstrIntf condition = new compiler.instr.InstrComp(loadCounterInstr, comparisionExpr, TokenIntf.Type.GREATER);
        compiler.InstrIntf condJmp = new compiler.instr.InstrCondJump(condition, execNBody, execNExit);
        env.addInstr(condition);
        env.addInstr(condJmp);

        //Execute the block statements and decrement counter by one
        env.setCurrentBlock(execNBody);
        block.codegen(env);
        compiler.InstrIntf loadCounterInstrBlock = new compiler.instr.InstrVariableExpr(counterSymbol.m_name);
        env.addInstr(loadCounterInstrBlock);
        compiler.InstrIntf decrement = new compiler.instr.InstrPlusMinus(TokenIntf.Type.MINUS, loadCounterInstrBlock, new compiler.instr.InstrIntegerLiteral("1"));
        env.addInstr(decrement);
        assignCounter = new compiler.instr.InstrAssign(counterSymbol, decrement);
        env.addInstr(assignCounter);
        env.addInstr(jmpHeadBlock);
        env.setCurrentBlock(execNHead);

        env.setCurrentBlock(execNExit);
    }
}

