package compiler.antlrvisitor;

import org.antlr.v4.runtime.tree.TerminalNode;

public class ExprEvalVisitor extends compiler.antlrcompiler.languageBaseVisitor<Integer> {

	  // questionMarkExpr: andOrExpr;

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
