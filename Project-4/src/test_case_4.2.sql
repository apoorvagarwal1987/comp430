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

-- Test case is failing need to fix this, there is some problem with the parser in this case.
 SELECT 
 		o1.o_orderkey,
 		o1.o_custkey,
 		o1.o_orderstatus
 FROM 
 		orders AS o1
 WHERE
 		o1.o_orderdate > "1996-12-19";

__________________________________________________

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
 		r1.r_name < "ASIA"   and r1.r_regionkey = 0;

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

 SELECT	
 		n1.n_nationkey,
 		n1.n_name,
 		n1.n_regionkey
 FROM
		nation AS n1
 WHERE
  		n1.n_nationkey > 5  and n1.n_regionkey = 2;

 __________________________________________________
 
 --TODO: Error in the following query: parser skips the last portion of the clause.

 SELECT	
 		n1.n_nationkey,
 		n1.n_name,
 		n1.n_regionkey
 FROM
		nation AS n1
 WHERE
  		(n1.n_nationkey > 5  and n1.n_regionkey = 2 ) or n1.n_regionkey = 1 ;

__________________________________________________  		

 SELECT	
 		n1.n_nationkey,
 		n1.n_name,
 		n1.n_regionkey
 FROM
		nation AS n1
 WHERE
  		( n1.n_nationkey > 5  and n1.n_regionkey = 2 ) and n1.n_name < "JAPAN";
 __________________________________________________


_________________________________________________

--Working on the JOIN Queries having more than one table_
_________________________________________________


 SELECT	
 		o1.o_custkey,
 		c1.c_custkey,
 		c1.c_name,
 		o1.o_orderkey
 FROM
		orders AS o1,
		customer AS c1
 WHERE
 		o1.o_custkey = c1.c_custkey and o1.o_custkey < 12
  		
__________________________________________________