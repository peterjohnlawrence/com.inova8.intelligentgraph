/*
 * inova8 2020
 */
package com.inova8.pathql.processor;

import static org.eclipse.rdf4j.model.util.Values.*;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.util.Values;

import com.inova8.intelligentgraph.exceptions.ScriptFailedException;
import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.pathCalc.CustomQueryOptions;
import com.inova8.intelligentgraph.pathQLModel.Thing;
import com.inova8.pathql.element.AlternativePathElement;
import com.inova8.pathql.element.BindVariableElement;
import com.inova8.pathql.element.BoundPathElement;
import com.inova8.pathql.element.CardinalityElement;
import com.inova8.pathql.element.FactFilterElement;
import com.inova8.pathql.element.FilterOperatorValueElement;
import com.inova8.pathql.element.IriRefValueElement;
import com.inova8.pathql.element.LiteralValueElement;
import com.inova8.pathql.element.ObjectElement;
import com.inova8.pathql.element.ObjectListValueElement;
import com.inova8.pathql.element.PathElement;
import com.inova8.pathql.element.PredicateElement;
import com.inova8.pathql.element.QueryOptionsPathElement;
import com.inova8.pathql.element.SequencePathElement;
import com.inova8.pathql.element.ValueElement;
import com.inova8.pathql.element.VerbObjectList;

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

import static org.eclipse.rdf4j.model.util.Values.literal;
/**
 * The Class PathPatternVisitor.
 */
public class PathPatternVisitor extends PathPatternBaseVisitor<PathElement> {
	
	Thing thing;
	
	private IntelligentGraphRepository source;

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
	public IntelligentGraphRepository getSource() {
		return source;
	}

	public PathPatternVisitor(IntelligentGraphRepository source) {
		super();
	this.source = source;
	}
	
	private ConcurrentHashMap<String, IRI> getPrefixes() {
		//if (thing!=null) 
		//	return thing.getPrefixes();
		//else
			return getSource().getPrefixes();
	}

	@Override 
	public PathElement visitBoundPattern(PathPatternParser.BoundPatternContext ctx) { 
		// boundPattern :  binding ('/'|'>') pathPatterns EOF 
		PathElement pathElement = new BoundPathElement(getSource());
		pathElement.setLeftPathElement( (FactFilterElement) visit(ctx.binding()));
		pathElement.setRightPathElement(visit(ctx.pathPatterns()));
		return pathElement;	
	}

	@Override 
	public PathElement visitMatchOnlyPattern(PathPatternParser.MatchOnlyPatternContext ctx) { 
		// matchOnlyPattern: pathPattern :  binding  EOF 
		PathElement pathElement = new BoundPathElement(getSource());
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
		CardinalityElement cardinalityElement = new CardinalityElement(getSource());
		cardinalityElement.setMinCardinality(Integer.decode(ctx.getChild(1).getText()));
		 String maxCardinalityText = ctx.getChild(3).getText();
		 if(maxCardinalityText.equals("}")) {
			 cardinalityElement.setUnboundedCardinality(true);
			 cardinalityElement.setMaxCardinality(null);
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
			SequencePathElement pathSequenceElement = new SequencePathElement(getSource());
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
			AlternativePathElement pathAlternativeElement = new AlternativePathElement(getSource());
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

	@Override
	public IriRefValueElement visitIriRef(IriRefContext ctx) {
		// iriRef  : IRI_REF  |  qname | pname_ns ;  
		IriRefValueElement iriRefElement = new IriRefValueElement(getSource());
		if (ctx.qname() != null) {
			iriRefElement.setIri(((IriRefValueElement)visit(ctx.qname())).getIri());
		} else if (ctx.pname_ns() != null) {
			iriRefElement.setIri(((IriRefValueElement)visit(ctx.pname_ns())).getIri());

		} else if (ctx.IRI_REF() != null) { 
			String iri = ctx.IRI_REF().getText();
			iri= iri.substring(1,iri.length()-1);
			iriRefElement.setIri(iri(iri));
		}else {
			throw new ScriptFailedException( String.format("Error identifying iriRef %s", ctx.getText()));
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
		// object : iriRef  | literal | factFilterPattern | BINDVARIABLE  ;
		ArrayList<ObjectElement> objectList = new ArrayList<ObjectElement>();
		for( ObjectContext objectContext: ctx.object()) {
			if(objectContext.iriRef()!=null) {
				objectList.add((ObjectElement) visit(objectContext.iriRef()));
			}else if(objectContext.literal()!=null) {
				objectList.add((ObjectElement) visit(objectContext.literal()));
			}else if(objectContext.factFilterPattern()!=null) {	
				objectList.add((ObjectElement) visit(objectContext.factFilterPattern()));
			}else if (objectContext.BINDVARIABLE() != null) {
				String bindVariableIndex = objectContext.BINDVARIABLE().getText().substring(1);
				BindVariableElement bindVariableElement = new BindVariableElement(getSource());
				bindVariableElement.setBindVariableIndex(Integer.parseInt(bindVariableIndex));
				objectList.add((ObjectElement) bindVariableElement);
			}	
		}
		ObjectListValueElement objectListElement = new ObjectListValueElement(getSource());
		objectListElement.setObjectList(objectList);
		return objectListElement;
	}

	@Override
	public PathElement visitObject(ObjectContext ctx) {
		// object : iriRef  | literal | blankNodePropertyListPath | BIBNDVARIABLE;
		//ValueElement objectElement = null ;
		if (ctx.iriRef() != null) {
			return (ValueElement) visit(ctx.iriRef()) ;
		} else if (ctx.literal() != null) {
			return (ValueElement) visit(ctx.literal());//objectElement;
		} else if (ctx.factFilterPattern() != null) {
			return (ValueElement) visit(ctx.factFilterPattern());
		}else if (ctx.BINDVARIABLE() != null) {
			String bindVariableIndex = ctx.BINDVARIABLE().getText().substring(1);
			BindVariableElement bindVariableElement = new BindVariableElement(getSource());
			bindVariableElement.setBindVariableIndex(Integer.parseInt(bindVariableIndex));
			return bindVariableElement;
		}
		return null;
	}

	@Override
	public IriRefValueElement visitQname(QnameContext ctx) {
		//qname : PNAME_NS PN_LOCAL; 
		IriRefValueElement qnameElement = new IriRefValueElement(getSource());
		IRI qname = getSource().convertQName(ctx.getText(),getPrefixes());
		if (qname!=null) {
			qnameElement.setIri(qname);
			return qnameElement;
		}else {
			if (thing!=null) thing.getEvaluationContext().getTracer().traceQNameError(ctx.getText());
			throw new ScriptFailedException( String.format("Error identifying namespace of qName %s", ctx.getText()));
		}
	}

	@Override
	public IriRefValueElement visitPname_ns(Pname_nsContext ctx) {
		// pname_ns : PNAME_NS ;   
		
		IriRefValueElement pname_nsElement = new IriRefValueElement(getSource());
		ConcurrentHashMap<String, IRI> prefixes=null;
		prefixes=source.getPrefixes();
	//	if(thing!=null) prefixes=thing.getPrefixes();
		IRI qname = getSource().convertQName(ctx.getText(),prefixes);
		if (qname!=null) {
			pname_nsElement.setIri(qname);
			return pname_nsElement;
		}else {
			if (thing!=null)  thing.getEvaluationContext().getTracer().traceQNameError(ctx.getText());
			throw new ScriptFailedException( String.format("Error identifying namespace of qName %s", ctx.getText()));
		}
	}

	@Override
	public LiteralValueElement visitLiteral(LiteralContext ctx) {
		//rdfLiteral: (DQLITERAL | SQLITERAL) ('^^' (IRI_REF |  qname) )? ;
		LiteralValueElement literalElement = new LiteralValueElement(getSource());
		String literalValue = null;
		if(ctx.DQLITERAL()==null) {
			literalValue = ctx.SQLITERAL().getText() ;
		}else {
			literalValue = ctx.DQLITERAL().getText() ;
		}
		literalValue =  literalValue.substring(1, literalValue.length() - 1); 
		IRI datatypeIRI = null ;
		if(ctx.IRI_REF() ==null) {
			if(ctx.qname() ==null ) {
				literalElement.setLiteral(literal(literalValue));
			}else {
				IriRefValueElement qnameElement = visitQname(ctx.qname());
				datatypeIRI = qnameElement.getIri();
				literalElement.setLiteral(literal(literalValue, datatypeIRI));
			}
		}else {
			String datatypeIRIString = ctx.IRI_REF().getText();
			literalElement.setLiteral(literal(literalValue, datatypeIRIString));
		}
		//literalElement.setLiteral(literal(literalValue));
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
		FilterOperatorValueElement operatorElement = new FilterOperatorValueElement(getSource());
		operatorElement.setFilterOperator(PathConstants.filterOperators.get(ctx.getText()));
		return operatorElement;
	}

	@Override
	public PathElement visitRdfType(RdfTypeContext ctx) {
		//rdfType : RDFTYPE ;
		return super.visitRdfType(ctx);
	}

}
