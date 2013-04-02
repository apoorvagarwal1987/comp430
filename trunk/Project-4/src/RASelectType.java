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
	private RAJoinType _raJoin;
	
	
	public RASelectType (RAJoinType _raJoin){
		this.type = "RA_SELECT_TYPE";
		this._raJoin = _raJoin;
	}
	/* (non-Javadoc)
	 * @see IRAType#getType()
	 */
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
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
	 * @return the _raJoin
	 */
	public RAJoinType get_raJoin() {
		return _raJoin;
	}
	/**
	 * @param _raJoin the _raJoin to set
	 */
	public void set_raJoin(RAJoinType _raJoin) {
		this._raJoin = _raJoin;
	}
	
}
