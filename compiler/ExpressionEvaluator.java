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
        // parentheseExpr : NUMBER | LParen sumExpr RParen

        Token token = m_lexer.lookAhead();
        // parentheseExpr : NUMBER
        if (m_lexer.accept(TokenIntf.Type.INTEGER)) { // consume NUMBER
            return Integer.parseInt(token.m_value);
        }
        else {
        // parentheseExpr : LParen sumExpr RParen
            m_lexer.expect(TokenIntf.Type.LPAREN); //consume Lparen
            int result = getQuestionMarkExpr(); //sumExpr
            m_lexer.expect(TokenIntf.Type.RPAREN); //consume Rparen
            return result;
        }
    }

    int getArrowExpr() throws Exception {
        //arrowExpr: ParantheseExpr ARROW ParantheseExpr
        int result = getParantheseExpr();
        Token nextToken = m_lexer.lookAhead();
        while(nextToken.m_type == Type.ARROW) {
            m_lexer.advance();
            int rParenExpr = getParantheseExpr();
            if (result>=rParenExpr){
                result = result-rParenExpr;
            }else{
                result = result+rParenExpr;
            }
        }
        return result;
    }

    int getDashExpr() throws Exception {

        // dashExp : arrowExpr (TDASH arrowExpr)*
        int result = getArrowExpr(); // lhsOperand
        Token nextToken = m_lexer.lookAhead();
        while (nextToken.m_type == TokenIntf.Type.DASH) {
            m_lexer.advance(); // consume TDASH
            int rhsOperand = getArrowExpr();
            result = (int) Math.pow(result, rhsOperand);
            nextToken = m_lexer.lookAhead();
        }
        return result;
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
        // NUMBER ((BITAND | BITOR) NUMBER)*
        // plusMinusExpr ((BITAND | BITOR) plusMinusExpr)*
        int result = getPlusMinusExpr(); // lhsOperand
        Token nextToken = m_lexer.lookAhead();
        while (nextToken.m_type == TokenIntf.Type.BITAND ||
            nextToken.m_type == TokenIntf.Type.BITOR) {
            // consume BITAND|BITOR
            m_lexer.advance();
            int rhsOperand = getPlusMinusExpr();
            if (nextToken.m_type == TokenIntf.Type.BITAND) {
                result = result & rhsOperand;
            } else {
                result = result | rhsOperand;
            }
            nextToken = m_lexer.lookAhead();
        }
        return result;
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
        int result = getMulDivExpr(); // lhsOperand
        Token nextToken = m_lexer.lookAhead();
        while (nextToken.m_type == TokenIntf.Type.GREATER ||
                nextToken.m_type == TokenIntf.Type.LESS ||
                nextToken.m_type == TokenIntf.Type.EQUAL) {
            m_lexer.advance();
            int rhsOperand = getShiftExpr();
            if (nextToken.m_type == TokenIntf.Type.GREATER) {
                return result > rhsOperand ? 1 : 0;
            } else if (nextToken.m_type == TokenIntf.Type.LESS) {
                return result < rhsOperand ? 1 : 0;
            } else {
                return result == rhsOperand ? 1 : 0;
            }
        }

        return result;
    }

    int getAndOrExpr() throws Exception {
        // AndOrExpr : CompareExpr ("&&"" | "||") CompareExpr
        int result = getCompareExpr();
        Token nextToken = m_lexer.lookAhead();
        while (nextToken.m_type == TokenIntf.Type.OR ||
                nextToken.m_type == TokenIntf.Type.AND) {
            m_lexer.advance(); // consume AND or OR
            int rhsOperand = getCompareExpr();
            if (nextToken.m_type == TokenIntf.Type.OR) {
                result = result == 1 || rhsOperand == 1 ? 1 : 0;
            } else {
                result = result == 1 && rhsOperand == 1 ? 1 : 0;
            }
            nextToken = m_lexer.lookAhead();
        }
        return result;
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
