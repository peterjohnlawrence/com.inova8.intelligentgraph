package recursivePathParser_Deprecated;

import java.util.HashMap;

import org.eclipse.rdf4j.model.IRI;

import pathCalc.Source;

public enum Parser {
	/*
	predicate := iri
	reification  := iri
	predicatePath :=  '[' predicate ']'	| '[' reification , predicate ']' | 	predicate 
	inversePredicatePath := '^' predicatePath 
	sequencePath:= pathElement '/' pathElement 
	alternativePath:= pathElement '|' pathElement 
	nestedPath := '(' pathElement ')
	pathElement := predicatePath | inversePredicatePath | sequencePath | alternativePath | nestedPath
	edgeElement := pathElement ( '<' nodeElement  '>' )?
	query := pathElement+
	*/

	QUERY{
	    public TreeNode<PathQueryNode> parse(String s) throws ParseException {
	        return PATHELEMENT.parse(s);
	    }	
	},
	PATHELEMENT{
		public TreeNode<PathQueryNode> parse(String s) throws ParseException {
			s= s.trim();
			if( s.startsWith("(")  && s.endsWith(")")) {
				return NESTEDPATH.parse(s);
			}else if( s.split("|").length >0 ){
				return ALTERNATIVEPATH.parse(s);
			}else if( s.split("/").length >0){
				return SEQUENCEPATH.parse(s);
			}else if( s.startsWith("^")){
				return INVERSEPREDICATEPATH.parse(s);
			}else {		
				return PREDICATEPATH.parse(s);		
			}
	    }		
	},
	PREDICATEPATH{
		public TreeNode<PathQueryNode> parse(String s) throws ParseException {
			s= s.trim();
			PathQueryNode predicatePathNode = new PathQueryNode();
			if( s.startsWith("[")  && s.endsWith("]")) {
				s = s.substring(1, s.length()-1).trim();
				String[] parts = s.split(",");
				if(parts.length==0) {
					predicatePathNode.predicate= parseIRI(s);
				}else {
					predicatePathNode.reification = parseIRI(parts[0]);
					predicatePathNode.predicate= parseIRI(parts[0]);
				}			
			}else {
				predicatePathNode.predicate= parseIRI(s);
			}
			return new TreeNode<PathQueryNode>(predicatePathNode);
	    }	
	},
	INVERSEPREDICATEPATH{
		public TreeNode<PathQueryNode> parse(String s) throws ParseException {
			s.substring(1).trim();
			PathQueryNode inversePredicatePathNode = new PathQueryNode();
			inversePredicatePathNode.nodeType = PathQueryNode.NodeType.INVERSEPREDICATEPATH;
			TreeNode<PathQueryNode> inverseNode = new  TreeNode<PathQueryNode>(inversePredicatePathNode);
			inverseNode.children.add( PREDICATEPATH.parse(s));
			return inverseNode;
	    }	
	},
	SEQUENCEPATH{
		public TreeNode<PathQueryNode> parse(String s) throws ParseException {
			return PATHELEMENT.parse(s);
	    }	
	},
	ALTERNATIVEPATH{
		public TreeNode<PathQueryNode> parse(String s) throws ParseException {
			return PATHELEMENT.parse(s);
	    }	
	},
	NESTEDPATH{
		public TreeNode<PathQueryNode> parse(String s) throws ParseException {
			return PATHELEMENT.parse(s);
	    }	
	};
	IRI parseIRI(String s) throws ParseException {		
			String[] predicateIRIParts = s.split(":");
			IRI predicate = null;
			if(predicateIRIParts[0].equals("http")||predicateIRIParts[0].equals("urn")) {
				predicate = Source.createIRI(s);
			}else {
				String namespace = prefixes.get(predicateIRIParts[0]);
				if(namespace==null) {
				//	addTrace(new ParameterizedMessage("Error identifying namespace of qName {}", s));
				}else {
					predicate = Source.createIRI( namespace, predicateIRIParts[1]);
				}
			}
			return predicate;
		}
	
	abstract TreeNode<PathQueryNode> parse(String s) throws ParseException;
	private static HashMap<String,String> prefixes;// = new HashMap<String,String>();
	
	
}
