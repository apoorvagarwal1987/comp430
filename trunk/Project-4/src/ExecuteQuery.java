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
	String outputFile;
	String compiler;
	String outputLocation;
	
	public ExecuteQuery(Map <String, String> myFrom,ArrayList <Expression> mySelect,String att,Expression where,
			ArrayList<ResultValue> selTypes){
		this.myFrom = myFrom;
		this.mySelect = mySelect;
		this.att =  att;
		this.where = where;	
		this.res = Interpreter.res;
		this.selTypes = selTypes;
		outputFile= "src/out.tbl";
		compiler = "g++";
		outputLocation = "src/cppDir/";
		
	}
	
	/**
	 * @param outputFile the outputFile to set
	 */
	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}

	/**
	 * @param compiler the compiler to set
	 */
	public void setCompiler(String compiler) {
		this.compiler = compiler;
	}

	/**
	 * @param outputLocation the outputLocation to set
	 */
	public void setOutputLocation(String outputLocation) {
		this.outputLocation = outputLocation;
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
	
	HashMap <String, String> makeSelectExpression (ArrayList<Attribute> selectExp){
		HashMap <String, String> exprs = new HashMap <String, String> ();
		Iterator<Attribute> attributes = selectExp.iterator();
		Iterator<Expression> selExprs = mySelect.iterator();
		while(attributes.hasNext()){
			String selExpression = selExprs.next().getValue();
			String param = selExpression.substring(selExpression.indexOf(".")+1);
			exprs.put(attributes.next().getName(),param);
		}		
		return exprs;
	}
	
	/**
	 * 
	 */
	public void execution() {
		doSelection();
	}
	
	
	public void doSelection(){
		ArrayList <Attribute> tableAttribute = new ArrayList<Attribute>();
		Iterator<String> aliases = myFrom.keySet().iterator();
		String tableName = null;
		while(aliases.hasNext()){
			tableName = myFrom.get(aliases.next().toString());
			tableAttribute = getAttributeInfo(tableName);
		}
		ArrayList <Attribute> selectExpTypes = makeTypeOutAttributes();
		HashMap <String, String> exprs = makeSelectExpression(selectExpTypes);
	    String selection = "";
	    String tableUsed = "src/"+tableName+".tbl";
	    System.out.println(tableUsed);
	    // run the selection operation
	    try {
	      Selection foo = new Selection (tableAttribute, selectExpTypes, selection, exprs, tableUsed, outputFile, compiler, outputLocation ); 
	    } catch (Exception e) {
	      throw new RuntimeException (e);
	    }
	}
}
