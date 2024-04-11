public class TestCharliteralMachine {
    public static void main(String[] args) throws Exception {
        compiler.StateMachine charliteralMachine = new compiler.machines.CharliteralMachine();
        java.io.OutputStreamWriter outWriter = new java.io.OutputStreamWriter(System.out);
        charliteralMachine.process("'A'", outWriter);
        System.out.print(charliteralMachine.asDot());
    }
}
