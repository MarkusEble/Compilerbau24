package compiler.ast;

import compiler.ExpressionEvaluator;
import compiler.info.ConstInfo;

import java.io.OutputStreamWriter;

public class ASTDashNode extends ASTExprNode {
    public ASTExprNode lhs, rhs;
    public ASTDashNode(ASTExprNode lhs, ASTExprNode rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public void print(OutputStreamWriter outStream, String indent) throws Exception {
        outStream.write(indent);
        lhs.print(outStream, indent+indent);
        outStream.write("DASH ");
        outStream.write("\n");
        rhs.print(outStream, indent+indent);
    }

    @Override
    public int eval() {
        return (int) Math.pow(lhs.eval(), rhs.eval());
    }

    @Override
    public compiler.InstrIntf codegen(compiler.CompileEnvIntf env) {
        // create instruction object
        // pass instruction objects of childs
        // as input arguments
        compiler.InstrIntf instr = new compiler.instr.InstrIntegerLiteral(Integer.valueOf(m_value));

        // add instruction to current code block
        env.addInstr(instr);
        return instr;
    }
    @Override
    public ConstInfo constFold() {
        return new ConstInfo(true, Integer.parseInt(this.m_value));
    }
}
