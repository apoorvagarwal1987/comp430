import java.util.ArrayList;
import java.util.Collections;
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

	ArrayList <Attribute> getAttributeInfo (String alias, String tableName){
		ArrayList <Attribute> attributes = new ArrayList<Attribute>();
		Map<String, AttInfo> attributesInfo = res.get(tableName).getAttributes();
		ArrayList<AttInfo> tempData = new ArrayList<AttInfo>();
		for(String att : attributesInfo.keySet()){
//			Collections.sort(attributesInfo.get(att),new AttInfoComparator());
//			attributes.add(new Attribute(""+alias+"."+attributesInfo.get(att).getDataType(), att));			
			tempData.add(attributesInfo.get(att));
		
		}
		Collections.sort(tempData, new AttInfoComparator());
		for (AttInfo attrib : tempData){
			//attrib.print();
			attributes.add(new Attribute(attrib.getDataType(),attrib.getAttName()));
		}
		Iterator<Attribute> att = attributes.iterator();
		System.out.println("Print __________");
		while(att.hasNext()){
			att.next().print();
		}
		System.out.println("__________");
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
			String alias = aliases.next().toString();
			tableName = myFrom.get(alias);
			tableAttribute = getAttributeInfo(alias,tableName);
		}
		ArrayList <Attribute> selectExpTypes = makeTypeOutAttributes();
		HashMap <String, String> exprs = makeSelectExpression(selectExpTypes);
	    String selection = "(Int)1 == (Int)1";  // "c_custkey < Int (10)";
	    String tableUsed = "src/"+tableName+".tbl";
	    
	    System.out.println("---------------------");
	    for (Attribute att : tableAttribute){
	    	att.print();
	    }
	    System.out.println("---------------------");

	    for (Attribute att : selectExpTypes){
	    	att.print();
	    }
	    System.out.println("---------------------");

	    System.out.println(exprs);
	    System.out.println("---------------------");

	    System.out.println(tableUsed+"\n"+outputFile+"\n"+compiler+"\n" + outputLocation);
	    
	    System.out.println("---------------------");

	    
	    
//	    System.out.println(tableAttribute+"   \n"+selectExpTypes+"\n"+exprs);
	    // run the selection operation
	    try {
	      Selection foo = new Selection (tableAttribute, selectExpTypes, selection, exprs, tableUsed, outputFile, compiler, outputLocation );

	    } catch (Exception e) {
	      throw new RuntimeException (e);
	    }
	}
}
