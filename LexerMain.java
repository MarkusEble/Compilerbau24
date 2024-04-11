import java.io.OutputStreamWriter;

public class LexerMain {

    public static void main(String[] args) throws Exception {
        compiler.Lexer lexer = new compiler.Lexer();
        OutputStreamWriter outStream = new OutputStreamWriter(System.out, "UTF-8");

        lexer.processInput("3.4 AB WHILE", outStream);
        lexer.processInput("/* */ A 45", outStream);
    }

}
