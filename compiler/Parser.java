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
        return new ASTIntegerLiteralNode(m_lexer.lookAhead().m_value);
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
        // shiftExpr : bitAndOrExpr ((SHIFTRIGHT|SHIFTLEFT) bitAndOrExpr)*
//        ASTExprNode shiftExprNode = new ASTShiftExprNode();
//        ASTExprNode result;
//        result = getBitAndOrExpr();
//        shiftExprNode.lhsOperand = getBitAndOrExpr();
//        Token token = m_lexer.lookAhead();
//        while (token.m_type == Type.SHIFTLEFT || token.m_type == Type.SHIFTRIGHT) {
//            m_lexer.advance();
//            shiftExprNode.rhsOperand = getBitAndOrExpr();
////            if (shiftExprNode.shiftKeyword.m_type == Type.SHIFTLEFT) {
////                result = result << rhsOperand;
////            } else {
////                result = result >> rhsOperand;
////            }
//            shiftExprNode.shiftKeyword = m_lexer.lookAhead();
//        }
//        return shiftExprNode;


        ASTExprNode result = getBitAndOrExpr();
        m_lexer.advance();
        Token nextToken = m_lexer.lookAhead();
        while (nextToken.m_type == Type.SHIFTLEFT || nextToken.m_type == Type.SHIFTRIGHT) {
            ASTShiftExprNode temp = new ASTShiftExprNode();
            temp.lhsOperand = result;
            temp.shiftKeyword = nextToken;
            m_lexer.advance();
            temp.rhsOperand = getBitAndOrExpr();
            m_lexer.advance();
            result = temp;
            nextToken = m_lexer.lookAhead();
        }
        return result;
    }

    ASTExprNode getCompareExpr() throws Exception {
        return getShiftExpr();
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
