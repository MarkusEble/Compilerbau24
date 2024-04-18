package compiler;

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
        // unaryExpr : (MINUS,NOT) DashExpr
        Token nextToken = m_lexer.lookAhead();

        if (nextToken.m_type == TokenIntf.Type.MINUS || nextToken.m_type == TokenIntf.Type.NOT) {
            m_lexer.advance();

            int result = getDashExpr();

            if (nextToken.m_type == TokenIntf.Type.NOT) {
                result = result > 0 ? 0 : 1;
            } else if (nextToken.m_type == TokenIntf.Type.MINUS) {
                result = -result;
            }

            return result;
        }

        return getDashExpr();
    }
    
    int getMulDivExpr() throws Exception {
        return getUnaryExpr();
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
        return getBitAndOrExpr();
    }

    int getCompareExpr() throws Exception {
        return getShiftExpr();
    }

    int getAndOrExpr() throws Exception {
        return getCompareExpr();
    }

    int getQuestionMarkExpr() throws Exception {
        return getAndOrExpr();
    }
}
