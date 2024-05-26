package compiler.ast;

import compiler.CompileEnvIntf;
import compiler.Token;
import compiler.TokenIntf;
import compiler.instr.InstrComp;
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
        compiler.InstrIntf exprInstr = expr.codegen(env);
        compiler.InstrIntf zeroLiteral = new InstrIntegerLiteral("0");
        env.addInstr(zeroLiteral);

        compiler.InstrBlock positiveBlock = env.createBlock("POSITIVE");
        compiler.InstrBlock negativeBlock = env.createBlock("NEGATIVE");
        compiler.InstrBlock zeroBlock = env.createBlock("ZERO");
        compiler.InstrBlock negativeOrZeroBlock = env.createBlock("NEGATIVEORZERO");
        compiler.InstrBlock blockExit = env.createBlock("EXIT");

        compiler.InstrIntf jmpExit = new compiler.instr.InstrJmp(blockExit);

        compiler.InstrIntf compareGreater = new InstrComp(exprInstr, zeroLiteral, TokenIntf.Type.GREATER);
        compiler.InstrIntf jmpPositive = new compiler.instr.InstrCondJump(compareGreater, positiveBlock, negativeOrZeroBlock);
//        env.addInstr(exprInstr);
        env.addInstr(compareGreater);
        env.addInstr(jmpPositive);
        env.setCurrentBlock(positiveBlock);
        pos.codegen(env);
        env.addInstr(jmpExit);

        env.setCurrentBlock(negativeOrZeroBlock);
        compiler.InstrIntf compareLess = new InstrComp(exprInstr, zeroLiteral, TokenIntf.Type.LESS);
        compiler.InstrIntf jmpNegative = new compiler.instr.InstrCondJump(compareLess, negativeBlock, zeroBlock);
        env.addInstr(compareLess);
        env.addInstr(jmpNegative);
        env.setCurrentBlock(negativeBlock);
        neg.codegen(env);
        env.addInstr(jmpExit);

        env.setCurrentBlock(zeroBlock);
        zero.codegen(env);
        env.addInstr(jmpExit);

        env.setCurrentBlock(blockExit);

//        compiler.InstrIntf resultExpr =  new compiler.instr.InstNumericIf(positiveBlock, negativeBlock, zeroBlock, exprInstr);
//        env.addInstr(resultExpr);
    }
}
