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
    public Integer visitShiftExpr(compiler.antlrcompiler.languageParser.ShiftExprContext ctx) {
        int cnt = ctx.getChildCount();
        int curChildIdx = 0;
        int curNumberIdx = 0;
        int curOpIdx = 0;
        int curResult = visitBitAndOrExpr(ctx.bitAndOrExpr(0));
        curChildIdx++;
        curNumberIdx++;
        while (curChildIdx < cnt) {
            TerminalNode nextOp = ctx.SHIFTOP(curOpIdx);
            curOpIdx++;
            curChildIdx++;
            int nextNumber = visitBitAndOrExpr(ctx.bitAndOrExpr(curNumberIdx));
            if (nextOp.getText().equals("<<")) {
                curResult = curResult << nextNumber;
            } else {
                curResult = curResult >> nextNumber;
            }
            curNumberIdx++;
            curChildIdx++;
        }
        return curResult;
    }

    // bitAndOrExpr: sumExpr (BITOP sumExpr)*;
    public Integer visitBitAndOrExpr(languageParser.BitAndOrExprContext ctx) {
        int cnt = ctx.getChildCount();
        int curChildIdx = 0;
        int curNumberIdx = 0;
        int curOpIdx = 0;
        int curResult = visitSumExpr(ctx.sumExpr(0));
        curChildIdx++;
        curNumberIdx++;
        while (curChildIdx < cnt) {
          TerminalNode nextOp = ctx.BITOP(curOpIdx);
          curOpIdx++;
          curChildIdx++;
          int nextNumber = visitSumExpr(ctx.sumExpr(curNumberIdx));
          if (nextOp.getText().equals("&")) {
            curResult &= nextNumber;
          } else {
            curResult |= nextNumber;
          }
          curNumberIdx++;
          curChildIdx++;
        }
        return curResult;
    }

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
    public Integer visitUnaryExpr(languageParser.UnaryExprContext ctx) {
        return visitDashExpr(ctx.dashExpr()) == 1 ? 1 : 0;
    }

    // dashExpr: arrowExpr;
    public Integer visitDashExpr(languageParser.DashExprContext ctx) {
        int cnt = ctx.getChildCount();
        int curChildIdx = 0;
        int curNumberIdx = 0;
        double curResult = visitArrowExpr(ctx.arrowExpr(0));
        curChildIdx++;
        curNumberIdx++;
        while (curChildIdx < cnt) {
            curChildIdx++;
            int nextNumber = visitArrowExpr(ctx.arrowExpr(curNumberIdx));
            curResult = Math.pow(curResult, nextNumber);
            curNumberIdx++;
            curChildIdx++;
        }
        return (int) curResult;
    }

    // arrowExpr: parantheseExpr;

    // parantheseExpr: NUMBER;
    public Integer visitNumber(compiler.antlrcompiler.languageParser.NumberContext ctx) {
      return Integer.valueOf(ctx.NUMBER().getText());
  }
}
