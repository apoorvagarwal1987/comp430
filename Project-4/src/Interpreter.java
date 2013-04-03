import java.io.*;
import org.antlr.runtime.*;
import java.util.*;
  
class Interpreter {
  
  static Map <String, TableData> res;	

 
public static void main (String [] args) throws Exception {
    
    try {
      
      CatalogReader foo = new CatalogReader ("src/Catalog.xml");      
      res = foo.getCatalog ();
      //System.out.println (foo.printCatalog (res));
      
    	
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
         * Doing the semantics check of the query for Hw 4.1		
         */
	  	System.out.println("\n\n************************Semantic Validation of Query********************************************\n");
	  	ResultValidQuery rvQuery = (new SemanticCheck(myFrom, mySelect, att, where)).validateQuery();
        if (rvQuery.isResult()){
        	System.out.println("Query is semantically correct");
        }        
	  	System.out.println("************************************************************************************************\n");

        
	  	
	  	 /*
	  	  * Executing the query for Hw 4.2
	  	  */
	 
	  	System.out.println("\n\n************************Execution of Query************************************************\n");

	    long startTime = System.currentTimeMillis(); 
	    new ExecuteQuery(myFrom,mySelect,att,where,rvQuery.getSelTypes()).execution();
	    long endTime = System.currentTimeMillis();
	    System.out.println("The run took " + (endTime - startTime) + " milliseconds");
	    
	  	System.out.println("***********************************************************************************************\n");

	  	
	  	System.out.format ("\nSQL>");
              
      } 
    } catch (Exception e) {
      System.out.println("Error! Exception: " + e); 
    } 
  }
}
