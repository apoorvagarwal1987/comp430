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
/*				if(CommonMethods.isBinaryOperation(exp.getType())){
					System.out.println("Error: Expression "+ exp.print() +" expression is not allowed in the select clause when GroupBy");
					return false;
				}
				else if(CommonMethods.isUnaryOperation(exp.getType())){
				}				
				else if (!(exp.getType().equals("identifier")&& exp.getValue().equals(att))){
					System.out.println("Error: Expression "+ exp.print() +" expression is not allowed in the select clause when GroupBy");
					return false;	
				}
				else{}*/
				
				if(!(CommonMethods.isValidSelExpression(exp, att)))
					return false;				
			}
			
			return true;
		}

	  
	  public ResultValidQuery validateQuery(){		    
		  
	        //Validating the from clause of the query
		  	ArrayList <ResultValue> selectionTypes = new ArrayList<ResultValue>();
		  
	        if(!isValidFromClause(myFrom)){
	        	System.out.println("Semantically incorrect query: Referenced table in from clause do not present in database");
	        	return (new ResultValidQuery(false,selectionTypes));
	        }
	        
	        //Validating the group by clause of the query
	        if((att != null) && !isValidGroupByClause(att,myFrom,mySelect)){
	        	System.out.println("Semantically incorrect query");
	        	return (new ResultValidQuery(false,selectionTypes));
	        }
	        
	        //Validating the Type mismatches in the WHERE Expression
	        if((where != null)&&!(CommonMethods.validateTypeExpression(where,myFrom).isResult())){
	        	System.out.println("Invalid Expression in WHERE  :" + where.print());
	        	return (new ResultValidQuery(false,selectionTypes));
	        }
	      
	        //Validating the Type mismatch in the SELECT Expression
	        for (Expression selectExp : mySelect){
	        	ResultValue rvTemp = CommonMethods.validateTypeExpression(selectExp,myFrom);
	        	selectionTypes.add(rvTemp);	       
	        	if(!(rvTemp.isResult())){
	        		System.out.println("Invalid Expression in SELECT  :" + selectExp.print());
	        		return (new ResultValidQuery(false,selectionTypes));
	        	}
	        }      
	        return (new ResultValidQuery(true,selectionTypes));
	  }	  
}

