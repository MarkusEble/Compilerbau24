package compiler.machines;

public class LineCommentMachine extends compiler.StateMachine {
    
    public void initStateTable() {
        compiler.State startState = new compiler.State("start", false);
        startState.addTransition('/', "expect/");
        addState(startState);

        compiler.State expectBState = new compiler.State("expect/", false);
        expectBState.addTransition('/', "expectComment");
        addState(expectBState);

        compiler.State expectCState = new compiler.State("expectComment", true);
        expectCState.addTransitionRange('a', 'z', "expectComment");
        expectCState.addTransitionRange('A', 'Z', "expectComment");
        expectCState.addTransitionRange('0', '9', "expectComment");
        expectCState.addTransition(' ', "expectComment");
        addState(expectCState);
    }

    @Override
    public String getStartState() {
        return "start";
    }

    public compiler.TokenIntf.Type getType() {
        return compiler.TokenIntf.Type.EOF;
    }
   
}


