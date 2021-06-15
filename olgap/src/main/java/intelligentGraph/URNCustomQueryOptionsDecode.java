package intelligentGraph;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.util.Values;

import pathCalc.CustomQueryOptions;
import pathCalc.Prefixes;
import pathQLRepository.PathQLRepository;

import static java.util.stream.Collectors.*;
import static org.eclipse.rdf4j.model.util.Values.iri;
import static org.eclipse.rdf4j.model.util.Values.literal;

public class URNCustomQueryOptionsDecode {
	public static CustomQueryOptions getCustomQueryOptions(org.eclipse.rdf4j.model.Resource[] contexts, Prefixes prefixes) {
		CustomQueryOptions customQueryOptions= new CustomQueryOptions();
		if(contexts!=null) {
			for(org.eclipse.rdf4j.model.Resource context:contexts) {
				if(context.stringValue().startsWith(IntelligentGraphSail.URN_CUSTOM_QUERY_OPTIONS) ) {
					String queryString = null;
					try {
						queryString = URLDecoder.decode(context.stringValue(), StandardCharsets.UTF_8.toString());
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					customQueryOptions = URNCustomQueryOptionsDecode.splitQuery(queryString,prefixes);
					
					return customQueryOptions;
				}
			}
			return customQueryOptions;
		}
		else 
			return null;
	}
	/**
	 * Decode parameters in query part of a URI into a map from parameter name
	 * to its parameter values. For parameters that occur multiple times each
	 * value is collected. Proper decoding of the parameters is performed.
	 * 
	 * Example
	 * 
	 * <pre>
	 * a=1&b=2&c=&a=4
	 * </pre>
	 * 
	 * is converted into
	 * 
	 * <pre>
	 * {a=[Optional[1], Optional[4]], b=[Optional[2]], c=[Optional.empty]}
	 * </pre>
	 * 
	 * @param query
	 *            the query part of an URI
	 * @return map of parameters names into a list of their values.
	 * 
	 */
	public static CustomQueryOptions splitQuery(String query,Prefixes prefixes) {
		if (query == null || query.isEmpty()) {
			return null;
		}
		query = query.substring(query.indexOf("?") + 1);
		//Map<String, List<Resource>> queryOptions = Arrays.stream(query.split("&")).map(p -> splitQueryParameter(p,prefixes)).collect(groupingBy(e -> e.get0(), // group by parameter name
		//		mapping(e -> e.get1(), toList())));// keep parameter values and assemble into list
		CustomQueryOptions customQueryOptions= new CustomQueryOptions();
		for(String queryParam: query.split("&")) {
			 Pair<String, Value> pair = splitQueryParameter(queryParam, prefixes) ;
			 customQueryOptions.add(pair.a, pair.b); 
		}
		return customQueryOptions;
	}

	public static Pair<String, Value> splitQueryParameter(String parameter,Prefixes prefixes) {
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
		} else {
			return new Pair<String,Value>(keyValue.get(0), null);
		}
	}
	public static IRI convertQName(String predicateIRI, ConcurrentHashMap<String, IRI> localPrefixes) {
		predicateIRI = PathQLRepository.trimIRIString(predicateIRI);
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
	 * @param namespaceString
	 *            the namespace string
	 * @param localPrefixes
	 *            the local prefixes
	 * @return the namespace
	 */
	private static IRI getNamespace(String namespaceString, ConcurrentHashMap<String, IRI> localPrefixes) {
		IRI namespace = null;
		if (localPrefixes != null) {
			namespace = localPrefixes.get(namespaceString);
			if (namespace != null)
				return namespace;
		}
		return namespace;
	}
	/**
	 * Runtime exception (instead of checked exception) to denote unsupported
	 * enconding
	 */
	public static class RuntimeUnsupportedEncodingException extends RuntimeException {
		private static final long serialVersionUID = 5437438619565978536L;

		public RuntimeUnsupportedEncodingException(Throwable cause) {
			super(cause);
		}
	}

	/**
	 * A simple pair of two elements
	 * 
	 * @param <U>
	 *            first element
	 * @param <V>
	 *            second element
	 */
	public static class Pair<U, V> {
		U a;
		V b;

		public Pair(U u, V v) {
			this.a = u;
			this.b = v;
		}

		public U get0() {
			return a;
		}

		public V get1() {
			return b;
		}
	}
}
