import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 */

/**
 * @author apoorv agarwal
 *
 */
public class RATableType implements IRAType {
	private String type;
	private String value;
	private String alias;
	private ArrayList <Attribute> attributes;
	private Map<String, AttInfo> attributesInfo;
	private double tupleCount;
	private int joinCount;
	private Map <String, TableData> res;
	private int position; 
	private IRAType _previous;
	
	public RATableType(String tableName, String alias,boolean replace, int position) {
		this.res = Interpreter.res;
		this.value = tableName;
		this.alias = alias;
		this.type = "RA_TABLE_TYPE";
		
		// publishing the attribute of the table in the objects of the RA Table-type		
		this.attributesInfo = new HashMap<String, AttInfo>();
		Map<String, AttInfo> tattributesInfo = res.get(tableName).getAttributes();		
		for(String att: tattributesInfo.keySet()){
			AttInfo oldAtt = tattributesInfo.get(att);
			AttInfo newAtt = new AttInfo(oldAtt.getNumDistinctVals(), oldAtt.getDataType(),
								oldAtt.getAttSequenceNumber(), oldAtt.getAttName());
			newAtt.setAlias(alias);
			newAtt.setTableName(tableName);
			attributesInfo.put(att, newAtt);
		}
		
		tupleCount = res.get(tableName).getTupleCount();
		this.position = position;
		
		ArrayList<AttInfo> tempData = new ArrayList<AttInfo>();
		Collections.sort(tempData, new AttInfoComparator());
		attributes = new ArrayList<Attribute>();
		for (AttInfo attrib : tempData){
			attrib.setAlias(alias);
			attrib.setTableName(tableName);
			attributes.add(new Attribute(attrib.getDataType(),""+alias+"_"+attrib.getAttName()));
		}		
	}
	
	/**
	 * @return the attributes
	 */
	public ArrayList<Attribute> getAttributes() {
		return attributes;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;		
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
	 * @return the alias
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * @param alias the alias to set
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * @return the attributesInfo
	 */
	public Map<String, AttInfo> getAttributesInfo() {
		return attributesInfo;
	}

	/**
	 * @param attributesInfo the attributesInfo to set
	 */
	public void setAttributesInfo(Map<String, AttInfo> attributesInfo) {
		this.attributesInfo = attributesInfo;
	}

	/**
	 * @return the tupleCount
	 */
	public double getTupleCount() {
		return tupleCount;
	}

	/**
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}


	public IRAType getPrevious() {
		return _previous;
	}


	public void setPrevious(IRAType _previous) {
		this._previous = _previous;
	}

	/* (non-Javadoc)
	 * @see IRAType#getNext()
	 */
	@Override
	public IRAType getNext() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see IRAType#setNext(IRAType)
	 */
	@Override
	public void setNext(IRAType _next) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @return the joinCount
	 */
	public int getJoinCount() {
		return joinCount;
	}

	/**
	 * @param joinCount the joinCount to set
	 */
	public void setJoinCount(int joinCount) {
		this.joinCount += joinCount;
	}


	public void setTupleCount(int tupleCount) {
				
	}

	/* (non-Javadoc)
	 * @see IRAType#setTupleCount(double)
	 */
	@Override
	public void setTupleCount(double tupleCount) {
		// TODO Auto-generated method stub
		
	}
	
}
