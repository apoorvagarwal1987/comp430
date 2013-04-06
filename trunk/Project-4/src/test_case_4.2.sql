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
        (n1.n_nationkey > 5  and n1.n_regionkey = 2 ) or ( n1.n_regionkey = 1 );

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
        o1.o_custkey = c1.c_custkey and o1.o_custkey < 12;
        
__________________________________________________

 SELECT 
        n1.n_nationkey,
        n1.n_name,
        n1.n_regionkey,
        r1.r_regionkey
 FROM
        nation AS n1,
        region AS r1        
 WHERE
        ( n1.n_nationkey > 5  and n1.n_regionkey = 2  ) and ( n1.n_name < "JAPAN"  and  r1.r_regionkey = 3) ;

 __________________________________________________

SELECT
      l1.l_orderkey,
      o1.o_custkey,
      o1.o_orderstatus
FROM
    lineitem AS l1,
    orders AS o1,
    customer AS c1

WHERE 
    (l1.l_orderkey =  o1.o_orderkey) and (o1.o_custkey = 7);
    
__________________________________________________

SELECT
      l1.l_orderkey,
      o1.o_custkey,
      o1.o_orderstatus
FROM
    lineitem AS l1,
    orders AS o1

WHERE 
    l1.l_orderkey =  o1.o_orderkey;

__________________________________________________

SELECT 
    r1.r_regionkey,
    n1.n_regionkey,
    s1.s_nationkey    
FROM 
    region AS r1,
    nation AS n1,
    supplier AS s1
WHERE 
    (r1.r_regionkey = n1.n_regionkey) and (n1.n_nationkey = s1.s_nationkey);
__________________________________________________


-- Test Case to check whether the same tables with different alias are correctly recognised
SELECT 
    r1.r_regionkey,
    r1.r_name,
    r2.r_name    
FROM 
    region AS r1,
    region AS r2
WHERE
    (r1.r_name < r2.r_name);

__________________________________________________
 
-- Testing the pushing of the joins
SELECT 
    r1.r_regionkey,
    n1.n_regionkey,
    s1.s_nationkey    
FROM 
    region AS r1,
    nation AS n1,
    supplier AS s1,
    part AS p1,
    orders AS o1
WHERE 
    (r1.r_regionkey = n1.n_regionkey OR p1.p_partkey = 12) and (n1.n_nationkey = 12 OR o1.o_custkey = 12 OR s1.s_nationkey = 12) and (n1.n_nationkey = s1.s_nationkey);
__________________________________________________





