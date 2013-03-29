/**
 * 
 */
import java.util.Map;

/**
 * @author apoorvagarwal
 *
 */
public class CommonMethods {

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
	
	public static boolean isUnaryOperation(String expType) {
		for (String operation : Expression.unaryTypes) {
			if(operation.equals(expType))
				return true;
		}
		return false;
	}
	  
	public static boolean isBinaryOperation(String expType) {
		for (String operation : Expression.binaryTypes) {
			if(operation.equals(expType))
				return true;
		}
		return false;
	}
	
	public static String parseExpression (Expression exp, Map <String, String> fromClause){
		
		if(exp.getType().equals("and") || exp.getType().equals("or")){
			  String resValue1 = parseExpression(exp.getLeftSubexpression(),fromClause);
			  String resValue2 = parseExpression(exp.getRightSubexpression(),fromClause);
			  
			  if(exp.getType().equals("and"))
					  return (resValue1 + " && " + resValue2);						  			
			  else{
				  return (resValue1 + " || " + resValue2);
			  }				  
		  }
		  
		  
		  if(CommonMethods.isUnaryOperation(exp.getType())){	
			  String rv = parseExpression(exp.getLeftSubexpression(),fromClause);
			  
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
			  String resValue1 = parseExpression(exp.getLeftSubexpression(),fromClause);
			  String resValue2 = parseExpression(exp.getRightSubexpression(),fromClause);
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
					  return (resValue1 + " = " + resValue2);
				  
				  if(exp.getType().equals("greater than"))
					  return (resValue1 + " > " + resValue2);
				  
				  if(exp.getType().equals("less than"))
					  return (resValue1 + " < " + resValue2);				  
			  }
		  }	  
		  
		  if(exp.getType().equals("identifier")){
			 return exp.getValue().replace('.', '_');
		  }
		  
		  /*
		   * Managing the constants in the expressions
		   */
		  String retString = "";
		  if(exp.getType().equals("literal string"))
			  retString = "Str (\"" + exp.getValue() + "\"";
		  		  
		  else if (exp.getType().equals("literal int"))
			  retString = "Int (" + exp.getValue() +")";
		  
		  else
			  retString = "Float (" + exp.getValue() +")";
			  
	      return retString;		
	}
	  
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

}