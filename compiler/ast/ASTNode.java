package compiler.ast;

import java.io.OutputStreamWriter;

public class ASTNode {
    public abstract void print(OutputStreamWriter outStream, String indent) throws Exception;
}
