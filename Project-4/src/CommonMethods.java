/**
 * 
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author apoorvagarwal
 * This class holds the collection of all the 
 * methods collectively used by the project
 * across the classes.
 */
public class CommonMethods {

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
	
	/**
	 * @@Deprecated for the Time being
	 * @param expression
	 * @return
	 *//*
	public static RAExpression createRAExpression (Expression expression){
		String type = expression.getType().toString();
		RAExpression _raExpression = new RAExpression();
		if(type.equals("and")||type.equals("or")){
			_raExpression.setExpType(type);
			_raExpression.setLeftExpression(createRAExpression(expression.getLeftSubexpression()));
			_raExpression.setRightExpression(createRAExpression(expression.getRightSubexpression()));
		}
		else{
			_raExpression.setExpression(expression);
		}
		return _raExpression;
	}
	*/
	/**
	 * Deprecated for the Time being
	 * @param raExpression
	 *//*
	public static void traverseRAExpression (RAExpression raExpression){
		if(raExpression.getExpType()!= null){
			traverseRAExpression(raExpression.getLeftExpression());
			traverseRAExpression(raExpression.getRightExpression());			
		}
		else
			System.out.println(raExpression.getExpression().print());
	}*/
	
	
	public static IRAType createRATree (Map <String, String> fromClause, ArrayList <Expression> selectClause,
										Expression whereClause){
		
		// First creating the leaf nodes which is basically the
		// tables present in the from clause of the query.		
		Map<Integer, RATableType> _tablePresent = new HashMap<Integer, RATableType>();
		Iterator<String> aliases = fromClause.keySet().iterator();
		int counter = 1;
		while(aliases.hasNext()){
			String alias = aliases.next().toString();
			String tableName = fromClause.get(alias);
			RATableType _raTable = new RATableType(tableName,alias,true, counter++);
			_tablePresent.put((counter-1),_raTable);
		}		
		
		//Creating all the join in the query 
		// starting with the basic cross joins in the query
		counter = 1;		
		Map<Integer, RAJoinType> _crossJoinPresent = new HashMap<Integer, RAJoinType>();		
		int countTable = _tablePresent.size();
		if(countTable == 1){
			
		}		
		else{
			int current = 1;
			RAJoinType _raJoin = new RAJoinType();
			RATableType _raLeftTable = _tablePresent.get(current);
			RATableType _raRightTable = _tablePresent.get(++current);
			_raJoin.setTables(_raLeftTable,_raRightTable);
			_crossJoinPresent.put(counter++,_raJoin);
			
			while(current < countTable){ 
				_raRightTable = _tablePresent.get(++current);
				RAJoinType _raTempJoin = new RAJoinType();
				RAJoinType _insertedRAJoin = _crossJoinPresent.get((counter-1));
				_raTempJoin.setTables(_raRightTable, _insertedRAJoin);
				_crossJoinPresent.put(counter++,_raTempJoin);				
			}			
		}
		
		// Creating part of the sub tree where nodes would be holding the 
		// data of the selection predicates.
		RAJoinType _raJoinTop = _crossJoinPresent.get(counter-1); 
		RASelectType _raSelect = new RASelectType(_raJoinTop);
		if(whereClause != null)
			_raSelect.setSelectPredicate(whereClause);
		
		RAProjectType _raProjectType = new RAProjectType(_raSelect);
		_raProjectType.setSelectExprs(selectClause);
		return _raProjectType;
	}
	
	
	public static void executeRATree (Map <String, String> fromClause, ArrayList <Expression> selectClause,
							Expression whereClause,ArrayList<ResultValue> selTypes){
		RAProjectType _raRaProjectType = (RAProjectType)createRATree(fromClause, selectClause, whereClause); 
		RASelectType _raSelectType = _raRaProjectType.get_raSelect();
		RAJoinType _raJoinType = _raSelectType.get_raJoin();
		if(_raJoinType.getType().equals("RA_JOIN_TYPE")){
			raJoinHelper(_raJoinType);
		}	
	}

	private static String raJoinHelper(RAJoinType _raJoinType){
		String outputFile = null;
		if(_raJoinType.get_raJoin()!= null)
			outputFile = raJoinHelper(_raJoinType.get_raJoin());
		
		//Case 2:  Where the underlying code is all table attributes 
		//		   and doing simple cross join	
		
		if((_raJoinType.get_leftTable()!= null) && (_raJoinType.get_rightTable() != null) ){
			RATableType _raLTableType = _raJoinType.get_leftTable();
			RATableType _raRTableType = _raJoinType.get_rightTable();
			
			//TODO : To fix the expression and the output to the file.
			ArrayList<ResultValue> selTypes = new ArrayList<ResultValue>();
			ArrayList<Expression> selectExprs = new ArrayList<Expression>();
			ArrayList<AttribJoin> joinOutAttribts = new ArrayList<AttribJoin>();
			
			Map<String, AttInfo> _leftTableAttributes = _raLTableType.getAttributesInfo();
			Map<String, AttInfo> _rightTableAttributes = _raRTableType.getAttributesInfo();
			
			
			//Code to let output the all the attributes from the 
			// left side of the table.
			
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
			
			//Code to let output the all the attributes from the 
			// right side of the table.
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
						
			 outputFile = doJoinHelper(_raLTableType.getAlias(), _raLTableType.getValue(), _raRTableType.getAlias(),
							_raRTableType.getValue(),selTypes,selectExprs);			
		}
		
		return outputFile;
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
	public static ArrayList <Attribute> makeTypeOutAttributes(ArrayList<ResultValue> selTypes){
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
	 * @param leftAlias
	 * @param leftTableName
	 * @param rightAlias
	 * @param rightTableName
	 */
	public static String doJoinHelper(String leftAlias, String leftTableName,String rightAlias, 
											String rightTableName,ArrayList<ResultValue> selTypes,ArrayList<Expression> selectExprs){
		
		String outputFile= "src/out.tbl";
		String compiler = "g++";
		String outputLocation = "src/cppDir/";
		
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
		HashMap <String, String> tempExprs = makeSelectExpression(outAtts,false,selectExprs);
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
	public static  HashMap <String, String> makeSelectExpression (ArrayList<Attribute> selectExp,boolean skip, ArrayList<Expression> selectExprs){
		HashMap <String, String> exprs = new HashMap <String, String> ();
		Iterator<Attribute> attributes = selectExp.iterator();
		Iterator<Expression> selExprs = selectExprs.iterator();
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
	
}

































