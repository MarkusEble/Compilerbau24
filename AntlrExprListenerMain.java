

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class AntlrExprListenerMain {

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
		// create visitor for expression evaluation
		compiler.antlrvisitor.ExprEvalListener  exprEvalListener = new compiler.antlrvisitor.ExprEvalListener();
		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(exprEvalListener, tree);
		Integer result = exprEvalListener.m_values.get(tree);
		System.out.println(result);
	}

}
