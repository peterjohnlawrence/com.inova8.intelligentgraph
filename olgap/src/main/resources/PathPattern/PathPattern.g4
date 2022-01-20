/** 
 * cd C:\Apps\Antlr\PathPattern
 * c:\apps\antlr\antlr4 PathPattern.g4
 * javac PathPattern*.java
 * // test lexer
 * c:\apps\antlr\grun PathPattern tokens -tokens    
 * //test graphPattern parser
 * c:\apps\antlr\grun PathPattern  pathPattern  -gui  
 * * //test predicatePattern parser
 *  c:\apps\antlr\grun PathPattern  pathPattern  -gui  
 * 
 * .... teststring
 * ctrl-Z
 * 
 * SPARQL Property Path Syntax
 * 
 * [88] Path	 ::=	PathAlternative
 * [89] PathAlternative	 ::=	PathSequence ( '|' PathSequence )*
 * [90] PathSequence	 ::=	PathEltOrInverse ( '/' PathEltOrInverse | '^' PathElt )*
 * [91] PathElt	 ::=	PathPrimary PathMod?
 * [92] PathEltOrInverse	 ::=	PathElt | '^' PathElt
 * [93] PathMod	 ::=	( '*' | '?' | '+' | '{' ( Integer ( ',' ( '}' | Integer '}' ) | '}' ) ) )
 * [94] PathPrimary	  ::=  	iri | 'a' | '!' PathNegatedPropertySet | '(' Path ')'
 * [95] PathNegatedPropertySet	  ::=  	PathOneInPropertySet | '(' ( PathOneInPropertySet ( '|' PathOneInPropertySet )* )? ')'
 * [96] PathOneInPropertySet	  ::=  	iri | 'a' | '^' ( iri | 'a' )
 * [101] BlankNodePropertyListPath	  ::=  	'[' PropertyListPathNotEmpty ']'
 * [136] iri	  ::=  	IRIREF | PrefixedName
 * [137] PrefixedName	  ::=  	PNAME_LN | PNAME_NS
 * 
 * 
 * pathPattern Examples
 * 
 * Simple pathPattern Examples
 * 
 * :volumeFlow
 * local:volumeFlow
 * <http://local#volumeFlow>
 * ^:volumeFlow
 * :Attribute@:volumeFlow
 * ^:Attribute@:volumeFlow
 * ^:hasProductBatteryLimit/:massThroughput
 * 
 * Filtered pathPattern Examples 
 * 
 * :volumeFlow [ gt "35" }
 * :Location@:appearsOn#/:lat
 * :Location@:appearsOn[ rdfs:label "Calc2Graph1" ]#/:lat
 * :Location@:appearsOn[ eq :eastman3d ]#/:lat
 * :Location@:appearsOn#[ :location.Map :Calc2Graph1 ]/:lat
 * :Location@:appearsOn[ eq [ rdfs:label "Calc2Graph1"] ]#/:lat
 * :Location@:appearsOn[ eq [ rdfs:label "Calc2Graph1"] ]#/^:lat/:long/^:left/:right
 * :volumeFlow [ gt "35" ; rdfs:label "Calc2Graph1" ; eq [ rdfs:label "Calc2Graph1"] , :Calc2Graph1 ,"Calc2Graph1" ]
 * 
 * Not yet supported
 * 
 * :Location@:appearsOn#{ :location.Map  [rdfs:label "Calc2Graph1" ] }/:lat
*/	

grammar				PathPattern;
@header {
package com.inova8.pathql.pathPattern;
}
// PARSER RULES
queryString 		:	pathPattern queryOptions? EOF ;
queryOptions 		:	( queryOption )+;
queryOption			:	KEY '=' literal ('^^' type )?;
type 				:	qname;
pathPattern 		:	binding ('/'|'>') pathPatterns  #boundPattern
 					|	binding  #matchOnlyPattern
 					|	pathPatterns  #pathOnlyPattern;
binding 			:	factFilterPattern  ;
pathPatterns 		:	pathEltOrInverse cardinality?  #Path  
					|	pathPatterns '|'  pathPatterns  #PathAlternative  
					|	pathPatterns ('/'|'>')  pathPatterns  #PathSequence
					|	negation? '(' pathPatterns ')'  cardinality? #PathParentheses;
					
//pathNegatedPropertySet	  :  	PathOneInPropertySet | '(' ( PathOneInPropertySet ( '|' PathOneInPropertySet )* )? ')' ;
//pathOneInPropertySet	  : 	iri | 'a' | '^' ( iri | 'a' );

cardinality 		:	'{'  INTEGER (',' ( INTEGER )? )?  '}'  ;
negation			:	'!';
pathEltOrInverse 	:	negation? INVERSE? predicate  ;
predicate 			:	( reifiedPredicate | predicateRef | rdfType | anyPredicate ) factFilterPattern? ;
anyPredicate		:	ANYPREDICATE ;
reifiedPredicate 	:	iriRef? REIFIER predicateRef  factFilterPattern?  dereifier? ;
predicateRef 		:	IRI_REF  | rdfType  |  qname | pname_ns ;
iriRef  			:	IRI_REF |  qname | pname_ns ;  
dereifier 			:	DEREIFIER ;
factFilterPattern 	:	'['  propertyListNotEmpty   ']';
propertyListNotEmpty:	verbObjectList ( ';' ( verbObjectList )? )* ;  
verbObjectList 		:	verb objectList;
verb 				:	operator | pathEltOrInverse ;
objectList 			:	object ( ',' object )*;
object 				:	iriRef  | literal | factFilterPattern | BINDVARIABLE ;
qname 				:	PNAME_NS PN_LOCAL; 
pname_ns			:	PNAME_NS ;   
literal 			:	(DQLITERAL | SQLITERAL) ('^^' (IRI_REF |  qname) )? ;  
operator 			:	OPERATOR ;
rdfType 			:	RDFTYPE ;

// LEXER RULES
KEY 				:	'&' [a-zA-Z]+ ;  
INTEGER 			:	DIGIT+ ; 
BINDVARIABLE 		:	'%' DIGIT+ ;
fragment
DIGIT				:	[0-9] ;  
INVERSE 			:	'^';
REIFIER 			:	'@';
DEREIFIER 			:	'#';
RDFTYPE 			:	'a';
ANYPREDICATE 		:	'*' ;
OPERATOR 			:	'lt'|'gt'|'le'|'ge'|'eq'|'ne'|'like'|'query'|'property';
DQLITERAL			:	'"' (~('"' | '\\' | '\r' | '\n') | '\\' ('"' | '\\'))* '"';
SQLITERAL			:	'\'' (~('\'' | '\\' | '\r' | '\n') | '\\' ('\'' | '\\'))* '\'';
IRI_REF				:	'<' ( ~('<' | '>' | '"' | '{' | '}' | '|' | '^' | '\\' | '`') | (PN_CHARS))* '>' ;      
PNAME_NS 			:	PN_PREFIX? (':'|'~')  ;   
VARNAME 			:	'?' [a-zA-Z]+ ;		
fragment
PN_CHARS_U			:	PN_CHARS_BASE | '_'  ;
fragment   
PN_CHARS			:	PN_CHARS_U
    				|	'-'
   					|	DIGIT  ;
fragment
PN_PREFIX			:	PN_CHARS_BASE ((PN_CHARS|'.')* PN_CHARS)? ;
PN_LOCAL			:	( PN_CHARS_U | DIGIT ) ((PN_CHARS|'.')* PN_CHARS)? ;
fragment
PN_CHARS_BASE		:	'A'..'Z'
					|	'a'..'z'
					|	'\u00C0'..'\u00D6'
					|	'\u00D8'..'\u00F6'
					|	'\u00F8'..'\u02FF'
					|	'\u0370'..'\u037D'
					|	'\u037F'..'\u1FFF'
					|	'\u200C'..'\u200D'
					|	'\u2070'..'\u218F'
					|	'\u2C00'..'\u2FEF'
					|	'\u3001'..'\uD7FF'
					|	'\uF900'..'\uFDCF'
					|	'\uFDF0'..'\uFFFD' ;
WS 					:	[ \t\r\n]+ -> skip ; 
