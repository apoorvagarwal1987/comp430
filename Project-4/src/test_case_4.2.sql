 __________________________________________________

 SELECT	
 		c.c_custkey,
		c.c_phone,
		c.c_acctbal		
 FROM
		customer AS c;
__________________________________________________

 SELECT	
 		c1.c_custkey,
		c1.c_phone,
		c1.c_acctbal		
 FROM
		customer AS c1

 WHERE
  		c1.c_custkey > 10 ;		
__________________________________________________

 SELECT	
 		c1.c_custkey,
		c1.c_phone,
		c1.c_acctbal		
 FROM
		customer AS c1,
		customer As c2

 WHERE
  		c1.c_custkey > 10 and c2.c_custkey;

__________________________________________________

 SELECT	
 		c1.c_custkey + 100,
		c1.c_phone,
		c1.c_acctbal		
 FROM
		customer AS c1

 WHERE
  		c1.c_custkey > 10 ;

__________________________________________________

 SELECT	
 		c1.c_custkey + 100 * 0.45,
		c1.c_phone,
		c1.c_acctbal		
 FROM
		customer AS c1

 WHERE
  		c1.c_custkey > 10 ;
__________________________________________________

 SELECT	
 		n1.n_nationkey,
 		n1.n_name,
 		n1.n_regionkey
 FROM
		nation AS n1
 WHERE
  		n1.n_nationkey > 5 ;

__________________________________________________

-- Test case is failing need to fix this

 SELECT	
 		n1.n_nationkey,
 		n1.n_name,
 		n1.n_regionkey
 FROM
		nation AS n1
 WHERE
  		n1.n_nationkey > 5  and n1.n_regionkey = 4;

 __________________________________________________

-- Test case is failing need to fix this
 SELECT 
 		o1.o_orderkey,
 		o1.o_custkey,
 		o1.o_orderstatus
 FROM 
 		orders AS o1
 WHERE
 		o1.o_orderdate > "1996-12-19";

__________________________________________________

-- Case in which string addition is working in the select clause 
 SELECT 
 		o1.o_orderkey,
 		o1.o_custkey,
 		o1.o_orderdate + " Apoorv"
 FROM 
 		orders AS o1;

__________________________________________________ 		


 SELECT 
 		r1.r_regionkey,
 		r1.r_name
 FROM 
 		region as r1
 WHERE
 		r1.r_name < "ASIA";



o_orderdate > Str ("1996-12-19") && o_custkey < Int (100)






