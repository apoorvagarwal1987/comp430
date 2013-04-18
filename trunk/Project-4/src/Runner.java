
import java.util.*;
import java.io.*;

class Runner {
 
  static public void main (String [] args) {

    long startTime = System.currentTimeMillis();  

    System.out.println ("first running a selection...");
    DoSelection ();

    System.out.println ("now running a join by...");
    DoJoin();
    
    System.out.println ("now running a group by...");
    DoGroupBy ();
    
    long endTime = System.currentTimeMillis();
    System.out.println("The run took " + (endTime - startTime) + " milliseconds");
  }
    
  /*****************************************/
  // This code shows how to run a group by //
  /*****************************************/
  
  private static void DoGroupBy () {
      
    ArrayList <Attribute> inAtts = new ArrayList <Attribute> ();
    inAtts.add (new Attribute ("Int", "o_orderkey"));
    inAtts.add (new Attribute ("Int", "o_custkey"));
    inAtts.add (new Attribute ("Str", "o_orderstatus"));
    inAtts.add (new Attribute ("Float", "o_totalprice"));
    inAtts.add (new Attribute ("Str", "o_orderdate"));
    inAtts.add (new Attribute ("Str", "o_orderpriority"));
    inAtts.add (new Attribute ("Str", "o_clerk"));
    inAtts.add (new Attribute ("Int", "o_shippriority"));
    inAtts.add (new Attribute ("Str", "o_comment"));
    
    ArrayList <Attribute> outAtts = new ArrayList <Attribute> ();
    outAtts.add (new Attribute ("Str", "att1"));
    outAtts.add (new Attribute ("Str", "att2"));
    outAtts.add (new Attribute ("Float", "att3"));
    outAtts.add (new Attribute ("Int", "att4"));
    
    ArrayList <String> groupingAtts = new ArrayList <String> ();
    groupingAtts.add ("o_orderdate");
    groupingAtts.add ("o_orderstatus");
    
    HashMap <String, AggFunc> myAggs = new HashMap <String, AggFunc>  ();
    myAggs.put ("att1", new AggFunc ("none", "Str(\"status: \") + o_orderstatus"));
    myAggs.put ("att2", new AggFunc ("none", "Str(\"date: \") + o_orderdate"));
    myAggs.put ("att3", new AggFunc ("avg", "o_totalprice * Int (100)"));
    myAggs.put ("att4", new AggFunc ("sum", "Int (1)"));
    
    // run the selection operation
    try {
      Grouping foo = new Grouping (inAtts, outAtts, groupingAtts, myAggs, "src/orders.tbl", "src/out.tbl", "g++", "src/cppDir/"); 
    } catch (Exception e) {
      throw new RuntimeException (e);
    }
  }
  
  
  
  
  /******************************************/
  // This code shows how to run a selection //
  /******************************************/
  
  private static void DoSelection () {
      
    ArrayList <Attribute> inAtts = new ArrayList <Attribute> ();
    inAtts.add (new Attribute ("Int", "o_orderkey"));
    inAtts.add (new Attribute ("Int", "o_custkey"));
    inAtts.add (new Attribute ("Str", "o_orderstatus"));
    inAtts.add (new Attribute ("Float", "o_totalprice"));
    inAtts.add (new Attribute ("Str", "o_orderdate"));
    inAtts.add (new Attribute ("Str", "o_orderpriority"));
    inAtts.add (new Attribute ("Str", "o_clerk"));
    inAtts.add (new Attribute ("Int", "o_shippriority"));
    inAtts.add (new Attribute ("Str", "o_comment"));
    
    ArrayList <Attribute> outAtts = new ArrayList <Attribute> ();
    outAtts.add (new Attribute ("Int", "att1"));
    outAtts.add (new Attribute ("Float", "att2"));
    outAtts.add (new Attribute ("Str", "att3"));
    outAtts.add (new Attribute ("Int", "att4"));
    
    String selection = "o_orderdate > Str (\"1996-12-19\") && o_custkey < Int (100)";
    
    HashMap <String, String> exprs = new HashMap <String, String> ();
    exprs.put ("att1", "o_orderkey");
    exprs.put ("att2", "(o_totalprice * Float (1.5)) + Int (1)");
    exprs.put ("att3", "o_orderdate + Str (\" this is my string\")");
    exprs.put ("att4", "o_custkey");
    
  
    // run the selection operation
    try {
      Selection foo = new Selection (inAtts, outAtts, selection, exprs, "src/orders.tbl", "src/out.tbl", "g++", "src/cppDir/"); 
    } catch (Exception e) {
      throw new RuntimeException (e);
    }
  }
  
  /*************************************/
  // This code shows how to run a join //
  /*************************************/
    
  private static void DoJoin () {
      
    ArrayList <Attribute> inAttsRight = new ArrayList <Attribute> ();
    inAttsRight.add (new Attribute ("Int", "o_orderkey"));
    inAttsRight.add (new Attribute ("Int", "o_custkey"));
    inAttsRight.add (new Attribute ("Str", "o_orderstatus"));
    inAttsRight.add (new Attribute ("Float", "o_totalprice"));
    inAttsRight.add (new Attribute ("Str", "o_orderdate"));
    inAttsRight.add (new Attribute ("Str", "o_orderpriority"));
    inAttsRight.add (new Attribute ("Str", "o_clerk"));
    inAttsRight.add (new Attribute ("Int", "o_shippriority"));
    inAttsRight.add (new Attribute ("Str", "o_comment"));
    
    ArrayList <Attribute> inAttsLeft = new ArrayList <Attribute> ();
    inAttsLeft.add (new Attribute ("Int", "c_custkey"));
    inAttsLeft.add (new Attribute ("Str", "c_name"));
    inAttsLeft.add (new Attribute ("Str", "c_address"));
    inAttsLeft.add (new Attribute ("Int", "c_nationkey"));
    inAttsLeft.add (new Attribute ("Str", "c_phone"));
    inAttsLeft.add (new Attribute ("Float", "c_acctbal"));
    inAttsLeft.add (new Attribute ("Str", "c_mktsegment"));
    inAttsLeft.add (new Attribute ("Str", "c_comment"));
    
    ArrayList <Attribute> outAtts = new ArrayList <Attribute> ();
    outAtts.add (new Attribute ("Int", "att1"));
    outAtts.add (new Attribute ("Str", "att2"));    
    outAtts.add (new Attribute ("Int", "att3"));
    outAtts.add (new Attribute ("Str", "att4"));
    outAtts.add (new Attribute ("Int", "att5"));
    
    ArrayList <String> leftHash = new ArrayList <String> ();
    leftHash.add ("c_custkey");

    ArrayList <String> rightHash = new ArrayList <String> ();
    rightHash.add ("o_custkey");
    
    String selection = "right.o_custkey == left.c_custkey && right.o_custkey < Int (135)";
                    
    HashMap <String, String> exprs = new HashMap <String, String> ();
    exprs.put ("att1", "right.o_custkey");
    exprs.put ("att2", "right.o_comment + Str(\" \") + left.c_comment");    
    exprs.put ("att3", "left.c_custkey");
    exprs.put ("att4", "left.c_name");
    exprs.put ("att5", "right.o_orderkey");           
    
    // run the join
    try {
    	
      Join foo = new Join (inAttsLeft, inAttsRight, outAtts, leftHash, rightHash, selection, exprs, 
                                "src/customer.tbl", "src/orders.tbl", "src/out.tbl", "g++", "src/cppDir/"); 
    } catch (Exception e) {
      throw new RuntimeException (e);
    }
    
  }
  
  
  
  private static void DoSelectionTemp () {
      
	    ArrayList <Attribute> inAtts = new ArrayList <Attribute> ();
	    inAtts.add (new Attribute ("Int", "l_orderkey"));
	    inAtts.add (new Attribute ("Int", "l_partkey"));
	    inAtts.add (new Attribute ("Int", "l_suppkey"));
	    inAtts.add (new Attribute ("Int", "l_linenumber"));
	    inAtts.add (new Attribute ("Int", "l_quantity"));
	    
	    inAtts.add (new Attribute ("Float", "l_extendedprice"));
	    inAtts.add (new Attribute ("Float", "l_discount"));
	    inAtts.add (new Attribute ("Float", "l_tax"));

	    inAtts.add (new Attribute ("Str", "l_returnflag"));
	    inAtts.add (new Attribute ("Str", "l_linestatus"));
	    inAtts.add (new Attribute ("Str", "l_shipdate"));
	    inAtts.add (new Attribute ("Str", "l_commitdate"));
	    inAtts.add (new Attribute ("Str", "l_receiptdate"));
	    inAtts.add (new Attribute ("Str", "l_shipinstruct"));
	    inAtts.add (new Attribute ("Str", "l_shipmode"));
	    inAtts.add (new Attribute ("Str", "l_comment"));


	    
	    ArrayList <Attribute> outAtts = new ArrayList <Attribute> ();
	    outAtts.add (new Attribute ("Int", "att1"));
	    outAtts.add (new Attribute ("Int", "att2"));
	    outAtts.add (new Attribute ("Int", "att3"));
	    outAtts.add (new Attribute ("Int", "att4"));
	    outAtts.add (new Attribute ("Int", "att5"));
	    
	    outAtts.add (new Attribute ("Float", "att6"));
	    outAtts.add (new Attribute ("Float", "att7"));
	    outAtts.add (new Attribute ("Float", "att8"));

	    outAtts.add (new Attribute ("Str", "att9"));
	    outAtts.add (new Attribute ("Str", "att10"));
	    outAtts.add (new Attribute ("Str", "att11"));
	    outAtts.add (new Attribute ("Str", "att12"));
	    outAtts.add (new Attribute ("Str", "att13"));
	    outAtts.add (new Attribute ("Str", "att14"));
	    outAtts.add (new Attribute ("Str", "att15"));
	    outAtts.add (new Attribute ("Str", "att16"));
	    
	    for (Attribute att : outAtts){
	    	att.print();
	    }
	    
	    String selection = "l_shipinstruct == Str (\"take back return\") && " +
    			"(l_extendedprice / l_quantity) > Float (1759.6) && " +
    			"(l_extendedprice / l_quantity) < Float (1759.8)";
	    
	    HashMap <String, String> exprs = new HashMap <String, String> ();
	    exprs.put ("att1", "l_orderkey");
	    exprs.put ("att2", "l_partkey");
	    exprs.put ("att3", "l_suppkey");
	    exprs.put ("att4", "l_linenumber");
	    exprs.put ("att5", "l_quantity");
	    exprs.put ("att6", "l_extendedprice");
	    exprs.put ("att7", "l_discount");
	    exprs.put ("att8", "l_tax");
	    exprs.put ("att9", "l_returnflag");
	    exprs.put ("att10", "l_linestatus");
	    exprs.put ("att11", "l_shipdate");
	    exprs.put ("att12", "l_commitdate");
	    exprs.put ("att13", "l_receiptdate");
	    exprs.put ("att14", "l_shipinstruct");
	    exprs.put ("att15", "l_shipmode");
	    exprs.put ("att16", "l_comment");
	    
	    System.out.println(exprs);
	    
	    // run the selection operation
	    try {
	      Selection foo = new Selection (inAtts, outAtts, selection, exprs, "src/lineitem.tbl", "src/outTemp.tbl", "g++", "src/cppDir/"); 
	    } catch (Exception e) {
	      throw new RuntimeException (e);
	    }
	  }
  
  
}