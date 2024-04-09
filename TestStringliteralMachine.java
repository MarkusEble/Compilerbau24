public class TestStringliteralMachine {
    public static void main(String[] args) throws Exception {
        compiler.StateMachine stringliteralMachine = new compiler.machines.StringliteralMachine();
        java.io.OutputStreamWriter outWriter = new java.io.OutputStreamWriter(System.out);
        stringliteralMachine.process("\"das ist ein String Literal\"", outWriter);
        System.out.print(stringliteralMachine.asDot());
    }
}
