/*
 * inova8 2020
 */
package com.inova8.pathql.element;

import org.eclipse.rdf4j.query.algebra.Join;
import org.eclipse.rdf4j.query.algebra.Projection;
import org.eclipse.rdf4j.query.algebra.TupleExpr;

import com.inova8.intelligentgraph.intelligentGraphRepository.IntelligentGraphRepository;
import com.inova8.intelligentgraph.path.PathBinding;
import com.inova8.intelligentgraph.path.PathTupleExpr;
import com.inova8.intelligentgraph.pathCalc.CustomQueryOptions;
import com.inova8.intelligentgraph.pathQLModel.Thing;
import com.inova8.pathql.processor.PathConstants;
import com.inova8.pathql.processor.PathConstants.EdgeCode;

/**
 * The Class SequencePathElement.
 */
public class SequencePathElement extends PathElement {

	/** The is negated. */
	private Boolean isNegated = false;

	/**
	 * Instantiates a new sequence path element.
	 *
	 * @param source
	 *            the source
	 */
	public SequencePathElement(IntelligentGraphRepository source) {
		super(source);
		operator = PathConstants.Operator.SEQUENCE;
	}

	/**
	 * To SPARQL.
	 *
	 * @return the string
	 */
	@Override
	public String toSPARQL() {

		String sequenceString = getMinCardinalityString();
		sequenceString += getLeftPathElement().toSPARQL();
		sequenceString += getRightPathElement().toSPARQL();
		sequenceString += getMaxCardinalityString();
		return sequenceString;
	}

	/**
	 * To HTML.
	 *
	 * @return the string
	 */
	@Override
	public String toHTML() {
		String sequenceString = getLeftPathElement().toHTML() + " / " + getRightPathElement().toHTML();
		return addCardinality(sequenceString);
	}

	/**
	 * To string.
	 *
	 * @return the string
	 */
	@Override
	public String toString() {
		String sequenceString = getLeftPathElement().toString() + " / " + getRightPathElement().toString();
		return addCardinality(sequenceString);
	}

	/**
	 * Path pattern query.
	 *
	 * @param thing
	 *            the thing
	 * @param sourceVariable
	 *            the source variable
	 * @param targetVariable
	 *            the target variable
	 * @return the tuple expr
	 */
	@Override
	public PathTupleExpr pathPatternQuery(Thing thing, Variable sourceVariable, Variable targetVariable, CustomQueryOptions customQueryOptions) {
		return pathPatternQuery(thing, sourceVariable, targetVariable, 0,customQueryOptions);
	}
	public Boolean hasNextCardinality(Integer iteration) {
			
			if (getLeftPathElement() != null) {
				if (getRightPathElement() != null) {
					if (getRightPathElement().hasNextCardinality(iteration)) {
						setCardinality(iteration,getCardinality(iteration-1));
						return true;
					} else {
						if (getLeftPathElement().hasNextCardinality(iteration)) {
							setCardinality(iteration,getCardinality(iteration-1));
							return true;
						} else {
							 Integer cardinality = getCardinality(iteration-1);
							 if( cardinality < getMaxCardinality()) {
								cardinality++;
								setCardinality(iteration,cardinality);
								return true;
							 }else {
								setCardinality(iteration,getMinCardinality());
								return false;
							 }
						}
					}
				}
			}
			return false;
	 }
	@Override
	public PathTupleExpr pathPatternQuery(Thing thing, Variable sourceVariable, Variable targetVariable,
			Integer pathIteration,CustomQueryOptions customQueryOptions) {
		if (sourceVariable == null)	sourceVariable = this.getSourceVariable();
		if(targetVariable==null)targetVariable = this.getTargetVariable();	
		TupleExpr intermediateJoinPattern = null;
		PathTupleExpr joinPattern = null;

		if (getCardinality(pathIteration) > 0) {
			Variable intermediateSourceVariable = null;
			Variable intermediateVariable = null;
			Variable intermediateTargetVariable = null;
			Variable priorIntermediateTargetVariable = null;
			for (int iteration = 1; iteration <= getCardinality(pathIteration); iteration++) {
				if (iteration == 1) {
					intermediateSourceVariable = sourceVariable;
					intermediateVariable=  new Variable(getLeftPathElement().getTargetVariable().getName(),getLeftPathElement().getTargetVariable().getValue());   //getLeftPathElement().getTargetVariable();
					intermediateTargetVariable=targetVariable;
				}
				if (iteration < getCardinality(pathIteration)) {
					if (iteration > 1) 
						intermediateSourceVariable = priorIntermediateTargetVariable;
					intermediateTargetVariable = new Variable(sourceVariable.getName() + "_i" + iteration);
					intermediateVariable = new Variable(sourceVariable.getName() + "_in" + iteration);
					priorIntermediateTargetVariable = intermediateTargetVariable;
				}
				if (iteration == getCardinality(pathIteration)) {
					if (iteration > 1) {
						intermediateSourceVariable = priorIntermediateTargetVariable;
						intermediateVariable = new Variable(sourceVariable.getName() + "_in" + iteration);
						intermediateTargetVariable = targetVariable;
					}
				}
				PathTupleExpr leftPattern = getLeftPathElement().pathPatternQuery(thing, intermediateSourceVariable,
						intermediateVariable, pathIteration,customQueryOptions);
				PathTupleExpr rightPattern;
				if (leftPattern==null){
					intermediateVariable.setValue(intermediateSourceVariable.getValue());
					//intermediateVariable.setName(intermediateSourceVariable.getName());
					rightPattern = getRightPathElement().pathPatternQuery(thing, intermediateVariable, intermediateTargetVariable,
							pathIteration,customQueryOptions);
				}else {
					rightPattern = getRightPathElement().pathPatternQuery(thing, intermediateVariable, intermediateTargetVariable,
							pathIteration,customQueryOptions);
				}
				
				if(leftPattern!=null)
					if( rightPattern!=null)
						intermediateJoinPattern = new Join(leftPattern.getTupleExpr(), rightPattern.getTupleExpr());
					else {
						intermediateVariable.setName(intermediateTargetVariable.getName());
						// Projection bindRight = new Projection((TupleExpr) intermediateVariable);
						 intermediateJoinPattern= leftPattern.getTupleExpr();
					}
				else {
					if( rightPattern!=null)
						intermediateJoinPattern =rightPattern.getTupleExpr();
					else
						intermediateJoinPattern=null;
				}
				if (joinPattern == null) {
					joinPattern = new PathTupleExpr((TupleExpr)intermediateJoinPattern);
					if(leftPattern!=null) joinPattern.getPath().addAll( leftPattern.getPath());
					if(rightPattern!=null) joinPattern.getPath().addAll( rightPattern.getPath());
				} else {
					joinPattern.setTupleExpr(new Join(joinPattern.getTupleExpr(), intermediateJoinPattern));
					if(leftPattern!=null)joinPattern.getPath().addAll( leftPattern.getPath());
					if(rightPattern!=null)joinPattern.getPath().addAll( rightPattern.getPath());
				}
			}
			//joinPattern.setPath(getPathBindings().get(pathIteration));
			return joinPattern;
		} else {
			return null;
		}

	}

	/**
	 * Gets the checks if is negated.
	 *
	 * @return the checks if is negated
	 */
	public Boolean getIsNegated() {
		return isNegated;
	}

	/**
	 * Sets the checks if is negated.
	 *
	 * @param isNegated
	 *            the new checks if is negated
	 */
	public void setIsNegated(Boolean isNegated) {
		this.isNegated = isNegated;
	}

	/**
	 * Index visitor.
	 *
	 * @param baseIndex
	 *            the base index
	 * @param entryIndex
	 *            the entry index
	 * @param edgeCode
	 *            the edge code
	 * @return the integer
	 */
	@Override
	public Integer indexVisitor(Integer baseIndex, Integer entryIndex, EdgeCode edgeCode) {
		setBaseIndex(baseIndex);
		setEntryIndex(entryIndex);
		Integer leftExitIndex = getLeftPathElement().indexVisitor(baseIndex, entryIndex, edgeCode);

		if (getLeftPathElement().getOperator().equals(PathConstants.Operator.PREDICATE)
				&& ((PredicateElement) getLeftPathElement()).getIsDereified()) {
			setEdgeCode(EdgeCode.DEREIFIED);
		}

		Integer rightExitIndex = getRightPathElement().indexVisitor(baseIndex, leftExitIndex, getEdgeCode());
		setExitIndex(rightExitIndex);
		return rightExitIndex;
	}

	/**
	 * Visit path.
	 *
	 * @param path
	 *            the path
	 * @return the path
	 */
	@Override
	public PathBinding visitPathBinding(PathBinding pathBinding, Integer pathIteration) {
	//	for( int iteration = 1; iteration<=getCardinality(pathIteration);iteration++ ) {
			pathBinding = getLeftPathElement().visitPathBinding(pathBinding,pathIteration);
			pathBinding = getRightPathElement().visitPathBinding(pathBinding,pathIteration);
	//	}
		
		return pathBinding;
		
		
//		EdgeBinding predicateEdge;
//		Variable sourceVariable = this.getSourceVariable();
//		Variable targetVariable = this.getTargetVariable();			
//		Variable intermediateSourceVariable = null ;
//		Variable intermediateVariable = null;
//		Variable intermediateTargetVariable = null;
//		Variable priorIntermediateTargetVariable = null ;
//		for( int iteration = 1; iteration<=getCardinality(pathIteration);iteration++ ) {
//			if( iteration==1) {
//				intermediateSourceVariable = sourceVariable;
//				intermediateVariable= getLeftPathElement().getTargetVariable();
//				intermediateTargetVariable=targetVariable;
//			}
//			if(iteration<getCardinality(pathIteration)) {
//				if (iteration > 1) 
//					intermediateSourceVariable = priorIntermediateTargetVariable;
//				intermediateTargetVariable = new Variable(sourceVariable.getName() + "_i" + iteration);
//				intermediateVariable = new Variable(sourceVariable.getName() + "_in" + iteration);
//				priorIntermediateTargetVariable = intermediateTargetVariable;
//				
//			}
//			if( iteration==getCardinality(pathIteration)) {
//				if (iteration > 1) {
//					intermediateSourceVariable = priorIntermediateTargetVariable;
//					intermediateVariable = new Variable(sourceVariable.getName() + "_in" + iteration);
//					intermediateTargetVariable = targetVariable;
//				}
//			}
//			pathBinding = getLeftPathElement().visitPathBinding(pathBinding,iteration);
//			pathBinding = getRightPathElement().visitPathBinding(pathBinding,iteration);			
////			predicateEdge = new EdgeBinding( intermediateSourceVariable,getPredicateVariable(), intermediateTargetVariable,getIsInverseOf());
////			pathBinding.add(predicateEdge);
//		}
//		return pathBinding;
		
	}

}
