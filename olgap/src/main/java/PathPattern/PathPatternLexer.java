/*
 * inova8 2020
 */
package PathPattern;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

/**
 * The Class PathPatternLexer.
 */
@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class PathPatternLexer extends Lexer {
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
	
	/** The channel names. */
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	/** The mode names. */
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	/**
	 * Make rule names.
	 *
	 * @return the string[]
	 */
	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
			"T__9", "T__10", "T__11", "INTEGER", "DIGIT", "INVERSE", "REIFIER", "DEREIFIER", 
			"RDFTYPE", "ANYPREDICATE", "OPERATOR", "DQLITERAL", "SQLITERAL", "IRI_REF", 
			"PNAME_NS", "VARNAME", "PN_CHARS_U", "PN_CHARS", "PN_PREFIX", "PN_LOCAL", 
			"PN_CHARS_BASE", "WS"
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
	 * Instantiates a new path pattern lexer.
	 *
	 * @param input the input
	 */
	public PathPatternLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
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
	 * Gets the channel names.
	 *
	 * @return the channel names
	 */
	@Override
	public String[] getChannelNames() { return channelNames; }

	/**
	 * Gets the mode names.
	 *
	 * @return the mode names
	 */
	@Override
	public String[] getModeNames() { return modeNames; }

	/**
	 * Gets the atn.
	 *
	 * @return the atn
	 */
	@Override
	public ATN getATN() { return _ATN; }

	/** The Constant _serializedATN. */
	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\34\u00df\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t"+
		" \3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n"+
		"\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16\6\16[\n\16\r\16\16\16\\\3\17\3\17"+
		"\3\20\3\20\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3\25\3\25\3\25\3\25"+
		"\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25"+
		"\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\5\25\u0088\n\25"+
		"\3\26\3\26\3\26\3\26\7\26\u008e\n\26\f\26\16\26\u0091\13\26\3\26\3\26"+
		"\3\27\3\27\3\27\3\27\7\27\u0099\n\27\f\27\16\27\u009c\13\27\3\27\3\27"+
		"\3\30\3\30\3\30\7\30\u00a3\n\30\f\30\16\30\u00a6\13\30\3\30\3\30\3\31"+
		"\5\31\u00ab\n\31\3\31\3\31\3\32\3\32\6\32\u00b1\n\32\r\32\16\32\u00b2"+
		"\3\33\3\33\5\33\u00b7\n\33\3\34\3\34\3\34\5\34\u00bc\n\34\3\35\3\35\3"+
		"\35\7\35\u00c1\n\35\f\35\16\35\u00c4\13\35\3\35\5\35\u00c7\n\35\3\36\3"+
		"\36\5\36\u00cb\n\36\3\36\3\36\7\36\u00cf\n\36\f\36\16\36\u00d2\13\36\3"+
		"\36\5\36\u00d5\n\36\3\37\3\37\3 \6 \u00da\n \r \16 \u00db\3 \3 \2\2!\3"+
		"\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\2\37"+
		"\20!\21#\22%\23\'\24)\25+\26-\27/\30\61\31\63\32\65\2\67\29\2;\33=\2?"+
		"\34\3\2\f\3\2\62;\6\2\f\f\17\17$$^^\4\2$$^^\6\2\f\f\17\17))^^\4\2))^^"+
		"\t\2$$>>@@^^``bb}\177\4\2<<\u0080\u0080\4\2C\\c|\17\2C\\c|\u00c2\u00d8"+
		"\u00da\u00f8\u00fa\u0301\u0372\u037f\u0381\u2001\u200e\u200f\u2072\u2191"+
		"\u2c02\u2ff1\u3003\ud801\uf902\ufdd1\ufdf2\uffff\5\2\13\f\17\17\"\"\2"+
		"\u00f5\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2"+
		"\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3"+
		"\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2"+
		"%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61"+
		"\3\2\2\2\2\63\3\2\2\2\2;\3\2\2\2\2?\3\2\2\2\3A\3\2\2\2\5C\3\2\2\2\7E\3"+
		"\2\2\2\tG\3\2\2\2\13I\3\2\2\2\rK\3\2\2\2\17M\3\2\2\2\21O\3\2\2\2\23Q\3"+
		"\2\2\2\25S\3\2\2\2\27U\3\2\2\2\31W\3\2\2\2\33Z\3\2\2\2\35^\3\2\2\2\37"+
		"`\3\2\2\2!b\3\2\2\2#d\3\2\2\2%f\3\2\2\2\'h\3\2\2\2)\u0087\3\2\2\2+\u0089"+
		"\3\2\2\2-\u0094\3\2\2\2/\u009f\3\2\2\2\61\u00aa\3\2\2\2\63\u00ae\3\2\2"+
		"\2\65\u00b6\3\2\2\2\67\u00bb\3\2\2\29\u00bd\3\2\2\2;\u00ca\3\2\2\2=\u00d6"+
		"\3\2\2\2?\u00d9\3\2\2\2AB\7\61\2\2B\4\3\2\2\2CD\7@\2\2D\6\3\2\2\2EF\7"+
		"~\2\2F\b\3\2\2\2GH\7*\2\2H\n\3\2\2\2IJ\7+\2\2J\f\3\2\2\2KL\7}\2\2L\16"+
		"\3\2\2\2MN\7.\2\2N\20\3\2\2\2OP\7\177\2\2P\22\3\2\2\2QR\7#\2\2R\24\3\2"+
		"\2\2ST\7]\2\2T\26\3\2\2\2UV\7_\2\2V\30\3\2\2\2WX\7=\2\2X\32\3\2\2\2Y["+
		"\5\35\17\2ZY\3\2\2\2[\\\3\2\2\2\\Z\3\2\2\2\\]\3\2\2\2]\34\3\2\2\2^_\t"+
		"\2\2\2_\36\3\2\2\2`a\7`\2\2a \3\2\2\2bc\7B\2\2c\"\3\2\2\2de\7%\2\2e$\3"+
		"\2\2\2fg\7c\2\2g&\3\2\2\2hi\7,\2\2i(\3\2\2\2jk\7n\2\2k\u0088\7v\2\2lm"+
		"\7i\2\2m\u0088\7v\2\2no\7n\2\2o\u0088\7g\2\2pq\7i\2\2q\u0088\7g\2\2rs"+
		"\7g\2\2s\u0088\7s\2\2tu\7p\2\2u\u0088\7g\2\2vw\7n\2\2wx\7k\2\2xy\7m\2"+
		"\2y\u0088\7g\2\2z{\7s\2\2{|\7w\2\2|}\7g\2\2}~\7t\2\2~\u0088\7{\2\2\177"+
		"\u0080\7r\2\2\u0080\u0081\7t\2\2\u0081\u0082\7q\2\2\u0082\u0083\7r\2\2"+
		"\u0083\u0084\7g\2\2\u0084\u0085\7t\2\2\u0085\u0086\7v\2\2\u0086\u0088"+
		"\7{\2\2\u0087j\3\2\2\2\u0087l\3\2\2\2\u0087n\3\2\2\2\u0087p\3\2\2\2\u0087"+
		"r\3\2\2\2\u0087t\3\2\2\2\u0087v\3\2\2\2\u0087z\3\2\2\2\u0087\177\3\2\2"+
		"\2\u0088*\3\2\2\2\u0089\u008f\7$\2\2\u008a\u008e\n\3\2\2\u008b\u008c\7"+
		"^\2\2\u008c\u008e\t\4\2\2\u008d\u008a\3\2\2\2\u008d\u008b\3\2\2\2\u008e"+
		"\u0091\3\2\2\2\u008f\u008d\3\2\2\2\u008f\u0090\3\2\2\2\u0090\u0092\3\2"+
		"\2\2\u0091\u008f\3\2\2\2\u0092\u0093\7$\2\2\u0093,\3\2\2\2\u0094\u009a"+
		"\7)\2\2\u0095\u0099\n\5\2\2\u0096\u0097\7^\2\2\u0097\u0099\t\6\2\2\u0098"+
		"\u0095\3\2\2\2\u0098\u0096\3\2\2\2\u0099\u009c\3\2\2\2\u009a\u0098\3\2"+
		"\2\2\u009a\u009b\3\2\2\2\u009b\u009d\3\2\2\2\u009c\u009a\3\2\2\2\u009d"+
		"\u009e\7)\2\2\u009e.\3\2\2\2\u009f\u00a4\7>\2\2\u00a0\u00a3\n\7\2\2\u00a1"+
		"\u00a3\5\67\34\2\u00a2\u00a0\3\2\2\2\u00a2\u00a1\3\2\2\2\u00a3\u00a6\3"+
		"\2\2\2\u00a4\u00a2\3\2\2\2\u00a4\u00a5\3\2\2\2\u00a5\u00a7\3\2\2\2\u00a6"+
		"\u00a4\3\2\2\2\u00a7\u00a8\7@\2\2\u00a8\60\3\2\2\2\u00a9\u00ab\59\35\2"+
		"\u00aa\u00a9\3\2\2\2\u00aa\u00ab\3\2\2\2\u00ab\u00ac\3\2\2\2\u00ac\u00ad"+
		"\t\b\2\2\u00ad\62\3\2\2\2\u00ae\u00b0\7A\2\2\u00af\u00b1\t\t\2\2\u00b0"+
		"\u00af\3\2\2\2\u00b1\u00b2\3\2\2\2\u00b2\u00b0\3\2\2\2\u00b2\u00b3\3\2"+
		"\2\2\u00b3\64\3\2\2\2\u00b4\u00b7\5=\37\2\u00b5\u00b7\7a\2\2\u00b6\u00b4"+
		"\3\2\2\2\u00b6\u00b5\3\2\2\2\u00b7\66\3\2\2\2\u00b8\u00bc\5\65\33\2\u00b9"+
		"\u00bc\7/\2\2\u00ba\u00bc\5\35\17\2\u00bb\u00b8\3\2\2\2\u00bb\u00b9\3"+
		"\2\2\2\u00bb\u00ba\3\2\2\2\u00bc8\3\2\2\2\u00bd\u00c6\5=\37\2\u00be\u00c1"+
		"\5\67\34\2\u00bf\u00c1\7\60\2\2\u00c0\u00be\3\2\2\2\u00c0\u00bf\3\2\2"+
		"\2\u00c1\u00c4\3\2\2\2\u00c2\u00c0\3\2\2\2\u00c2\u00c3\3\2\2\2\u00c3\u00c5"+
		"\3\2\2\2\u00c4\u00c2\3\2\2\2\u00c5\u00c7\5\67\34\2\u00c6\u00c2\3\2\2\2"+
		"\u00c6\u00c7\3\2\2\2\u00c7:\3\2\2\2\u00c8\u00cb\5\65\33\2\u00c9\u00cb"+
		"\5\35\17\2\u00ca\u00c8\3\2\2\2\u00ca\u00c9\3\2\2\2\u00cb\u00d4\3\2\2\2"+
		"\u00cc\u00cf\5\67\34\2\u00cd\u00cf\7\60\2\2\u00ce\u00cc\3\2\2\2\u00ce"+
		"\u00cd\3\2\2\2\u00cf\u00d2\3\2\2\2\u00d0\u00ce\3\2\2\2\u00d0\u00d1\3\2"+
		"\2\2\u00d1\u00d3\3\2\2\2\u00d2\u00d0\3\2\2\2\u00d3\u00d5\5\67\34\2\u00d4"+
		"\u00d0\3\2\2\2\u00d4\u00d5\3\2\2\2\u00d5<\3\2\2\2\u00d6\u00d7\t\n\2\2"+
		"\u00d7>\3\2\2\2\u00d8\u00da\t\13\2\2\u00d9\u00d8\3\2\2\2\u00da\u00db\3"+
		"\2\2\2\u00db\u00d9\3\2\2\2\u00db\u00dc\3\2\2\2\u00dc\u00dd\3\2\2\2\u00dd"+
		"\u00de\b \2\2\u00de@\3\2\2\2\27\2\\\u0087\u008d\u008f\u0098\u009a\u00a2"+
		"\u00a4\u00aa\u00b2\u00b6\u00bb\u00c0\u00c2\u00c6\u00ca\u00ce\u00d0\u00d4"+
		"\u00db\3\b\2\2";
	
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