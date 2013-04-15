import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;


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
 	private HashSet<String> underlyingTables;
 	private ArrayList<Expression> selectionPredicate;
 	private int tupleCount;
 	
	/**
	 * @return the tupleCount
	 */
	public int getTupleCount() {
		return tupleCount;
	}


	/**
	 * @param tupleCount the tupleCount to set
	 */
	public void setTupleCount(int tupleCount) {
		this.tupleCount = tupleCount;
	}


	public RAJoinType (){
		this.type = "RA_JOIN_TYPE";
		this.underlyingTables = new HashSet<String>();
		this.selectionPredicate = new ArrayList<Expression>();
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
		this.underlyingTables.add(_raLeftTable.getAlias());
		this.underlyingTables.add(_raRightTable.getAlias());
	}

	/**
	 * @param _raRightTable
	 * @param _insertedRAJoin
	 */
	public void setBranch(RAJoinType _insertedRAJoin,RATableType _raRightTable) {
		this._left = _insertedRAJoin;
		this._right = _raRightTable;
		Iterator<String> tableIterator = _insertedRAJoin.getUnderlyingTables().iterator();
		while (tableIterator.hasNext()){
			this.underlyingTables.add(tableIterator.next());
		}
		this.underlyingTables.add(_raRightTable.getAlias());
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
	public IRAType getLeft() {
		return _left;
	}


	/**
	 * @param _left the _left to set
	 */
	public void setLeft(IRAType _left) {
		this._left = _left;
	}


	/**
	 * @return the _right
	 */
	public IRAType getRight() {
		return _right;
	}


	/**
	 * @param _right the _right to set
	 */
	public void setRight(IRAType _right) {
		this._right = _right;
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


	/**
	 * @return the _outputInfo
	 */
	public ReturnJoin getOutputInfo() {
		return _outputInfo;
	}


	/**
	 * @param _outputInfo the _outputInfo to set
	 */
	public void setOutputInfo(ReturnJoin _outputInfo) {
		this._outputInfo = _outputInfo;
	}


	/**
	 * @return the underlyingTables
	 */
	public HashSet<String> getUnderlyingTables() {
		return underlyingTables;
	}


	/**
	 * @param underlyingTables the underlyingTables to set
	 */
	public void setUnderlyingTables(HashSet<String> underlyingTables) {
		this.underlyingTables = underlyingTables;
	}


	public IRAType getNext() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setNext(IRAType _next) {
		// TODO Auto-generated method stub
		
	}


	/**
	 * @return the selectionPredicate
	 */
	public ArrayList<Expression> getSelectionPredicate() {
		return selectionPredicate;
	}


	/**
	 * @param selectionPredicate the selectionPredicate to set
	 */
	public void setSelectionPredicate(Expression selectionPredicate) {
		this.selectionPredicate.add(selectionPredicate);
	}


	
	
}
