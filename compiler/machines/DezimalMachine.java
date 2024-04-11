package compiler.machines;

import compiler.TokenIntf;

public class DezimalMachine extends compiler.StateMachine{
    @Override
    public void initStateTable() {
        compiler.State startState =new compiler.State("start", false);
        startState.addTransitionRange('0','9',"start");
        startState.addTransition('.',"expectedValueAfterPoint");
        addState(startState);
        compiler.State expectedValueAfterPointState = new compiler.State("expectedValueAfterPoint",false);
        expectedValueAfterPointState.addTransitionRange('0','9',"expectedMoreOrFinal");
        addState(expectedValueAfterPointState);
        compiler.State expectedMoreOrFinalState = new compiler.State("expectedMoreOrFinal", true);
        expectedValueAfterPointState.addTransitionRange('0','9',"expectedMoreOrFinal");
        addState(expectedMoreOrFinalState);
    }

    @Override
    public String getStartState() {
        return "start";
    }

    @Override
    public TokenIntf.Type getType() {
        return TokenIntf.Type.DECIMAL;
    }
}
