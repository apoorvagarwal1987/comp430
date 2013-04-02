import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * 
 */

/**
 * @author apoorvagarwal
 *
 */
public class RATableType implements IRAType {
	private String type;
	private String value;
	private String alias;
	private ArrayList <Attribute> attributes;
	private Map<String, AttInfo> attributesInfo;
	private int tupleCount;
	private Map <String, TableData> res;
	private int position; 
	
	
	public RATableType(String tableName, String alias,boolean replace, int position) {
		this.res = Interpreter.res;
		this.value = tableName;
		this.alias = alias;
		this.type = "RA_TABLE_TYPE";
		
		// publishing the attribute of the table in the objects of the RA Table-type		
		attributesInfo = res.get(tableName).getAttributes();
		tupleCount = res.get(tableName).getTupleCount();
		this.position = position;
		ArrayList<AttInfo> tempData = new ArrayList<AttInfo>();
		for(String att : attributesInfo.keySet()){
			tempData.add(attributesInfo.get(att));		
		}
		Collections.sort(tempData, new AttInfoComparator());
		for (AttInfo attrib : tempData){
			attrib.setAlias(alias);
			attrib.setTableName(tableName);
			if(replace)
				attributes.add(new Attribute(attrib.getDataType(),""+alias+"_"+attrib.getAttName()));
			else
				attributes.add(new Attribute(attrib.getDataType(),attrib.getAttName()));
		}		
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
	
}
