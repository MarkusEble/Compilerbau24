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
        ASTExprNode result = new ASTIntegerLiteralNode(m_lexer.lookAhead().m_value);
        m_lexer.advance();
        return result;
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
        ASTExprNode result = getMulDivExpr(); // lhsOperand
        Token nextToken = m_lexer.lookAhead();
        while (nextToken.m_type == TokenIntf.Type.GREATER ||
                nextToken.m_type == TokenIntf.Type.LESS ||
                nextToken.m_type == TokenIntf.Type.EQUAL) {
            m_lexer.advance();
            ASTExprNode rhsOperand = getShiftExpr();
            result = new ASTExprCompNode(result, nextToken, rhsOperand);
            nextToken = m_lexer.lookAhead();
        }

        return result;
    }

    ASTExprNode getAndOrExpr() throws Exception {
        return getCompareExpr();
    }

    ASTExprNode getQuestionMarkExpr() throws Exception {
        return getAndOrExpr();
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

    ASTStmtNode getPrintStmt() throws Exception{
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
