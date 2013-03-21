import java.io.*;
import org.antlr.runtime.*;
import java.util.*;
  
class Interpreter {
  
  static Map <String, TableData> res;	

  private static boolean validateFromClause (Map <String, String> fromClause){	  
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
  
  private static boolean validateGroupByClause(String att, Map <String, String> fromClause) {
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
  
  private static String getAtributeType(String att, Map <String, String> fromClause){
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
  
  private static boolean checkUnaryOperation(String expType) {
	for (String operation : Expression.unaryTypes) {
		if(operation.equals(expType))
			return true;
	}
	return false;
}
  
  private static boolean checkBinaryOperation(String expType) {
	for (String operation : Expression.binaryTypes) {
		if(operation.equals(expType))
			return true;
	}
	return false;
}
  
  private static boolean checkCompatibility(String lExpAttribType,String rExpAttribType, String expType){
	  
	  if((lExpAttribType.equals("Str") && rExpAttribType.equals("Int") ) || (lExpAttribType.equals("Int") && rExpAttribType.equals("Str"))
		  || 	  
		  (lExpAttribType.equals("Str") && rExpAttribType.equals("Float") ) || (lExpAttribType.equals("Float") && rExpAttribType.equals("Str"))
	     ){
		for(String incompatibility : Expression.incompatibleTypes)
			  if(expType.equals(incompatibility))
				  return false;
	  }	  
	  
	  if((lExpAttribType.equals("Str") && rExpAttribType.equals("Str") ) || (lExpAttribType.equals("Str") && rExpAttribType.equals("Str"))){
		  switch(expType){
		  //	case "plus":
		  	case "minus":
		  	case "times":
		  	case "divided by"  : return false;
		  }
	  }	  
	  return true;
  }
  
  private static boolean validateTypeExpression(Expression exp, Map <String, String> fromClause){
	  
/*	  if(exp.getType().equals("and")|| exp.getType().equals("or")){
		  return validateTypeExpression(exp.getLeftSubexpression(),fromClause) && validateTypeExpression(exp.getRightSubexpression(),fromClause);
	  }*/
	  
	  if(exp.getType().equals("and") || exp.getType().equals("or"))
		  return validateTypeExpression(exp.getLeftSubexpression(),fromClause) && validateTypeExpression(exp.getRightSubexpression(),fromClause);
	  
	  if(checkUnaryOperation(exp.getType()))
		  return validateTypeExpression(exp.getLeftSubexpression(),fromClause);
	  
	  String expType = exp.getType();
	  
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
				  return false;
			  
			  if(!checkCompatibility(lExpAttribType, rExpAttribType, expType))
				  return false;
		  }
		  
		  if((lExpType.equals("literal string") && rExpType.equals("literal int") )
				  	|| 
		     (lExpType.equals("literal int") && rExpType.equals("literal string"))
		     		||
		     (lExpType.equals("literal string") && rExpType.equals("literal float") )
				  	|| 
		     (lExpType.equals("literal float") && rExpType.equals("literal string"))
		     ){
		    	 return false;
		     }
		  
		  if(lExpType.equals("literal string") && rExpType.equals("identifier")){
			  String rExpAttribType = getAtributeType(rExp.getValue(), fromClause);
			  if((rExpAttribType == null))
				  return false;
			  
			  if(rExpAttribType.equals("Float")|| rExpAttribType.equals("Int")){
				  for(String incompatibility : Expression.incompatibleTypes)
					  if(expType.equals(incompatibility))
						  return false;
			  }
			  
			  if(rExpAttribType.equals("Str")){
				  switch(expType){
				  //	case "plus":
				  	case "minus":
				  	case "times":
				  	case "divided by"  : return false;
				  }				  
			  }			  
		  }
		  
		  if(rExpType.equals("literal string") && lExpType.equals("identifier")){
			  String lExpAttribType = getAtributeType(lExp.getValue(), fromClause);
			  if((lExpAttribType == null))
				  return false;
			  
			  if(lExpAttribType.equals("Float")|| lExpAttribType.equals("Int")){
				  for(String incompatibility : Expression.incompatibleTypes)
					  if(expType.equals(incompatibility))
						  return false;
			  }	
			  if(lExpAttribType.equals("Str")){
				  switch(expType){
				  //	case "plus":
				  	case "minus":
				  	case "times":
				  	case "divided by"  : return false;
				  }				  
			  }	
		  }
		  
		  if(lExpType.equals("literal int") && rExpType.equals("identifier")
			||
			(lExpType.equals("literal float") && rExpType.equals("identifier"))){
			  
			  String rExpAttribType = getAtributeType(rExp.getValue(), fromClause);
			  if((rExpAttribType == null))
				  return false;
			  if(rExpAttribType.equals("Str"))
				  return false;
		  }
			
		   if(lExpType.equals("identifier") && rExpType.equals("literal int")
				   ||
		    (lExpType.equals("identifier") && rExpType.equals("literal float"))){
			
			  String lExpAttribType = getAtributeType(lExp.getValue(), fromClause);
			  if((lExpAttribType == null))
				  return false;
			  if(lExpAttribType.equals("Str"))
				  return false;
		  }		  
	  }	  
	  return true;
  }
  
  
public static void main (String [] args) throws Exception {
    
    try {
      
      CatalogReader foo = new CatalogReader ("C:\\Users\\ApoorvAgarwal\\workspace\\Assignment04\\src\\Catalog.xml");      
      res = foo.getCatalog ();
      System.out.println (foo.printCatalog (res));
      
    	
      InputStreamReader converter = new InputStreamReader(System.in);
      BufferedReader in = new BufferedReader(converter);
      
      System.out.format ("\nSQL>");
      String soFar = in.readLine () + " ";
      
      // loop forever, or until someone asks to quit
      while (true) {
        
        // keep on reading from standard in until we hit a ";"
        while (soFar.indexOf (';') == -1) {
          soFar += (in.readLine () + " ");
        }
        
        // split the string
        String toParse = soFar.substring (0, soFar.indexOf (';') + 1);
        soFar = soFar.substring (soFar.indexOf (';') + 1, soFar.length ());
        toParse = toParse.toLowerCase ();
        
        // parse it
        ANTLRStringStream parserIn = new ANTLRStringStream (toParse);
        SQLLexer lexer = new SQLLexer (parserIn);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SQLParser parser = new SQLParser (tokens);
        
        // if we got a quit
        if (parser.parse () == false) {
          break; 
        }
        
        // print the results
        System.out.println ("RESULT OF PARSING");
        System.out.println ("Expressions in SELECT:");
        ArrayList <Expression> mySelect = parser.getSELECT ();
        for (Expression e : mySelect)
          System.out.println ("\t" + e.print ());
        
        System.out.println ("Tables in FROM:");
        Map <String, String> myFrom = parser.getFROM ();
        System.out.println ("\t" + myFrom);
        
        Expression where = parser.getWHERE ();
        if (where != null){
        	System.out.println ("WHERE clause:");
            System.out.println ("\t" + where.print ());            
        }  
        	

        String att = parser.getGROUPBY ();
        if (att != null){
            System.out.println ("GROUPING att:");
            System.out.println ("\t" + att);
        }
        
              
        /*
         * Doing the semantics check of the query :			
         */
        
        //Validating the from clause of the query
        if(!validateFromClause(myFrom)){
        	System.out.println("\n\n\nSemantically incorrect query: Referenced table in from clause do not present in database");
        }
        
        //Validating the group by clause of the query
        if((att != null) && !validateGroupByClause(att,myFrom)){
        	System.out.println("\n\n\nSemantically incorrect query: Referenced attribute in the group by clause do not present in database");
        }
        
        //Validating the Type mismatches in the WHERE Expression
        if(validateTypeExpression(where,myFrom)){
        		System.out.println("Valid Expression in WHERE  :" + where.print());
        	}
        	else{
        		System.out.println("Invalid Expression in WHERE  :" + where.print());
        	}
      
        
        //Validating the Type mismatch in the SELECT Expression
        for (Expression selectExp : mySelect){
        	if(validateTypeExpression(selectExp,myFrom)){
        		System.out.println("Valid Expression in SELECT  :" + selectExp.print() );
        	}
        	else{
        		System.out.println("Invalid Expression in SELECT  :" + selectExp.print());
        	}
        }
        
        
        
        
        //
        System.out.format ("\nSQL>");
              
      } 
    } catch (Exception e) {
      System.out.println("Error! Exception: " + e); 
    } 
  }
}
