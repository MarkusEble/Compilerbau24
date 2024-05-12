package compiler.ast;

import compiler.Token;
import compiler.TokenIntf;
//import compiler.info.ConstInfo;

import java.io.OutputStreamWriter;

public class ASTUnaryExprNode extends ASTExprNode {
    public ASTExprNode m_child;
    public TokenIntf.Type m_type;
    public ASTUnaryExprNode(ASTExprNode child, TokenIntf.Type type) {
        m_child = child;
        m_type = type;
    }

    @Override
    public void print(OutputStreamWriter outStream, String indent) throws Exception {
        outStream.write(indent);
        outStream.write(String.format("UnaryExpr (%s)\n", m_type));
        m_child.print(outStream, indent + "  ");
        outStream.write("\n");
    }

    @Override
    public int eval() {
        int result = m_child.eval();
        if(m_type == TokenIntf.Type.NOT){
            return result > 0 ? 0 : 1;
        } else if (m_type == TokenIntf.Type.MINUS) {
            return  - result;
        }
        return 0;
    }


    @Override
    public compiler.InstrIntf codegen(compiler.CompileEnvIntf env) {
        compiler.InstrIntf child = m_child.codegen(env);

        compiler.InstrIntf instr = new compiler.instr.InstrUnaryExpr( m_type, child);

        env.addInstr(instr);
        return instr;
    }
}
