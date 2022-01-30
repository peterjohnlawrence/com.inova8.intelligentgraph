/*
 * inova8 2020
 */
package com.inova8.intelligentgraph.path;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.query.Binding;
import org.eclipse.rdf4j.query.BindingSet;

import com.inova8.intelligentgraph.vocabulary.PATHQL;
import com.inova8.pathql.element.Variable;

/**
 * The Class StatementBinding.
 */
public class StatementBinding extends EdgeBinding{
	
	/** The Direction. */
	Edge.Direction Direction;
//	enum Direction {
//		DIRECT, 
//		INVERSE
//	};

	/** The source variable. */
Variable sourceVariable;
	
	/** The predicate variable. */
	Variable predicateVariable;
	
	/** The reification. */
	IRI reification;
	
	/** The target variable. */
	Variable targetVariable;
	
	/** The direction. */
	Edge.Direction direction;
	
	/** The is dereified. */
	Boolean isDereified;

	/**
	 * Instantiates a new statement binding.
	 *
	 * @param sourceVariable the source variable
	 * @param predicateVariable the predicate variable
	 * @param targetVariable the target variable
	 * @param isInverseOf the is inverse of
	 */
	public StatementBinding(Variable sourceVariable, Variable predicateVariable, Variable targetVariable, Boolean isInverseOf) {
		this.sourceVariable = sourceVariable;
		this.predicateVariable = predicateVariable;
		this.targetVariable = targetVariable;
		if (isInverseOf)
			direction = Edge.Direction.INVERSE;
		else
			direction = Edge.Direction.DIRECT;
	}

	/**
	 * Instantiates a new statement binding.
	 *
	 * @param sourceVariable the source variable
	 * @param reification the reification
	 * @param predicateVariable the predicate variable
	 * @param targetVariable the target variable
	 * @param isInverseOf the is inverse of
	 * @param isDereified the is dereified
	 */
	public StatementBinding(Variable sourceVariable, IRI reification, Variable predicateVariable, Variable targetVariable, Boolean isInverseOf, Boolean isDereified) {
		this.sourceVariable = sourceVariable;
		this.predicateVariable = predicateVariable;
		this.reification = reification;
		this.targetVariable = targetVariable;
		if (isInverseOf)
			direction = Edge.Direction.INVERSE;
		else
			direction = Edge.Direction.DIRECT;
		this.isDereified=isDereified;
	}

	/**
	 * Gets the source variable.
	 *
	 * @return the source variable
	 */
	public Variable getSourceVariable() {
		return sourceVariable;
	}

	/**
	 * Gets the predicate variable.
	 *
	 * @return the predicate variable
	 */
	public Variable getPredicateVariable() {
		return predicateVariable;
	}

	/**
	 * Gets the reification.
	 *
	 * @return the reification
	 */
	public IRI getReification() {
		return reification;
	}

	/**
	 * Gets the target variable.
	 *
	 * @return the target variable
	 */
	public Variable getTargetVariable() {
		return targetVariable;
	}


	/**
	 * Gets the direction.
	 *
	 * @return the direction
	 */
	public Edge.Direction getDirection() {
		return direction;
	}
	

	/**
	 * Checks if is inverse.
	 *
	 * @return the boolean
	 */
	public Boolean isInverse() {
		if (direction == Edge.Direction.INVERSE)
			return true;
		else
			return false;
	}
	

	/**
	 * Gets the checks if is dereified.
	 *
	 * @return the checks if is dereified
	 */
	public Boolean getIsDereified() {
		return isDereified;
	}


	/**
	 * To SPARQL.
	 *
	 * @return the string
	 */
	String toSPARQL() {
		return null;
	};
	

	/**
	 * To string.
	 *
	 * @return the string
	 */
	public String toString() {
		if(reification!=null)
			return "[" + sourceVariable.toString() +",<"+ reification.stringValue() +">@"+ predicateVariable.toString() +","+ targetVariable.toString() +"," + direction +"," + isDereified  +"]" ;
		else
			return "[" + sourceVariable.toString() +","+ predicateVariable.toString() +","+ targetVariable.toString() +"," + direction +"]" ;
	};	
	
	/**
	 * Adds the edge to path model.
	 *
	 * @param builder the builder
	 * @param bindingset the bindingset
	 */
	public void addEdgeToPathModel(ModelBuilder builder, BindingSet bindingset){
		ModelBuilder pathModelBuilder = builder.subject("urn://path/"+bindingset.hashCode());
		String edgeCode = "urn://edge/"+bindingset.hashCode()+"/"+ this.toString().hashCode();
		pathModelBuilder.add(PATHQL.path_Edge, edgeCode);
		ModelBuilder subject = builder.subject(edgeCode);
		subject.add(RDF.TYPE ,PATHQL.EDGE);
		subject.add(PATHQL.edge_Source , bindingset.getBinding(this.getSourceVariable().getName()).getValue());
		subject.add(PATHQL.edge_Predicate ,  getAlternatePredicateBindingValue(bindingset));
		subject.add(PATHQL.edge_Target , bindingset.getBinding(this.getTargetVariable().getName()).getValue());
		subject.add(PATHQL.edge_Direction ,this.getDirection());
		if(this.getIsDereified()!=null) subject.add(PATHQL.edge_Dereified , this.getIsDereified());
		if(this.getReification()!=null) subject.add(PATHQL.edge_Reification , this.getReification());	
	}
	
	/**
	 * Gets the alternate predicate binding value.
	 *
	 * @param bindingset the bindingset
	 * @return the alternate predicate binding value
	 */
	private  Value getAlternatePredicateBindingValue(BindingSet bindingset ) {
		
		Binding predicateBinding = getAlternatePredicateBinding(bindingset, predicateVariable );
		if(predicateBinding!=null) {
			return predicateBinding.getValue();
		}else {
			return null;
		}
	}
	
	/**
	 * Gets the alternate predicate binding.
	 *
	 * @param bindingset the bindingset
	 * @param predicateVariable the predicate variable
	 * @return the alternate predicate binding
	 */
	public static Binding getAlternatePredicateBinding(BindingSet bindingset, Variable predicateVariable ) {
		String predicateVariableName = predicateVariable.getName();
		String predicateVariableNameRoot = predicateVariableName.split("_alt")[0];
		if( bindingset.hasBinding(predicateVariableNameRoot)) {
			return bindingset.getBinding(predicateVariableNameRoot);
		}else {
			for(Binding binding:bindingset) {
				String bindingName =  binding.getName();
				String bindingNameRoot = bindingName.split("_alt")[0];
				if(bindingNameRoot.equals(predicateVariableNameRoot)) {
					return binding;
				}
			}
		}	
		return null;
	}
}
