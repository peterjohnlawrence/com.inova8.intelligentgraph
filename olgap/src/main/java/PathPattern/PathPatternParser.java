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
		T__13=1, T__12=2, T__11=3, T__10=4, T__9=5, T__8=6, T__7=7, T__6=8, T__5=9, 
		T__4=10, T__3=11, T__2=12, T__1=13, T__0=14, KEY=15, INTEGER=16, BINDVARIABLE=17, 
		INVERSE=18, REIFIER=19, DEREIFIER=20, RDFTYPE=21, ANYPREDICATE=22, OPERATOR=23, 
		DQLITERAL=24, SQLITERAL=25, IRI_REF=26, PNAME_NS=27, VARNAME=28, PN_LOCAL=29, 
		WS=30;
	public static final String[] tokenNames = {
		"<INVALID>", "'/'", "';'", "'['", "'{'", "'^^'", "'|'", "']'", "'}'", 
		"'='", "'>'", "'!'", "'('", "')'", "','", "KEY", "INTEGER", "BINDVARIABLE", 
		"'^'", "'@'", "'#'", "'a'", "'*'", "OPERATOR", "DQLITERAL", "SQLITERAL", 
		"IRI_REF", "PNAME_NS", "VARNAME", "PN_LOCAL", "WS"
	};
	public static final int
		RULE_queryString = 0, RULE_queryOptions = 1, RULE_queryOption = 2, RULE_type = 3, 
		RULE_pathPattern = 4, RULE_binding = 5, RULE_pathPatterns = 6, RULE_cardinality = 7, 
		RULE_negation = 8, RULE_pathEltOrInverse = 9, RULE_predicate = 10, RULE_anyPredicate = 11, 
		RULE_reifiedPredicate = 12, RULE_predicateRef = 13, RULE_iriRef = 14, 
		RULE_dereifier = 15, RULE_factFilterPattern = 16, RULE_propertyListNotEmpty = 17, 
		RULE_verbObjectList = 18, RULE_verb = 19, RULE_objectList = 20, RULE_object = 21, 
		RULE_qname = 22, RULE_pname_ns = 23, RULE_literal = 24, RULE_operator = 25, 
		RULE_rdfType = 26;
	public static final String[] ruleNames = {
		"queryString", "queryOptions", "queryOption", "type", "pathPattern", "binding", 
		"pathPatterns", "cardinality", "negation", "pathEltOrInverse", "predicate", 
		"anyPredicate", "reifiedPredicate", "predicateRef", "iriRef", "dereifier", 
		"factFilterPattern", "propertyListNotEmpty", "verbObjectList", "verb", 
		"objectList", "object", "qname", "pname_ns", "literal", "operator", "rdfType"
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
	public static class QueryStringContext extends ParserRuleContext {
		public QueryOptionsContext queryOptions() {
			return getRuleContext(QueryOptionsContext.class,0);
		}
		public TerminalNode EOF() { return getToken(PathPatternParser.EOF, 0); }
		public PathPatternContext pathPattern() {
			return getRuleContext(PathPatternContext.class,0);
		}
		public QueryStringContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_queryString; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterQueryString(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitQueryString(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitQueryString(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QueryStringContext queryString() throws RecognitionException {
		QueryStringContext _localctx = new QueryStringContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_queryString);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(54); pathPattern();
			setState(56);
			_la = _input.LA(1);
			if (_la==KEY) {
				{
				setState(55); queryOptions();
				}
			}

			setState(58); match(EOF);
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

	public static class QueryOptionsContext extends ParserRuleContext {
		public List<QueryOptionContext> queryOption() {
			return getRuleContexts(QueryOptionContext.class);
		}
		public QueryOptionContext queryOption(int i) {
			return getRuleContext(QueryOptionContext.class,i);
		}
		public QueryOptionsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_queryOptions; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterQueryOptions(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitQueryOptions(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitQueryOptions(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QueryOptionsContext queryOptions() throws RecognitionException {
		QueryOptionsContext _localctx = new QueryOptionsContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_queryOptions);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(61); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(60); queryOption();
				}
				}
				setState(63); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==KEY );
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

	public static class QueryOptionContext extends ParserRuleContext {
		public TerminalNode KEY() { return getToken(PathPatternParser.KEY, 0); }
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public QueryOptionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_queryOption; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterQueryOption(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitQueryOption(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitQueryOption(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QueryOptionContext queryOption() throws RecognitionException {
		QueryOptionContext _localctx = new QueryOptionContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_queryOption);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(65); match(KEY);
			setState(66); match(T__5);
			setState(67); literal();
			setState(70);
			_la = _input.LA(1);
			if (_la==T__9) {
				{
				setState(68); match(T__9);
				setState(69); type();
				}
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

	public static class TypeContext extends ParserRuleContext {
		public QnameContext qname() {
			return getRuleContext(QnameContext.class,0);
		}
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeContext type() throws RecognitionException {
		TypeContext _localctx = new TypeContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_type);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(72); qname();
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

	public static class PathPatternContext extends ParserRuleContext {
		public PathPatternContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pathPattern; }
	 
		public PathPatternContext() { }
		public void copyFrom(PathPatternContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class BoundPatternContext extends PathPatternContext {
		public PathPatternsContext pathPatterns() {
			return getRuleContext(PathPatternsContext.class,0);
		}
		public BindingContext binding() {
			return getRuleContext(BindingContext.class,0);
		}
		public BoundPatternContext(PathPatternContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterBoundPattern(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitBoundPattern(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitBoundPattern(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MatchOnlyPatternContext extends PathPatternContext {
		public BindingContext binding() {
			return getRuleContext(BindingContext.class,0);
		}
		public MatchOnlyPatternContext(PathPatternContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterMatchOnlyPattern(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitMatchOnlyPattern(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitMatchOnlyPattern(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PathOnlyPatternContext extends PathPatternContext {
		public PathPatternsContext pathPatterns() {
			return getRuleContext(PathPatternsContext.class,0);
		}
		public PathOnlyPatternContext(PathPatternContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterPathOnlyPattern(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitPathOnlyPattern(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitPathOnlyPattern(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PathPatternContext pathPattern() throws RecognitionException {
		PathPatternContext _localctx = new PathPatternContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_pathPattern);
		int _la;
		try {
			setState(80);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				_localctx = new BoundPatternContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(74); binding();
				setState(75);
				_la = _input.LA(1);
				if ( !(_la==T__13 || _la==T__4) ) {
				_errHandler.recoverInline(this);
				}
				consume();
				setState(76); pathPatterns(0);
				}
				break;
			case 2:
				_localctx = new MatchOnlyPatternContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(78); binding();
				}
				break;
			case 3:
				_localctx = new PathOnlyPatternContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(79); pathPatterns(0);
				}
				break;
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

	public static class BindingContext extends ParserRuleContext {
		public FactFilterPatternContext factFilterPattern() {
			return getRuleContext(FactFilterPatternContext.class,0);
		}
		public BindingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_binding; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterBinding(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitBinding(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitBinding(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BindingContext binding() throws RecognitionException {
		BindingContext _localctx = new BindingContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_binding);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(82); factFilterPattern();
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

	public static class PathPatternsContext extends ParserRuleContext {
		public PathPatternsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pathPatterns; }
	 
		public PathPatternsContext() { }
		public void copyFrom(PathPatternsContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class PathContext extends PathPatternsContext {
		public CardinalityContext cardinality() {
			return getRuleContext(CardinalityContext.class,0);
		}
		public PathEltOrInverseContext pathEltOrInverse() {
			return getRuleContext(PathEltOrInverseContext.class,0);
		}
		public PathContext(PathPatternsContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterPath(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitPath(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitPath(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PathAlternativeContext extends PathPatternsContext {
		public List<PathPatternsContext> pathPatterns() {
			return getRuleContexts(PathPatternsContext.class);
		}
		public PathPatternsContext pathPatterns(int i) {
			return getRuleContext(PathPatternsContext.class,i);
		}
		public PathAlternativeContext(PathPatternsContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterPathAlternative(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitPathAlternative(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitPathAlternative(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PathParenthesesContext extends PathPatternsContext {
		public NegationContext negation() {
			return getRuleContext(NegationContext.class,0);
		}
		public CardinalityContext cardinality() {
			return getRuleContext(CardinalityContext.class,0);
		}
		public PathPatternsContext pathPatterns() {
			return getRuleContext(PathPatternsContext.class,0);
		}
		public PathParenthesesContext(PathPatternsContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterPathParentheses(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitPathParentheses(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitPathParentheses(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PathSequenceContext extends PathPatternsContext {
		public List<PathPatternsContext> pathPatterns() {
			return getRuleContexts(PathPatternsContext.class);
		}
		public PathPatternsContext pathPatterns(int i) {
			return getRuleContext(PathPatternsContext.class,i);
		}
		public PathSequenceContext(PathPatternsContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterPathSequence(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitPathSequence(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitPathSequence(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PathPatternsContext pathPatterns() throws RecognitionException {
		return pathPatterns(0);
	}

	private PathPatternsContext pathPatterns(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		PathPatternsContext _localctx = new PathPatternsContext(_ctx, _parentState);
		PathPatternsContext _prevctx = _localctx;
		int _startState = 12;
		enterRecursionRule(_localctx, 12, RULE_pathPatterns, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(98);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				{
				_localctx = new PathContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(85); pathEltOrInverse();
				setState(87);
				switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
				case 1:
					{
					setState(86); cardinality();
					}
					break;
				}
				}
				break;
			case 2:
				{
				_localctx = new PathParenthesesContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(90);
				_la = _input.LA(1);
				if (_la==T__3) {
					{
					setState(89); negation();
					}
				}

				setState(92); match(T__2);
				setState(93); pathPatterns(0);
				setState(94); match(T__1);
				setState(96);
				switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
				case 1:
					{
					setState(95); cardinality();
					}
					break;
				}
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(108);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(106);
					switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
					case 1:
						{
						_localctx = new PathAlternativeContext(new PathPatternsContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_pathPatterns);
						setState(100);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(101); match(T__8);
						setState(102); pathPatterns(4);
						}
						break;
					case 2:
						{
						_localctx = new PathSequenceContext(new PathPatternsContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_pathPatterns);
						setState(103);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(104);
						_la = _input.LA(1);
						if ( !(_la==T__13 || _la==T__4) ) {
						_errHandler.recoverInline(this);
						}
						consume();
						setState(105); pathPatterns(3);
						}
						break;
					}
					} 
				}
				setState(110);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class CardinalityContext extends ParserRuleContext {
		public List<TerminalNode> INTEGER() { return getTokens(PathPatternParser.INTEGER); }
		public TerminalNode INTEGER(int i) {
			return getToken(PathPatternParser.INTEGER, i);
		}
		public CardinalityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cardinality; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterCardinality(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitCardinality(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitCardinality(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CardinalityContext cardinality() throws RecognitionException {
		CardinalityContext _localctx = new CardinalityContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_cardinality);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(111); match(T__10);
			setState(112); match(INTEGER);
			setState(117);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(113); match(T__0);
				setState(115);
				_la = _input.LA(1);
				if (_la==INTEGER) {
					{
					setState(114); match(INTEGER);
					}
				}

				}
			}

			setState(119); match(T__6);
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

	public static class NegationContext extends ParserRuleContext {
		public NegationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_negation; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterNegation(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitNegation(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitNegation(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NegationContext negation() throws RecognitionException {
		NegationContext _localctx = new NegationContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_negation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(121); match(T__3);
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

	public static class PathEltOrInverseContext extends ParserRuleContext {
		public NegationContext negation() {
			return getRuleContext(NegationContext.class,0);
		}
		public TerminalNode INVERSE() { return getToken(PathPatternParser.INVERSE, 0); }
		public PredicateContext predicate() {
			return getRuleContext(PredicateContext.class,0);
		}
		public PathEltOrInverseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pathEltOrInverse; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterPathEltOrInverse(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitPathEltOrInverse(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitPathEltOrInverse(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PathEltOrInverseContext pathEltOrInverse() throws RecognitionException {
		PathEltOrInverseContext _localctx = new PathEltOrInverseContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_pathEltOrInverse);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(124);
			_la = _input.LA(1);
			if (_la==T__3) {
				{
				setState(123); negation();
				}
			}

			setState(127);
			_la = _input.LA(1);
			if (_la==INVERSE) {
				{
				setState(126); match(INVERSE);
				}
			}

			setState(129); predicate();
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
		public AnyPredicateContext anyPredicate() {
			return getRuleContext(AnyPredicateContext.class,0);
		}
		public ReifiedPredicateContext reifiedPredicate() {
			return getRuleContext(ReifiedPredicateContext.class,0);
		}
		public PredicateRefContext predicateRef() {
			return getRuleContext(PredicateRefContext.class,0);
		}
		public RdfTypeContext rdfType() {
			return getRuleContext(RdfTypeContext.class,0);
		}
		public FactFilterPatternContext factFilterPattern() {
			return getRuleContext(FactFilterPatternContext.class,0);
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
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitPredicate(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PredicateContext predicate() throws RecognitionException {
		PredicateContext _localctx = new PredicateContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_predicate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(135);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				{
				setState(131); reifiedPredicate();
				}
				break;
			case 2:
				{
				setState(132); predicateRef();
				}
				break;
			case 3:
				{
				setState(133); rdfType();
				}
				break;
			case 4:
				{
				setState(134); anyPredicate();
				}
				break;
			}
			setState(138);
			switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
			case 1:
				{
				setState(137); factFilterPattern();
				}
				break;
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

	public static class AnyPredicateContext extends ParserRuleContext {
		public TerminalNode ANYPREDICATE() { return getToken(PathPatternParser.ANYPREDICATE, 0); }
		public AnyPredicateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_anyPredicate; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterAnyPredicate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitAnyPredicate(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitAnyPredicate(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AnyPredicateContext anyPredicate() throws RecognitionException {
		AnyPredicateContext _localctx = new AnyPredicateContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_anyPredicate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(140); match(ANYPREDICATE);
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

	public static class ReifiedPredicateContext extends ParserRuleContext {
		public PredicateRefContext predicateRef() {
			return getRuleContext(PredicateRefContext.class,0);
		}
		public DereifierContext dereifier() {
			return getRuleContext(DereifierContext.class,0);
		}
		public TerminalNode REIFIER() { return getToken(PathPatternParser.REIFIER, 0); }
		public FactFilterPatternContext factFilterPattern() {
			return getRuleContext(FactFilterPatternContext.class,0);
		}
		public IriRefContext iriRef() {
			return getRuleContext(IriRefContext.class,0);
		}
		public ReifiedPredicateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_reifiedPredicate; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterReifiedPredicate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitReifiedPredicate(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitReifiedPredicate(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReifiedPredicateContext reifiedPredicate() throws RecognitionException {
		ReifiedPredicateContext _localctx = new ReifiedPredicateContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_reifiedPredicate);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(143);
			_la = _input.LA(1);
			if (_la==IRI_REF || _la==PNAME_NS) {
				{
				setState(142); iriRef();
				}
			}

			setState(145); match(REIFIER);
			setState(146); predicateRef();
			setState(148);
			switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
			case 1:
				{
				setState(147); factFilterPattern();
				}
				break;
			}
			setState(151);
			switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
			case 1:
				{
				setState(150); dereifier();
				}
				break;
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

	public static class PredicateRefContext extends ParserRuleContext {
		public QnameContext qname() {
			return getRuleContext(QnameContext.class,0);
		}
		public Pname_nsContext pname_ns() {
			return getRuleContext(Pname_nsContext.class,0);
		}
		public RdfTypeContext rdfType() {
			return getRuleContext(RdfTypeContext.class,0);
		}
		public TerminalNode IRI_REF() { return getToken(PathPatternParser.IRI_REF, 0); }
		public PredicateRefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_predicateRef; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterPredicateRef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitPredicateRef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitPredicateRef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PredicateRefContext predicateRef() throws RecognitionException {
		PredicateRefContext _localctx = new PredicateRefContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_predicateRef);
		try {
			setState(157);
			switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(153); match(IRI_REF);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(154); rdfType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(155); qname();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(156); pname_ns();
				}
				break;
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
		public QnameContext qname() {
			return getRuleContext(QnameContext.class,0);
		}
		public Pname_nsContext pname_ns() {
			return getRuleContext(Pname_nsContext.class,0);
		}
		public TerminalNode IRI_REF() { return getToken(PathPatternParser.IRI_REF, 0); }
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
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitIriRef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IriRefContext iriRef() throws RecognitionException {
		IriRefContext _localctx = new IriRefContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_iriRef);
		try {
			setState(162);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(159); match(IRI_REF);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(160); qname();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(161); pname_ns();
				}
				break;
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

	public static class DereifierContext extends ParserRuleContext {
		public TerminalNode DEREIFIER() { return getToken(PathPatternParser.DEREIFIER, 0); }
		public DereifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_dereifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterDereifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitDereifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitDereifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DereifierContext dereifier() throws RecognitionException {
		DereifierContext _localctx = new DereifierContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_dereifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(164); match(DEREIFIER);
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

	public static class FactFilterPatternContext extends ParserRuleContext {
		public PropertyListNotEmptyContext propertyListNotEmpty() {
			return getRuleContext(PropertyListNotEmptyContext.class,0);
		}
		public FactFilterPatternContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_factFilterPattern; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterFactFilterPattern(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitFactFilterPattern(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitFactFilterPattern(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FactFilterPatternContext factFilterPattern() throws RecognitionException {
		FactFilterPatternContext _localctx = new FactFilterPatternContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_factFilterPattern);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(166); match(T__11);
			setState(167); propertyListNotEmpty();
			setState(168); match(T__7);
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

	public static class PropertyListNotEmptyContext extends ParserRuleContext {
		public List<VerbObjectListContext> verbObjectList() {
			return getRuleContexts(VerbObjectListContext.class);
		}
		public VerbObjectListContext verbObjectList(int i) {
			return getRuleContext(VerbObjectListContext.class,i);
		}
		public PropertyListNotEmptyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_propertyListNotEmpty; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterPropertyListNotEmpty(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitPropertyListNotEmpty(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitPropertyListNotEmpty(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PropertyListNotEmptyContext propertyListNotEmpty() throws RecognitionException {
		PropertyListNotEmptyContext _localctx = new PropertyListNotEmptyContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_propertyListNotEmpty);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(170); verbObjectList();
			setState(177);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__12) {
				{
				{
				setState(171); match(T__12);
				setState(173);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << INVERSE) | (1L << REIFIER) | (1L << RDFTYPE) | (1L << ANYPREDICATE) | (1L << OPERATOR) | (1L << IRI_REF) | (1L << PNAME_NS))) != 0)) {
					{
					setState(172); verbObjectList();
					}
				}

				}
				}
				setState(179);
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

	public static class VerbObjectListContext extends ParserRuleContext {
		public ObjectListContext objectList() {
			return getRuleContext(ObjectListContext.class,0);
		}
		public VerbContext verb() {
			return getRuleContext(VerbContext.class,0);
		}
		public VerbObjectListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_verbObjectList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterVerbObjectList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitVerbObjectList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitVerbObjectList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VerbObjectListContext verbObjectList() throws RecognitionException {
		VerbObjectListContext _localctx = new VerbObjectListContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_verbObjectList);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(180); verb();
			setState(181); objectList();
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

	public static class VerbContext extends ParserRuleContext {
		public OperatorContext operator() {
			return getRuleContext(OperatorContext.class,0);
		}
		public PathEltOrInverseContext pathEltOrInverse() {
			return getRuleContext(PathEltOrInverseContext.class,0);
		}
		public VerbContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_verb; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterVerb(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitVerb(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitVerb(this);
			else return visitor.visitChildren(this);
		}
	}

	public final VerbContext verb() throws RecognitionException {
		VerbContext _localctx = new VerbContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_verb);
		try {
			setState(185);
			switch (_input.LA(1)) {
			case OPERATOR:
				enterOuterAlt(_localctx, 1);
				{
				setState(183); operator();
				}
				break;
			case T__3:
			case INVERSE:
			case REIFIER:
			case RDFTYPE:
			case ANYPREDICATE:
			case IRI_REF:
			case PNAME_NS:
				enterOuterAlt(_localctx, 2);
				{
				setState(184); pathEltOrInverse();
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

	public static class ObjectListContext extends ParserRuleContext {
		public ObjectContext object(int i) {
			return getRuleContext(ObjectContext.class,i);
		}
		public List<ObjectContext> object() {
			return getRuleContexts(ObjectContext.class);
		}
		public ObjectListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_objectList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterObjectList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitObjectList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitObjectList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectListContext objectList() throws RecognitionException {
		ObjectListContext _localctx = new ObjectListContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_objectList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(187); object();
			setState(192);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0) {
				{
				{
				setState(188); match(T__0);
				setState(189); object();
				}
				}
				setState(194);
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

	public static class ObjectContext extends ParserRuleContext {
		public TerminalNode BINDVARIABLE() { return getToken(PathPatternParser.BINDVARIABLE, 0); }
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public FactFilterPatternContext factFilterPattern() {
			return getRuleContext(FactFilterPatternContext.class,0);
		}
		public IriRefContext iriRef() {
			return getRuleContext(IriRefContext.class,0);
		}
		public ObjectContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_object; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterObject(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitObject(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitObject(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ObjectContext object() throws RecognitionException {
		ObjectContext _localctx = new ObjectContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_object);
		try {
			setState(199);
			switch (_input.LA(1)) {
			case IRI_REF:
			case PNAME_NS:
				enterOuterAlt(_localctx, 1);
				{
				setState(195); iriRef();
				}
				break;
			case DQLITERAL:
			case SQLITERAL:
				enterOuterAlt(_localctx, 2);
				{
				setState(196); literal();
				}
				break;
			case T__11:
				enterOuterAlt(_localctx, 3);
				{
				setState(197); factFilterPattern();
				}
				break;
			case BINDVARIABLE:
				enterOuterAlt(_localctx, 4);
				{
				setState(198); match(BINDVARIABLE);
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

	public static class QnameContext extends ParserRuleContext {
		public TerminalNode PN_LOCAL() { return getToken(PathPatternParser.PN_LOCAL, 0); }
		public TerminalNode PNAME_NS() { return getToken(PathPatternParser.PNAME_NS, 0); }
		public QnameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qname; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterQname(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitQname(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitQname(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QnameContext qname() throws RecognitionException {
		QnameContext _localctx = new QnameContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_qname);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(201); match(PNAME_NS);
			setState(202); match(PN_LOCAL);
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

	public static class Pname_nsContext extends ParserRuleContext {
		public TerminalNode PNAME_NS() { return getToken(PathPatternParser.PNAME_NS, 0); }
		public Pname_nsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pname_ns; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterPname_ns(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitPname_ns(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitPname_ns(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Pname_nsContext pname_ns() throws RecognitionException {
		Pname_nsContext _localctx = new Pname_nsContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_pname_ns);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(204); match(PNAME_NS);
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
		public QnameContext qname() {
			return getRuleContext(QnameContext.class,0);
		}
		public TerminalNode IRI_REF() { return getToken(PathPatternParser.IRI_REF, 0); }
		public TerminalNode SQLITERAL() { return getToken(PathPatternParser.SQLITERAL, 0); }
		public TerminalNode DQLITERAL() { return getToken(PathPatternParser.DQLITERAL, 0); }
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
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_literal);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(206);
			_la = _input.LA(1);
			if ( !(_la==DQLITERAL || _la==SQLITERAL) ) {
			_errHandler.recoverInline(this);
			}
			consume();
			setState(212);
			switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
			case 1:
				{
				setState(207); match(T__9);
				setState(210);
				switch (_input.LA(1)) {
				case IRI_REF:
					{
					setState(208); match(IRI_REF);
					}
					break;
				case PNAME_NS:
					{
					setState(209); qname();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				break;
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

	public static class OperatorContext extends ParserRuleContext {
		public TerminalNode OPERATOR() { return getToken(PathPatternParser.OPERATOR, 0); }
		public OperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterOperator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitOperator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OperatorContext operator() throws RecognitionException {
		OperatorContext _localctx = new OperatorContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_operator);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(214); match(OPERATOR);
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

	public static class RdfTypeContext extends ParserRuleContext {
		public TerminalNode RDFTYPE() { return getToken(PathPatternParser.RDFTYPE, 0); }
		public RdfTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_rdfType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterRdfType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitRdfType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitRdfType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RdfTypeContext rdfType() throws RecognitionException {
		RdfTypeContext _localctx = new RdfTypeContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_rdfType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(216); match(RDFTYPE);
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

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 6: return pathPatterns_sempred((PathPatternsContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean pathPatterns_sempred(PathPatternsContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0: return precpred(_ctx, 3);
		case 1: return precpred(_ctx, 2);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3 \u00dd\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\3\2\3\2\5\2;\n\2\3\2\3\2\3\3\6\3@\n\3\r"+
		"\3\16\3A\3\4\3\4\3\4\3\4\3\4\5\4I\n\4\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6"+
		"\5\6S\n\6\3\7\3\7\3\b\3\b\3\b\5\bZ\n\b\3\b\5\b]\n\b\3\b\3\b\3\b\3\b\5"+
		"\bc\n\b\5\be\n\b\3\b\3\b\3\b\3\b\3\b\3\b\7\bm\n\b\f\b\16\bp\13\b\3\t\3"+
		"\t\3\t\3\t\5\tv\n\t\5\tx\n\t\3\t\3\t\3\n\3\n\3\13\5\13\177\n\13\3\13\5"+
		"\13\u0082\n\13\3\13\3\13\3\f\3\f\3\f\3\f\5\f\u008a\n\f\3\f\5\f\u008d\n"+
		"\f\3\r\3\r\3\16\5\16\u0092\n\16\3\16\3\16\3\16\5\16\u0097\n\16\3\16\5"+
		"\16\u009a\n\16\3\17\3\17\3\17\3\17\5\17\u00a0\n\17\3\20\3\20\3\20\5\20"+
		"\u00a5\n\20\3\21\3\21\3\22\3\22\3\22\3\22\3\23\3\23\3\23\5\23\u00b0\n"+
		"\23\7\23\u00b2\n\23\f\23\16\23\u00b5\13\23\3\24\3\24\3\24\3\25\3\25\5"+
		"\25\u00bc\n\25\3\26\3\26\3\26\7\26\u00c1\n\26\f\26\16\26\u00c4\13\26\3"+
		"\27\3\27\3\27\3\27\5\27\u00ca\n\27\3\30\3\30\3\30\3\31\3\31\3\32\3\32"+
		"\3\32\3\32\5\32\u00d5\n\32\5\32\u00d7\n\32\3\33\3\33\3\34\3\34\3\34\2"+
		"\3\16\35\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\66\2"+
		"\4\4\2\3\3\f\f\3\2\32\33\u00e5\28\3\2\2\2\4?\3\2\2\2\6C\3\2\2\2\bJ\3\2"+
		"\2\2\nR\3\2\2\2\fT\3\2\2\2\16d\3\2\2\2\20q\3\2\2\2\22{\3\2\2\2\24~\3\2"+
		"\2\2\26\u0089\3\2\2\2\30\u008e\3\2\2\2\32\u0091\3\2\2\2\34\u009f\3\2\2"+
		"\2\36\u00a4\3\2\2\2 \u00a6\3\2\2\2\"\u00a8\3\2\2\2$\u00ac\3\2\2\2&\u00b6"+
		"\3\2\2\2(\u00bb\3\2\2\2*\u00bd\3\2\2\2,\u00c9\3\2\2\2.\u00cb\3\2\2\2\60"+
		"\u00ce\3\2\2\2\62\u00d0\3\2\2\2\64\u00d8\3\2\2\2\66\u00da\3\2\2\28:\5"+
		"\n\6\29;\5\4\3\2:9\3\2\2\2:;\3\2\2\2;<\3\2\2\2<=\7\2\2\3=\3\3\2\2\2>@"+
		"\5\6\4\2?>\3\2\2\2@A\3\2\2\2A?\3\2\2\2AB\3\2\2\2B\5\3\2\2\2CD\7\21\2\2"+
		"DE\7\13\2\2EH\5\62\32\2FG\7\7\2\2GI\5\b\5\2HF\3\2\2\2HI\3\2\2\2I\7\3\2"+
		"\2\2JK\5.\30\2K\t\3\2\2\2LM\5\f\7\2MN\t\2\2\2NO\5\16\b\2OS\3\2\2\2PS\5"+
		"\f\7\2QS\5\16\b\2RL\3\2\2\2RP\3\2\2\2RQ\3\2\2\2S\13\3\2\2\2TU\5\"\22\2"+
		"U\r\3\2\2\2VW\b\b\1\2WY\5\24\13\2XZ\5\20\t\2YX\3\2\2\2YZ\3\2\2\2Ze\3\2"+
		"\2\2[]\5\22\n\2\\[\3\2\2\2\\]\3\2\2\2]^\3\2\2\2^_\7\16\2\2_`\5\16\b\2"+
		"`b\7\17\2\2ac\5\20\t\2ba\3\2\2\2bc\3\2\2\2ce\3\2\2\2dV\3\2\2\2d\\\3\2"+
		"\2\2en\3\2\2\2fg\f\5\2\2gh\7\b\2\2hm\5\16\b\6ij\f\4\2\2jk\t\2\2\2km\5"+
		"\16\b\5lf\3\2\2\2li\3\2\2\2mp\3\2\2\2nl\3\2\2\2no\3\2\2\2o\17\3\2\2\2"+
		"pn\3\2\2\2qr\7\6\2\2rw\7\22\2\2su\7\20\2\2tv\7\22\2\2ut\3\2\2\2uv\3\2"+
		"\2\2vx\3\2\2\2ws\3\2\2\2wx\3\2\2\2xy\3\2\2\2yz\7\n\2\2z\21\3\2\2\2{|\7"+
		"\r\2\2|\23\3\2\2\2}\177\5\22\n\2~}\3\2\2\2~\177\3\2\2\2\177\u0081\3\2"+
		"\2\2\u0080\u0082\7\24\2\2\u0081\u0080\3\2\2\2\u0081\u0082\3\2\2\2\u0082"+
		"\u0083\3\2\2\2\u0083\u0084\5\26\f\2\u0084\25\3\2\2\2\u0085\u008a\5\32"+
		"\16\2\u0086\u008a\5\34\17\2\u0087\u008a\5\66\34\2\u0088\u008a\5\30\r\2"+
		"\u0089\u0085\3\2\2\2\u0089\u0086\3\2\2\2\u0089\u0087\3\2\2\2\u0089\u0088"+
		"\3\2\2\2\u008a\u008c\3\2\2\2\u008b\u008d\5\"\22\2\u008c\u008b\3\2\2\2"+
		"\u008c\u008d\3\2\2\2\u008d\27\3\2\2\2\u008e\u008f\7\30\2\2\u008f\31\3"+
		"\2\2\2\u0090\u0092\5\36\20\2\u0091\u0090\3\2\2\2\u0091\u0092\3\2\2\2\u0092"+
		"\u0093\3\2\2\2\u0093\u0094\7\25\2\2\u0094\u0096\5\34\17\2\u0095\u0097"+
		"\5\"\22\2\u0096\u0095\3\2\2\2\u0096\u0097\3\2\2\2\u0097\u0099\3\2\2\2"+
		"\u0098\u009a\5 \21\2\u0099\u0098\3\2\2\2\u0099\u009a\3\2\2\2\u009a\33"+
		"\3\2\2\2\u009b\u00a0\7\34\2\2\u009c\u00a0\5\66\34\2\u009d\u00a0\5.\30"+
		"\2\u009e\u00a0\5\60\31\2\u009f\u009b\3\2\2\2\u009f\u009c\3\2\2\2\u009f"+
		"\u009d\3\2\2\2\u009f\u009e\3\2\2\2\u00a0\35\3\2\2\2\u00a1\u00a5\7\34\2"+
		"\2\u00a2\u00a5\5.\30\2\u00a3\u00a5\5\60\31\2\u00a4\u00a1\3\2\2\2\u00a4"+
		"\u00a2\3\2\2\2\u00a4\u00a3\3\2\2\2\u00a5\37\3\2\2\2\u00a6\u00a7\7\26\2"+
		"\2\u00a7!\3\2\2\2\u00a8\u00a9\7\5\2\2\u00a9\u00aa\5$\23\2\u00aa\u00ab"+
		"\7\t\2\2\u00ab#\3\2\2\2\u00ac\u00b3\5&\24\2\u00ad\u00af\7\4\2\2\u00ae"+
		"\u00b0\5&\24\2\u00af\u00ae\3\2\2\2\u00af\u00b0\3\2\2\2\u00b0\u00b2\3\2"+
		"\2\2\u00b1\u00ad\3\2\2\2\u00b2\u00b5\3\2\2\2\u00b3\u00b1\3\2\2\2\u00b3"+
		"\u00b4\3\2\2\2\u00b4%\3\2\2\2\u00b5\u00b3\3\2\2\2\u00b6\u00b7\5(\25\2"+
		"\u00b7\u00b8\5*\26\2\u00b8\'\3\2\2\2\u00b9\u00bc\5\64\33\2\u00ba\u00bc"+
		"\5\24\13\2\u00bb\u00b9\3\2\2\2\u00bb\u00ba\3\2\2\2\u00bc)\3\2\2\2\u00bd"+
		"\u00c2\5,\27\2\u00be\u00bf\7\20\2\2\u00bf\u00c1\5,\27\2\u00c0\u00be\3"+
		"\2\2\2\u00c1\u00c4\3\2\2\2\u00c2\u00c0\3\2\2\2\u00c2\u00c3\3\2\2\2\u00c3"+
		"+\3\2\2\2\u00c4\u00c2\3\2\2\2\u00c5\u00ca\5\36\20\2\u00c6\u00ca\5\62\32"+
		"\2\u00c7\u00ca\5\"\22\2\u00c8\u00ca\7\23\2\2\u00c9\u00c5\3\2\2\2\u00c9"+
		"\u00c6\3\2\2\2\u00c9\u00c7\3\2\2\2\u00c9\u00c8\3\2\2\2\u00ca-\3\2\2\2"+
		"\u00cb\u00cc\7\35\2\2\u00cc\u00cd\7\37\2\2\u00cd/\3\2\2\2\u00ce\u00cf"+
		"\7\35\2\2\u00cf\61\3\2\2\2\u00d0\u00d6\t\3\2\2\u00d1\u00d4\7\7\2\2\u00d2"+
		"\u00d5\7\34\2\2\u00d3\u00d5\5.\30\2\u00d4\u00d2\3\2\2\2\u00d4\u00d3\3"+
		"\2\2\2\u00d5\u00d7\3\2\2\2\u00d6\u00d1\3\2\2\2\u00d6\u00d7\3\2\2\2\u00d7"+
		"\63\3\2\2\2\u00d8\u00d9\7\31\2\2\u00d9\65\3\2\2\2\u00da\u00db\7\27\2\2"+
		"\u00db\67\3\2\2\2\36:AHRY\\bdlnuw~\u0081\u0089\u008c\u0091\u0096\u0099"+
		"\u009f\u00a4\u00af\u00b3\u00bb\u00c2\u00c9\u00d4\u00d6";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}