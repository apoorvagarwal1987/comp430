use master;

-- Test Case 1
--part (a)

delete FROM CLIMBED
delete FROM PARTICIPATED

drop trigger trigClosestDays
drop trigger trigINSERTClimbed
drop trigger trigDelParticipated

SELECT Count(*) FROM CLIMBED

INSERT INTO climbed VALUES (1, 'Kearsarge Peak', '06/28/2002');
INSERT INTO climbed VALUES (6, 'Mount Guyot', '06/21/2002');
INSERT INTO climbed VALUES (23, 'Lion Rock', '08/09/2004');
INSERT INTO climbed VALUES (23, 'Mount Williamson', '06/09/2004');
INSERT INTO climbed VALUES (29, 'Lion Rock', '06/09/2004');

SELECT min(temp.Closest)
	FROM (SELECT abs(datediff(day,WHEN_CLIMBED,'2004-08-09 00:00:00.000')) as "Closest"
			FROM climbed 
			WHERE TRIP_ID = 29
			) as temp			


-- Test Case 1
--part (b)


delete FROM CLIMBED
delete FROM PARTICIPATED

drop table CLIMBED
drop table PARTICIPATED


DELETE FROM participated WHERE trip_id = 12; 
SELECT COUNT(*) FROM climbed WHERE trip_id = 12;

DELETE FROM participated WHERE trip_id = 13 AND name <> 'ELIZABETH'; 
SELECT COUNT(*) FROM climbed WHERE trip_id = 13;

DELETE FROM participated WHERE name = 'ELIZABETH';
 SELECT COUNT(*) FROM climbed WHERE trip_id = 13;

SELECT COUNT (DISTINCT trip_id) FROM climbed;
DELETE FROM participated WHERE trip_id IN (SELECT trip_id FROM participated WHERE name = 'LINDA'); 
SELECT COUNT (DISTINCT trip_id) FROM climbed;

SELECT * FROM PARTICIPATED WHERE TRIP_ID = 13
SELECT *  FROM CLIMBED WHERE TRIP_ID = 13




-- Test Case 2
delete FROM CLIMBED
drop TABLE CLIMBED
drop trigger trigINSERTClimbed
SELECT * FROM CLIMBED
SELECT * FROM ed_cutoff

SELECT dbo.levenshteinDistance('Moses Mount','Moses Mountain')

create TABLE ed_cutoff(cutoff INT);
INSERT INTO ed_cutoff values(3)

INSERT INTO climbed VALUES (30, 'North Guard', '09/06/2002');
INSERT INTO climbed VALUES (30, 'Home Nose', '09/06/2002');
SELECT * FROM climbed WHERE trip_id = 30;


INSERT INTO climbed VALUES (31, 'Moses Mount', '09/06/2002');
INSERT INTO climbed VALUES (31, 'Olancha Mountain', '09/06/2002');
INSERT INTO climbed VALUES (31, 'Mt. Hitchcock', '09/06/2002');
INSERT INTO climbed VALUES (31, 'Mt Hitchcock', '09/06/2002');
INSERT INTO climbed VALUES (31, 'Milestoan Mounten', '09/06/2002');
INSERT INTO climbed VALUES (31, 'Milestoan Mountan', '09/06/2002');
SELECT * FROM climbed WHERE trip_id = 31;


DROP TABLE ed_cutoff;
CREATE TABLE ed_cutoff (cutoff INT);
INSERT INTO ed_cutoff VALUES (2);



INSERT INTO climbed VALUES (32, 'Piket Gard Peak', '09/06/2002');
INSERT INTO climbed VALUES (32, 'Milestoan Mounten', '09/06/2002');
INSERT INTO climbed VALUES (32, 'Milestoan Mountain', '09/06/2002');
SELECT * FROM climbed WHERE trip_id = 32;


-- Test Case 4


SET NOCOUNT ON
EXECUTE Bacon 'EDWARD';

SET NOCOUNT ON
EXECUTE Bacon 'PAUL';

SET NOCOUNT ON
EXECUTE Bacon 'MARY';

SET NOCOUNT ON
EXECUTE Bacon 'ROBERT';

SET NOCOUNT ON
EXECUTE Bacon 'BETTY';

SELECT distinct trip_id FROM PARTICIPATED WHERE name in (
SELECT distinct name 
	FROM PARTICIPATED
	WHERE TRIP_ID in (SELECT distinct trip_id FROM PARTICIPATED WHERE name in (
SELECT name 
	FROM PARTICIPATED
	WHERE TRIP_ID in (SELECT distinct trip_id FROM PARTICIPATED WHERE name in (
SELECT distinct name 
	FROM PARTICIPATED
	WHERE TRIP_ID in (SELECT distinct trip_id FROM PARTICIPATED WHERE name in (
SELECT name 
	FROM PARTICIPATED
	WHERE TRIP_ID = 16)))))))
	