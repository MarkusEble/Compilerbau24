grammar language;

// rules

// minimal expression 
start: questionMarkExpr EOF;


// start symbol
expr: questionMarkExpr;

// expressions
questionMarkExpr: andOrExpr (QUESTIONMARK questionMarkExpr DOUBLECOLON questionMarkExpr)?;

andOrExpr: cmpExpr (ANDOROP cmpExpr)*;

cmpExpr: shiftExpr ((GREATER|EQUAL|LESS) shiftExpr)*; 

shiftExpr: bitAndOrExpr (SHIFTOP bitAndOrExpr)*;

bitAndOrExpr: sumExpr ((BITAND | BITOR) sumExpr)*;

sumExpr: mulDivExpr (SUMOP  mulDivExpr)*;

mulDivExpr: unaryExpr (MULDIVOP unaryExpr)*;

unaryExpr: (INVERTER)? dashExpr;

dashExpr: arrowExpr (DASH arrowExpr)*;

arrowExpr: parantheseExpr (ARROW parantheseExpr)*;

parantheseExpr: NUMBER #Number | varExpr #Variable | LPAREN questionMarkExpr RPAREN #Paranthese;

varExpr: IDENT;

// tokens
NUMBER: [0-9]+;
IDENT: [a-z]+;

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
SHIFTOP: SHIFTLEFT|SHIFTRIGHT;
SHIFTLEFT: '<<';
SHIFTRIGHT: '>>';

// bitAndOrExpr tokens
BITOR: '|';
BITAND: '&';

// sumExpr tokens
SUMOP: PLUS|MINUS;
PLUS: '+';
MINUS: '-';

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
