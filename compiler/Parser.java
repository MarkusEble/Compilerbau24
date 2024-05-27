package compiler;

import compiler.TokenIntf.Type;
import compiler.ast.*;

import java.util.LinkedList;
import java.util.List;

public class Parser {
    private Lexer m_lexer;
    private SymbolTableIntf m_symbolTable;

    public Parser(Lexer lexer, SymbolTableIntf symbolTable, FunctionTableIntf fucntionTable) {
        m_lexer = lexer;
        m_symbolTable = symbolTable;
    }

    public ASTExprNode parseExpression(String val) throws Exception {
        m_lexer.init(val);
        return getQuestionMarkExpr();
    }

    public ASTStmtNode parseStmt(String val) throws Exception {
        m_lexer.init(val);
        return getBlockStmt();
    }

    ASTExprNode getParantheseExpr() throws Exception {
        // parentheseExpr : NUMBER | varExpr | LParen sumExpr RParen

        Token token = m_lexer.lookAhead();
        // parentheseExpr : NUMBER
        if (token.m_type == Type.INTEGER) { // consume NUMBER
            m_lexer.advance();
            return new ASTIntegerLiteralNode(token.m_value);
        } else if (token.m_type == Type.IDENT) {
            // parentheseExpr : varExpr
            return getVariableExpr();
        } else {
            // parentheseExpr : LParen sumExpr RParen
            m_lexer.expect(TokenIntf.Type.LPAREN); //consume Lparen
            ASTExprNode result = new ASTParantheseNode(getQuestionMarkExpr()); //sumExpr
            m_lexer.expect(TokenIntf.Type.RPAREN); //consume Rparen
            return result;
        }
    }

    ASTExprNode getArrowExpr() throws Exception {
        ASTExprNode result = getParantheseExpr();
        Token nextToken = m_lexer.lookAhead();
        while (nextToken.m_type == Type.ARROW) {
            m_lexer.advance();
            result = new ASTArrowNode(result, getParantheseExpr());
            nextToken = m_lexer.lookAhead();
        }
        return result;
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
        Token nextToken = m_lexer.lookAhead();
        if (nextToken.m_type == Type.MINUS || nextToken.m_type == Type.NOT) {
            m_lexer.advance();
            return new ASTUnaryExprNode(getDashExpr(), nextToken.m_type);
        }
        return getDashExpr();
    }

    ASTExprNode getMulDivExpr() throws Exception {
        ASTExprNode result = getUnaryExpr(); // lhsOperand
        Token nextToken = m_lexer.lookAhead();
        while (nextToken.m_type == TokenIntf.Type.MUL || nextToken.m_type == TokenIntf.Type.DIV) {
            m_lexer.advance(); // consume DIV | MUL
            ASTExprNode rhsOperand = getUnaryExpr();
            result = new ASTMulDivExprNode(result, nextToken, rhsOperand);
            nextToken = m_lexer.lookAhead();
        }
        return result;
    }

    ASTExprNode getPlusMinusExpr() throws Exception {
        // plusMinusExpr: mulDivExpr ((PLUS|MINUS) mulDivExpr)*
        ASTExprNode result = getMulDivExpr(); // lhsOperand
        Token nextToken = m_lexer.lookAhead();
        while (nextToken.m_type == TokenIntf.Type.PLUS || nextToken.m_type == TokenIntf.Type.MINUS) {
            m_lexer.advance(); // consume PLUS | MINUS
            ASTExprNode rhsOperand = getMulDivExpr();
            result = new ASTPlusMinusExprNode(nextToken, result, rhsOperand);
            nextToken = m_lexer.lookAhead();
        }
        return result;
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
        ASTExprNode result = getBitAndOrExpr();
        Token nextToken = m_lexer.lookAhead();
        while (nextToken.m_type == Type.SHIFTLEFT || nextToken.m_type == Type.SHIFTRIGHT) {
            ASTShiftExprNode temp = new ASTShiftExprNode();
            temp.lhsOperand = result;
            temp.shiftKeyword = nextToken;
            m_lexer.advance();
            temp.rhsOperand = getBitAndOrExpr();
            result = temp;
            nextToken = m_lexer.lookAhead();
        }
        return result;
    }

    ASTExprNode getCompareExpr() throws Exception {
        ASTExprNode result = getShiftExpr(); // lhsOperand
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

    ASTExprNode getSpaceshipExpr() throws Exception {
        ASTExprNode result = getCompareExpr(); //lhsOperand
        Token nextToken = m_lexer.lookAhead();
        while(nextToken.m_type == Type.SPACESHIP) {
            m_lexer.advance();
            result = new ASTSpaceshipExprNode(result, getCompareExpr());
            nextToken = m_lexer.lookAhead();
        }
        return result;
    }

    ASTExprNode getAndOrExpr() throws Exception {
        ASTExprNode left = getSpaceshipExpr(); // lhsOperand
        Token nextToken = m_lexer.lookAhead();
        while (nextToken.m_type == TokenIntf.Type.AND || nextToken.m_type == TokenIntf.Type.OR) {
            // consume BITAND|BITOR
            m_lexer.advance();
            ASTExprNode rhsOperand = getSpaceshipExpr();
            left = new ASTAndOrExpr(nextToken, left, rhsOperand);
            nextToken = m_lexer.lookAhead();
        }
        return left;

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
        return condition;
    }


    ASTExprNode getVariableExpr() throws Exception {
        // variable: IDENT
        Token nextToken = m_lexer.lookAhead();
        Symbol value = m_symbolTable.getSymbol(nextToken.m_value);
        m_lexer.advance();
        if (value == null) {
            m_lexer.throwCompilerException(
                "Identifier has not been declared " + nextToken.m_value,
                "Identifier should have been declared before use");
        }
        ASTExprNode result = new ASTVariableExprNode(value);
        return result;
    }

    ASTStmtNode getAssignStmt() throws Exception {
        // assignStmt: IDENTIFIER ASSIGN expr SEMICOLON
        Token nextToken = m_lexer.lookAhead();
        m_lexer.expect(Type.IDENT);
        if (m_symbolTable.getSymbol(nextToken.m_value) != null) {
            m_lexer.expect(Type.ASSIGN);
            ASTExprNode expr = getQuestionMarkExpr();
            m_lexer.expect(Type.SEMICOLON);
            return new ASTAssignStmtNode(nextToken, expr, m_symbolTable);
        } else {
            m_lexer.throwCompilerException("Identifier has not been declared " + nextToken.m_value,
                "Identifier should have been declared before use");
            return null;
        }
    }

    ASTStmtNode getVarDeclareStmt() throws Exception {
        // declareStmt: DECLARE identifier SEMICOL
        m_lexer.expect(Type.DECLARE);
        TokenIntf ident = m_lexer.lookAhead();
        m_lexer.expect(Type.IDENT);
        if (m_symbolTable.getSymbol(ident.m_value) != null) {
            m_lexer.throwCompilerException("Illegal redefinition of identifier " + ident.m_value, "new identifier");
            return null;
        } else {
            m_lexer.expect(Type.SEMICOLON);
            Symbol varSymbol = m_symbolTable.createSymbol(ident.m_value);
            return new ASTVariableDeclareNode(varSymbol);
        }
    }

    ASTStmtNode getPrintStmt() throws Exception {
        // printStmt: PRINT expr SEMICOLON
        m_lexer.expect(TokenIntf.Type.PRINT);
        ASTExprNode expression = getQuestionMarkExpr();
        m_lexer.expect(TokenIntf.Type.SEMICOLON);
        return new ASTPrintStmtNode(expression);
    }

    ASTStmtNode getStmt() throws Exception {
        Token nextToken = m_lexer.lookAhead();
        // stmt: printStmt
        // stmt: declareStmt
        // stmt: assignStmt
        if (nextToken.m_type == TokenIntf.Type.PRINT) {
            return getPrintStmt();
        } else if (nextToken.m_type == TokenIntf.Type.DECLARE) {
            return getVarDeclareStmt();
        } else if (nextToken.m_type == TokenIntf.Type.IDENT) {
            return getAssignStmt();
        } else if (nextToken.m_type == TokenIntf.Type.BLOCK) {
            return getBlock2Stmt();
        } else if (nextToken.m_type == TokenIntf.Type.NUMERIC_IF) {
            return getNumericIf();
        } else if (nextToken.m_type == Type.LOOP) {
            return getLoopStmt();
        } else if (nextToken.m_type == Type.BREAK) {
            return getBreakStmt();
        }
        return null;
    }

    List<ASTStmtNode> getStmtList() throws Exception {
        // stmtList: stmt stmtList
        // stmtList: epsilon
        List<ASTStmtNode> stmts = new LinkedList<>();
        TokenIntf nextToken = m_lexer.lookAhead();
        while (nextToken.m_type != Type.RBRACE && nextToken.m_type != Type.EOF) {
            ASTStmtNode stmt = getStmt();
            stmts.add(stmt);
            nextToken = m_lexer.lookAhead();
        }
        return stmts;
    }

    ASTStmtNode getBlockStmt() throws Exception {
        // LBRACE stmtList RBRACE

        m_lexer.expect(Type.LBRACE);
        List<ASTStmtNode> stmts = getStmtList();

        m_lexer.expect(Type.RBRACE);

        return new ASTBlockStmtNode(stmts);
    }

    ASTStmtNode getBlock2Stmt() throws Exception {
        // BLOCK LBRACE stmtList RBRACE
        m_lexer.expect(Type.BLOCK);
        m_lexer.expect(Type.LBRACE);

        List<ASTStmtNode> stmts = getStmtList();

        m_lexer.expect(Type.RBRACE);

        return new ASTBlock2StmtNode(stmts);
    }

    ASTStmtNode getNumericIf() throws Exception {
//        numeric_if: NUMERIC_IF LPAREN expr RPAREN pos neg zero
//        pos: POSITIVE blockstmt
//        neg: NEGATIVE blockstmt
//        zero: ZERO blockstmt
        m_lexer.expect(Type.NUMERIC_IF);
        m_lexer.expect(Type.LPAREN);

        ASTExprNode expr = getQuestionMarkExpr();

        m_lexer.expect(Type.RPAREN);

        m_lexer.expect(Type.POSITIVE);
        ASTStmtNode pos = getBlockStmt();
        m_lexer.expect(Type.NEGATIVE);
        ASTStmtNode neg = getBlockStmt();
        m_lexer.expect(Type.ZERO);
        ASTStmtNode zero = getBlockStmt();


        return new ASTNumericIfNode(expr, pos, neg, zero);
    }
    
    ASTStmtNode getLoopStmt() throws Exception {
        // LOOP blockStmt ENDLOOP
        m_lexer.expect(Type.LOOP);
        ASTStmtNode blockStmt = getBlockStmt();
        m_lexer.expect(Type.ENDLOOP);
        return new ASTLoopStmtNode(blockStmt);
    }

    ASTStmtNode getBreakStmt() throws Exception {
        // BREAK SEMICOLON
        m_lexer.expect(Type.BREAK);
        m_lexer.expect(Type.SEMICOLON);
        return new ASTBreakStmtNode();
    }
}
