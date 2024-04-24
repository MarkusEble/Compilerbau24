package compiler.ast;

import compiler.info.ConstInfo;

import java.io.OutputStreamWriter;

public class ASTIntegerLiteralNode extends ASTExprNode {
    public String m_value;
    
    public ASTIntegerLiteralNode(String value) {
        m_value = value;
    }

    @Override
    public void print(OutputStreamWriter outStream, String indent) throws Exception {
        outStream.write(indent);
        outStream.write("INTEGER ");
        outStream.write(m_value);
        outStream.write("\n");
    }

    @Override
    public int eval() {
        return Integer.valueOf(m_value);
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
