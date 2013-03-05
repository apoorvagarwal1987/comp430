use master;

-- Test Case 1
--part (a)

delete from CLIMBED
delete from PARTICIPATED

select Count(*) from CLIMBED

 INSERT INTO climbed VALUES (1, 'Kearsarge Peak', '06/28/2002');
INSERT INTO climbed VALUES (6, 'Mount Guyot', '06/21/2002');
INSERT INTO climbed VALUES (23, 'Lion Rock', '08/09/2004');
INSERT INTO climbed VALUES (23, 'Mount Williamson', '06/09/2004');
INSERT INTO climbed VALUES (29, 'Lion Rock', '06/09/2004');

select min(temp.Closest)
	from (select abs(datediff(day,WHEN_CLIMBED,'2004-08-09 00:00:00.000')) as "Closest"
			from climbed 
			where TRIP_ID = 29
			) as temp			


-- Test Case 1
--part (b)


delete from CLIMBED
delete from PARTICIPATED


DELETE FROM participated WHERE trip_id = 12; 
SELECT COUNT(*) FROM climbed WHERE trip_id = 12;

DELETE FROM participated WHERE trip_id = 13 AND name <> 'ELIZABETH'; 
SELECT COUNT(*) FROM climbed WHERE trip_id = 13;

DELETE FROM participated WHERE name = 'ELIZABETH';
 SELECT COUNT(*) FROM climbed WHERE trip_id = 13;

SELECT COUNT (DISTINCT trip_id) FROM climbed;
DELETE FROM participated WHERE trip_id IN (SELECT trip_id FROM participated WHERE name = 'LINDA'); 

SELECT COUNT (DISTINCT trip_id) FROM climbed;

select * from PARTICIPATED where TRIP_ID = 13
select *  from CLIMBED where TRIP_ID = 13




-- Test Case 2


select dbo.levenshteinDistance('sd','sfds')