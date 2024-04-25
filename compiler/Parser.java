package compiler;

import compiler.TokenIntf.Type;
import compiler.ast.*;

public class Parser {
    private Lexer m_lexer;
    private SymbolTableIntf m_symbolTable;

    public Parser(Lexer lexer) {
        m_lexer = lexer;
        m_symbolTable = null;
    }

    public ASTExprNode parseExpression(String val) throws Exception {
        m_lexer.init(val);
        return getQuestionMarkExpr();
    }

    public ASTStmtNode parseStmt(String val) throws Exception {
        m_lexer.init(val);
        return getStmtList();
    }

    ASTExprNode getParantheseExpr() throws Exception {
        String value = m_lexer.lookAhead().m_value;
        m_lexer.advance();
        return new ASTIntegerLiteralNode(value);
    }

    ASTExprNode getArrowExpr() throws Exception {
        return getParantheseExpr();
    }

    ASTExprNode getDashExpr() throws Exception {
        return getArrowExpr();
    }

    ASTExprNode getUnaryExpr() throws Exception {
        return getDashExpr();
    }

    ASTExprNode getMulDivExpr() throws Exception {
        return getUnaryExpr();
    }

    ASTExprNode getPlusMinusExpr() throws Exception {
        return getMulDivExpr();
    }

    ASTExprNode getBitAndOrExpr() throws Exception {
        return getPlusMinusExpr();
    }

    ASTExprNode getShiftExpr() throws Exception {
        return getBitAndOrExpr();
    }

    ASTExprNode getCompareExpr() throws Exception {
        return getShiftExpr();
    }

    ASTExprNode getAndOrExpr() throws Exception {
        return getCompareExpr();
    }

    ASTExprNode getQuestionMarkExpr() throws Exception {
        ASTExprNode condition = getAndOrExpr();
        Token nextToken = m_lexer.lookAhead();
        if(nextToken.m_type == TokenIntf.Type.QUESTIONMARK){
            m_lexer.advance();
            ASTExprNode result1 = getQuestionMarkExpr();
            ASTExprNode result2;

            nextToken = m_lexer.lookAhead();
            if(nextToken.m_type == TokenIntf.Type.DOUBLECOLON){
                m_lexer.advance();
                result2 = getQuestionMarkExpr();
            } else {
                throw new Exception("QuestionMarkExpression Error: expected double Colon");
            }
            return new ASTQuestionMarkExprNode(condition, result1, result2);
        }
        return new ASTQuestionMarkExprNode(condition);
    }

    ASTExprNode getVariableExpr() throws Exception {
        return null;
    }

    ASTStmtNode getAssignStmt() throws Exception {
        return null;
    }

    ASTStmtNode getVarDeclareStmt() throws Exception {
        return null;
    }

    ASTStmtNode getPrintStmt() throws Exception {
        return null;

    }

    ASTStmtNode getStmt() throws Exception {
        return null;
    }

    ASTStmtNode getStmtList() throws Exception {
        return null;
    }

    ASTStmtNode getBlockStmt() throws Exception {
        return null;
    }

}
