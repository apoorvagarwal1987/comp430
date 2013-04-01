import java.util.ArrayList;
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
	private ArrayList <Attribute> tableAttribute;
	private Map<String, AttInfo> attributesInfo;
	
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
	
}
