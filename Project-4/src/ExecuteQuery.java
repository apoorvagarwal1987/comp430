import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 
 */

/**
 * @author apoorvagarwal
 *
 */
public class ExecuteQuery {
	Map <String, String> myFrom;
	ArrayList <Expression> mySelect;
	String att;
	Expression where;
	Map <String, TableData> res;
	ArrayList<ResultValue> selTypes;
	
	public ExecuteQuery(Map <String, String> myFrom,ArrayList <Expression> mySelect,String att,Expression where,
			ArrayList<ResultValue> selTypes){
		this.myFrom = myFrom;
		this.mySelect = mySelect;
		this.att =  att;
		this.where = where;	
		this.res = Interpreter.res;
		this.selTypes = selTypes;
	}
	
	ArrayList <Attribute> getAttributeInfo (String tableName){
		ArrayList <Attribute> attributes = new ArrayList<Attribute>();
		Map<String, AttInfo> attributesInfo = res.get(tableName).getAttributes();
		for(String att : attributesInfo.keySet()){
			attributes.add(new Attribute(attributesInfo.get(att).getDataType(), att));			
		}
		Iterator<Attribute> att = attributes.iterator();
		return attributes;
	}
	
	ArrayList <Attribute> makeTypeOutAttributes(){
		ArrayList <Attribute> outAttributes = new ArrayList<Attribute>();
		int outCount = 1;
		Iterator<ResultValue> rv = selTypes.iterator();
		while(rv.hasNext()){
			switch((rv.next()).getType()){
				case 0:
					outAttributes.add(new Attribute("Str", "att"+outCount));
					break;
				case 1:
					outAttributes.add(new Attribute("Int", "att"+outCount));
					break;				
				case 2:
					outAttributes.add(new Attribute("Float", "att"+outCount));
					break;
				default:
					System.out.println("Serious ERROR");
					System.exit(-1);
			}
			outCount++;
		}		
		return outAttributes;
	}
	
	HashMap <String, String> makeSelectExpression (){
		HashMap <String, String> exprs = new HashMap <String, String> ();
		
		
		
		return exprs;
	}
	
	/**
	 * 
	 */
	public void execution() {
		doSelection();
	}
	
	
	public void doSelection(){
		ArrayList <Attribute> tableAttribute = null;
		Iterator<String> aliases = myFrom.keySet().iterator();
		while(aliases.hasNext()){
			String tableName = myFrom.get(aliases.next().toString());
			tableAttribute = getAttributeInfo(tableName);
		}
		Iterator<Attribute> att = makeTypeOutAttributes().iterator();
		
	}
}
