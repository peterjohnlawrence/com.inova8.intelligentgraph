// Generated from PathPattern.g4 by ANTLR 4.4
package PathPattern;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link PathPatternParser}.
 */
public interface PathPatternListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#negation}.
	 * @param ctx the parse tree
	 */
	void enterNegation(@NotNull PathPatternParser.NegationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#negation}.
	 * @param ctx the parse tree
	 */
	void exitNegation(@NotNull PathPatternParser.NegationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#verbObjectList}.
	 * @param ctx the parse tree
	 */
	void enterVerbObjectList(@NotNull PathPatternParser.VerbObjectListContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#verbObjectList}.
	 * @param ctx the parse tree
	 */
	void exitVerbObjectList(@NotNull PathPatternParser.VerbObjectListContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#anyPredicate}.
	 * @param ctx the parse tree
	 */
	void enterAnyPredicate(@NotNull PathPatternParser.AnyPredicateContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#anyPredicate}.
	 * @param ctx the parse tree
	 */
	void exitAnyPredicate(@NotNull PathPatternParser.AnyPredicateContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#binding}.
	 * @param ctx the parse tree
	 */
	void enterBinding(@NotNull PathPatternParser.BindingContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#binding}.
	 * @param ctx the parse tree
	 */
	void exitBinding(@NotNull PathPatternParser.BindingContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#type}.
	 * @param ctx the parse tree
	 */
	void enterType(@NotNull PathPatternParser.TypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#type}.
	 * @param ctx the parse tree
	 */
	void exitType(@NotNull PathPatternParser.TypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#dereifier}.
	 * @param ctx the parse tree
	 */
	void enterDereifier(@NotNull PathPatternParser.DereifierContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#dereifier}.
	 * @param ctx the parse tree
	 */
	void exitDereifier(@NotNull PathPatternParser.DereifierContext ctx);
	/**
	 * Enter a parse tree produced by the {@code pathOnlyPattern}
	 * labeled alternative in {@link PathPatternParser#pathPattern}.
	 * @param ctx the parse tree
	 */
	void enterPathOnlyPattern(@NotNull PathPatternParser.PathOnlyPatternContext ctx);
	/**
	 * Exit a parse tree produced by the {@code pathOnlyPattern}
	 * labeled alternative in {@link PathPatternParser#pathPattern}.
	 * @param ctx the parse tree
	 */
	void exitPathOnlyPattern(@NotNull PathPatternParser.PathOnlyPatternContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#reifiedPredicate}.
	 * @param ctx the parse tree
	 */
	void enterReifiedPredicate(@NotNull PathPatternParser.ReifiedPredicateContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#reifiedPredicate}.
	 * @param ctx the parse tree
	 */
	void exitReifiedPredicate(@NotNull PathPatternParser.ReifiedPredicateContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#operator}.
	 * @param ctx the parse tree
	 */
	void enterOperator(@NotNull PathPatternParser.OperatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#operator}.
	 * @param ctx the parse tree
	 */
	void exitOperator(@NotNull PathPatternParser.OperatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#literal}.
	 * @param ctx the parse tree
	 */
	void enterLiteral(@NotNull PathPatternParser.LiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#literal}.
	 * @param ctx the parse tree
	 */
	void exitLiteral(@NotNull PathPatternParser.LiteralContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#predicate}.
	 * @param ctx the parse tree
	 */
	void enterPredicate(@NotNull PathPatternParser.PredicateContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#predicate}.
	 * @param ctx the parse tree
	 */
	void exitPredicate(@NotNull PathPatternParser.PredicateContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#queryOption}.
	 * @param ctx the parse tree
	 */
	void enterQueryOption(@NotNull PathPatternParser.QueryOptionContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#queryOption}.
	 * @param ctx the parse tree
	 */
	void exitQueryOption(@NotNull PathPatternParser.QueryOptionContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#qname}.
	 * @param ctx the parse tree
	 */
	void enterQname(@NotNull PathPatternParser.QnameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#qname}.
	 * @param ctx the parse tree
	 */
	void exitQname(@NotNull PathPatternParser.QnameContext ctx);
	/**
	 * Enter a parse tree produced by the {@code boundPattern}
	 * labeled alternative in {@link PathPatternParser#pathPattern}.
	 * @param ctx the parse tree
	 */
	void enterBoundPattern(@NotNull PathPatternParser.BoundPatternContext ctx);
	/**
	 * Exit a parse tree produced by the {@code boundPattern}
	 * labeled alternative in {@link PathPatternParser#pathPattern}.
	 * @param ctx the parse tree
	 */
	void exitBoundPattern(@NotNull PathPatternParser.BoundPatternContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PathParentheses}
	 * labeled alternative in {@link PathPatternParser#pathPatterns}.
	 * @param ctx the parse tree
	 */
	void enterPathParentheses(@NotNull PathPatternParser.PathParenthesesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PathParentheses}
	 * labeled alternative in {@link PathPatternParser#pathPatterns}.
	 * @param ctx the parse tree
	 */
	void exitPathParentheses(@NotNull PathPatternParser.PathParenthesesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#predicateRef}.
	 * @param ctx the parse tree
	 */
	void enterPredicateRef(@NotNull PathPatternParser.PredicateRefContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#predicateRef}.
	 * @param ctx the parse tree
	 */
	void exitPredicateRef(@NotNull PathPatternParser.PredicateRefContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Path}
	 * labeled alternative in {@link PathPatternParser#pathPatterns}.
	 * @param ctx the parse tree
	 */
	void enterPath(@NotNull PathPatternParser.PathContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Path}
	 * labeled alternative in {@link PathPatternParser#pathPatterns}.
	 * @param ctx the parse tree
	 */
	void exitPath(@NotNull PathPatternParser.PathContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#pathEltOrInverse}.
	 * @param ctx the parse tree
	 */
	void enterPathEltOrInverse(@NotNull PathPatternParser.PathEltOrInverseContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#pathEltOrInverse}.
	 * @param ctx the parse tree
	 */
	void exitPathEltOrInverse(@NotNull PathPatternParser.PathEltOrInverseContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#queryOptions}.
	 * @param ctx the parse tree
	 */
	void enterQueryOptions(@NotNull PathPatternParser.QueryOptionsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#queryOptions}.
	 * @param ctx the parse tree
	 */
	void exitQueryOptions(@NotNull PathPatternParser.QueryOptionsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#verb}.
	 * @param ctx the parse tree
	 */
	void enterVerb(@NotNull PathPatternParser.VerbContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#verb}.
	 * @param ctx the parse tree
	 */
	void exitVerb(@NotNull PathPatternParser.VerbContext ctx);
	/**
	 * Enter a parse tree produced by the {@code matchOnlyPattern}
	 * labeled alternative in {@link PathPatternParser#pathPattern}.
	 * @param ctx the parse tree
	 */
	void enterMatchOnlyPattern(@NotNull PathPatternParser.MatchOnlyPatternContext ctx);
	/**
	 * Exit a parse tree produced by the {@code matchOnlyPattern}
	 * labeled alternative in {@link PathPatternParser#pathPattern}.
	 * @param ctx the parse tree
	 */
	void exitMatchOnlyPattern(@NotNull PathPatternParser.MatchOnlyPatternContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#queryString}.
	 * @param ctx the parse tree
	 */
	void enterQueryString(@NotNull PathPatternParser.QueryStringContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#queryString}.
	 * @param ctx the parse tree
	 */
	void exitQueryString(@NotNull PathPatternParser.QueryStringContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PathSequence}
	 * labeled alternative in {@link PathPatternParser#pathPatterns}.
	 * @param ctx the parse tree
	 */
	void enterPathSequence(@NotNull PathPatternParser.PathSequenceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PathSequence}
	 * labeled alternative in {@link PathPatternParser#pathPatterns}.
	 * @param ctx the parse tree
	 */
	void exitPathSequence(@NotNull PathPatternParser.PathSequenceContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#cardinality}.
	 * @param ctx the parse tree
	 */
	void enterCardinality(@NotNull PathPatternParser.CardinalityContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#cardinality}.
	 * @param ctx the parse tree
	 */
	void exitCardinality(@NotNull PathPatternParser.CardinalityContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#objectList}.
	 * @param ctx the parse tree
	 */
	void enterObjectList(@NotNull PathPatternParser.ObjectListContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#objectList}.
	 * @param ctx the parse tree
	 */
	void exitObjectList(@NotNull PathPatternParser.ObjectListContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#pname_ns}.
	 * @param ctx the parse tree
	 */
	void enterPname_ns(@NotNull PathPatternParser.Pname_nsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#pname_ns}.
	 * @param ctx the parse tree
	 */
	void exitPname_ns(@NotNull PathPatternParser.Pname_nsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#iriRef}.
	 * @param ctx the parse tree
	 */
	void enterIriRef(@NotNull PathPatternParser.IriRefContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#iriRef}.
	 * @param ctx the parse tree
	 */
	void exitIriRef(@NotNull PathPatternParser.IriRefContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#propertyListNotEmpty}.
	 * @param ctx the parse tree
	 */
	void enterPropertyListNotEmpty(@NotNull PathPatternParser.PropertyListNotEmptyContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#propertyListNotEmpty}.
	 * @param ctx the parse tree
	 */
	void exitPropertyListNotEmpty(@NotNull PathPatternParser.PropertyListNotEmptyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code PathAlternative}
	 * labeled alternative in {@link PathPatternParser#pathPatterns}.
	 * @param ctx the parse tree
	 */
	void enterPathAlternative(@NotNull PathPatternParser.PathAlternativeContext ctx);
	/**
	 * Exit a parse tree produced by the {@code PathAlternative}
	 * labeled alternative in {@link PathPatternParser#pathPatterns}.
	 * @param ctx the parse tree
	 */
	void exitPathAlternative(@NotNull PathPatternParser.PathAlternativeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#factFilterPattern}.
	 * @param ctx the parse tree
	 */
	void enterFactFilterPattern(@NotNull PathPatternParser.FactFilterPatternContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#factFilterPattern}.
	 * @param ctx the parse tree
	 */
	void exitFactFilterPattern(@NotNull PathPatternParser.FactFilterPatternContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#rdfType}.
	 * @param ctx the parse tree
	 */
	void enterRdfType(@NotNull PathPatternParser.RdfTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#rdfType}.
	 * @param ctx the parse tree
	 */
	void exitRdfType(@NotNull PathPatternParser.RdfTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#object}.
	 * @param ctx the parse tree
	 */
	void enterObject(@NotNull PathPatternParser.ObjectContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#object}.
	 * @param ctx the parse tree
	 */
	void exitObject(@NotNull PathPatternParser.ObjectContext ctx);
}