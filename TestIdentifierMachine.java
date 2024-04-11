public class TestIdentifierMachine {
    public static void main(String[] args) throws Exception {
        final String identifier = "_name";
        compiler.StateMachine identifierMachine = new compiler.machines.IdentifierMachine();
        java.io.OutputStreamWriter outWriter = new java.io.OutputStreamWriter(System.out);
        identifierMachine.process(identifier, outWriter);
        System.out.print(identifierMachine.asDot());
    }
}