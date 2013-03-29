	SELECT
		 n.n_name,
		 SUM(l.l_extendedprice * (1 - l.l_discount))
	FROM
		 customer AS c,
		 orders AS o,
		 lineitem AS l,
		 supplier AS s,
		 nation AS n,
		 region AS r
	WHERE
		 (c.c_custkey = o.o_custkey)
		 AND (l.l_orderkey = o.o_orderkey)
		 AND (l.l_suppkey = s.s_suppkey)
		 AND (c.c_nationkey = s.s_nationkey)
		 AND (s.s_nationkey = n.n_nationkey)
		 AND (n.n_regionkey = r.r_regionkey)
		 AND (r.r_name = "region")
		 AND (o.o_orderdate > "date1" OR o.o_orderdate = "date1")
		 AND (NOT o.o_orderdate < "date2")
GROUP BY
		n.n_name;
 ________________________________________________________________________________________________
 
 SELECT
		c.c_phone
 FROM
		customer AS c,
		orders AS o,
		region AS r
 WHERE
		(c.c_custkey = o.o_custkey)
		AND (r.r_name = "region") ;
 ________________________________________________________________________________________________
  
 -- query with mismatch between int and string in WHERE clause with "literal int"
 SELECT
		c.c_phone
 FROM
		customer AS c,
		orders AS o,
		region AS r
 WHERE
		(r.r_name = 10)	 ;
 _________________________________________________________________________________________________
 
 -- query with mismatch between int and string in WHERE clause "identifier"
 SELECT
		c.c_phone
 FROM
		customer AS c,
		orders AS o,
		region AS r
 WHERE
		(c.c_custkey = o.o_orderpriority) ;
 _________________________________________________________________________________________________
 
  -- query with mismatch between int and string in SELECT clause "identifier" / "literal identifier"
 SELECT
		(c.c_phone * c.c_custkey),
		(1 - c.c_phone)
 FROM
		customer AS c,
		orders AS o,
		region AS r
 WHERE
		(c.c_custkey = 4) ;
 _________________________________________________________________________________________________
 
 -- query with mismatch between int and string in SELECT clause attribute do not exist
 SELECT
		(1 - c.c_phone1)
 FROM
		customer AS c,
		orders AS o,
		region AS r
 WHERE
		(c.c_custkey = 4) ;
  _________________________________________________________________________________________________

  -- correct query
 SELECT
		(1 - c.c_custkey)
 FROM
		customer AS c,
		orders AS o,
		region AS r
 WHERE
		(c.c_custkey = 4) ;
 
   _________________________________________________________________________________________________
 
 	SELECT
		 SUM(l.l_shipinstruct * (1 - l.l_discount))
	FROM
		 customer AS c,
		 orders AS o,
		 lineitem AS l,
		 supplier AS s,
		 nation AS n,
		 region AS r
	WHERE
		 (c.c_custkey = o.o_custkey)
GROUP BY
		n.n_name;
    _________________________________________________________________________________________________
 
 --Query is correct
 SELECT
		("asd" - c.c_phone)
 FROM
		customer AS c,
		orders AS o,
		region AS r
 WHERE
		(c.c_custkey = 4) ;
 
     _________________________________________________________________________________________________
 
  --Query is incorrect
 SELECT
		AVG("sad"*(2 - c.c_custkey))
 FROM
		customer AS c,
		orders AS o,
		region AS r
 WHERE
		(c.c_custkey = 4) ;
 
     _________________________________________________________________________________________________
 
 	SELECT
		 n.n_name,
		 SUM(l.l_shipinstruct * (1 - l.l_discount))
	FROM
		 customer AS c,
		 orders AS o,
		 lineitem AS l,
		 supplier AS s,
		 nation AS n,
		 region AS r
	WHERE
		 (c.c_custkey = o.o_custkey)	
		AND (o.o_orderdate > "date1" OR o.o_orderdate = "date1")		 
 		 AND (l.l_orderkey = o.o_orderkey)
		 AND (l.l_suppkey2 = s.s_suppkey)
		 AND (c.c_nationkey = s.s_nationkey)
		 AND (s.s_nationkey = n.n_nationkey)
		 AND (n.n_regionkey = r.r_regionkey)
		 AND (r.r_name = "region")
		AND (NOT o.o_orderdate < "date2")		
	GROUP BY
		n.n_name;
     _________________________________________________________________________________________________
 
	  -- query with mismatch between string and string in SELECT clause "identifier" / "literal identifier" but doing times operation
	 SELECT
			(c.c_phone * c.c_address)			
	 FROM
			customer AS c,
			orders AS o,
			region AS r
	 WHERE
			(c.c_custkey = 4) ;
	 _________________________________________________________________________________________________

	
SELECT
        n.n_name,
        SUM(l.l_shipdate * (1 - l.l_discount))
FROM
        customer AS c,
        orders AS o,
        lineitem AS l,
        supplier AS s,
        nation AS n,
        region AS r
WHERE
        (c.c_custkey = o.o_custkey)
        AND (l.l_orderkey = o.o_orderkey)
        AND (l.l_suppkey = s.s_suppkey)
        AND (c.c_nationkey = s.s_nationkey)
        AND (s.s_nationkey = n.n_nationkey)
        AND (n.n_regionkey = r.r_regionkey)
        AND (r.r_name = "region")
        AND (o.o_orderdate > "date1" OR o.o_orderdate = "date1")
        AND (NOT o.o_orderdate < "date2")
GROUP BY
        n.n_name;

_________________________________________________________________________________________________
SELECT 
	n.n_name,
	s.s_suppkey,
	SUM(l.l_extendedprice)
FROM 
	nation AS n,
	supplier AS s,
	lineitem AS l
WHERE 
	(l.l_suppkey = s.s_suppkey)
	AND (s.s_nationkey = n.n_nationkey)
GROUP BY
	n.n_name;
_________________________________________________________________________________________________
SELECT
	SUM(l.l_extendedprice * (1 - l.l_discount))
FROM
	lineitem AS l
WHERE
	(l.l_extendedprice * (1 - l.l_discount) > "a string");

_________________________________________________________________________________________________
SELECT
	SUM(l.l_returnflag + l.l_shipdate + l.l_commitdate)
FROM 
	lineitem AS l;

_________________________________________________________________________________________________
SELECT
	l.l_returnflag + l.l_shipdate + l.l_commitdate
FROM 
	lineitem AS l
WHERE
	(l.l_shipdate > "a date"); 

_________________________________________________________________________________________________
SELECT
        n.n_name,
        SUM(l.l_extendedprice * (1 - l.l_discount))
FROM
        customer AS c,
        orders AS o,
        lineitem AS l,
        supplier AS s,
        nation AS n,
        region AS r
WHERE
        (c.c_custkey = o.o_custkey)
        AND (l.l_orderkey = o.o_orderkey)
        AND (l.l_suppkey = s.s_suppkey)
        AND (c.c_nationkey = s.s_nationkey)
        AND (s.s_nationkey = n.n_nationkey)
        AND (n.n_regionkey = r.r_regionkey)
        AND (r.r_name = "region")
        AND (l.l_tax + l.l_discount > "date2" OR o.o_orderdate = "date1")
        AND (NOT o.o_orderdate < "date2")
GROUP BY
        n.n_name;
_________________________________________________________________________________________________
SELECT
        SUM(l.l_extendedprice * (1 - l.l_discount))
FROM
        customer AS c,
        orders AS o,
        lineitem AS l
WHERE
        (c.c_custkey = o.o_custkey)
        AND (l.l_orderkey = o.k_orderkey);
_________________________________________________________________________________________________
SELECT
        SUM(l.l_extendedprice * (1 - l.l_discount))
FROM
        customer AS c,
        orders AS o,
        lineitem AS l
WHERE
        (c.c_custkey = h.o_custkey)
        AND (l.l_orderkey = o.o_orderkey);
_________________________________________________________________________________________________
SELECT
        SUM(l.l_extendedprice * (1 - l.l_discount))
FROM
        customer AS c,
        orders AS o,
        lineitem AS l
WHERE
        (c.c_custkey = o.o_custkey)
	AND ("this is a string" + "this is another string" > 15.0 - "here is another")
        AND (l.l_orderkey = o.o_orderkey);
_________________________________________________________________________________________________
SELECT
        SUM(l.l_extendedprice * (1 - l.l_discount))
FROM
        customers AS c,
        orders AS o,
        lineitem AS l
WHERE
        (c.c_custkey = o.o_custkey)
        AND (l.l_orderkey = o.o_orderkey);

_________________________________________________________________________________________________
SELECT
        SUM(l.l_extendedprice * (1 - l.l_discount))
FROM
        customer AS c,
        orders AS o,
        lineitem AS l
WHERE
        (c.c_custkey = o.o_custkey)
        AND (l.l_orderkey = o.o_orderkey)
	AND (1200 + l.l_quantity / (300.0 + 34) = 33);
_________________________________________________________________________________________________
SELECT
        SUM(l.l_extendedprice * (1 - l.l_discount))
FROM
        customer AS c,
        orders AS o,
        lineitem AS l
WHERE
        (c.c_custkey = o.o_custkey)
	AND ("this is a string" + "this is another string" > "here is another")
        AND (l.l_orderkey = o.o_orderkey);

_________________________________________________________________________________________________
SELECT
        SUM(l.l_extendedprice * (1 - l.l_discount)),
	SUM(c.l_extendedprice * 1.01)
FROM
        customer AS c,
        orders AS o,
        lineitem AS l
WHERE
        (c.c_custkey = o.o_custkey)
        AND (l.l_orderkey = o.o_orderkey);

_________________________________________________________________________________________________
SELECT
        SUM(l.l_extendedprice * (1 - l.l_discount))
FROM
        customer AS c,
        orders AS o,
        lineitem AS l
WHERE
        (c.c_custkey = o.o_custkey)
	AND ("1204" + "this is a string" = l.l_returnflag)
	AND (l.l_tax + l.l_discount + l.l_extendedprice > 3.27)
        AND (l.l_orderkey = o.o_orderkey);


_________________________________________________________________________________________________
SELECT
        SUM(l.l_extendedprice * (1 - l.l_discount))
FROM
        customer AS c,
        orders AS o,
        lineitem AS l
WHERE
        (c.c_custkey = o.o_custkey)
	AND (l.l_tax + l.l_discount + l.l_extendedprice > 3.27 OR 
		l.l_discount + l.l_extendedprice > "327")
        AND (l.l_orderkey = o.o_orderkey);

_________________________________________________________________________________________________
SELECT
        SUM(l.l_extendedprice * (1 - l.l_discount)),
	SUM(1 - l.l_discount)
FROM
        customer AS c,
        orders AS o,
        lineitem AS l
WHERE
        (c.c_custkey = o.o_custkey)
	AND (l.l_tax + l.l_discount + l.l_extendedprice > 3.27 OR 
		"here is a string" > "327")
        AND (l.l_orderkey = o.o_orderkey);
_________________________________________________________________________________________________
SELECT
	SUM(12 + 13.4 + 19),
	SUM(l.l_extendedprice)
FROM 
	lineitem AS l;

_________________________________________________________________________________________________




