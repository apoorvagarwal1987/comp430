use master;


/************************************************Query : 1***************************************************/
SELECT DISTINCT(c1.name)
FROM climber AS c1 ,  participated AS p , climbed AS c2, peak AS p1
WHERE c1.sex ='F' and p.name = c1.name and p.trip_id = c2.trip_id and c2.peak = p1.name and p1.elev >= 14000
ORDER BY c1.name
/************************************************************************************************************/




/*************************************Query : 2***************************************************/
SELECT c1.peak
FROM climbed AS c1
GROUP BY c1.peak
having (COUNT(c1.peak)=1)
ORDER BY c1.peak
/**************************************************************************************************/

/**************************************Query : 3***************************************************/

SELECT DISTINCT(oc1.name)

FROM	climber AS oc1 , 
		participated AS op1, 
		climbed AS oc2

WHERE	oc1.name = op1.name and 
		op1.trip_id = oc2.trip_id and 
		oc2.peak in 	 
					(
						SELECT DISTINCT(ic1.peak)

						FROM	climbed AS ic1 , 
								participated AS ip1, 
								climber AS ic2	

						WHERE	ic2.name ='Patricia' and
								ic2.name = ip1.name and ip1.trip_id = ic1.trip_id 

					)


/****************************************************************************************************/


/****************************************Query : 4***************************************************/

SELECT *
FROM climber AS c1 
WHERE c1.sex ='M'

drop view person_climbed

CREATE VIEW person_climbed 
AS
	SELECT name, peak, when_climbed
	FROM climbed AS c1, participated AS p1
	WHERE c1.trip_id = p1.trip_id

SELECT * 
FROM person_climbed

SELECT d1.sex , Round((d1.Climbed * 1.0 / d2.COUNT),3) AS "Average"
FROM (
		SELECT c1.sex,COUNT(*) AS "Climbed"
		FROM person_climbed AS p1 , climber AS c1
		WHERE p1.name = c1.name
		GROUP BY c1.sex
	) AS d1,
 
	(	SELECT c.sex , COUNT(*) AS "COUNT"
		FROM climber AS c
		GROUP BY c.sex
	) AS d2

WHERE d1.sex = d2.sex

/**************************************************************************************************/



/***************************************Query : 5***************************************************/


 SELECT DISTINCT temp.map
 FROM		(	SELECT p1.map 
				FROM peak AS p1
				GROUP BY p1.map 
				having COUNT(*) >1
			) AS temp , peak AS p1

 WHERE  p1.map = temp.map
		and exists
						(
							SELECT *
							FROM peak AS p2
							WHERE p2.name != p1.name and p1.map = p2.map and p1.region != p2.region
						)


/***************************************************************************************************/



/***************************************Query : 6***************************************************/

SELECT p.name
FROM peak AS p
WHERE p.map in (
				SELECT p1.map
				FROM peak AS p1
				WHERE p1.ELEV >= 14000
			  )

ORDER BY p.name
--
/************************************************************************************************************/



/************************************************Query : 7***************************************************/
SELECT p.name
FROM peak AS p
WHERE p.name in (
				SELECT p1.name FROM peak AS p1
				EXCEPT
				SELECT c1.peak FROM climbed AS c1
			 ) 
	and  p.diff = 5

/************************************************************************************************************/



/************************************************Query : 8***************************************************/
SELECT p.map

FROM peak AS p

GROUP BY p.map

having ( MAX(p.elev)- MIN(p.elev)) >= 2000

/************************************************************************************************************/


/************************************************Query : 9***************************************************/

	SELECT Top (10) d1.peak , SUM(d1.times) as "Times"
	FROM 
		(
			SELECT p1.name ,c1.peak, COUNT(*)-1 as "times" 
			FROM climbed AS c1, participated AS p1
			WHERE c1.trip_id = p1.trip_id
			group BY p1.name, c1.peak			
		) as d1
	group BY d1.peak
	having (SUM(d1.times)>1)
	ORDER BY SUM(d1.times) DESC

/**********************************************Query 10**************************************************************/

	SELECT  p.name 

	FROM	climbed AS c  , 
			participated AS p

	WHERE	c.trip_id = p.trip_id 
			
	GROUP BY p.name 

	having min(c.when_climbed) >= '2003-06-01 00:00:00.000'

/**************************************************Query 11******************************************************************/

drop view person_peak
CREATE VIEW person_peak AS 
SELECT DISTINCT p2.name , c1.peak 
			FROM	peak AS p1 ,
					participated AS p2,
					climbed AS c1
			WHERE	c1.trip_id = p2.trip_id	

SELECT p1.region, count(DISTINCT(p1.name))
FROM	peak AS p1,
		person_peak AS p2
WHERE	p1.name = p2.peak 	
GROUP BY p1.region

/***************************************************************************************************************************/


/************************************************Query 12*******************************************************************/

CREATE VIEW person_peak AS 
SELECT DISTINCT p2.name , c1.peak 
			FROM	peak AS p1 ,
					participated AS p2,
					climbed AS c1
			WHERE	c1.trip_id = p2.trip_id	


SELECT AVG(p2.elev) AS "Avg Height"
FROM	(
			SELECT	 p1.name 
			FROM	 peak AS p1
			WHERE	 p1. region = 'Corocoran to Whitney'
				Except
			SELECT p2.peak
			FROM person_peak AS p2
		)	AS		temp , 
			peak AS p2

WHERE temp.name = p2.name 


/***************************************************************************************************************************/


/*************************************************Query 13******************************************************************/

CREATE VIEW person_peak AS 
SELECT DISTINCT p2.name , c1.peak , p2.trip_id
			FROM	peak AS p1 ,
					participated AS p2,
					climbed AS c1
			WHERE	c1.trip_id = p2.trip_id	

SELECT DISTINCT p2.name
FROM person_peak AS p2 
WHERE not exists (
						SELECT p1.peak
						FROM person_peak AS p1
						WHERE p1.name = 'BARBARA' 
						
						Except
						
						SELECT p3.peak
						FROM person_peak AS p3
						WHERE p3.name = p2.name 
					)
	and p2.name != 'BARBARA'

	

/******************************************************************************************************/



/*************************************************Query 14*********************************************/

CREATE VIEW person_peak AS 
SELECT DISTINCT p2.name , c1.peak , p2.trip_id
			FROM	participated AS p2,
					climbed AS c1
			WHERE	c1.trip_id = p2.trip_id	


SELECT DISTINCT  temp.peak as "Peak" ,  temp.name as "Climber"
FROM person_peak as temp 
WHERE not exists(	

				SELECT  temp2.trip_id
				FROM	person_peak as temp2
				WHERE	temp2.peak = temp.peak 
				
				EXCEPT

				SELECT	temp1.trip_id
				FROM	person_peak as temp1
				WHERE	temp1.name = temp.name and
						temp1.peak = temp.peak			
				
				) 
ORDER BY temp.peak

/**********************************************************************************************************/




/***************************************************Query 15***********************************************/


CREATE VIEW trip_asccents AS
SELECT top (1)temp1.when_climbed,count(*) AS "Ascents"
							FROM		climbed as temp1, climbed as temp2
							WHERE		(	temp1.peak != temp2.peak
											or	temp1.trip_id != temp2.trip_id
										--	or	temp1.when_climbed != temp2.when_climbed	
										)
									and datediff(DAY,temp1.when_climbed, temp2.when_climbed )<= 90
							group BY temp1.when_climbed
							ORDER BY count(*) desc


SELECT Top(1) c1.when_climbed as "Start Date", c2.when_climbed as "End Date" , 
				datediff(DAY,c1.when_climbed,c2.when_climbed ) as "Span",t.ascents AS "Ascents"

FROM climbed as c1 , climbed as c2 , trip_asccents AS t
WHERE c1.when_climbed = t.when_climbed

	and datediff(DAY,c1.when_climbed, c2.when_climbed )<= 90
	ORDER BY c2.when_climbed DESC


/*********************************************************************************************************/