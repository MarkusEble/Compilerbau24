import java.io.OutputStreamWriter;

import compiler.machines.BlockkommentarMachine;

public class TestBlockkommentarMachine {

	public static void main(String[] args) throws Exception {
		BlockkommentarMachine machine = new BlockkommentarMachine();
		OutputStreamWriter outWriter = new OutputStreamWriter(System.out);
		machine.process("/* ** /* \n // test comment \n\n */", outWriter);
		System.out.print(machine.asDot());
	}

}
