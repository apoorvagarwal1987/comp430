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
	
	  private  boolean isValidFromClause (Map <String, String> fromClause){	  
		  	Set<String> tableNames = res.keySet();
			Iterator<String> aliases = fromClause.keySet().iterator();
			while(aliases.hasNext()){
				String tableName = fromClause.get(aliases.next().toString());
				if(!(tableNames.contains(tableName))){
					System.out.println("Error: Table "+ tableName +" do not exist in the CATALOGUE");
					return false;
				}
			}		
			return true;	  
	  }
	  
	  private  boolean isValidGroupByClause(String att, Map <String, String> fromClause, ArrayList<Expression> mySelect) {
			String alias = att.substring(0, att.indexOf("."));
			
			String attName = att.substring(att.indexOf(".")+1);
			String tableName = fromClause.get(alias);
			if(tableName == null){
				System.out.println("Error: Alias "+ alias +" do not correspond to the any table in the FROM clause");
				return false;
			}
			Map<String, AttInfo> attributesInfo = res.get(tableName).getAttributes();
			if (attributesInfo == null){
				System.out.println("Error: Table "+ tableName +" do not exist in the CATALOGUE");
				return false;
			}
			
			if(!(attributesInfo.containsKey(attName))){
				System.out.println("Error: Attribute "+ attName +" attribute absent in the table" + tableName +" database used in GroupBy");
				return false;
			}
			for(Expression exp : mySelect){
				if(isBinaryOperation(exp.getType())){
					System.out.println("Error: Expression "+ exp.print() +" expression is not allowed in the select clause when GroupBy");
					return false;
				}
				else if(isUnaryOperation(exp.getType())){
				}				
				else if (!(exp.getType().equals("identifier")&& exp.getValue().equals(att))){
					System.out.println("Error: Expression "+ exp.print() +" expression is not allowed in the select clause when GroupBy");
					return false;	
				}
				else{}
			}
			
			return true;
		}
	  
	  private String getAtributeType(String att, Map <String, String> fromClause){
		  	String attributeType;
			String alias = att.substring(0, att.indexOf("."));
			String tableName = fromClause.get(alias);
			if(tableName == null){
				System.out.println("Error: Alias "+ alias +" do not correspond to the any table in the FROM clause");
				return null;
			}
			String attName = att.substring(att.indexOf(".")+1);
			Map<String, AttInfo> attributesInfo = res.get(tableName).getAttributes();
			
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
	  
	  private boolean isUnaryOperation(String expType) {
		for (String operation : Expression.unaryTypes) {
			if(operation.equals(expType))
				return true;
		}
		return false;
	}
	  
	  private boolean isBinaryOperation(String expType) {
		for (String operation : Expression.binaryTypes) {
			if(operation.equals(expType))
				return true;
		}
		return false;
	}
	  
	  
	  private ResultValue checkCompatibility(ResultValue _resValue1,ResultValue _resValue2,String _type){
		
		  if(_resValue1.isResult()&& _resValue2.isResult()){			  
			  if(_resValue1.getType()==1)
				  return (new IntegerCompatibility().compatibility(_resValue1, _resValue2, _type));			 
			  else
				  return (new StringCompatibility().compatibility(_resValue1, _resValue2, _type));
		  }
		  else
			  return (new ResultValue(-1, false));		  
	  }
	  
	  private ResultValue validateTypeExpression(Expression exp, Map <String, String> fromClause){
		  
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
			  else
				  return (new ResultValue(1, true));
		  }
		  
		  if(exp.getType().equals("literal string"))
			   return (new ResultValue(0, true));
		  else
			   return (new ResultValue(1, true));
		  }
	  
	  public boolean validateQuery(){		    
		  
	        //Validating the from clause of the query
	        if(!isValidFromClause(myFrom)){
	        	System.out.println("Semantically incorrect query: Referenced table in from clause do not present in database");
	        	return false;
	        }
	        
	        //Validating the group by clause of the query
	        if((att != null) && !isValidGroupByClause(att,myFrom,mySelect)){
	        	System.out.println("Semantically incorrect query");
	        	return false;
	        }
	        
	        //Validating the Type mismatches in the WHERE Expression
	        if((where != null)&&!(validateTypeExpression(where,myFrom).isResult())){
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

