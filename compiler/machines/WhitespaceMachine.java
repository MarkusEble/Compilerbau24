package compiler.machines;

import compiler.TokenIntf;

public class WhitespaceMachine extends compiler.StateMachine {

    public void initStateTable() {
        compiler.State startState = new compiler.State("start", false);
        startState.addTransition(' ', "expectWhitespace");
        startState.addTransition('\n', "expectWhitespace");
        startState.addTransition('\r', "expectWhitespace");
        startState.addTransition('\t', "expectWhitespace");
        addState(startState);
        compiler.State expectWhitespaceState = new compiler.State("expectWhitespace", true);
        expectWhitespaceState.addTransition(' ', "expectWhitespace");
        expectWhitespaceState.addTransition('\n', "expectWhitespace");
        expectWhitespaceState.addTransition('\r', "expectWhitespace");
        expectWhitespaceState.addTransition('\t', "expectWhitespace");
        addState(expectWhitespaceState);
    }

    @Override
    public String getStartState() {
        return "start";
    }

    public compiler.TokenIntf.Type getType() {
        return TokenIntf.Type.WHITESPACE;
    }

}

