package pathPatternProcessor;

import static org.eclipse.rdf4j.model.util.Values.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.logging.log4j.message.ParameterizedMessage;
import org.eclipse.rdf4j.model.IRI;

import PathPattern.PathPatternBaseVisitor;
import PathPattern.PathPatternParser;
import PathPattern.PathPatternParser.BindingContext;
import PathPattern.PathPatternParser.CardinalityContext;
import PathPattern.PathPatternParser.DereifierContext;
import PathPattern.PathPatternParser.FactFilterPatternContext;
import PathPattern.PathPatternParser.IriRefContext;
import PathPattern.PathPatternParser.LiteralContext;
import PathPattern.PathPatternParser.ObjectContext;
import PathPattern.PathPatternParser.ObjectListContext;
import PathPattern.PathPatternParser.OperatorContext;
import PathPattern.PathPatternParser.PathAlternativeContext;
import PathPattern.PathPatternParser.PathContext;
import PathPattern.PathPatternParser.PathEltOrInverseContext;
import PathPattern.PathPatternParser.PathParenthesesContext;
import PathPattern.PathPatternParser.PathSequenceContext;
import PathPattern.PathPatternParser.Pname_nsContext;
import PathPattern.PathPatternParser.PredicateContext;
import PathPattern.PathPatternParser.PredicateRefContext;
import PathPattern.PathPatternParser.PropertyListNotEmptyContext;
import PathPattern.PathPatternParser.QnameContext;
import PathPattern.PathPatternParser.RdfTypeContext;
import PathPattern.PathPatternParser.ReifiedPredicateContext;
import PathPattern.PathPatternParser.VerbContext;
import PathPattern.PathPatternParser.VerbObjectListContext;
import pathCalc.Thing;
import pathPatternElement.AlternativePathElement;
import pathPatternElement.BoundPathElement;
import pathPatternElement.FactFilterElement;
import pathPatternElement.CardinalityElement;
import pathPatternElement.FilterOperatorValueElement;
import pathPatternElement.IriRefValueElement;
import pathPatternElement.LiteralValueElement;
import pathPatternElement.ObjectElement;
import pathPatternElement.ObjectListValueElement;
import pathPatternElement.PathElement;
import pathPatternElement.PredicateElement;
import pathPatternElement.SequencePathElement;
import pathPatternElement.ValueElement;
import pathPatternElement.VerbObjectList;
import pathQLRepository.PathQLRepository;

public class PathPatternVisitor extends PathPatternBaseVisitor<PathElement> {
	Thing thing;
//	private PathQLRepository source;

	public PathPatternVisitor(Thing thing) {
		super();
		this.thing = thing;
	}
	public PathPatternVisitor() {
		super();
//		this.source = source;
	}
	private HashMap<String, IRI> getPrefixes() {
		if (thing!=null) 
			return thing.getPrefixes();
		else
			return PathQLRepository.getPrefixes();
	}
	@Override 
	public PathElement visitBoundPattern(PathPatternParser.BoundPatternContext ctx) { 
		// boundPattern :  binding ('/'|'>') pathPatterns EOF 
		PathElement pathElement = new BoundPathElement();
		pathElement.setLeftPathElement( (FactFilterElement) visit(ctx.binding()));
		pathElement.setRightPathElement(visit(ctx.pathPatterns()));
		return pathElement;	
	}
	@Override 
	public PathElement visitMatchOnlyPattern(PathPatternParser.MatchOnlyPatternContext ctx) { 
		// matchOnlyPattern: pathPattern :  binding  EOF 
		PathElement pathElement = new BoundPathElement();
		pathElement.setLeftPathElement( (FactFilterElement) visit(ctx.binding()));
		return pathElement;	
	}
	@Override 
	public PathElement visitPathOnlyPattern(PathPatternParser.PathOnlyPatternContext ctx) { 
		//pathOnlyPattern: pathPatterns  EOF #pathOnlyPattern
		PathElement pathPatterns = visit(ctx.pathPatterns());
		return pathPatterns;
		}
	@Override
	public FactFilterElement  visitBinding(BindingContext ctx) {
		//binding : factFilterPattern ('/'|'>') ;	
		return (FactFilterElement) visit(ctx.factFilterPattern());
	}
	@Override
	public PathElement visitPath(PathContext ctx) {
		//pathPattern :  pathEltOrInverse cardinality?  #Path  
		
		PathElement pathElement =  visit(ctx.pathEltOrInverse());
       if(ctx.cardinality()!=null ) {
    	   CardinalityElement cardinalityElement = (CardinalityElement) visit(ctx.cardinality());
    	   pathElement.setMinCardinality(cardinalityElement.getMinCardinality());
    	   pathElement.setMaxCardinality(cardinalityElement.getMaxCardinality());
    	   pathElement.setUnboundedCardinality(cardinalityElement.getUnboundedCardinality());
       }
		return pathElement;
	}

	@Override
	public CardinalityElement visitCardinality(CardinalityContext ctx) {
		//cardinality :	  '{'  INTEGER (',' ( INTEGER )? )?  '}'  
		CardinalityElement cardinalityElement = new CardinalityElement();
		cardinalityElement.setMinCardinality(Integer.decode(ctx.getChild(1).getText()));
		 String maxCardinalityText = ctx.getChild(3).getText();
		 if(maxCardinalityText.equals("}")) {
			 cardinalityElement.setUnboundedCardinality(true);
		 }else {
			 cardinalityElement.setMaxCardinality(Integer.decode(maxCardinalityText));
		 }
		return cardinalityElement;
	}

	@Override
	public SequencePathElement visitPathSequence(PathSequenceContext ctx) {
		//pathPatterns :   pathPatterns '/'  pathPatterns  #PathSequence
		if (ctx.pathPatterns().size() == 1) {
			return (SequencePathElement)visit(ctx.pathPatterns(0));
		} else {
			SequencePathElement pathSequenceElement = new SequencePathElement();
			pathSequenceElement.setLeftPathElement( visit(ctx.pathPatterns(0)));
			pathSequenceElement.setRightPathElement( visit(ctx.pathPatterns(1)));
			return pathSequenceElement;
		}
	}

	@Override
	public AlternativePathElement visitPathAlternative(PathAlternativeContext ctx) {
		// pathPatterns :  pathPatterns '|'  pathPatterns  #PathAlternative 
		if (ctx.pathPatterns().size() == 1) {
			return (AlternativePathElement) visit(ctx.pathPatterns(0));
		} else {
			AlternativePathElement pathAlternativeElement = new AlternativePathElement();
			pathAlternativeElement.setLeftPathElement( visit(ctx.pathPatterns(0)));
			pathAlternativeElement.setRightPathElement( visit(ctx.pathPatterns(1)));
			return pathAlternativeElement;
		}
	}

	@Override
	public PathElement visitPathParentheses(PathParenthesesContext ctx) {
		// pathPatterns :    '(' pathPatterns ')' cardinality?  #PathParentheses;
		PathElement pathParentheses = visit(ctx.pathPatterns());
		if (ctx.negation() != null) {
			pathParentheses.setIsNegated(true);
		} else {
			pathParentheses.setIsNegated(false);
		}
		if (ctx.cardinality() != null) {
			CardinalityElement cardinalityElement = (CardinalityElement) visit(ctx.cardinality());
			pathParentheses.setMinCardinality(cardinalityElement.getMinCardinality());
			pathParentheses.setMaxCardinality(cardinalityElement.getMaxCardinality());
			pathParentheses.setUnboundedCardinality(cardinalityElement.getUnboundedCardinality());
		}
		return pathParentheses;
	}

	@Override
	public PredicateElement visitPathEltOrInverse(PathEltOrInverseContext ctx) {
		// pathEltOrInverse : negation? INVERSE? predicate ;
		PredicateElement pathEltOrInverseElement = (PredicateElement) visit(ctx.predicate());
		if (ctx.negation() != null) {
			pathEltOrInverseElement.setIsNegated(true);
		} else {
			pathEltOrInverseElement.setIsNegated(false);
		}
		if (ctx.INVERSE() != null) {
			pathEltOrInverseElement.setIsInverseOf(true);
		} else {
			pathEltOrInverseElement.setIsInverseOf(false);
		}

		return pathEltOrInverseElement;
	}

	@Override
	public PredicateElement visitPredicate(PredicateContext ctx) {
		// predicate :   ( reifiedPredicate | predicateRef | rdfType | '*' ) factFilterPattern? #objectFilterPattern
		PredicateElement predicateElement = null;
		if (ctx.reifiedPredicate() != null) {
			predicateElement = (PredicateElement) visit(ctx.reifiedPredicate());
			predicateElement.setOperator(PathConstants.Operator.PREDICATE);
		} else if (ctx.predicateRef() != null) {
			predicateElement = new PredicateElement();
			predicateElement.setOperator(PathConstants.Operator.PREDICATE);
			predicateElement.setPredicate(((IriRefValueElement) visit(ctx.predicateRef())).getIri());
		}else if (ctx.rdfType() != null) {
			predicateElement = new PredicateElement();
			predicateElement.setOperator(PathConstants.Operator.PREDICATE);
			predicateElement.setPredicate((iri("http://rdftype")));
		}else if (ctx.anyPredicate() != null) {
			predicateElement = new PredicateElement();
			predicateElement.setOperator(PathConstants.Operator.PREDICATE);
			predicateElement.setAnyPredicate(true);
		}
		if (ctx.factFilterPattern() != null) {
			if (ctx.reifiedPredicate() != null) {
				predicateElement.setStatementFilterElement((FactFilterElement) visit(ctx.factFilterPattern()));
			}else {
				predicateElement.setObjectFilterElement((FactFilterElement) visit(ctx.factFilterPattern()));
			}
		}

		return predicateElement;
	}

	@Override
	public PredicateElement visitReifiedPredicate(ReifiedPredicateContext ctx) {
		// reifiedPredicate :  iriRef? REIFIER predicateRef  factFilterPattern?  dereifier? ; #statementFilterPattern
		PredicateElement reifiedPredicateElement = new PredicateElement();
		reifiedPredicateElement.setIsReified(true);
		if (ctx.iriRef() != null) {
			reifiedPredicateElement.setReification(((IriRefValueElement) visit(ctx.iriRef())).getIri());
		}
		if (ctx.predicateRef() != null) {
			reifiedPredicateElement.setPredicate(((IriRefValueElement) visit(ctx.predicateRef())).getIri());
		}
		if (ctx.factFilterPattern() != null) {
			reifiedPredicateElement.setObjectFilterElement((FactFilterElement) visit(ctx.factFilterPattern()));
		}
		if (ctx.dereifier() != null) {
			reifiedPredicateElement.setIsDereified(true);
		}
		return reifiedPredicateElement;
	}

	@Override
	public IriRefValueElement visitPredicateRef(PredicateRefContext ctx) {
		// predicateRef :  IRI_REF  | rdfType  |  qname | pname_ns ;
		IriRefValueElement predicateRefElement;
		if (ctx.qname() != null) {
			predicateRefElement = (IriRefValueElement) visit(ctx.qname());
		} else if (ctx.pname_ns() != null) {
			predicateRefElement = (IriRefValueElement) visit(ctx.pname_ns());

		} else if(ctx.rdfType() != null) {
			predicateRefElement = new IriRefValueElement();
			predicateRefElement.setIri(iri("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"));
		}else {
			predicateRefElement = new IriRefValueElement();
			String iri = ctx.IRI_REF().getText();
			iri = iri.substring(1, iri.length() - 1);
			predicateRefElement.setIri(iri(iri));
		}
		return predicateRefElement;
	}

	@Override
	public IriRefValueElement visitIriRef(IriRefContext ctx) {
		// iriRef  : IRI_REF  |  qname | pname_ns ;  
		IriRefValueElement iriRefElement = new IriRefValueElement();
		if (ctx.qname() != null) {
			iriRefElement.setIri(((IriRefValueElement)visit(ctx.qname())).getIri());
		} else if (ctx.pname_ns() != null) {
			iriRefElement.setIri(((IriRefValueElement)visit(ctx.pname_ns())).getIri());

		} else {
			String iri = ctx.IRI_REF().getText();
			iri= iri.substring(1,iri.length()-1);
			iriRefElement.setIri(iri(iri));
		}
		return iriRefElement;
	}

	@Override
	public PathElement visitDereifier(DereifierContext ctx) {
		//dereifier : DEREIFIER ; 
		return super.visitDereifier(ctx);
	}

	@Override
	public FactFilterElement visitFactFilterPattern(FactFilterPatternContext ctx) {
		// factFilterPattern : '['  propertyListNotEmpty   ']';
		return (FactFilterElement) visit(ctx.propertyListNotEmpty());
	}

	@Override
	public FactFilterElement visitPropertyListNotEmpty(PropertyListNotEmptyContext ctx) {
		// propertyListNotEmpty :   verbObjectList ( ';' ( verbObjectList )? )* ;  
		FactFilterElement propertyListNotEmptyElement = new FactFilterElement();
		ArrayList<VerbObjectList> propertyListNotEmpty = new ArrayList<VerbObjectList>();
		for (VerbObjectListContext verbObjectListContext : ctx.verbObjectList()) {

			PathElement verb = visit(verbObjectListContext.verb());
			ObjectListValueElement objectList =  (ObjectListValueElement) visit(verbObjectListContext.objectList());
			
			VerbObjectList verbObjectList = new VerbObjectList();
			if(verb instanceof ValueElement) {
				verbObjectList.setFilterOperator(((FilterOperatorValueElement) verb).getFilterOperator());
			}else if(verb instanceof PredicateElement) {
				verbObjectList.setPredicate(((PredicateElement) verb));
			}
			verbObjectList.setObjectList(objectList.getObjectList());
			propertyListNotEmpty.add(verbObjectList);
		}
		propertyListNotEmptyElement.setPropertyListNotEmpty(propertyListNotEmpty);
		return propertyListNotEmptyElement;
	}

	@Override
	public PathElement visitVerb(VerbContext ctx) {
		// verb : operator | pathEltOrInverse 
		if(ctx.operator()!=null ) {
			return (ValueElement) visit(ctx.operator());
			
		}else if(ctx.pathEltOrInverse()!=null ) {
			
			return (PredicateElement) visit(ctx.pathEltOrInverse());
		}
		return null;
	}

	@Override
	public ObjectListValueElement visitObjectList(ObjectListContext ctx) {
		// objectList : object ( ',' object )*;
		// object : iriRef  | literal | blankNodePropertyListPath ;
		ArrayList<ObjectElement> objectList = new ArrayList<ObjectElement>();
		for( ObjectContext objectContext: ctx.object()) {
			if(objectContext.iriRef()!=null) {
				objectList.add((ObjectElement) visit(objectContext.iriRef()));
			}else if(objectContext.literal()!=null) {
				objectList.add((ObjectElement) visit(objectContext.literal()));
			}else if(objectContext.factFilterPattern()!=null) {	
				objectList.add((ObjectElement) visit(objectContext.factFilterPattern()));
			}		
		}
		ObjectListValueElement objectListElement = new ObjectListValueElement();
		objectListElement.setObjectList(objectList);
		return objectListElement;
	}

	@Override
	public ValueElement visitObject(ObjectContext ctx) {
		// object : iriRef  | literal | blankNodePropertyListPath ;
		//ValueElement objectElement = null ;
		if (ctx.iriRef() != null) {
			return (ValueElement) visit(ctx.iriRef()) ;
		} else if (ctx.literal() != null) {
			return (ValueElement) visit(ctx.literal());//objectElement;
		} else if (ctx.factFilterPattern() != null) {
			return (ValueElement) visit(ctx.factFilterPattern());
		}
		return null;
	}

	@Override
	public IriRefValueElement visitQname(QnameContext ctx) {
		//qname : PNAME_NS PN_LOCAL; 
		IriRefValueElement qnameElement = new IriRefValueElement();
		IRI qname = PathQLRepository.convertQName(ctx.getText(),getPrefixes());
		if (qname!=null) {
			qnameElement.setIri(qname);
			return qnameElement;
		}else {
			thing.addTrace(new ParameterizedMessage("Error identifying namespace of qName {}", ctx.getText()));
			return null;
		}
	}

	@Override
	public IriRefValueElement visitPname_ns(Pname_nsContext ctx) {
		// pname_ns : PNAME_NS ;   
		IriRefValueElement pname_nsElement = new IriRefValueElement();
		IRI qname = PathQLRepository.convertQName(ctx.getText(),thing.getPrefixes());
		if (qname!=null) {
			pname_nsElement.setIri(qname);
			return pname_nsElement;
		}else {
			thing.addTrace(new ParameterizedMessage("Error identifying namespace of qName {}", ctx.getText()));
			return null;
		}
	}

	@Override
	public LiteralValueElement visitLiteral(LiteralContext ctx) {
		//literal : LITERAL ;
		LiteralValueElement literalElement = new LiteralValueElement();
		String literalValue = ctx.getText();
		literalValue =  literalValue.substring(1, literalValue.length() - 1); 
		literalElement.setLiteral(literal(literalValue));
		return literalElement;
	}

	@Override
	public PathElement visitVerbObjectList(VerbObjectListContext ctx) {
		// TODO Auto-generated method stub
		return super.visitVerbObjectList(ctx);
	}

	@Override
	public FilterOperatorValueElement visitOperator(OperatorContext ctx) {
		// operator : OPERATOR ;
		// OPERATOR : 'lt'|'gt'|'le'|'ge'|'eq'|'ne'|'like';
		FilterOperatorValueElement operatorElement = new FilterOperatorValueElement();
		operatorElement.setFilterOperator(PathConstants.filterOperators.get(ctx.getText()));
		return operatorElement;
	}

	@Override
	public PathElement visitRdfType(RdfTypeContext ctx) {
		//rdfType : RDFTYPE ;
		return super.visitRdfType(ctx);
	}

}
