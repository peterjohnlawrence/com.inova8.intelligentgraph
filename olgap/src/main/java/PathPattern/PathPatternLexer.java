// Generated from PathPattern.g4 by ANTLR 4.9
package PathPattern;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class PathPatternLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, INTEGER=12, INVERSE=13, REIFIER=14, DEREIFIER=15, RDFTYPE=16, 
		ANYPREDICATE=17, OPERATOR=18, LITERAL=19, SQLITERAL=20, IRI_REF=21, PNAME_NS=22, 
		VARNAME=23, PN_LOCAL=24, WS=25;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
			"T__9", "T__10", "INTEGER", "DIGIT", "INVERSE", "REIFIER", "DEREIFIER", 
			"RDFTYPE", "ANYPREDICATE", "OPERATOR", "LITERAL", "SQLITERAL", "IRI_REF", 
			"PNAME_NS", "VARNAME", "PN_CHARS_U", "PN_CHARS", "PN_PREFIX", "PN_LOCAL", 
			"PN_CHARS_BASE", "WS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'|'", "'/'", "'('", "')'", "'{'", "','", "'}'", "'!'", "'['", 
			"']'", "';'", null, "'^'", "'@'", "'#'", "'a'", "'*'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			"INTEGER", "INVERSE", "REIFIER", "DEREIFIER", "RDFTYPE", "ANYPREDICATE", 
			"OPERATOR", "LITERAL", "SQLITERAL", "IRI_REF", "PNAME_NS", "VARNAME", 
			"PN_LOCAL", "WS"
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


	public PathPatternLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "PathPattern.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\33\u00ca\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\3\2"+
		"\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3"+
		"\13\3\13\3\f\3\f\3\r\6\rW\n\r\r\r\16\rX\3\16\3\16\3\17\3\17\3\20\3\20"+
		"\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24"+
		"\3\24\3\24\3\24\3\24\5\24s\n\24\3\25\3\25\3\25\3\25\7\25y\n\25\f\25\16"+
		"\25|\13\25\3\25\3\25\3\26\3\26\3\26\3\26\7\26\u0084\n\26\f\26\16\26\u0087"+
		"\13\26\3\26\3\26\3\27\3\27\3\27\7\27\u008e\n\27\f\27\16\27\u0091\13\27"+
		"\3\27\3\27\3\30\5\30\u0096\n\30\3\30\3\30\3\31\3\31\6\31\u009c\n\31\r"+
		"\31\16\31\u009d\3\32\3\32\5\32\u00a2\n\32\3\33\3\33\3\33\5\33\u00a7\n"+
		"\33\3\34\3\34\3\34\7\34\u00ac\n\34\f\34\16\34\u00af\13\34\3\34\5\34\u00b2"+
		"\n\34\3\35\3\35\5\35\u00b6\n\35\3\35\3\35\7\35\u00ba\n\35\f\35\16\35\u00bd"+
		"\13\35\3\35\5\35\u00c0\n\35\3\36\3\36\3\37\6\37\u00c5\n\37\r\37\16\37"+
		"\u00c6\3\37\3\37\2\2 \3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27"+
		"\r\31\16\33\2\35\17\37\20!\21#\22%\23\'\24)\25+\26-\27/\30\61\31\63\2"+
		"\65\2\67\29\32;\2=\33\3\2\13\3\2\62;\6\2\f\f\17\17$$^^\4\2$$^^\6\2\f\f"+
		"\17\17))^^\4\2))^^\t\2$$>>@@^^``bb}\177\4\2C\\c|\17\2C\\c|\u00c2\u00d8"+
		"\u00da\u00f8\u00fa\u0301\u0372\u037f\u0381\u2001\u200e\u200f\u2072\u2191"+
		"\u2c02\u2ff1\u3003\ud801\uf902\ufdd1\ufdf2\uffff\5\2\13\f\17\17\"\"\2"+
		"\u00dd\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2"+
		"\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3"+
		"\2\2\2\2\31\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2"+
		"%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61"+
		"\3\2\2\2\29\3\2\2\2\2=\3\2\2\2\3?\3\2\2\2\5A\3\2\2\2\7C\3\2\2\2\tE\3\2"+
		"\2\2\13G\3\2\2\2\rI\3\2\2\2\17K\3\2\2\2\21M\3\2\2\2\23O\3\2\2\2\25Q\3"+
		"\2\2\2\27S\3\2\2\2\31V\3\2\2\2\33Z\3\2\2\2\35\\\3\2\2\2\37^\3\2\2\2!`"+
		"\3\2\2\2#b\3\2\2\2%d\3\2\2\2\'r\3\2\2\2)t\3\2\2\2+\177\3\2\2\2-\u008a"+
		"\3\2\2\2/\u0095\3\2\2\2\61\u0099\3\2\2\2\63\u00a1\3\2\2\2\65\u00a6\3\2"+
		"\2\2\67\u00a8\3\2\2\29\u00b5\3\2\2\2;\u00c1\3\2\2\2=\u00c4\3\2\2\2?@\7"+
		"~\2\2@\4\3\2\2\2AB\7\61\2\2B\6\3\2\2\2CD\7*\2\2D\b\3\2\2\2EF\7+\2\2F\n"+
		"\3\2\2\2GH\7}\2\2H\f\3\2\2\2IJ\7.\2\2J\16\3\2\2\2KL\7\177\2\2L\20\3\2"+
		"\2\2MN\7#\2\2N\22\3\2\2\2OP\7]\2\2P\24\3\2\2\2QR\7_\2\2R\26\3\2\2\2ST"+
		"\7=\2\2T\30\3\2\2\2UW\5\33\16\2VU\3\2\2\2WX\3\2\2\2XV\3\2\2\2XY\3\2\2"+
		"\2Y\32\3\2\2\2Z[\t\2\2\2[\34\3\2\2\2\\]\7`\2\2]\36\3\2\2\2^_\7B\2\2_ "+
		"\3\2\2\2`a\7%\2\2a\"\3\2\2\2bc\7c\2\2c$\3\2\2\2de\7,\2\2e&\3\2\2\2fg\7"+
		"n\2\2gs\7v\2\2hi\7i\2\2is\7v\2\2jk\7n\2\2ks\7g\2\2lm\7i\2\2ms\7g\2\2n"+
		"o\7g\2\2os\7s\2\2pq\7p\2\2qs\7g\2\2rf\3\2\2\2rh\3\2\2\2rj\3\2\2\2rl\3"+
		"\2\2\2rn\3\2\2\2rp\3\2\2\2s(\3\2\2\2tz\7$\2\2uy\n\3\2\2vw\7^\2\2wy\t\4"+
		"\2\2xu\3\2\2\2xv\3\2\2\2y|\3\2\2\2zx\3\2\2\2z{\3\2\2\2{}\3\2\2\2|z\3\2"+
		"\2\2}~\7$\2\2~*\3\2\2\2\177\u0085\7)\2\2\u0080\u0084\n\5\2\2\u0081\u0082"+
		"\7^\2\2\u0082\u0084\t\6\2\2\u0083\u0080\3\2\2\2\u0083\u0081\3\2\2\2\u0084"+
		"\u0087\3\2\2\2\u0085\u0083\3\2\2\2\u0085\u0086\3\2\2\2\u0086\u0088\3\2"+
		"\2\2\u0087\u0085\3\2\2\2\u0088\u0089\7)\2\2\u0089,\3\2\2\2\u008a\u008f"+
		"\7>\2\2\u008b\u008e\n\7\2\2\u008c\u008e\5\65\33\2\u008d\u008b\3\2\2\2"+
		"\u008d\u008c\3\2\2\2\u008e\u0091\3\2\2\2\u008f\u008d\3\2\2\2\u008f\u0090"+
		"\3\2\2\2\u0090\u0092\3\2\2\2\u0091\u008f\3\2\2\2\u0092\u0093\7@\2\2\u0093"+
		".\3\2\2\2\u0094\u0096\5\67\34\2\u0095\u0094\3\2\2\2\u0095\u0096\3\2\2"+
		"\2\u0096\u0097\3\2\2\2\u0097\u0098\7<\2\2\u0098\60\3\2\2\2\u0099\u009b"+
		"\7A\2\2\u009a\u009c\t\b\2\2\u009b\u009a\3\2\2\2\u009c\u009d\3\2\2\2\u009d"+
		"\u009b\3\2\2\2\u009d\u009e\3\2\2\2\u009e\62\3\2\2\2\u009f\u00a2\5;\36"+
		"\2\u00a0\u00a2\7a\2\2\u00a1\u009f\3\2\2\2\u00a1\u00a0\3\2\2\2\u00a2\64"+
		"\3\2\2\2\u00a3\u00a7\5\63\32\2\u00a4\u00a7\7/\2\2\u00a5\u00a7\5\33\16"+
		"\2\u00a6\u00a3\3\2\2\2\u00a6\u00a4\3\2\2\2\u00a6\u00a5\3\2\2\2\u00a7\66"+
		"\3\2\2\2\u00a8\u00b1\5;\36\2\u00a9\u00ac\5\65\33\2\u00aa\u00ac\7\60\2"+
		"\2\u00ab\u00a9\3\2\2\2\u00ab\u00aa\3\2\2\2\u00ac\u00af\3\2\2\2\u00ad\u00ab"+
		"\3\2\2\2\u00ad\u00ae\3\2\2\2\u00ae\u00b0\3\2\2\2\u00af\u00ad\3\2\2\2\u00b0"+
		"\u00b2\5\65\33\2\u00b1\u00ad\3\2\2\2\u00b1\u00b2\3\2\2\2\u00b28\3\2\2"+
		"\2\u00b3\u00b6\5\63\32\2\u00b4\u00b6\5\33\16\2\u00b5\u00b3\3\2\2\2\u00b5"+
		"\u00b4\3\2\2\2\u00b6\u00bf\3\2\2\2\u00b7\u00ba\5\65\33\2\u00b8\u00ba\7"+
		"\60\2\2\u00b9\u00b7\3\2\2\2\u00b9\u00b8\3\2\2\2\u00ba\u00bd\3\2\2\2\u00bb"+
		"\u00b9\3\2\2\2\u00bb\u00bc\3\2\2\2\u00bc\u00be\3\2\2\2\u00bd\u00bb\3\2"+
		"\2\2\u00be\u00c0\5\65\33\2\u00bf\u00bb\3\2\2\2\u00bf\u00c0\3\2\2\2\u00c0"+
		":\3\2\2\2\u00c1\u00c2\t\t\2\2\u00c2<\3\2\2\2\u00c3\u00c5\t\n\2\2\u00c4"+
		"\u00c3\3\2\2\2\u00c5\u00c6\3\2\2\2\u00c6\u00c4\3\2\2\2\u00c6\u00c7\3\2"+
		"\2\2\u00c7\u00c8\3\2\2\2\u00c8\u00c9\b\37\2\2\u00c9>\3\2\2\2\27\2Xrxz"+
		"\u0083\u0085\u008d\u008f\u0095\u009d\u00a1\u00a6\u00ab\u00ad\u00b1\u00b5"+
		"\u00b9\u00bb\u00bf\u00c6\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}