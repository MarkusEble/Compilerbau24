package compiler.machines;

public class CharliteralMachine extends compiler.StateMachine{

    public void initStateTable() {
        compiler.State startState = new compiler.State("start", false);
        startState.addTransition('\'', "expectChar");
        addState(startState);
        compiler.State expectCharState = new compiler.State("expectChar", false);
        expectCharState.addTransitionRange((char) 0, (char)('\'' - 1), "expectCharEnd");
        expectCharState.addTransitionRange((char)('\'' + 1), (char) 255, "expectCharEnd");
        addState(expectCharState);
        compiler.State expectCharEndState = new compiler.State("expectCharEnd", false);
        expectCharEndState.addTransition('\'', "end");
        addState(expectCharEndState);
        compiler.State end = new compiler.State("end", true);
        addState(end);
    }

    @Override
    public String getStartState() {
        return "start";
    }

    public compiler.TokenIntf.Type getType() {
        return compiler.TokenIntf.Type.CHAR;
    }
}
