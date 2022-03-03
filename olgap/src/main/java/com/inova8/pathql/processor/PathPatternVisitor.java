/*
 * inova8 2020
 */
package com.inova8.pathql.processor;

import static org.eclipse.rdf4j.model.util.Values.*;

import java.util.ArrayList;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.util.Values;

import com.inova8.intelligentgraph.context.CustomQueryOptions;
import com.inova8.intelligentgraph.exceptions.ScriptFailedException;
import com.inova8.intelligentgraph.model.Thing;
import com.inova8.pathql.context.Prefixes;
import com.inova8.pathql.context.RepositoryContext;
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
import com.inova8.pathql.pathPattern.PathPatternBaseVisitor;
import com.inova8.pathql.pathPattern.PathPatternParser;
import com.inova8.pathql.pathPattern.PathPatternParser.BindingContext;
import com.inova8.pathql.pathPattern.PathPatternParser.CardinalityContext;
import com.inova8.pathql.pathPattern.PathPatternParser.DereifierContext;
import com.inova8.pathql.pathPattern.PathPatternParser.FactFilterPatternContext;
import com.inova8.pathql.pathPattern.PathPatternParser.IriRefContext;
import com.inova8.pathql.pathPattern.PathPatternParser.LiteralContext;
import com.inova8.pathql.pathPattern.PathPatternParser.ObjectContext;
import com.inova8.pathql.pathPattern.PathPatternParser.ObjectListContext;
import com.inova8.pathql.pathPattern.PathPatternParser.OperatorContext;
import com.inova8.pathql.pathPattern.PathPatternParser.PathAlternativeContext;
import com.inova8.pathql.pathPattern.PathPatternParser.PathContext;
import com.inova8.pathql.pathPattern.PathPatternParser.PathEltOrInverseContext;
import com.inova8.pathql.pathPattern.PathPatternParser.PathParenthesesContext;
import com.inova8.pathql.pathPattern.PathPatternParser.PathSequenceContext;
import com.inova8.pathql.pathPattern.PathPatternParser.Pname_nsContext;
import com.inova8.pathql.pathPattern.PathPatternParser.PredicateContext;
import com.inova8.pathql.pathPattern.PathPatternParser.PredicateRefContext;
import com.inova8.pathql.pathPattern.PathPatternParser.PropertyListNotEmptyContext;
import com.inova8.pathql.pathPattern.PathPatternParser.QnameContext;
import com.inova8.pathql.pathPattern.PathPatternParser.QueryOptionContext;
import com.inova8.pathql.pathPattern.PathPatternParser.QueryOptionsContext;
import com.inova8.pathql.pathPattern.PathPatternParser.QueryStringContext;
import com.inova8.pathql.pathPattern.PathPatternParser.RdfTypeContext;
import com.inova8.pathql.pathPattern.PathPatternParser.ReifiedPredicateContext;
import com.inova8.pathql.pathPattern.PathPatternParser.TypeContext;
import com.inova8.pathql.pathPattern.PathPatternParser.VerbContext;
import com.inova8.pathql.pathPattern.PathPatternParser.VerbObjectListContext;

import static org.eclipse.rdf4j.model.util.Values.literal;

/**
 * The Class PathPatternVisitor.
 */
public class PathPatternVisitor extends PathPatternBaseVisitor<PathElement> {
	
	/** The thing. */
	Thing thing;
	
//	private IntelligentGraphRepository source;
	
	/** The repository context. */
private RepositoryContext repositoryContext;
//   @Deprecated
//	public PathPatternVisitor(Thing thing) {
//		super();
//		this.thing = thing;
//		this.source = thing.this.repositoryContext;
//	}
//	public PathPatternVisitor(IntelligentGraphRepository source) {
//		super();
//	this.source = source;
/**
 * Instantiates a new path pattern visitor.
 *
 * @param repositoryContext the repository context
 */
//	}
	public PathPatternVisitor(RepositoryContext repositoryContext) {
		super();
		this.repositoryContext = repositoryContext;
	}
//	public PathPatternVisitor() {
//		super();
//	
/**
 * Visit query string.
 *
 * @param ctx the ctx
 * @return the path element
 */
//	}
	@Override
	public PathElement visitQueryString(QueryStringContext ctx) {
		// queryString : pathPattern queryOptions? EOF ;
		PathElement pathElement =   visit(ctx.pathPattern()) ;
		if(ctx.queryOptions()!=null ) {
			pathElement.setCustomQueryOptions(visit(ctx.queryOptions()).getCustomQueryOptions()) ;
		}
		return pathElement;
	}

	/**
	 * Visit query options.
	 *
	 * @param ctx the ctx
	 * @return the query options path element
	 */
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
		QueryOptionsPathElement queryOptionsPathElement = new QueryOptionsPathElement(this.repositoryContext);
		queryOptionsPathElement.setCustomQueryOptions(customQueryOptions);
		return queryOptionsPathElement;
	}

	/**
	 * Visit type.
	 *
	 * @param ctx the ctx
	 * @return the iri ref value element
	 */
	@Override
	public IriRefValueElement visitType(TypeContext ctx) {
		//type : qname;
		return visitQname(ctx.qname());
	}

//	/**
//	 * Gets the source.
//	 *
//	 * @return the source
//	 */
//	public IntelligentGraphRepository getSource() {
//		return source;
//	}

	/**
 * Gets the repository context.
 *
 * @return the repository context
 */
public RepositoryContext getRepositoryContext() {
		return repositoryContext;
    }
	
	/**
	 * Gets the prefixes.
	 *
	 * @return the prefixes
	 */
	private  com.inova8.pathql.context.Prefixes getPrefixes() {
		//if (thing!=null) 
		//	return thing.getPrefixes();
		//else
			return repositoryContext.getPrefixes();
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
		PathElement pathElement = new BoundPathElement(this.repositoryContext);
		pathElement.setLeftPathElement( (FactFilterElement) visit(ctx.binding()));
		if(ctx.pathPatterns()!=null )
			pathElement.setRightPathElement(visit(ctx.pathPatterns()));
		else
			throw new ScriptFailedException( String.format("Bolund pattern %s must be followed by pathPatterns", ctx.getText()));
		return pathElement;	
	}

//	@Override 
//	public PathElement visitMatchOnlyPattern(PathPatternParser.MatchOnlyPatternContext ctx) { 
//		// matchOnlyPattern: pathPattern :  binding  EOF 
//		PathElement pathElement = new BoundPathElement(this.repositoryContext);
//		pathElement.setLeftPathElement( (FactFilterElement) visit(ctx.binding()));
//		return pathElement;	
//	}
	

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
		CardinalityElement cardinalityElement = new CardinalityElement(this.repositoryContext);
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
			SequencePathElement pathSequenceElement = new SequencePathElement(this.repositoryContext);
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
			AlternativePathElement pathAlternativeElement = new AlternativePathElement(this.repositoryContext);
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
			predicateElement = new PredicateElement(this.repositoryContext);
			predicateElement.setOperator(PathConstants.Operator.PREDICATE);
			IriRefValueElement predicateRef = ((IriRefValueElement) visit(ctx.predicateRef()));
			predicateElement.setPredicate(predicateRef.getIri());
		}else if (ctx.rdfType() != null) {
			predicateElement = new PredicateElement(this.repositoryContext);
			predicateElement.setOperator(PathConstants.Operator.PREDICATE);
			predicateElement.setPredicate((iri("http://rdftype")));
		}else if (ctx.anyPredicate() != null) {
			predicateElement = new PredicateElement(this.repositoryContext);
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
		PredicateElement reifiedPredicateElement = new PredicateElement(this.repositoryContext);
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
			predicateRefElement = new IriRefValueElement(this.repositoryContext);
			predicateRefElement.setIri(iri("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"));
		}else {
			predicateRefElement = new IriRefValueElement(this.repositoryContext);
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
		IriRefValueElement iriRefElement = new IriRefValueElement(this.repositoryContext);
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
		FactFilterElement propertyListNotEmptyElement = new FactFilterElement(this.repositoryContext);
		ArrayList<VerbObjectList> propertyListNotEmpty = new ArrayList<VerbObjectList>();
		for (VerbObjectListContext verbObjectListContext : ctx.verbObjectList()) {

			PathElement verb = visit(verbObjectListContext.verb());
			ObjectListValueElement objectList =  (ObjectListValueElement) visit(verbObjectListContext.objectList());
			
			VerbObjectList verbObjectList = new VerbObjectList(this.repositoryContext);
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
				BindVariableElement bindVariableElement = new BindVariableElement(this.repositoryContext);
				bindVariableElement.setBindVariableIndex(Integer.parseInt(bindVariableIndex));
				objectList.add((ObjectElement) bindVariableElement);
			}	
		}
		ObjectListValueElement objectListElement = new ObjectListValueElement(this.repositoryContext);
		objectListElement.setObjectList(objectList);
		return objectListElement;
	}

	/**
	 * Visit object.
	 *
	 * @param ctx the ctx
	 * @return the path element
	 */
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
			BindVariableElement bindVariableElement = new BindVariableElement(this.repositoryContext);
			bindVariableElement.setBindVariableIndex(Integer.parseInt(bindVariableIndex));
			return bindVariableElement;
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
		IriRefValueElement qnameElement = new IriRefValueElement(this.repositoryContext);
		IRI qname = this.repositoryContext.convertQName(ctx.getText(),getPrefixes());
		if (qname!=null) {
			qnameElement.setIri(qname);
			return qnameElement;
		}else {
			if (thing!=null) thing.getEvaluationContext().getTracer().traceQNameError(ctx.getText());
			throw new ScriptFailedException( String.format("Error identifying namespace of qName %s", ctx.getText()));
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
		
		IriRefValueElement pname_nsElement = new IriRefValueElement(this.repositoryContext);
		Prefixes prefixes=null;
		prefixes=this.repositoryContext.getPrefixes();
	//	if(thing!=null) prefixes=thing.getPrefixes();
		IRI qname = this.repositoryContext.convertQName(ctx.getText(),prefixes);
		if (qname!=null) {
			pname_nsElement.setIri(qname);
			return pname_nsElement;
		}else {
			if (thing!=null)  thing.getEvaluationContext().getTracer().traceQNameError(ctx.getText());
			throw new ScriptFailedException( String.format("Error identifying namespace of qName %s", ctx.getText()));
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
		//rdfLiteral: (DQLITERAL | SQLITERAL) ('^^' (IRI_REF |  qname) )? ;
		LiteralValueElement literalElement = new LiteralValueElement(this.repositoryContext);
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
		FilterOperatorValueElement operatorElement = new FilterOperatorValueElement(this.repositoryContext);
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
