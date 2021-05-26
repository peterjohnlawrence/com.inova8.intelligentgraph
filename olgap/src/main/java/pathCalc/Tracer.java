/*
 * inova8 2020
 */
package pathCalc;

import org.apache.commons.lang3.StringUtils;

/**
 * The Class Tracer.
 */
public class Tracer {
	
	/** The trace. */
	private StringBuilder trace = new StringBuilder("<ol style='list-style-type:none;'>");
	
	/** The level. */
	private int level =0;
	
	/** The tracing. */
	private boolean tracing =false;
	
	
	/**
	 * Adds the trace.
	 *
	 * @param message the message
	 * @return the tracer
	 */
	public Tracer addTrace(String message) {
		if(tracing) {
			this.trace.append("<li>").append(message).append("</li>").append("\r\n");

		}
		return this;	
	}
	
	/**
	 * Checks if is tracing.
	 *
	 * @return true, if is tracing
	 */
	public boolean isTracing() {
		return tracing;
	}
	
	/**
	 * Sets the tracing.
	 *
	 * @param tracing the tracing
	 * @return the tracer
	 */
	public Tracer setTracing(boolean tracing) {
		this.tracing = tracing;
		return this;
	}
	
	/**
	 * Gets the level.
	 *
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}
	
	/**
	 * Sets the level.
	 *
	 * @param level the level
	 * @return the tracer
	 */
	public Tracer setLevel(int level) {
		if(tracing)
			this.level = level;
		return this;
	}
	
	/**
	 * Decrement level.
	 *
	 * @return the tracer
	 */
	public Tracer decrementLevel() {
	//	if(tracing)this.level--;
		if(tracing)
			this.trace.append("</ol>");
		return this;
	}
	
	/**
	 * Increment level.
	 *
	 * @return the tracer
	 */
	public Tracer incrementLevel() {
	//	if(tracing)this.level++;
		if(tracing)
			this.trace.append("<ol style='list-style-type:none;'>");;
		return this;
	}
	
	/**
	 * Gets the trace.
	 *
	 * @return the trace
	 */
	public String getTrace() {
		if(tracing) {
			return this.trace.append("</ol>").toString();
		}
		else {
			return "empty";
		}
	}
	
	/**
	 * Indent script for trace.
	 *
	 * @param script the script
	 * @return the string
	 */
	public String indentScriptForTrace(String script) {
		//String indentedScriptString = ("\n"+script).replace("\n", "\n"+StringUtils.repeat("\t",getLevel()+1));
		String indentedScriptString = ("\n"+script).replace("\n", "\n"+StringUtils.repeat("\t",getLevel()+1));
		return "<code>"+ indentedScriptString + "</code>";
	}
	
	/**
	 * Adds the script.
	 *
	 * @param script the script
	 */
	public void addScript(String script) {
		if(tracing) {
			this.trace.append("<li>").append("<div  style='border: 1px solid black;'> <pre><code >"+ toHTML(script) + "</code></pre></div>").append("</li>").append("\r\n");
		}
	}
	
	/**
	 * To HTML.
	 *
	 * @param s the s
	 * @return the string
	 */
	public static String toHTML(String s) {
	    StringBuilder builder = new StringBuilder();
	    boolean previousWasASpace = false;
	    for( char c : s.toCharArray() ) {
	        if( c == ' ' ) {
	            if( previousWasASpace ) {
	                builder.append("&nbsp;");
	                previousWasASpace = false;
	                continue;
	            }
	            previousWasASpace = true;
	        } else {
	            previousWasASpace = false;
	        }
	        switch(c) {
	            case '<': builder.append("&lt;"); break;
	            case '>': builder.append("&gt;"); break;
	            case '&': builder.append("&amp;"); break;
	            case '"': builder.append("&quot;"); break;
	            case '\n': builder.append("<br>"); break;
	            // We need Tab support here, because we print StackTraces as HTML
	            case '\t': builder.append("&nbsp; &nbsp; &nbsp;"); break;  
	            default:
	                if( c < 128 ) {
	                    builder.append(c);
	                } else {
	                    builder.append("&#").append((int)c).append(";");
	                }    
	        }
	    }
	    return builder.toString();
	}

}
