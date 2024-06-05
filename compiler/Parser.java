package compiler;

import compiler.TokenIntf.Type;
import compiler.ast.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Parser {
    private Lexer m_lexer;
    private SymbolTableIntf m_symbolTable;
    private final FunctionTableIntf m_functionTable;

    public Parser(Lexer lexer, SymbolTableIntf symbolTable, FunctionTableIntf functionTable) {
        m_lexer = lexer;
        m_symbolTable = symbolTable;
        m_functionTable = functionTable;
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
        }else if (token.m_type == Type.CALL) {
            // parentheseExpr : varExpr
            return getFunctionCallExpr();
        } else {
            // parentheseExpr : LParen sumExpr RParen
            m_lexer.expect(TokenIntf.Type.LPAREN); //consume Lparen
            ASTExprNode result = new ASTParantheseNode(getQuestionMarkExpr()); //sumExpr
            m_lexer.expect(TokenIntf.Type.RPAREN); //consume Rparen
            return result;
        }
    }

    private ASTExprNode getFunctionCallExpr() throws Exception{
        // functionCallExpr := CALL IDENT LPAREN (exprList)? RPAREN
        m_lexer.expect(Type.CALL);
        Token functionNameToken = m_lexer.lookAhead();
        m_lexer.expect(Type.IDENT);
        m_lexer.expect(Type.LPAREN);
        Token nextToken = m_lexer.lookAhead();
        // expressionList can be optional
        List<ASTExprNode> expressions = new ArrayList<>();
        if (nextToken.m_type != Type.RPAREN){
             expressions = getExpressionList();
        }
        m_lexer.expect(Type.RPAREN);

        return new ASTExprFunctionCallNode(functionNameToken.m_value, expressions);
    }

    private List<ASTExprNode> getExpressionList() throws Exception{
        // exprList := questionMarkExpr (COMMA questionMarkExpr)*
        List<ASTExprNode> expressionList = new ArrayList<>();
        ASTExprNode firstExpr = getQuestionMarkExpr();
        expressionList.add(firstExpr);
        Token nextToken = m_lexer.lookAhead();
        while(nextToken.m_type == Type.COMMA){
            m_lexer.advance();
            ASTExprNode expressionRecursive = getQuestionMarkExpr();
            expressionList.add(expressionRecursive);
            nextToken = m_lexer.lookAhead();
        }
        return expressionList;

    }
    private List<String> getParamList() throws Exception{
        // paramList := IDENT (COMMA IDENT)*
        List<String> paramList = new ArrayList<>();
        Token nextToken = m_lexer.lookAhead();
        if (nextToken.m_type == Type.IDENT) {
            m_lexer.advance();
            paramList.add(nextToken.m_value);
            nextToken = m_lexer.lookAhead();
            while (nextToken.m_type == Type.COMMA) {
                m_lexer.advance();
                nextToken = m_lexer.lookAhead();
                m_lexer.expect(Type.IDENT);
                paramList.add(nextToken.m_value);
                nextToken = m_lexer.lookAhead();
            }
        }
        return paramList;

    }

    ASTStmtNode getFunctionStmt() throws Exception{
        // FunctionStmt := Function IDENT LPAREN paramList blockStmt (SEMICOLON)?
        m_lexer.expect(Type.FUNCTION);
        Token functionName = m_lexer.lookAhead();
        m_lexer.expect(Type.IDENT);
        m_lexer.expect(Type.LPAREN);
        List<String> paramList = getParamList();
        paramList.forEach(param -> {
            m_symbolTable.createSymbol(param);
        });
        m_lexer.expect(Type.RPAREN);
        ASTStmtNode stmtList = getBlockStmt();
        if(m_lexer.lookAhead().m_type == Type.SEMICOLON){
            m_lexer.advance();  // Semicolon is optional
        }
        return new ASTFunctionStmtNode(functionName.m_value,paramList,stmtList,m_functionTable,m_symbolTable);
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
        ASTExprNode left = getSpaceshipExpr();
        Token nextToken = m_lexer.lookAhead();
        while (nextToken.m_type == TokenIntf.Type.AND || nextToken.m_type == TokenIntf.Type.OR) {
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
        // stmt: forStmt
        if (nextToken.m_type == TokenIntf.Type.PRINT) {
            return getPrintStmt();
        } else if (nextToken.m_type == TokenIntf.Type.DECLARE) {
            return getVarDeclareStmt();
        } else if (nextToken.m_type == TokenIntf.Type.IDENT) {
            return getAssignStmt();
        } else if (nextToken.m_type == TokenIntf.Type.BLOCK) {
            return getBlock2Stmt();
        } else if (nextToken.m_type == TokenIntf.Type.EXECUTE) {
            return getExecuteNStmt();
        } else if (nextToken.m_type == TokenIntf.Type.FOR) {
            return getForStmt();
        }else if (nextToken.m_type == TokenIntf.Type.SWITCH) {
            return getSwitchStmt();
        } else if (nextToken.m_type == TokenIntf.Type.NUMERIC_IF) {
            return getNumericIf();
        } else if (nextToken.m_type == Type.LOOP) {
            return getLoopStmt();
        } else if (nextToken.m_type == Type.BREAK) {
            return getBreakStmt();
        } else if (nextToken.m_type == Type.WHILE) {
            return getWhileStmt();
        } else if (nextToken.m_type == Type.DO) {
            return getDoWhileStmt();
        } else if (nextToken.m_type == Type.RETURN) {
            return getReturnStmt();
        } else if (nextToken.m_type == Type.FUNCTION) {
            return getFunctionStmt();
        }

        m_lexer.throwCompilerException(
            "Token " + nextToken.m_value + " does not start a statement",
            "Statement");
        return null;
    }

    List<ASTStmtNode> getStmtList() throws Exception {
        // stmtList: stmt stmtList
        // stmtList: epsilon
        List<ASTStmtNode> stmts = new LinkedList<>();
        TokenIntf nextToken = m_lexer.lookAhead();
        while (nextToken.m_type != Type.RBRACE && nextToken.m_type != Type.EOF && nextToken.m_type != Type.CASE) {
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

    private ASTStmtNode getReturnStmt() throws Exception{
        m_lexer.expect(Type.RETURN);
        ASTExprNode returnValue = getQuestionMarkExpr();
        ASTStmtNode returnStmt = new ASTReturnStmtNode(returnValue);
        m_lexer.expect(Type.SEMICOLON);
        return returnStmt;
    }

    ASTStmtNode getExecuteNStmt() throws Exception {
        // EXECUTE QuestionMarkExpr TIMES BlockStmt SEMICOLON
        m_lexer.expect(Type.EXECUTE);
        ASTExprNode mulDivExpr = getQuestionMarkExpr();
        m_lexer.expect(Type.TIMES);
        ASTStmtNode blockStmt = getBlockStmt();
        m_lexer.expect(Type.SEMICOLON);
        return new ASTExecuteNNode(mulDivExpr, blockStmt);
    }

    ASTStmtNode getSwitchStmt() throws Exception{
        // switchStmt: SWITCH LPAREN expr RPAREN LBRACE caseList RBRACE
        m_lexer.expect(Type.SWITCH);
        m_lexer.expect(Type.LPAREN);
        ASTExprNode expr = getQuestionMarkExpr();
        m_lexer.expect(Type.RPAREN);
        m_lexer.expect(Type.LBRACE);
        List<ASTCaseNode> caseList = getCaseList();
        m_lexer.expect(Type.RBRACE);
        return new ASTSwitchNode(expr, caseList);
    }

    List<ASTCaseNode> getCaseList() throws Exception{
        // caseList: case | case caseList
        Token token;
        List<ASTCaseNode> caseList = new LinkedList<>();
        do {
            caseList.add(getCase());
            token = m_lexer.lookAhead();
        } while(token.m_type == Type.CASE);
        return caseList;
    }

    ASTCaseNode getCase() throws Exception{
        // case: CASE NUMBER COLON stmtList
        m_lexer.expect(Type.CASE);
        String numberRaw = m_lexer.lookAhead().m_value;
        m_lexer.expect(Type.INTEGER);
        int number = Integer.parseInt(numberRaw);
        m_lexer.expect(Type.DOUBLECOLON);
        List<ASTStmtNode> stmtList = getStmtList();
        return new ASTCaseNode(number, stmtList);
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

    ASTStmtNode getWhileStmt() throws Exception {
        // WHILE LPAREN expr RPAREN blockStmt SEMICOLON
        m_lexer.expect(Type.WHILE);
        m_lexer.expect(Type.LPAREN);
        ASTExprNode expr = getQuestionMarkExpr();
        m_lexer.expect(Type.RPAREN);
        ASTStmtNode blockStmt = getBlockStmt();
        m_lexer.expect(Type.SEMICOLON);
        return new ASTWhileStmtNode(blockStmt, expr);
    }

    ASTStmtNode getDoWhileStmt() throws Exception {
        // DO blockstmt WHILE LPAREN expr RPAREN SEMICOLON
        m_lexer.expect(Type.DO);
        ASTStmtNode blockStmt = getBlockStmt();
        m_lexer.expect(Type.WHILE);
        m_lexer.expect(Type.LPAREN);
        ASTExprNode expr = getQuestionMarkExpr();
        m_lexer.expect(Type.RPAREN);
        m_lexer.expect(Type.SEMICOLON);
        return new ASTDoWhileStmtNode(blockStmt, expr);
    }

    ASTStmtNode getForStmt() throws Exception {
        /*
            FOR_LOOP        := FOR LPAREN INITIALIZATION CONDITION SEMICOLON ACTION RPAREN L_CURLY_PAREN BLOCK R_CURLY_PAREN SEMICOLON
            INITIALIZATION  := STATEMENT | epsilon
            CONDITION       := EXPRESSION | epsilon
            ACTION          := STATEMENT | epsilon
        */

        m_lexer.expect(Type.FOR);
        m_lexer.expect(Type.LPAREN);

        ASTStmtNode stmt = getStmt();
        ASTExprNode expr = getQuestionMarkExpr();
        m_lexer.expect(Type.SEMICOLON);
        ASTStmtNode action = getStmt();

        m_lexer.expect(Type.RPAREN);

        // Block statement
        ASTStmtNode block = getBlockStmt();

        m_lexer.expect(Type.SEMICOLON);

        return new ASTForNode(
                stmt,
                expr,
                action,
                block
        );
    }



}
