use master;


/************************************************Query : 1***************************************************/
select Distinct(c1.name)
from climber as c1 ,  participated as p , climbed as c2, peak as p1
where c1.sex ='F' and p.name = c1.name and p.trip_id = c2.trip_id and c2.peak = p1.name and p1.elev >= 14000
/************************************************************************************************************/


/************************************************Query : 2***************************************************/
select peak
from CLIMBED as c1
group by peak
having (count(peak)=1)
/************************************************************************************************************/

/************************************************Query : 3***************************************************/

select Distinct(oc1.NAME)

from	CLIMBER as oc1 , 
		PARTICIPATED as op1, 
		CLIMBED as oc2

where	oc1.NAME = op1.NAME and 
		op1.TRIP_ID = oc2.TRIP_ID and 
		oc2.PEAK in 	 
					(
						select Distinct(ic1.PEAK)

						from	CLIMBED as ic1 , 
								PARTICIPATED as ip1, 
								CLIMBER as ic2	

						where	ic2.NAME ='Patricia' and
								ic2.NAME = ip1.NAME and ip1.TRIP_ID = ic1.TRIP_ID 

					)


/************************************************************************************************************/


/************************************************Query : 4***************************************************/





/************************************************************************************************************/



/************************************************Query : 5***************************************************/


 



/************************************************************************************************************/



/************************************************Query : 6***************************************************/

select p1.NAME
from peak as p1
where p1.ELEV >= 14000

/************************************************************************************************************/



/************************************************Query : 7***************************************************/
select * 
from peak 
where name in (
				select name from peak
				except
				select peak from CLIMBED
			 ) 
	and  DIFF = 5

/************************************************************************************************************/



/************************************************Query : 8***************************************************/
select Map

from peak 

group by MAP

having ( MAX(Elev)- MIN(ELEV)) >= 2000

/************************************************************************************************************/


/************************************************Query : 9***************************************************/

create view person_climbed 
as
	select name, peak, when_climbed
	from CLIMBED as c1, PARTICIPATED as p1
	where c1.TRIP_ID = p1.TRIP_ID

		
select *
from person_climbed as p2 
where p2.when_climbed = 
						select min(when_climbed) 
						from person_climbed as p1
						group by peak
						where p1.peak = p2.peak
		

	create view peak_climbed as
	select c.peak , count(*) as "Peak Climbed"	
	from	climbed as c  , 
			participated as p
	where	c.trip_id = p.trip_id
	group by c.peak 
	order by count(*) DESC

	create view earliest_peak_climbed as
	select  c.peak, min(c.when_climbed) as "Earliest"
	from	climbed as c  , 
			participated as p
	where	c.trip_id = p.trip_id
	group by c.peak 
	
	create view person_earliest_peak_climbed as
	select Distinct(c.peak), p.name
		from climbed as c , earliest_peak_climbed as e, participated as p
		where	c.peak = e.peak and 
				e.Earliest = c.when_climbed and 
				p.trip_id = c.trip_id
					


	select c.peak , p1.name, count(*)	
	from	climbed as c  , 
			participated as p1,
			person_earliest_peak_climbed as p2

	where	c.trip_id = p1.trip_id and 
			p2.name = p1.name and
			c.peak = p2.peak

	group by c.peak , p1.name
	order by count(*) desc


/**********************************************Query 10**************************************************************/

	select  p.name , min(c.when_climbed) as "First"

	from	climbed as c  , 
			participated as p

	where	c.trip_id = p.trip_id 
			
	group by p.name 

	having min(c.when_climbed) >= '2003-06-01 00:00:00.000'






/**************************************************Query 11******************************************************************/

create view person_peak as 
select Distinct p2.name , c1.peak 
			from	peak as p1 ,
					participated as p2,
					climbed as c1
			where	c1.trip_id = p2.trip_id	

create view peak_times as 
select temp.peak,count (*) as "times"
from	person_peak as temp , 		
		peak as p
where	p.name = temp.peak  
group by temp.peak 


select p1.region, sum(p2.times)
from	peak as p1,
		peak_times as p2
where	p1.name = p2.peak 	
group by p1.region

/***************************************************************************************************************************/


/************************************************Query 12*******************************************************************/

create view person_peak as 
select Distinct p2.name , c1.peak 
			from	peak as p1 ,
					participated as p2,
					climbed as c1
			where	c1.trip_id = p2.trip_id	


select avg(p2.elev) as "Avg Height"
from	(
			select	 p1.name 
			from	 peak as p1
			where	 p1. region = 'Corocoran to Whitney'
				Except
			select p2.peak
			from person_peak as p2
		)	as		temp , 
			peak as p2

where temp.name = p2.name 


/***************************************************************************************************************************/


/*************************************************Query 13******************************************************************/

create view person_peak as 
select Distinct p2.name , c1.peak 
			from	peak as p1 ,
					participated as p2,
					climbed as c1
			where	c1.trip_id = p2.trip_id	


select Distinct p2.name
from person_peak as p2 
where not exists (
						select p1.peak
						from person_peak as p1
						where p1.name = 'BARBARA' 
						
						Except
						
						select p3.peak
						from person_peak as p3
						where p3.name = p2.name 
					)

	and not exists (	
						select p3.peak
						from person_peak as p3
						where p3.name = p2.name 

						Except
					
						select p1.peak
						from person_peak as p1
						where p1.name = 'BARBARA' 						
						
				)
			
	and p2.name != 'BARBARA'

/***************************************************************************************************************************/



/*************************************************Query 14*******************************************************************/







/****************************************************************************************************************************/
/************************************************************************************************************/
/************************************************************************************************************/