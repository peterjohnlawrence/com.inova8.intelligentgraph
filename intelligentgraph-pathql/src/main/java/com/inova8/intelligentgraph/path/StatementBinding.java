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

import com.inova8.intelligentgraph.model.Direction;

import com.inova8.intelligentgraph.vocabulary.PATHQL;

public class StatementBinding extends EdgeBinding{
//Edge.Direction Direction;
//	enum Direction {
//		DIRECT, 
//		INVERSE
//	};

	Variable sourceVariable;
	Variable predicateVariable;
	IRI reification;
	Variable targetVariable;
	Direction direction;
	Boolean isDereified;

	public StatementBinding(Variable sourceVariable, Variable predicateVariable, Variable targetVariable, Boolean isInverseOf) {
		this.sourceVariable = sourceVariable;
		this.predicateVariable = predicateVariable;
		this.targetVariable = targetVariable;
		if (isInverseOf)
			direction = Direction.INVERSE;
		else
			direction = Direction.DIRECT;
	}

	public StatementBinding(Variable sourceVariable, IRI reification, Variable predicateVariable, Variable targetVariable, Boolean isInverseOf, Boolean isDereified) {
		this.sourceVariable = sourceVariable;
		this.predicateVariable = predicateVariable;
		this.reification = reification;
		this.targetVariable = targetVariable;
		if (isInverseOf)
			direction = Direction.INVERSE;
		else
			direction = Direction.DIRECT;
		this.isDereified=isDereified;
	}

	public Variable getSourceVariable() {
		return sourceVariable;
	}

	public Variable getPredicateVariable() {
		return predicateVariable;
	}

	public IRI getReification() {
		return reification;
	}

	public Variable getTargetVariable() {
		return targetVariable;
	}


	public Direction getDirection() {
		return direction;
	}
	

	public Boolean isInverse() {
		if (direction == Direction.INVERSE)
			return true;
		else
			return false;
	}
	

	public Boolean getIsDereified() {
		return isDereified;
	}


	String toSPARQL() {
		return null;
	};
	

	public String toString() {
		if(reification!=null)
			return "[" + sourceVariable.toString() +",<"+ reification.stringValue() +">@"+ predicateVariable.toString() +","+ targetVariable.toString() +"," + direction +"," + isDereified  +"]" ;
		else
			return "[" + sourceVariable.toString() +","+ predicateVariable.toString() +","+ targetVariable.toString() +"," + direction +"]" ;
	};	
	public void addEdgeToPathModel(ModelBuilder builder, BindingSet bindingset){
		ModelBuilder pathModelBuilder = builder.subject("urn://path/"+bindingset.hashCode());
		String edgeCode = "urn://edge/"+bindingset.hashCode()+"/"+ this.toString().hashCode();
		pathModelBuilder.add(PATHQL.path_Edge, edgeCode);
		ModelBuilder subject = builder.subject(edgeCode);
		subject.add(RDF.TYPE ,PATHQL.EDGE);
		subject.add(PATHQL.edge_Source , bindingset.getBinding(this.getSourceVariable().getName()).getValue());
		subject.add(PATHQL.edge_Predicate , getAlternatePredicateBindingValue(bindingset));
		subject.add(PATHQL.edge_Target , bindingset.getBinding(this.getTargetVariable().getName()).getValue());
		subject.add(PATHQL.edge_Direction ,this.getDirection());
		if(this.getIsDereified()!=null) subject.add(PATHQL.edge_Dereified , this.getIsDereified());
		if(this.getReification()!=null) subject.add(PATHQL.edge_Reification , this.getReification());	
	}
	private  Value getAlternatePredicateBindingValue(BindingSet bindingset ) {
		
		Binding predicateBinding = getAlternatePredicateBinding(bindingset, predicateVariable );
		if(predicateBinding!=null) {
			return predicateBinding.getValue();
		}else {
			return null;
		}
//		String predicateVariableName = predicateVariable.getName();
//		String predicateVariableNameRoot = predicateVariableName.split("_alt")[0];
//		if( bindingset.hasBinding(predicateVariableNameRoot)) {
//			return bindingset.getBinding(predicateVariableNameRoot).getValue();
//		}else {
//			for(Binding binding:bindingset) {
//				String bindingName =  binding.getName();
//				String bindingNameRoot = bindingName.split("_alt")[0];
//				if(bindingNameRoot.equals(predicateVariableNameRoot)) {
//					return binding.getValue();
//				}
//			}
//		}	
//		return null;
	}
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
