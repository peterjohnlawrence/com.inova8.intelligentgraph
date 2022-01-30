/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.utilities;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.util.Values;

import com.inova8.intelligentgraph.constants.IntelligentGraphConstants;
import com.inova8.intelligentgraph.context.CustomQueryOptions;
import com.inova8.pathql.context.Prefixes;
import com.inova8.pathql.utilities.Utilities;

import static java.util.stream.Collectors.*;
import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;

/**
 * The Class CustomQueryOption.
 */
public class CustomQueryOption {
	
	/**
	 * Gets the custom query options.
	 *
	 * @param contexts the contexts
	 * @param prefixes the prefixes
	 * @return the custom query options
	 */
	public static CustomQueryOptions getCustomQueryOptions(org.eclipse.rdf4j.model.Resource[] contexts, Prefixes prefixes) {
		CustomQueryOptions customQueryOptions= new CustomQueryOptions();
		if(contexts!=null) {
			for(org.eclipse.rdf4j.model.Resource context:contexts) {
				if(context.stringValue().startsWith(IntelligentGraphConstants.URN_CUSTOM_QUERY_OPTIONS) ) {
					String queryString = null;
					try {
						queryString = URLDecoder.decode(context.stringValue(), StandardCharsets.UTF_8.toString());
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					customQueryOptions = CustomQueryOption.splitQuery(queryString,prefixes);
					
					return customQueryOptions;
				}
			}
			return customQueryOptions;
		}
		else 
			return null;
	}
	
	/**
	 * Gets the core contexts.
	 *
	 * @param contexts the contexts
	 * @return the core contexts
	 */
	public static ArrayList<Resource> getCoreContexts(org.eclipse.rdf4j.model.Resource[] contexts) {	
		ArrayList<Resource> coreContexts = new ArrayList<Resource>() ;
		if(contexts!=null) {
			for(org.eclipse.rdf4j.model.Resource context:contexts) {
				if(!context.stringValue().startsWith(IntelligentGraphConstants.URN_CUSTOM_QUERY_OPTIONS) ) {
					coreContexts.add(context);
				}
			}
			return coreContexts;
		}
		else 
			return coreContexts;
	}
	
	/**
	 * Split query.
	 *
	 * @param query the query
	 * @param prefixes the prefixes
	 * @return the custom query options
	 */
	private static CustomQueryOptions splitQuery(String query,Prefixes prefixes) {
		if (query == null || query.isEmpty()) {
			return null;
		}
		query = query.substring(query.indexOf("?") + 1);
		CustomQueryOptions customQueryOptions= new CustomQueryOptions();
		for(String queryParam: query.split("&")) {
			 Pair<String, Value> pair = splitQueryParameter(queryParam, prefixes) ;
			 customQueryOptions.add(pair.a, pair.b); 
		}
		return customQueryOptions;
	}

	/**
	 * Split query parameter.
	 *
	 * @param parameter the parameter
	 * @param prefixes the prefixes
	 * @return the pair
	 */
	private static Pair<String, Value> splitQueryParameter(String parameter,Prefixes prefixes) {
		final String enc = "UTF-8";
		List<String> keyValue = Arrays.stream(parameter.split("=")).map(e -> {
			try {
				return URLDecoder.decode(e, enc);
			} catch (UnsupportedEncodingException ex) {
				throw new RuntimeUnsupportedEncodingException(ex);
			}
		}).collect(toList());

		if (keyValue.size() == 2) {
			String value = keyValue.get(1);
			String[] valueParts = value.split("\\^\\^");
			Pair<String,Value> pair;
			String valueString = valueParts[0];
			if(valueString.startsWith("<")) {
				valueString=valueString.substring(1,valueString.length()-1);
				pair = new Pair<String,Value>(keyValue.get(0),  iri(valueString));
				return pair;
			} else {
				if(valueString.startsWith("'")||valueString.startsWith("\""))
					valueString=valueString.substring(1,valueString.length()-1);
				if (valueParts.length == 2) {
					IRI qname = convertQName(valueParts[1],prefixes);
					Literal typedLiteral = Values.literal(Values.getValueFactory(), valueString, qname);
					pair = new Pair<String,Value>(keyValue.get(0),  typedLiteral);
				} else {
					Literal literal = literal(valueString);
					pair = new Pair<String,Value>(keyValue.get(0),  literal);
				}
	
				return pair;
			}
		} else {
			return new Pair<String,Value>(keyValue.get(0), null);
		}
	}
	
	/**
	 * Convert Q name.
	 *
	 * @param predicateIRI the predicate IRI
	 * @param localPrefixes the local prefixes
	 * @return the iri
	 */
	private static IRI convertQName(String predicateIRI, Prefixes localPrefixes) {
		predicateIRI = Utilities.trimIRIString(predicateIRI);
		String[] predicateIRIParts = predicateIRI.split(":|~");
		IRI predicate = null;
		if (predicateIRIParts[0].equals("http") || predicateIRIParts[0].equals("urn")) {
			predicate = iri(predicateIRI);
		} else {
			IRI namespace = getNamespace(predicateIRIParts[0], localPrefixes);
			if (namespace == null) {
				return null;
			} else {
				predicate = iri(namespace.stringValue(), predicateIRIParts[1]);
			}
		}
		return predicate;
	}

	/**
	 * Gets the namespace.
	 *
	 * @param namespaceString the namespace string
	 * @param localPrefixes the local prefixes
	 * @return the namespace
	 */
	private static IRI getNamespace(String namespaceString, Prefixes  localPrefixes) {
		IRI namespace = null;
		if (localPrefixes != null) {
			namespace = localPrefixes.get(namespaceString);
			if (namespace != null)
				return namespace;
		}
		return namespace;
	}
	
	/**
	 * The Class RuntimeUnsupportedEncodingException.
	 */
	public static class RuntimeUnsupportedEncodingException extends RuntimeException {
		
		/** The Constant serialVersionUID. */
		private static final long serialVersionUID = 5437438619565978536L;

		/**
		 * Instantiates a new runtime unsupported encoding exception.
		 *
		 * @param cause the cause
		 */
		public RuntimeUnsupportedEncodingException(Throwable cause) {
			super(cause);
		}
	}

	/**
	 * The Class Pair.
	 *
	 * @param <U> the generic type
	 * @param <V> the value type
	 */
	private static class Pair<U, V> {
		
		/** The a. */
		U a;
		
		/** The b. */
		V b;

		/**
		 * Instantiates a new pair.
		 *
		 * @param u the u
		 * @param v the v
		 */
		public Pair(U u, V v) {
			this.a = u;
			this.b = v;
		}

		/**
		 * Gets the 0.
		 *
		 * @return the 0
		 */
		@SuppressWarnings("unused")
		public U get0() {
			return a;
		}

		/**
		 * Gets the 1.
		 *
		 * @return the 1
		 */
		@SuppressWarnings("unused")
		public V get1() {
			return b;
		}
	}
}
