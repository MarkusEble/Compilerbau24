package compiler;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Utils {

	public static final int MIN_ELEMENTS_FOR_INTERVAL = 3;

	static public Map<String, String> collapse(Map<String, String> transitionMap) {
		Map<String, List<String>> combinedTransitions = new HashMap<>();
		for (String terminal : transitionMap.keySet()) {
			String state = transitionMap.get(terminal);
			List<String> terminals = combinedTransitions.get(state);
			if (null == terminals) {
				terminals = new LinkedList<>();
				combinedTransitions.put(state, terminals);
			}
			terminals.add(terminal);
		}

		Map<String, String> collapsedMap = new HashMap<>();
		for (Map.Entry<String, List<String>> entry : combinedTransitions.entrySet()) {
			StringBuilder stringBuilder = new StringBuilder();
			String state = entry.getKey();
			List<String> terminals = entry.getValue();
			Collections.sort(terminals);
			List<String> intervals = extendInterval(terminals.iterator());
			for (String terminal : intervals) {
				stringBuilder.append(terminal + "|");
			}
			collapsedMap.put(stringBuilder.substring(0, stringBuilder.length() - 1).toString(), state);
		}

		return collapsedMap;
	}

	static public List<String> extendInterval(Iterator<String> terminals) {
		List<String> intervals = new LinkedList<>();
		extendInterval(intervals, terminals);
		return intervals;
	}

	static private void extendInterval(List<String> intervals, char lowestCodepoint, char lastCodepoint,
			Iterator<String> terminals) {
		if (!terminals.hasNext()) {
			if (lastCodepoint - lowestCodepoint >= MIN_ELEMENTS_FOR_INTERVAL) {
				intervals.add(String.format("[%c-%c]", lowestCodepoint, lastCodepoint));
			} else {
				for (char c = lowestCodepoint; c <= lastCodepoint; ++c) {
					intervals.add(c + "");
				}
			}
			return;
		}
		String terminal = terminals.next();
		if (1 == terminal.length()) {
			char codepoint = terminal.charAt(0);
			if (lastCodepoint + 1 == codepoint) {
				extendInterval(intervals, lowestCodepoint, codepoint, terminals);
			} else {
				if (lastCodepoint - lowestCodepoint >= MIN_ELEMENTS_FOR_INTERVAL) {
					intervals.add(String.format("[%c-%c]", lowestCodepoint, lastCodepoint));
				}
				extendInterval(intervals, codepoint, codepoint, terminals);
			}
		} else {
			intervals.add(terminal);
			extendInterval(intervals, lowestCodepoint, lastCodepoint, terminals);
		}
	}

	static private void extendInterval(List<String> intervals, Iterator<String> terminals) {
		if (!terminals.hasNext()) {
			return;
		}
		String terminal = terminals.next();
		if (1 == terminal.length()) {
			char codepoint = terminal.charAt(0);
			extendInterval(intervals, codepoint, codepoint, terminals);
		} else {
			intervals.add(terminal);
			extendInterval(intervals, terminals);
		}
	}

}
