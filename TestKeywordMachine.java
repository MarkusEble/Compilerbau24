import compiler.TokenIntf;

public class TestKeywordMachine {
    public static void main(String[] args) throws Exception {
        final String keyword = "while";
        compiler.StateMachine keywordMachine = new compiler.machines.KeywordMachine(keyword, TokenIntf.Type.WHILE);
        java.io.OutputStreamWriter outWriter = new java.io.OutputStreamWriter(System.out);
        keywordMachine.process("while", outWriter);
        System.out.print(keywordMachine.asDot());
    }
}
