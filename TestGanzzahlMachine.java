public class TestGanzzahlMachine {

  public static void main(String[] args) throws Exception {
    compiler.StateMachine ganzzahlMachine = new compiler.machines.GanzzahlMachine();
    java.io.OutputStreamWriter outWriter = new java.io.OutputStreamWriter(System.out);
    ganzzahlMachine.process("1238102389009232", outWriter);
    System.out.print(ganzzahlMachine.asDot());
  }
}
