/**
 * 
 */
import java.awt.geom.CubicCurve2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

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
	
	//This needs to be initialized during the call of the class
	static Map <String, String> fromClause;
	
	static{
		nameCounter = 1;
		_selectionPredicates = new ArrayList<Expression>();
		contributedTable = new HashSet<String>();				
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
										Expression whereClause){
		
		// First creating the leaf nodes which is basically the
		// tables present in the from clause of the query.		
		Map<Integer, RATableType> _tablePresent = new HashMap<Integer, RATableType>();
		Map<Integer, RAJoinType> _crossJoinPresent = new HashMap<Integer, RAJoinType>();	
		Map<Integer, RASelectType> _selectPredicatePresent = new HashMap<Integer, RASelectType>();

		Iterator<String> aliases = fromClause.keySet().iterator();
		int counter = 1;
		while(aliases.hasNext()){
			String alias = aliases.next().toString();
			String tableName = fromClause.get(alias);
			_tablePresent.put(counter,new RATableType(tableName,alias,true, counter++));
		}		

		//Creating all the join in the query 
		// starting with the basic cross joins in the query
		counter = 1;		
		int countTable = _tablePresent.size();
		if(countTable == 1){
			
		}		
		else{
			int current = 1;
			RAJoinType _raJoin = new RAJoinType();
			RATableType _raLeftTable = _tablePresent.get(current);
			RATableType _raRightTable = _tablePresent.get(++current);
			_raJoin.setBranch(_raLeftTable,_raRightTable);
			_raLeftTable.setPrevious(_raJoin);
			_raRightTable.setPrevious(_raJoin);
			_crossJoinPresent.put(counter++,_raJoin);
			
			while(current < countTable){ 
				_raRightTable = _tablePresent.get(++current);
				RAJoinType _raTempJoin = new RAJoinType();
				RAJoinType _insertedRAJoin = _crossJoinPresent.get((counter-1));
				_raTempJoin.setBranch(_insertedRAJoin,_raRightTable);
				_raRightTable.setPrevious(_raTempJoin);
				_insertedRAJoin.setPrevious(_raTempJoin);
				_crossJoinPresent.put(counter++,_raTempJoin);				
			}			
		}
		
		
		// Creating part of the sub tree where nodes would be holding the 
		// data of the selection predicates.
		RAJoinType _raJoinTop = _crossJoinPresent.get(counter-1); 
		int current = 1;
		if(whereClause != null){			
			traverseSelExpression(createSelPredicate(whereClause));
			for(Expression exp : _selectionPredicates){
				RASelectType _raSelectTemp = new RASelectType(exp);
				_selectPredicatePresent.put(current++,_raSelectTemp);
			}
			current = 1;
			RASelectType _raSelect = _selectPredicatePresent.get(current++);
			_raSelect.setNext(_raJoinTop);
			_raJoinTop.setPrevious(_raSelect);			
			while(current <= _selectPredicatePresent.size()){
				_selectPredicatePresent.get(current-1).setPrevious(_selectPredicatePresent.get(current));
				_selectPredicatePresent.get(current).setNext(_selectPredicatePresent.get(current-1));
				current++;
			}
		}	
		
		RAProjectType _raProjectType = new RAProjectType(_selectPredicatePresent.get(current-1));
		_selectPredicatePresent.get(current-1).setPrevious(_raProjectType);
		_raProjectType.setSelectExprs(selectClause);
		
		int index = 1;
		while(index <= _selectPredicatePresent.size())
			sendSelPredicateDown(_selectPredicatePresent.get(index++));
		
		index = 1 ;
		while(index <= _selectPredicatePresent.size())
			createNewConnection(_selectPredicatePresent.get(index++));
		
		return _raProjectType;
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
		
		String type = _root.getType();
		
		RASelectType raSelectType = null;
		RAProjectType raProjectType = null;
		
		
		if(type.equals("RA_SELECT_TYPE"))
			raSelectType = (RASelectType) _root;
		
		else //if (type.equals("RA_PROJECT_TYPE"))
			raProjectType = (RAProjectType) _root;
		
		
	}
	
	
	private static ReturnJoin execute(RASelectType current){
		ReturnJoin nextOutput = null;
		
		if(current.getNext().getType().equals("RA_SELECT_TYPE")){
			ArrayList<AttribJoin> joinOutAttribts = new ArrayList<AttribJoin>();

			String outputFile= "src/out_"+nameCounter +".tbl";
			String compiler = "g++";
			String outputLocation = "src/cppDir/";
			nameCounter++;
			
			nextOutput = execute((RASelectType) current.getNext());
			ReturnJoin _outputInfo = new ReturnJoin(joinOutAttribts, outputFile);
			current.set_outputInfo(_outputInfo);
			return _outputInfo;
			 
		
		}
		else if (current.getNext().getType().equals("RA_JOIN_TYPE")){
			ArrayList<AttribJoin> joinOutAttribts = new ArrayList<AttribJoin>();
			
			String outputFile= "src/out_"+nameCounter +".tbl";
			String compiler = "g++";
			String outputLocation = "src/cppDir/";
			nameCounter++;
			
			nextOutput = execute((RAJoinType) current.getNext());
			ReturnJoin _outputInfo = new ReturnJoin(joinOutAttribts, outputFile);
			current.set_outputInfo(_outputInfo);
			return _outputInfo;
		}
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
			String selection = CommonMethods.parseExpression(current.getSelectPredicate(), fromClause,true);			
		    try {
			      @SuppressWarnings("unused")
			      Selection foo = new Selection (inAttribute, outAttributes, selection, exprs, infile, outputFile, 
			    		  compiler, outputLocation );	
			      
			      ReturnJoin _outputInfo = new ReturnJoin(joinOutAttribts, outputFile);
			      current.set_outputInfo(_outputInfo);
			      return _outputInfo;			      
			    }
		    catch (Exception e) {
		    	System.out.println("Exception in the exeution of the ");
		    	throw new RuntimeException (e);
		    }		
		   }		
 	}
	
	private static ReturnJoin execute(RAJoinType current){
		
		return null;
	}
	
	
	
	
	/*
	
	public static void executeRATree (Map <String, String> fromClause, ArrayList <Expression> selectClause,
							Expression whereClause){
		ReturnJoin finalJoin  = null;
		RAProjectType _raProjectType = (RAProjectType)createRATree(fromClause, selectClause, whereClause); 
		RASelectType _raSelectType = _raProjectType.get_raSelect();
		RAJoinType _raJoinType = (RAJoinType) _raSelectType.get_raJoin();
		if(_raJoinType.getType().equals("RA_JOIN_TYPE")){
			finalJoin = raJoinHelper(_raJoinType,fromClause);
			System.out.println("Total Joins: "+finalJoin.getOutputFile());
		}	
		raExecuteSelection(finalJoin,fromClause,_raProjectType,_raSelectType);
	}

	private static void raExecuteSelection (ReturnJoin finalJoin, Map<String, String> fromClause, RAProjectType _raRaProjectType, RASelectType _raSelectType){
		String outputFile= "src/out_"+nameCounter +".tbl";
		String compiler = "g++";
		String outputLocation = "src/cppDir/";
		nameCounter++;
		
		ArrayList <Attribute> tableAttribute = new ArrayList<Attribute>();
		ArrayList<AttribJoin> totalJoins = finalJoin.getJoinOutAttribts();
		Collections.sort(totalJoins,new AttribJoinComparator());
		Iterator<AttribJoin> totalJoinsIt = totalJoins.iterator();
		while (totalJoinsIt.hasNext()){
			AttInfo attrib = totalJoinsIt.next().get_attinfo();
			tableAttribute.add(new Attribute(attrib.getDataType(),""+attrib.getAlias()+"_"+attrib.getAttName()));
		}
		ArrayList<ResultValue> selResultValue = new ArrayList<ResultValue>();
		for(Expression exp : _raRaProjectType.getSelectExprs()){
			selResultValue.add(validateTypeExpression(exp, fromClause));
		}
		ArrayList <Attribute> selectExpTypes = CommonMethods.makeTypeOutAttributes(selResultValue);
		HashMap <String, String> exprs = makeSelectExpression(selectExpTypes, true, _raRaProjectType.getSelectExprs(), fromClause);
		String selection = parseExpression(_raSelectType.getSelectPredicate(), fromClause,true);
		String tableUsed = finalJoin.getOutputFile();
	    try {
		      @SuppressWarnings("unused")
		      Selection foo = new Selection (tableAttribute, selectExpTypes, selection, exprs, tableUsed, 
		    		  								outputFile, compiler, outputLocation );
		      System.out.println("Final output: "+outputFile);
		      nameCounter = 0;
		    } 
	   catch (Exception e) {
		      throw new RuntimeException (e);
       }
	}
	
	private static ReturnJoin raJoinHelper(RAJoinType _raJoinType, Map<String, String> fromClause){
		
		ArrayList<ResultValue> selTypes = new ArrayList<ResultValue>();
		ArrayList<Expression> selectExprs = new ArrayList<Expression>();
		
		//Case 1 : When the join is between cross product of the tables and the underlying table
		if(_raJoinType.get_raJoin()!= null){
			ReturnJoin _returnJoin =  raJoinHelper(_raJoinType.get_raJoin(),fromClause);
			HashSet<String> leftAlias = new HashSet<String>();
			ArrayList<AttribJoin> oldJoinAttribts = _returnJoin.getJoinOutAttribts();
			String outputFile = _returnJoin.getOutputFile();
			ArrayList<AttribJoin> joinOutAttribts = new ArrayList<AttribJoin>();		
			Collections.sort(oldJoinAttribts, new AttribJoinComparator());
			int index = 1;
			Iterator<AttribJoin> oldAttributes = oldJoinAttribts.iterator();
			while (oldAttributes.hasNext()){
				AttInfo attrib = oldAttributes.next().get_attinfo();
				ResultValue _rv = new ResultValue(-1, true);
				if(attrib.getDataType().equals("Int"))
					_rv.setType(1);
				else if(attrib.getDataType().equals("Str"))
					_rv.setType(0);				
				else
					_rv.setType(2);
				
				selTypes.add(_rv);
				Expression _exp = new Expression("identifier");
				_exp.setValue("" + attrib.getAlias()+"."+attrib.getAttName());
				leftAlias.add(attrib.getAlias());
				selectExprs.add(_exp);
				AttribJoin _attribJoin = new AttribJoin(attrib,index++);
				joinOutAttribts.add(_attribJoin);
			}
			
			ArrayList<AttInfo> tempData = new ArrayList<AttInfo>();
			RATableType _raRTableType = _raJoinType.get_rightTable();
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
			
			String noutputFile = joinExecution(leftAlias,outputFile,oldJoinAttribts, _raRTableType.getAlias(),
					_raRTableType.getValue(),selTypes,selectExprs,fromClause);
			
			return (new ReturnJoin( joinOutAttribts,  noutputFile));
		}
		//Case 2:  Where the underlying code is all table attributes and doing simple cross join			
		if((_raJoinType.get_leftTable()!= null) && (_raJoinType.get_rightTable() != null) ){
			RATableType _raLTableType = _raJoinType.get_leftTable();
			RATableType _raRTableType = _raJoinType.get_rightTable();
			
			ArrayList<AttribJoin> joinOutAttribts = new ArrayList<AttribJoin>();
			Map<String, AttInfo> _leftTableAttributes = _raLTableType.getAttributesInfo();
			Map<String, AttInfo> _rightTableAttributes = _raRTableType.getAttributesInfo();
			
			
			//Code to let output the all the attributes from the left side of the table.			
			ArrayList<AttInfo> tempData = new ArrayList<AttInfo>();
			for(String att : _leftTableAttributes.keySet()){
				tempData.add(_leftTableAttributes.get(att));		
			}
			Collections.sort(tempData, new AttInfoComparator());
			int index = 1;
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
			
			//Code to let output the all the attributes from the right side of the table.
			tempData.clear();
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
			 String outputFile = joinExecution(_raLTableType.getAlias(), _raLTableType.getValue(), _raRTableType.getAlias(),
							_raRTableType.getValue(),selTypes,selectExprs,fromClause);
			 
			 return (new ReturnJoin( joinOutAttribts,  outputFile));
		}
		return null;		
	}
	
	*/
	
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
			Map<String, String> fromClause) {
		
		String noutputFile= "src/out_"+nameCounter+".tbl";
		String compiler = "g++";
		String outputLocation = "src/cppDir/";
		nameCounter ++;
		
		ArrayList <Attribute> inAttsLeft = new ArrayList<Attribute>();
		Collections.sort(oldJoinAttribts,new AttribJoinComparator());
		Iterator<AttribJoin> _leftAttributes = oldJoinAttribts.iterator();
		while(_leftAttributes.hasNext()){
			AttInfo attribute = _leftAttributes.next().get_attinfo();
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
			while(aliasIt.hasNext()){
				String tempAlias = aliasIt.next().toString();
				selectionPredicates = selectionPredicates.replaceAll(tempAlias, "left");
				//leftAlias.iterator().remove();
			}
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
	    		  leftTablePath, rightTablePath, noutputFile, compiler, outputLocation);
	      System.out.println(foo);
	      return noutputFile;
	      
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
			switch((rv.next()).getType()){
				case 0:
					outAttributes.add(new Attribute("Str", "att"+outCount));
					break;
					
					//sendSelPredicateDown(_selectPredicatePresent.get(2));
					//System.out.println(_selectPredicatePresent.get(2).getContributedTable());
					
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
	 * @param leftAlias
	 * @param leftTableName
	 * @param rightAlias
	 * @param rightTableName
	 * @param fromClause 
	 */
	public static String joinExecution(String leftAlias, String leftTableName,String rightAlias, 
											String rightTableName,ArrayList<ResultValue> selTypes,ArrayList<Expression> selectExprs, Map<String, String> fromClause){
		
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
	      System.out.println(foo);
	      return outputFile;
	      
	    } 
	    catch (Exception e) {
	      throw new RuntimeException (e);
	    }
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
                    System.out.println(selectExpression.getExpression().print());
                    _selectionPredicates.add(selectExpression.getExpression());
            }
    }
    
    public static void sendSelPredicateDown(RASelectType raSelectType){
    	
    	//IRAType next = raSelectType.getNext();
    	//IRAType previous = raSelectType.getPrevious();
    	contributedTable.clear();
    	Expression selExpression = raSelectType.getSelectPredicate();
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
}

































