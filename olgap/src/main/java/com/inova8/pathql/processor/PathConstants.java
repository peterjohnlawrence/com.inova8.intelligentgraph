/*
 * inova8 2020
 */
package com.inova8.pathql.processor;

import java.util.HashMap;
import static org.eclipse.rdf4j.model.util.Values.iri;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.query.algebra.Compare.CompareOp;

/**
 * The Class PathConstants.
 */
public final class PathConstants {
 
 /** The maxcardinality. */
 static public Integer MAXCARDINALITY = 20;
	
	/**
	 * The Enum ErrorCode.
	 */
	static public enum ErrorCode {
		
		/** The lexer. */
		LEXER, 
 
 /** The parser. */
 PARSER
	}

	/**
	 * The Enum EdgeCode.
	 */
	static public enum EdgeCode {

		/** The predicate. */
		PREDICATE,

		/** The filter. */
		FILTER,

		/** The dereified. */
		DEREIFIED
	}

	/**
	 * The Enum Operator.
	 */
	static public enum Operator {

		/** The predicate. */
		PREDICATE,

		/** The sequence. */
		SEQUENCE,

		/** The alternative. */
		ALTERNATIVE,

		/** The negation. */
		NEGATION,

		/** The filter. */
		FILTER,

		/** The predicateobject. */
		PREDICATEOBJECT,

		/** The iriref. */
		IRIREF,

		/** The literal. */
		LITERAL,

		/** The filteroperator. */
		FILTEROPERATOR,

		/** The propertylist. */
		PROPERTYLIST,

		/** The objectlist. */
		OBJECTLIST,

		/** The verbobjectlist. */
		VERBOBJECTLIST,

		/** The object. */
		OBJECT,

		/** The cardinality. */
		CARDINALITY,

		/** The binding. */
		BINDING, 
 
 /** The queryoptions. */
 QUERYOPTIONS
	}

	/**
	 * The Enum FilterOperator.
	 */
	static public enum FilterOperator {

		/** The lt. */
		LT, 
 
 /** The gt. */
 GT, 
 
 /** The le. */
 LE, 
 
 /** The ge. */
 GE, 
 
 /** The eq. */
 EQ, 
 
 /** The ne. */
 NE, 
 
 /** The like. */
 LIKE, 
 
 /** The query. */
 QUERY, 
 
 /** The property. */
 PROPERTY
	}

	/** The filter operators. */
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

	/** The filter lookup. */
	static HashMap<FilterOperator, String> filterLookup;
	static {
		filterLookup = new HashMap<FilterOperator, String>();
		getFilterLookup().put(FilterOperator.LT, "lt");
		getFilterLookup().put(FilterOperator.GT, "gt");
		getFilterLookup().put(FilterOperator.LE, "le");
		getFilterLookup().put(FilterOperator.GE, "ge");
		getFilterLookup().put(FilterOperator.EQ, "eq");
		getFilterLookup().put(FilterOperator.NE, "ne");
		getFilterLookup().put(FilterOperator.LIKE, "like");
		getFilterLookup().put(FilterOperator.QUERY, "query");
		getFilterLookup().put(FilterOperator.PROPERTY, "property");
	}
	
	/** The compare operators. */
	public static HashMap<FilterOperator, CompareOp> compareOperators;
	static {
		compareOperators = new HashMap<FilterOperator, CompareOp>();
		compareOperators.put(FilterOperator.LT, CompareOp.LT);
		compareOperators.put(FilterOperator.GT, CompareOp.GT);
		compareOperators.put(FilterOperator.LE, CompareOp.LE);
		compareOperators.put(FilterOperator.GE, CompareOp.GE);
		compareOperators.put(FilterOperator.EQ, CompareOp.EQ);
		compareOperators.put(FilterOperator.NE, CompareOp.NE);
	}

	/**
	 * Gets the filter lookup.
	 *
	 * @return the filter lookup
	 */
	static HashMap<FilterOperator, String> getFilterLookup() {
		return filterLookup;
	}

	/**
	 * Gets the filter lookup.
	 *
	 * @param filterOperator the filter operator
	 * @return the filter lookup
	 */
	public static String getFilterLookup(FilterOperator filterOperator) {
		return filterLookup.get(filterOperator);
	}

	/** The Constant RDF_STATEMENT. */
	public static final String RDF_STATEMENT = "http://www.w3.org/1999/02/22-rdf-syntax-ns#Statement";

	/** The Constant RDF_OBJECT. */
	public static final String RDF_OBJECT = "http://www.w3.org/1999/02/22-rdf-syntax-ns#object";

	/** The Constant RDF_PREDICATE. */
	public static final String RDF_PREDICATE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate";

	/** The Constant RDF_SUBJECT. */
	public static final String RDF_SUBJECT = "http://www.w3.org/1999/02/22-rdf-syntax-ns#subject";

	/** The Constant RDF_ISOBJECTOF. */
	public static final String RDF_ISOBJECTOF = "http://www.w3.org/1999/02/22-rdf-syntax-ns#object";

	/** The Constant RDF_ISPREDICATEOF. */
	public static final String RDF_ISPREDICATEOF = "http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate";

	/** The Constant RDF_ISSUBJECTOF. */
	public static final String RDF_ISSUBJECTOF = "http://www.w3.org/1999/02/22-rdf-syntax-ns#subject";

	/** The Constant RDF_STATEMENT_IRI. */
	public static final Resource RDF_STATEMENT_IRI = iri("http://www.w3.org/1999/02/22-rdf-syntax-ns#Statement");

	/** The Constant RDF_OBJECT_IRI. */
	public static final Resource RDF_OBJECT_IRI = iri("http://www.w3.org/1999/02/22-rdf-syntax-ns#object");

	/** The Constant RDF_PREDICATE_IRI. */
	public static final Resource RDF_PREDICATE_IRI = iri("http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate");

	/** The Constant RDF_SUBJECT_IRI. */
	public static final Resource RDF_SUBJECT_IRI = iri("http://www.w3.org/1999/02/22-rdf-syntax-ns#subject");

	/** The Constant RDF_ISOBJECTOF_IRI. */
	public static final Resource RDF_ISOBJECTOF_IRI = iri("http://www.w3.org/1999/02/22-rdf-syntax-ns#object");

	/** The Constant RDF_ISPREDICATEOF_IRI. */
	public static final Resource RDF_ISPREDICATEOF_IRI = iri("http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate");

	/** The Constant RDF_ISSUBJECTOF_IRI. */
	public static final Resource RDF_ISSUBJECTOF_IRI = iri("http://www.w3.org/1999/02/22-rdf-syntax-ns#subject");
}
