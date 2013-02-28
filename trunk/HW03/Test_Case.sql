use master;

-- Test Case 1
INSERT INTO climbed VALUES (1, 'Kearsarge Peak', '06/28/2002');
INSERT INTO climbed VALUES (6, 'Mount Guyot', '06/21/2002');
INSERT INTO climbed VALUES (23, 'Lion Rock', '08/09/2004');
INSERT INTO climbed VALUES (23, 'Mount Williamson', '06/09/2004');
INSERT INTO climbed VALUES (29, 'Lion Rock', '06/09/2004');


	select min(temp.closest)
					from 
							(select abs(DATEDIFF(day,d.WHEN_CLIMBED,i.WHEN_CLIMBED)) as "closest"
								from		
									inserted as i,
								    climbed as d
								where 
									i.TRIP_ID = d.TRIP_ID 
							) as temp


select temp.diff
 from (
	select top(1)abs(datediff(day,c.when_climbed,'06/28/2002')) as "diff"
	from Climbed as c
	where c.trip_id = 1
	order by abs(datediff(day,c.when_climbed,'06/28/2002')) desc
	) as temp