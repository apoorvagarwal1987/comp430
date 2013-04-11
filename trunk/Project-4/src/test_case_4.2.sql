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
    (r1.r_regionkey = n1.n_regionkey OR p1.p_partkey = 12) and 
    (n1.n_nationkey = 12 OR o1.o_custkey = 12 OR s1.s_nationkey = 12) and 
    (n1.n_nationkey = s1.s_nationkey) and 
    (s1.s_nationkey = 19) and
    (o1.o_custkey = 5);
__________________________________________________

select
    "supplier name was " + s.s_name
from
    supplier as s,
    lineitem as l1,
    lineitem as l2,
    orders as o,
    nation as n
where
    (s.s_suppkey = l1.l_suppkey)
    and (o.o_orderkey = l1.l_orderkey)
    and (o.o_orderstatus = "F")
    and (l1.l_receiptdate > l1.l_commitdate)
    and (l2.l_orderkey = l1.l_orderkey)
    and (not l2.l_suppkey = l1.l_suppkey);
__________________________________________________


select
    "supplier name was " + s.s_name
from
    supplier as s,
    lineitem as l1,
    lineitem as l2,
    orders as o,
    nation as n 
where
    (s.s_suppkey = l1.l_suppkey)
    and (o.o_orderkey = l1.l_orderkey)
    and (o.o_orderstatus = "F")
    and (l1.l_receiptdate > l1.l_commitdate)
    and (l2.l_orderkey = l1.l_orderkey)
    and (not l2.l_suppkey = l1.l_suppkey);

    __________________________________________________


--Under Debugging mode
select
    "supplier name was " + s.s_name
from
    supplier as s,
    lineitem as l1,
    lineitem as l2,
    orders as o
where
    (s.s_suppkey = l1.l_suppkey)
    and (o.o_orderkey = l1.l_orderkey)
    and (o.o_orderstatus = "F")
    and (l1.l_receiptdate > l1.l_commitdate)
    and (not l2.l_suppkey = l1.l_suppkey);

__________________________________________________


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
    (r1.r_regionkey = n1.n_regionkey OR p1.p_partkey = 12) and 
    (n1.n_nationkey = s1.s_nationkey) and 
    (s1.s_nationkey = 19) and
    (o1.o_custkey = 5);

__________________________________________________


select 
    sum (1), 
    avg ((o.o_totalprice - 32592.14) / 32592.14)
from
    orders as o
where
    (o.o_orderstatus = "F") and 
    (o.o_orderpriority < "2-HIGH" or o.o_orderpriority = "2-HIGH");

__________________________________________________

select 
    o.o_orderstatus, 
    o.o_orderpriority,
    o.o_totalprice
from
    orders as o
where
    (o.o_orderstatus = "F") and 
    (o.o_orderpriority < "2-HIGH" or o.o_orderpriority = "2-HIGH");

__________________________________________________


select 
    "status :" + o.o_orderstatus,
    "data: " + o.o_orderdate,
    avg(o.o_totalprice * 100),
    sum(1)
from
    orders as o
where
    (o.o_orderstatus = "F")
group by
    o.o_orderdate;


__________________________________________________


select
    p2.ps_partkey, 
    avg (p2.ps_supplycost)
from
    part as p1,
    partsupp as p2,
    supplier as s,
    nation as n,
    region as r
where
    (p1.p_partkey = p2.ps_partkey)
    and (s.s_suppkey = p2.ps_suppkey)
    and (s.s_nationkey = n.n_nationkey)
    and (n.n_regionkey = r.r_regionkey)
    and (r.r_name = "AMERICA")
group by
    p2.ps_partkey;

  4763 milliseconds  w/o sel down
  8228 milliseconds  w sel down
_______________________________________________


select
    n.n_name,
    o.o_orderdate,
    l.l_extendedprice * (1 - l.l_discount) - p2.ps_supplycost * l.l_quantity 
from
    part as p1,
    supplier as s,
    lineitem as l,
    partsupp as p2,
    orders as o,
    nation as n
where
    (s.s_suppkey = l.l_suppkey)
    and (p2.ps_suppkey = l.l_suppkey)
    and (p2.ps_partkey = l.l_partkey)
    and (p1.p_partkey = l.l_partkey)
    and (o.o_orderkey = l.l_orderkey)
    and (s.s_nationkey = n.n_nationkey)
    and (p1.p_type = "STANDARD POLISHED TIN" or
         p1.p_type = "ECONOMY BRUSHED TIN" or
         p1.p_type = "ECONOMY POLISHED NICKEL" or
         p1.p_type = "SMALL ANODIZED COPPER");


__________________________________________________

select
	l1.l_shipmode,
	sum (1)
from
	orders as o1,
	lineitem as l1
where
	(o1.o_orderkey = l1.l_orderkey)
	and (o1.o_orderstatus = "F")
	and (l1.l_shipmode = "truck" or l1.l_shipmode = "rail")
	and (l1.l_commitdate < l1.l_receiptdate)
	and (l1.l_shipdate < l1.l_commitdate)
	and (l1.l_receiptdate > "1996-06-20" or l1.l_receiptdate = "1996-06-20")
	and (l1.l_receiptdate < "1997-06-20" or l1.l_receiptdate = "1997-06-20")
group by
	l1.l_shipmode;

_________________________________________________________

_______________________________________________
