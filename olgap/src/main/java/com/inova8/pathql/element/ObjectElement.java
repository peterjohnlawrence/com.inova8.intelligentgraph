/*
 * inova8 2020
 */
package com.inova8.pathql.element;

import java.util.ArrayList;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Value;

import com.inova8.intelligentgraph.context.CustomQueryOptions;
import com.inova8.intelligentgraph.path.PathBinding;
import com.inova8.intelligentgraph.path.PathTupleExpr;
import com.inova8.pathql.context.RepositoryContext;
import com.inova8.pathql.processor.PathConstants;
import com.inova8.pathql.processor.PathConstants.EdgeCode;

/**
 * The Class ObjectElement.
 */
public class ObjectElement extends PathElement{

	/** The iri. */
	IRI iri;
	
	/** The literal. */
	Literal literal;
	
	/** The blank node verb object list. */
	ArrayList<VerbObjectList>  blankNodeVerbObjectList;
	
	/**
	 * Instantiates a new object element.
	 *
	 * @param repositoryContext the repository context
	 */
	public ObjectElement(RepositoryContext repositoryContext) {
		super(repositoryContext);
		operator=PathConstants.Operator.OBJECT;
	}
	
	/**
	 * Gets the iri.
	 *
	 * @param customQueryOptions the custom query options
	 * @return the iri
	 */
	public IRI getIri(CustomQueryOptions customQueryOptions) {
		return iri;
	}
	
	/**
	 * Sets the iri.
	 *
	 * @param iri the new iri
	 */
	public void setIri(IRI iri) {
		this.iri = iri;
	}
	
	/**
	 * Gets the literal.
	 *
	 * @param customQueryOptions the custom query options
	 * @return the literal
	 */
	public Literal getLiteral(CustomQueryOptions customQueryOptions) {
		return literal;
	}
	
	/**
	 * Gets the value.
	 *
	 * @param customQueryOptions the custom query options
	 * @return the value
	 */
	public Value getValue(CustomQueryOptions customQueryOptions) {
		return literal;
	}	
	
	/**
	 * Sets the literal.
	 *
	 * @param literal the new literal
	 */
	public void setLiteral(Literal literal) {
		this.literal = literal;
	}
	
	/**
	 * Gets the blank node verb object list.
	 *
	 * @return the blank node verb object list
	 */
	public ArrayList<VerbObjectList> getBlankNodeVerbObjectList() {
		return blankNodeVerbObjectList;
	}
	
	/**
	 * Sets the blank node verb object list.
	 *
	 * @param blankNodeVerbObjectList the new blank node verb object list
	 */
	public void setBlankNodeVerbObjectList(ArrayList<VerbObjectList> blankNodeVerbObjectList) {
		this.blankNodeVerbObjectList = blankNodeVerbObjectList;
	}
	
	/**
	 * To string.
	 *
	 * @return the string
	 */
	public String toString() {
		String object ="";
		if(iri!=null ) {
			object += "<"+ iri.stringValue() +">";
		}else if(literal!=null ) {
			object +=  literal.stringValue();
		}else if(blankNodeVerbObjectList!=null) {
			object +=  blankNodeVerbObjectList.toString() ;	
		}
		return object;
	}
	
	/**
	 * To SPARQL.
	 *
	 * @return the string
	 */
	public String toSPARQL() {
		String object ="";
		if(iri!=null ) {
			object += "<"+ iri.stringValue() +">";
		}else if(literal!=null ) {
			object +=  literal.stringValue();
		}else if(blankNodeVerbObjectList!=null) {
			for(   VerbObjectList verbObjectList  : blankNodeVerbObjectList) {
				object +=  verbObjectList.toSPARQL() ;	
			}			
		}
		return object;
	}
	
	/**
	 * To HTML.
	 *
	 * @return the string
	 */
	@Override
	public String toHTML() {

		return null;
	}
	
	/**
	 * Path pattern query.
	 *
	 * @param sourceVariable the source variable
	 * @param predicateVariable the predicate variable
	 * @param targetVariable the target variable
	 * @param customQueryOptions the custom query options
	 * @return the path tuple expr
	 */
	@Override
	public PathTupleExpr pathPatternQuery( Variable sourceVariable,Variable predicateVariable, Variable targetVariable, CustomQueryOptions customQueryOptions) {
		return null;
	}
	
	/**
	 * Path pattern query.
	 *
	 * @param sourceVariable the source variable
	 * @param predicateVariable the predicate variable
	 * @param targetVariable the target variable
	 * @param pathIteration the path iteration
	 * @param customQueryOptions the custom query options
	 * @return the path tuple expr
	 */
	@Override
	public PathTupleExpr pathPatternQuery( Variable sourceVariable,Variable predicateVariable, Variable targetVariable,
			Integer pathIteration, CustomQueryOptions customQueryOptions) {
		return null;
	}
	
	/**
	 * Index visitor.
	 *
	 * @param baseIndex the base index
	 * @param entryIndex the entry index
	 * @param edgeCode the edge code
	 * @return the integer
	 */
	@Override
	public Integer indexVisitor(Integer baseIndex, Integer entryIndex, EdgeCode edgeCode) {
		setEntryIndex(entryIndex);
		setBaseIndex(baseIndex);
		if(blankNodeVerbObjectList!=null) {
			for(   VerbObjectList verbObjectList  : blankNodeVerbObjectList) {
				verbObjectList.indexVisitor(baseIndex, entryIndex,edgeCode) ;	
			}			
		}
		setExitIndex(entryIndex);
		return getExitIndex();
	}
	
	/**
	 * Gets the checks if is negated.
	 *
	 * @return the checks if is negated
	 */
	@Override
	public Boolean getIsNegated() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Sets the checks if is negated.
	 *
	 * @param isDereified the new checks if is negated
	 */
	@Override
	public void setIsNegated(Boolean isDereified) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * Visit path binding.
	 *
	 * @param pathBinding the path binding
	 * @param pathIteration the path iteration
	 * @return the path binding
	 */
	@Override
	public PathBinding visitPathBinding(PathBinding pathBinding, Integer pathIteration) {
		// TODO Auto-generated method stub
		return null;
	}




}
