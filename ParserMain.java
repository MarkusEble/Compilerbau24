import java.io.OutputStreamWriter;

public class ParserMain {

    public static void main(String[] args) throws Exception {
        compiler.Lexer lexer = new compiler.Lexer();
        compiler.Parser parser = new compiler.Parser(lexer, new compiler.SymbolTable(), null);
        //compiler.ast.ASTExprNode rootExpr = parser.parseExpression("2^3 < 7");
        compiler.ast.ASTStmtNode printStmt = parser.parseStmt("{DECLARE a;\n DECLARE b\n; a = 1 + 2;\nb = 5;\nPRINT 2+2?5+b:0;\n}");
        printStmt.execute();
        //OutputStreamWriter outputWriter = new OutputStreamWriter(System.out);
        //rootExpr.print(outputWriter, "  ");
        //printStmt.print(outputWriter, "");
        //outputWriter.flush();
        //int result = rootExpr.eval();
        //System.out.println(result);
    }

}
