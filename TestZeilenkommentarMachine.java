import java.io.OutputStreamWriter;

import compiler.machines.ZeilenkommentarMachine;

public class TestZeilenkommentarMachine {

	public static void main(String[] args) throws Exception {
		ZeilenkommentarMachine machine = new ZeilenkommentarMachine();
		OutputStreamWriter outWriter = new OutputStreamWriter(System.out);
		machine.process("// test comment\n", outWriter);
		System.out.print(machine.asDot());
	}

}
