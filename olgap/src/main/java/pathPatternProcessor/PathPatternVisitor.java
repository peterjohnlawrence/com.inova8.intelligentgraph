/*
 * inova8 2020
 */
package pathPatternProcessor;

import static org.eclipse.rdf4j.model.util.Values.*;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.util.Values;

import Exceptions.ScriptFailedException;
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
import PathPattern.PathPatternParser.QueryOptionContext;
import PathPattern.PathPatternParser.QueryOptionsContext;
import PathPattern.PathPatternParser.QueryStringContext;
import PathPattern.PathPatternParser.RdfTypeContext;
import PathPattern.PathPatternParser.ReifiedPredicateContext;
import PathPattern.PathPatternParser.TypeContext;
import PathPattern.PathPatternParser.VerbContext;
import PathPattern.PathPatternParser.VerbObjectListContext;
import pathCalc.CustomQueryOptions;
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
import pathPatternElement.QueryOptionsPathElement;
import pathPatternElement.SequencePathElement;
import pathPatternElement.ValueElement;
import pathPatternElement.VerbObjectList;
import pathQLRepository.PathQLRepository;
import static org.eclipse.rdf4j.model.util.Values.literal;
/**
 * The Class PathPatternVisitor.
 */
public class PathPatternVisitor extends PathPatternBaseVisitor<PathElement> {
	
	/** The thing. */
	Thing thing;
	
	/** The source. */
	private PathQLRepository source;

	/**
 * Instantiates a new path pattern visitor.
 *
 * @param thing the thing
 */
public PathPatternVisitor(Thing thing) {
		super();
		this.thing = thing;
		this.source = thing.getSource();
	}
	
	@Override
	public PathElement visitQueryString(QueryStringContext ctx) {
		// queryString : pathPattern queryOptions? EOF ;
		PathElement pathElement =   visit(ctx.pathPattern()) ;
		if(ctx.queryOptions()!=null ) {
			pathElement.setCustomQueryOptions(visit(ctx.queryOptions()).getCustomQueryOptions()) ;
		}
		return pathElement;
	}

	@Override
	public QueryOptionsPathElement visitQueryOptions(QueryOptionsContext ctx) {
		// queryOptions : ( queryOption )+;
		// queryOption: KEY '=' literal ('^^' type )?;
		// KEY : '&' [a-zA-Z]+ ; 
		CustomQueryOptions customQueryOptions= new CustomQueryOptions();
		for( QueryOptionContext queryOption: ctx.queryOption()) {
			 String key = queryOption.KEY().getText().substring(1);
			 LiteralValueElement literal = (LiteralValueElement) visit(queryOption.literal());
			 if(queryOption.type()!=null) {
				  IriRefValueElement type = (IriRefValueElement) visit(queryOption.type()) ;
				   Literal typeLiteral = Values.literal(Values.getValueFactory(),literal.getLiteral().getLabel(),type.getIri());
				   customQueryOptions.add(key, typeLiteral);
			 }else {
				 customQueryOptions.add(key, literal.getLiteral());
			 }
		}
		QueryOptionsPathElement queryOptionsPathElement = new QueryOptionsPathElement(getSource());
		queryOptionsPathElement.setCustomQueryOptions(customQueryOptions);
		return queryOptionsPathElement;
	}

	@Override
	public IriRefValueElement visitType(TypeContext ctx) {
		//type : qname;
		return visitQname(ctx.qname());
	}

	/**
	 * Gets the source.
	 *
	 * @return the source
	 */
	public PathQLRepository getSource() {
		return source;
	}

	/**
	 * Instantiates a new path pattern visitor.
	 *
	 * @param source the source
	 */
	public PathPatternVisitor(PathQLRepository source) {
		super();
	this.source = source;
	}
	
	/**
	 * Gets the prefixes.
	 *
	 * @return the prefixes
	 */
	private ConcurrentHashMap<String, IRI> getPrefixes() {
		if (thing!=null) 
			return thing.getPrefixes();
		else
			return getSource().getPrefixes();
	}
	
	/**
	 * Visit bound pattern.
	 *
	 * @param ctx the ctx
	 * @return the path element
	 */
	@Override 
	public PathElement visitBoundPattern(PathPatternParser.BoundPatternContext ctx) { 
		// boundPattern :  binding ('/'|'>') pathPatterns EOF 
		PathElement pathElement = new BoundPathElement(getSource());
		pathElement.setLeftPathElement( (FactFilterElement) visit(ctx.binding()));
		pathElement.setRightPathElement(visit(ctx.pathPatterns()));
		return pathElement;	
	}
	
	/**
	 * Visit match only pattern.
	 *
	 * @param ctx the ctx
	 * @return the path element
	 */
	@Override 
	public PathElement visitMatchOnlyPattern(PathPatternParser.MatchOnlyPatternContext ctx) { 
		// matchOnlyPattern: pathPattern :  binding  EOF 
		PathElement pathElement = new BoundPathElement(getSource());
		pathElement.setLeftPathElement( (FactFilterElement) visit(ctx.binding()));
		return pathElement;	
	}
	
	/**
	 * Visit path only pattern.
	 *
	 * @param ctx the ctx
	 * @return the path element
	 */
	@Override 
	public PathElement visitPathOnlyPattern(PathPatternParser.PathOnlyPatternContext ctx) { 
		//pathOnlyPattern: pathPatterns  EOF #pathOnlyPattern
		PathElement pathPatterns = visit(ctx.pathPatterns());
		return pathPatterns;
		}
	
	/**
	 * Visit binding.
	 *
	 * @param ctx the ctx
	 * @return the fact filter element
	 */
	@Override
	public FactFilterElement  visitBinding(BindingContext ctx) {
		//binding : factFilterPattern ('/'|'>') ;	
		return (FactFilterElement) visit(ctx.factFilterPattern());
	}
	
	/**
	 * Visit path.
	 *
	 * @param ctx the ctx
	 * @return the path element
	 */
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

	/**
	 * Visit cardinality.
	 *
	 * @param ctx the ctx
	 * @return the cardinality element
	 */
	@Override
	public CardinalityElement visitCardinality(CardinalityContext ctx) {
		//cardinality :	  '{'  INTEGER (',' ( INTEGER )? )?  '}'  
		CardinalityElement cardinalityElement = new CardinalityElement(getSource());
		cardinalityElement.setMinCardinality(Integer.decode(ctx.getChild(1).getText()));
		 String maxCardinalityText = ctx.getChild(3).getText();
		 if(maxCardinalityText.equals("}")) {
			 cardinalityElement.setUnboundedCardinality(true);
		 }else {
			 cardinalityElement.setMaxCardinality(Integer.decode(maxCardinalityText));
		 }
		return cardinalityElement;
	}

	/**
	 * Visit path sequence.
	 *
	 * @param ctx the ctx
	 * @return the sequence path element
	 */
	@Override
	public SequencePathElement visitPathSequence(PathSequenceContext ctx) {
		//pathPatterns :   pathPatterns '/'  pathPatterns  #PathSequence
		if (ctx.pathPatterns().size() == 1) {
			return (SequencePathElement)visit(ctx.pathPatterns(0));
		} else {
			SequencePathElement pathSequenceElement = new SequencePathElement(getSource());
			pathSequenceElement.setLeftPathElement( visit(ctx.pathPatterns(0)));
			pathSequenceElement.setRightPathElement( visit(ctx.pathPatterns(1)));
			return pathSequenceElement;
		}
	}

	/**
	 * Visit path alternative.
	 *
	 * @param ctx the ctx
	 * @return the alternative path element
	 */
	@Override
	public AlternativePathElement visitPathAlternative(PathAlternativeContext ctx) {
		// pathPatterns :  pathPatterns '|'  pathPatterns  #PathAlternative 
		if (ctx.pathPatterns().size() == 1) {
			return (AlternativePathElement) visit(ctx.pathPatterns(0));
		} else {
			AlternativePathElement pathAlternativeElement = new AlternativePathElement(getSource());
			pathAlternativeElement.setLeftPathElement( visit(ctx.pathPatterns(0)));
			pathAlternativeElement.setRightPathElement( visit(ctx.pathPatterns(1)));
			return pathAlternativeElement;
		}
	}

	/**
	 * Visit path parentheses.
	 *
	 * @param ctx the ctx
	 * @return the path element
	 */
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

	/**
	 * Visit path elt or inverse.
	 *
	 * @param ctx the ctx
	 * @return the predicate element
	 */
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

	/**
	 * Visit predicate.
	 *
	 * @param ctx the ctx
	 * @return the predicate element
	 */
	@Override
	public PredicateElement visitPredicate(PredicateContext ctx) {
		// predicate :   ( reifiedPredicate | predicateRef | rdfType | '*' ) factFilterPattern? #objectFilterPattern
		PredicateElement predicateElement = null;
		if (ctx.reifiedPredicate() != null) {
			predicateElement = (PredicateElement) visit(ctx.reifiedPredicate());
			predicateElement.setOperator(PathConstants.Operator.PREDICATE);
		} else if (ctx.predicateRef() != null) {
			predicateElement = new PredicateElement(getSource());
			predicateElement.setOperator(PathConstants.Operator.PREDICATE);
			IriRefValueElement predicateRef = ((IriRefValueElement) visit(ctx.predicateRef()));
			predicateElement.setPredicate(predicateRef.getIri());
		}else if (ctx.rdfType() != null) {
			predicateElement = new PredicateElement(getSource());
			predicateElement.setOperator(PathConstants.Operator.PREDICATE);
			predicateElement.setPredicate((iri("http://rdftype")));
		}else if (ctx.anyPredicate() != null) {
			predicateElement = new PredicateElement(getSource());
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

	/**
	 * Visit reified predicate.
	 *
	 * @param ctx the ctx
	 * @return the predicate element
	 */
	@Override
	public PredicateElement visitReifiedPredicate(ReifiedPredicateContext ctx) {
		// reifiedPredicate :  iriRef? REIFIER predicateRef  factFilterPattern?  dereifier? ; #statementFilterPattern
		PredicateElement reifiedPredicateElement = new PredicateElement(getSource());
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

	/**
	 * Visit predicate ref.
	 *
	 * @param ctx the ctx
	 * @return the iri ref value element
	 */
	@Override
	public IriRefValueElement visitPredicateRef(PredicateRefContext ctx) {
		// predicateRef :  IRI_REF  | rdfType  |  qname | pname_ns ;
		IriRefValueElement predicateRefElement;
		if (ctx.qname() != null) {
			predicateRefElement = (IriRefValueElement) visit(ctx.qname());
		} else if (ctx.pname_ns() != null) {
			predicateRefElement = (IriRefValueElement) visit(ctx.pname_ns());

		} else if(ctx.rdfType() != null) {
			predicateRefElement = new IriRefValueElement(getSource());
			predicateRefElement.setIri(iri("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"));
		}else {
			predicateRefElement = new IriRefValueElement(getSource());
			String iri = ctx.IRI_REF().getText();
			iri = iri.substring(1, iri.length() - 1);
			predicateRefElement.setIri(iri(iri));
		}
		return predicateRefElement;
	}

	/**
	 * Visit iri ref.
	 *
	 * @param ctx the ctx
	 * @return the iri ref value element
	 */
	@Override
	public IriRefValueElement visitIriRef(IriRefContext ctx) {
		// iriRef  : IRI_REF  |  qname | pname_ns ;  
		IriRefValueElement iriRefElement = new IriRefValueElement(getSource());
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

	/**
	 * Visit dereifier.
	 *
	 * @param ctx the ctx
	 * @return the path element
	 */
	@Override
	public PathElement visitDereifier(DereifierContext ctx) {
		//dereifier : DEREIFIER ; 
		return super.visitDereifier(ctx);
	}

	/**
	 * Visit fact filter pattern.
	 *
	 * @param ctx the ctx
	 * @return the fact filter element
	 */
	@Override
	public FactFilterElement visitFactFilterPattern(FactFilterPatternContext ctx) {
		// factFilterPattern : '['  propertyListNotEmpty   ']';
		return (FactFilterElement) visit(ctx.propertyListNotEmpty());
	}

	/**
	 * Visit property list not empty.
	 *
	 * @param ctx the ctx
	 * @return the fact filter element
	 */
	@Override
	public FactFilterElement visitPropertyListNotEmpty(PropertyListNotEmptyContext ctx) {
		// propertyListNotEmpty :   verbObjectList ( ';' ( verbObjectList )? )* ;  
		FactFilterElement propertyListNotEmptyElement = new FactFilterElement(getSource());
		ArrayList<VerbObjectList> propertyListNotEmpty = new ArrayList<VerbObjectList>();
		for (VerbObjectListContext verbObjectListContext : ctx.verbObjectList()) {

			PathElement verb = visit(verbObjectListContext.verb());
			ObjectListValueElement objectList =  (ObjectListValueElement) visit(verbObjectListContext.objectList());
			
			VerbObjectList verbObjectList = new VerbObjectList(getSource());
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

	/**
	 * Visit verb.
	 *
	 * @param ctx the ctx
	 * @return the path element
	 */
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

	/**
	 * Visit object list.
	 *
	 * @param ctx the ctx
	 * @return the object list value element
	 */
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
		ObjectListValueElement objectListElement = new ObjectListValueElement(getSource());
		objectListElement.setObjectList(objectList);
		return objectListElement;
	}

	/**
	 * Visit object.
	 *
	 * @param ctx the ctx
	 * @return the value element
	 */
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

	/**
	 * Visit qname.
	 *
	 * @param ctx the ctx
	 * @return the iri ref value element
	 */
	@Override
	public IriRefValueElement visitQname(QnameContext ctx) {
		//qname : PNAME_NS PN_LOCAL; 
		IriRefValueElement qnameElement = new IriRefValueElement(getSource());
		IRI qname = getSource().convertQName(ctx.getText(),getPrefixes());
		if (qname!=null) {
			qnameElement.setIri(qname);
			return qnameElement;
		}else {
			String message = String.format("Error identifying namespace of qName %s", ctx.getText());
			if (thing!=null) thing.addTrace(message);
			throw new ScriptFailedException("QNAME", message);
		}
	}

	/**
	 * Visit pname ns.
	 *
	 * @param ctx the ctx
	 * @return the iri ref value element
	 */
	@Override
	public IriRefValueElement visitPname_ns(Pname_nsContext ctx) {
		// pname_ns : PNAME_NS ;   
		IriRefValueElement pname_nsElement = new IriRefValueElement(getSource());
		ConcurrentHashMap<String, IRI> prefixes=null;
		if(thing!=null) prefixes=thing.getPrefixes();
		IRI qname = getSource().convertQName(ctx.getText(),prefixes);
		if (qname!=null) {
			pname_nsElement.setIri(qname);
			return pname_nsElement;
		}else {
			String message = String.format("Error identifying namespace of qName %s", ctx.getText());
			if (thing!=null) thing.addTrace(message);
			throw new ScriptFailedException("QNAME", message);
		}
	}

	/**
	 * Visit literal.
	 *
	 * @param ctx the ctx
	 * @return the literal value element
	 */
	@Override
	public LiteralValueElement visitLiteral(LiteralContext ctx) {
		//literal : LITERAL ;
		LiteralValueElement literalElement = new LiteralValueElement(getSource());
		String literalValue = ctx.getText();
		literalValue =  literalValue.substring(1, literalValue.length() - 1); 
		literalElement.setLiteral(literal(literalValue));
		return literalElement;
	}

	/**
	 * Visit verb object list.
	 *
	 * @param ctx the ctx
	 * @return the path element
	 */
	@Override
	public PathElement visitVerbObjectList(VerbObjectListContext ctx) {
		// TODO Auto-generated method stub
		return super.visitVerbObjectList(ctx);
	}

	/**
	 * Visit operator.
	 *
	 * @param ctx the ctx
	 * @return the filter operator value element
	 */
	@Override
	public FilterOperatorValueElement visitOperator(OperatorContext ctx) {
		// operator : OPERATOR ;
		// OPERATOR : 'lt'|'gt'|'le'|'ge'|'eq'|'ne'|'like';
		FilterOperatorValueElement operatorElement = new FilterOperatorValueElement(getSource());
		operatorElement.setFilterOperator(PathConstants.filterOperators.get(ctx.getText()));
		return operatorElement;
	}

	/**
	 * Visit rdf type.
	 *
	 * @param ctx the ctx
	 * @return the path element
	 */
	@Override
	public PathElement visitRdfType(RdfTypeContext ctx) {
		//rdfType : RDFTYPE ;
		return super.visitRdfType(ctx);
	}

}
