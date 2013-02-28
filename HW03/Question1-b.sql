-- *****************Assignmnet 3**********************------------
---Question 1---

--Trigger 2
 alter  TRIGGER trigDelParticipated
 on Participated  
 after delete
 as
 begin
 	if @@ROWCOUNT = 0
		return

	delete 
	from climbed
	where TRIP_ID not in (
							select TRIP_ID
							from PARTICIPATED	
							)
 end
