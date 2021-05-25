/*
 * inova8 2020
 */
package PathPattern;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

/**
 * The Class PathPatternParser.
 */
@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class PathPatternParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.9", RuntimeMetaData.VERSION); }

	/** The Constant _decisionToDFA. */
	protected static final DFA[] _decisionToDFA;
	
	/** The Constant _sharedContextCache. */
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	
	/** The Constant WS. */
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, INTEGER=13, INVERSE=14, REIFIER=15, DEREIFIER=16, 
		RDFTYPE=17, ANYPREDICATE=18, OPERATOR=19, DQLITERAL=20, SQLITERAL=21, 
		IRI_REF=22, PNAME_NS=23, VARNAME=24, PN_LOCAL=25, WS=26;
	
	/** The Constant RULE_rdfType. */
	public static final int
		RULE_pathPattern = 0, RULE_binding = 1, RULE_pathPatterns = 2, RULE_cardinality = 3, 
		RULE_negation = 4, RULE_pathEltOrInverse = 5, RULE_predicate = 6, RULE_anyPredicate = 7, 
		RULE_reifiedPredicate = 8, RULE_predicateRef = 9, RULE_iriRef = 10, RULE_dereifier = 11, 
		RULE_factFilterPattern = 12, RULE_propertyListNotEmpty = 13, RULE_verbObjectList = 14, 
		RULE_verb = 15, RULE_objectList = 16, RULE_object = 17, RULE_qname = 18, 
		RULE_pname_ns = 19, RULE_literal = 20, RULE_operator = 21, RULE_rdfType = 22;
	
	/**
	 * Make rule names.
	 *
	 * @return the string[]
	 */
	private static String[] makeRuleNames() {
		return new String[] {
			"pathPattern", "binding", "pathPatterns", "cardinality", "negation", 
			"pathEltOrInverse", "predicate", "anyPredicate", "reifiedPredicate", 
			"predicateRef", "iriRef", "dereifier", "factFilterPattern", "propertyListNotEmpty", 
			"verbObjectList", "verb", "objectList", "object", "qname", "pname_ns", 
			"literal", "operator", "rdfType"
		};
	}
	
	/** The Constant ruleNames. */
	public static final String[] ruleNames = makeRuleNames();

	/**
	 * Make literal names.
	 *
	 * @return the string[]
	 */
	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'/'", "'>'", "'|'", "'('", "')'", "'{'", "','", "'}'", "'!'", 
			"'['", "']'", "';'", null, "'^'", "'@'", "'#'", "'a'", "'*'"
		};
	}
	
	/** The Constant _LITERAL_NAMES. */
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	
	/**
	 * Make symbolic names.
	 *
	 * @return the string[]
	 */
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, "INTEGER", "INVERSE", "REIFIER", "DEREIFIER", "RDFTYPE", "ANYPREDICATE", 
			"OPERATOR", "DQLITERAL", "SQLITERAL", "IRI_REF", "PNAME_NS", "VARNAME", 
			"PN_LOCAL", "WS"
		};
	}
	
	/** The Constant _SYMBOLIC_NAMES. */
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	
	/** The Constant VOCABULARY. */
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * The Constant tokenNames.
	 *
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

	/**
	 * Gets the token names.
	 *
	 * @return the token names
	 */
	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	/**
	 * Gets the vocabulary.
	 *
	 * @return the vocabulary
	 */
	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	/**
	 * Gets the grammar file name.
	 *
	 * @return the grammar file name
	 */
	@Override
	public String getGrammarFileName() { return "PathPattern.g4"; }

	/**
	 * Gets the rule names.
	 *
	 * @return the rule names
	 */
	@Override
	public String[] getRuleNames() { return ruleNames; }

	/**
	 * Gets the serialized ATN.
	 *
	 * @return the serialized ATN
	 */
	@Override
	public String getSerializedATN() { return _serializedATN; }

	/**
	 * Gets the atn.
	 *
	 * @return the atn
	 */
	@Override
	public ATN getATN() { return _ATN; }

	/**
	 * Instantiates a new path pattern parser.
	 *
	 * @param input the input
	 */
	public PathPatternParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	/**
	 * The Class PathPatternContext.
	 */
	public static class PathPatternContext extends ParserRuleContext {
		
		/**
		 * Instantiates a new path pattern context.
		 *
		 * @param parent the parent
		 * @param invokingState the invoking state
		 */
		public PathPatternContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		
		/**
		 * Gets the rule index.
		 *
		 * @return the rule index
		 */
		@Override public int getRuleIndex() { return RULE_pathPattern; }
	 
		/**
		 * Instantiates a new path pattern context.
		 */
		public PathPatternContext() { }
		
		/**
		 * Copy from.
		 *
		 * @param ctx the ctx
		 */
		public void copyFrom(PathPatternContext ctx) {
			super.copyFrom(ctx);
		}
	}
	
	/**
	 * The Class BoundPatternContext.
	 */
	public static class BoundPatternContext extends PathPatternContext {
		
		/**
		 * Binding.
		 *
		 * @return the binding context
		 */
		public BindingContext binding() {
			return getRuleContext(BindingContext.class,0);
		}
		
		/**
		 * Path patterns.
		 *
		 * @return the path patterns context
		 */
		public PathPatternsContext pathPatterns() {
			return getRuleContext(PathPatternsContext.class,0);
		}
		
		/**
		 * Eof.
		 *
		 * @return the terminal node
		 */
		public TerminalNode EOF() { return getToken(PathPatternParser.EOF, 0); }
		
		/**
		 * Instantiates a new bound pattern context.
		 *
		 * @param ctx the ctx
		 */
		public BoundPatternContext(PathPatternContext ctx) { copyFrom(ctx); }
		
		/**
		 * Enter rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterBoundPattern(this);
		}
		
		/**
		 * Exit rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitBoundPattern(this);
		}
		
		/**
		 * Accept.
		 *
		 * @param <T> the generic type
		 * @param visitor the visitor
		 * @return the t
		 */
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitBoundPattern(this);
			else return visitor.visitChildren(this);
		}
	}
	
	/**
	 * The Class MatchOnlyPatternContext.
	 */
	public static class MatchOnlyPatternContext extends PathPatternContext {
		
		/**
		 * Binding.
		 *
		 * @return the binding context
		 */
		public BindingContext binding() {
			return getRuleContext(BindingContext.class,0);
		}
		
		/**
		 * Eof.
		 *
		 * @return the terminal node
		 */
		public TerminalNode EOF() { return getToken(PathPatternParser.EOF, 0); }
		
		/**
		 * Instantiates a new match only pattern context.
		 *
		 * @param ctx the ctx
		 */
		public MatchOnlyPatternContext(PathPatternContext ctx) { copyFrom(ctx); }
		
		/**
		 * Enter rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterMatchOnlyPattern(this);
		}
		
		/**
		 * Exit rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitMatchOnlyPattern(this);
		}
		
		/**
		 * Accept.
		 *
		 * @param <T> the generic type
		 * @param visitor the visitor
		 * @return the t
		 */
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitMatchOnlyPattern(this);
			else return visitor.visitChildren(this);
		}
	}
	
	/**
	 * The Class PathOnlyPatternContext.
	 */
	public static class PathOnlyPatternContext extends PathPatternContext {
		
		/**
		 * Path patterns.
		 *
		 * @return the path patterns context
		 */
		public PathPatternsContext pathPatterns() {
			return getRuleContext(PathPatternsContext.class,0);
		}
		
		/**
		 * Eof.
		 *
		 * @return the terminal node
		 */
		public TerminalNode EOF() { return getToken(PathPatternParser.EOF, 0); }
		
		/**
		 * Instantiates a new path only pattern context.
		 *
		 * @param ctx the ctx
		 */
		public PathOnlyPatternContext(PathPatternContext ctx) { copyFrom(ctx); }
		
		/**
		 * Enter rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterPathOnlyPattern(this);
		}
		
		/**
		 * Exit rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitPathOnlyPattern(this);
		}
		
		/**
		 * Accept.
		 *
		 * @param <T> the generic type
		 * @param visitor the visitor
		 * @return the t
		 */
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitPathOnlyPattern(this);
			else return visitor.visitChildren(this);
		}
	}

	/**
	 * Path pattern.
	 *
	 * @return the path pattern context
	 * @throws RecognitionException the recognition exception
	 */
	public final PathPatternContext pathPattern() throws RecognitionException {
		PathPatternContext _localctx = new PathPatternContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_pathPattern);
		int _la;
		try {
			setState(57);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				_localctx = new BoundPatternContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(46);
				binding();
				setState(47);
				_la = _input.LA(1);
				if ( !(_la==T__0 || _la==T__1) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(48);
				pathPatterns(0);
				setState(49);
				match(EOF);
				}
				break;
			case 2:
				_localctx = new MatchOnlyPatternContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(51);
				binding();
				setState(52);
				match(EOF);
				}
				break;
			case 3:
				_localctx = new PathOnlyPatternContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(54);
				pathPatterns(0);
				setState(55);
				match(EOF);
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

	/**
	 * The Class BindingContext.
	 */
	public static class BindingContext extends ParserRuleContext {
		
		/**
		 * Fact filter pattern.
		 *
		 * @return the fact filter pattern context
		 */
		public FactFilterPatternContext factFilterPattern() {
			return getRuleContext(FactFilterPatternContext.class,0);
		}
		
		/**
		 * Instantiates a new binding context.
		 *
		 * @param parent the parent
		 * @param invokingState the invoking state
		 */
		public BindingContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		
		/**
		 * Gets the rule index.
		 *
		 * @return the rule index
		 */
		@Override public int getRuleIndex() { return RULE_binding; }
		
		/**
		 * Enter rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterBinding(this);
		}
		
		/**
		 * Exit rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitBinding(this);
		}
		
		/**
		 * Accept.
		 *
		 * @param <T> the generic type
		 * @param visitor the visitor
		 * @return the t
		 */
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitBinding(this);
			else return visitor.visitChildren(this);
		}
	}

	/**
	 * Binding.
	 *
	 * @return the binding context
	 * @throws RecognitionException the recognition exception
	 */
	public final BindingContext binding() throws RecognitionException {
		BindingContext _localctx = new BindingContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_binding);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(59);
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

	/**
	 * The Class PathPatternsContext.
	 */
	public static class PathPatternsContext extends ParserRuleContext {
		
		/**
		 * Instantiates a new path patterns context.
		 *
		 * @param parent the parent
		 * @param invokingState the invoking state
		 */
		public PathPatternsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		
		/**
		 * Gets the rule index.
		 *
		 * @return the rule index
		 */
		@Override public int getRuleIndex() { return RULE_pathPatterns; }
	 
		/**
		 * Instantiates a new path patterns context.
		 */
		public PathPatternsContext() { }
		
		/**
		 * Copy from.
		 *
		 * @param ctx the ctx
		 */
		public void copyFrom(PathPatternsContext ctx) {
			super.copyFrom(ctx);
		}
	}
	
	/**
	 * The Class PathContext.
	 */
	public static class PathContext extends PathPatternsContext {
		
		/**
		 * Path elt or inverse.
		 *
		 * @return the path elt or inverse context
		 */
		public PathEltOrInverseContext pathEltOrInverse() {
			return getRuleContext(PathEltOrInverseContext.class,0);
		}
		
		/**
		 * Cardinality.
		 *
		 * @return the cardinality context
		 */
		public CardinalityContext cardinality() {
			return getRuleContext(CardinalityContext.class,0);
		}
		
		/**
		 * Instantiates a new path context.
		 *
		 * @param ctx the ctx
		 */
		public PathContext(PathPatternsContext ctx) { copyFrom(ctx); }
		
		/**
		 * Enter rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterPath(this);
		}
		
		/**
		 * Exit rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitPath(this);
		}
		
		/**
		 * Accept.
		 *
		 * @param <T> the generic type
		 * @param visitor the visitor
		 * @return the t
		 */
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitPath(this);
			else return visitor.visitChildren(this);
		}
	}
	
	/**
	 * The Class PathAlternativeContext.
	 */
	public static class PathAlternativeContext extends PathPatternsContext {
		
		/**
		 * Path patterns.
		 *
		 * @return the list
		 */
		public List<PathPatternsContext> pathPatterns() {
			return getRuleContexts(PathPatternsContext.class);
		}
		
		/**
		 * Path patterns.
		 *
		 * @param i the i
		 * @return the path patterns context
		 */
		public PathPatternsContext pathPatterns(int i) {
			return getRuleContext(PathPatternsContext.class,i);
		}
		
		/**
		 * Instantiates a new path alternative context.
		 *
		 * @param ctx the ctx
		 */
		public PathAlternativeContext(PathPatternsContext ctx) { copyFrom(ctx); }
		
		/**
		 * Enter rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterPathAlternative(this);
		}
		
		/**
		 * Exit rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitPathAlternative(this);
		}
		
		/**
		 * Accept.
		 *
		 * @param <T> the generic type
		 * @param visitor the visitor
		 * @return the t
		 */
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitPathAlternative(this);
			else return visitor.visitChildren(this);
		}
	}
	
	/**
	 * The Class PathParenthesesContext.
	 */
	public static class PathParenthesesContext extends PathPatternsContext {
		
		/**
		 * Path patterns.
		 *
		 * @return the path patterns context
		 */
		public PathPatternsContext pathPatterns() {
			return getRuleContext(PathPatternsContext.class,0);
		}
		
		/**
		 * Negation.
		 *
		 * @return the negation context
		 */
		public NegationContext negation() {
			return getRuleContext(NegationContext.class,0);
		}
		
		/**
		 * Cardinality.
		 *
		 * @return the cardinality context
		 */
		public CardinalityContext cardinality() {
			return getRuleContext(CardinalityContext.class,0);
		}
		
		/**
		 * Instantiates a new path parentheses context.
		 *
		 * @param ctx the ctx
		 */
		public PathParenthesesContext(PathPatternsContext ctx) { copyFrom(ctx); }
		
		/**
		 * Enter rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterPathParentheses(this);
		}
		
		/**
		 * Exit rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitPathParentheses(this);
		}
		
		/**
		 * Accept.
		 *
		 * @param <T> the generic type
		 * @param visitor the visitor
		 * @return the t
		 */
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitPathParentheses(this);
			else return visitor.visitChildren(this);
		}
	}
	
	/**
	 * The Class PathSequenceContext.
	 */
	public static class PathSequenceContext extends PathPatternsContext {
		
		/**
		 * Path patterns.
		 *
		 * @return the list
		 */
		public List<PathPatternsContext> pathPatterns() {
			return getRuleContexts(PathPatternsContext.class);
		}
		
		/**
		 * Path patterns.
		 *
		 * @param i the i
		 * @return the path patterns context
		 */
		public PathPatternsContext pathPatterns(int i) {
			return getRuleContext(PathPatternsContext.class,i);
		}
		
		/**
		 * Instantiates a new path sequence context.
		 *
		 * @param ctx the ctx
		 */
		public PathSequenceContext(PathPatternsContext ctx) { copyFrom(ctx); }
		
		/**
		 * Enter rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterPathSequence(this);
		}
		
		/**
		 * Exit rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitPathSequence(this);
		}
		
		/**
		 * Accept.
		 *
		 * @param <T> the generic type
		 * @param visitor the visitor
		 * @return the t
		 */
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitPathSequence(this);
			else return visitor.visitChildren(this);
		}
	}

	/**
	 * Path patterns.
	 *
	 * @return the path patterns context
	 * @throws RecognitionException the recognition exception
	 */
	public final PathPatternsContext pathPatterns() throws RecognitionException {
		return pathPatterns(0);
	}

	/**
	 * Path patterns.
	 *
	 * @param _p the p
	 * @return the path patterns context
	 * @throws RecognitionException the recognition exception
	 */
	private PathPatternsContext pathPatterns(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		PathPatternsContext _localctx = new PathPatternsContext(_ctx, _parentState);
		PathPatternsContext _prevctx = _localctx;
		int _startState = 4;
		enterRecursionRule(_localctx, 4, RULE_pathPatterns, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(75);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				{
				_localctx = new PathContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(62);
				pathEltOrInverse();
				setState(64);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
				case 1:
					{
					setState(63);
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
				setState(67);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==T__8) {
					{
					setState(66);
					negation();
					}
				}

				setState(69);
				match(T__3);
				setState(70);
				pathPatterns(0);
				setState(71);
				match(T__4);
				setState(73);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
				case 1:
					{
					setState(72);
					cardinality();
					}
					break;
				}
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(85);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(83);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
					case 1:
						{
						_localctx = new PathAlternativeContext(new PathPatternsContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_pathPatterns);
						setState(77);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(78);
						match(T__2);
						setState(79);
						pathPatterns(4);
						}
						break;
					case 2:
						{
						_localctx = new PathSequenceContext(new PathPatternsContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_pathPatterns);
						setState(80);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(81);
						_la = _input.LA(1);
						if ( !(_la==T__0 || _la==T__1) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(82);
						pathPatterns(3);
						}
						break;
					}
					} 
				}
				setState(87);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
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

	/**
	 * The Class CardinalityContext.
	 */
	public static class CardinalityContext extends ParserRuleContext {
		
		/**
		 * Integer.
		 *
		 * @return the list
		 */
		public List<TerminalNode> INTEGER() { return getTokens(PathPatternParser.INTEGER); }
		
		/**
		 * Integer.
		 *
		 * @param i the i
		 * @return the terminal node
		 */
		public TerminalNode INTEGER(int i) {
			return getToken(PathPatternParser.INTEGER, i);
		}
		
		/**
		 * Instantiates a new cardinality context.
		 *
		 * @param parent the parent
		 * @param invokingState the invoking state
		 */
		public CardinalityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		
		/**
		 * Gets the rule index.
		 *
		 * @return the rule index
		 */
		@Override public int getRuleIndex() { return RULE_cardinality; }
		
		/**
		 * Enter rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterCardinality(this);
		}
		
		/**
		 * Exit rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitCardinality(this);
		}
		
		/**
		 * Accept.
		 *
		 * @param <T> the generic type
		 * @param visitor the visitor
		 * @return the t
		 */
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitCardinality(this);
			else return visitor.visitChildren(this);
		}
	}

	/**
	 * Cardinality.
	 *
	 * @return the cardinality context
	 * @throws RecognitionException the recognition exception
	 */
	public final CardinalityContext cardinality() throws RecognitionException {
		CardinalityContext _localctx = new CardinalityContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_cardinality);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(88);
			match(T__5);
			setState(89);
			match(INTEGER);
			setState(94);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__6) {
				{
				setState(90);
				match(T__6);
				setState(92);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==INTEGER) {
					{
					setState(91);
					match(INTEGER);
					}
				}

				}
			}

			setState(96);
			match(T__7);
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

	/**
	 * The Class NegationContext.
	 */
	public static class NegationContext extends ParserRuleContext {
		
		/**
		 * Instantiates a new negation context.
		 *
		 * @param parent the parent
		 * @param invokingState the invoking state
		 */
		public NegationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		
		/**
		 * Gets the rule index.
		 *
		 * @return the rule index
		 */
		@Override public int getRuleIndex() { return RULE_negation; }
		
		/**
		 * Enter rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterNegation(this);
		}
		
		/**
		 * Exit rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitNegation(this);
		}
		
		/**
		 * Accept.
		 *
		 * @param <T> the generic type
		 * @param visitor the visitor
		 * @return the t
		 */
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitNegation(this);
			else return visitor.visitChildren(this);
		}
	}

	/**
	 * Negation.
	 *
	 * @return the negation context
	 * @throws RecognitionException the recognition exception
	 */
	public final NegationContext negation() throws RecognitionException {
		NegationContext _localctx = new NegationContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_negation);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(98);
			match(T__8);
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

	/**
	 * The Class PathEltOrInverseContext.
	 */
	public static class PathEltOrInverseContext extends ParserRuleContext {
		
		/**
		 * Predicate.
		 *
		 * @return the predicate context
		 */
		public PredicateContext predicate() {
			return getRuleContext(PredicateContext.class,0);
		}
		
		/**
		 * Negation.
		 *
		 * @return the negation context
		 */
		public NegationContext negation() {
			return getRuleContext(NegationContext.class,0);
		}
		
		/**
		 * Inverse.
		 *
		 * @return the terminal node
		 */
		public TerminalNode INVERSE() { return getToken(PathPatternParser.INVERSE, 0); }
		
		/**
		 * Instantiates a new path elt or inverse context.
		 *
		 * @param parent the parent
		 * @param invokingState the invoking state
		 */
		public PathEltOrInverseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		
		/**
		 * Gets the rule index.
		 *
		 * @return the rule index
		 */
		@Override public int getRuleIndex() { return RULE_pathEltOrInverse; }
		
		/**
		 * Enter rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterPathEltOrInverse(this);
		}
		
		/**
		 * Exit rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitPathEltOrInverse(this);
		}
		
		/**
		 * Accept.
		 *
		 * @param <T> the generic type
		 * @param visitor the visitor
		 * @return the t
		 */
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitPathEltOrInverse(this);
			else return visitor.visitChildren(this);
		}
	}

	/**
	 * Path elt or inverse.
	 *
	 * @return the path elt or inverse context
	 * @throws RecognitionException the recognition exception
	 */
	public final PathEltOrInverseContext pathEltOrInverse() throws RecognitionException {
		PathEltOrInverseContext _localctx = new PathEltOrInverseContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_pathEltOrInverse);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(101);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__8) {
				{
				setState(100);
				negation();
				}
			}

			setState(104);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==INVERSE) {
				{
				setState(103);
				match(INVERSE);
				}
			}

			setState(106);
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

	/**
	 * The Class PredicateContext.
	 */
	public static class PredicateContext extends ParserRuleContext {
		
		/**
		 * Reified predicate.
		 *
		 * @return the reified predicate context
		 */
		public ReifiedPredicateContext reifiedPredicate() {
			return getRuleContext(ReifiedPredicateContext.class,0);
		}
		
		/**
		 * Predicate ref.
		 *
		 * @return the predicate ref context
		 */
		public PredicateRefContext predicateRef() {
			return getRuleContext(PredicateRefContext.class,0);
		}
		
		/**
		 * Rdf type.
		 *
		 * @return the rdf type context
		 */
		public RdfTypeContext rdfType() {
			return getRuleContext(RdfTypeContext.class,0);
		}
		
		/**
		 * Any predicate.
		 *
		 * @return the any predicate context
		 */
		public AnyPredicateContext anyPredicate() {
			return getRuleContext(AnyPredicateContext.class,0);
		}
		
		/**
		 * Fact filter pattern.
		 *
		 * @return the fact filter pattern context
		 */
		public FactFilterPatternContext factFilterPattern() {
			return getRuleContext(FactFilterPatternContext.class,0);
		}
		
		/**
		 * Instantiates a new predicate context.
		 *
		 * @param parent the parent
		 * @param invokingState the invoking state
		 */
		public PredicateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		
		/**
		 * Gets the rule index.
		 *
		 * @return the rule index
		 */
		@Override public int getRuleIndex() { return RULE_predicate; }
		
		/**
		 * Enter rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterPredicate(this);
		}
		
		/**
		 * Exit rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitPredicate(this);
		}
		
		/**
		 * Accept.
		 *
		 * @param <T> the generic type
		 * @param visitor the visitor
		 * @return the t
		 */
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitPredicate(this);
			else return visitor.visitChildren(this);
		}
	}

	/**
	 * Predicate.
	 *
	 * @return the predicate context
	 * @throws RecognitionException the recognition exception
	 */
	public final PredicateContext predicate() throws RecognitionException {
		PredicateContext _localctx = new PredicateContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_predicate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(112);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				{
				setState(108);
				reifiedPredicate();
				}
				break;
			case 2:
				{
				setState(109);
				predicateRef();
				}
				break;
			case 3:
				{
				setState(110);
				rdfType();
				}
				break;
			case 4:
				{
				setState(111);
				anyPredicate();
				}
				break;
			}
			setState(115);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				{
				setState(114);
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

	/**
	 * The Class AnyPredicateContext.
	 */
	public static class AnyPredicateContext extends ParserRuleContext {
		
		/**
		 * Anypredicate.
		 *
		 * @return the terminal node
		 */
		public TerminalNode ANYPREDICATE() { return getToken(PathPatternParser.ANYPREDICATE, 0); }
		
		/**
		 * Instantiates a new any predicate context.
		 *
		 * @param parent the parent
		 * @param invokingState the invoking state
		 */
		public AnyPredicateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		
		/**
		 * Gets the rule index.
		 *
		 * @return the rule index
		 */
		@Override public int getRuleIndex() { return RULE_anyPredicate; }
		
		/**
		 * Enter rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterAnyPredicate(this);
		}
		
		/**
		 * Exit rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitAnyPredicate(this);
		}
		
		/**
		 * Accept.
		 *
		 * @param <T> the generic type
		 * @param visitor the visitor
		 * @return the t
		 */
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitAnyPredicate(this);
			else return visitor.visitChildren(this);
		}
	}

	/**
	 * Any predicate.
	 *
	 * @return the any predicate context
	 * @throws RecognitionException the recognition exception
	 */
	public final AnyPredicateContext anyPredicate() throws RecognitionException {
		AnyPredicateContext _localctx = new AnyPredicateContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_anyPredicate);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(117);
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

	/**
	 * The Class ReifiedPredicateContext.
	 */
	public static class ReifiedPredicateContext extends ParserRuleContext {
		
		/**
		 * Reifier.
		 *
		 * @return the terminal node
		 */
		public TerminalNode REIFIER() { return getToken(PathPatternParser.REIFIER, 0); }
		
		/**
		 * Predicate ref.
		 *
		 * @return the predicate ref context
		 */
		public PredicateRefContext predicateRef() {
			return getRuleContext(PredicateRefContext.class,0);
		}
		
		/**
		 * Iri ref.
		 *
		 * @return the iri ref context
		 */
		public IriRefContext iriRef() {
			return getRuleContext(IriRefContext.class,0);
		}
		
		/**
		 * Fact filter pattern.
		 *
		 * @return the fact filter pattern context
		 */
		public FactFilterPatternContext factFilterPattern() {
			return getRuleContext(FactFilterPatternContext.class,0);
		}
		
		/**
		 * Dereifier.
		 *
		 * @return the dereifier context
		 */
		public DereifierContext dereifier() {
			return getRuleContext(DereifierContext.class,0);
		}
		
		/**
		 * Instantiates a new reified predicate context.
		 *
		 * @param parent the parent
		 * @param invokingState the invoking state
		 */
		public ReifiedPredicateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		
		/**
		 * Gets the rule index.
		 *
		 * @return the rule index
		 */
		@Override public int getRuleIndex() { return RULE_reifiedPredicate; }
		
		/**
		 * Enter rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterReifiedPredicate(this);
		}
		
		/**
		 * Exit rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitReifiedPredicate(this);
		}
		
		/**
		 * Accept.
		 *
		 * @param <T> the generic type
		 * @param visitor the visitor
		 * @return the t
		 */
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitReifiedPredicate(this);
			else return visitor.visitChildren(this);
		}
	}

	/**
	 * Reified predicate.
	 *
	 * @return the reified predicate context
	 * @throws RecognitionException the recognition exception
	 */
	public final ReifiedPredicateContext reifiedPredicate() throws RecognitionException {
		ReifiedPredicateContext _localctx = new ReifiedPredicateContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_reifiedPredicate);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(120);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==IRI_REF || _la==PNAME_NS) {
				{
				setState(119);
				iriRef();
				}
			}

			setState(122);
			match(REIFIER);
			setState(123);
			predicateRef();
			setState(125);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				{
				setState(124);
				factFilterPattern();
				}
				break;
			}
			setState(128);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
			case 1:
				{
				setState(127);
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

	/**
	 * The Class PredicateRefContext.
	 */
	public static class PredicateRefContext extends ParserRuleContext {
		
		/**
		 * Iri ref.
		 *
		 * @return the terminal node
		 */
		public TerminalNode IRI_REF() { return getToken(PathPatternParser.IRI_REF, 0); }
		
		/**
		 * Rdf type.
		 *
		 * @return the rdf type context
		 */
		public RdfTypeContext rdfType() {
			return getRuleContext(RdfTypeContext.class,0);
		}
		
		/**
		 * Qname.
		 *
		 * @return the qname context
		 */
		public QnameContext qname() {
			return getRuleContext(QnameContext.class,0);
		}
		
		/**
		 * Pname ns.
		 *
		 * @return the pname ns context
		 */
		public Pname_nsContext pname_ns() {
			return getRuleContext(Pname_nsContext.class,0);
		}
		
		/**
		 * Instantiates a new predicate ref context.
		 *
		 * @param parent the parent
		 * @param invokingState the invoking state
		 */
		public PredicateRefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		
		/**
		 * Gets the rule index.
		 *
		 * @return the rule index
		 */
		@Override public int getRuleIndex() { return RULE_predicateRef; }
		
		/**
		 * Enter rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterPredicateRef(this);
		}
		
		/**
		 * Exit rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitPredicateRef(this);
		}
		
		/**
		 * Accept.
		 *
		 * @param <T> the generic type
		 * @param visitor the visitor
		 * @return the t
		 */
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitPredicateRef(this);
			else return visitor.visitChildren(this);
		}
	}

	/**
	 * Predicate ref.
	 *
	 * @return the predicate ref context
	 * @throws RecognitionException the recognition exception
	 */
	public final PredicateRefContext predicateRef() throws RecognitionException {
		PredicateRefContext _localctx = new PredicateRefContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_predicateRef);
		try {
			setState(134);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(130);
				match(IRI_REF);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(131);
				rdfType();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(132);
				qname();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(133);
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

	/**
	 * The Class IriRefContext.
	 */
	public static class IriRefContext extends ParserRuleContext {
		
		/**
		 * Iri ref.
		 *
		 * @return the terminal node
		 */
		public TerminalNode IRI_REF() { return getToken(PathPatternParser.IRI_REF, 0); }
		
		/**
		 * Qname.
		 *
		 * @return the qname context
		 */
		public QnameContext qname() {
			return getRuleContext(QnameContext.class,0);
		}
		
		/**
		 * Pname ns.
		 *
		 * @return the pname ns context
		 */
		public Pname_nsContext pname_ns() {
			return getRuleContext(Pname_nsContext.class,0);
		}
		
		/**
		 * Instantiates a new iri ref context.
		 *
		 * @param parent the parent
		 * @param invokingState the invoking state
		 */
		public IriRefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		
		/**
		 * Gets the rule index.
		 *
		 * @return the rule index
		 */
		@Override public int getRuleIndex() { return RULE_iriRef; }
		
		/**
		 * Enter rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterIriRef(this);
		}
		
		/**
		 * Exit rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitIriRef(this);
		}
		
		/**
		 * Accept.
		 *
		 * @param <T> the generic type
		 * @param visitor the visitor
		 * @return the t
		 */
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitIriRef(this);
			else return visitor.visitChildren(this);
		}
	}

	/**
	 * Iri ref.
	 *
	 * @return the iri ref context
	 * @throws RecognitionException the recognition exception
	 */
	public final IriRefContext iriRef() throws RecognitionException {
		IriRefContext _localctx = new IriRefContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_iriRef);
		try {
			setState(139);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(136);
				match(IRI_REF);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(137);
				qname();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(138);
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

	/**
	 * The Class DereifierContext.
	 */
	public static class DereifierContext extends ParserRuleContext {
		
		/**
		 * Dereifier.
		 *
		 * @return the terminal node
		 */
		public TerminalNode DEREIFIER() { return getToken(PathPatternParser.DEREIFIER, 0); }
		
		/**
		 * Instantiates a new dereifier context.
		 *
		 * @param parent the parent
		 * @param invokingState the invoking state
		 */
		public DereifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		
		/**
		 * Gets the rule index.
		 *
		 * @return the rule index
		 */
		@Override public int getRuleIndex() { return RULE_dereifier; }
		
		/**
		 * Enter rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterDereifier(this);
		}
		
		/**
		 * Exit rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitDereifier(this);
		}
		
		/**
		 * Accept.
		 *
		 * @param <T> the generic type
		 * @param visitor the visitor
		 * @return the t
		 */
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitDereifier(this);
			else return visitor.visitChildren(this);
		}
	}

	/**
	 * Dereifier.
	 *
	 * @return the dereifier context
	 * @throws RecognitionException the recognition exception
	 */
	public final DereifierContext dereifier() throws RecognitionException {
		DereifierContext _localctx = new DereifierContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_dereifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(141);
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

	/**
	 * The Class FactFilterPatternContext.
	 */
	public static class FactFilterPatternContext extends ParserRuleContext {
		
		/**
		 * Property list not empty.
		 *
		 * @return the property list not empty context
		 */
		public PropertyListNotEmptyContext propertyListNotEmpty() {
			return getRuleContext(PropertyListNotEmptyContext.class,0);
		}
		
		/**
		 * Instantiates a new fact filter pattern context.
		 *
		 * @param parent the parent
		 * @param invokingState the invoking state
		 */
		public FactFilterPatternContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		
		/**
		 * Gets the rule index.
		 *
		 * @return the rule index
		 */
		@Override public int getRuleIndex() { return RULE_factFilterPattern; }
		
		/**
		 * Enter rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterFactFilterPattern(this);
		}
		
		/**
		 * Exit rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitFactFilterPattern(this);
		}
		
		/**
		 * Accept.
		 *
		 * @param <T> the generic type
		 * @param visitor the visitor
		 * @return the t
		 */
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitFactFilterPattern(this);
			else return visitor.visitChildren(this);
		}
	}

	/**
	 * Fact filter pattern.
	 *
	 * @return the fact filter pattern context
	 * @throws RecognitionException the recognition exception
	 */
	public final FactFilterPatternContext factFilterPattern() throws RecognitionException {
		FactFilterPatternContext _localctx = new FactFilterPatternContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_factFilterPattern);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(143);
			match(T__9);
			setState(144);
			propertyListNotEmpty();
			setState(145);
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

	/**
	 * The Class PropertyListNotEmptyContext.
	 */
	public static class PropertyListNotEmptyContext extends ParserRuleContext {
		
		/**
		 * Verb object list.
		 *
		 * @return the list
		 */
		public List<VerbObjectListContext> verbObjectList() {
			return getRuleContexts(VerbObjectListContext.class);
		}
		
		/**
		 * Verb object list.
		 *
		 * @param i the i
		 * @return the verb object list context
		 */
		public VerbObjectListContext verbObjectList(int i) {
			return getRuleContext(VerbObjectListContext.class,i);
		}
		
		/**
		 * Instantiates a new property list not empty context.
		 *
		 * @param parent the parent
		 * @param invokingState the invoking state
		 */
		public PropertyListNotEmptyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		
		/**
		 * Gets the rule index.
		 *
		 * @return the rule index
		 */
		@Override public int getRuleIndex() { return RULE_propertyListNotEmpty; }
		
		/**
		 * Enter rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterPropertyListNotEmpty(this);
		}
		
		/**
		 * Exit rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitPropertyListNotEmpty(this);
		}
		
		/**
		 * Accept.
		 *
		 * @param <T> the generic type
		 * @param visitor the visitor
		 * @return the t
		 */
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitPropertyListNotEmpty(this);
			else return visitor.visitChildren(this);
		}
	}

	/**
	 * Property list not empty.
	 *
	 * @return the property list not empty context
	 * @throws RecognitionException the recognition exception
	 */
	public final PropertyListNotEmptyContext propertyListNotEmpty() throws RecognitionException {
		PropertyListNotEmptyContext _localctx = new PropertyListNotEmptyContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_propertyListNotEmpty);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(147);
			verbObjectList();
			setState(154);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__11) {
				{
				{
				setState(148);
				match(T__11);
				setState(150);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__8) | (1L << INVERSE) | (1L << REIFIER) | (1L << RDFTYPE) | (1L << ANYPREDICATE) | (1L << OPERATOR) | (1L << IRI_REF) | (1L << PNAME_NS))) != 0)) {
					{
					setState(149);
					verbObjectList();
					}
				}

				}
				}
				setState(156);
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

	/**
	 * The Class VerbObjectListContext.
	 */
	public static class VerbObjectListContext extends ParserRuleContext {
		
		/**
		 * Verb.
		 *
		 * @return the verb context
		 */
		public VerbContext verb() {
			return getRuleContext(VerbContext.class,0);
		}
		
		/**
		 * Object list.
		 *
		 * @return the object list context
		 */
		public ObjectListContext objectList() {
			return getRuleContext(ObjectListContext.class,0);
		}
		
		/**
		 * Instantiates a new verb object list context.
		 *
		 * @param parent the parent
		 * @param invokingState the invoking state
		 */
		public VerbObjectListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		
		/**
		 * Gets the rule index.
		 *
		 * @return the rule index
		 */
		@Override public int getRuleIndex() { return RULE_verbObjectList; }
		
		/**
		 * Enter rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterVerbObjectList(this);
		}
		
		/**
		 * Exit rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitVerbObjectList(this);
		}
		
		/**
		 * Accept.
		 *
		 * @param <T> the generic type
		 * @param visitor the visitor
		 * @return the t
		 */
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitVerbObjectList(this);
			else return visitor.visitChildren(this);
		}
	}

	/**
	 * Verb object list.
	 *
	 * @return the verb object list context
	 * @throws RecognitionException the recognition exception
	 */
	public final VerbObjectListContext verbObjectList() throws RecognitionException {
		VerbObjectListContext _localctx = new VerbObjectListContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_verbObjectList);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(157);
			verb();
			setState(158);
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

	/**
	 * The Class VerbContext.
	 */
	public static class VerbContext extends ParserRuleContext {
		
		/**
		 * Operator.
		 *
		 * @return the operator context
		 */
		public OperatorContext operator() {
			return getRuleContext(OperatorContext.class,0);
		}
		
		/**
		 * Path elt or inverse.
		 *
		 * @return the path elt or inverse context
		 */
		public PathEltOrInverseContext pathEltOrInverse() {
			return getRuleContext(PathEltOrInverseContext.class,0);
		}
		
		/**
		 * Instantiates a new verb context.
		 *
		 * @param parent the parent
		 * @param invokingState the invoking state
		 */
		public VerbContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		
		/**
		 * Gets the rule index.
		 *
		 * @return the rule index
		 */
		@Override public int getRuleIndex() { return RULE_verb; }
		
		/**
		 * Enter rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterVerb(this);
		}
		
		/**
		 * Exit rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitVerb(this);
		}
		
		/**
		 * Accept.
		 *
		 * @param <T> the generic type
		 * @param visitor the visitor
		 * @return the t
		 */
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitVerb(this);
			else return visitor.visitChildren(this);
		}
	}

	/**
	 * Verb.
	 *
	 * @return the verb context
	 * @throws RecognitionException the recognition exception
	 */
	public final VerbContext verb() throws RecognitionException {
		VerbContext _localctx = new VerbContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_verb);
		try {
			setState(162);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case OPERATOR:
				enterOuterAlt(_localctx, 1);
				{
				setState(160);
				operator();
				}
				break;
			case T__8:
			case INVERSE:
			case REIFIER:
			case RDFTYPE:
			case ANYPREDICATE:
			case IRI_REF:
			case PNAME_NS:
				enterOuterAlt(_localctx, 2);
				{
				setState(161);
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

	/**
	 * The Class ObjectListContext.
	 */
	public static class ObjectListContext extends ParserRuleContext {
		
		/**
		 * Object.
		 *
		 * @return the list
		 */
		public List<ObjectContext> object() {
			return getRuleContexts(ObjectContext.class);
		}
		
		/**
		 * Object.
		 *
		 * @param i the i
		 * @return the object context
		 */
		public ObjectContext object(int i) {
			return getRuleContext(ObjectContext.class,i);
		}
		
		/**
		 * Instantiates a new object list context.
		 *
		 * @param parent the parent
		 * @param invokingState the invoking state
		 */
		public ObjectListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		
		/**
		 * Gets the rule index.
		 *
		 * @return the rule index
		 */
		@Override public int getRuleIndex() { return RULE_objectList; }
		
		/**
		 * Enter rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterObjectList(this);
		}
		
		/**
		 * Exit rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitObjectList(this);
		}
		
		/**
		 * Accept.
		 *
		 * @param <T> the generic type
		 * @param visitor the visitor
		 * @return the t
		 */
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitObjectList(this);
			else return visitor.visitChildren(this);
		}
	}

	/**
	 * Object list.
	 *
	 * @return the object list context
	 * @throws RecognitionException the recognition exception
	 */
	public final ObjectListContext objectList() throws RecognitionException {
		ObjectListContext _localctx = new ObjectListContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_objectList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(164);
			object();
			setState(169);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__6) {
				{
				{
				setState(165);
				match(T__6);
				setState(166);
				object();
				}
				}
				setState(171);
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

	/**
	 * The Class ObjectContext.
	 */
	public static class ObjectContext extends ParserRuleContext {
		
		/**
		 * Iri ref.
		 *
		 * @return the iri ref context
		 */
		public IriRefContext iriRef() {
			return getRuleContext(IriRefContext.class,0);
		}
		
		/**
		 * Literal.
		 *
		 * @return the literal context
		 */
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		
		/**
		 * Fact filter pattern.
		 *
		 * @return the fact filter pattern context
		 */
		public FactFilterPatternContext factFilterPattern() {
			return getRuleContext(FactFilterPatternContext.class,0);
		}
		
		/**
		 * Instantiates a new object context.
		 *
		 * @param parent the parent
		 * @param invokingState the invoking state
		 */
		public ObjectContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		
		/**
		 * Gets the rule index.
		 *
		 * @return the rule index
		 */
		@Override public int getRuleIndex() { return RULE_object; }
		
		/**
		 * Enter rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterObject(this);
		}
		
		/**
		 * Exit rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitObject(this);
		}
		
		/**
		 * Accept.
		 *
		 * @param <T> the generic type
		 * @param visitor the visitor
		 * @return the t
		 */
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitObject(this);
			else return visitor.visitChildren(this);
		}
	}

	/**
	 * Object.
	 *
	 * @return the object context
	 * @throws RecognitionException the recognition exception
	 */
	public final ObjectContext object() throws RecognitionException {
		ObjectContext _localctx = new ObjectContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_object);
		try {
			setState(175);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IRI_REF:
			case PNAME_NS:
				enterOuterAlt(_localctx, 1);
				{
				setState(172);
				iriRef();
				}
				break;
			case DQLITERAL:
			case SQLITERAL:
				enterOuterAlt(_localctx, 2);
				{
				setState(173);
				literal();
				}
				break;
			case T__9:
				enterOuterAlt(_localctx, 3);
				{
				setState(174);
				factFilterPattern();
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

	/**
	 * The Class QnameContext.
	 */
	public static class QnameContext extends ParserRuleContext {
		
		/**
		 * Pname ns.
		 *
		 * @return the terminal node
		 */
		public TerminalNode PNAME_NS() { return getToken(PathPatternParser.PNAME_NS, 0); }
		
		/**
		 * Pn local.
		 *
		 * @return the terminal node
		 */
		public TerminalNode PN_LOCAL() { return getToken(PathPatternParser.PN_LOCAL, 0); }
		
		/**
		 * Instantiates a new qname context.
		 *
		 * @param parent the parent
		 * @param invokingState the invoking state
		 */
		public QnameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		
		/**
		 * Gets the rule index.
		 *
		 * @return the rule index
		 */
		@Override public int getRuleIndex() { return RULE_qname; }
		
		/**
		 * Enter rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterQname(this);
		}
		
		/**
		 * Exit rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitQname(this);
		}
		
		/**
		 * Accept.
		 *
		 * @param <T> the generic type
		 * @param visitor the visitor
		 * @return the t
		 */
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitQname(this);
			else return visitor.visitChildren(this);
		}
	}

	/**
	 * Qname.
	 *
	 * @return the qname context
	 * @throws RecognitionException the recognition exception
	 */
	public final QnameContext qname() throws RecognitionException {
		QnameContext _localctx = new QnameContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_qname);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(177);
			match(PNAME_NS);
			setState(178);
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

	/**
	 * The Class Pname_nsContext.
	 */
	public static class Pname_nsContext extends ParserRuleContext {
		
		/**
		 * Pname ns.
		 *
		 * @return the terminal node
		 */
		public TerminalNode PNAME_NS() { return getToken(PathPatternParser.PNAME_NS, 0); }
		
		/**
		 * Instantiates a new pname ns context.
		 *
		 * @param parent the parent
		 * @param invokingState the invoking state
		 */
		public Pname_nsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		
		/**
		 * Gets the rule index.
		 *
		 * @return the rule index
		 */
		@Override public int getRuleIndex() { return RULE_pname_ns; }
		
		/**
		 * Enter rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterPname_ns(this);
		}
		
		/**
		 * Exit rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitPname_ns(this);
		}
		
		/**
		 * Accept.
		 *
		 * @param <T> the generic type
		 * @param visitor the visitor
		 * @return the t
		 */
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitPname_ns(this);
			else return visitor.visitChildren(this);
		}
	}

	/**
	 * Pname ns.
	 *
	 * @return the pname ns context
	 * @throws RecognitionException the recognition exception
	 */
	public final Pname_nsContext pname_ns() throws RecognitionException {
		Pname_nsContext _localctx = new Pname_nsContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_pname_ns);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(180);
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

	/**
	 * The Class LiteralContext.
	 */
	public static class LiteralContext extends ParserRuleContext {
		
		/**
		 * Dqliteral.
		 *
		 * @return the terminal node
		 */
		public TerminalNode DQLITERAL() { return getToken(PathPatternParser.DQLITERAL, 0); }
		
		/**
		 * Sqliteral.
		 *
		 * @return the terminal node
		 */
		public TerminalNode SQLITERAL() { return getToken(PathPatternParser.SQLITERAL, 0); }
		
		/**
		 * Instantiates a new literal context.
		 *
		 * @param parent the parent
		 * @param invokingState the invoking state
		 */
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		
		/**
		 * Gets the rule index.
		 *
		 * @return the rule index
		 */
		@Override public int getRuleIndex() { return RULE_literal; }
		
		/**
		 * Enter rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterLiteral(this);
		}
		
		/**
		 * Exit rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitLiteral(this);
		}
		
		/**
		 * Accept.
		 *
		 * @param <T> the generic type
		 * @param visitor the visitor
		 * @return the t
		 */
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	/**
	 * Literal.
	 *
	 * @return the literal context
	 * @throws RecognitionException the recognition exception
	 */
	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_literal);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(182);
			_la = _input.LA(1);
			if ( !(_la==DQLITERAL || _la==SQLITERAL) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
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

	/**
	 * The Class OperatorContext.
	 */
	public static class OperatorContext extends ParserRuleContext {
		
		/**
		 * Operator.
		 *
		 * @return the terminal node
		 */
		public TerminalNode OPERATOR() { return getToken(PathPatternParser.OPERATOR, 0); }
		
		/**
		 * Instantiates a new operator context.
		 *
		 * @param parent the parent
		 * @param invokingState the invoking state
		 */
		public OperatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		
		/**
		 * Gets the rule index.
		 *
		 * @return the rule index
		 */
		@Override public int getRuleIndex() { return RULE_operator; }
		
		/**
		 * Enter rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterOperator(this);
		}
		
		/**
		 * Exit rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitOperator(this);
		}
		
		/**
		 * Accept.
		 *
		 * @param <T> the generic type
		 * @param visitor the visitor
		 * @return the t
		 */
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitOperator(this);
			else return visitor.visitChildren(this);
		}
	}

	/**
	 * Operator.
	 *
	 * @return the operator context
	 * @throws RecognitionException the recognition exception
	 */
	public final OperatorContext operator() throws RecognitionException {
		OperatorContext _localctx = new OperatorContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_operator);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(184);
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

	/**
	 * The Class RdfTypeContext.
	 */
	public static class RdfTypeContext extends ParserRuleContext {
		
		/**
		 * Rdftype.
		 *
		 * @return the terminal node
		 */
		public TerminalNode RDFTYPE() { return getToken(PathPatternParser.RDFTYPE, 0); }
		
		/**
		 * Instantiates a new rdf type context.
		 *
		 * @param parent the parent
		 * @param invokingState the invoking state
		 */
		public RdfTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		
		/**
		 * Gets the rule index.
		 *
		 * @return the rule index
		 */
		@Override public int getRuleIndex() { return RULE_rdfType; }
		
		/**
		 * Enter rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).enterRdfType(this);
		}
		
		/**
		 * Exit rule.
		 *
		 * @param listener the listener
		 */
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof PathPatternListener ) ((PathPatternListener)listener).exitRdfType(this);
		}
		
		/**
		 * Accept.
		 *
		 * @param <T> the generic type
		 * @param visitor the visitor
		 * @return the t
		 */
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof PathPatternVisitor ) return ((PathPatternVisitor<? extends T>)visitor).visitRdfType(this);
			else return visitor.visitChildren(this);
		}
	}

	/**
	 * Rdf type.
	 *
	 * @return the rdf type context
	 * @throws RecognitionException the recognition exception
	 */
	public final RdfTypeContext rdfType() throws RecognitionException {
		RdfTypeContext _localctx = new RdfTypeContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_rdfType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(186);
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

	/**
	 * Sempred.
	 *
	 * @param _localctx the localctx
	 * @param ruleIndex the rule index
	 * @param predIndex the pred index
	 * @return true, if successful
	 */
	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 2:
			return pathPatterns_sempred((PathPatternsContext)_localctx, predIndex);
		}
		return true;
	}
	
	/**
	 * Path patterns sempred.
	 *
	 * @param _localctx the localctx
	 * @param predIndex the pred index
	 * @return true, if successful
	 */
	private boolean pathPatterns_sempred(PathPatternsContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 3);
		case 1:
			return precpred(_ctx, 2);
		}
		return true;
	}

	/** The Constant _serializedATN. */
	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\34\u00bf\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\3\2\3\2\3"+
		"\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\2\5\2<\n\2\3\3\3\3\3\4\3\4\3\4\5\4C\n"+
		"\4\3\4\5\4F\n\4\3\4\3\4\3\4\3\4\5\4L\n\4\5\4N\n\4\3\4\3\4\3\4\3\4\3\4"+
		"\3\4\7\4V\n\4\f\4\16\4Y\13\4\3\5\3\5\3\5\3\5\5\5_\n\5\5\5a\n\5\3\5\3\5"+
		"\3\6\3\6\3\7\5\7h\n\7\3\7\5\7k\n\7\3\7\3\7\3\b\3\b\3\b\3\b\5\bs\n\b\3"+
		"\b\5\bv\n\b\3\t\3\t\3\n\5\n{\n\n\3\n\3\n\3\n\5\n\u0080\n\n\3\n\5\n\u0083"+
		"\n\n\3\13\3\13\3\13\3\13\5\13\u0089\n\13\3\f\3\f\3\f\5\f\u008e\n\f\3\r"+
		"\3\r\3\16\3\16\3\16\3\16\3\17\3\17\3\17\5\17\u0099\n\17\7\17\u009b\n\17"+
		"\f\17\16\17\u009e\13\17\3\20\3\20\3\20\3\21\3\21\5\21\u00a5\n\21\3\22"+
		"\3\22\3\22\7\22\u00aa\n\22\f\22\16\22\u00ad\13\22\3\23\3\23\3\23\5\23"+
		"\u00b2\n\23\3\24\3\24\3\24\3\25\3\25\3\26\3\26\3\27\3\27\3\30\3\30\3\30"+
		"\2\3\6\31\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\2\4\3\2\3\4"+
		"\3\2\26\27\2\u00c5\2;\3\2\2\2\4=\3\2\2\2\6M\3\2\2\2\bZ\3\2\2\2\nd\3\2"+
		"\2\2\fg\3\2\2\2\16r\3\2\2\2\20w\3\2\2\2\22z\3\2\2\2\24\u0088\3\2\2\2\26"+
		"\u008d\3\2\2\2\30\u008f\3\2\2\2\32\u0091\3\2\2\2\34\u0095\3\2\2\2\36\u009f"+
		"\3\2\2\2 \u00a4\3\2\2\2\"\u00a6\3\2\2\2$\u00b1\3\2\2\2&\u00b3\3\2\2\2"+
		"(\u00b6\3\2\2\2*\u00b8\3\2\2\2,\u00ba\3\2\2\2.\u00bc\3\2\2\2\60\61\5\4"+
		"\3\2\61\62\t\2\2\2\62\63\5\6\4\2\63\64\7\2\2\3\64<\3\2\2\2\65\66\5\4\3"+
		"\2\66\67\7\2\2\3\67<\3\2\2\289\5\6\4\29:\7\2\2\3:<\3\2\2\2;\60\3\2\2\2"+
		";\65\3\2\2\2;8\3\2\2\2<\3\3\2\2\2=>\5\32\16\2>\5\3\2\2\2?@\b\4\1\2@B\5"+
		"\f\7\2AC\5\b\5\2BA\3\2\2\2BC\3\2\2\2CN\3\2\2\2DF\5\n\6\2ED\3\2\2\2EF\3"+
		"\2\2\2FG\3\2\2\2GH\7\6\2\2HI\5\6\4\2IK\7\7\2\2JL\5\b\5\2KJ\3\2\2\2KL\3"+
		"\2\2\2LN\3\2\2\2M?\3\2\2\2ME\3\2\2\2NW\3\2\2\2OP\f\5\2\2PQ\7\5\2\2QV\5"+
		"\6\4\6RS\f\4\2\2ST\t\2\2\2TV\5\6\4\5UO\3\2\2\2UR\3\2\2\2VY\3\2\2\2WU\3"+
		"\2\2\2WX\3\2\2\2X\7\3\2\2\2YW\3\2\2\2Z[\7\b\2\2[`\7\17\2\2\\^\7\t\2\2"+
		"]_\7\17\2\2^]\3\2\2\2^_\3\2\2\2_a\3\2\2\2`\\\3\2\2\2`a\3\2\2\2ab\3\2\2"+
		"\2bc\7\n\2\2c\t\3\2\2\2de\7\13\2\2e\13\3\2\2\2fh\5\n\6\2gf\3\2\2\2gh\3"+
		"\2\2\2hj\3\2\2\2ik\7\20\2\2ji\3\2\2\2jk\3\2\2\2kl\3\2\2\2lm\5\16\b\2m"+
		"\r\3\2\2\2ns\5\22\n\2os\5\24\13\2ps\5.\30\2qs\5\20\t\2rn\3\2\2\2ro\3\2"+
		"\2\2rp\3\2\2\2rq\3\2\2\2su\3\2\2\2tv\5\32\16\2ut\3\2\2\2uv\3\2\2\2v\17"+
		"\3\2\2\2wx\7\24\2\2x\21\3\2\2\2y{\5\26\f\2zy\3\2\2\2z{\3\2\2\2{|\3\2\2"+
		"\2|}\7\21\2\2}\177\5\24\13\2~\u0080\5\32\16\2\177~\3\2\2\2\177\u0080\3"+
		"\2\2\2\u0080\u0082\3\2\2\2\u0081\u0083\5\30\r\2\u0082\u0081\3\2\2\2\u0082"+
		"\u0083\3\2\2\2\u0083\23\3\2\2\2\u0084\u0089\7\30\2\2\u0085\u0089\5.\30"+
		"\2\u0086\u0089\5&\24\2\u0087\u0089\5(\25\2\u0088\u0084\3\2\2\2\u0088\u0085"+
		"\3\2\2\2\u0088\u0086\3\2\2\2\u0088\u0087\3\2\2\2\u0089\25\3\2\2\2\u008a"+
		"\u008e\7\30\2\2\u008b\u008e\5&\24\2\u008c\u008e\5(\25\2\u008d\u008a\3"+
		"\2\2\2\u008d\u008b\3\2\2\2\u008d\u008c\3\2\2\2\u008e\27\3\2\2\2\u008f"+
		"\u0090\7\22\2\2\u0090\31\3\2\2\2\u0091\u0092\7\f\2\2\u0092\u0093\5\34"+
		"\17\2\u0093\u0094\7\r\2\2\u0094\33\3\2\2\2\u0095\u009c\5\36\20\2\u0096"+
		"\u0098\7\16\2\2\u0097\u0099\5\36\20\2\u0098\u0097\3\2\2\2\u0098\u0099"+
		"\3\2\2\2\u0099\u009b\3\2\2\2\u009a\u0096\3\2\2\2\u009b\u009e\3\2\2\2\u009c"+
		"\u009a\3\2\2\2\u009c\u009d\3\2\2\2\u009d\35\3\2\2\2\u009e\u009c\3\2\2"+
		"\2\u009f\u00a0\5 \21\2\u00a0\u00a1\5\"\22\2\u00a1\37\3\2\2\2\u00a2\u00a5"+
		"\5,\27\2\u00a3\u00a5\5\f\7\2\u00a4\u00a2\3\2\2\2\u00a4\u00a3\3\2\2\2\u00a5"+
		"!\3\2\2\2\u00a6\u00ab\5$\23\2\u00a7\u00a8\7\t\2\2\u00a8\u00aa\5$\23\2"+
		"\u00a9\u00a7\3\2\2\2\u00aa\u00ad\3\2\2\2\u00ab\u00a9\3\2\2\2\u00ab\u00ac"+
		"\3\2\2\2\u00ac#\3\2\2\2\u00ad\u00ab\3\2\2\2\u00ae\u00b2\5\26\f\2\u00af"+
		"\u00b2\5*\26\2\u00b0\u00b2\5\32\16\2\u00b1\u00ae\3\2\2\2\u00b1\u00af\3"+
		"\2\2\2\u00b1\u00b0\3\2\2\2\u00b2%\3\2\2\2\u00b3\u00b4\7\31\2\2\u00b4\u00b5"+
		"\7\33\2\2\u00b5\'\3\2\2\2\u00b6\u00b7\7\31\2\2\u00b7)\3\2\2\2\u00b8\u00b9"+
		"\t\3\2\2\u00b9+\3\2\2\2\u00ba\u00bb\7\25\2\2\u00bb-\3\2\2\2\u00bc\u00bd"+
		"\7\23\2\2\u00bd/\3\2\2\2\31;BEKMUW^`gjruz\177\u0082\u0088\u008d\u0098"+
		"\u009c\u00a4\u00ab\u00b1";
	
	/** The Constant _ATN. */
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}