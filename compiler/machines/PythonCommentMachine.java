package compiler.machines;

import compiler.State;

public class PythonCommentMachine extends compiler.StateMachine {
    @Override
    public void initStateTable() {
        compiler.State startState = new compiler.State("prefix1", false);
        startState.addTransition('\"', "prefix2");
        addState(startState);

        compiler.State prefix2State = new compiler.State("prefix2", false);
        prefix2State.addTransition('\"', "prefix3");
        addState(prefix2State);

        compiler.State prefix3State = new compiler.State("prefix3", false);
        prefix3State.addTransition('\"', "content");
        addState(prefix3State);

        compiler.State contentState = new compiler.State("content", false);
        addTransitionWithAllLettersExceptQuotationMark(contentState);
        contentState.addTransition('\"', "suffix1");
        addState(contentState);

        compiler.State suffix1State = new compiler.State("suffix1", false);
        addTransitionWithAllLettersExceptQuotationMark(suffix1State);
        suffix1State.addTransition('\"', "suffix2");
        addState(suffix1State);

        compiler.State suffix2State = new compiler.State("suffix2", false);
        addTransitionWithAllLettersExceptQuotationMark(suffix2State);
        suffix2State.addTransition('\"', "suffix3");
        addState(suffix2State);

        compiler.State suffix3State = new compiler.State("suffix3", true);
        addState(suffix3State);
    }
    private void addTransitionWithAllLettersExceptQuotationMark(State state) {
        state.addTransition((char) 33, "content");
        state.addTransitionRange((char) 35, (char) 126, "content");
    }

    @Override
    public String getStartState() {
        return "prefix1";
    }

    @Override
    public compiler.TokenIntf.Type getType() {
        return compiler.TokenIntf.Type.EOF;
    }
}
