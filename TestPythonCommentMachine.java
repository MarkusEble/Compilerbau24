public class TestPythonCommentMachine {
    public static void main(String[] args) throws Exception {
        compiler.StateMachine pythonCommentMachine = new compiler.machines.PythonCommentMachine();
        java.io.OutputStreamWriter outWriter = new java.io.OutputStreamWriter(System.out);
        pythonCommentMachine.process("\"\"\"In234234halt\"\"sdkfksdk\"\"\"", outWriter);
        System.out.print(pythonCommentMachine.asDot());
    }
}
