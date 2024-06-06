

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class AntlrExprVisitor {

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
		compiler.antlrvisitor.ExprEvalVisitor  exprEvalVisitor = new compiler.antlrvisitor.ExprEvalVisitor();
		Integer result = exprEvalVisitor.visit(tree);
		System.out.println(result);
	}

	// questionMarkExpr: andOrExpr;

    // andOrExpr: cmpExpr;

    // cmpExpr: shiftExpr;

    // shiftExpr: bitAndOrExpr;

    // bitAndOrExpr: sumExpr;

    // sumExpr: mulDivExpr (SUMOP  mulDivExpr)*;

    // mulDivExpr: unaryExpr;

    // unaryExpr: dashExpr;

    // dashExpr: arrowExpr;

    // arrowExpr: parantheseExpr;

    // parantheseExpr: NUMBER;

}
