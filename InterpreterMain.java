import java.io.OutputStreamWriter;

public class InterpreterMain {

    public static void main(String[] args) throws Exception {
        String input = new String("""
            // SWITCH CASE Testprogramm 2:
            {
              DECLARE in;
              DECLARE out;
              in = 3;
              out = 0;
              SWITCH(in) {
              CASE 1:
                out = 2;
              CASE 2:
                out = 3;
              CASE 4:
                out = 3;
              CASE 3:
                out = 5;
              CASE 5:
                out = 2;
              }
              PRINT out;
            }                
                """);
        compiler.CompileEnv compileEnv = new compiler.CompileEnv(input, true);
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
