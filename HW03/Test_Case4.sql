SELECT * FROM PARTICIPATED

SELECT trip_id
	FROM PARTICIPATED
	WHERE name = 'BETTY'

SELECT trip_id
	FROM PARTICIPATED
	WHERE name = 'KENNETH'
	
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
	
SELECT * FROM PARTICIPATED

SELECT distinct name
FROM PARTICIPATED

create  TABLE bTABLE (name VARCHAR (8000),number INT);

delete FROM bTABLE
drop TABLE bTABLE
SELECT * FROM bTABLE
INSERT INTO bTABLE values ('BETTY',0)

INSERT INTO bTABLE
	SELECT name , 3
	FROM PARTICIPATED 
	WHERE TRIP_ID in (
					SELECT TRIP_ID
					FROM PARTICIPATED 
					WHERE name in (
										SELECT name 
										FROM bTABLE 
										WHERE number = 2
									)	
				)
	and name not in (SELECT name FROM bTABLE) 


	SELECT distinct name, number FROM bTABLE ORDER by number



