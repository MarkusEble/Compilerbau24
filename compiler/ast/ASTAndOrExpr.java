package compiler.ast;

import java.io.OutputStreamWriter;

import compiler.CompileEnvIntf;
import compiler.InstrIntf;
import compiler.Token;
import compiler.TokenIntf.Type;
import compiler.instr.InstrCondJump;

public class ASTAndOrExpr extends ASTExprNode {
    ASTExprNode lhs, rhs;
    Token token;
    private static int symbolCount = 0;
    public ASTAndOrExpr(Token or, ASTExprNode left, ASTExprNode right) {
        this.lhs = left;
        this.token = or;
        this.rhs = right;
    }

    @Override
    public void print(OutputStreamWriter outStream, String indent) throws Exception {
        outStream.write(indent);
        outStream.write("AndOr\n");
        outStream.write(indent);
        this.lhs.print(outStream, indent + "   ");
        outStream.write("\n");
        outStream.write(indent);
        outStream.write(token.toString());
        outStream.write("\n");
        outStream.write(indent);
        this.rhs.print(outStream, indent + "   ");
        outStream.write("\n");
    }

    @Override
    public int eval() {
        if (token.m_type == Type.OR) {
            return lhs.eval() == 1 || rhs.eval() == 1 ? 1 : 0;
        } else {
            return lhs.eval() == 1 && rhs.eval() == 1 ? 1 : 0;
        }
    }

    @Override
    public InstrIntf codegen(CompileEnvIntf env) {
        Integer constFold = this.constFold();
        compiler.Symbol returnVal = env.getSymbolTable().createSymbol("$AND_OR_SHORT" + (symbolCount++));
        compiler.Symbol lhsRetVal = env.getSymbolTable().createSymbol("$AND_OR_SHORT" + (symbolCount++));
        compiler.InstrBlock calBlock = null, assignTrue = null, assignFalse = null;
        compiler.InstrBlock retBlock;

        // Create Blocks -> Ret block is in the end of these instructions.
        if(constFold != null) {
            retBlock = env.createBlock("RET");
        } else {
            calBlock = env.createBlock("cal");
            if (token.m_type == Type.OR) {
                assignTrue = env.createBlock("assignTrue");
            } else {
                assignFalse = env.createBlock("assignFalse");
            }
            retBlock = env.createBlock("RET");
        }

        // Add instructions to blocks
        if (constFold != null) {
            compiler.InstrIntf loadConst  = new compiler.instr.InstrIntegerLiteral(constFold.toString());
            env.addInstr(loadConst);
            env.addInstr(new compiler.instr.InstrAssign(returnVal, loadConst));
            env.addInstr(new compiler.instr.InstrJmp(retBlock));
        } else {
            compiler.InstrIntf lhsExpr = lhs.codegen(env);
            env.addInstr(new compiler.instr.InstrAssign(lhsRetVal, lhsExpr));
            compiler.InstrIntf lhrResValRead = new compiler.instr.InstrVariableExpr(lhsRetVal.m_name);
            env.addInstr(lhrResValRead);
            if(token.m_type == Type.OR){
                
                env.addInstr(new compiler.instr.InstrCondJump(lhrResValRead, assignTrue, calBlock));
                
                env.setCurrentBlock(assignTrue);
                compiler.InstrIntf trueLit =  new compiler.instr.InstrIntegerLiteral("1");
                assignTrue.addInstr(trueLit);
                assignTrue.addInstr(new compiler.instr.InstrAssign(returnVal, trueLit));
                assignTrue.addInstr(new compiler.instr.InstrJmp(retBlock));
                
            } else {
                env.addInstr(new compiler.instr.InstrCondJump(lhrResValRead, calBlock, assignFalse));
                
                env.setCurrentBlock(assignFalse);
                compiler.InstrIntf falseLit = new compiler.instr.InstrIntegerLiteral("0");
                assignFalse.addInstr(falseLit);
                assignFalse.addInstr(new compiler.instr.InstrAssign(returnVal, falseLit));
                assignFalse.addInstr(new compiler.instr.InstrJmp(retBlock));
            }
            env.setCurrentBlock(calBlock);
            compiler.InstrIntf rhsExpr = rhs.codegen(env);
            compiler.InstrIntf loadLhs = new compiler.instr.InstrVariableExpr(lhsRetVal.m_name);
            compiler.InstrIntf compare = new compiler.instr.InstrAndOr(token.m_type, loadLhs, rhsExpr);
            calBlock.addInstr(loadLhs);
            calBlock.addInstr(compare);
            calBlock.addInstr(new compiler.instr.InstrAssign( returnVal , compare));
            calBlock.addInstr(new compiler.instr.InstrJmp(retBlock));

            
        }
        env.setCurrentBlock(retBlock);
        compiler.InstrIntf r = new compiler.instr.InstrVariableExpr(returnVal.m_name);
        retBlock.addInstr(r);
        return r;
    }

    public Integer constFold() {
        // NULL, wenn nicht konstant, sonst den wert
        Integer lhsConstFold = lhs.constFold();
        Integer rhsConstFold = rhs.constFold();
        
        if (token.m_type == Type.OR) {
            if ((lhsConstFold != null && lhsConstFold == 1) || (rhsConstFold != null && rhsConstFold == 1)) {
                return 1;
            } else if (lhsConstFold != null && rhsConstFold != null && lhsConstFold == 0 && rhsConstFold == 0) {
                return 0;
            }
        } else if (token.m_type == Type.AND) {
            if (lhsConstFold != null && rhsConstFold != null && lhsConstFold == 1 && rhsConstFold == 1) {
                return 1;
            } else if ((lhsConstFold != null && lhsConstFold == 0) || (rhsConstFold != null && rhsConstFold == 0)) {
                return 0;
            }
        }

        return null;
    }
}
