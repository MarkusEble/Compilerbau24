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
        compiler.State expectedValueAfterPointState = new compiler.State("expectedValueAfterPoint",true);
        expectedValueAfterPointState.addTransition('0',"expectedValueAfterPoint");
        expectedValueAfterPointState.addTransition('1',"expectedValueAfterPoint");
        expectedValueAfterPointState.addTransition('2',"expectedValueAfterPoint");
        expectedValueAfterPointState.addTransition('3',"expectedValueAfterPoint");
        expectedValueAfterPointState.addTransition('4',"expectedValueAfterPoint");
        expectedValueAfterPointState.addTransition('5',"expectedValueAfterPoint");
        expectedValueAfterPointState.addTransition('6',"expectedValueAfterPoint");
        expectedValueAfterPointState.addTransition('7',"expectedValueAfterPoint");
        expectedValueAfterPointState.addTransition('8',"expectedValueAfterPoint");
        expectedValueAfterPointState.addTransition('9',"expectedValueAfterPoint");
        addState(expectedValueAfterPointState);
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
