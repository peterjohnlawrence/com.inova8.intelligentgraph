// Generated from PathPattern.g4 by ANTLR 4.4
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
	static { RuntimeMetaData.checkVersion("4.4", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__11=1, T__10=2, T__9=3, T__8=4, T__7=5, T__6=6, T__5=7, T__4=8, T__3=9, 
		T__2=10, T__1=11, T__0=12, LITERAL=13, IRI_REF=14, PNAME_NS=15, PNAME_LN=16, 
		VARNAME=17, PN_PREFIX=18, PN_LOCAL=19, WS=20;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"'\\u0000'", "'\\u0001'", "'\\u0002'", "'\\u0003'", "'\\u0004'", "'\\u0005'", 
		"'\\u0006'", "'\\u0007'", "'\b'", "'\t'", "'\n'", "'\\u000B'", "'\f'", 
		"'\r'", "'\\u000E'", "'\\u000F'", "'\\u0010'", "'\\u0011'", "'\\u0012'", 
		"'\\u0013'", "'\\u0014'"
	};
	public static final String[] ruleNames = {
		"T__11", "T__10", "T__9", "T__8", "T__7", "T__6", "T__5", "T__4", "T__3", 
		"T__2", "T__1", "T__0", "LITERAL", "IRI_REF", "PNAME_NS", "PNAME_LN", 
		"VARNAME", "PN_CHARS_U", "PN_CHARS", "PN_PREFIX", "PN_LOCAL", "PN_CHARS_BASE", 
		"DIGIT", "WS"
	};


	public PathPatternLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "PathPattern.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\26\u00a0\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\3\2\3\2\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7"+
		"\3\b\3\b\3\b\3\t\3\t\3\t\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3"+
		"\16\3\16\7\16V\n\16\f\16\16\16Y\13\16\3\16\3\16\3\17\3\17\3\17\7\17`\n"+
		"\17\f\17\16\17c\13\17\3\17\3\17\3\20\5\20h\n\20\3\20\3\20\3\21\3\21\3"+
		"\21\3\22\6\22p\n\22\r\22\16\22q\3\23\3\23\5\23v\n\23\3\24\3\24\3\24\5"+
		"\24{\n\24\3\25\3\25\3\25\7\25\u0080\n\25\f\25\16\25\u0083\13\25\3\25\5"+
		"\25\u0086\n\25\3\26\3\26\5\26\u008a\n\26\3\26\3\26\7\26\u008e\n\26\f\26"+
		"\16\26\u0091\13\26\3\26\5\26\u0094\n\26\3\27\3\27\3\30\3\30\3\31\6\31"+
		"\u009b\n\31\r\31\16\31\u009c\3\31\3\31\2\2\32\3\3\5\4\7\5\t\6\13\7\r\b"+
		"\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\2\'\2)\24"+
		"+\25-\2/\2\61\26\3\2\b\6\2\f\f\17\17$$^^\4\2$$^^\t\2$$>>@@^^``bb}\177"+
		"\4\2C\\c|\17\2C\\c|\u00c2\u00d8\u00da\u00f8\u00fa\u0301\u0372\u037f\u0381"+
		"\u2001\u200e\u200f\u2072\u2191\u2c02\u2ff1\u3003\ud801\uf902\ufdd1\ufdf2"+
		"\uffff\5\2\13\f\17\17\"\"\u00ac\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2"+
		"\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2"+
		"\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2"+
		"\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2\61\3\2\2\2"+
		"\3\63\3\2\2\2\5\65\3\2\2\2\7\67\3\2\2\2\t=\3\2\2\2\13?\3\2\2\2\rA\3\2"+
		"\2\2\17C\3\2\2\2\21F\3\2\2\2\23I\3\2\2\2\25K\3\2\2\2\27M\3\2\2\2\31O\3"+
		"\2\2\2\33Q\3\2\2\2\35\\\3\2\2\2\37g\3\2\2\2!k\3\2\2\2#o\3\2\2\2%u\3\2"+
		"\2\2\'z\3\2\2\2)|\3\2\2\2+\u0089\3\2\2\2-\u0095\3\2\2\2/\u0097\3\2\2\2"+
		"\61\u009a\3\2\2\2\63\64\7A\2\2\64\4\3\2\2\2\65\66\7c\2\2\66\6\3\2\2\2"+
		"\678\7&\2\289\7v\2\29:\7j\2\2:;\7k\2\2;<\7u\2\2<\b\3\2\2\2=>\7]\2\2>\n"+
		"\3\2\2\2?@\7=\2\2@\f\3\2\2\2AB\7}\2\2B\16\3\2\2\2CD\7@\2\2DE\7@\2\2E\20"+
		"\3\2\2\2FG\7>\2\2GH\7>\2\2H\22\3\2\2\2IJ\7_\2\2J\24\3\2\2\2KL\7\177\2"+
		"\2L\26\3\2\2\2MN\7`\2\2N\30\3\2\2\2OP\7\60\2\2P\32\3\2\2\2QW\7$\2\2RV"+
		"\n\2\2\2ST\7^\2\2TV\t\3\2\2UR\3\2\2\2US\3\2\2\2VY\3\2\2\2WU\3\2\2\2WX"+
		"\3\2\2\2XZ\3\2\2\2YW\3\2\2\2Z[\7$\2\2[\34\3\2\2\2\\a\7>\2\2]`\n\4\2\2"+
		"^`\5\'\24\2_]\3\2\2\2_^\3\2\2\2`c\3\2\2\2a_\3\2\2\2ab\3\2\2\2bd\3\2\2"+
		"\2ca\3\2\2\2de\7@\2\2e\36\3\2\2\2fh\5)\25\2gf\3\2\2\2gh\3\2\2\2hi\3\2"+
		"\2\2ij\7<\2\2j \3\2\2\2kl\5\37\20\2lm\5+\26\2m\"\3\2\2\2np\t\5\2\2on\3"+
		"\2\2\2pq\3\2\2\2qo\3\2\2\2qr\3\2\2\2r$\3\2\2\2sv\5-\27\2tv\7a\2\2us\3"+
		"\2\2\2ut\3\2\2\2v&\3\2\2\2w{\5%\23\2x{\7/\2\2y{\5/\30\2zw\3\2\2\2zx\3"+
		"\2\2\2zy\3\2\2\2{(\3\2\2\2|\u0085\5-\27\2}\u0080\5\'\24\2~\u0080\7\60"+
		"\2\2\177}\3\2\2\2\177~\3\2\2\2\u0080\u0083\3\2\2\2\u0081\177\3\2\2\2\u0081"+
		"\u0082\3\2\2\2\u0082\u0084\3\2\2\2\u0083\u0081\3\2\2\2\u0084\u0086\5\'"+
		"\24\2\u0085\u0081\3\2\2\2\u0085\u0086\3\2\2\2\u0086*\3\2\2\2\u0087\u008a"+
		"\5%\23\2\u0088\u008a\5/\30\2\u0089\u0087\3\2\2\2\u0089\u0088\3\2\2\2\u008a"+
		"\u0093\3\2\2\2\u008b\u008e\5\'\24\2\u008c\u008e\7\60\2\2\u008d\u008b\3"+
		"\2\2\2\u008d\u008c\3\2\2\2\u008e\u0091\3\2\2\2\u008f\u008d\3\2\2\2\u008f"+
		"\u0090\3\2\2\2\u0090\u0092\3\2\2\2\u0091\u008f\3\2\2\2\u0092\u0094\5\'"+
		"\24\2\u0093\u008f\3\2\2\2\u0093\u0094\3\2\2\2\u0094,\3\2\2\2\u0095\u0096"+
		"\t\6\2\2\u0096.\3\2\2\2\u0097\u0098\4\62;\2\u0098\60\3\2\2\2\u0099\u009b"+
		"\t\7\2\2\u009a\u0099\3\2\2\2\u009b\u009c\3\2\2\2\u009c\u009a\3\2\2\2\u009c"+
		"\u009d\3\2\2\2\u009d\u009e\3\2\2\2\u009e\u009f\b\31\2\2\u009f\62\3\2\2"+
		"\2\23\2UW_agquz\177\u0081\u0085\u0089\u008d\u008f\u0093\u009c\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}