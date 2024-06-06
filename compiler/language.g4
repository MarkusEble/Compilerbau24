grammar language;

// rules

// minimal expression 
// expr: NUMBER (SUMOP  NUMBER)*;


// start symbol
expr: questionMarkExpr;

// expressions
questionMarkExpr: andOrExpr;

andOrExpr: cmpExpr;

cmpExpr: shiftExpr;

shiftExpr: bitAndOrExpr (SHIFTOP bitAndOrExpr)*;

bitAndOrExpr: sumExpr;

sumExpr: mulDivExpr (SUMOP  mulDivExpr)*;

mulDivExpr: unaryExpr;

unaryExpr: (INVERTER)? dashExpr;

dashExpr: arrowExpr;

arrowExpr: parantheseExpr;

parantheseExpr: NUMBER;

// tokens
NUMBER: [0-9]+;

// questionMarkExpr tokens

// andOrExpr tokens

// cmpExpr tokens

// shiftExpr tokens

// bitAndOrExpr tokens

// sumExpr tokens
SUMOP: PLUS|MINUS;
PLUS: '+';
MINUS: '-';
SHIFTOP: SHIFTLEFT|SHIFTRIGHT;
SHIFTLEFT: '<<';
SHIFTRIGHT: '>>';


// mulDivExpr tokens

// unaryExpr tokens
INVERTER: '!';

// dashExpr tokens

// arrowExpr tokens

// parantheseExpr tokens

// skip whitespaces
WS: [ \t\r\n]+ -> skip;

