package compiler;

import compiler.TokenIntf.Type;

public class ExpressionEvaluator {
    private Lexer m_lexer;
    
    public ExpressionEvaluator(Lexer lexer) {
        m_lexer = lexer;
    }
    
    public int eval(String val) throws Exception {
        m_lexer.init(val);
        return getQuestionMarkExpr();
    }
    
    int getParantheseExpr() throws Exception {
        Token curToken = m_lexer.lookAhead();
        m_lexer.expect(Token.Type.INTEGER);
        return Integer.valueOf(curToken.m_value);
    }
    
    int getArrowExpr() throws Exception {
        return getParantheseExpr();
    }

    int getDashExpr() throws Exception {
        return getArrowExpr();
    }

    int getUnaryExpr() throws Exception {
        return getDashExpr();
    }
    
    int getMulDivExpr() throws Exception {
        // mulDivExpr: unaryExpr ((MUL | DIV) unaryExpr )*
        int result = getUnaryExpr(); // lhsOperand
        Token nextToken = m_lexer.lookAhead();
        while (nextToken.m_type == TokenIntf.Type.MUL || nextToken.m_type == TokenIntf.Type.DIV ){
            m_lexer.advance(); // consume DIV | MUL
            int rhsOperand = getUnaryExpr();

            if (nextToken.m_type == TokenIntf.Type.MUL) {
                result = result * rhsOperand;
            } else {
                result = result / rhsOperand;
            }
            nextToken = m_lexer.lookAhead();
        }
        return result;
    }
    
    int getPlusMinusExpr() throws Exception {
        // plusMinusExpr : mulDivExpr ((PLUS|MINUS) mulDivExpr)*
        int result = getMulDivExpr(); // lhsOperand
        Token nextToken = m_lexer.lookAhead();
        while (nextToken.m_type == TokenIntf.Type.PLUS ||
            nextToken.m_type == TokenIntf.Type.MINUS) {
            m_lexer.advance(); // consume PLUS|MINUS
            int rhsOperand = getMulDivExpr();
            if (nextToken.m_type == TokenIntf.Type.PLUS) {
                result = result + rhsOperand;
            } else {
                result = result - rhsOperand;
            }
            nextToken = m_lexer.lookAhead();
        }
        return result;
    }

    int getBitAndOrExpr() throws Exception {
        return getPlusMinusExpr();
    }

    int getShiftExpr() throws Exception {
        // shiftExpr : bitAndOrExpr ((SHIFTRIGHT|SHIFTLEFT) bitAndOrExpr)*
        int result = getBitAndOrExpr();
        Token nextToken = m_lexer.lookAhead();
        while (nextToken.m_type == Type.SHIFTLEFT || nextToken.m_type == Type.SHIFTRIGHT) {
            m_lexer.advance();
            int rhsOperand = getBitAndOrExpr();
            if (nextToken.m_type == Type.SHIFTLEFT) {
                result = result << rhsOperand;
            } else {
                result = result >> rhsOperand;
            }
            nextToken = m_lexer.lookAhead();
        }
        return result;
    }

    int getCompareExpr() throws Exception {
        return getShiftExpr();
    }

    int getAndOrExpr() throws Exception {
        return getCompareExpr();
    }

    int getQuestionMarkExpr() throws Exception {
        // getQuestionMark = AndOrExpr ? AndOrExpr : AndOrExpr
        int condition = getAndOrExpr();
        Token nextToken = m_lexer.lookAhead();
        if(nextToken.m_type == TokenIntf.Type.QUESTIONMARK){
            m_lexer.advance();
            int result1 = getQuestionMarkExpr();
            int result2;

            nextToken = m_lexer.lookAhead();
            if(nextToken.m_type == TokenIntf.Type.DOUBLECOLON){
                m_lexer.advance();
                result2 = getQuestionMarkExpr();
            } else {
                throw new Exception("QuestionMarkExpression Error: expected double Column");
            }


            // semantik
            return condition > 0 ? result1 : result2;
        }

        return condition;
    }
}
