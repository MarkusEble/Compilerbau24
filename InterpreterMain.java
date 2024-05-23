import java.io.OutputStreamWriter;

public class InterpreterMain {

    public static void main(String[] args) throws Exception {
        compiler.CompileEnv compileEnv = new compiler.CompileEnv("{DECLARE a; a = 1; BLOCK { a = a + 2; } PRINT a;}", true);
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
