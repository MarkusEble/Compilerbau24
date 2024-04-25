package test;

import java.io.OutputStreamWriter;

public interface TestSuiteIntf {

	// execute a suite of test cases read from input reader
	void execute(compiler.InputReader testCaseSequence, TestCaseIntf testCase) throws Exception;

	// execute a single test case with input and expected output
	void executeTestCase(String input, String expectedOutput, TestCaseIntf testCase) throws Exception;

	// trace test suite content to stream
	void dump(compiler.InputReader testCaseSequence, OutputStreamWriter outStream) throws Exception;
}
