

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class AntlrParseTreeMain {

	public static void main(String[] args) throws Exception {
		// create input stream
		CharStream input = CharStreams.fromFileName("compiler/language.txt");
		// create lexer
		compiler.antlrcompiler.languageLexer lexer = new compiler.antlrcompiler.languageLexer(input);
		// create token stream
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		// create parser
		compiler.antlrcompiler.languageParser parser = new compiler.antlrcompiler.languageParser(tokens);
		parser.setBuildParseTree(true);
		// build parse tree
		ParseTree tree = parser.expr();
		// output parse tree
		System.out.println(tree.toStringTree(parser));
	}
}
