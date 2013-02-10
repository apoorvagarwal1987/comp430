use master;


/************************************************Query : 1***************************************************/
SELECT Distinct(c1.name)
FROM climber AS c1 ,  participated AS p , climbed AS c2, peak AS p1
WHERE c1.sex ='F' and p.name = c1.name and p.trip_id = c2.trip_id and c2.peak = p1.name and p1.elev >= 14000
/************************************************************************************************************/


/*************************************Query : 2***************************************************/
SELECT peak
FROM CLIMBED AS c1
GROUP BY peak
having (COUNT(peak)=1)
/**************************************************************************************************/

/**************************************Query : 3***************************************************/

SELECT Distinct(oc1.NAME)

FROM	CLIMBER AS oc1 , 
		PARTICIPATED AS op1, 
		CLIMBED AS oc2

WHERE	oc1.NAME = op1.NAME and 
		op1.TRIP_ID = oc2.TRIP_ID and 
		oc2.PEAK in 	 
					(
						SELECT Distinct(ic1.PEAK)

						FROM	CLIMBED AS ic1 , 
								PARTICIPATED AS ip1, 
								CLIMBER AS ic2	

						WHERE	ic2.NAME ='Patricia' and
								ic2.NAME = ip1.NAME and ip1.TRIP_ID = ic1.TRIP_ID 

					)


/****************************************************************************************************/


/****************************************Query : 4***************************************************/

SELECT *
FROM CLIMBER AS c1 
WHERE c1.SEX ='M'

CREATE VIEW person_climbed 
AS
	SELECT name, peak, when_climbed
	FROM CLIMBED AS c1, PARTICIPATED AS p1
	WHERE c1.TRIP_ID = p1.TRIP_ID


SELECT 
avg (  temp.times)  AS "Average"
FROM (SELECT COUNT(*) AS "times"
			FROM person_climbed AS p1 , (	SELECT *
											FROM CLIMBER AS c1 
											WHERE c1.SEX ='M'
										)  AS pMale
			WHERE pMale.NAME = p1.name 
			GROUP BY p1.name) AS temp 


union

SELECT 
avg (  temp.times)  AS "Average"
FROM (SELECT COUNT(*) AS "times"
			FROM person_climbed AS p1 , (	SELECT *
											FROM CLIMBER AS c1 
											WHERE c1.SEX ='F'
										)  AS pMale
			WHERE pMale.NAME = p1.name 
			GROUP BY p1.name) AS temp 


SELECT d1.SEX , (d1.Climbed / d2.Count) AS "Average"
FROM (
		SELECT c1.SEX,COUNT(*) AS "Climbed"
		FROM person_climbed AS p1 , climber AS c1
		WHERE p1.name = c1.NAME
		GROUP BY c1.SEX
	) AS d1,
 
	(	SELECT c.sex , COUNT(*) AS "Count"
		FROM climber AS c
		GROUP BY c.SEX
	) AS d2

WHERE d1.SEX = d2.SEX

/**************************************************************************************************/



/***************************************Query : 5***************************************************/


 SELECT distinct temp.MAP
 FROM		(	SELECT p1.MAP 
				FROM peak AS p1
				GROUP BY p1.MAP 
				having Count(*) >1
			) AS temp , peak AS p1

 WHERE  p1.MAP = temp.MAP
		and exists
						(
							SELECT *
							FROM peak AS p2
							WHERE p2.name != p1.name and p1.map = p2.MAP and p1.REGION != p2.REGION
						)


/***************************************************************************************************/



/***************************************Query : 6***************************************************/

SELECT p1.NAME
FROM peak AS p1
WHERE p1.ELEV >= 14000

/************************************************************************************************************/



/************************************************Query : 7***************************************************/
SELECT * 
FROM peak 
WHERE name in (
				SELECT name FROM peak
				except
				SELECT peak FROM CLIMBED
			 ) 
	and  DIFF = 5

/************************************************************************************************************/



/************************************************Query : 8***************************************************/
SELECT Map

FROM peak 

GROUP BY MAP

having ( MAX(Elev)- MIN(ELEV)) >= 2000

/************************************************************************************************************/


/************************************************Query : 9***************************************************/

	select Top (10) d1.PEAK , sum(d1.times) as "Times"
	from 
		(
			SELECT p1.name ,c1.PEAK, Count(*)-1 as "times" 
			FROM CLIMBED AS c1, PARTICIPATED AS p1
			WHERE c1.TRIP_ID = p1.TRIP_ID
			group by p1.NAME, c1.PEAK			
		) as d1
	group by d1.PEAK
	having (sum(d1.times)>1)
	order by sum(d1.times) DESC

/**********************************************Query 10**************************************************************/

	SELECT  p.name , min(c.when_climbed) AS "First"

	FROM	climbed AS c  , 
			participated AS p

	WHERE	c.trip_id = p.trip_id 
			
	GROUP BY p.name 

	having min(c.when_climbed) >= '2003-06-01 00:00:00.000'

/**************************************************Query 11******************************************************************/

CREATE VIEW person_peak AS 
SELECT Distinct p2.name , c1.peak 
			FROM	peak AS p1 ,
					participated AS p2,
					climbed AS c1
			WHERE	c1.trip_id = p2.trip_id	

CREATE VIEW peak_times AS 
SELECT temp.peak,COUNT (*) AS "times"
FROM	person_peak AS temp , 		
		peak AS p
WHERE	p.name = temp.peak  
GROUP BY temp.peak 


SELECT p1.region, sum(p2.times)
FROM	peak AS p1,
		peak_times AS p2
WHERE	p1.name = p2.peak 	
GROUP BY p1.region

/***************************************************************************************************************************/


/************************************************Query 12*******************************************************************/

CREATE VIEW person_peak AS 
SELECT Distinct p2.name , c1.peak 
			FROM	peak AS p1 ,
					participated AS p2,
					climbed AS c1
			WHERE	c1.trip_id = p2.trip_id	


SELECT avg(p2.elev) AS "Avg Height"
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
SELECT Distinct p2.name , c1.peak , p2.TRIP_ID
			FROM	peak AS p1 ,
					participated AS p2,
					climbed AS c1
			WHERE	c1.trip_id = p2.trip_id	

SELECT Distinct p2.name
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

	and not exists (	
						SELECT p3.peak
						FROM person_peak AS p3
						WHERE p3.name = p2.name 

						Except
					
						SELECT p1.peak
						FROM person_peak AS p1
						WHERE p1.name = 'BARBARA' 						
						
				)
			
	and p2.name != 'BARBARA'

/******************************************************************************************************/



/*************************************************Query 14*********************************************/

CREATE VIEW person_peak AS 
SELECT Distinct p2.name , c1.peak , p2.TRIP_ID
			FROM	participated AS p2,
					climbed AS c1
			WHERE	c1.trip_id = p2.trip_id	


select distinct  temp.peak as "Peak" ,  temp.name as "Climber"
from person_peak as temp 
where not exists(	

				select  temp2.trip_id
				from	person_peak as temp2
				where	temp2.peak = temp.peak 
				
				except

				select	temp1.trip_id
				from	person_peak as temp1
				where	temp1.name = temp.name and
						temp1.peak = temp.peak			
				
				) 


/**********************************************************************************************************/




/***************************************************Query 15***********************************************/


select Top(1) c1.WHEN_CLIMBED as "Start Date", c2.WHEN_CLIMBED as "End Date" , 
				datediff(DAY,c1.WHEN_CLIMBED,c2.WHEN_CLIMBED ) as "Span"
from climbed as c1 , climbed as c2 
where c1.WHEN_CLIMBED = (
							select top (1)temp1.when_climbed 
							from		climbed as temp1, climbed as temp2
							where		(	temp1.PEAK != temp2.PEAK
											or	temp1.TRIP_ID != temp2.TRIP_ID
										--	or	temp1.WHEN_CLIMBED != temp2.WHEN_CLIMBED	
										)
									and datediff(DAY,temp1.when_climbed, temp2.when_climbed )<= 90
							group by temp1.when_climbed
							order by count(*) desc
						)

	and datediff(DAY,c1.when_climbed, c2.when_climbed )<= 90
	order by c2.WHEN_CLIMBED DESC


/*********************************************************************************************************/