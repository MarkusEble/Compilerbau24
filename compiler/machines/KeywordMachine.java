package compiler.machines;

public class KeywordMachine extends compiler.StateMachine {
    private String keyword;

    public KeywordMachine(String keyword) {
        this.keyword = keyword;
    }

    public void initStateTable() {
        compiler.State startState = new compiler.State("0", false);
        startState.addTransition(this.keyword.charAt(0), "1");
        addState(startState);
        for (int i = 1; i < this.keyword.length(); i++) {
            compiler.State state = new compiler.State(String.valueOf(i), false);
            state.addTransition(this.keyword.charAt(i), String.valueOf(i + 1));
            addState(state);
        }
        compiler.State endState = new compiler.State(String.valueOf(this.keyword.length()), true);
        addState(endState);
    }

    @Override
    public String getStartState() {
        return "0";
    }

    public compiler.TokenIntf.Type getType() {
        return compiler.TokenIntf.Type.EOF;
    }

}
