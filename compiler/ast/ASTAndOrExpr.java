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
        int counter = symbolCount++;
        String symName = "$AND_OR" + (counter);
        compiler.Symbol returnVal = env.getSymbolTable().createSymbol(symName);
        compiler.InstrBlock retBlock = env.createBlock("RET");
        if (constFold != null) {
            env.addInstr(new compiler.instr.InstrAssign(returnVal, new compiler.instr.InstrIntegerLiteral(constFold.toString())));
        } else {
            compiler.InstrIntf lhsExpr = lhs.codegen(env);
            compiler.InstrIntf rhsExpr = rhs.codegen(env);

            compiler.InstrBlock assignTrue = env.createBlock("assignTrue");
            compiler.InstrBlock calBlock = env.createBlock("cal");
            compiler.InstrBlock assignFalse = env.createBlock("assignFalse");
            
            
            if(token.m_type == Type.OR){
                compiler.InstrIntf cmp = new compiler.instr.InstrCondJump(lhsExpr, assignTrue, calBlock);
                env.addInstr(cmp);
            } else {
                compiler.InstrIntf cmp = new compiler.instr.InstrCondJump(new compiler.instr.InstrUnary(Type.NOT, lhsExpr), assignTrue,calBlock);
                env.addInstr(cmp);
            }
            env.setCurrentBlock(calBlock);
            calBlock.addInstr(rhsExpr);
            calBlock.addInstr(new compiler.instr.InstrJmp(retBlock));
            env.setCurrentBlock(assignTrue);
            assignTrue.addInstr(new compiler.instr.InstrAssign(returnVal, new compiler.instr.InstrIntegerLiteral("1")));
            assignTrue.addInstr(new compiler.instr.InstrJmp(retBlock));
            
            env.setCurrentBlock(assignFalse);
            assignFalse.addInstr(new compiler.instr.InstrAssign(returnVal, new compiler.instr.InstrIntegerLiteral("0")));
            assignFalse.addInstr(new compiler.instr.InstrJmp(retBlock));
        }
        env.setCurrentBlock(retBlock);
        compiler.InstrIntf r = new compiler.instr.InstrVariableExpr(symName);
        retBlock.addInstr(r);
        return r;
    }

    public Integer constFold() {
        // NULL, wenn nicht konstant, sonst den wert
        // Integer lhsConstFold = lhs.constFold();
        // Integer rhsConstFold = rhs.constFold();
        
        // if (token.m_type == Type.OR) {
        //     if ((lhsConstFold != null && lhsConstFold == 1) || (rhsConstFold != null && rhsConstFold == 1)) {
        //         return 1;
        //     } else if (lhsConstFold != null && rhsConstFold != null && lhsConstFold == 0 && rhsConstFold == 0) {
        //         return 0;
        //     }
        // } else if (token.m_type == Type.AND) {
        //     if (lhsConstFold != null && rhsConstFold != null && lhsConstFold == 1 && rhsConstFold == 1) {
        //         return 1;
        //     } else if ((lhsConstFold != null && lhsConstFold == 0) || (rhsConstFold != null && rhsConstFold == 0)) {
        //         return 0;
        //     }
        // }

        return null;
    }
}
