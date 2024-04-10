public class TestCommentMachine {

  // public static void main(String[] args) throws Exception {
  //   System.out.println("HelloWorld");
  //   compiler.StateMachine abMachine = new compiler.machines.ABMachine();
  //   java.io.OutputStreamWriter outWriter = new java.io.OutputStreamWriter(System.out);
  //   abMachine.process("AABBBB", outWriter);
  //   System.out.print(abMachine.asDot());
  // }
  public static void main(String[] args) throws Exception {
    System.out.println("HelloWorld");
    compiler.StateMachine abMachine = new compiler.machines.LineCommentMachine();
    java.io.OutputStreamWriter outWriter = new java.io.OutputStreamWriter(System.out);
    String testString = "// hallo welt";
    abMachine.process(testString, outWriter);
    System.out.print(abMachine.asDot());
  }
}
