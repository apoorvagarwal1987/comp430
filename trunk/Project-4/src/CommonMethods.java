/**
 * 
 */
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author apoorvagarwal
 * This class holds the collection of all the 
 * methods collectively used by the project
 * across the classes.
 */
public class CommonMethods {
	
	static int nameCounter ;
	static ArrayList<Expression> _selectionPredicates;
	static HashSet<String> contributedTable;
	static IRAType helper ;
	static boolean merge ;
	
	//This needs to be initialized during the call of the class
	static Map <String, String> fromClause;
	
	static{
		nameCounter = 1;
		_selectionPredicates = new ArrayList<Expression>();
		contributedTable = new HashSet<String>();			
		merge = true;
	}
	
	/**
	 * This function return the attribute type from the table. 	
	 * @param att : The name of the attribute
	 * @param fromClause : The from clause used in the query
	 * @return : returns the type of the attribute.
	 */
	public static String getAtributeType(String att, Map <String, String> fromClause){
	  	String attributeType;
		String alias = att.substring(0, att.indexOf("."));
		String tableName = fromClause.get(alias);
		if(tableName == null){
			System.out.println("Error: Alias "+ alias +" do not correspond to the any table in the FROM clause");
			return null;
		}
		String attName = att.substring(att.indexOf(".")+1);
		Map<String, AttInfo> attributesInfo = (Interpreter.res).get(tableName).getAttributes();
		
		if (attributesInfo == null){
			System.out.println("Error: Table "+ tableName +" do not exist in the CATALOGUE");
			return null;
		}
		
		if(!(attributesInfo.containsKey(attName))){
			System.out.println("Error: Attribute "+ attName +" do not exist in the TABLE: "+ tableName);
			return null;
		}
		else
			attributeType = attributesInfo.get(attName).getDataType();
		
	  return attributeType;
	  
  }
	
	/**
	 * To check whether the expression type is binary or not
	 * @param expType : Type of the expression
	 * @return : true or false
	 */
	public static boolean isUnaryOperation(String expType) {
		for (String operation : Expression.unaryTypes) {
			if(operation.equals(expType))
				return true;
		}
		return false;
	}
	  
	/**
	 * To check whether the expression type is unary or not
	 * @param expType : Type of the expression
	 * @return : true or false
	 */
	public static boolean isBinaryOperation(String expType) {
		for (String operation : Expression.binaryTypes) {
			if(operation.equals(expType))
				return true;
		}
		return false;
	}
	
	/**
	 * The functions converts the where clause of the query into the parsed expression 
	 * @param exp : expression that needs to be parsed
	 * @param fromClause : From clause of the query
	 * @param skip : Used during the Selection or Join operation
	 * @return : return the parsed string.
	 */
	public static String parseExpression (Expression exp, Map <String, String> fromClause, boolean skip ){
		
		if(exp.getType().equals("and") || exp.getType().equals("or")){
			  String resValue1 = parseExpression(exp.getLeftSubexpression(),fromClause,skip);
			  String resValue2 = parseExpression(exp.getRightSubexpression(),fromClause,skip);
			  
			  if(exp.getType().equals("and"))
					  return (resValue1 + " && " + resValue2 );						  			
			  else{
				  return (resValue1 + " || " + resValue2);
			  }				  
		  }
		  
		  
		  if(CommonMethods.isUnaryOperation(exp.getType())){	
			  String rv = parseExpression(exp.getLeftSubexpression(),fromClause,skip);
			  
			  /*
			   *Operators handled in this part is 
			   * "not", "unary minus", "sum", "avg" 
			   */
			  
			  if(exp.getType().equals("not"))
				return ("not ( "+ rv +" )");
 
			  if(exp.getType().equals("sum"))
					return ("sum ( "+ rv +" )");
			  
			  if(exp.getType().equals("avg"))
					return ("avg ( "+ rv +" )");			  
			  
			  if(exp.getType().equals("unary minus"))
					return (" -  ( "+ rv +" )");		  
		  }
	  
		  if(CommonMethods.isBinaryOperation(exp.getType())){			  
			  String resValue1 = parseExpression(exp.getLeftSubexpression(),fromClause,skip);
			  String resValue2 = parseExpression(exp.getRightSubexpression(),fromClause,skip);
			  /*
			   * Operators handled in this function is 
			   * "plus", "minus", "times", "divided by", "or", "and", "equals", "greater than", "less than"
			   */
			  
			  if((resValue1!=null) && (resValue2 !=null)){				  
				  if(exp.getType().equals("plus"))
					  return (resValue1 + " + " + resValue2);	
				  
				  if(exp.getType().equals("minus"))
					  return (resValue1 + " - " + resValue2);
				  
				  if(exp.getType().equals("times"))
					  return (resValue1 + " * " + resValue2);
				  
				  if(exp.getType().equals("divided by"))
					  return (resValue1 + " / " + resValue2);
				  
				  if(exp.getType().equals("equals"))
					  return (resValue1 + " == " + resValue2);
				  
				  if(exp.getType().equals("greater than"))
					  return (resValue1 + " > " + resValue2);
				  
				  if(exp.getType().equals("less than"))
					  return (resValue1 + " < " + resValue2);				  
			  }
		  }	  
		  
		  if(exp.getType().equals("identifier")){			 
			  if(skip)
				  return exp.getValue().replace('.', '_');
			  else
				  return exp.getValue();
		  }
		  
		  /*
		   * Managing the constants in the expressions
		   */
		  String retString = "";
		  if(exp.getType().equals("literal string"))
			  retString = "Str (" + exp.getValue() + ")";
		  		  
		  else if (exp.getType().equals("literal int"))
			  retString = "Int (" + exp.getValue() +")";
		  
		  else
			  retString = "Float (" + exp.getValue() +")";
			  
	      return retString;		
	}
	
	/**
	 * The function checks whether the select exp is valid or not under the 
	 * group by clause
	 * @param exp : Expression to validate
	 * @param groupByAtt : Group by attribute
	 * @return : true or false
	 */
	public static boolean isValidSelExpressionGP(Expression exp, String groupByAtt){
		
		if(isBinaryOperation(exp.getType())){
			return true;
			//return isValidSelExpression(exp.getLeftSubexpression(), groupByAtt) && isValidSelExpression(exp.getRightSubexpression(), groupByAtt);			
		}
		
		else if (isUnaryOperation(exp.getType())){
			return true;
			//return isValidSelExpression(exp.getLeftSubexpression(), groupByAtt);
		}
		
		else if (exp.getType().equals("identifier") && !(exp.getValue().equals(groupByAtt))){
			System.out.println("Error: Expression "+ exp.print() +" expression is not allowed in the select clause when GroupBy");
			return false;
		}
		else
			return true;
		
	}
	
	/**
	 * The functions the compatibility between the result types received from left and right
	 * branch of the expression tree
	 * @param _resValue1 : Left side result value
	 * @param _resValue2 : Right side result value
	 * @param _type : Type of the caller
	 * @return : true or false
	 */
	public static ResultValue checkCompatibility(ResultValue _resValue1,ResultValue _resValue2,String _type){
		
		  if(_resValue1.isResult()&& _resValue2.isResult()){			  
			  if(_resValue1.getType()==1||_resValue1.getType()==2)
				  return (new IntegerCompatibility().compatibility(_resValue1, _resValue2, _type));			 
			  else
				  return (new StringCompatibility().compatibility(_resValue1, _resValue2, _type));
		  }
		  else
			  return (new ResultValue(-1, false));		  
	  }
	  
	/**
	 * The functions checks the validity of the expressions 
	 * @param exp : Expression to validate
	 * @param fromClause : From clause from the query
	 * @return : true or false
	 */
	public static ResultValue validateTypeExpression(Expression exp, Map <String, String> fromClause){
		  
		  if(exp.getType().equals("and") || exp.getType().equals("or")){
			  ResultValue resValue1 = validateTypeExpression(exp.getLeftSubexpression(),fromClause);
			  ResultValue resValue2 = validateTypeExpression(exp.getRightSubexpression(),fromClause);
			  
			  if(resValue1.isResult() && resValue2.isResult())
					  return (new ResultValue(-1, true));						  			
			  else{
				  return (new ResultValue(-1, false));
			  }				  
		  }
		  
		  
		  if(isUnaryOperation(exp.getType())){	
			  ResultValue rv = validateTypeExpression(exp.getLeftSubexpression(),fromClause);
			  if(!rv.isResult())
				  System.out.println("Error: Incompatible expression computation in: " + exp.print());			
			  
			  if(exp.getType().equals("not"))
				return rv;
			  else if (rv.getType()== 0 ){
				  System.out.println("Error: Incompatible expression computation in: " + exp.print());
				  return new ResultValue(-1, false);
			  }
			  else
				  return rv;
		  }
		  String expType = exp.getType();
		  String retType;
		  
		  if(isBinaryOperation(expType)){
			  ResultValue resValue1 = null;
			  ResultValue resValue2 = null;
			  resValue1 = validateTypeExpression(exp.getLeftSubexpression(),fromClause);
			  resValue2 = validateTypeExpression(exp.getRightSubexpression(),fromClause);
			  
			  if((resValue1!=null) && (resValue2 !=null)){
				  ResultValue rv = checkCompatibility(resValue1, resValue2, expType);	
				  if(!rv.isResult())
					  System.out.println("Error: Incompatible expression computation in: " + exp.print());
				  
				  return rv;		  
			  }
		  }	  
		  
		  if(exp.getType().equals("identifier")){
			  retType = getAtributeType(exp.getValue(), fromClause);
			  if(retType == null){
				 // System.out.println("Error: "+exp.getValue() +"  is not the valid attribute of the table");
				  return (new ResultValue(-1, false));
			  }
			  
			  if(retType.equals("Str"))
				  return (new ResultValue(0, true));
			  
			  else if (retType.equals("Int"))
				  return (new ResultValue(1, true));
			  
			  else
				  return (new ResultValue(2, true));
		  }
		  
		  if(exp.getType().equals("literal string"))
			   return (new ResultValue(0, true));
		  
		  else if (exp.getType().equals("literal int"))
			   return (new ResultValue(1, true));
		  
		  else
			  return (new ResultValue(2, true));
		  }
		
	public static IRAType createRATree (Map <String, String> fromClause, ArrayList <Expression> selectClause,
										Expression whereClause, String groupBy){
		
		// First creating the leaf nodes which is basically the
		// tables present in the from clause of the query.		
		Map<Integer, RATableType> tablePresent = new HashMap<Integer, RATableType>();
		Map<Integer, RAJoinType> crossJoinPresent = new HashMap<Integer, RAJoinType>();	
		Map<Integer, RASelectType> selectPredicatePresent = new HashMap<Integer, RASelectType>();
		Map<String,RATableType> tableMap = new HashMap<String, RATableType>();
		Iterator<String> aliases = fromClause.keySet().iterator();
		int counter = 1;
		ArrayList<RATableType> tableOrder = new ArrayList<RATableType>();
		while(aliases.hasNext()){
			String alias = aliases.next().toString();
			String tableName = fromClause.get(alias);
			RATableType tempRaTableType = new RATableType(tableName,alias,true, counter++);
			tableMap.put(alias, tempRaTableType);
		}
		if(whereClause != null)
			traverseSelExpression(createSelPredicate(whereClause));
		
		for (Expression exp : _selectionPredicates){	
			if(exp.getType().equals("equals")){
				Expression lExp = exp.getLeftSubexpression();
				Expression rExp = exp.getRightSubexpression();
				if(lExp.getType().equals("identifier") && rExp.getType().equals("identifier")){
					String lExpId = lExp.getValue();
					String rExpId = rExp.getValue();
					String lAlias = lExpId.substring(0,lExpId.indexOf('.'));
					String rAlias = rExpId.substring(0,rExpId.indexOf('.'));
					
					RATableType lTableType = tableMap.get(lAlias);
					lTableType.setJoinCount(1);
					tableMap.put(lAlias, lTableType);
					
					RATableType rTableType = tableMap.get(rAlias);
					rTableType.setJoinCount(1);
					tableMap.put(rAlias, rTableType);
					
				}
			}
		}
		_selectionPredicates.clear();
		
		Iterator<String> tableAlias = tableMap.keySet().iterator();
		while(tableAlias.hasNext()){
			tableOrder.add(tableMap.get(tableAlias.next()));
		}
		
		Collections.sort(tableOrder, new TableOrderComparator());
		counter = 1;
		for(RATableType tableType : tableOrder){
			tablePresent.put(counter++,tableType);
		}		

		//Creating all the join in the query 
		// starting with the basic cross joins in the query
		counter = 1;		
		int countTable = tablePresent.size();
		if(countTable == 1){
			RATableType _raJoinTop = tablePresent.get(counter); 
	  		int current = 1;
			if(whereClause != null){			
				traverseSelExpression(createSelPredicate(whereClause));
				for(Expression exp : _selectionPredicates){
					RASelectType _raSelectTemp = new RASelectType();
					_raSelectTemp.setSelectPredicate(exp);
					selectPredicatePresent.put(current++,_raSelectTemp);
				}
				current = 1;
				RASelectType _raSelect = selectPredicatePresent.get(current++);
				_raSelect.setNext(_raJoinTop);
				_raJoinTop.setPrevious(_raSelect);			
				while(current <= selectPredicatePresent.size()){
					selectPredicatePresent.get(current-1).setPrevious(selectPredicatePresent.get(current));
					selectPredicatePresent.get(current).setNext(selectPredicatePresent.get(current-1));
					current++;
				}
			}	
			
			RAProjectType _raProjectType = new RAProjectType(selectPredicatePresent.get(current-1));
			selectPredicatePresent.get(current-1).setPrevious(_raProjectType);
			_raProjectType.setSelectExprs(selectClause);
			ArrayList<String> groupbyClause = new ArrayList<String>();
			groupbyClause.add(groupBy);
			_raProjectType.setGroupBy(groupbyClause);
			
			while(merge){
				merge = false;
				mergeSelJoinNodes(_raProjectType);
			}
			System.out.println(_raProjectType);
			return _raProjectType;
			
		}		
		else{
			int current = 1;
			RAJoinType _raJoin = new RAJoinType();
			RATableType _raLeftTable = tablePresent.get(current);
			RATableType _raRightTable = tablePresent.get(++current);
			_raJoin.setBranch(_raLeftTable,_raRightTable);
			_raLeftTable.setPrevious(_raJoin);
			_raRightTable.setPrevious(_raJoin);
			crossJoinPresent.put(counter++,_raJoin);
			
			while(current < countTable){ 
				_raRightTable = tablePresent.get(++current);
				RAJoinType _raTempJoin = new RAJoinType();
				RAJoinType _insertedRAJoin = crossJoinPresent.get((counter-1));
				_raTempJoin.setBranch(_insertedRAJoin,_raRightTable);
				_raRightTable.setPrevious(_raTempJoin);
				_insertedRAJoin.setPrevious(_raTempJoin);
				crossJoinPresent.put(counter++,_raTempJoin);				
			}		
			RAJoinType _raJoinTop = crossJoinPresent.get(counter-1); 
	  		current = 1;
			if(whereClause != null){			
				traverseSelExpression(createSelPredicate(whereClause));
				for(Expression exp : _selectionPredicates){
					RASelectType _raSelectTemp = new RASelectType();
					_raSelectTemp.setSelectPredicate(exp);
					selectPredicatePresent.put(current++,_raSelectTemp);
				}
				current = 1;
				RASelectType _raSelect = selectPredicatePresent.get(current++);
				_raSelect.setNext(_raJoinTop);
				_raJoinTop.setPrevious(_raSelect);			
				while(current <= selectPredicatePresent.size()){
					selectPredicatePresent.get(current-1).setPrevious(selectPredicatePresent.get(current));
					selectPredicatePresent.get(current).setNext(selectPredicatePresent.get(current-1));
					current++;
				}
			}	
			
			RAProjectType _raProjectType = new RAProjectType(selectPredicatePresent.get(current-1));
			selectPredicatePresent.get(current-1).setPrevious(_raProjectType);
			_raProjectType.setSelectExprs(selectClause);
			
			int index = 1;
			while(index <= selectPredicatePresent.size())
				sendSelPredicateDown(selectPredicatePresent.get(index++));
			
			index = 1 ;
			while(index <= selectPredicatePresent.size())
				createNewConnection(selectPredicatePresent.get(index++));
			
			ArrayList<String> groupbyClause = new ArrayList<String>();
			groupbyClause.add(groupBy);
			_raProjectType.setGroupBy(groupbyClause);
			
			
			System.out.println(_raProjectType);

			while(merge){
				merge = false;
				mergeSelJoinNodes(_raProjectType);
			}
			mergeSelSelNodes(_raProjectType);
			System.out.println(_raProjectType);
			
			
			
			return _raProjectType;
		}
	}
	
	/**
	 * @param tableMap
	 * @param selectClause
	 */
	private static void analyzeTableOrder(Map<String, RATableType> tableMap, ArrayList<Expression> selectClause) {
		for (Expression exp : selectClause){	
			if(exp.getType().equals("equals")){
				Expression lExp = exp.getLeftSubexpression();
				Expression rExp = exp.getRightSubexpression();
				if(lExp.getType().equals("identifier") && rExp.getType().equals("identifier")){
					String lExpId = lExp.getValue();
					String rExpId = rExp.getValue();
					String lAlias = lExpId.substring(0,lExpId.indexOf('.'));
					String rAlias = rExpId.substring(0,rExpId.indexOf('.'));
					tableMap.get(lAlias).setJoinCount(1);
					tableMap.get(rAlias).setJoinCount(1);
				}
			}
		}
	}

	/**
	 * @param _raProjectType
	 */
	private static void mergeSelSelNodes(IRAType current) {
		String ctype = current.getType();
		
		if (ctype.equals("RA_JOIN_TYPE")){
			mergeSelSelNodes(((RAJoinType)current).getLeft());
			mergeSelSelNodes(((RAJoinType)current).getRight());
		}
		
		else if(ctype.equals("RA_SELECT_TYPE")){
			String ntype = current.getNext().getType();
			if (ntype.equals("RA_SELECT_TYPE")){
				ArrayList<Expression> currSelExp = ((RASelectType)current).getSelectPredicate();
				for(Expression exp: currSelExp){
					((RASelectType)current.getNext()).setSelectPredicate(exp);
				}
				
				IRAType currentPrevious = current.getPrevious();
				IRAType currentNext = current.getNext(); 
				
				if(currentPrevious.getType().equals("RA_SELECT_TYPE") ||
						currentPrevious.getType().equals("RA_PROJECT_TYPE") ){
					currentPrevious.setNext(currentNext);
					currentNext.setPrevious(currentPrevious);
				}				
				else if (currentPrevious.getType().equals("RA_JOIN_TYPE")){
					
					if(((RAJoinType)currentPrevious).getLeft() == current){
						((RAJoinType)currentPrevious).setLeft(currentNext);
					}
					else
						((RAJoinType)currentPrevious).setRight(currentNext);
					
					currentNext.setPrevious(currentPrevious);
				}
				else{
					System.out.println("SERIOUS ERROR undefined Tree structure");
				}
				
				mergeSelSelNodes(current.getNext());				
			}
			else if (!ntype.equals("RA_TABLE_TYPE"))
				mergeSelSelNodes(current.getNext());
		}	
		else{
			if (!ctype.equals("RA_TABLE_TYPE")&&!current.getNext().equals("RA_TABLE_TYPE"))
				mergeSelSelNodes(current.getNext());
		}		
	}

	private static void mergeSelJoinNodes(IRAType current){
		String ctype = current.getType();
		
		if (ctype.equals("RA_JOIN_TYPE")){
			mergeSelJoinNodes(((RAJoinType)current).getLeft());
			mergeSelJoinNodes(((RAJoinType)current).getRight());
		}
		
		else if(ctype.equals("RA_SELECT_TYPE")){
			String ntype = current.getNext().getType();
			if (ntype.equals("RA_JOIN_TYPE")){
				Expression selExp = ((RASelectType)current).getSelectPredicate().get(0);
				if(selExp.getType().equals("equals") && selExp.getLeftSubexpression().getType().equals("identifier")
						&& selExp.getRightSubexpression().getType().equals("identifier")
						){
							((RAJoinType)current.getNext()).setSelectionPredicate(selExp);
							merge = true;
							IRAType currentPrevious = current.getPrevious();
							IRAType currentNext = current.getNext(); 
							
							if(currentPrevious.getType().equals("RA_SELECT_TYPE")|| currentPrevious.getType().equals("RA_PROJECT_TYPE") ){
								currentPrevious.setNext(currentNext);
								currentNext.setPrevious(currentPrevious);
							}
							else if (currentPrevious.getType().equals("RA_JOIN_TYPE")){
								if(((RAJoinType)currentPrevious).getLeft() == current){
									((RAJoinType)currentPrevious).setLeft(currentNext);
								}
								else
									((RAJoinType)currentPrevious).setRight(currentNext);
								
								currentNext.setPrevious(currentPrevious);
							}
							else{
								System.out.println("SERIOUS ERROR undefined Tree structure");
							}
							
							mergeSelJoinNodes(current.getNext());				
				}
				else
					mergeSelJoinNodes(current.getNext());
			}
			else
				mergeSelJoinNodes(current.getNext());
		}
		
		else if (ctype.equals("RA_PROJECT_TYPE")){
			mergeSelJoinNodes(current.getNext());
		}
		
		
		else{
			
		}
	}
	
	
	private static void createNewConnection(IRAType selType){
		RASelectType selectionType = (RASelectType)selType;
		IRAType helper = selectionType.getUnderlyingJoin();
		IRAType selectionNext = selectionType.getNext();
		IRAType selectionPrevious = selectionType.getPrevious();
		
		//making changes in the connection			
		//Loop to check whether the helper join is not acted upon by any of the select statement
		
		boolean movement = false;
		IRAType tempPrev = helper.getPrevious();
		while(true){
			if (tempPrev == selType)
				return;
			
			else if(tempPrev.getType().equals("RA_SELECT_TYPE")){
				movement = true;
				helper = tempPrev;
				tempPrev = tempPrev.getPrevious();
			}
			else
				break;
		}
		
		if(!movement){
			selectionType.setNext(helper);
			IRAType helperPrevious = helper.getPrevious();
			helper.setPrevious(selectionType);
			selectionType.setPrevious(helperPrevious);
			if(helperPrevious.getType().equals("RA_JOIN_TYPE")){
				if(((RAJoinType)helperPrevious).getLeft()==helper)
					((RAJoinType)helperPrevious).setLeft(selectionType);
				
				else
					((RAJoinType)helperPrevious).setRight(selectionType);
			}
			
			else
				System.out.println(helperPrevious.getType());	
			
			selectionNext.setPrevious(selectionPrevious);
			selectionPrevious.setNext(selectionNext);
		}
		
		else{
			selectionType.setNext(helper);
			helper.setPrevious(selectionType);
			selectionType.setPrevious(tempPrev);
			
			if(tempPrev.getType().equals("RA_JOIN_TYPE")){
				if(((RAJoinType)tempPrev).getLeft()==helper)
					((RAJoinType)tempPrev).setLeft(selectionType);
				
				else
					((RAJoinType)tempPrev).setRight(selectionType);
			}
			else
				System.out.println(tempPrev.getType());	
			
			selectionNext.setPrevious(selectionPrevious);
			selectionPrevious.setNext(selectionNext);
		}

	}
	
	
	public static void executeRATree(IRAType _root){
		
		String type = _root.getNext().getType();
		

		ReturnJoin previousOutput = null;
		
		if(type.equals("RA_SELECT_TYPE"))
			previousOutput = execute((RASelectType)_root.getNext());
		
		else if(type.equals("RA_JOIN_TYPE"))
			previousOutput = execute((RAJoinType)_root.getNext());
		
		String outputFile= "src/out_"+nameCounter +".tbl";
		String compiler = "g++";
		String outputLocation = "src/cppDir/";
		nameCounter++;
		
		ArrayList <Attribute> tableAttribute = new ArrayList<Attribute>();
		ArrayList<AttribJoin> totalJoins = previousOutput.getJoinOutAttribts();
		Collections.sort(totalJoins,new AttribJoinComparator());
		
		Iterator<AttribJoin> totalJoinsIt = totalJoins.iterator();
		while (totalJoinsIt.hasNext()){
			AttInfo attrib = totalJoinsIt.next().getAttinfo();
			tableAttribute.add(new Attribute(attrib.getDataType(),""+attrib.getAlias()+"_"+attrib.getAttName()));
		}
		
		ArrayList<ResultValue> selResultValue = new ArrayList<ResultValue>();
		for(Expression exp : ((RAProjectType)_root).getSelectExprs()){
			selResultValue.add(validateTypeExpression(exp, fromClause));
		}
		
		ArrayList <Attribute> selectExpTypes = CommonMethods.makeTypeOutAttributes(selResultValue);
		
	//	HashMap <String, String> exprs = makeSelectExpression(selectExpTypes, true, ((RAProjectType)_root).getSelectExprs(), fromClause);
		
		HashMap<String,AggFunc> aggsSelect = makeProjectExpression(selectExpTypes, true, ((RAProjectType)_root).getSelectExprs(), fromClause);
		
		ArrayList <String> groupingAtts = new ArrayList <String> ();
		String groupAtt = ((RAProjectType)_root).getGroupBy().get(0);
		if(groupAtt != null){
			groupAtt = groupAtt.replace('.', '_');
			groupingAtts.add(groupAtt);
		}
		
		
		String selection = "(Int)1 == (Int)1";//parseExpression(_raSelectType.getSelectPredicate(), fromClause,true);
		String tableUsed = previousOutput.getOutputFile();
	    try {
		      @SuppressWarnings("unused")
		      Grouping foo = new Grouping (tableAttribute, selectExpTypes, groupingAtts, aggsSelect, 
		    		  tableUsed, outputFile,compiler, outputLocation); 

		      System.out.println("Final output: "+outputFile);
		      
		      nameCounter = 0;
		    } 
	   catch (Exception e) {
		      throw new RuntimeException (e);
       }		
	}
	
	
	/**
	 * @param selectExpTypes
	 * @param skip
	 * @param selectExprs
	 * @param fromClause2
	 * @return
	 */
	private static HashMap<String, AggFunc> makeProjectExpression(ArrayList<Attribute> selectExpTypes,
			boolean skip, ArrayList<Expression> selectExprs, Map<String, String> fromClause2) {
		
		HashMap <String, AggFunc> exprs = new HashMap <String, AggFunc> ();
		Iterator<Attribute> attributes = selectExpTypes.iterator();
		Iterator<Expression> selExprs = selectExprs.iterator();
		while(attributes.hasNext()){
			Expression exp = selExprs.next();
			String selExpression = CommonMethods.parseExpression(exp,fromClause,skip);	
			if(!isUnaryOperation(exp.getType())){
				AggFunc tempAggFunc = new AggFunc("none", selExpression); 
				exprs.put(attributes.next().getName(),tempAggFunc);
			}
				
			else{
				String funcName = exp.getType();
				int start = selExpression.indexOf('(');
				int end = selExpression.lastIndexOf(')');
				String newExpr = selExpression.substring(start+1 ,end);
				AggFunc tempAggFunc = new AggFunc(funcName, newExpr); 
				exprs.put(attributes.next().getName(),tempAggFunc);
			}		
		}				
		return exprs;
	}

	private static ReturnJoin execute(RASelectType current){
		
		//CASE 1: The case SELECT statement is followed by another SELECT statement.
		//CASE 2: The case SELECT statement is followed by another CROSS JOIN statement.
		if((current.getNext().getType().equals("RA_SELECT_TYPE"))|| (current.getNext().getType().equals("RA_JOIN_TYPE"))){
			ReturnJoin nextOutput = null;
			int nextType = 0;
			if (current.getNext().getType().equals("RA_SELECT_TYPE")){
				nextOutput = execute((RASelectType) current.getNext());
				nextType = 1;
			}
			
			else{
				nextOutput = execute((RAJoinType) current.getNext());
				nextType = 2;
			}
			String outputFile= "src/out_"+nameCounter +".tbl";
			String compiler = "g++";
			String outputLocation = "src/cppDir/";
			nameCounter++;				
			
			ArrayList <Expression> outExp = new ArrayList<Expression>();
			ArrayList<AttribJoin> currentOutAttribts = nextOutput.getJoinOutAttribts();
			String infile = nextOutput.getOutputFile();
			Collections.sort(currentOutAttribts,new AttribJoinComparator());
			
			ArrayList <Attribute> inAttribute = new ArrayList<Attribute>();
			ArrayList<ResultValue> outTypes = new ArrayList<ResultValue>();
			Iterator<AttribJoin> currentAtt = currentOutAttribts.iterator();
			int pos =1;
			while(currentAtt.hasNext() ){	
				AttribJoin attInformation = currentAtt.next();
				String dataType = attInformation.getAttinfo().getDataType();
				if(dataType.equals("Int"))
					outTypes.add(new ResultValue(1 , true));				
				else if(dataType.equals("Str"))
					outTypes.add(new ResultValue(0 ,true));				
				else
					outTypes.add(new ResultValue(2 , true));	
				
				inAttribute.add(new Attribute(dataType,""+attInformation.getAttinfo().getAlias()+"_"+attInformation.getAttinfo().getAttName()));
				Expression exp = new Expression("identifier");
				exp.setValue(""+attInformation.getAttinfo().getAlias()+"."+attInformation.getAttinfo().getAttName());
				outExp.add(exp);				
			}
			
			ArrayList <Attribute> outAttributes = CommonMethods.makeTypeOutAttributes(outTypes);
			HashMap <String, String> exprs = makeSelectExpression(outAttributes, true, outExp, fromClause);		
			
			ArrayList<Expression> currentExpression = current.getSelectPredicate();
			int expCount = currentExpression.size();
			int index = 0;
			ArrayList<String> selectionList = new ArrayList<String>(); 
					CommonMethods.parseExpression(current.getSelectPredicate().get(0), fromClause,true);
			while(index < expCount){
				String nselection = "(" + CommonMethods.parseExpression(current.getSelectPredicate().get(index++), fromClause,true) + ")" ;
				selectionList.add(nselection);
			}
			String selection;	
			index = 0;
			selection = "(" + selectionList.get(index++) + ")";
			while(index < expCount){
				String nsel = "(" + selectionList.get(index++) + ")";
				selection = selection + "&&" + nsel;
			}
		    
			
			try {
			      @SuppressWarnings("unused")
			      Selection foo = new Selection (inAttribute, outAttributes, selection, exprs, infile, outputFile, 
			    		  compiler, outputLocation );	
			      
			      ReturnJoin _outputInfo = new ReturnJoin(currentOutAttribts, outputFile);
			      current.setOutputInfo(_outputInfo);
			      
			      if (nextType == 1)
			    		System.out.println("SUCCESSFUL execution of the SELECT: "+ 
			    					current.getSelectPredicate() + " over the SELECT: "
			    						+ ((RASelectType) current.getNext()).getSelectPredicate() +"  Output in: " + outputFile );
			    	else
			    		System.out.println("SUCCESSFUL execution of the SELECT: "+ 
		    					current.getSelectPredicate() + " over the CROSS Join with underlying table Aliases : "
		    						+ ((RAJoinType) current.getNext()).getUnderlyingTables()  +"  Output in: " + outputFile);
			      
			      File delfile = new File(infile); 
			      
			      if(delfile.delete()){
		    			System.out.println(delfile.getName() + " is deleted!");
		    		}else{
		    			System.out.println("Delete operation is failed.");
		    		}
			      
			      
			      return _outputInfo;			      
			    }
		    
		    
		    catch (Exception e) {
		    	
		    	if (nextType == 1)
		    		System.out.println("Exception in the execution of the SELECT: "+ 
		    					current.getSelectPredicate() + " over the SELECT: "
		    						+ ((RASelectType) current.getNext()).getSelectPredicate()  +"  Output in: " + outputFile);
		    	else
		    		System.out.println("Exception in the execution of the SELECT: "+ 
	    					current.getSelectPredicate() + " over the CROSS Join with underlying table Aliases : "
	    						+ ((RAJoinType) current.getNext()).getUnderlyingTables()  +"  Output in: " + outputFile);
		    	
		    	throw new RuntimeException (e);
		    }		
		}
	
		// CASE 3: Where There is a TABLE under the select predicate
		
		else {
			ArrayList<AttribJoin> joinOutAttribts = new ArrayList<AttribJoin>();
			

			RATableType next = (RATableType) current.getNext();
			String tableName = next.getValue();
			Map<String, AttInfo> tableMap = next.getAttributesInfo();
			ArrayList <Attribute> inAttribute = CommonMethods.getTableAttributeInfo(next.getAlias(),tableName,true);
			ArrayList <Expression> outExp = new ArrayList<Expression>();
			
			String infile = "src/"+tableName +".tbl" ;
			String outputFile= "src/out_"+nameCounter +".tbl";
			String compiler = "g++";
			String outputLocation = "src/cppDir/";
			nameCounter++;
			
			ArrayList<AttInfo> tempInfo = new ArrayList<AttInfo>();
			for (Entry<String, AttInfo> entry : tableMap.entrySet()) {
				  AttInfo value = entry.getValue();
				  tempInfo.add(value);
				}
			int pos = 1 ;
			Collections.sort(tempInfo, new AttInfoComparator());
			ArrayList<ResultValue> outTypes = new ArrayList<ResultValue>();
			for(AttInfo attInformation : tempInfo){				
				String dataType = attInformation.getDataType();
				if(dataType.equals("Int"))
					outTypes.add(new ResultValue(1 , true));
				
				else if(dataType.equals("Str"))
					outTypes.add(new ResultValue(0 ,true));
				
				else
					outTypes.add(new ResultValue(2 , true));	
				
				joinOutAttribts.add(new AttribJoin(attInformation,pos++));
				Expression exp = new Expression("identifier");
				exp.setValue(""+attInformation.getAlias()+"."+attInformation.getAttName());
				outExp.add(exp);				
				
			}
			ArrayList <Attribute> outAttributes = CommonMethods.makeTypeOutAttributes(outTypes);
			HashMap <String, String> exprs = makeSelectExpression(outAttributes, true, outExp, fromClause);			
			//String selection = CommonMethods.parseExpression(current.getSelectPredicate(), fromClause,true);			
		    
			
			ArrayList<Expression> currentExpression = current.getSelectPredicate();
			int expCount = currentExpression.size();
			int index = 0;
			ArrayList<String> selectionList = new ArrayList<String>(); 
					CommonMethods.parseExpression(current.getSelectPredicate().get(0), fromClause,true);
			while(index < expCount){
				String nselection = "(" + CommonMethods.parseExpression(current.getSelectPredicate().get(index++), fromClause,true) + ")" ;
				selectionList.add(nselection);
			}
			String selection;	
			index = 0;
			selection = "(" + selectionList.get(index++) + ")";
			while(index < expCount){
				String nsel = "(" + selectionList.get(index++) + ")";
				selection = selection + "&&" + nsel;
			}
			
			try {
			      @SuppressWarnings("unused")
			      Selection foo = new Selection (inAttribute, outAttributes, selection, exprs, infile, outputFile, 
			    		  compiler, outputLocation );	
			      
			      ReturnJoin _outputInfo = new ReturnJoin(joinOutAttribts, outputFile);
			      current.setOutputInfo(_outputInfo);
			      
			      System.out.println("SUCCESSFUL execution of the Selection:"+ 
	    					current.getSelectPredicate() + " over the table:" + tableName + " Result file :" + outputFile);
			      
			      
			      return _outputInfo;			      
			    }
		    catch (Exception e) {
		    	System.out.println("Exception in the execution of the Selection:"+ 
		    					current.getSelectPredicate() + " over the table:" + tableName);
		    	throw new RuntimeException (e);
		    }		
		}		
 	}
	
	private static ReturnJoin execute(RAJoinType current){
		
		IRAType left = current.getLeft();
		IRAType right = current.getRight();
		
		// CASE 1: Case where the underlying type under the JOIN are simple TABLE - TABLE Type
		if(left.getType().equals("RA_TABLE_TYPE") && right.getType().equals("RA_TABLE_TYPE")){
			RATableType raLTableType = (RATableType)left;
			RATableType raRTableType = (RATableType)right;
			
			ArrayList<AttribJoin> joinOutAttribts = new ArrayList<AttribJoin>();
			Map<String, AttInfo> leftTableAttributes = raLTableType.getAttributesInfo();
			Map<String, AttInfo> rightTableAttributes = raRTableType.getAttributesInfo();
			ArrayList<ResultValue> selTypes = new ArrayList<ResultValue>();
			ArrayList<Expression> selectExprs = new ArrayList<Expression>();
			
			//Code to let output all the attributes from the left side of the table.			
			ArrayList<AttInfo> tempData = new ArrayList<AttInfo>();
			for(String att : leftTableAttributes.keySet()){
				tempData.add(leftTableAttributes.get(att));		
			}
			Collections.sort(tempData, new AttInfoComparator());
			int index = 1;
			for (AttInfo attrib : tempData){
				ResultValue rv = new ResultValue(-1, true);
				if(attrib.getDataType().equals("Int"))
					rv.setType(1);
				else if(attrib.getDataType().equals("Str"))
					rv.setType(0);				
				else
					rv.setType(2);
				
				selTypes.add(rv);
				Expression exp = new Expression("identifier");
				exp.setValue("" + raLTableType.getAlias()+"."+attrib.getAttName());
				selectExprs.add(exp);
				AttribJoin _attribJoin = new AttribJoin(attrib,index++);
				joinOutAttribts.add(_attribJoin);
			}
			
			//Code to let output the all the attributes from the right side of the table.
			tempData.clear();
			for(String att : rightTableAttributes.keySet()){
				tempData.add(rightTableAttributes.get(att));		
			}
			Collections.sort(tempData, new AttInfoComparator());
			for (AttInfo attrib : tempData){
				ResultValue rv = new ResultValue(-1, true);
				if(attrib.getDataType().equals("Int"))
					rv.setType(1);
				else if(attrib.getDataType().equals("Str"))
					rv.setType(0);				
				else
					rv.setType(2);
				
				selTypes.add(rv);
				Expression exp = new Expression("identifier");
				exp.setValue("" + raRTableType.getAlias()+"."+attrib.getAttName());
				selectExprs.add(exp);
				
				AttribJoin _attribJoin = new AttribJoin(attrib,index++);
				joinOutAttribts.add(_attribJoin);
			}						
			 String outputFile = joinExecution(raLTableType.getAlias(), raLTableType.getValue(), raRTableType.getAlias(),
							raRTableType.getValue(),selTypes,selectExprs,fromClause,current.getSelectionPredicate());
			 ReturnJoin _outputInfo = new ReturnJoin( joinOutAttribts,  outputFile);
			 current.setOutputInfo(_outputInfo);
			 

			 System.out.println("SUCCESSFUL execution of the JOIN operation between TABLE: "+ 
					 raLTableType.getValue() + " over the TABLE: "
 						+ raRTableType.getValue() + " Result file :" + outputFile);
			 
			 return _outputInfo;
		}
		
		
		// CASE 2: Case where the underlying type under the JOIN are simple JOIN - TABLE Type
		// CASE 3: Case where the underlying type under the JOIN are simple SELECT - TABLE Type

		else if ((left.getType().equals("RA_JOIN_TYPE") && right.getType().equals("RA_TABLE_TYPE"))
					||
				(left.getType().equals("RA_SELECT_TYPE") && right.getType().equals("RA_TABLE_TYPE"))){
			
			ArrayList<ResultValue> selTypes = new ArrayList<ResultValue>();
			ArrayList<Expression> selectExprs = new ArrayList<Expression>();
			ReturnJoin previousOutput ;
			
			int nextType = 0;
			
			if (left.getType().equals("RA_JOIN_TYPE")){
				previousOutput =  execute((RAJoinType)current.getLeft());
				nextType =1;
			}
			
			else{
				previousOutput = execute ((RASelectType) current.getLeft());
				nextType = 2;
			}
			
			HashSet<String> leftAlias = new HashSet<String>();
			ArrayList<AttribJoin> oldJoinAttribtsLeft = previousOutput.getJoinOutAttribts();
			String prevOutputFile = previousOutput.getOutputFile();
			ArrayList<AttribJoin> joinOutAttribts = new ArrayList<AttribJoin>();		
			Collections.sort(oldJoinAttribtsLeft, new AttribJoinComparator());
			int index = 1;
			Iterator<AttribJoin> oldAttributes = oldJoinAttribtsLeft.iterator();
			while (oldAttributes.hasNext()){
				AttInfo attrib = oldAttributes.next().getAttinfo();
				ResultValue rv = new ResultValue(-1, true);
				if(attrib.getDataType().equals("Int"))
					rv.setType(1);
				else if(attrib.getDataType().equals("Str"))
					rv.setType(0);				
				else
					rv.setType(2);
				
				selTypes.add(rv);
				Expression _exp = new Expression("identifier");
				_exp.setValue("" + attrib.getAlias()+"."+attrib.getAttName());
				leftAlias.add(attrib.getAlias());
				selectExprs.add(_exp);
				AttribJoin _attribJoin = new AttribJoin(attrib,index++);
				joinOutAttribts.add(_attribJoin);
			}
			
			ArrayList<AttInfo> tempData = new ArrayList<AttInfo>();
			RATableType _raRTableType = (RATableType) ((RAJoinType)current).getRight();
			Map<String, AttInfo> _rightTableAttributes = _raRTableType.getAttributesInfo();
			for(String att : _rightTableAttributes.keySet()){
				tempData.add(_rightTableAttributes.get(att));		
			}
			Collections.sort(tempData, new AttInfoComparator());
			for (AttInfo attrib : tempData){
				ResultValue _rv = new ResultValue(-1, true);
				if(attrib.getDataType().equals("Int"))
					_rv.setType(1);
				else if(attrib.getDataType().equals("Str"))
					_rv.setType(0);				
				else
					_rv.setType(2);
				
				selTypes.add(_rv);
				Expression _exp = new Expression("identifier");
				_exp.setValue("" + _raRTableType.getAlias()+"."+attrib.getAttName());
				selectExprs.add(_exp);
				
				AttribJoin _attribJoin = new AttribJoin(attrib,index++);
				joinOutAttribts.add(_attribJoin);
			}
			
			String noutputFile = joinExecution(leftAlias,prevOutputFile,oldJoinAttribtsLeft, _raRTableType.getAlias(),
					_raRTableType.getValue(),selTypes,selectExprs,fromClause,current.getSelectionPredicate());
			
			 if (nextType == 1)
		    		System.out.println("SUCCESSFUL execution of the JOIN Operation over JOIN: "+ 
		    				((RAJoinType)current.getLeft()).getUnderlyingTables() + " and the TABLE: "
		    						+  _raRTableType.getValue());
		    	else
		    		System.out.println("SUCCESSFUL execution of the Join Operation over SELECT: "+ 
		    				((RASelectType) current.getLeft()).getSelectPredicate()  + " AND TABLE: "
	    						+ _raRTableType.getValue());
			 
		      
		      File delfileLeft = new File(prevOutputFile); 
		      
		      if(delfileLeft.delete()){
	    			System.out.println(delfileLeft.getName() + " is deleted!");
	    		}else{
	    			System.out.println( delfileLeft.getName()  + " Delete operation is failed.");
	    		}
						
			return (new ReturnJoin( joinOutAttribts,  noutputFile));
			
		}
		
		// CASE 4: Case where the underlying type under the JOIN are simple SELECT - SELECT Type
		// CASE 5: Case where the underlying type under the JOIN are simple JOIN - SELECT Type
		
		else if((left.getType().equals("RA_SELECT_TYPE") && right.getType().equals("RA_SELECT_TYPE"))
				||
				(left.getType().equals("RA_JOIN_TYPE") && right.getType().equals("RA_SELECT_TYPE"))){
			
			ArrayList<ResultValue> selTypes = new ArrayList<ResultValue>();
			ArrayList<Expression> selectExprs = new ArrayList<Expression>();
			ReturnJoin previousOutputLeft ;
			ReturnJoin previousOutputRight ;
			int nextType = 0;
			
			if (left.getType().equals("RA_JOIN_TYPE")){
				previousOutputLeft =  execute((RAJoinType)current.getLeft());
				nextType = 1;
			}
			else{
				previousOutputLeft = execute ((RASelectType) current.getLeft());
				nextType = 2;
			}
			previousOutputRight = execute ((RASelectType) current.getRight());			
			
			
			//Collecting information from the output of the LEFT branch of the JOIN Statement
			HashSet<String> leftAlias = new HashSet<String>();
			ArrayList<AttribJoin> oldJoinAttribtsLeft = previousOutputLeft.getJoinOutAttribts();
			String outputFileLeft = previousOutputLeft.getOutputFile();
			ArrayList<AttribJoin> joinOutAttribts = new ArrayList<AttribJoin>();		
			Collections.sort(oldJoinAttribtsLeft, new AttribJoinComparator());
			int index = 1;
			Iterator<AttribJoin> oldAttributesLeft = oldJoinAttribtsLeft.iterator();
			while (oldAttributesLeft.hasNext()){
				AttInfo attrib = oldAttributesLeft.next().getAttinfo();
				ResultValue rv = new ResultValue(-1, true);
				if(attrib.getDataType().equals("Int"))
					rv.setType(1);
				else if(attrib.getDataType().equals("Str"))
					rv.setType(0);				
				else
					rv.setType(2);
				
				selTypes.add(rv);
				Expression _exp = new Expression("identifier");
				_exp.setValue("" + attrib.getAlias()+"."+attrib.getAttName());
				leftAlias.add(attrib.getAlias());
				selectExprs.add(_exp);
				AttribJoin _attribJoin = new AttribJoin(attrib,index++);
				joinOutAttribts.add(_attribJoin);
			}
			
			//Collecting information from the output of the RIGHT branch of the JOIN Statement
			HashSet<String> rightAlias = new HashSet<String>();
			ArrayList<AttribJoin> oldJoinAttribtsRight = previousOutputRight.getJoinOutAttribts();
			String outputFileRight = previousOutputRight.getOutputFile();
			Collections.sort(oldJoinAttribtsRight, new AttribJoinComparator());
			Iterator<AttribJoin> oldAttributesRight = oldJoinAttribtsRight.iterator();
			while (oldAttributesRight.hasNext()){
				AttInfo attrib = oldAttributesRight.next().getAttinfo();
				ResultValue rv = new ResultValue(-1, true);
				if(attrib.getDataType().equals("Int"))
					rv.setType(1);
				else if(attrib.getDataType().equals("Str"))
					rv.setType(0);				
				else
					rv.setType(2);
				
				selTypes.add(rv);
				Expression _exp = new Expression("identifier");
				_exp.setValue("" + attrib.getAlias()+"."+attrib.getAttName());
				rightAlias.add(attrib.getAlias());
				selectExprs.add(_exp);
				AttribJoin _attribJoin = new AttribJoin(attrib,index++);
				joinOutAttribts.add(_attribJoin);
			}
			
			String noutputFile = joinExecution(leftAlias, outputFileLeft , oldJoinAttribtsLeft, rightAlias,
					outputFileRight,oldJoinAttribtsRight , selTypes, selectExprs , fromClause,current.getSelectionPredicate());
			
		      if (nextType == 1)
		    		System.out.println("SUCCESSFUL execution of the JOIN Operation over JOIN: "+ 
		    				((RAJoinType)current.getLeft()).getUnderlyingTables() + " and the SELECT: "
		    						+ ((RASelectType) current.getRight()).getSelectPredicate()  +"  Output in: " + noutputFile);
		    	else
		    		System.out.println("SUCCESSFUL execution of the Join Operation over SELECT: "+ 
		    				((RASelectType) current.getLeft()).getSelectPredicate()  + " AND SELECT: "
	    						+ ((RASelectType) current.getRight()).getSelectPredicate()  +"  Output in: " + noutputFile);
			 
		      
		      File delfileLeft = new File(outputFileLeft); 
		      
		      if(delfileLeft.delete()){
	    			System.out.println(delfileLeft.getName() + " is deleted!");
	    		}else{
	    			System.out.println( delfileLeft.getName()  + " Delete operation is failed.");
	    		}
			
		      
		      File delfileRight = new File(outputFileRight); 
		      
		      if(delfileRight.delete()){
	    			System.out.println(delfileRight.getName() + " is deleted!");
	    		}else{
	    			System.out.println( delfileRight.getName()  + " Delete operation is failed.");
	    		}
		      
		      
			return (new ReturnJoin( joinOutAttribts,  noutputFile));
			
		}		
		// CASE 6: Case where the underlying type under the JOIN are TABLE and  SELECT  Type
		else if ((left.getType().equals("RA_TABLE_TYPE") && right.getType().equals("RA_SELECT_TYPE"))){
		
			ArrayList<ResultValue> selTypes = new ArrayList<ResultValue>();
			ArrayList<Expression> selectExprs = new ArrayList<Expression>();
			ReturnJoin previousOutputRight ;
			previousOutputRight = execute ((RASelectType) current.getRight());
			
			ArrayList<AttribJoin> joinOutAttribts = new ArrayList<AttribJoin>();		
			int index = 1;
	
			ArrayList<AttInfo> tempData = new ArrayList<AttInfo>();
			RATableType _raLTableType = (RATableType) ((RAJoinType)current).getLeft();
			Map<String, AttInfo> _leftTableAttributes = _raLTableType.getAttributesInfo();
			for(String att : _leftTableAttributes.keySet()){
				tempData.add(_leftTableAttributes.get(att));		
			}
			Collections.sort(tempData, new AttInfoComparator());
			for (AttInfo attrib : tempData){
				ResultValue _rv = new ResultValue(-1, true);
				if(attrib.getDataType().equals("Int"))
					_rv.setType(1);
				else if(attrib.getDataType().equals("Str"))
					_rv.setType(0);				
				else
					_rv.setType(2);
				
				selTypes.add(_rv);
				Expression _exp = new Expression("identifier");
				_exp.setValue("" + _raLTableType.getAlias()+"."+attrib.getAttName());
				selectExprs.add(_exp);
				
				AttribJoin _attribJoin = new AttribJoin(attrib,index++);
				joinOutAttribts.add(_attribJoin);
			}
			
			HashSet<String> rightAlias = new HashSet<String>();
			ArrayList<AttribJoin> oldJoinAttribtsRight = previousOutputRight.getJoinOutAttribts();
			String prevOutputFileRight = previousOutputRight.getOutputFile();
			Collections.sort(oldJoinAttribtsRight, new AttribJoinComparator());
			Iterator<AttribJoin> oldAttributes = oldJoinAttribtsRight.iterator();
			while (oldAttributes.hasNext()){
				AttInfo attrib = oldAttributes.next().getAttinfo();
				ResultValue rv = new ResultValue(-1, true);
				if(attrib.getDataType().equals("Int"))
					rv.setType(1);
				else if(attrib.getDataType().equals("Str"))
					rv.setType(0);				
				else
					rv.setType(2);
				
				selTypes.add(rv);
				Expression _exp = new Expression("identifier");
				_exp.setValue("" + attrib.getAlias()+"."+attrib.getAttName());
				rightAlias.add(attrib.getAlias());
				selectExprs.add(_exp);
				AttribJoin _attribJoin = new AttribJoin(attrib,index++);
				joinOutAttribts.add(_attribJoin);
			}
					
	
			String noutputFile = joinExecution(_raLTableType.getAlias(), _raLTableType.getValue(),oldJoinAttribtsRight,
					rightAlias,prevOutputFileRight,selTypes,selectExprs,fromClause,current.getSelectionPredicate());
			
			
			/**
			 * (HashSet<String> leftAlias, String outputFile,ArrayList<AttribJoin> 
	oldJoinAttribts, String rightAlias, String rightTableName,
				ArrayList<ResultValue> selTypes, ArrayList<Expression> selectExprs,
				Map<String, String> fromClause) 
			 */
			
			  System.out.println("SUCCESSFUL execution of the Join Operation over TABLE: "+ 
					  _raLTableType.getValue()  + " AND SELECT: "
						+ ((RASelectType) current.getRight()).getSelectPredicate() +" the output is : "+ noutputFile);
			 
		      
		      File delfileLeft = new File(prevOutputFileRight); 
		      
		      if(delfileLeft.delete()){
	    			System.out.println(delfileLeft.getName() + " is deleted!");
	    		}else{
	    			System.out.println( delfileLeft.getName()  + " Delete operation is failed.");
	    		}
					
		return (new ReturnJoin( joinOutAttribts,  noutputFile));
		
	}
		else{
			System.out.println("SERIOUS ERROR" + current.getUnderlyingTables());
			return null;
		}
	}	
		
	/**
	 * @param Alias
	 * @param leftTableName
	 * @param oldJoinAttribtsRight
	 * @param Alias
	 * @param outputFileRight
	 * @param selTypes
	 * @param selectExprs
	 * @param fromClause
	 * @return
	 */
	private static String joinExecution(String leftAlias, String leftTableName,	ArrayList<AttribJoin> oldJoinAttribtsRight,
			HashSet<String> rightAlias, String outputFileRight,	ArrayList<ResultValue> selTypes, ArrayList<Expression> selectExprs,
			Map<String, String> fromClause,ArrayList<Expression> whereClauses) {
		
		
		String noutputFile= "src/out_"+nameCounter+".tbl";
		String compiler = "g++";
		String outputLocation = "src/cppDir/";
		nameCounter ++;		
		
		ArrayList <Attribute> inAttsLeft = getTableAttributeInfo(leftAlias, leftTableName,false);
		ArrayList <Attribute> outAtts =  makeTypeOutAttributes(selTypes);		
		String leftTablePath = "src/"+leftTableName+".tbl";
		String rightTablePath = outputFileRight;		
		
		ArrayList <Attribute> inAttsRight = new ArrayList<Attribute>();
		Collections.sort(oldJoinAttribtsRight,new AttribJoinComparator());
		Iterator<AttribJoin> _rightAttributes = oldJoinAttribtsRight.iterator();
		while(_rightAttributes.hasNext()){
			AttInfo attribute = _rightAttributes.next().getAttinfo();
			inAttsRight.add(new Attribute(attribute.getDataType(),attribute.getAttName()));
		}	

		
		/*
		 * Code to replace the alias
		 * with the left and right "keywords"
		 * in the select expressions.
		 */
		
		HashMap <String, String> tempExprs = makeSelectExpression(outAtts,false,selectExprs,fromClause);
		Iterator<String> exprsIterator = tempExprs.keySet().iterator();
		HashMap <String, String> exprs = new HashMap<String, String>();
		while(exprsIterator.hasNext()){
			String tempExp = exprsIterator.next().toString();
			String selectionPredicates = tempExprs.get(tempExp);
			Iterator<String> aliasIt = rightAlias.iterator();
			
			
			selectionPredicates = replace(selectionPredicates,leftAlias, "left");
			selectionPredicates = replace(selectionPredicates,rightAlias, "right");
			exprs.put(tempExp,selectionPredicates);		
			
			
		}
		
		
		ArrayList <String> leftHash = new ArrayList <String> ();

		
	    ArrayList <String> rightHash = new ArrayList <String> ();

	    
	    String wherePredicate = null;
	    
	    Map<Integer,String> tempLHash = new HashMap<Integer, String>();
	    Map<Integer,String> tempRHash = new HashMap<Integer, String>();
	    String nwherePredicate ;

	    int selCount = 0;
	    if(whereClauses.size()!= 0){
	    	Expression where = whereClauses.get(selCount++);
		    while(where!= null){
	
		    	Expression lExp = where.getLeftSubexpression();
		    	Expression rExp = where.getRightSubexpression();
		    	
		    	String convert_1 = replace(lExp.getValue(),leftAlias,"left");
		    	if(convert_1.indexOf("left")!= -1){
		    		String convert_2 = replace(rExp.getValue(),rightAlias,"right");
		    		nwherePredicate = "" + convert_1 + " == " + convert_2;
		    		if(nwherePredicate.indexOf("left.") != -1){
						String tempString = nwherePredicate.substring(nwherePredicate.indexOf("left."));
						int finalpos = tempString.indexOf(' ');
						if (finalpos == -1)
							tempString = tempString.substring(tempString.indexOf('.')+1);					
						else
							tempString = tempString.substring(tempString.indexOf('.')+1,finalpos);
						
						if(tempLHash.get(selCount-1)==null )
							tempLHash.put(selCount-1, tempString);
					}
		    		if (nwherePredicate.indexOf("right.") != -1){
						String tempString = nwherePredicate.substring(nwherePredicate.indexOf("right."));
						int finalpos = tempString.indexOf(' ');
						if (finalpos == -1)
							tempString = tempString.substring(tempString.indexOf('.')+1);					
						else
							tempString = tempString.substring(tempString.indexOf('.')+1,finalpos);
						
						if(tempRHash.get(selCount-1) == null )
							tempRHash.put(selCount-1, tempString);		
					}	    				
		    	}
		    	
		    	else{
		    		convert_1 = replace(lExp.getValue(),rightAlias,"right");
		    		String convert_2 = replace(rExp.getValue(),leftAlias,"left");
		    		nwherePredicate = "" + convert_1 + " == " + convert_2;
		    		if(nwherePredicate.indexOf("left.") != -1){
						String tempString = nwherePredicate.substring(nwherePredicate.indexOf("left."));
						int finalpos = tempString.indexOf(' ');
						if (finalpos == -1)
							tempString = tempString.substring(tempString.indexOf('.')+1);					
						else
							tempString = tempString.substring(tempString.indexOf('.')+1,finalpos);
						
						if(tempLHash.get(selCount-1)== null )
							tempLHash.put(selCount-1, tempString);
					}
		    		if (nwherePredicate.indexOf("right.") != -1){
						String tempString = nwherePredicate.substring(nwherePredicate.indexOf("right."));
						int finalpos = tempString.indexOf(' ');
						if (finalpos == -1)
							tempString = tempString.substring(tempString.indexOf('.')+1);					
						else
							tempString = tempString.substring(tempString.indexOf('.')+1,finalpos);
						
						if(tempRHash.get(selCount-1) == null )
							tempRHash.put(selCount-1, tempString);			
					}	 
		    	}
		    if (wherePredicate == null )	
		    	wherePredicate = nwherePredicate;
		    
		    else
		    	wherePredicate = wherePredicate + " && " + nwherePredicate;
		    
		    if(selCount<whereClauses.size())
		    	where = whereClauses.get(selCount++);
		    else 
		    	break;
	    	}
		    
		    int count = 0;
		    while(count < selCount){
		    	leftHash.add(tempLHash.get(count));
		    	rightHash.add(tempRHash.get(count));
		    	count++;
		    }
	    }	    
	    
	    if(wherePredicate == null )
	    	wherePredicate = "(Int)1 == (Int) 1";
		 // run the join
	    try {
	    	
	      @SuppressWarnings("unused")
	      Join foo = new Join (inAttsLeft, inAttsRight, outAtts, leftHash, rightHash, wherePredicate, exprs, 
	    		  leftTablePath, rightTablePath, noutputFile, compiler, outputLocation);
	      
	      System.out.println("Computation JOIN Operation over TABLE: "+ 
  				leftTablePath + " and the TABLE: "
  						+  rightTablePath + "and the result in "+ noutputFile );
	      
	      return noutputFile;
	      
	    } 
	    catch (Exception e) {
	      throw new RuntimeException (e);
	    }		
	}

	/**
	 * @param leftAlias
	 * @param outputFileLeft
	 * @param oldJoinAttribtsLeft
	 * @param rightAlias
	 * @param outputFileRight
	 * @param oldJoinAttribtsRight
	 * @param selTypes
	 * @param selectExprs
	 * @param fromClause2
	 * @return
	 */
	private static String joinExecution(HashSet<String> leftAlias, 	String outputFileLeft, ArrayList<AttribJoin> oldJoinAttribtsLeft,
			HashSet<String> rightAlias, String outputFileRight, ArrayList<AttribJoin> oldJoinAttribtsRight, ArrayList<ResultValue> selTypes, 
			ArrayList<Expression> selectExprs, 	Map<String, String> fromClause2,ArrayList<Expression> whereClauses) {
		
		
		String noutputFile= "src/out_"+nameCounter+".tbl";
		String compiler = "g++";
		String outputLocation = "src/cppDir/";
		nameCounter ++;
		
		String leftTablePath = outputFileLeft;
		String rightTablePath = outputFileRight;
		
		//Accessing the information about the LEFT Branch of the JOIN predicate
		ArrayList <Attribute> inAttsLeft = new ArrayList<Attribute>();
		Collections.sort(oldJoinAttribtsLeft,new AttribJoinComparator());
		Iterator<AttribJoin> leftAttributes = oldJoinAttribtsLeft.iterator();
		while(leftAttributes.hasNext()){
			AttInfo attribute = leftAttributes.next().getAttinfo();
			inAttsLeft.add(new Attribute(attribute.getDataType(),attribute.getAttName()));
		}
		
		
		//Accessing the information about the RIGHT Branch of the JOIN predicate
		ArrayList <Attribute> inAttsRight = new ArrayList<Attribute>();
		Collections.sort(oldJoinAttribtsRight,new AttribJoinComparator());
		Iterator<AttribJoin> rightAttributes = oldJoinAttribtsRight.iterator();
		while(rightAttributes.hasNext()){
			AttInfo attribute = rightAttributes.next().getAttinfo();
			inAttsRight.add(new Attribute(attribute.getDataType(),attribute.getAttName()));
		}
		
		ArrayList <Attribute> outAtts =  makeTypeOutAttributes(selTypes);	
		
		/*
		 * Code to replace the alias
		 * with the left and right "keywords"
		 * in the select expressions.
		 */
		HashMap <String, String> tempExprs = makeSelectExpression(outAtts,false,selectExprs,fromClause);
		Iterator<String> exprsIterator = tempExprs.keySet().iterator();
		HashMap <String, String> exprs = new HashMap<String, String>();
		while(exprsIterator.hasNext()){
			String tempExp = exprsIterator.next().toString();
			String selectionPredicates = tempExprs.get(tempExp);
			selectionPredicates = replace(selectionPredicates,leftAlias, "left");
			selectionPredicates = replace(selectionPredicates,rightAlias, "right");
			exprs.put(tempExp,selectionPredicates);	
		}
		ArrayList <String> leftHash = new ArrayList <String> ();
//		leftHash.add ("o_custkey");
		
	    ArrayList <String> rightHash = new ArrayList <String> ();
//	    rightHash.add ("c_custkey");
	    
	    
		String wherePredicate = null;
		
	    Map<Integer,String> tempLHash = new HashMap<Integer, String>();
	    Map<Integer,String> tempRHash = new HashMap<Integer, String>();
	    String nwherePredicate ;
		
	    int selCount = 0;
	    if(whereClauses.size()!= 0){
	    	Expression where = whereClauses.get(selCount++);
			    while(where!= null){
			    	Expression lExp = where.getLeftSubexpression();
			    	Expression rExp = where.getRightSubexpression();
			    	
			    	String convert_1 = replace(lExp.getValue(),leftAlias,"left");
			    	if(convert_1.indexOf("left")!= -1){
			    		String convert_2 = replace(rExp.getValue(),rightAlias,"right");
			    		nwherePredicate = "" + convert_1 + " == " + convert_2;
			    		if(nwherePredicate.indexOf("left.") != -1){
							String tempString = nwherePredicate.substring(nwherePredicate.indexOf("left."));
							int finalpos = tempString.indexOf(' ');
							if (finalpos == -1)
								tempString = tempString.substring(tempString.indexOf('.')+1);					
							else
								tempString = tempString.substring(tempString.indexOf('.')+1,finalpos);
							
							if(tempLHash.get(selCount-1)== null )
								tempLHash.put(selCount-1, tempString);
						}
			    		if (nwherePredicate.indexOf("right.") != -1){
							String tempString = nwherePredicate.substring(nwherePredicate.indexOf("right."));
							int finalpos = tempString.indexOf(' ');
							if (finalpos == -1)
								tempString = tempString.substring(tempString.indexOf('.')+1);					
							else
								tempString = tempString.substring(tempString.indexOf('.')+1,finalpos);
							
							if(tempRHash.get(selCount-1) == null )
								tempRHash.put(selCount-1, tempString);		
						}	    				
			    	}
			    	
			    	else{
			    		convert_1 = replace(lExp.getValue(),rightAlias,"right");
			    		String convert_2 = replace(rExp.getValue(),leftAlias,"left");
			    		nwherePredicate = "" + convert_1 + " == " + convert_2;
			    		if(nwherePredicate.indexOf("left.") != -1){
							String tempString = nwherePredicate.substring(nwherePredicate.indexOf("left."));
							int finalpos = tempString.indexOf(' ');
							if (finalpos == -1)
								tempString = tempString.substring(tempString.indexOf('.')+1);					
							else
								tempString = tempString.substring(tempString.indexOf('.')+1,finalpos);
							
							if(tempLHash.get(selCount-1)==null )
								tempLHash.put(selCount-1, tempString);
						}
			    		if (nwherePredicate.indexOf("right.") != -1){
							String tempString = nwherePredicate.substring(nwherePredicate.indexOf("right."));
							int finalpos = tempString.indexOf(' ');
							if (finalpos == -1)
								tempString = tempString.substring(tempString.indexOf('.')+1);					
							else
								tempString = tempString.substring(tempString.indexOf('.')+1,finalpos);
							
							if(tempRHash.get(selCount-1) == null )
								tempRHash.put(selCount-1, tempString);			
						}
					}		    	 
		    	if (wherePredicate == null)	
			    	wherePredicate = nwherePredicate;
				    
			    else
			    	wherePredicate = wherePredicate + " && " + nwherePredicate;
		    	
			    if(selCount<whereClauses.size())
			    	where = whereClauses.get(selCount++);
			    else 
			    	break;
		    	}
			    
			    int count = 0;
			    while(count < selCount){
			    	leftHash.add(tempLHash.get(count));
			    	rightHash.add(tempRHash.get(count));
			    	count++;
			    }
	    	}
	    
	    if(wherePredicate == null)
	    	wherePredicate = "(Int)1 == (Int) 1";
		 // run the join
	    try {
	    	
	      @SuppressWarnings("unused")
	      Join foo = new Join (inAttsLeft, inAttsRight, outAtts, leftHash, rightHash, wherePredicate, exprs, 
	    		  leftTablePath, rightTablePath, noutputFile, compiler, outputLocation);
	      
	      System.out.println("Computation JOIN Operation over TABLE: "+ 
  				leftTablePath + " and the TABLE: "
  						+  rightTablePath + "and the result in "+ noutputFile );
	      
	      return noutputFile;
	      
	    } 
	    catch (Exception e) {
	      throw new RuntimeException (e);
	    }		
	}

	/**
	 * @param leftAlias 
	 * @param outputFile
	 * @param oldJoinAttribts
	 * @param alias
	 * @param value
	 * @param selTypes
	 * @param selectExprs
	 * @param fromClause
	 * @return
	 */
	private static String joinExecution(HashSet<String> leftAlias, String outputFile,ArrayList<AttribJoin> 
oldJoinAttribts, String rightAlias, String rightTableName,
			ArrayList<ResultValue> selTypes, ArrayList<Expression> selectExprs,
			Map<String, String> fromClause,ArrayList<Expression> whereClauses) {
		
		String noutputFile= "src/out_"+nameCounter+".tbl";
		String compiler = "g++";
		String outputLocation = "src/cppDir/";
		nameCounter ++;
		
		ArrayList <Attribute> inAttsLeft = new ArrayList<Attribute>();
		Collections.sort(oldJoinAttribts,new AttribJoinComparator());
		Iterator<AttribJoin> _leftAttributes = oldJoinAttribts.iterator();
		while(_leftAttributes.hasNext()){
			AttInfo attribute = _leftAttributes.next().getAttinfo();
			inAttsLeft.add(new Attribute(attribute.getDataType(),attribute.getAttName()));
		}
		
		ArrayList <Attribute> inAttsRight = getTableAttributeInfo(rightAlias, rightTableName,false);
		ArrayList <Attribute> outAtts =  makeTypeOutAttributes(selTypes);
		
		String leftTablePath = outputFile;
		String rightTablePath = "src/"+rightTableName+".tbl";
		
		/*
		 * Code to replace the alias
		 * with the left and right "keywords"
		 * in the select expressions.
		 */
		
		HashMap <String, String> tempExprs = makeSelectExpression(outAtts,false,selectExprs,fromClause);
		Iterator<String> exprsIterator = tempExprs.keySet().iterator();
		HashMap <String, String> exprs = new HashMap<String, String>();
		while(exprsIterator.hasNext()){
			String tempExp = exprsIterator.next().toString();
			String selectionPredicates = tempExprs.get(tempExp);
			Iterator<String> aliasIt = leftAlias.iterator();
			
			String rightReplace = rightAlias+"\\.";
			
			selectionPredicates = replace(selectionPredicates,leftAlias,"left");
			selectionPredicates = replace(selectionPredicates,rightAlias,"right");
			exprs.put(tempExp,selectionPredicates);

		}
		ArrayList <String> leftHash = new ArrayList <String> ();
//		leftHash.add ("o_custkey");
		
	    ArrayList <String> rightHash = new ArrayList <String> ();
//	    rightHash.add ("c_custkey");
	    
	    
		String wherePredicate = null;

		
	    Map<Integer,String> tempLHash = new HashMap<Integer, String>();
	    Map<Integer,String> tempRHash = new HashMap<Integer, String>();
	    String nwherePredicate ;

	    int selCount = 0;
	    if(whereClauses.size()!= 0){
	    	Expression where = whereClauses.get(selCount++);
		    while(where!= null){
				Expression lExp = where.getLeftSubexpression();
				Expression rExp = where.getRightSubexpression();
				
				String convert_1 = replace(lExp.getValue(),leftAlias,"left");
		    	if(convert_1.indexOf("left")!= -1){
		    		String convert_2 = replace(rExp.getValue(),rightAlias,"right");
		    		nwherePredicate = "" + convert_1 + " == " + convert_2;
		    		if(nwherePredicate.indexOf("left.") != -1){
						String tempString = nwherePredicate.substring(nwherePredicate.indexOf("left."));
						int finalpos = tempString.indexOf(' ');
						if (finalpos == -1)
							tempString = tempString.substring(tempString.indexOf('.')+1);					
						else
							tempString = tempString.substring(tempString.indexOf('.')+1,finalpos);
						
						if(tempLHash.get(selCount-1)== null )
							tempLHash.put(selCount-1, tempString);
					}
		    		if (nwherePredicate.indexOf("right.") != -1){
						String tempString = nwherePredicate.substring(nwherePredicate.indexOf("right."));
						int finalpos = tempString.indexOf(' ');
						if (finalpos == -1)
							tempString = tempString.substring(tempString.indexOf('.')+1);					
						else
							tempString = tempString.substring(tempString.indexOf('.')+1,finalpos);
						
						if(tempRHash.get(selCount-1) == null )
							tempRHash.put(selCount-1, tempString);	
					}	    				
		    	}
				
		    	else{
		    		convert_1 = replace(lExp.getValue(),rightAlias,"right");
		    		String convert_2 = replace(rExp.getValue(),leftAlias,"left");
		    		nwherePredicate = "" + convert_1 + " == " + convert_2;
		    		if(nwherePredicate.indexOf("left.") != -1){
						String tempString = nwherePredicate.substring(nwherePredicate.indexOf("left."));
						int finalpos = tempString.indexOf(' ');
						if (finalpos == -1)
							tempString = tempString.substring(tempString.indexOf('.')+1);					
						else
							tempString = tempString.substring(tempString.indexOf('.')+1,finalpos);
						
						if(tempLHash.get(selCount-1)==null )
							tempLHash.put(selCount-1, tempString);
					}
		    		if (nwherePredicate.indexOf("right.") != -1){
						String tempString = nwherePredicate.substring(nwherePredicate.indexOf("right."));
						int finalpos = tempString.indexOf(' ');
						if (finalpos == -1)
							tempString = tempString.substring(tempString.indexOf('.')+1);					
						else
							tempString = tempString.substring(tempString.indexOf('.')+1,finalpos);
						
						if(tempRHash.get(selCount-1) == null )
							tempRHash.put(selCount-1, tempString);		
					}
				}
		    
		    	 if (wherePredicate == null )	
				    	wherePredicate = nwherePredicate;
				    
			     else
			    	wherePredicate = wherePredicate + " && " + nwherePredicate;
		    	 
			    if(selCount<whereClauses.size())
			    	where = whereClauses.get(selCount++);
			    else 
			    	break;
		    }
		    int count = 0;
		    while(count < selCount){
		    	leftHash.add(tempLHash.get(count));
		    	rightHash.add(tempRHash.get(count));
		    	count++;
		    }

	    }		
	    
	    if(wherePredicate == null )
	    	wherePredicate = "(Int)1 == (Int) 1";
		 // run the join
	    try {
	    	
	      Join foo = new Join (inAttsLeft, inAttsRight, outAtts, leftHash, rightHash, wherePredicate, exprs, 
	    		  leftTablePath, rightTablePath, noutputFile, compiler, outputLocation);
	      
	      System.out.println("Computation JOIN Operation over TABLE: "+ 
  				leftTablePath + " and the TABLE: "
  						+  rightTablePath + "and the result in "+ noutputFile );
	      
	      return noutputFile;
	      
	    } 
	    catch (Exception e) {
	      throw new RuntimeException (e);
	    }
	}

		/**
	 * 
	 * @param leftAlias
	 * @param leftTableName
	 * @param rightAlias
	 * @param rightTableName
	 * @param fromClause 
	 */
	private static String joinExecution(String leftAlias, String leftTableName,String rightAlias, 
											String rightTableName,ArrayList<ResultValue> selTypes,
											ArrayList<Expression> selectExprs, 
											Map<String, String> fromClause, ArrayList<Expression> whereClauses){
		
		String outputFile= "src/out_"+nameCounter +".tbl";
		String compiler = "g++";
		String outputLocation = "src/cppDir/";
		nameCounter++;
		
		ArrayList <Attribute> inAttsLeft = getTableAttributeInfo(leftAlias, leftTableName,false);
		ArrayList <Attribute> inAttsRight = getTableAttributeInfo(rightAlias, rightTableName,false);
		ArrayList <Attribute> outAtts =  makeTypeOutAttributes(selTypes);
		
		
		String leftTablePath = "src/"+leftTableName+".tbl";
		String rightTablePath = "src/"+rightTableName+".tbl";
		
		/*
		 * Code to replace the alias
		 * with the left and right "keywords"
		 * in the select expressions.
		 */
		HashMap <String, String> tempExprs = makeSelectExpression(outAtts,false,selectExprs,fromClause);
		Iterator<String> exprsIterator = tempExprs.keySet().iterator();
		HashMap <String, String> exprs = new HashMap<String, String>();
		while(exprsIterator.hasNext()){
			String tempExp = exprsIterator.next().toString();
			String selectionPredicates = tempExprs.get(tempExp);
			selectionPredicates = replace(selectionPredicates,leftAlias, "left");
			selectionPredicates = replace(selectionPredicates,rightAlias, "right");
			exprs.put(tempExp,selectionPredicates);	
		}
		
		
		ArrayList <String> leftHash = new ArrayList <String> ();
//		leftHash.add ("o_custkey");
		
	    ArrayList <String> rightHash = new ArrayList <String> ();
//	    rightHash.add ("c_custkey");
	    
	    
		String wherePredicate = null;
	
		Map<Integer,String> tempLHash = new HashMap<Integer, String>();
	    Map<Integer,String> tempRHash = new HashMap<Integer, String>();
	    String nwherePredicate ;

	    int selCount = 0;
	    if(whereClauses.size()!= 0){
	    	Expression where = whereClauses.get(selCount++);
		    while(where!= null){

				Expression lExp = where.getLeftSubexpression();
				Expression rExp = where.getRightSubexpression();
		    	
				String convert_1 = replace(lExp.getValue(),leftAlias,"left");
		    	if(convert_1.indexOf("left")!= -1){
		    		String convert_2 = replace(rExp.getValue(),rightAlias,"right");
		    		nwherePredicate = "" + convert_1 + " == " + convert_2;
		    		if(nwherePredicate.indexOf("left.") != -1){
						String tempString = nwherePredicate.substring(nwherePredicate.indexOf("left."));
						int finalpos = tempString.indexOf(' ');
						if (finalpos == -1)
							tempString = tempString.substring(tempString.indexOf('.')+1);					
						else
							tempString = tempString.substring(tempString.indexOf('.')+1,finalpos);
						
						if(tempLHash.get(selCount-1)==null )
							tempLHash.put(selCount-1, tempString);
					}
		    		if (nwherePredicate.indexOf("right.") != -1){
						String tempString = nwherePredicate.substring(nwherePredicate.indexOf("right."));
						int finalpos = tempString.indexOf(' ');
						if (finalpos == -1)
							tempString = tempString.substring(tempString.indexOf('.')+1);					
						else
							tempString = tempString.substring(tempString.indexOf('.')+1,finalpos);
						
						if(tempRHash.get(selCount-1) == null )
							tempRHash.put(selCount-1, tempString);	
					}	    				
		    	}
		    	
		    	else{
		    		convert_1 = replace(lExp.getValue(),rightAlias,"right");
		    		String convert_2 = replace(rExp.getValue(),leftAlias,"left");
		    		nwherePredicate = "" + convert_1 + " == " + convert_2;
		    		if(nwherePredicate.indexOf("left.") != -1){
						String tempString = nwherePredicate.substring(nwherePredicate.indexOf("left."));
						int finalpos = tempString.indexOf(' ');
						if (finalpos == -1)
							tempString = tempString.substring(tempString.indexOf('.')+1);					
						else
							tempString = tempString.substring(tempString.indexOf('.')+1,finalpos);
						
						if(tempLHash.get(selCount-1)==null )
							tempLHash.put(selCount-1, tempString);
					}
		    		if (nwherePredicate.indexOf("right.") != -1){
						String tempString = nwherePredicate.substring(nwherePredicate.indexOf("right."));
						int finalpos = tempString.indexOf(' ');
						if (finalpos == -1)
							tempString = tempString.substring(tempString.indexOf('.')+1);					
						else
							tempString = tempString.substring(tempString.indexOf('.')+1,finalpos);
						
						if(tempRHash.get(selCount-1) == null )
							tempRHash.put(selCount-1, tempString);	
					}
				}
		    	if (wherePredicate == null )	
			    	wherePredicate = nwherePredicate;
			    
			    else
			    	wherePredicate = wherePredicate + " && " + nwherePredicate;
		    	
			    if(selCount<whereClauses.size())
			    	where = whereClauses.get(selCount++);
			    else 
			    	break;
		    }
		    
		    int count = 0;
		    while(count < selCount){
		    	leftHash.add(tempLHash.get(count));
		    	rightHash.add(tempRHash.get(count));
		    	count++;
		    }
	    }
	    
	    if(wherePredicate == null )
	    	wherePredicate = "(Int)1 == (Int) 1";
		 // run the join
	    try {
	    	
	      @SuppressWarnings("unused")
	      Join foo = new Join (inAttsLeft, inAttsRight, outAtts, leftHash, rightHash, wherePredicate, exprs, 
	    		  leftTablePath, rightTablePath, outputFile, compiler, outputLocation);

	      System.out.println("Computation JOIN Operation over TABLE: "+ 
  				leftTablePath + " and the TABLE: "
  						+  rightTablePath + "and the result in "+ outputFile );
	      
	      return outputFile;
	      
	    } 
	    catch (Exception e) {
	      throw new RuntimeException (e);
	    }
	}
	
	
	/**
	 * 
	 * @param alias
	 * @param tableName
	 * @param replace
	 * @return
	 */
	public static ArrayList <Attribute> getTableAttributeInfo (String alias, String tableName,boolean replace ){
		ArrayList <Attribute> attributes = new ArrayList<Attribute>();
		Map<String, AttInfo> attributesInfo = Interpreter.res.get(tableName).getAttributes();
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
		return attributes;
	}
	
	/**
	 * 
	 * @return
	 */
	public static ArrayList <Attribute> makeTypeOutAttributes(ArrayList<ResultValue> selTypes){
		ArrayList <Attribute> outAttributes = new ArrayList<Attribute>();
		int outCount = 1;
		Iterator<ResultValue> rv = selTypes.iterator();
		while(rv.hasNext()){
			int type = (rv.next()).getType();
			switch(type){
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
					System.out.println("Serious ERROR Unknown Type :" + type );
					System.exit(-1);
			}
			outCount++;
		}		
		return outAttributes;
	}
	
	
	
	/**
	 * 
	 * @param selectExp
	 * @param selExprs 
	 * @return
	 */
	public static  HashMap <String, String> makeSelectExpression (ArrayList<Attribute> selectExp,
								boolean skip, ArrayList<Expression> selectExprs,Map <String, String> fromClause){
		HashMap <String, String> exprs = new HashMap <String, String> ();
		Iterator<Attribute> attributes = selectExp.iterator();
		Iterator<Expression> selExprs = selectExprs.iterator();
		while(attributes.hasNext()){
			String selExpression = CommonMethods.parseExpression(selExprs.next(),fromClause,skip);	
			exprs.put(attributes.next().getName(),selExpression);
		}		
		return exprs;
	}
	
	private static SelectExpression createSelPredicate (Expression expression){
            String type = expression.getType().toString();
            SelectExpression _selectExpression = new SelectExpression();
            if(type.equals("and")){
                    _selectExpression.setExpType(type);
                    _selectExpression.setLeftExpression(createSelPredicate(expression.getLeftSubexpression()));
                    _selectExpression.setRightExpression(createSelPredicate(expression.getRightSubexpression()));
            }
            else{
                    _selectExpression.setExpression(expression);
            }
            return _selectExpression;
    }

    private static void traverseSelExpression (SelectExpression selectExpression){
            if(selectExpression.getExpType()!= null){
                    traverseSelExpression(selectExpression.getLeftExpression());
                    traverseSelExpression(selectExpression.getRightExpression());                        
            }
            else{
                    _selectionPredicates.add(selectExpression.getExpression());
            }
    }
    
    public static void sendSelPredicateDown(RASelectType raSelectType){
    	
    	//IRAType next = raSelectType.getNext();
    	//IRAType previous = raSelectType.getPrevious();
    	contributedTable.clear();
    	Expression selExpression = raSelectType.getSelectPredicate().get(0);
    	sendSelPredicateHelper(selExpression);
    	Iterator<String> tablePresent = contributedTable.iterator();
    	while(tablePresent.hasNext() ){
    		raSelectType.getContributedTable().add(tablePresent.next());
    	}
    	String nextType = raSelectType.getNext().getType();
    	IRAType tempIraType = raSelectType.getNext();
    	while(true){    		
    		if(nextType.equals("RA_JOIN_TYPE") || (nextType.equals("RA_TABLE_TYPE")))
    			break;
    		
    		tempIraType = ((RASelectType)tempIraType).getNext();
    		nextType = tempIraType.getType();
    	}
    	
    	if(nextType.equals("RA_TABLE_TYPE")){    		
    	}    	
    	else{
    		suitableJoin(raSelectType, (RAJoinType) tempIraType);    			
    	}    		
    }
    
    private static void suitableJoin(RASelectType raSelectType, RAJoinType raJoinType){
    		boolean status = true;
    		int right = 1;
    		int left = 1;
    		Iterator<String> tableIterator = raJoinType.getUnderlyingTables().iterator();
    		while(tableIterator.hasNext()){
    			if(!raSelectType.getContributedTable().contains(tableIterator.next())){
    				status = false;
    				if(raJoinType.getLeft().getType().equals("RA_JOIN_TYPE")){
    					Iterator<String> selectTableIterator = raSelectType.getContributedTable().iterator();
    					while(selectTableIterator.hasNext()){
    						if(!(((RAJoinType)raJoinType.getLeft()).getUnderlyingTables().contains(selectTableIterator.next())))
    							left = 0;
    					}
    					right = isSuitableJoin(raSelectType, (RATableType) raJoinType.getRight());
    					
    					if(right == 1){
    						int len1 = raSelectType.getContributedTable().size();
    						if (len1 == 1)
    							raSelectType.setUnderlyingJoin(raJoinType.getRight());
    						
    						else
    							raSelectType.setUnderlyingJoin(raJoinType);    
    						
    						break;
    					}   	
    					
    					else {
    						int len1 = ((RAJoinType)raJoinType.getLeft()).getUnderlyingTables().size();
    						int len2 = raSelectType.getContributedTable().size();
    						if(len1 == len2)
    							raSelectType.setUnderlyingJoin(raJoinType.getLeft());    						
    						else
    							suitableJoin(raSelectType, (RAJoinType) raJoinType.getLeft());    
    						
    						break;
    					}    					    						
    				}
    				else{
    					left = isSuitableJoin(raSelectType, (RATableType) raJoinType.getLeft());
    					right = isSuitableJoin(raSelectType, (RATableType) raJoinType.getRight());
    					
    					if(left == 1)
    						raSelectType.setUnderlyingJoin(raJoinType.getLeft());
    					
    					else
    						raSelectType.setUnderlyingJoin(raJoinType.getRight());
    					
    					break;
    				}
    			}    				
    		}
    		if(status)
    			raSelectType.setUnderlyingJoin(raJoinType);
    }
    
    private static int isSuitableJoin (RASelectType raSelectType, RATableType raTableType){
    	int status = 0;
    	Iterator<String> selTableIterator = raSelectType.getContributedTable().iterator();
    	while(selTableIterator.hasNext()){
    		if(selTableIterator.next().equals(raTableType.getAlias())){
    			status = 1;
    		}
    	}
    	return status;
    }
    
    
    private static void sendSelPredicateHelper(Expression exp){
    	
    	if(exp.getType().equals("or") || isBinaryOperation(exp.getType())){
    		sendSelPredicateHelper(exp.getLeftSubexpression());
    		sendSelPredicateHelper(exp.getRightSubexpression());
    	}
    	else if (isUnaryOperation(exp.getType()))
    		sendSelPredicateHelper(exp.getLeftSubexpression());
    	
    	else{
    		if (exp.getType().equals("identifier")){
    			int index = exp.getValue().indexOf(".");
    			contributedTable.add(exp.getValue().substring(0,index));
    		}
    	}    	
    }
    
    
	private static String  replace (String orig, HashSet<String> alias, String replaceWith){
		String result = orig;
		Iterator<String> aliasIt = alias.iterator();
		
		while(aliasIt.hasNext()){
			String replace = aliasIt.next().toString();
			int pos =  orig.indexOf('.');
			String currentAlias = orig.substring(0,pos);
			String identifier = orig.substring(pos+1);
			if(currentAlias.equals(replace))
				result = ""+replaceWith+"."+identifier;
		}		
		return result;		
	}
	
	private static String  replace (String orig, String replace, String replaceWith){
		String result = orig;
		int pos =  orig.indexOf('.');
		String currentAlias = orig.substring(0,pos);
		String identifier = orig.substring(pos+1);
		if(currentAlias.equals(replace))
			result = ""+replaceWith+"."+identifier;		
		return result;		
	}
}

































