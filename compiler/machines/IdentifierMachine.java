package compiler.machines;

import compiler.TokenIntf;

public class IdentifierMachine extends compiler.StateMachine {

    public void initStateTable() {
        compiler.State startState = new compiler.State("start", false);
        startState.addTransitionRange('a', 'z', "end");
        startState.addTransitionRange('A', 'Z', "end");
        startState.addTransition('_', "end");
        addState(startState);
        compiler.State endState = new compiler.State("end", true);
        endState.addTransitionRange('a', 'z', "end");
        endState.addTransitionRange('A', 'Z', "end");
        endState.addTransitionRange('0', '9', "end");
        endState.addTransition('_', "end");
        addState(endState);
    }

    @Override
    public String getStartState() {
        return "start";
    }

    @Override
    public TokenIntf.Type getType() {
        return TokenIntf.Type.IDENT;
    }
}