import java.util.ArrayList;


/**
 * @author apoorvagarwal
 *
 */
public class RAJoinType implements IRAType {
	private String type;
	private String value;
	private IRAType _left;
	private IRAType _right;
	private IRAType _previous;
//	private RAJoinType _raJoin;
 	private ReturnJoin _outputInfo;
 	
	public RAJoinType (){
		this.type = "RA_JOIN_TYPE";
	}
	

	public String getType() {
		return this.type;
	}


	public void setType(String type) {
		
	}

	/**
	 * @param _raLeftTable
	 * @param _raRightTable
	 */
	public void setBranch(RATableType _raLeftTable, RATableType _raRightTable) {
		this._left = _raLeftTable;
		this._right = _raRightTable;
//		this._raJoin = null;
//		this._outattribs = new ArrayList<AttribJoin>();
	}

	/**
	 * @param _raRightTable
	 * @param _insertedRAJoin
	 */
	public void setBranch(RAJoinType _insertedRAJoin,RATableType _raRightTable) {
		this._left = _insertedRAJoin;
		this._right = _raRightTable;
//		this._raJoin = _insertedRAJoin;		
	}

	/**
	 * @return the _leftTable
	 */
	public IRAType get_leftTable() {
		return _left;
	}

	/**
	 * @param _leftTable the _leftTable to set
	 */
	public void set_leftTable(RATableType _leftTable) {
		this._left = _leftTable;
	}

	/**
	 * @return the _rightTable
	 */
	public IRAType get_rightTable() {
		return _right;
	}

	/**
	 * @param _rightTable the _rightTable to set
	 */
	public void set_rightTable(RATableType _rightTable) {
		this._right = _rightTable;
	}


	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}


	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}


	/**
	 * @return the _left
	 */
	public IRAType get_left() {
		return _left;
	}


	/**
	 * @param _left the _left to set
	 */
	public void set_left(IRAType _left) {
		this._left = _left;
	}


	/**
	 * @return the _right
	 */
	public IRAType get_right() {
		return _right;
	}


	/**
	 * @param _right the _right to set
	 */
	public void set_right(IRAType _right) {
		this._right = _right;
	}


	/**
	 * @return the _previous
	 */
	public IRAType get_previous() {
		return _previous;
	}


	/**
	 * @param _previous the _previous to set
	 */
	public void setPrevious(IRAType _previous) {
		this._previous = _previous;
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
	 * @return the _raJoin
	 */
/*	public IRAType get_raJoin() {
		return _raJoin;
	}*/

	/**
	 * @param _raJoin the _raJoin to set
	 */
	/*public void IRAType(IRAType _raJoin) {
		this._raJoin = _raJoin;
	}*/
	
}
