import java.io.OutputStreamWriter;

public class ExpressionEvaluatorMain {

    public static void main(String[] args) throws Exception {
        compiler.Lexer lexer = new compiler.Lexer();
        compiler.ExpressionEvaluator evaluator = new compiler.ExpressionEvaluator(lexer);
        int result = evaluator.eval("42");
        System.out.println(result);
    }

}
