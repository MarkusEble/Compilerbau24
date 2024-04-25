package compiler.ast;

import compiler.Symbol;

import java.io.OutputStreamWriter;

public class ASTVariableExprNode extends ASTExprNode {
    public String m_name;
    public int m_value;

    public ASTVariableExprNode(Symbol symbol) {
        m_value = symbol.m_number;
        m_name = symbol.m_name;
    }

    @Override
    public void print(OutputStreamWriter outStream, String indent) throws Exception {
        outStream.write(indent);
        outStream.write("VARIABLE ");
        outStream.write(m_name+" ");
        outStream.write(m_value);
        outStream.write("\n");
    }

    @Override
    public int eval() {
        return Integer.valueOf(m_value);
    }
}
