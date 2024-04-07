public class TestWhitespaceMachine {
    public static void main(String[] args) throws Exception {
        compiler.StateMachine whitespaceMachine = new compiler.machines.WhitespaceMachine();
        java.io.OutputStreamWriter outWriter = new java.io.OutputStreamWriter(System.out);
        whitespaceMachine.process("    \n     \t    \r\r    ", outWriter);
        System.out.print(whitespaceMachine.asDot());
    }
}
