/** 
 * 
 * ?? sequencePath:= pathElement '/' pathElement 
 * ?? alternativePath:= pathElement '|' pathElement 
 * ?? nestedPath := '(' pathElement ')'
 * ?? pathElement := predicatePath | inversePredicatePath | sequencePath | alternativePath | nestedPath
 * 
 * Examples
 * 
 * { $this :volumeFlow ?A }
 * { $this ^rdf:subject [  rdf:predicate  def:massFlow ;  rdf:object ?A  ] }
 * { <<$this def:massFlow ?A >> a rdf:Statement}
 * <<$this def:massFlow ?A >> 
 * { [ rdf:subject   ?this ; rdf:predicate  def:massFlow ;  rdf:object  ?value ] a rdf:Statement }
 * $this
 * { <<$this :density ?A >> a :Attribute }
 * { :Attribute ^a <<$this :density ?A >>  }
 * { $this ^:hasProductBatteryLimit [:massThroughput ?A] }
 * { <<$this :appearsOn  :Calc2Graph1 >> a :Location ; :long  ?A  } 
 * { <<$this :appearsOn  [ rdfs:label  "Calc2Graph1"] >> a :Location ; :long  ?A  } 
 * 
*/	

grammar PathPattern;

graphPattern  :  source | '{' triples '}' ;         

triples : triple ( '.' triple )* ;
triple : source  predicateObjects ;
predicateObjects : predicateObject ( ';' predicateObject )* ;
predicateObject : predicatePattern target  ;

source : '$this' | var | iriRef | reification | bnode  ;
target : '$this' | var | iriRef | reification | bnode | literal ;
reification : '<<' source  predicate target '>>' ; // ?R rdf:subject ?this ;  rdf:predicate  def:massFlow ;   rdf:object  ?A  ;
bnode : '[' predicateObjects ']' ;
var : '?' VARNAME ;
reificate : iriRef ;
literal : LITERAL ;

predicatePattern : predicatePath | inversePredicatePath ;
predicatePath :  predicate ;
inversePredicatePath :  '^' predicate ;
predicate : iriRef | 'a' ;
iriRef  : IRI_REF  | prefixedName   ;
prefixedName : PNAME_LN  | PNAME_NS ;   

// LEXER RULES
LITERAL:  '"' (~('"' | '\\' | '\r' | '\n') | '\\' ('"' | '\\'))* '"';
IRI_REF
    : '<' ( ~('<' | '>' | '"' | '{' | '}' | '|' | '^' | '\\' | '`') | (PN_CHARS))* '>' 
    ;
       
PNAME_NS : PN_PREFIX? ':'  ;

PNAME_LN : PNAME_NS PN_LOCAL  ;
    
VARNAME : [a-zA-Z]+ ;		// match upper-case identifiers for now

//IRI : [a-z]+ ; 		// match lower-case identifiers for now

fragment
PN_CHARS_U
    : PN_CHARS_BASE | '_' 
    ;
fragment   
PN_CHARS
    : PN_CHARS_U
    | '-'
    | DIGIT
    ;
PN_PREFIX
    : PN_CHARS_BASE ((PN_CHARS|'.')* PN_CHARS)?
    ;

PN_LOCAL
    : ( PN_CHARS_U | DIGIT ) ((PN_CHARS|'.')* PN_CHARS)?
    ;
fragment
PN_CHARS_BASE
    : 'A'..'Z'
    | 'a'..'z'
    | '\u00C0'..'\u00D6'
    | '\u00D8'..'\u00F6'
    | '\u00F8'..'\u02FF'
    | '\u0370'..'\u037D'
    | '\u037F'..'\u1FFF'
    | '\u200C'..'\u200D'
    | '\u2070'..'\u218F'
    | '\u2C00'..'\u2FEF'
    | '\u3001'..'\uD7FF'
    | '\uF900'..'\uFDCF'
    | '\uFDF0'..'\uFFFD'
    ;

fragment
DIGIT
    : '0'..'9'
    ;
WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines
