/*
 * inova8 2020
 */
package pathPatternElement;

import pathPatternProcessor.PathConstants;
import pathPatternProcessor.PathConstants.EdgeCode;
import pathPatternProcessor.PathConstants.FilterOperator;
import pathQLRepository.PathQLRepository;

/**
 * The Class FilterOperatorValueElement.
 */
public class FilterOperatorValueElement extends ValueElement {
	
	/** The filter operator. */
	FilterOperator filterOperator;
	
	/**
	 * Instantiates a new filter operator value element.
	 *
	 * @param source the source
	 */
	public FilterOperatorValueElement(PathQLRepository source) {
		super(source);
		operator =PathConstants.Operator.FILTEROPERATOR;
	}
	
	/**
	 * Gets the filter operator.
	 *
	 * @return the filter operator
	 */
	public FilterOperator getFilterOperator() {
		return filterOperator;
	}

	/**
	 * Sets the filter operator.
	 *
	 * @param operator the new filter operator
	 */
	public void setFilterOperator(FilterOperator operator) {
		this.filterOperator = operator;
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
	public Integer indexVisitor(Integer baseIndex, Integer entryIndex,EdgeCode edgeCode) {
		// TODO Auto-generated method stub
		return null;
	}

}
