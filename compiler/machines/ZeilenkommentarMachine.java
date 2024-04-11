package compiler.machines;

import compiler.State;
import compiler.StateMachine;
import compiler.TokenIntf.Type;

public class ZeilenkommentarMachine extends StateMachine {

	@Override
	public void initStateTable() {
		State startState = new State("start", false);
		startState.addTransition('/', "expectSlash");
		addState(startState);
		
		State expectSlash = new State("expectSlash", false);
		expectSlash.addTransition('/', "expectAny");
		addState(expectSlash);
		
		State expectAny = new State("expectAny", false);
		// any character except newline results in staying in the same state
		expectAny.addTransitionRange((char) 1, (char) 9, "expectAny");
		expectAny.addTransitionRange((char) 11, (char) 255, "expectAny");
		expectAny.addTransition('\n', "end");
		addState(expectAny);
		
		State endState = new State("end", true);
		addState(endState);
	}

	@Override
	public String getStartState() {
		return "start";
	}

	@Override
	public Type getType() {
		return Type.LINECOMMENT;
	}

}
