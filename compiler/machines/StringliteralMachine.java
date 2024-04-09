package compiler.machines;

public class StringliteralMachine extends compiler.StateMachine{
    public void initStateTable() {
        compiler.State startState = new compiler.State("start", false);
        startState.addTransition('"', "expectStringEnd");
        addState(startState);
        compiler.State expectStringEnd = new compiler.State("expectStringEnd", false);
        expectStringEnd.addTransitionRange((char) 0, (char) 33, "expectStringEnd");
        expectStringEnd.addTransition('"', "end");
        expectStringEnd.addTransitionRange((char) 35, (char) 255, "expectStringEnd");
        addState(expectStringEnd);
        compiler.State end = new compiler.State("end", true);
        addState(end);
    }

    @Override
    public String getStartState() {
        return "start";
    }

    public compiler.TokenIntf.Type getType() {
        return compiler.TokenIntf.Type.STRING;
    }

}
