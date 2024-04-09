import compiler.StateMachine;

public class TestDezimalMachine {

  public static void main(String[] args) throws Exception {
    System.out.println("HelloWorld");
    StateMachine dezimalMachine = new compiler.machines.DezimalMachine();
    java.io.OutputStreamWriter outWriter = new java.io.OutputStreamWriter(System.out);
    dezimalMachine.process("10.0", outWriter);
    System.out.print(dezimalMachine.asDot());
  }
}
