package compiler.machines;

import compiler.State;
import compiler.StateMachine;
import compiler.TokenIntf.Type;

public class BlockkommentarMachine extends StateMachine {

	@Override
	public void initStateTable() {
		State startState = new State("start", false);
		startState.addTransition('/', "expectAsterisk");
		addState(startState);

		State expectAsterisk = new State("expectAsterisk", false);
		expectAsterisk.addTransition('*', "expectAny");
		addState(expectAsterisk);

		State expectAny = new State("expectAny", false);
		// any character except '*' results in staying in the same state
		expectAny.addTransitionRange((char) 1, (char) 41, "expectAny");
		expectAny.addTransitionRange((char) 43, (char) 255, "expectAny");
		expectAny.addTransition('*', "expectSlash");
		addState(expectAny);

		State expectSlash = new State("expectSlash", false);
		expectSlash.addTransition('*', "expectSlash");
		// any character except '*' and '/' results in the "expectAny" state
		expectSlash.addTransitionRange((char) 1, (char) 41, "expectAny");
		expectSlash.addTransitionRange((char) 43, (char) 46, "expectAny");
		expectSlash.addTransitionRange((char) 48, (char) 255, "expectAny");
		expectSlash.addTransition('/', "end");
		addState(expectSlash);

		State endState = new State("end", true);
		addState(endState);
	}

	@Override
	public String getStartState() {
		return "start";
	}

	@Override
	public Type getType() {
		return Type.MULTILINECOMMENT;
	}

}
