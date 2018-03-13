grammar Notation;

preposition : Text ;

operation : Conjunction
          | Disjunction
          | Exclusive_Disjuction
          | Conditional
          | Biconditional ;

not : Negation ;

notation : notationPart operation notationPart ;

notationPart : not '(' notation ')'
             | '(' notation ')'
             | not preposition
             | preposition
             ;

Text                 : Letter+;
fragment Upper       :  'A' .. 'Z' ;
fragment Lower       :  'a' .. 'z';
Letter               :  Upper | Lower;
Negation             :  '!' | '¬' | '~' ;
Conjunction          :  '^' | '∧' | '⋅' | '&' ;
Disjunction          :  'v' | '+' | '|' ;
Exclusive_Disjuction : '⊕' | '=/' ;
Conditional          : '->' ;
Biconditional        : '<->' ;

WS  :  [ \t\r\n\u000C]+ -> skip
    ;