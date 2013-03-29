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
 		c1.c_custkey + 100 * 0.01,
		c1.c_phone,
		c1.c_acctbal		
 FROM
		customer AS c1

 WHERE
  		c1.c_custkey > 10 ;
__________________________________________________




















