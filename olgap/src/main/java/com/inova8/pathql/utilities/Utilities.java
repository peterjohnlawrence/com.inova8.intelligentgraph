package com.inova8.pathql.utilities;

import static org.eclipse.rdf4j.model.util.Values.iri;

import org.eclipse.rdf4j.model.IRI;

public class Utilities {
	public static IRI trimAndCheckIRIString(String IRI) {
		return iri(trimIRIString(IRI));
	}

	public static String trimIRIString(String IRI) {
		IRI = IRI.trim();
		if (IRI.startsWith("<") && IRI.endsWith(">")) {
			IRI = IRI.substring(1, IRI.length() - 1);
			return IRI;
		}
		return IRI;
	}

//	private IRI getNamespace(String namespaceString, ConcurrentHashMap<String, IRI> localPrefixes) {
//		IRI namespace = null;
//		if (localPrefixes != null) {
//			namespace = localPrefixes.get(namespaceString);
//			if (namespace != null)
//				return namespace;
//		}
////		namespace = getPrefixes().get(namespaceString);
//		return namespace;
//	}
//	public static IRI convertQName(String predicateIRI, Prefixes prefixes) {
//	predicateIRI = Utilities.trimIRIString(predicateIRI);
//	String[] predicateIRIParts = predicateIRI.split(":");
//	IRI predicate = null;
//	if (predicateIRIParts[0].equals("a")) {
//		predicate = iri("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
//	} else if (predicateIRIParts[0].equals("http") || predicateIRIParts[0].equals("urn")) {
//		predicate = iri(predicateIRI);
//	} else {
//		IRI namespace = prefixes.get(predicateIRIParts[0]);
//		if (namespace == null) {
////TODO			logger.error(String.format("Error identifying namespace of qName %s", predicateIRI));
////TODO				this.getEvaluationContext().getTracer().traceQNameError(predicateIRI);
//			//	addTrace(message);
//		} else {
//			predicate = iri(namespace.stringValue(), predicateIRIParts[1]);
//		}
//	}
//	return predicate;
//}
}
