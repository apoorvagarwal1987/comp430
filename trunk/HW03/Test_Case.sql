use master;

-- Test Case 1

delete from CLIMBED

select Count(*) from CLIMBED

 INSERT INTO climbed VALUES (1, 'Kearsarge Peak', '06/28/2002');
INSERT INTO climbed VALUES (6, 'Mount Guyot', '06/21/2002');
INSERT INTO climbed VALUES (23, 'Lion Rock', '08/09/2004');
INSERT INTO climbed VALUES (23, 'Mount Williamson', '06/09/2004');
INSERT INTO climbed VALUES (29, 'Lion Rock', '06/09/2004');





select min(temp.Closest)
	from (select abs(datediff(day,WHEN_CLIMBED,'2004-06-09 00:00:00.000')) as "Closest"
			from climbed 
			where TRIP_ID = 29
			) as temp			




