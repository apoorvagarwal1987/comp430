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
	
	/**
	 * 
	 * @param alias
	 * @param tableName
	 * @param replace
	 * @return
	 */
	private ArrayList <Attribute> getTableAttributeInfo (String alias, String tableName,boolean replace ){
		ArrayList <Attribute> attributes = new ArrayList<Attribute>();
		Map<String, AttInfo> attributesInfo = res.get(tableName).getAttributes();
		ArrayList<AttInfo> tempData = new ArrayList<AttInfo>();
		for(String att : attributesInfo.keySet()){
			tempData.add(attributesInfo.get(att));		
		}
		Collections.sort(tempData, new AttInfoComparator());
		for (AttInfo attrib : tempData){
			if(replace)
				attributes.add(new Attribute(attrib.getDataType(),""+alias+"_"+attrib.getAttName()));
			else
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
	
	/**
	 * 
	 * @return
	 */
	private ArrayList <Attribute> makeTypeOutAttributes(){
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
	
	/**
	 * 
	 * @param selectExp
	 * @return
	 */
	private HashMap <String, String> makeSelectExpression (ArrayList<Attribute> selectExp,boolean skip){
		HashMap <String, String> exprs = new HashMap <String, String> ();
		Iterator<Attribute> attributes = selectExp.iterator();
		Iterator<Expression> selExprs = mySelect.iterator();
		while(attributes.hasNext()){
			String selExpression = CommonMethods.parseExpression(selExprs.next(),myFrom,skip);
//			String param = selExpression.substring(selExpression.indexOf(".")+1);
			int ind1 = selExpression.indexOf('(');
			int ind2 = selExpression.lastIndexOf(')');
			//if((ind1 == 0) &&(ind2 == selExpression.length()-1))
				//selExpression = selExpression.substring(1, selExpression.length()-2);
			
			exprs.put(attributes.next().getName(),selExpression);
		}		
		return exprs;
	}
	
	/**
	 * Function to execute the query with only one table in FROM clause
	 */
	public void doSelection(){
		ArrayList <Attribute> tableAttribute = new ArrayList<Attribute>();
		Iterator<String> aliases = myFrom.keySet().iterator();
		String tableName = null;
		while(aliases.hasNext()){
			String alias = aliases.next().toString();
			tableName = myFrom.get(alias);
			tableAttribute = getTableAttributeInfo(alias,tableName,true);
		}		
		ArrayList <Attribute> selectExpTypes = makeTypeOutAttributes();
		HashMap <String, String> exprs = makeSelectExpression(selectExpTypes,true);
		
		String selection = "(Int)1 == (Int) 1";
		if(where!= null){
			selection = CommonMethods.parseExpression(where, myFrom,true);
//			int ind1 = selection.indexOf('(');
//			int ind2 = selection.lastIndexOf(')');
			//if((ind1 ==0) &&(ind2 == selection.length()-1))
				//selection = selection.substring(1, selection.length()-1);
		}
		
//		String selection = "c_custkey < Int (10)";
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
	    
	    System.out.println(selection);
	    
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
	
	/**
	 * 
	 * @param leftAlias
	 * @param leftTableName
	 * @param rightAlias
	 * @param rightTableName
	 */
	private void doJoinHelper(String leftAlias, String leftTableName,String rightAlias, String rightTableName){
		ArrayList <Attribute> inAttsLeft = getTableAttributeInfo(leftAlias, leftTableName,false);
		ArrayList <Attribute> inAttsRight = getTableAttributeInfo(rightAlias, rightTableName,false);
		ArrayList <Attribute> outAtts =  makeTypeOutAttributes();
		String leftTablePath = "src/"+leftTableName+".tbl";
		String rightTablePath = "src/"+rightTableName+".tbl";
		
		/*
		 * Code to replace the alias
		 * with the left and right "keywords"
		 * in the select expressions.
		 */
		HashMap <String, String> tempExprs = makeSelectExpression(outAtts,false);
		Iterator<String> exprsIterator = tempExprs.keySet().iterator();
		HashMap <String, String> exprs = new HashMap<String, String>();
		while(exprsIterator.hasNext()){
			String tempExp = exprsIterator.next().toString();
			String selectionPredicates = tempExprs.get(tempExp);
			selectionPredicates = selectionPredicates.replaceAll(leftAlias, "left");
			selectionPredicates = selectionPredicates.replaceAll(rightAlias, "right");
			exprs.put(tempExp,selectionPredicates);
		}
		
		
		ArrayList <String> leftHash = new ArrayList <String> ();
//		leftHash.add ("o_custkey");
		
	    ArrayList <String> rightHash = new ArrayList <String> ();
//	    rightHash.add ("c_custkey");
	    
	    
		String wherePredicate = "(Int)1 == (Int) 1";
	/*	if(where!= null){
			wherePredicate = CommonMethods.parseExpression(where, myFrom,false);
			wherePredicate = wherePredicate.replaceAll(leftAlias, "left");
			wherePredicate = wherePredicate.replaceAll(rightAlias, "right");
		}
		*/
		 // run the join
	    try {
	    	
	      Join foo = new Join (inAttsLeft, inAttsRight, outAtts, leftHash, rightHash, wherePredicate, exprs, 
	    		  leftTablePath, rightTablePath, outputFile, compiler, outputLocation); 
	    } catch (Exception e) {
	      throw new RuntimeException (e);
	    }
	}
	
	
	
	
	
	
	/**
	 * Function to execute the query with more than one table in FROM clause
	 */
	
	//TODO STUB code need to fix this by Monday.
	public void doJoin() {
		doJoinHelper("l1", "lineitem", "o1", "orders");		
		/*RAExpression _raWhereRoot = CommonMethods.createRAExpression(where);
		CommonMethods.traverseRAExpression(_raWhereRoot);
		while (mySelect.iterator().hasNext()){
			Expression exp = mySelect.iterator().next();
			RAExpression selRAExpression = CommonMethods.createRAExpression(exp);
			
		}*/
	}
	
	/**
	 * 
	 */
	public void execution() {
		switch(myFrom.size()){
			case 1:
				doSelection();
				break;
			
			default:
				doJoin();
				break;				
		}		
	}	
}
