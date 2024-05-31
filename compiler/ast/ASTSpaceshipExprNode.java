package compiler.ast;

import compiler.CompileEnvIntf;
import compiler.InstrIntf;
import compiler.TokenIntf;
import compiler.instr.*;

import java.io.OutputStreamWriter;

public class ASTSpaceshipExprNode extends ASTExprNode {
    public ASTExprNode m_rvalue;
    public ASTExprNode m_lvalue;

    private static int symbolValue = 0;

    public ASTSpaceshipExprNode(ASTExprNode lCompareExpr, ASTExprNode rCompareExpr) {
        m_rvalue = rCompareExpr;
        m_lvalue = lCompareExpr;
    }

    @Override
    public int eval() {
        //lhs und rhs operator
        int lvalue = m_lvalue.eval();
        int rvalue = m_rvalue.eval();
        if(lvalue > rvalue) {
            //1, when lhs>rhs
            return 1;
        }else if(lvalue < rvalue){
            //-1, when lhs<rhs
            return -1;
        }
        //0, when lhs==rhs
        return 0;
    }

    @Override
    public void print(OutputStreamWriter outStream, String indent) throws Exception {
        outStream.write(indent);
        outStream.write("SPACESHIPExpr ");
        outStream.write("\n");
        m_lvalue.print(outStream, indent +" ");
        outStream.write(indent + " ");
        outStream.write("SPACESHIP");
        outStream.write("\n");
        m_rvalue.print(outStream, indent + " ");
    }

    @Override
    public InstrIntf codegen(CompileEnvIntf compileEnv) {
        //init local variable
        compiler.Symbol resultSymbol = compileEnv.getSymbolTable().createSymbol("$resultSpace" + symbolValue);
        symbolValue++;

        // Codegen for lhs and rhs + init intern Variable
        compiler.InstrIntf lexprInstr = m_lvalue.codegen(compileEnv);
        compiler.InstrIntf rexprInstr = m_rvalue.codegen(compileEnv);

        //Init jumpblocks
        compiler.InstrBlock greaterBlock = compileEnv.createBlock("GREATER");
        compiler.InstrBlock lessOrEquBlock = compileEnv.createBlock("LESSOREQUAL");
        compiler.InstrBlock lesserBlock = compileEnv.createBlock("LESSER");
        compiler.InstrBlock equalBlock = compileEnv.createBlock("EQUAL");
        compiler.InstrBlock exitBlock = compileEnv.createBlock("EXIT");

        //jump exit
        compiler.InstrIntf jmpExit = new compiler.instr.InstrJmp(exitBlock);

        //compare if lhs > rhs + jump condition
        compiler.InstrIntf compareGreater = new InstrComp(lexprInstr, rexprInstr, TokenIntf.Type.GREATER);
        compiler.InstrIntf jmpPositive = new InstrCondJump(compareGreater, greaterBlock, lessOrEquBlock);

        compileEnv.addInstr(compareGreater);
        compileEnv.addInstr(jmpPositive);

        //jump to greaterBlock
        compileEnv.setCurrentBlock(greaterBlock);
        compiler.InstrIntf resultValueGreater = new InstrAssign(resultSymbol, new InstrIntegerLiteral("1"));
        greaterBlock.addInstr(resultValueGreater);
        compileEnv.addInstr(jmpExit);

        //compare if lhs < rhs + jump condition
        compiler.InstrIntf compareLess = new InstrComp(lexprInstr, rexprInstr, TokenIntf.Type.LESS);
        compiler.InstrIntf jmpNegative = new InstrCondJump(compareLess, lesserBlock, equalBlock);
        compileEnv.setCurrentBlock(lessOrEquBlock);
        compileEnv.addInstr(compareLess);
        compileEnv.addInstr(jmpNegative);

        //jump to lesserBlock
        compileEnv.setCurrentBlock(lesserBlock);
        compiler.InstrIntf resultValueLesser = new InstrAssign(resultSymbol, new InstrIntegerLiteral("-1"));
        lesserBlock.addInstr(resultValueLesser);
        compileEnv.addInstr(jmpExit);

        //jump to equalBlock
        compileEnv.setCurrentBlock(equalBlock);
        compiler.InstrIntf resultValueEqual = new InstrAssign(resultSymbol, new InstrIntegerLiteral("0"));
        equalBlock.addInstr(resultValueEqual);
        compileEnv.addInstr(jmpExit);

        //jump exitBlock
        compileEnv.setCurrentBlock(exitBlock);
        compiler.InstrIntf loadResult = new InstrVariableExpr(resultSymbol.m_name);
        exitBlock.addInstr(loadResult);

        return loadResult;
    }

}
