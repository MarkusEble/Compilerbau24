package compiler.ast;

import compiler.CompileEnvIntf;
import compiler.InstrIntf;
import compiler.TokenIntf;
import compiler.instr.InstrComp;
import compiler.instr.InstrCondJump;
import compiler.instr.InstrIntegerLiteral;

import java.io.OutputStreamWriter;

public class ASTSpaceshipExprNode extends ASTExprNode {
    public ASTExprNode m_rvalue;
    public ASTExprNode m_lvalue;

    public ASTSpaceshipExprNode(ASTExprNode lCompareExpr, ASTExprNode rCompareExpr) {
        m_rvalue = rCompareExpr;
        m_lvalue = lCompareExpr;
    }

    @Override
    public int eval() {
        //lhs und rhs operator
        int lvalue = m_lvalue.eval();
        int rvalue = m_rvalue.eval();
        if(lvalue > rvalue) {
            //1, when lhs>rhs
            return 1;
        }else if(lvalue < rvalue){
            //-1, when lhs<rhs
            return -1;
        }
        //0, when lhs==rhs
        return 0;
    }

    @Override
    public void print(OutputStreamWriter outStream, String indent) throws Exception {
        outStream.write(indent);
        outStream.write("SPACESHIPExpr ");
        outStream.write("\n");
        m_lvalue.print(outStream, indent +" ");
        outStream.write(indent + " ");
        outStream.write("SPACESHIP");
        outStream.write("\n");
        m_rvalue.print(outStream, indent + " ");
    }

    @Override
    public InstrIntf codegen(CompileEnvIntf compileEnv) {
        // codegen der beiden Seiten die miteinander verglichen werden sollen
        compiler.InstrIntf lexprInstr = m_lvalue.codegen(compileEnv);
        compiler.InstrIntf rexprInstr = m_rvalue.codegen(compileEnv);

        //Init Jumpblöcke
        compiler.InstrBlock greaterBlock = compileEnv.createBlock("GREATER");
        compiler.InstrBlock lessOrEquBlock = compileEnv.createBlock("LESSOREQUAL");
        compiler.InstrBlock lesserBlock = compileEnv.createBlock("LESSER");
        compiler.InstrBlock equalBlock = compileEnv.createBlock("EQUAL");
        compiler.InstrBlock exitBlock = compileEnv.createBlock("EXIT");

        compiler.InstrIntf jmpExit = new compiler.instr.InstrJmp(exitBlock);

        //Compare, if l>r
        compiler.InstrIntf compareGreater = new InstrComp(lexprInstr, rexprInstr, TokenIntf.Type.GREATER);
        compiler.InstrIntf jmpPositive = new InstrCondJump(compareGreater, greaterBlock, lessOrEquBlock);
        //compare, if l<r
        compiler.InstrIntf compareLess = new InstrComp(lexprInstr, rexprInstr, TokenIntf.Type.LESS);
        compiler.InstrIntf jmpNegative = new InstrCondJump(compareLess, lesserBlock, equalBlock);

        //Schritt 1: linke und rechte Seite Codegenerieren
        compileEnv.addInstr(lexprInstr);
        compileEnv.addInstr(rexprInstr);

        //Schritt 2: Vergleich ob l>r
        compileEnv.addInstr(compareGreater);
        compileEnv.addInstr(jmpPositive);

        //Schritt 3: Wenn gößer dann 1 zurückgeben
        compileEnv.setCurrentBlock(greaterBlock);
        greaterBlock.addInstr(new InstrIntegerLiteral("1"));

        //compiler.InstrIntf resultExpr = greaterLiteral.codegen(compileEnv);
        //TODO: hier muss der Wert zurückgegeben werden irgendwie
        compileEnv.addInstr(jmpExit);

        //Schritt 4: Vergleich ob l<r, wenn true dann in LesserBlock springen ansonsten in equalBlock springen
        compileEnv.setCurrentBlock(lessOrEquBlock);
        compileEnv.addInstr(compareLess);
        compileEnv.addInstr(jmpNegative);

        //SChritt 5: Wenn kleiner dann -1 zurückgeben
        compileEnv.setCurrentBlock(lesserBlock);
        lesserBlock.addInstr(new InstrIntegerLiteral("-1"));
        ASTIntegerLiteralNode("-1").codegen(compileEnv);
//        lesserLiteral.codegen(compileEnv);
        compileEnv.addInstr(jmpExit);

        //Schritt 6: beide Werte gleich, dann 0 zurückgeben
        compileEnv.setCurrentBlock(equalBlock);
        equalBlock.addInstr(new InstrIntegerLiteral("0"));
        //equalLiteral.codegen(compileEnv);
        compileEnv.addInstr(jmpExit);

        //exit
        compileEnv.setCurrentBlock(exitBlock);

        return lexprInstr;
    }

}
