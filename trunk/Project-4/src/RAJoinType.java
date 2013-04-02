import java.util.ArrayList;


/**
 * @author apoorvagarwal
 *
 */
public class RAJoinType implements IRAType {
	private String type;
	private String value;
	private RATableType _leftTable;
	private RATableType _rightTable;
	private RAJoinType _raJoin;
 	private ArrayList<AttribJoin> _outattribs ;
	public RAJoinType (){
		this.type = "RA_JOIN_TYPE";
	}
	
	/* (non-Javadoc)
	 * @see IRAType#getType()
	 */
	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return this.type;
	}

	/* (non-Javadoc)
	 * @see IRAType#setType(java.lang.String)
	 */
	@Override
	public void setType(String type) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @param _raLeftTable
	 * @param _raRightTable
	 */
	public void setTables(RATableType _raLeftTable, RATableType _raRightTable) {
		this._leftTable = _raLeftTable;
		this._rightTable = _raRightTable;
		this._raJoin = null;
		this._outattribs = new ArrayList<AttribJoin>();
	}

	/**
	 * @param _raRightTable
	 * @param _insertedRAJoin
	 */
	public void setTables(RATableType _raRightTable, RAJoinType _insertedRAJoin) {
		this._leftTable = null;
		this._rightTable = _raRightTable;
		this._raJoin = _insertedRAJoin;		
	}

	/**
	 * @return the _leftTable
	 */
	public RATableType get_leftTable() {
		return _leftTable;
	}

	/**
	 * @param _leftTable the _leftTable to set
	 */
	public void set_leftTable(RATableType _leftTable) {
		this._leftTable = _leftTable;
	}

	/**
	 * @return the _rightTable
	 */
	public RATableType get_rightTable() {
		return _rightTable;
	}

	/**
	 * @param _rightTable the _rightTable to set
	 */
	public void set_rightTable(RATableType _rightTable) {
		this._rightTable = _rightTable;
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
