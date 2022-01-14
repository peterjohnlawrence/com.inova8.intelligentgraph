// Generated from PathPattern.g4 by ANTLR 4.9.3
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
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
			"T__9", "T__10", "T__11", "T__12", "T__13", "KEY", "INTEGER", "BINDVARIABLE", 
			"DIGIT", "INVERSE", "REIFIER", "DEREIFIER", "RDFTYPE", "ANYPREDICATE", 
			"OPERATOR", "DQLITERAL", "SQLITERAL", "IRI_REF", "PNAME_NS", "VARNAME", 
			"PN_CHARS_U", "PN_CHARS", "PN_PREFIX", "PN_LOCAL", "PN_CHARS_BASE", "WS"
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2 \u00f8\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\3\2\3\2\3\3\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3"+
		"\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\17"+
		"\3\17\3\20\3\20\6\20i\n\20\r\20\16\20j\3\21\6\21n\n\21\r\21\16\21o\3\22"+
		"\3\22\6\22t\n\22\r\22\16\22u\3\23\3\23\3\24\3\24\3\25\3\25\3\26\3\26\3"+
		"\27\3\27\3\30\3\30\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3"+
		"\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3"+
		"\31\3\31\3\31\3\31\3\31\5\31\u00a1\n\31\3\32\3\32\3\32\3\32\7\32\u00a7"+
		"\n\32\f\32\16\32\u00aa\13\32\3\32\3\32\3\33\3\33\3\33\3\33\7\33\u00b2"+
		"\n\33\f\33\16\33\u00b5\13\33\3\33\3\33\3\34\3\34\3\34\7\34\u00bc\n\34"+
		"\f\34\16\34\u00bf\13\34\3\34\3\34\3\35\5\35\u00c4\n\35\3\35\3\35\3\36"+
		"\3\36\6\36\u00ca\n\36\r\36\16\36\u00cb\3\37\3\37\5\37\u00d0\n\37\3 \3"+
		" \3 \5 \u00d5\n \3!\3!\3!\7!\u00da\n!\f!\16!\u00dd\13!\3!\5!\u00e0\n!"+
		"\3\"\3\"\5\"\u00e4\n\"\3\"\3\"\7\"\u00e8\n\"\f\"\16\"\u00eb\13\"\3\"\5"+
		"\"\u00ee\n\"\3#\3#\3$\6$\u00f3\n$\r$\16$\u00f4\3$\3$\2\2%\3\3\5\4\7\5"+
		"\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23"+
		"%\2\'\24)\25+\26-\27/\30\61\31\63\32\65\33\67\349\35;\36=\2?\2A\2C\37"+
		"E\2G \3\2\f\4\2C\\c|\3\2\62;\6\2\f\f\17\17$$^^\4\2$$^^\6\2\f\f\17\17)"+
		")^^\4\2))^^\t\2$$>>@@^^``bb}\177\4\2<<\u0080\u0080\17\2C\\c|\u00c2\u00d8"+
		"\u00da\u00f8\u00fa\u0301\u0372\u037f\u0381\u2001\u200e\u200f\u2072\u2191"+
		"\u2c02\u2ff1\u3003\ud801\uf902\ufdd1\ufdf2\uffff\5\2\13\f\17\17\"\"\2"+
		"\u0110\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2"+
		"\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3"+
		"\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2"+
		"\2#\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2"+
		"\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2"+
		"\2\2C\3\2\2\2\2G\3\2\2\2\3I\3\2\2\2\5K\3\2\2\2\7N\3\2\2\2\tP\3\2\2\2\13"+
		"R\3\2\2\2\rT\3\2\2\2\17V\3\2\2\2\21X\3\2\2\2\23Z\3\2\2\2\25\\\3\2\2\2"+
		"\27^\3\2\2\2\31`\3\2\2\2\33b\3\2\2\2\35d\3\2\2\2\37f\3\2\2\2!m\3\2\2\2"+
		"#q\3\2\2\2%w\3\2\2\2\'y\3\2\2\2){\3\2\2\2+}\3\2\2\2-\177\3\2\2\2/\u0081"+
		"\3\2\2\2\61\u00a0\3\2\2\2\63\u00a2\3\2\2\2\65\u00ad\3\2\2\2\67\u00b8\3"+
		"\2\2\29\u00c3\3\2\2\2;\u00c7\3\2\2\2=\u00cf\3\2\2\2?\u00d4\3\2\2\2A\u00d6"+
		"\3\2\2\2C\u00e3\3\2\2\2E\u00ef\3\2\2\2G\u00f2\3\2\2\2IJ\7?\2\2J\4\3\2"+
		"\2\2KL\7`\2\2LM\7`\2\2M\6\3\2\2\2NO\7\61\2\2O\b\3\2\2\2PQ\7@\2\2Q\n\3"+
		"\2\2\2RS\7~\2\2S\f\3\2\2\2TU\7*\2\2U\16\3\2\2\2VW\7+\2\2W\20\3\2\2\2X"+
		"Y\7}\2\2Y\22\3\2\2\2Z[\7.\2\2[\24\3\2\2\2\\]\7\177\2\2]\26\3\2\2\2^_\7"+
		"#\2\2_\30\3\2\2\2`a\7]\2\2a\32\3\2\2\2bc\7_\2\2c\34\3\2\2\2de\7=\2\2e"+
		"\36\3\2\2\2fh\7(\2\2gi\t\2\2\2hg\3\2\2\2ij\3\2\2\2jh\3\2\2\2jk\3\2\2\2"+
		"k \3\2\2\2ln\5%\23\2ml\3\2\2\2no\3\2\2\2om\3\2\2\2op\3\2\2\2p\"\3\2\2"+
		"\2qs\7\'\2\2rt\5%\23\2sr\3\2\2\2tu\3\2\2\2us\3\2\2\2uv\3\2\2\2v$\3\2\2"+
		"\2wx\t\3\2\2x&\3\2\2\2yz\7`\2\2z(\3\2\2\2{|\7B\2\2|*\3\2\2\2}~\7%\2\2"+
		"~,\3\2\2\2\177\u0080\7c\2\2\u0080.\3\2\2\2\u0081\u0082\7,\2\2\u0082\60"+
		"\3\2\2\2\u0083\u0084\7n\2\2\u0084\u00a1\7v\2\2\u0085\u0086\7i\2\2\u0086"+
		"\u00a1\7v\2\2\u0087\u0088\7n\2\2\u0088\u00a1\7g\2\2\u0089\u008a\7i\2\2"+
		"\u008a\u00a1\7g\2\2\u008b\u008c\7g\2\2\u008c\u00a1\7s\2\2\u008d\u008e"+
		"\7p\2\2\u008e\u00a1\7g\2\2\u008f\u0090\7n\2\2\u0090\u0091\7k\2\2\u0091"+
		"\u0092\7m\2\2\u0092\u00a1\7g\2\2\u0093\u0094\7s\2\2\u0094\u0095\7w\2\2"+
		"\u0095\u0096\7g\2\2\u0096\u0097\7t\2\2\u0097\u00a1\7{\2\2\u0098\u0099"+
		"\7r\2\2\u0099\u009a\7t\2\2\u009a\u009b\7q\2\2\u009b\u009c\7r\2\2\u009c"+
		"\u009d\7g\2\2\u009d\u009e\7t\2\2\u009e\u009f\7v\2\2\u009f\u00a1\7{\2\2"+
		"\u00a0\u0083\3\2\2\2\u00a0\u0085\3\2\2\2\u00a0\u0087\3\2\2\2\u00a0\u0089"+
		"\3\2\2\2\u00a0\u008b\3\2\2\2\u00a0\u008d\3\2\2\2\u00a0\u008f\3\2\2\2\u00a0"+
		"\u0093\3\2\2\2\u00a0\u0098\3\2\2\2\u00a1\62\3\2\2\2\u00a2\u00a8\7$\2\2"+
		"\u00a3\u00a7\n\4\2\2\u00a4\u00a5\7^\2\2\u00a5\u00a7\t\5\2\2\u00a6\u00a3"+
		"\3\2\2\2\u00a6\u00a4\3\2\2\2\u00a7\u00aa\3\2\2\2\u00a8\u00a6\3\2\2\2\u00a8"+
		"\u00a9\3\2\2\2\u00a9\u00ab\3\2\2\2\u00aa\u00a8\3\2\2\2\u00ab\u00ac\7$"+
		"\2\2\u00ac\64\3\2\2\2\u00ad\u00b3\7)\2\2\u00ae\u00b2\n\6\2\2\u00af\u00b0"+
		"\7^\2\2\u00b0\u00b2\t\7\2\2\u00b1\u00ae\3\2\2\2\u00b1\u00af\3\2\2\2\u00b2"+
		"\u00b5\3\2\2\2\u00b3\u00b1\3\2\2\2\u00b3\u00b4\3\2\2\2\u00b4\u00b6\3\2"+
		"\2\2\u00b5\u00b3\3\2\2\2\u00b6\u00b7\7)\2\2\u00b7\66\3\2\2\2\u00b8\u00bd"+
		"\7>\2\2\u00b9\u00bc\n\b\2\2\u00ba\u00bc\5? \2\u00bb\u00b9\3\2\2\2\u00bb"+
		"\u00ba\3\2\2\2\u00bc\u00bf\3\2\2\2\u00bd\u00bb\3\2\2\2\u00bd\u00be\3\2"+
		"\2\2\u00be\u00c0\3\2\2\2\u00bf\u00bd\3\2\2\2\u00c0\u00c1\7@\2\2\u00c1"+
		"8\3\2\2\2\u00c2\u00c4\5A!\2\u00c3\u00c2\3\2\2\2\u00c3\u00c4\3\2\2\2\u00c4"+
		"\u00c5\3\2\2\2\u00c5\u00c6\t\t\2\2\u00c6:\3\2\2\2\u00c7\u00c9\7A\2\2\u00c8"+
		"\u00ca\t\2\2\2\u00c9\u00c8\3\2\2\2\u00ca\u00cb\3\2\2\2\u00cb\u00c9\3\2"+
		"\2\2\u00cb\u00cc\3\2\2\2\u00cc<\3\2\2\2\u00cd\u00d0\5E#\2\u00ce\u00d0"+
		"\7a\2\2\u00cf\u00cd\3\2\2\2\u00cf\u00ce\3\2\2\2\u00d0>\3\2\2\2\u00d1\u00d5"+
		"\5=\37\2\u00d2\u00d5\7/\2\2\u00d3\u00d5\5%\23\2\u00d4\u00d1\3\2\2\2\u00d4"+
		"\u00d2\3\2\2\2\u00d4\u00d3\3\2\2\2\u00d5@\3\2\2\2\u00d6\u00df\5E#\2\u00d7"+
		"\u00da\5? \2\u00d8\u00da\7\60\2\2\u00d9\u00d7\3\2\2\2\u00d9\u00d8\3\2"+
		"\2\2\u00da\u00dd\3\2\2\2\u00db\u00d9\3\2\2\2\u00db\u00dc\3\2\2\2\u00dc"+
		"\u00de\3\2\2\2\u00dd\u00db\3\2\2\2\u00de\u00e0\5? \2\u00df\u00db\3\2\2"+
		"\2\u00df\u00e0\3\2\2\2\u00e0B\3\2\2\2\u00e1\u00e4\5=\37\2\u00e2\u00e4"+
		"\5%\23\2\u00e3\u00e1\3\2\2\2\u00e3\u00e2\3\2\2\2\u00e4\u00ed\3\2\2\2\u00e5"+
		"\u00e8\5? \2\u00e6\u00e8\7\60\2\2\u00e7\u00e5\3\2\2\2\u00e7\u00e6\3\2"+
		"\2\2\u00e8\u00eb\3\2\2\2\u00e9\u00e7\3\2\2\2\u00e9\u00ea\3\2\2\2\u00ea"+
		"\u00ec\3\2\2\2\u00eb\u00e9\3\2\2\2\u00ec\u00ee\5? \2\u00ed\u00e9\3\2\2"+
		"\2\u00ed\u00ee\3\2\2\2\u00eeD\3\2\2\2\u00ef\u00f0\t\n\2\2\u00f0F\3\2\2"+
		"\2\u00f1\u00f3\t\13\2\2\u00f2\u00f1\3\2\2\2\u00f3\u00f4\3\2\2\2\u00f4"+
		"\u00f2\3\2\2\2\u00f4\u00f5\3\2\2\2\u00f5\u00f6\3\2\2\2\u00f6\u00f7\b$"+
		"\2\2\u00f7H\3\2\2\2\31\2jou\u00a0\u00a6\u00a8\u00b1\u00b3\u00bb\u00bd"+
		"\u00c3\u00cb\u00cf\u00d4\u00d9\u00db\u00df\u00e3\u00e7\u00e9\u00ed\u00f4"+
		"\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}