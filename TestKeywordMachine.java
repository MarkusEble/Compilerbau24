public class TestKeywordMachine {
    public static void main(String[] args) throws Exception {
        final String keyword = "while";
        compiler.StateMachine keywordMachine = new compiler.machines.KeywordMachine(keyword);
        java.io.OutputStreamWriter outWriter = new java.io.OutputStreamWriter(System.out);
        keywordMachine.process("while", outWriter);
        System.out.print(keywordMachine.asDot());
    }
}
