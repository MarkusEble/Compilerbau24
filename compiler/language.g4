grammar language;

// rules

// minimal expression 
start: questionMarkExpr EOF;


// start symbol
expr: questionMarkExpr;

// expressions
questionMarkExpr: andOrExpr (QUESTIONMARK questionMarkExpr DOUBLECOLON questionMarkExpr)*;

andOrExpr: cmpExpr ANDOROP cmpExpr | andOrExpr ANDOROP cmpExpr;

cmpExpr: shiftExpr (mulDivExpr (GREATER|EQUAL|LESS) mulDivExpr); 

shiftExpr: bitAndOrExpr (SHIFTOP bitAndOrExpr)*;

bitAndOrExpr: sumExpr;

sumExpr: mulDivExpr (SUMOP  mulDivExpr)*;

mulDivExpr: unaryExpr (MULDIVOP unaryExpr)*;

unaryExpr: (INVERTER)? dashExpr;

dashExpr: arrowExpr (DASH arrowExpr)*;

arrowExpr: parantheseExpr (ARROW parantheseExpr)*;

parantheseExpr: NUMBER | varExpr | LPAREN questionMarkExpr RPAREN;

// tokens
NUMBER: [0-9]+;

// questionMarkExpr tokens
QUESTIONMARK: '?';
DOUBLECOLON: ':';

// andOrExpr tokens
ANDOROP: AND|OR;
AND: '&&';
OR: '||';
// cmpExpr tokens
GREATER: '>';
EQUAL: '=';
LESS: '<';

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
MULDIVOP: MUL | DIV;
MUL: '*';
DIV: '/';

// unaryExpr tokens
INVERTER: '!';

// dashExpr tokens
DASH: '^';

// arrowExpr tokens
ARROW: '->';
// parantheseExpr tokens
LPAREN: '(';
RPAREN: ')';

// skip whitespaces
WS: [ \t\r\n]+ -> skip;
