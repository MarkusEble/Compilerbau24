package compiler.ast;

import java.io.OutputStreamWriter;

public abstract class ASTNode {
    public abstract void print(OutputStreamWriter outStream, String indent) throws Exception;
}
