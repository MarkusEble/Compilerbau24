import java.io.OutputStreamWriter;

public class ExpressionEvaluatorTest implements test.TestCaseIntf {

    @Override
    public void executeTest(String input, OutputStreamWriter outputWriter) throws Exception {
        compiler.Lexer lexer = new compiler.Lexer();
        compiler.ExpressionEvaluator evaluator = new compiler.ExpressionEvaluator(lexer);
        int result = evaluator.eval(input);
        outputWriter.write(Integer.toString(result));
        outputWriter.write("\n");
        outputWriter.flush();
    }

}
