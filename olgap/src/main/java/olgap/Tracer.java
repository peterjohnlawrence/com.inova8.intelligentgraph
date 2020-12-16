package olgap;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.message.Message;

public class Tracer {
	private StringBuilder trace = new StringBuilder("<ol style='list-style-type:none;'>");
	private int level =0;
	private boolean tracing =false;
	public Tracer addTrace(Message message) {
		if(tracing) {
			String messageString = message.getFormattedMessage();
			this.trace.append("<li>").append(messageString).append("</li>").append("\r\n");
			//this.trace.append("<li>").append(message.getFormattedMessage()).append("</li>").append("\r\n");
		}
		return this;
	}
	public Tracer addTrace(String message) {
		if(tracing) {
			this.trace.append("<li>").append(message).append("</li>").append("\r\n");

		}
		return this;	
	}
	public boolean isTracing() {
		return tracing;
	}
	public Tracer setTracing(boolean tracing) {
		this.tracing = tracing;
		return this;
	}
	public int getLevel() {
		return level;
	}
	public Tracer setLevel(int level) {
		if(tracing)
			this.level = level;
		return this;
	}
	public Tracer decrementLevel() {
	//	if(tracing)this.level--;
		if(tracing)
			this.trace.append("</ol>");
		return this;
	}
	public Tracer incrementLevel() {
	//	if(tracing)this.level++;
		if(tracing)
			this.trace.append("<ol style='list-style-type:none;'>");;
		return this;
	}
	public String getTrace() {
		if(tracing) {
			return this.trace.append("</ol>").toString();
		}
		else {
			return null;
		}
	}
	public String indentScriptForTrace(String script) {
		//String indentedScriptString = ("\n"+script).replace("\n", "\n"+StringUtils.repeat("\t",getLevel()+1));
		String indentedScriptString = ("\n"+script).replace("\n", "\n"+StringUtils.repeat("\t",getLevel()+1));
		return "<code>"+ indentedScriptString + "</code>";
	}
	public void addScript(String script) {
		if(tracing) {
			this.trace.append("<li>").append("<div  style='border: 1px solid black;'> <pre><code >"+ toHTML(script) + "</code></pre></div>").append("</li>").append("\r\n");
		}
	}
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
