package compiler.antlrvisitor;

import org.antlr.v4.runtime.tree.TerminalNode;

public class ExprAstVisitor extends compiler.antlrcompiler.languageBaseVisitor<compiler.ast.ASTExprNode> {

    // questionMarkExpr: andOrExpr;

    // andOrExpr: cmpExpr;

    // cmpExpr: shiftExpr;

    // shiftExpr: bitAndOrExpr;

    // bitAndOrExpr: sumExpr;

    // sumExpr: mulDivExpr (SUMOP  mulDivExpr)*;

    // mulDivExpr: unaryExpr;

    // unaryExpr: arrowExpr (DASH arrowExpr)*;

    // dashExpr: arrowExpr;

    // arrowExpr: parantheseExpr;

    // parantheseExpr: NUMBER;

}
