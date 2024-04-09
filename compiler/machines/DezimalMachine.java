package compiler.machines;

import compiler.TokenIntf;

public class DezimalMachine extends compiler.StateMachine{
    @Override
    public void initStateTable() {
        compiler.State startState =new compiler.State("start", false);
        startState.addTransition('0',"start");
        startState.addTransition('1',"start");
        startState.addTransition('2',"start");
        startState.addTransition('3',"start");
        startState.addTransition('4',"start");
        startState.addTransition('5',"start");
        startState.addTransition('6',"start");
        startState.addTransition('7',"start");
        startState.addTransition('8',"start");
        startState.addTransition('9',"start");
        startState.addTransition('.',"expectedValueAfterPoint");
        addState(startState);
        compiler.State expectedValueAfterPointState = new compiler.State("expectedValueAfterPoint",false);
        expectedValueAfterPointState.addTransition('0',"expectedMoreOrFinal");
        expectedValueAfterPointState.addTransition('1',"expectedMoreOrFinal");
        expectedValueAfterPointState.addTransition('2',"expectedMoreOrFinal");
        expectedValueAfterPointState.addTransition('3',"expectedMoreOrFinal");
        expectedValueAfterPointState.addTransition('4',"expectedMoreOrFinal");
        expectedValueAfterPointState.addTransition('5',"expectedMoreOrFinal");
        expectedValueAfterPointState.addTransition('6',"expectedMoreOrFinal");
        expectedValueAfterPointState.addTransition('7',"expectedMoreOrFinal");
        expectedValueAfterPointState.addTransition('8',"expectedMoreOrFinal");
        expectedValueAfterPointState.addTransition('9',"expectedMoreOrFinal");
        addState(expectedValueAfterPointState);
        compiler.State expectedMoreOrFinalState = new compiler.State("expectedMoreOrFinal", true);
        expectedValueAfterPointState.addTransition('0',"expectedMoreOrFinal");
        expectedValueAfterPointState.addTransition('1',"expectedMoreOrFinal");
        expectedValueAfterPointState.addTransition('2',"expectedMoreOrFinal");
        expectedValueAfterPointState.addTransition('3',"expectedMoreOrFinal");
        expectedValueAfterPointState.addTransition('4',"expectedMoreOrFinal");
        expectedValueAfterPointState.addTransition('5',"expectedMoreOrFinal");
        expectedValueAfterPointState.addTransition('6',"expectedMoreOrFinal");
        expectedValueAfterPointState.addTransition('7',"expectedMoreOrFinal");
        expectedValueAfterPointState.addTransition('8',"expectedMoreOrFinal");
        expectedValueAfterPointState.addTransition('9',"expectedMoreOrFinal");
        addState(expectedMoreOrFinalState);
    }

    @Override
    public String getStartState() {
        return "start";
    }

    @Override
    public TokenIntf.Type getType() {
        return compiler.TokenIntf.Type.EOF;
    }
}
