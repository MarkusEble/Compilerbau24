import java.io.OutputStreamWriter;

public class InterpreterMain {

    public static void main(String[] args) throws Exception {
        String input = new String("""
          {
            DECLARE in0;
            DECLARE in1;
            DECLARE out;
            in0 = 0;
            in1 = 0;
            out = 0;
            IF (in0) {
              out = 1;
            } ELSE IF (in1) {
              out = 2;
            } ELSE {
              out = 3;
            }
            PRINT out;
          }
                """);
        compiler.CompileEnv compileEnv = new compiler.CompileEnv(input, false);

        compileEnv.compile();
        OutputStreamWriter outStream = new OutputStreamWriter(System.out, "UTF-8");
        /*System.out.println("AST:");
        compileEnv.dumpAst(System.out);*/
        System.out.println("\n\nPROGRAM:");
        compileEnv.dump(System.out);
        System.out.println("EXECUTE:");
        compileEnv.execute(outStream);
        outStream.flush();
    }

}
