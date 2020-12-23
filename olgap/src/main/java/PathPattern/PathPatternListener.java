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
	 * Enter a parse tree produced by {@link PathPatternParser#predicateObjects}.
	 * @param ctx the parse tree
	 */
	void enterPredicateObjects(@NotNull PathPatternParser.PredicateObjectsContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#predicateObjects}.
	 * @param ctx the parse tree
	 */
	void exitPredicateObjects(@NotNull PathPatternParser.PredicateObjectsContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#triples}.
	 * @param ctx the parse tree
	 */
	void enterTriples(@NotNull PathPatternParser.TriplesContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#triples}.
	 * @param ctx the parse tree
	 */
	void exitTriples(@NotNull PathPatternParser.TriplesContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#graphPattern}.
	 * @param ctx the parse tree
	 */
	void enterGraphPattern(@NotNull PathPatternParser.GraphPatternContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#graphPattern}.
	 * @param ctx the parse tree
	 */
	void exitGraphPattern(@NotNull PathPatternParser.GraphPatternContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#var}.
	 * @param ctx the parse tree
	 */
	void enterVar(@NotNull PathPatternParser.VarContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#var}.
	 * @param ctx the parse tree
	 */
	void exitVar(@NotNull PathPatternParser.VarContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#prefixedName}.
	 * @param ctx the parse tree
	 */
	void enterPrefixedName(@NotNull PathPatternParser.PrefixedNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#prefixedName}.
	 * @param ctx the parse tree
	 */
	void exitPrefixedName(@NotNull PathPatternParser.PrefixedNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#reificate}.
	 * @param ctx the parse tree
	 */
	void enterReificate(@NotNull PathPatternParser.ReificateContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#reificate}.
	 * @param ctx the parse tree
	 */
	void exitReificate(@NotNull PathPatternParser.ReificateContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#predicateObject}.
	 * @param ctx the parse tree
	 */
	void enterPredicateObject(@NotNull PathPatternParser.PredicateObjectContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#predicateObject}.
	 * @param ctx the parse tree
	 */
	void exitPredicateObject(@NotNull PathPatternParser.PredicateObjectContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#source}.
	 * @param ctx the parse tree
	 */
	void enterSource(@NotNull PathPatternParser.SourceContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#source}.
	 * @param ctx the parse tree
	 */
	void exitSource(@NotNull PathPatternParser.SourceContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#inversePredicatePath}.
	 * @param ctx the parse tree
	 */
	void enterInversePredicatePath(@NotNull PathPatternParser.InversePredicatePathContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#inversePredicatePath}.
	 * @param ctx the parse tree
	 */
	void exitInversePredicatePath(@NotNull PathPatternParser.InversePredicatePathContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#reification}.
	 * @param ctx the parse tree
	 */
	void enterReification(@NotNull PathPatternParser.ReificationContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#reification}.
	 * @param ctx the parse tree
	 */
	void exitReification(@NotNull PathPatternParser.ReificationContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#target}.
	 * @param ctx the parse tree
	 */
	void enterTarget(@NotNull PathPatternParser.TargetContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#target}.
	 * @param ctx the parse tree
	 */
	void exitTarget(@NotNull PathPatternParser.TargetContext ctx);
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
	 * Enter a parse tree produced by {@link PathPatternParser#triple}.
	 * @param ctx the parse tree
	 */
	void enterTriple(@NotNull PathPatternParser.TripleContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#triple}.
	 * @param ctx the parse tree
	 */
	void exitTriple(@NotNull PathPatternParser.TripleContext ctx);
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
	 * Enter a parse tree produced by {@link PathPatternParser#predicatePattern}.
	 * @param ctx the parse tree
	 */
	void enterPredicatePattern(@NotNull PathPatternParser.PredicatePatternContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#predicatePattern}.
	 * @param ctx the parse tree
	 */
	void exitPredicatePattern(@NotNull PathPatternParser.PredicatePatternContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#bnode}.
	 * @param ctx the parse tree
	 */
	void enterBnode(@NotNull PathPatternParser.BnodeContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#bnode}.
	 * @param ctx the parse tree
	 */
	void exitBnode(@NotNull PathPatternParser.BnodeContext ctx);
	/**
	 * Enter a parse tree produced by {@link PathPatternParser#predicatePath}.
	 * @param ctx the parse tree
	 */
	void enterPredicatePath(@NotNull PathPatternParser.PredicatePathContext ctx);
	/**
	 * Exit a parse tree produced by {@link PathPatternParser#predicatePath}.
	 * @param ctx the parse tree
	 */
	void exitPredicatePath(@NotNull PathPatternParser.PredicatePathContext ctx);
}