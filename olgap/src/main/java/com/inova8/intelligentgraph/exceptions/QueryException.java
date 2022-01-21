package com.inova8.intelligentgraph.exceptions;

public class QueryException extends HandledException{

	private static final long serialVersionUID = 1L;
	public QueryException(String code) {
		super(code);
	}
	public QueryException(String code,String message) {
		super(code,message);
	}

}
