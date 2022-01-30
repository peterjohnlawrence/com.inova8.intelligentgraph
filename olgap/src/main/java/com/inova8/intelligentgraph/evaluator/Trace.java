/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.evaluator;

/**
 * The Class Trace.
 */
public class Trace {

/** The trace. */
//	private StringBuilder htmlTrace = new StringBuilder("<ol style='list-style-type:none;'>");
	private String trace;
	
	/**
	 * Instantiates a new trace.
	 *
	 * @param trace the trace
	 */
	public Trace( String trace) {
		this.trace = trace;
	}
	
	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public	String toString() {
		return asHTML();
	}
	
	/**
	 * To HTML.
	 *
	 * @param s the s
	 * @return the string
	 */
	@SuppressWarnings("unused")
	private static String toHTML(String s) {
		StringBuilder builder = new StringBuilder();
		boolean previousWasASpace = false;
		for (char c : s.toCharArray()) {
			if (c == ' ') {
				if (previousWasASpace) {
					builder.append("&nbsp;");
					previousWasASpace = false;
					continue;
				}
				previousWasASpace = true;
			} else {
				previousWasASpace = false;
			}
			switch (c) {
			case '<':
				builder.append("&lt;");
				break;
			case '>':
				builder.append("&gt;");
				break;
			case '&':
				builder.append("&amp;");
				break;
			case '"':
				builder.append("&quot;");
				break;
			case '\n':
				builder.append("<br>");
				break;
			// We need Tab support here, because we print StackTraces as HTML
			case '\t':
				builder.append("&nbsp; &nbsp; &nbsp;");
				break;
			default:
				if (c < 128) {
					builder.append(c);
				} else {
					builder.append("&#").append((int) c).append(";");
				}
			}
		}
		return builder.toString();
	}
	
	/**
	 * As HTML.
	 *
	 * @return the string
	 */
	public String asHTML() {
		return trace;
	}
	
	/**
	 * As text.
	 *
	 * @return the string
	 */
	public String asText() {
		return new net.htmlparser.jericho.Source(trace).getRenderer().setMaxLineLength(Integer.MAX_VALUE).setNewLine(null).toString();
	}
}
