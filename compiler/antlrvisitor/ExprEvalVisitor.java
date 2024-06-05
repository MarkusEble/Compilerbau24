package compiler.antlrvisitor;

import org.antlr.v4.runtime.tree.TerminalNode;

import compiler.antlrcompiler.languageParser.ExprContext;

public class ExprEvalVisitor extends compiler.antlrcompiler.languageBaseVisitor<Integer> {

    public Integer visitExpr(ExprContext ctx) {
        int cnt = ctx.getChildCount();
        int curChildIdx = 0;
        int curNumberIdx = 0;
        int curOpIdx = 0;
        int curResult = Integer.valueOf(ctx.NUMBER(0).getText());
        curChildIdx++;
        curNumberIdx++;
        while (curChildIdx < cnt) {
          TerminalNode nextOp = ctx.SUMOP(curOpIdx);
          curOpIdx++;
          curChildIdx++;
          int nextNumber = Integer.valueOf(ctx.NUMBER(curNumberIdx).getText());
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

}
