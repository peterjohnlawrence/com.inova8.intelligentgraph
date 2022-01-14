// Generated from PathPattern.g4 by ANTLR 4.9.3
package PathPattern;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link PathPatternParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface PathPatternVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link PathPatternParser#queryString}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQueryString(PathPatternParser.QueryStringContext ctx);
	/**
	 * Visit a parse tree produced by {@link PathPatternParser#queryOptions}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQueryOptions(PathPatternParser.QueryOptionsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PathPatternParser#queryOption}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQueryOption(PathPatternParser.QueryOptionContext ctx);
	/**
	 * Visit a parse tree produced by {@link PathPatternParser#type}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitType(PathPatternParser.TypeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code boundPattern}
	 * labeled alternative in {@link PathPatternParser#pathPattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBoundPattern(PathPatternParser.BoundPatternContext ctx);
	/**
	 * Visit a parse tree produced by the {@code matchOnlyPattern}
	 * labeled alternative in {@link PathPatternParser#pathPattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMatchOnlyPattern(PathPatternParser.MatchOnlyPatternContext ctx);
	/**
	 * Visit a parse tree produced by the {@code pathOnlyPattern}
	 * labeled alternative in {@link PathPatternParser#pathPattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPathOnlyPattern(PathPatternParser.PathOnlyPatternContext ctx);
	/**
	 * Visit a parse tree produced by {@link PathPatternParser#binding}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinding(PathPatternParser.BindingContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Path}
	 * labeled alternative in {@link PathPatternParser#pathPatterns}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPath(PathPatternParser.PathContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PathAlternative}
	 * labeled alternative in {@link PathPatternParser#pathPatterns}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPathAlternative(PathPatternParser.PathAlternativeContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PathParentheses}
	 * labeled alternative in {@link PathPatternParser#pathPatterns}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPathParentheses(PathPatternParser.PathParenthesesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code PathSequence}
	 * labeled alternative in {@link PathPatternParser#pathPatterns}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPathSequence(PathPatternParser.PathSequenceContext ctx);
	/**
	 * Visit a parse tree produced by {@link PathPatternParser#cardinality}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCardinality(PathPatternParser.CardinalityContext ctx);
	/**
	 * Visit a parse tree produced by {@link PathPatternParser#negation}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNegation(PathPatternParser.NegationContext ctx);
	/**
	 * Visit a parse tree produced by {@link PathPatternParser#pathEltOrInverse}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPathEltOrInverse(PathPatternParser.PathEltOrInverseContext ctx);
	/**
	 * Visit a parse tree produced by {@link PathPatternParser#predicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPredicate(PathPatternParser.PredicateContext ctx);
	/**
	 * Visit a parse tree produced by {@link PathPatternParser#anyPredicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnyPredicate(PathPatternParser.AnyPredicateContext ctx);
	/**
	 * Visit a parse tree produced by {@link PathPatternParser#reifiedPredicate}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReifiedPredicate(PathPatternParser.ReifiedPredicateContext ctx);
	/**
	 * Visit a parse tree produced by {@link PathPatternParser#predicateRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPredicateRef(PathPatternParser.PredicateRefContext ctx);
	/**
	 * Visit a parse tree produced by {@link PathPatternParser#iriRef}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIriRef(PathPatternParser.IriRefContext ctx);
	/**
	 * Visit a parse tree produced by {@link PathPatternParser#dereifier}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDereifier(PathPatternParser.DereifierContext ctx);
	/**
	 * Visit a parse tree produced by {@link PathPatternParser#factFilterPattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFactFilterPattern(PathPatternParser.FactFilterPatternContext ctx);
	/**
	 * Visit a parse tree produced by {@link PathPatternParser#propertyListNotEmpty}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPropertyListNotEmpty(PathPatternParser.PropertyListNotEmptyContext ctx);
	/**
	 * Visit a parse tree produced by {@link PathPatternParser#verbObjectList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVerbObjectList(PathPatternParser.VerbObjectListContext ctx);
	/**
	 * Visit a parse tree produced by {@link PathPatternParser#verb}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVerb(PathPatternParser.VerbContext ctx);
	/**
	 * Visit a parse tree produced by {@link PathPatternParser#objectList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObjectList(PathPatternParser.ObjectListContext ctx);
	/**
	 * Visit a parse tree produced by {@link PathPatternParser#object}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitObject(PathPatternParser.ObjectContext ctx);
	/**
	 * Visit a parse tree produced by {@link PathPatternParser#qname}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQname(PathPatternParser.QnameContext ctx);
	/**
	 * Visit a parse tree produced by {@link PathPatternParser#pname_ns}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPname_ns(PathPatternParser.Pname_nsContext ctx);
	/**
	 * Visit a parse tree produced by {@link PathPatternParser#literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral(PathPatternParser.LiteralContext ctx);
	/**
	 * Visit a parse tree produced by {@link PathPatternParser#operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperator(PathPatternParser.OperatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link PathPatternParser#rdfType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRdfType(PathPatternParser.RdfTypeContext ctx);
}