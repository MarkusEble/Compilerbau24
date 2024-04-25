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
        // parentheseExpr : NUMBER | LParen sumExpr RParen

        Token token = m_lexer.lookAhead();
        // parentheseExpr : NUMBER
        if (m_lexer.accept(TokenIntf.Type.INTEGER)) { // consume NUMBER
            return new ASTIntegerLiteralNode(token.m_value);
        }
        else {
            // parentheseExpr : LParen sumExpr RParen
            m_lexer.expect(TokenIntf.Type.LPAREN); //consume Lparen
            ASTExprNode result = new ASTParantheseNode(getQuestionMarkExpr()); //sumExpr
            m_lexer.expect(TokenIntf.Type.RPAREN); //consume Rparen
            return result;
        }
    }
    
    ASTExprNode getArrowExpr() throws Exception {
        return getParantheseExpr();
    }

    ASTExprNode getDashExpr() throws Exception {
        ASTExprNode lhsOperand = getArrowExpr();
        Token nextToken = m_lexer.lookAhead();
        while (nextToken.m_type == TokenIntf.Type.DASH) {
            m_lexer.advance();
            ASTExprNode rhsOperand = getArrowExpr();
            lhsOperand = new ASTDashNode(lhsOperand, rhsOperand);
            nextToken = m_lexer.lookAhead();
        }
        return lhsOperand;
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
        // NUMBER ((BITAND | BITOR) NUMBER)*
        // plusMinusExpr ((BITAND | BITOR) plusMinusExpr)*
        ASTExprNode lhs = getPlusMinusExpr();
        Token nextToken = m_lexer.lookAhead();
        while (nextToken.m_type == TokenIntf.Type.BITAND || nextToken.m_type == TokenIntf.Type.BITOR) {
            // consume BITAND|BITOR
            TokenIntf.Type operator = nextToken.m_type;
            m_lexer.advance();
            ASTExprNode rhs = getPlusMinusExpr();

            lhs = new ASTBitAndOr(lhs, operator, rhs);
            nextToken = m_lexer.lookAhead();
        }
        return lhs;
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
