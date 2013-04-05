import java.util.ArrayList;

/**
 * 
 */

/**
 * @author apoorvagarwal
 *
 */
public class RASelectType implements IRAType {
	private String type;
	private String value;
	private Expression selectPredicate;
	private IRAType _next;
	private ReturnJoin _outputInfo;
	private IRAType _previous;
	
	public RASelectType (Expression selExpression){
		this.type = "RA_SELECT_TYPE";		
		this.selectPredicate = selExpression;
	}
	/* (non-Javadoc)
	 * @see IRAType#getType()
	 */
	@Override
	public String getType() {
		return type;
	}

	/* (non-Javadoc)
	 * @see IRAType#setType(java.lang.String)
	 */
	@Override
	public void setType(String type) {
		// TODO Auto-generated method stub

	}
	/**
	 * @return the selectPredicate
	 */
	public Expression getSelectPredicate() {
		return selectPredicate;
	}
	/**
	 * @param selectPredicate the selectPredicate to set
	 */
	public void setSelectPredicate(Expression selectPredicate) {
		this.selectPredicate = selectPredicate;
	}
	/**
	 * @return the _next
	 */
	public IRAType getNext() {
		return _next;
	}
	/**
	 * @param _next the _next to set
	 */
	public void setNext(IRAType _next) {
		this._next = _next;
	}

	/**
	 * @return the _outputInfo
	 */
	public ReturnJoin get_outputInfo() {
		return _outputInfo;
	}
	/**
	 * @param _outputInfo the _outputInfo to set
	 */
	public void set_outputInfo(ReturnJoin _outputInfo) {
		this._outputInfo = _outputInfo;
	}
	/**
	 * @return the _previous
	 */
	public IRAType getPrevious() {
		return _previous;
	}
	/**
	 * @param _previous the _previous to set
	 */
	public void setPrevious(IRAType _previous) {
		this._previous = _previous;
	}

	
	
}
