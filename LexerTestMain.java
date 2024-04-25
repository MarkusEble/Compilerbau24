import java.io.OutputStreamWriter;

public class LexerTestMain {

    public static void main(String[] args) throws Exception {
        // open input file
        String testSuiteContent = compiler.InputReader.fileToString("LexerTestInput.txt");
        compiler.InputReader inputReader = new compiler.InputReader(testSuiteContent);

        test.TestSuite testSuite = new test.TestSuite();

        // output test content
        //OutputStreamWriter outStream = new OutputStreamWriter(System.out, "UTF-8");
        //testSuite.dump(inputReader, outStream);

        // execute test content
        testSuite.execute(inputReader, new LexerTest());
    }
}
