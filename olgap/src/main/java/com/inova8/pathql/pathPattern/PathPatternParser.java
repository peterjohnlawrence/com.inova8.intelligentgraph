// Generated from PathPattern.g4 by ANTLR 4.9.3

package com.inova8.pathql.pathPattern;

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
	static { RuntimeMetaData.checkVersion("4.9.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, KEY=15, INTEGER=16, BINDVARIABLE=17, 
		INVERSE=18, REIFIER=19, DEREIFIER=20, RDFTYPE=21, ANYPREDICATE=22, OPERATOR=23, 
		DQLITERAL=24, SQLITERAL=25, IRI_REF=26, PNAME_NS=27, VARNAME=28, PN_LOCAL=29, 
		WS=30;
	public static final int
		RULE_queryString = 0, RULE_queryOptions = 1, RULE_queryOption = 2, RULE_type = 3, 
		RULE_pathPattern = 4, RULE_binding = 5, RULE_pathPatterns = 6, RULE_cardinality = 7, 
		RULE_negation = 8, RULE_pathEltOrInverse = 9, RULE_predicate = 10, RULE_anyPredicate = 11, 
		RULE_reifiedPredicate = 12, RULE_predicateRef = 13, RULE_iriRef = 14, 
		RULE_dereifier = 15, RULE_factFilterPattern = 16, RULE_propertyListNotEmpty = 17, 
		RULE_verbObjectList = 18, RULE_verb = 19, RULE_objectList = 20, RULE_object = 21, 
		RULE_qname = 22, RULE_pname_ns = 23, RULE_literal = 24, RULE_operator = 25, 
		RULE_rdfType = 26;
	private static String[] makeRuleNames() {
		return new String[] {
			"queryString", "queryOptions", "queryOption", "type", "pathPattern", 
			"binding", "pathPatterns", "cardinality", "negation", "pathEltOrInverse", 
			"predicate", "anyPredicate", "reifiedPredicate", "predicateRef", "iriRef", 
			"dereifier", "factFilterPattern", "propertyListNotEmpty", "verbObjectList", 
			"verb", "objectList", "object", "qname", "pname_ns", "literal", "operator", 
			"rdfType"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'='", "'^^'", "'/'", "'>'", "'|'", "'('", "')'", "'{'", "','", 
			"'}'", "'!'", "'['", "']'", "';'", null, null, null, "'^'", "'@'", "'#'", 
			"'a'", "'*'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, "KEY", "INTEGER", "BINDVARIABLE", "INVERSE", "REIFIER", 
			"DEREIFIER", "RDFTYPE", "ANYPREDICATE", "OPERATOR", "DQLITERAL", "SQLITERAL", 
			"IRI_REF", "PNAME_NS", "VARNAME", "PN_LOCAL", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "PathPattern.g4"; }

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
		public PathPatternContext pathPattern() {
			return getRuleContext(PathPatternContext.class,0);
		}
		public TerminalNode EOF() { return getToken(PathPatternParser.EOF, 0); }
		public QueryOptionsContext queryOptions() {
			return getRuleContext(QueryOptionsContext.class,0);
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
			setState(54);
			pathPattern();
			setState(56);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KEY) {
				{
				setState(55);
				queryOptions();
				}
			}

			setState(58);
			match(EOF);
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
				setState(60);
				queryOption();
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
			setState(65);
			match(KEY);
			setState(66);
			match(T__0);
			setState(67);
			literal();
			setState(70);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(68);
				match(T__1);
				setState(69);
				type();
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
			setState(72);
			qname();
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
		public BindingContext binding() {
			return getRuleContext(BindingContext.class,0);
		}
		public PathPatternsContext pathPatterns() {
			return getRuleContext(PathPatternsContext.class,0);
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
			setState(79);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__11:
				_localctx = new BoundPatternContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(74);
				binding();
				setState(75);
				_la = _input.LA(1);
				if ( !(_la==T__2 || _la==T__3) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(76);
				pathPatterns(0);
				}
				break;
			case T__5:
			case T__10:
			case INVERSE:
			case REIFIER:
			case RDFTYPE:
			case ANYPREDICATE:
			case IRI_REF:
			case PNAME_NS:
				_localctx = new PathOnlyPatternContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(78);
				pathPatterns(0);
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
			setState(81);
			factFilterPattern();
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
		public PathEltOrInverseContext pathEltOrInverse() {
			return getRuleContext(PathEltOrInverseContext.class,0);
		}
		public CardinalityContext cardinality() {
			return getRuleContext(CardinalityContext.class,0);
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
		public PathPatternsContext pathPatterns() {
			return getRuleContext(PathPatternsContext.class,0);
		}
		public NegationContext negation() {
			return getRuleContext(NegationContext.class,0);
		}
		public CardinalityContext cardinality() {
			return getRuleContext(CardinalityContext.class,0);
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
			setState(97);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				{
				_localctx = new PathContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(84);
				pathEltOrInverse();
				setState(86);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
				case 1:
					{
					setState(85);
					cardinality();
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
				setState(89);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__10) {
					{
					setState(88);
					negation();
					}
				}

				setState(91);
				match(T__5);
				setState(92);
				pathPatterns(0);
				setState(93);
				match(T__6);
				setState(95);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
				case 1:
					{
					setState(94);
					cardinality();
					}
					break;
				}
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(107);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(105);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
					case 1:
						{
						_localctx = new PathAlternativeContext(new PathPatternsContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_pathPatterns);
						setState(99);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(100);
						match(T__4);
						setState(101);
						pathPatterns(4);
						}
						break;
					case 2:
						{
						_localctx = new PathSequenceContext(new PathPatternsContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_pathPatterns);
						setState(102);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(103);
						_la = _input.LA(1);
						if ( !(_la==T__2 || _la==T__3) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(104);
						pathPatterns(3);
						}
						break;
					}
					} 
				}
				setState(109);
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
			setState(110);
			match(T__7);
			setState(111);
			match(INTEGER);
			setState(116);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__8) {
				{
				setState(112);
				match(T__8);
				setState(114);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==INTEGER) {
					{
					setState(113);
					match(INTEGER);
					}
				}

				}
			}

			setState(118);
			match(T__9);
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
			setState(120);
			match(T__10);
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
		public PredicateContext predicate() {
			return getRuleContext(PredicateContext.class,0);
		}
		public NegationContext negation() {
			return getRuleContext(NegationContext.class,0);
		}
		public TerminalNode INVERSE() { return getToken(PathPatternParser.INVERSE, 0); }
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
			setState(123);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__10) {
				{
				setState(122);
				negation();
				}
			}

			setState(126);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==INVERSE) {
				{
				setState(125);
				match(INVERSE);
				}
			}

			setState(128);
			predicate();
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
		public ReifiedPredicateContext reifiedPredicate() {
			return getRuleContext(ReifiedPredicateContext.class,0);
		}
		public PredicateRefContext predicateRef() {
			return getRuleContext(PredicateRefContext.class,0);
		}
		public RdfTypeContext rdfType() {
			return getRuleContext(RdfTypeContext.class,0);
		}
		public AnyPredicateContext anyPredicate() {
			return getRuleContext(AnyPredicateContext.class,0);
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
			setState(134);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				{
				setState(130);
				reifiedPredicate();
				}
				break;
			case 2:
				{
				setState(131);
				predicateRef();
				}
				break;
			case 3:
				{
				setState(132);
				rdfType();
				}
				break;
			case 4:
				{
				setState(133);
				anyPredicate();
				}
				break;
			}
			setState(137);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
			case 1:
				{
				setState(136);
				factFilterPattern();
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
			setState(139);
			match(ANYPREDICATE);
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
		public TerminalNode REIFIER() { return getToken(PathPatternParser.REIFIER, 0); }
		public PredicateRefContext predicateRef() {
			return getRuleContext(PredicateRefContext.class,0);
		}
		public IriRefContext iriRef() {
			return getRuleContext(IriRefContext.class,0);
		}
		public FactFilterPatternContext factFilterPattern() {
			return getRuleContext(FactFilterPatternContext.class,0);
		}
		public DereifierContext dereifier() {
			return getRuleContext(DereifierContext.class,0);
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
			setState(142);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IRI_REF || _la==PNAME_NS) {
				{
				setState(141);
				iriRef();
				}
			}

			setState(144);
			match(REIFIER);
			setState(145);
			predicateRef();
			setState(147);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
			case 1:
				{
				setState(146);
				factFilterPattern();
				}
				break;
			}
			setState(150);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
			case 1:
				{
				setState(149);
				dereifier();
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
		public TerminalNode IRI_REF() { return getToken(PathPatternParser.IRI_REF, 0); }
		public RdfTypeContext rdfType() {
			return getRuleContext(RdfTypeContext.class,0);
		}
		public QnameContext qname() {
			return getRuleContext(QnameContext.class,0);
		}
		public Pname_nsContext pname_ns() {
			return getRuleContext(Pname_nsContext.class,0);
		}
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
			setState(156);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(152);
				match(IRI_REF);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(153);
				rdfType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(154);
				qname();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(155);
				pname_ns();
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
		public TerminalNode IRI_REF() { return getToken(PathPatternParser.IRI_REF, 0); }
		public QnameContext qname() {
			return getRuleContext(QnameContext.class,0);
		}
		public Pname_nsContext pname_ns() {
			return getRuleContext(Pname_nsContext.class,0);
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
			setState(161);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(158);
				match(IRI_REF);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(159);
				qname();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(160);
				pname_ns();
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
			setState(163);
			match(DEREIFIER);
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
			setState(165);
			match(T__11);
			setState(166);
			propertyListNotEmpty();
			setState(167);
			match(T__12);
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
			setState(169);
			verbObjectList();
			setState(176);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__13) {
				{
				{
				setState(170);
				match(T__13);
				setState(172);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__10) | (1L << INVERSE) | (1L << REIFIER) | (1L << RDFTYPE) | (1L << ANYPREDICATE) | (1L << OPERATOR) | (1L << IRI_REF) | (1L << PNAME_NS))) != 0)) {
					{
					setState(171);
					verbObjectList();
					}
				}

				}
				}
				setState(178);
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
		public VerbContext verb() {
			return getRuleContext(VerbContext.class,0);
		}
		public ObjectListContext objectList() {
			return getRuleContext(ObjectListContext.class,0);
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
			setState(179);
			verb();
			setState(180);
			objectList();
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
			setState(184);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case OPERATOR:
				enterOuterAlt(_localctx, 1);
				{
				setState(182);
				operator();
				}
				break;
			case T__10:
			case INVERSE:
			case REIFIER:
			case RDFTYPE:
			case ANYPREDICATE:
			case IRI_REF:
			case PNAME_NS:
				enterOuterAlt(_localctx, 2);
				{
				setState(183);
				pathEltOrInverse();
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
		public List<ObjectContext> object() {
			return getRuleContexts(ObjectContext.class);
		}
		public ObjectContext object(int i) {
			return getRuleContext(ObjectContext.class,i);
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
			setState(186);
			object();
			setState(191);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__8) {
				{
				{
				setState(187);
				match(T__8);
				setState(188);
				object();
				}
				}
				setState(193);
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
		public IriRefContext iriRef() {
			return getRuleContext(IriRefContext.class,0);
		}
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public FactFilterPatternContext factFilterPattern() {
			return getRuleContext(FactFilterPatternContext.class,0);
		}
		public TerminalNode BINDVARIABLE() { return getToken(PathPatternParser.BINDVARIABLE, 0); }
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
			setState(198);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IRI_REF:
			case PNAME_NS:
				enterOuterAlt(_localctx, 1);
				{
				setState(194);
				iriRef();
				}
				break;
			case DQLITERAL:
			case SQLITERAL:
				enterOuterAlt(_localctx, 2);
				{
				setState(195);
				literal();
				}
				break;
			case T__11:
				enterOuterAlt(_localctx, 3);
				{
				setState(196);
				factFilterPattern();
				}
				break;
			case BINDVARIABLE:
				enterOuterAlt(_localctx, 4);
				{
				setState(197);
				match(BINDVARIABLE);
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
		public TerminalNode PNAME_NS() { return getToken(PathPatternParser.PNAME_NS, 0); }
		public TerminalNode PN_LOCAL() { return getToken(PathPatternParser.PN_LOCAL, 0); }
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
			setState(200);
			match(PNAME_NS);
			setState(201);
			match(PN_LOCAL);
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
			setState(203);
			match(PNAME_NS);
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
		public TerminalNode DQLITERAL() { return getToken(PathPatternParser.DQLITERAL, 0); }
		public TerminalNode SQLITERAL() { return getToken(PathPatternParser.SQLITERAL, 0); }
		public TerminalNode IRI_REF() { return getToken(PathPatternParser.IRI_REF, 0); }
		public QnameContext qname() {
			return getRuleContext(QnameContext.class,0);
		}
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
			setState(205);
			_la = _input.LA(1);
			if ( !(_la==DQLITERAL || _la==SQLITERAL) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			setState(211);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
			case 1:
				{
				setState(206);
				match(T__1);
				setState(209);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case IRI_REF:
					{
					setState(207);
					match(IRI_REF);
					}
					break;
				case PNAME_NS:
					{
					setState(208);
					qname();
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
			setState(213);
			match(OPERATOR);
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
			setState(215);
			match(RDFTYPE);
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
		case 6:
			return pathPatterns_sempred((PathPatternsContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean pathPatterns_sempred(PathPatternsContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 3);
		case 1:
			return precpred(_ctx, 2);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3 \u00dc\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\3\2\3\2\5\2;\n\2\3\2\3\2\3\3\6\3@\n\3\r"+
		"\3\16\3A\3\4\3\4\3\4\3\4\3\4\5\4I\n\4\3\5\3\5\3\6\3\6\3\6\3\6\3\6\5\6"+
		"R\n\6\3\7\3\7\3\b\3\b\3\b\5\bY\n\b\3\b\5\b\\\n\b\3\b\3\b\3\b\3\b\5\bb"+
		"\n\b\5\bd\n\b\3\b\3\b\3\b\3\b\3\b\3\b\7\bl\n\b\f\b\16\bo\13\b\3\t\3\t"+
		"\3\t\3\t\5\tu\n\t\5\tw\n\t\3\t\3\t\3\n\3\n\3\13\5\13~\n\13\3\13\5\13\u0081"+
		"\n\13\3\13\3\13\3\f\3\f\3\f\3\f\5\f\u0089\n\f\3\f\5\f\u008c\n\f\3\r\3"+
		"\r\3\16\5\16\u0091\n\16\3\16\3\16\3\16\5\16\u0096\n\16\3\16\5\16\u0099"+
		"\n\16\3\17\3\17\3\17\3\17\5\17\u009f\n\17\3\20\3\20\3\20\5\20\u00a4\n"+
		"\20\3\21\3\21\3\22\3\22\3\22\3\22\3\23\3\23\3\23\5\23\u00af\n\23\7\23"+
		"\u00b1\n\23\f\23\16\23\u00b4\13\23\3\24\3\24\3\24\3\25\3\25\5\25\u00bb"+
		"\n\25\3\26\3\26\3\26\7\26\u00c0\n\26\f\26\16\26\u00c3\13\26\3\27\3\27"+
		"\3\27\3\27\5\27\u00c9\n\27\3\30\3\30\3\30\3\31\3\31\3\32\3\32\3\32\3\32"+
		"\5\32\u00d4\n\32\5\32\u00d6\n\32\3\33\3\33\3\34\3\34\3\34\2\3\16\35\2"+
		"\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\66\2\4\3\2\5\6"+
		"\3\2\32\33\2\u00e3\28\3\2\2\2\4?\3\2\2\2\6C\3\2\2\2\bJ\3\2\2\2\nQ\3\2"+
		"\2\2\fS\3\2\2\2\16c\3\2\2\2\20p\3\2\2\2\22z\3\2\2\2\24}\3\2\2\2\26\u0088"+
		"\3\2\2\2\30\u008d\3\2\2\2\32\u0090\3\2\2\2\34\u009e\3\2\2\2\36\u00a3\3"+
		"\2\2\2 \u00a5\3\2\2\2\"\u00a7\3\2\2\2$\u00ab\3\2\2\2&\u00b5\3\2\2\2(\u00ba"+
		"\3\2\2\2*\u00bc\3\2\2\2,\u00c8\3\2\2\2.\u00ca\3\2\2\2\60\u00cd\3\2\2\2"+
		"\62\u00cf\3\2\2\2\64\u00d7\3\2\2\2\66\u00d9\3\2\2\28:\5\n\6\29;\5\4\3"+
		"\2:9\3\2\2\2:;\3\2\2\2;<\3\2\2\2<=\7\2\2\3=\3\3\2\2\2>@\5\6\4\2?>\3\2"+
		"\2\2@A\3\2\2\2A?\3\2\2\2AB\3\2\2\2B\5\3\2\2\2CD\7\21\2\2DE\7\3\2\2EH\5"+
		"\62\32\2FG\7\4\2\2GI\5\b\5\2HF\3\2\2\2HI\3\2\2\2I\7\3\2\2\2JK\5.\30\2"+
		"K\t\3\2\2\2LM\5\f\7\2MN\t\2\2\2NO\5\16\b\2OR\3\2\2\2PR\5\16\b\2QL\3\2"+
		"\2\2QP\3\2\2\2R\13\3\2\2\2ST\5\"\22\2T\r\3\2\2\2UV\b\b\1\2VX\5\24\13\2"+
		"WY\5\20\t\2XW\3\2\2\2XY\3\2\2\2Yd\3\2\2\2Z\\\5\22\n\2[Z\3\2\2\2[\\\3\2"+
		"\2\2\\]\3\2\2\2]^\7\b\2\2^_\5\16\b\2_a\7\t\2\2`b\5\20\t\2a`\3\2\2\2ab"+
		"\3\2\2\2bd\3\2\2\2cU\3\2\2\2c[\3\2\2\2dm\3\2\2\2ef\f\5\2\2fg\7\7\2\2g"+
		"l\5\16\b\6hi\f\4\2\2ij\t\2\2\2jl\5\16\b\5ke\3\2\2\2kh\3\2\2\2lo\3\2\2"+
		"\2mk\3\2\2\2mn\3\2\2\2n\17\3\2\2\2om\3\2\2\2pq\7\n\2\2qv\7\22\2\2rt\7"+
		"\13\2\2su\7\22\2\2ts\3\2\2\2tu\3\2\2\2uw\3\2\2\2vr\3\2\2\2vw\3\2\2\2w"+
		"x\3\2\2\2xy\7\f\2\2y\21\3\2\2\2z{\7\r\2\2{\23\3\2\2\2|~\5\22\n\2}|\3\2"+
		"\2\2}~\3\2\2\2~\u0080\3\2\2\2\177\u0081\7\24\2\2\u0080\177\3\2\2\2\u0080"+
		"\u0081\3\2\2\2\u0081\u0082\3\2\2\2\u0082\u0083\5\26\f\2\u0083\25\3\2\2"+
		"\2\u0084\u0089\5\32\16\2\u0085\u0089\5\34\17\2\u0086\u0089\5\66\34\2\u0087"+
		"\u0089\5\30\r\2\u0088\u0084\3\2\2\2\u0088\u0085\3\2\2\2\u0088\u0086\3"+
		"\2\2\2\u0088\u0087\3\2\2\2\u0089\u008b\3\2\2\2\u008a\u008c\5\"\22\2\u008b"+
		"\u008a\3\2\2\2\u008b\u008c\3\2\2\2\u008c\27\3\2\2\2\u008d\u008e\7\30\2"+
		"\2\u008e\31\3\2\2\2\u008f\u0091\5\36\20\2\u0090\u008f\3\2\2\2\u0090\u0091"+
		"\3\2\2\2\u0091\u0092\3\2\2\2\u0092\u0093\7\25\2\2\u0093\u0095\5\34\17"+
		"\2\u0094\u0096\5\"\22\2\u0095\u0094\3\2\2\2\u0095\u0096\3\2\2\2\u0096"+
		"\u0098\3\2\2\2\u0097\u0099\5 \21\2\u0098\u0097\3\2\2\2\u0098\u0099\3\2"+
		"\2\2\u0099\33\3\2\2\2\u009a\u009f\7\34\2\2\u009b\u009f\5\66\34\2\u009c"+
		"\u009f\5.\30\2\u009d\u009f\5\60\31\2\u009e\u009a\3\2\2\2\u009e\u009b\3"+
		"\2\2\2\u009e\u009c\3\2\2\2\u009e\u009d\3\2\2\2\u009f\35\3\2\2\2\u00a0"+
		"\u00a4\7\34\2\2\u00a1\u00a4\5.\30\2\u00a2\u00a4\5\60\31\2\u00a3\u00a0"+
		"\3\2\2\2\u00a3\u00a1\3\2\2\2\u00a3\u00a2\3\2\2\2\u00a4\37\3\2\2\2\u00a5"+
		"\u00a6\7\26\2\2\u00a6!\3\2\2\2\u00a7\u00a8\7\16\2\2\u00a8\u00a9\5$\23"+
		"\2\u00a9\u00aa\7\17\2\2\u00aa#\3\2\2\2\u00ab\u00b2\5&\24\2\u00ac\u00ae"+
		"\7\20\2\2\u00ad\u00af\5&\24\2\u00ae\u00ad\3\2\2\2\u00ae\u00af\3\2\2\2"+
		"\u00af\u00b1\3\2\2\2\u00b0\u00ac\3\2\2\2\u00b1\u00b4\3\2\2\2\u00b2\u00b0"+
		"\3\2\2\2\u00b2\u00b3\3\2\2\2\u00b3%\3\2\2\2\u00b4\u00b2\3\2\2\2\u00b5"+
		"\u00b6\5(\25\2\u00b6\u00b7\5*\26\2\u00b7\'\3\2\2\2\u00b8\u00bb\5\64\33"+
		"\2\u00b9\u00bb\5\24\13\2\u00ba\u00b8\3\2\2\2\u00ba\u00b9\3\2\2\2\u00bb"+
		")\3\2\2\2\u00bc\u00c1\5,\27\2\u00bd\u00be\7\13\2\2\u00be\u00c0\5,\27\2"+
		"\u00bf\u00bd\3\2\2\2\u00c0\u00c3\3\2\2\2\u00c1\u00bf\3\2\2\2\u00c1\u00c2"+
		"\3\2\2\2\u00c2+\3\2\2\2\u00c3\u00c1\3\2\2\2\u00c4\u00c9\5\36\20\2\u00c5"+
		"\u00c9\5\62\32\2\u00c6\u00c9\5\"\22\2\u00c7\u00c9\7\23\2\2\u00c8\u00c4"+
		"\3\2\2\2\u00c8\u00c5\3\2\2\2\u00c8\u00c6\3\2\2\2\u00c8\u00c7\3\2\2\2\u00c9"+
		"-\3\2\2\2\u00ca\u00cb\7\35\2\2\u00cb\u00cc\7\37\2\2\u00cc/\3\2\2\2\u00cd"+
		"\u00ce\7\35\2\2\u00ce\61\3\2\2\2\u00cf\u00d5\t\3\2\2\u00d0\u00d3\7\4\2"+
		"\2\u00d1\u00d4\7\34\2\2\u00d2\u00d4\5.\30\2\u00d3\u00d1\3\2\2\2\u00d3"+
		"\u00d2\3\2\2\2\u00d4\u00d6\3\2\2\2\u00d5\u00d0\3\2\2\2\u00d5\u00d6\3\2"+
		"\2\2\u00d6\63\3\2\2\2\u00d7\u00d8\7\31\2\2\u00d8\65\3\2\2\2\u00d9\u00da"+
		"\7\27\2\2\u00da\67\3\2\2\2\36:AHQX[ackmtv}\u0080\u0088\u008b\u0090\u0095"+
		"\u0098\u009e\u00a3\u00ae\u00b2\u00ba\u00c1\u00c8\u00d3\u00d5";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}