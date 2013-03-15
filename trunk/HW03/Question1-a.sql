-- *****************Assignmnet 3**********************------------
---Question 1---

--Trigger 1

 CREATE  TRIGGER trigClosestDays
 ON Climbed  
 FOR INSERT
 AS
 BEGIN
 	IF @@ROWCOUNT = 0
		RETURN

	DECLARE myRes Cursor for
		SELECT  Top (1) i.trip_id, abs(datediff(day,c.when_climbed,i.when_climbed)) as "diff"
		FROM	INSERTed  as i,
				climbed as c
		WHERE c.trip_id = i.trip_id and c.PEAK <> i.PEAK 
		ORDER by abs(datediff(day,c.when_climbed,i.when_climbed));

	DECLARE @tripID INT;
	DECLARE @Diff INT;
		
	OPEN myRes ;
	FETCH myRes INTO @tripID, @Diff
	CLOSE myRes;
	Deallocate myRes;

	IF @Diff > 20
	BEGIN
		PRINT 'WARNING: The climb you INSERTED is days ' + cast(@Diff as varchar(10)) + ' FROM the closest existing climb in trip ' +  cast(@tripId as varchar(10))
		RETURN 
	END

END