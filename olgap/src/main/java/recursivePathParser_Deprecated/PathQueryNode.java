package recursivePathParser_Deprecated;

import org.eclipse.rdf4j.model.IRI;

public class PathQueryNode {

	enum NodeType {
		PREDICATE, INVERSEPREDICATEPATH, ALTERNATIVEPATH, NESTEDPATH, SEQUENCEPATH
	}

	NodeType nodeType;
	IRI reification;
	IRI predicate;
}
