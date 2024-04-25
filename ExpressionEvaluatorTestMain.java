public class ExpressionEvaluatorTestMain {

    public static void main(String[] args) throws Exception {
        // open input file
        String testSuiteContent = compiler.InputReader.fileToString("ExpressionEvaluatorTestInput.txt");
        compiler.InputReader inputReader = new compiler.InputReader(testSuiteContent);

        test.TestSuite testSuite = new test.TestSuite();

        // execute test content
        testSuite.execute(inputReader, new ExpressionEvaluatorTest());
    }
}
