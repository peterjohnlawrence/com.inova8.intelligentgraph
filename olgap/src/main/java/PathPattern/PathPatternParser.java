// Generated from PathPattern.g4 by ANTLR 4.4
package PathPattern;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class PathPatternParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.4", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__11=1, T__10=2, T__9=3, T__8=4, T__7=5, T__6=6, T__5=7, T__4=8, T__3=9, 
		T__2=10, T__1=11, T__0=12, LITERAL=13, IRI_REF=14, PNAME_NS=15, PNAME_LN=16, 
		VARNAME=17, PN_PREFIX=18, PN_LOCAL=19, WS=20;
	public static final String[] tokenNames = {
		"<INVALID>", "'?'", "'a'", "'$this'", "'['", "';'", "'{'", "'>>'", "'<<'", 
		"']'", "'}'", "'^'", "'.'", "LITERAL", "IRI_REF", "PNAME_NS", "PNAME_LN", 
		"VARNAME", "PN_PREFIX", "PN_LOCAL", "WS"
	};
	public static final int
		RULE_graphPattern = 0, RULE_triples = 1, RULE_triple = 2, RULE_predicateObjects = 3, 
		RULE_predicateObject = 4, RULE_source = 5, RULE_target = 6, RULE_reification = 7, 
		RULE_bnode = 8, RULE_var = 9, RULE_reificate = 10, RULE_literal = 11, 
		RULE_predicatePattern = 12, RULE_predicatePath = 13, RULE_inversePredicatePath = 14, 
		RULE_predicate = 15, RULE_iriRef = 16, RULE_prefixedName = 17;
	public static final String[] ruleNames = {
		"graphPattern", "triples", "triple", "predicateObjects", "predicateObject", 
		"source", "target", "reification", "bnode", "var", "reificate", "literal", 
		"predicatePattern", "predicatePath", "inversePredicatePath", "predicate", 
		"iriRef", "prefixedName"
	};

	@Override
	public String getGrammarFileName() { return "PathPattern.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public PathPatternParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class GraphPatternContext extends ParserRuleContext {
		public SourceContext source() {
			return getRuleContext(SourceContext.class,0);
		}
		public TriplesContext triples() {
			return getRuleContext(TriplesContext.class,0);
		}
		public GraphPatternContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_graphPattern; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterGraphPattern(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitGraphPattern(this);
		}
	}

	public final GraphPatternContext graphPattern() throws RecognitionException {
		GraphPatternContext _localctx = new GraphPatternContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_graphPattern);
		try {
			setState(41);
			switch (_input.LA(1)) {
			case T__11:
			case T__9:
			case T__8:
			case T__4:
			case IRI_REF:
			case PNAME_NS:
			case PNAME_LN:
				enterOuterAlt(_localctx, 1);
				{
				setState(36); source();
				}
				break;
			case T__6:
				enterOuterAlt(_localctx, 2);
				{
				setState(37); match(T__6);
				setState(38); triples();
				setState(39); match(T__2);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TriplesContext extends ParserRuleContext {
		public TripleContext triple(int i) {
			return getRuleContext(TripleContext.class,i);
		}
		public List<TripleContext> triple() {
			return getRuleContexts(TripleContext.class);
		}
		public TriplesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_triples; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterTriples(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitTriples(this);
		}
	}

	public final TriplesContext triples() throws RecognitionException {
		TriplesContext _localctx = new TriplesContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_triples);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(43); triple();
			setState(48);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0) {
				{
				{
				setState(44); match(T__0);
				setState(45); triple();
				}
				}
				setState(50);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TripleContext extends ParserRuleContext {
		public SourceContext source() {
			return getRuleContext(SourceContext.class,0);
		}
		public PredicateObjectsContext predicateObjects() {
			return getRuleContext(PredicateObjectsContext.class,0);
		}
		public TripleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_triple; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterTriple(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitTriple(this);
		}
	}

	public final TripleContext triple() throws RecognitionException {
		TripleContext _localctx = new TripleContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_triple);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(51); source();
			setState(52); predicateObjects();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PredicateObjectsContext extends ParserRuleContext {
		public List<PredicateObjectContext> predicateObject() {
			return getRuleContexts(PredicateObjectContext.class);
		}
		public PredicateObjectContext predicateObject(int i) {
			return getRuleContext(PredicateObjectContext.class,i);
		}
		public PredicateObjectsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_predicateObjects; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterPredicateObjects(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitPredicateObjects(this);
		}
	}

	public final PredicateObjectsContext predicateObjects() throws RecognitionException {
		PredicateObjectsContext _localctx = new PredicateObjectsContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_predicateObjects);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(54); predicateObject();
			setState(59);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__7) {
				{
				{
				setState(55); match(T__7);
				setState(56); predicateObject();
				}
				}
				setState(61);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PredicateObjectContext extends ParserRuleContext {
		public TargetContext target() {
			return getRuleContext(TargetContext.class,0);
		}
		public PredicatePatternContext predicatePattern() {
			return getRuleContext(PredicatePatternContext.class,0);
		}
		public PredicateObjectContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_predicateObject; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterPredicateObject(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitPredicateObject(this);
		}
	}

	public final PredicateObjectContext predicateObject() throws RecognitionException {
		PredicateObjectContext _localctx = new PredicateObjectContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_predicateObject);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(62); predicatePattern();
			setState(63); target();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SourceContext extends ParserRuleContext {
		public VarContext var() {
			return getRuleContext(VarContext.class,0);
		}
		public ReificationContext reification() {
			return getRuleContext(ReificationContext.class,0);
		}
		public BnodeContext bnode() {
			return getRuleContext(BnodeContext.class,0);
		}
		public IriRefContext iriRef() {
			return getRuleContext(IriRefContext.class,0);
		}
		public SourceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_source; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterSource(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitSource(this);
		}
	}

	public final SourceContext source() throws RecognitionException {
		SourceContext _localctx = new SourceContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_source);
		try {
			setState(70);
			switch (_input.LA(1)) {
			case T__9:
				enterOuterAlt(_localctx, 1);
				{
				setState(65); match(T__9);
				}
				break;
			case T__11:
				enterOuterAlt(_localctx, 2);
				{
				setState(66); var();
				}
				break;
			case IRI_REF:
			case PNAME_NS:
			case PNAME_LN:
				enterOuterAlt(_localctx, 3);
				{
				setState(67); iriRef();
				}
				break;
			case T__4:
				enterOuterAlt(_localctx, 4);
				{
				setState(68); reification();
				}
				break;
			case T__8:
				enterOuterAlt(_localctx, 5);
				{
				setState(69); bnode();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TargetContext extends ParserRuleContext {
		public VarContext var() {
			return getRuleContext(VarContext.class,0);
		}
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public ReificationContext reification() {
			return getRuleContext(ReificationContext.class,0);
		}
		public BnodeContext bnode() {
			return getRuleContext(BnodeContext.class,0);
		}
		public IriRefContext iriRef() {
			return getRuleContext(IriRefContext.class,0);
		}
		public TargetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_target; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterTarget(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitTarget(this);
		}
	}

	public final TargetContext target() throws RecognitionException {
		TargetContext _localctx = new TargetContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_target);
		try {
			setState(78);
			switch (_input.LA(1)) {
			case T__9:
				enterOuterAlt(_localctx, 1);
				{
				setState(72); match(T__9);
				}
				break;
			case T__11:
				enterOuterAlt(_localctx, 2);
				{
				setState(73); var();
				}
				break;
			case IRI_REF:
			case PNAME_NS:
			case PNAME_LN:
				enterOuterAlt(_localctx, 3);
				{
				setState(74); iriRef();
				}
				break;
			case T__4:
				enterOuterAlt(_localctx, 4);
				{
				setState(75); reification();
				}
				break;
			case T__8:
				enterOuterAlt(_localctx, 5);
				{
				setState(76); bnode();
				}
				break;
			case LITERAL:
				enterOuterAlt(_localctx, 6);
				{
				setState(77); literal();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ReificationContext extends ParserRuleContext {
		public SourceContext source() {
			return getRuleContext(SourceContext.class,0);
		}
		public TargetContext target() {
			return getRuleContext(TargetContext.class,0);
		}
		public PredicateContext predicate() {
			return getRuleContext(PredicateContext.class,0);
		}
		public ReificationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_reification; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterReification(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitReification(this);
		}
	}

	public final ReificationContext reification() throws RecognitionException {
		ReificationContext _localctx = new ReificationContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_reification);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(80); match(T__4);
			setState(81); source();
			setState(82); predicate();
			setState(83); target();
			setState(84); match(T__5);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BnodeContext extends ParserRuleContext {
		public PredicateObjectsContext predicateObjects() {
			return getRuleContext(PredicateObjectsContext.class,0);
		}
		public BnodeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bnode; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterBnode(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitBnode(this);
		}
	}

	public final BnodeContext bnode() throws RecognitionException {
		BnodeContext _localctx = new BnodeContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_bnode);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(86); match(T__8);
			setState(87); predicateObjects();
			setState(88); match(T__3);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VarContext extends ParserRuleContext {
		public TerminalNode VARNAME() { return getToken(PathPatternParser.VARNAME, 0); }
		public VarContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_var; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterVar(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitVar(this);
		}
	}

	public final VarContext var() throws RecognitionException {
		VarContext _localctx = new VarContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_var);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(90); match(T__11);
			setState(91); match(VARNAME);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ReificateContext extends ParserRuleContext {
		public IriRefContext iriRef() {
			return getRuleContext(IriRefContext.class,0);
		}
		public ReificateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_reificate; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterReificate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitReificate(this);
		}
	}

	public final ReificateContext reificate() throws RecognitionException {
		ReificateContext _localctx = new ReificateContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_reificate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(93); iriRef();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LiteralContext extends ParserRuleContext {
		public TerminalNode LITERAL() { return getToken(PathPatternParser.LITERAL, 0); }
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitLiteral(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_literal);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(95); match(LITERAL);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PredicatePatternContext extends ParserRuleContext {
		public PredicatePathContext predicatePath() {
			return getRuleContext(PredicatePathContext.class,0);
		}
		public InversePredicatePathContext inversePredicatePath() {
			return getRuleContext(InversePredicatePathContext.class,0);
		}
		public PredicatePatternContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_predicatePattern; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterPredicatePattern(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitPredicatePattern(this);
		}
	}

	public final PredicatePatternContext predicatePattern() throws RecognitionException {
		PredicatePatternContext _localctx = new PredicatePatternContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_predicatePattern);
		try {
			setState(99);
			switch (_input.LA(1)) {
			case T__10:
			case IRI_REF:
			case PNAME_NS:
			case PNAME_LN:
				enterOuterAlt(_localctx, 1);
				{
				setState(97); predicatePath();
				}
				break;
			case T__1:
				enterOuterAlt(_localctx, 2);
				{
				setState(98); inversePredicatePath();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PredicatePathContext extends ParserRuleContext {
		public PredicateContext predicate() {
			return getRuleContext(PredicateContext.class,0);
		}
		public PredicatePathContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_predicatePath; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterPredicatePath(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitPredicatePath(this);
		}
	}

	public final PredicatePathContext predicatePath() throws RecognitionException {
		PredicatePathContext _localctx = new PredicatePathContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_predicatePath);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(101); predicate();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InversePredicatePathContext extends ParserRuleContext {
		public PredicateContext predicate() {
			return getRuleContext(PredicateContext.class,0);
		}
		public InversePredicatePathContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inversePredicatePath; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterInversePredicatePath(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitInversePredicatePath(this);
		}
	}

	public final InversePredicatePathContext inversePredicatePath() throws RecognitionException {
		InversePredicatePathContext _localctx = new InversePredicatePathContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_inversePredicatePath);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(103); match(T__1);
			setState(104); predicate();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PredicateContext extends ParserRuleContext {
		public IriRefContext iriRef() {
			return getRuleContext(IriRefContext.class,0);
		}
		public PredicateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_predicate; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterPredicate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitPredicate(this);
		}
	}

	public final PredicateContext predicate() throws RecognitionException {
		PredicateContext _localctx = new PredicateContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_predicate);
		try {
			setState(108);
			switch (_input.LA(1)) {
			case IRI_REF:
			case PNAME_NS:
			case PNAME_LN:
				enterOuterAlt(_localctx, 1);
				{
				setState(106); iriRef();
				}
				break;
			case T__10:
				enterOuterAlt(_localctx, 2);
				{
				setState(107); match(T__10);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IriRefContext extends ParserRuleContext {
		public TerminalNode IRI_REF() { return getToken(PathPatternParser.IRI_REF, 0); }
		public PrefixedNameContext prefixedName() {
			return getRuleContext(PrefixedNameContext.class,0);
		}
		public IriRefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_iriRef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterIriRef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitIriRef(this);
		}
	}

	public final IriRefContext iriRef() throws RecognitionException {
		IriRefContext _localctx = new IriRefContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_iriRef);
		try {
			setState(112);
			switch (_input.LA(1)) {
			case IRI_REF:
				enterOuterAlt(_localctx, 1);
				{
				setState(110); match(IRI_REF);
				}
				break;
			case PNAME_NS:
			case PNAME_LN:
				enterOuterAlt(_localctx, 2);
				{
				setState(111); prefixedName();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PrefixedNameContext extends ParserRuleContext {
		public TerminalNode PNAME_LN() { return getToken(PathPatternParser.PNAME_LN, 0); }
		public TerminalNode PNAME_NS() { return getToken(PathPatternParser.PNAME_NS, 0); }
		public PrefixedNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prefixedName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterPrefixedName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitPrefixedName(this);
		}
	}

	public final PrefixedNameContext prefixedName() throws RecognitionException {
		PrefixedNameContext _localctx = new PrefixedNameContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_prefixedName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(114);
			_la = _input.LA(1);
			if ( !(_la==PNAME_NS || _la==PNAME_LN) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\26w\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t\13\4"+
		"\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22\4\23"+
		"\t\23\3\2\3\2\3\2\3\2\3\2\5\2,\n\2\3\3\3\3\3\3\7\3\61\n\3\f\3\16\3\64"+
		"\13\3\3\4\3\4\3\4\3\5\3\5\3\5\7\5<\n\5\f\5\16\5?\13\5\3\6\3\6\3\6\3\7"+
		"\3\7\3\7\3\7\3\7\5\7I\n\7\3\b\3\b\3\b\3\b\3\b\3\b\5\bQ\n\b\3\t\3\t\3\t"+
		"\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16"+
		"\5\16f\n\16\3\17\3\17\3\20\3\20\3\20\3\21\3\21\5\21o\n\21\3\22\3\22\5"+
		"\22s\n\22\3\23\3\23\3\23\2\2\24\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36"+
		" \"$\2\3\3\2\21\22s\2+\3\2\2\2\4-\3\2\2\2\6\65\3\2\2\2\b8\3\2\2\2\n@\3"+
		"\2\2\2\fH\3\2\2\2\16P\3\2\2\2\20R\3\2\2\2\22X\3\2\2\2\24\\\3\2\2\2\26"+
		"_\3\2\2\2\30a\3\2\2\2\32e\3\2\2\2\34g\3\2\2\2\36i\3\2\2\2 n\3\2\2\2\""+
		"r\3\2\2\2$t\3\2\2\2&,\5\f\7\2\'(\7\b\2\2()\5\4\3\2)*\7\f\2\2*,\3\2\2\2"+
		"+&\3\2\2\2+\'\3\2\2\2,\3\3\2\2\2-\62\5\6\4\2./\7\16\2\2/\61\5\6\4\2\60"+
		".\3\2\2\2\61\64\3\2\2\2\62\60\3\2\2\2\62\63\3\2\2\2\63\5\3\2\2\2\64\62"+
		"\3\2\2\2\65\66\5\f\7\2\66\67\5\b\5\2\67\7\3\2\2\28=\5\n\6\29:\7\7\2\2"+
		":<\5\n\6\2;9\3\2\2\2<?\3\2\2\2=;\3\2\2\2=>\3\2\2\2>\t\3\2\2\2?=\3\2\2"+
		"\2@A\5\32\16\2AB\5\16\b\2B\13\3\2\2\2CI\7\5\2\2DI\5\24\13\2EI\5\"\22\2"+
		"FI\5\20\t\2GI\5\22\n\2HC\3\2\2\2HD\3\2\2\2HE\3\2\2\2HF\3\2\2\2HG\3\2\2"+
		"\2I\r\3\2\2\2JQ\7\5\2\2KQ\5\24\13\2LQ\5\"\22\2MQ\5\20\t\2NQ\5\22\n\2O"+
		"Q\5\30\r\2PJ\3\2\2\2PK\3\2\2\2PL\3\2\2\2PM\3\2\2\2PN\3\2\2\2PO\3\2\2\2"+
		"Q\17\3\2\2\2RS\7\n\2\2ST\5\f\7\2TU\5 \21\2UV\5\16\b\2VW\7\t\2\2W\21\3"+
		"\2\2\2XY\7\6\2\2YZ\5\b\5\2Z[\7\13\2\2[\23\3\2\2\2\\]\7\3\2\2]^\7\23\2"+
		"\2^\25\3\2\2\2_`\5\"\22\2`\27\3\2\2\2ab\7\17\2\2b\31\3\2\2\2cf\5\34\17"+
		"\2df\5\36\20\2ec\3\2\2\2ed\3\2\2\2f\33\3\2\2\2gh\5 \21\2h\35\3\2\2\2i"+
		"j\7\r\2\2jk\5 \21\2k\37\3\2\2\2lo\5\"\22\2mo\7\4\2\2nl\3\2\2\2nm\3\2\2"+
		"\2o!\3\2\2\2ps\7\20\2\2qs\5$\23\2rp\3\2\2\2rq\3\2\2\2s#\3\2\2\2tu\t\2"+
		"\2\2u%\3\2\2\2\n+\62=HPenr";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}