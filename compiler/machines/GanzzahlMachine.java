package compiler.machines;

public class GanzzahlMachine extends compiler.StateMachine {
    
    public void initStateTable() {
        compiler.State startState = new compiler.State("start", false);
        startState.addTransition('0', "expectNull");
        startState.addTransitionRange('1', '9',"expectZ");
        addState(startState);
        compiler.State expectNullState = new compiler.State("expectNull", true);
        addState(expectNullState);
        compiler.State expectZState = new compiler.State("expectZ", true);
        expectZState.addTransitionRange('0', '9',"expectZ");
        addState(expectZState);
    }

    @Override
    public String getStartState() {
        return "start";
    }


    public compiler.TokenIntf.Type getType() {
        return compiler.TokenIntf.Type.INTEGER;
    }


}


