// Generated from PathPattern.g4 by ANTLR 4.9.3
package PathPattern;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link PathPatternParser}.
 */
public interface PathPatternListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#queryString}.
	 * @param ctx the parse tree
	 */
	void enterQueryString(PathPatternParser.QueryStringContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#queryString}.
	 * @param ctx the parse tree
	 */
	void exitQueryString(PathPatternParser.QueryStringContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#queryOptions}.
	 * @param ctx the parse tree
	 */
	void enterQueryOptions(PathPatternParser.QueryOptionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#queryOptions}.
	 * @param ctx the parse tree
	 */
	void exitQueryOptions(PathPatternParser.QueryOptionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#queryOption}.
	 * @param ctx the parse tree
	 */
	void enterQueryOption(PathPatternParser.QueryOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#queryOption}.
	 * @param ctx the parse tree
	 */
	void exitQueryOption(PathPatternParser.QueryOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(PathPatternParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(PathPatternParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code boundPattern}
	 * labeled alternative in {@link PathPatternParser#pathPattern}.
	 * @param ctx the parse tree
	 */
	void enterBoundPattern(PathPatternParser.BoundPatternContext ctx);
	/**
	 * Exit a parse tree produced by the {@code boundPattern}
	 * labeled alternative in {@link PathPatternParser#pathPattern}.
	 * @param ctx the parse tree
	 */
	void exitBoundPattern(PathPatternParser.BoundPatternContext ctx);
	/**
	 * Enter a parse tree produced by the {@code matchOnlyPattern}
	 * labeled alternative in {@link PathPatternParser#pathPattern}.
	 * @param ctx the parse tree
	 */
	void enterMatchOnlyPattern(PathPatternParser.MatchOnlyPatternContext ctx);
	/**
	 * Exit a parse tree produced by the {@code matchOnlyPattern}
	 * labeled alternative in {@link PathPatternParser#pathPattern}.
	 * @param ctx the parse tree
	 */
	void exitMatchOnlyPattern(PathPatternParser.MatchOnlyPatternContext ctx);
	/**
	 * Enter a parse tree produced by the {@code pathOnlyPattern}
	 * labeled alternative in {@link PathPatternParser#pathPattern}.
	 * @param ctx the parse tree
	 */
	void enterPathOnlyPattern(PathPatternParser.PathOnlyPatternContext ctx);
	/**
	 * Exit a parse tree produced by the {@code pathOnlyPattern}
	 * labeled alternative in {@link PathPatternParser#pathPattern}.
	 * @param ctx the parse tree
	 */
	void exitPathOnlyPattern(PathPatternParser.PathOnlyPatternContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#binding}.
	 * @param ctx the parse tree
	 */
	void enterBinding(PathPatternParser.BindingContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#binding}.
	 * @param ctx the parse tree
	 */
	void exitBinding(PathPatternParser.BindingContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Path}
	 * labeled alternative in {@link PathPatternParser#pathPatterns}.
	 * @param ctx the parse tree
	 */
	void enterPath(PathPatternParser.PathContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Path}
	 * labeled alternative in {@link PathPatternParser#pathPatterns}.
	 * @param ctx the parse tree
	 */
	void exitPath(PathPatternParser.PathContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PathAlternative}
	 * labeled alternative in {@link PathPatternParser#pathPatterns}.
	 * @param ctx the parse tree
	 */
	void enterPathAlternative(PathPatternParser.PathAlternativeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PathAlternative}
	 * labeled alternative in {@link PathPatternParser#pathPatterns}.
	 * @param ctx the parse tree
	 */
	void exitPathAlternative(PathPatternParser.PathAlternativeContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PathParentheses}
	 * labeled alternative in {@link PathPatternParser#pathPatterns}.
	 * @param ctx the parse tree
	 */
	void enterPathParentheses(PathPatternParser.PathParenthesesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PathParentheses}
	 * labeled alternative in {@link PathPatternParser#pathPatterns}.
	 * @param ctx the parse tree
	 */
	void exitPathParentheses(PathPatternParser.PathParenthesesContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PathSequence}
	 * labeled alternative in {@link PathPatternParser#pathPatterns}.
	 * @param ctx the parse tree
	 */
	void enterPathSequence(PathPatternParser.PathSequenceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PathSequence}
	 * labeled alternative in {@link PathPatternParser#pathPatterns}.
	 * @param ctx the parse tree
	 */
	void exitPathSequence(PathPatternParser.PathSequenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#cardinality}.
	 * @param ctx the parse tree
	 */
	void enterCardinality(PathPatternParser.CardinalityContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#cardinality}.
	 * @param ctx the parse tree
	 */
	void exitCardinality(PathPatternParser.CardinalityContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#negation}.
	 * @param ctx the parse tree
	 */
	void enterNegation(PathPatternParser.NegationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#negation}.
	 * @param ctx the parse tree
	 */
	void exitNegation(PathPatternParser.NegationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#pathEltOrInverse}.
	 * @param ctx the parse tree
	 */
	void enterPathEltOrInverse(PathPatternParser.PathEltOrInverseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#pathEltOrInverse}.
	 * @param ctx the parse tree
	 */
	void exitPathEltOrInverse(PathPatternParser.PathEltOrInverseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterPredicate(PathPatternParser.PredicateContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitPredicate(PathPatternParser.PredicateContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#anyPredicate}.
	 * @param ctx the parse tree
	 */
	void enterAnyPredicate(PathPatternParser.AnyPredicateContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#anyPredicate}.
	 * @param ctx the parse tree
	 */
	void exitAnyPredicate(PathPatternParser.AnyPredicateContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#reifiedPredicate}.
	 * @param ctx the parse tree
	 */
	void enterReifiedPredicate(PathPatternParser.ReifiedPredicateContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#reifiedPredicate}.
	 * @param ctx the parse tree
	 */
	void exitReifiedPredicate(PathPatternParser.ReifiedPredicateContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#predicateRef}.
	 * @param ctx the parse tree
	 */
	void enterPredicateRef(PathPatternParser.PredicateRefContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#predicateRef}.
	 * @param ctx the parse tree
	 */
	void exitPredicateRef(PathPatternParser.PredicateRefContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#iriRef}.
	 * @param ctx the parse tree
	 */
	void enterIriRef(PathPatternParser.IriRefContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#iriRef}.
	 * @param ctx the parse tree
	 */
	void exitIriRef(PathPatternParser.IriRefContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#dereifier}.
	 * @param ctx the parse tree
	 */
	void enterDereifier(PathPatternParser.DereifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#dereifier}.
	 * @param ctx the parse tree
	 */
	void exitDereifier(PathPatternParser.DereifierContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#factFilterPattern}.
	 * @param ctx the parse tree
	 */
	void enterFactFilterPattern(PathPatternParser.FactFilterPatternContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#factFilterPattern}.
	 * @param ctx the parse tree
	 */
	void exitFactFilterPattern(PathPatternParser.FactFilterPatternContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#propertyListNotEmpty}.
	 * @param ctx the parse tree
	 */
	void enterPropertyListNotEmpty(PathPatternParser.PropertyListNotEmptyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#propertyListNotEmpty}.
	 * @param ctx the parse tree
	 */
	void exitPropertyListNotEmpty(PathPatternParser.PropertyListNotEmptyContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#verbObjectList}.
	 * @param ctx the parse tree
	 */
	void enterVerbObjectList(PathPatternParser.VerbObjectListContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#verbObjectList}.
	 * @param ctx the parse tree
	 */
	void exitVerbObjectList(PathPatternParser.VerbObjectListContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#verb}.
	 * @param ctx the parse tree
	 */
	void enterVerb(PathPatternParser.VerbContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#verb}.
	 * @param ctx the parse tree
	 */
	void exitVerb(PathPatternParser.VerbContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#objectList}.
	 * @param ctx the parse tree
	 */
	void enterObjectList(PathPatternParser.ObjectListContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#objectList}.
	 * @param ctx the parse tree
	 */
	void exitObjectList(PathPatternParser.ObjectListContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#object}.
	 * @param ctx the parse tree
	 */
	void enterObject(PathPatternParser.ObjectContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#object}.
	 * @param ctx the parse tree
	 */
	void exitObject(PathPatternParser.ObjectContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#qname}.
	 * @param ctx the parse tree
	 */
	void enterQname(PathPatternParser.QnameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#qname}.
	 * @param ctx the parse tree
	 */
	void exitQname(PathPatternParser.QnameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#pname_ns}.
	 * @param ctx the parse tree
	 */
	void enterPname_ns(PathPatternParser.Pname_nsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#pname_ns}.
	 * @param ctx the parse tree
	 */
	void exitPname_ns(PathPatternParser.Pname_nsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(PathPatternParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(PathPatternParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#operator}.
	 * @param ctx the parse tree
	 */
	void enterOperator(PathPatternParser.OperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#operator}.
	 * @param ctx the parse tree
	 */
	void exitOperator(PathPatternParser.OperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#rdfType}.
	 * @param ctx the parse tree
	 */
	void enterRdfType(PathPatternParser.RdfTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#rdfType}.
	 * @param ctx the parse tree
	 */
	void exitRdfType(PathPatternParser.RdfTypeContext ctx);
}