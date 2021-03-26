package pathPatternElement;

import pathPatternProcessor.PathConstants;
import pathPatternProcessor.PathConstants.EdgeCode;
import pathPatternProcessor.PathConstants.FilterOperator;

public class FilterOperatorValueElement extends ValueElement {
	FilterOperator filterOperator;
	public FilterOperatorValueElement() {
		super();
		operator =PathConstants.Operator.FILTEROPERATOR;
	}
	public FilterOperator getFilterOperator() {
		return filterOperator;
	}

	public void setFilterOperator(FilterOperator operator) {
		this.filterOperator = operator;
	}
	@Override
	public Integer indexVisitor(Integer baseIndex, Integer entryIndex,EdgeCode edgeCode) {
		// TODO Auto-generated method stub
		return null;
	}

}
