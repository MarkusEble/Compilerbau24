package compiler.ast;

import compiler.Symbol;

import java.io.OutputStreamWriter;

public class ASTVariableExprNode extends ASTExprNode {
    public Symbol m_symbol;

    public ASTVariableExprNode(Symbol symbol) {
        m_symbol = symbol;
    }

    @Override
    public void print(OutputStreamWriter outStream, String indent) throws Exception {
        outStream.write(indent);
        outStream.write("VARIABLE ");
        outStream.write(m_symbol.m_name+" ");
        outStream.write(m_symbol.m_number);
        outStream.write("\n");
    }

    @Override
    public int eval() {
        return Integer.valueOf(m_symbol.m_number);
    }
}
