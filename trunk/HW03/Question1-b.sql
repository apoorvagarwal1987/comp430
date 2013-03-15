-- *****************Assignmnet 3**********************------------
---Question 1---

--Trigger 2
 CREATE  TRIGGER trigDelParticipated
 ON Participated  
 AFTER DELETE
 AS
 BEGIN
 	IF @@ROWCOUNT = 0
		RETURN

	DELETE 
	FROM climbed
	WHERE TRIP_ID not in (
							SELECT TRIP_ID
							FROM PARTICIPATED	
							)
 END
