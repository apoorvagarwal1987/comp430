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
		String alias = att.substring(0, att.indexOf('.'));
		String tableName = fromClause.get(alias);
		TableData tableInfo = res.get(tableName);
		System.out.println(tableInfo.print());
		return false;
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
        
        System.out.println ("WHERE clause:");
        Expression where = parser.getWHERE ();  
        	
        if (where != null)
          System.out.println ("\t" + where.print ());
        
        System.out.println ("GROUPING att:");
        String att = parser.getGROUPBY ();
        if (att != null) 
          System.out.println ("\t" + att);
                  
        /*
         * Doing the semantics check of the query :			
         */
        
        //Validating the from clause of the query
        if(!validateFromClause(myFrom)){
        	System.out.println("\n\n\nSemantically incorrect query: Referenced table in from clause do not present in database");
        }
        
        //Validating the group by clause of the query
        if((att != null) && !validateGroupByClause(att,myFrom))
        
        System.out.format ("\nSQL>");
              
      } 
    } catch (Exception e) {
      System.out.println("Error! Exception: " + e); 
    } 
  }
}
