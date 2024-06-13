package compiler.antlrvisitor;

import compiler.antlrcompiler.languageParser;
import org.antlr.v4.runtime.tree.TerminalNode;

public class ExprEvalVisitor extends compiler.antlrcompiler.languageBaseVisitor<Integer> {

	  // questionMarkExpr: andOrExpr;
    public Integer visitQuestionMarkExpr(languageParser.QuestionMarkExprContext ctx){
        // andOrExpr (QUESTIONMARK questionMarkExpr DOUBLECOLON questionMarkExpr)?;
        int cnt = ctx.getChildCount();
        int curExpressionResult = visitAndOrExpr(ctx.andOrExpr());
        if (cnt > 1){
            if (curExpressionResult != 0){
                return visitQuestionMarkExpr(ctx.questionMarkExpr(1));
            } else {
                return visitQuestionMarkExpr(ctx.questionMarkExpr(2));
            }
        }
        return curExpressionResult;
    }

    // andOrExpr: cmpExpr;

    // cmpExpr: shiftExpr;

    // shiftExpr: bitAndOrExpr;

    // bitAndOrExpr: sumExpr;

    // sumExpr: mulDivExpr (SUMOP  mulDivExpr)*;
    public Integer visitSumExpr(compiler.antlrcompiler.languageParser.SumExprContext ctx) {
        int cnt = ctx.getChildCount();
        int curChildIdx = 0;
        int curNumberIdx = 0;
        int curOpIdx = 0;
        int curResult = visitMulDivExpr(ctx.mulDivExpr(0));
        curChildIdx++;
        curNumberIdx++;
        while (curChildIdx < cnt) {
          TerminalNode nextOp = ctx.SUMOP(curOpIdx);
          curOpIdx++;
          curChildIdx++;
          int nextNumber = visitMulDivExpr(ctx.mulDivExpr(curNumberIdx));
          if (nextOp.getText().equals("+")) {
            curResult += nextNumber;
          } else {
            curResult -= nextNumber;
          }
          curNumberIdx++;
          curChildIdx++;
        }
        return curResult;
    }

    // mulDivExpr: unaryExpr;

    // unaryExpr: dashExpr;

    // dashExpr: arrowExpr;

    // arrowExpr: parantheseExpr;

    // parantheseExpr: NUMBER;
    public Integer visitNumber(compiler.antlrcompiler.languageParser.NumberContext ctx) {
      return Integer.valueOf(ctx.NUMBER().getText());
  }
}
