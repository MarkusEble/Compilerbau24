import java.io.OutputStreamWriter;

public class InterpreterTest implements test.TestCaseIntf {

    @Override
    public void executeTest(String input, OutputStreamWriter outputWriter) throws Exception {
        compiler.CompileEnv env = new compiler.CompileEnv(input, false);
        env.compile();
        env.execute(outputWriter);
        outputWriter.flush();
    }

}
