import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class SemanticCheck {
	
	Map <String, String> myFrom;
	ArrayList <Expression> mySelect;
	String att;
	Expression where;
	Map <String, TableData> res;
	
	public SemanticCheck(Map <String, String> myFrom,ArrayList <Expression> mySelect,String att,Expression where){
		this.myFrom = myFrom;
		this.mySelect = mySelect;
		this.att =  att;
		this.where = where;	
		this.res = Interpreter.res;
	}
	
	  private  boolean validateFromClause (Map <String, String> fromClause){	  
		  	Set<String> tableNames = res.keySet();
			Iterator<String> aliases = fromClause.keySet().iterator();
			while(aliases.hasNext()){
				String tableName = fromClause.get(aliases.next().toString());
				if(!(tableNames.contains(tableName))){
					return false;
				}
			}		
			return true;	  
	  }
	  
	  private  boolean validateGroupByClause(String att, Map <String, String> fromClause) {
			String alias = att.substring(0, att.indexOf("."));
			
			String attName = att.substring(att.indexOf(".")+1);
			//System.out.println(att.in);
			String tableName = fromClause.get(alias);
			Map<String, AttInfo> attributesInfo = res.get(tableName).getAttributes();
			if(!(attributesInfo.containsKey(attName)))
				return false;
			
			System.out.println(getAtributeType(att, fromClause));
			return true;
		}
	  
	  private String getAtributeType(String att, Map <String, String> fromClause){
		  	String attributeType;
			String alias = att.substring(0, att.indexOf("."));
			String tableName = fromClause.get(alias);
			String attName = att.substring(att.indexOf(".")+1);
			Map<String, AttInfo> attributesInfo = res.get(tableName).getAttributes();
			
			if (attributesInfo == null)
				return null;
			
			if(!(attributesInfo.containsKey(attName)))
				return null;
			
			else
				attributeType = attributesInfo.get(attName).getDataType();
			
		  return attributeType;
		  
	  }
	  
	  private boolean checkUnaryOperation(String expType) {
		for (String operation : Expression.unaryTypes) {
			if(operation.equals(expType))
				return true;
		}
		return false;
	}
	  
	  private boolean checkBinaryOperation(String expType) {
		for (String operation : Expression.binaryTypes) {
			if(operation.equals(expType))
				return true;
		}
		return false;
	}
	  
	  private ResultValue checkCompatibility(String lExpAttribType,String rExpAttribType, String expType){
		  
		  if((lExpAttribType.equals("Str") && rExpAttribType.equals("Int") ) || (lExpAttribType.equals("Int") && rExpAttribType.equals("Str"))
			  || 	  
			  (lExpAttribType.equals("Str") && rExpAttribType.equals("Float") ) || (lExpAttribType.equals("Float") && rExpAttribType.equals("Str"))
		     ){
			for(String incompatibility : Expression.incompatibleTypes)
				  if(expType.equals(incompatibility))
					  return (new ResultValue(null, false));
		  }	  
		  
		  if((lExpAttribType.equals("Str") && rExpAttribType.equals("Str") ) || (lExpAttribType.equals("Str") && rExpAttribType.equals("Str"))){
			  switch(expType){
			  	case "minus":
			  	case "times":
			  	case "divided by"  : 
			  		return (new ResultValue(null, false));
			  }
		  }	 
		  
		  return (new ResultValue(lExpAttribType, true));
	  }
	  
	  private ResultValue validateTypeExpression(Expression exp, Map <String, String> fromClause){
		  
	/*	  if(exp.getType().equals("and")|| exp.getType().equals("or")){
			  return validateTypeExpression(exp.getLeftSubexpression(),fromClause) && validateTypeExpression(exp.getRightSubexpression(),fromClause);
		  }*/
		  
		  if(exp.getType().equals("and") || exp.getType().equals("or")){
			  ResultValue resValue1 = validateTypeExpression(exp.getLeftSubexpression(),fromClause);
			  ResultValue resValue2 = validateTypeExpression(exp.getRightSubexpression(),fromClause);
			  
			  if(resValue1.isResult() && resValue2.isResult()){
				  if(resValue1.getType().equals(resValue2.getType())|| resValue1.getType().equals("Int")|| resValue1.getType().equals("Float")) {
					  return (new ResultValue(resValue1.getType(), true));
				  }					  
			  }
			  else{
				  return (new ResultValue(null, true));
			  }			  
		  }
		  
		  
		  if(checkUnaryOperation(exp.getType())){			  
			  return validateTypeExpression(exp.getLeftSubexpression(),fromClause);
		  }
		  String expType = exp.getType();
		  String retType;
		  
		  if(checkBinaryOperation(expType)){
			  Expression lExp = exp.getLeftSubexpression();
			  Expression rExp = exp.getRightSubexpression();
			  
			  String lExpType = lExp.getType();
			  String rExpType = rExp.getType();
			  
			  if(checkBinaryOperation(lExpType))
				 return validateTypeExpression(lExp,fromClause);
			  
			  if(checkBinaryOperation(rExpType))
				  return validateTypeExpression(rExp,fromClause);
			  
			  if(lExpType.equals("identifier") && rExpType.equals("identifier")){		  
				  String lExpAttribType = getAtributeType(lExp.getValue(), fromClause);
				  String rExpAttribType = getAtributeType(rExp.getValue(), fromClause);
				  if((lExpAttribType == null) || (rExpAttribType == null))
					  return (new ResultValue(null, false));
				  
				  if(!checkCompatibility(lExpAttribType, rExpAttribType, expType).isResult())
					  return (new ResultValue(null, false));
			  }
			  
			  if((lExpType.equals("literal string") && rExpType.equals("literal int") )
					  	|| 
			     (lExpType.equals("literal int") && rExpType.equals("literal string"))
			     		||
			     (lExpType.equals("literal string") && rExpType.equals("literal float") )
					  	|| 
			     (lExpType.equals("literal float") && rExpType.equals("literal string"))
			     ){
				  	return (new ResultValue(null, false));
			     }
			  
			  if(lExpType.equals("literal string") && rExpType.equals("identifier")){
				  String rExpAttribType = getAtributeType(rExp.getValue(), fromClause);
				  if((rExpAttribType == null))
					  return (new ResultValue(null, false));
				  
				  if(rExpAttribType.equals("Float")|| rExpAttribType.equals("Int")){
					  for(String incompatibility : Expression.incompatibleTypes)
						  if(expType.equals(incompatibility))
							  return (new ResultValue(null, false));
				  }
				  
				  if(rExpAttribType.equals("Str")){
					  switch(expType){
					  //	case "plus":
					  	case "minus":
					  	case "times":
					  	case "divided by"  : 
					  		return (new ResultValue(null, false));
					  }				  
				  }			  
			  }
			  
			  if(rExpType.equals("literal string") && lExpType.equals("identifier")){
				  String lExpAttribType = getAtributeType(lExp.getValue(), fromClause);
				  if((lExpAttribType == null))
					  return (new ResultValue(null, false));
				  
				  if(lExpAttribType.equals("Float")|| lExpAttribType.equals("Int")){
					  for(String incompatibility : Expression.incompatibleTypes)
						  if(expType.equals(incompatibility))
							  return (new ResultValue(null, false));
				  }	
				  if(lExpAttribType.equals("Str")){
					  switch(expType){
					  //	case "plus":
					  	case "minus":
					  	case "times":
					  	case "divided by"  : 
					  		return (new ResultValue(null, false));
					  }				  
				  }	
			  }
			  
			  if(lExpType.equals("literal int") && rExpType.equals("identifier")
				||
				(lExpType.equals("literal float") && rExpType.equals("identifier"))){
				  
				  String rExpAttribType = getAtributeType(rExp.getValue(), fromClause);
				  if((rExpAttribType == null))
					  return (new ResultValue(null, false));
				  
				  if(rExpAttribType.equals("Str"))
					  return (new ResultValue(null, false));
			  }
				
			   if(lExpType.equals("identifier") && rExpType.equals("literal int")
					   ||
			    (lExpType.equals("identifier") && rExpType.equals("literal float"))){
				
				  String lExpAttribType = getAtributeType(lExp.getValue(), fromClause);
				  if((lExpAttribType == null))
					  return (new ResultValue(null, false));
				  
				  if(lExpAttribType.equals("Str"))
					  return (new ResultValue(null, false));
				  
			  }		  
		  }	  
		  
		  //TODO: Fix the return type of the
		  // string and float types.
		  
		  if(exp.getType().equals("identifier"))
			  retType = getAtributeType(exp.getValue(), fromClause);
		  
		  
		  return (new ResultValue(exp.getType(), true));
	  }
	  
	  public boolean validateQuery(){		  
	        
	        //Validating the from clause of the query
	        if(!validateFromClause(myFrom)){
	        	System.out.println("\n\n\nSemantically incorrect query: Referenced table in from clause do not present in database");
	        	return false;
	        }
	        
	        //Validating the group by clause of the query
	        if((att != null) && !validateGroupByClause(att,myFrom)){
	        	System.out.println("\n\n\nSemantically incorrect query: Referenced attribute in the group by clause do not present in database");
	        	return false;
	        }
	        
	        //Validating the Type mismatches in the WHERE Expression
	        if(!(validateTypeExpression(where,myFrom).isResult())){
	        	System.out.println("Invalid Expression in WHERE  :" + where.print());
	        	return false;
	        }
	      
	        
	        //Validating the Type mismatch in the SELECT Expression
	        for (Expression selectExp : mySelect){
	        	if(!(validateTypeExpression(selectExp,myFrom).isResult())){
	        		System.out.println("Invalid Expression in SELECT  :" + selectExp.print());
	        		return false;
	        	}
	        }      
	        
	        return true;
	  }	  
}
