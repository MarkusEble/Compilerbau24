import java.io.OutputStreamWriter;

public class InterpreterMain {

    public static void main(String[] args) throws Exception {
        String input = new String("""
                {
                    // FUNCTION Testprogramm 3
                    FUNCTION myFct1(a, b) {
                      // PRINT a;
                      // PRINT b;
                      DECLARE c;
                      c = a + 2 * b;
                      /// PRINT c;
                      RETURN c;
                    };
                    FUNCTION myFct2(d) {
                      RETURN d + 1;
                    };

                    FUNCTION myFct3(){
                        RETURN CALL myFct2(2);
                    }

                    DECLARE e;
                    e = CALL myFct1(1, 2) + CALL myFct3(40);
                    PRINT e;
                }
                """);
        compiler.CompileEnv compileEnv = new compiler.CompileEnv(input, false);

        compileEnv.compile();
        OutputStreamWriter outStream = new OutputStreamWriter(System.out, "UTF-8");
        /*System.out.println("AST:");
        compileEnv.dumpAst(System.out);*/
        System.out.println("\n\nPROGRAM:");
        compileEnv.dump(System.out);
        /*System.out.println("EXECUTE:");
        compileEnv.execute(outStream);*/
        outStream.flush();
    }

}
