package compiler.ast;

import compiler.CompileEnvIntf;
import compiler.Token;
import compiler.TokenIntf;
import compiler.instr.InstrComp;
import compiler.instr.InstrCondJump;
import compiler.instr.InstrIntegerLiteral;

import java.io.OutputStreamWriter;

public class ASTNumericIfNode extends ASTStmtNode {

    private ASTExprNode expr;
    private ASTStmtNode pos;
    private ASTStmtNode neg;
    private ASTStmtNode zero;

    public ASTNumericIfNode(ASTExprNode expr, ASTStmtNode pos, ASTStmtNode neg, ASTStmtNode zero) {
        this.expr = expr;
        this.pos = pos;
        this.neg = neg;
        this.zero = zero;
    }

    @Override
    public void print(OutputStreamWriter outStream, String indent) throws Exception {
        outStream.write(indent + TokenIntf.Type.NUMERIC_IF.toString() + " " + expr.eval() + "\n");
        outStream.write(indent + "POSITIVE");
        pos.print(outStream, indent + "  ");
        outStream.write(indent + "NEGATIVE");
        neg.print(outStream, indent + "  ");
        outStream.write(indent + "ZERO");
        zero.print(outStream, indent + "  ");
    }

    @Override
    public void execute() {
        if (expr.eval() > 0) {
            pos.execute();
        } else if (expr.eval() < 0) {
            neg.execute();
        } else {
            zero.execute();
        }
    }

    @Override
    public void codegen(CompileEnvIntf env) {
        //NUMERIC_IF(expr)
        compiler.InstrIntf exprInstr = expr.codegen(env);
        //Es wird immer mit 0 verglichen
        compiler.InstrIntf zeroLiteral = new InstrIntegerLiteral("0");
        env.addInstr(zeroLiteral);

        compiler.InstrBlock positiveBlock = env.createBlock("POSITIVE");
        compiler.InstrBlock negativeOrZeroBlock = env.createBlock("NEGATIVE_OR_ZERO");
        compiler.InstrBlock negativeBlock = env.createBlock("NEGATIVE");
        compiler.InstrBlock zeroBlock = env.createBlock("ZERO");
        compiler.InstrBlock exitBlock = env.createBlock("EXIT");

        compiler.InstrIntf jmpExit = new compiler.instr.InstrJmp(exitBlock);

        //wenn expr>0 -> positiv, ansonsten negativ oder null
        compiler.InstrIntf compareGreater = new InstrComp(exprInstr, zeroLiteral, TokenIntf.Type.GREATER);
        compiler.InstrIntf jmpPositive = new InstrCondJump(compareGreater, positiveBlock, negativeOrZeroBlock);

        env.addInstr(compareGreater);
        env.addInstr(jmpPositive);

        //codegen für den positiven Block
        env.setCurrentBlock(positiveBlock);
        pos.codegen(env);
        env.addInstr(jmpExit);

        //wenn expr<0 -> negativ, sonst null
        env.setCurrentBlock(negativeOrZeroBlock);
        compiler.InstrIntf compareLess = new InstrComp(exprInstr, zeroLiteral, TokenIntf.Type.LESS);
        compiler.InstrIntf jmpNegative = new InstrCondJump(compareLess, negativeBlock, zeroBlock);
        env.addInstr(compareLess);
        env.addInstr(jmpNegative);

        //codegen für negativen Block
        env.setCurrentBlock(negativeBlock);
        neg.codegen(env);
        env.addInstr(jmpExit);

        //codegen für null Block
        env.setCurrentBlock(zeroBlock);
        zero.codegen(env);
        env.addInstr(jmpExit);

        env.setCurrentBlock(exitBlock);
    }
}
