package compiler.machines;

public class GanzzahlMachine extends compiler.StateMachine {
    
    public void initStateTable() {
        compiler.State startState = new compiler.State("start", false);
        startState.addTransition('0', "expectNull");
        startState.addTransition('1', "expectZ");
        startState.addTransition('2', "expectZ");
        startState.addTransition('3', "expectZ");
        startState.addTransition('4', "expectZ");
        startState.addTransition('5', "expectZ");
        startState.addTransition('6', "expectZ");
        startState.addTransition('7', "expectZ");
        startState.addTransition('8', "expectZ");
        startState.addTransition('9', "expectZ");
        addState(startState);
        compiler.State expectNullState = new compiler.State("expectNull", true);
        addState(expectNullState);
        compiler.State expectZState = new compiler.State("expectZ", true);
        expectZState.addTransition('0', "expectZ");
        expectZState.addTransition('1', "expectZ");
        expectZState.addTransition('2', "expectZ");
        expectZState.addTransition('3', "expectZ");
        expectZState.addTransition('4', "expectZ");
        expectZState.addTransition('5', "expectZ");
        expectZState.addTransition('6', "expectZ");
        expectZState.addTransition('7', "expectZ");
        expectZState.addTransition('8', "expectZ");
        expectZState.addTransition('9', "expectZ");
        addState(expectZState);
    }

    @Override
    public String getStartState() {
        return "start";
    }

/*
    public compiler.TokenIntf.Type getType() {
        return compiler.TokenIntf.Type.EOF;
    }
*/

}


