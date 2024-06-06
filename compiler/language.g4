grammar language;

// rules

// minimal expression 
start: questionMarkExpr EOF;


// start symbol
expr: questionMarkExpr;

// expressions
questionMarkExpr: andOrExpr (QUESTIONMARK questionMarkExpr DOUBLECOLON questionMarkExpr)*;

andOrExpr: cmpExpr;

cmpExpr: shiftExpr;

shiftExpr: bitAndOrExpr (SHIFTOP bitAndOrExpr)*;

bitAndOrExpr: sumExpr;

sumExpr: mulDivExpr (SUMOP  mulDivExpr)*;

mulDivExpr: unaryExpr;

unaryExpr: (INVERTER)? dashExpr;

dashExpr: arrowExpr;

arrowExpr: parantheseExpr (ARROW parantheseExpr)*;

parantheseExpr: NUMBER;

// tokens
NUMBER: [0-9]+;

// questionMarkExpr tokens
QUESTIONMARK: '?';
DOUBLECOLON: ':';

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
ARROW: '->';
// parantheseExpr tokens

// skip whitespaces
WS: [ \t\r\n]+ -> skip;

