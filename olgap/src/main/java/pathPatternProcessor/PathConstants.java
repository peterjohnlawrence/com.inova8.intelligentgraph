package pathPatternProcessor;

import java.util.HashMap;
import static org.eclipse.rdf4j.model.util.Values.iri;
import org.eclipse.rdf4j.model.Resource;

public final class PathConstants {

	static public enum ErrorCode {
		LEXER, PARSER
	}
	static public enum EdgeCode {
		PREDICATE,FILTER, DEREIFIED
	}	
	static public enum Operator {
		PREDICATE, SEQUENCE, ALTERNATIVE, NEGATION, FILTER, PREDICATEOBJECT, IRIREF, LITERAL, FILTEROPERATOR,PROPERTYLIST,OBJECTLIST,VERBOBJECTLIST, OBJECT, CARDINALITY, BINDING
	}
	static public enum FilterOperator {
		LT, GT, LE, GE, EQ, NE, LIKE, QUERY, PROPERTY
	}

	static HashMap<String, FilterOperator> filterOperators;
	static {
		filterOperators = new HashMap<String, FilterOperator>();
		filterOperators.put("lt", FilterOperator.LT);
		filterOperators.put("gt", FilterOperator.GT);
		filterOperators.put("le", FilterOperator.LE);
		filterOperators.put("ge", FilterOperator.GE);
		filterOperators.put("eq", FilterOperator.EQ);
		filterOperators.put("ne", FilterOperator.NE);
		filterOperators.put("like", FilterOperator.LIKE);
		filterOperators.put("query", FilterOperator.QUERY);
		filterOperators.put("property", FilterOperator.PROPERTY);
	}
	
	static HashMap<FilterOperator, String> filterLookup;
	static {
		filterLookup = new HashMap<FilterOperator, String>();
		getFilterLookup().put(FilterOperator.LT, "lt");
		getFilterLookup().put( FilterOperator.GT, "gt");
		getFilterLookup().put(FilterOperator.LE, "le");
		getFilterLookup().put( FilterOperator.GE, "ge");
		getFilterLookup().put(	FilterOperator.EQ, "eq");
		getFilterLookup().put(FilterOperator.NE, "ne");
		getFilterLookup().put(FilterOperator.LIKE, "like");
		getFilterLookup().put(FilterOperator.QUERY, "query");
		getFilterLookup().put(FilterOperator.PROPERTY, "property");
	}
	 static HashMap<FilterOperator, String> getFilterLookup() {
		return filterLookup;
	}
	public static  String getFilterLookup(FilterOperator filterOperator) {
		return filterLookup.get(filterOperator);
	}	
	public static final String RDF_STATEMENT = "http://www.w3.org/1999/02/22-rdf-syntax-ns#Statement";
	public static final String RDF_OBJECT = "http://www.w3.org/1999/02/22-rdf-syntax-ns#object";
	public static final String RDF_PREDICATE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate";
	public static final String RDF_SUBJECT = "http://www.w3.org/1999/02/22-rdf-syntax-ns#subject";
	public static final String RDF_ISOBJECTOF = "http://www.w3.org/1999/02/22-rdf-syntax-ns#object";
	public static final String RDF_ISPREDICATEOF = "http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate";
	public static final String RDF_ISSUBJECTOF = "http://www.w3.org/1999/02/22-rdf-syntax-ns#subject";

	public static final Resource RDF_STATEMENT_IRI = iri("http://www.w3.org/1999/02/22-rdf-syntax-ns#Statement");
	public static final Resource  RDF_OBJECT_IRI = iri("http://www.w3.org/1999/02/22-rdf-syntax-ns#object");
	public static final Resource RDF_PREDICATE_IRI = iri("http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate");
	public static final Resource RDF_SUBJECT_IRI = iri("http://www.w3.org/1999/02/22-rdf-syntax-ns#subject");
	public static final Resource RDF_ISOBJECTOF_IRI = iri("http://www.w3.org/1999/02/22-rdf-syntax-ns#object");
	public static final Resource RDF_ISPREDICATEOF_IRI = iri("http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate");
	public static final Resource RDF_ISSUBJECTOF_IRI = iri("http://www.w3.org/1999/02/22-rdf-syntax-ns#subject");
}
