/**
 * 
 */
import java.util.Map;

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
	 */
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
	
	/**
	 * Deprecated for the Time being
	 * @param raExpression
	 */
	public static void traverseRAExpression (RAExpression raExpression){
		if(raExpression.getExpType()!= null){
			traverseRAExpression(raExpression.getLeftExpression());
			traverseRAExpression(raExpression.getRightExpression());			
		}
		else
			System.out.println(raExpression.getExpression().print());
	}
}