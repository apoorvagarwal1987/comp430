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
		('asd' - c.c_phone1)
 FROM
		customer AS c,
		orders AS o,
		region AS r
 WHERE
		(c.c_custkey = 4) ;
 
     _________________________________________________________________________________________________
 
 
 
 
 