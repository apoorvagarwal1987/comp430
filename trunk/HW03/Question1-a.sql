-- *****************Assignmnet 3**********************------------
---Question 1---

--Trigger 1

 ALTER  TRIGGER trigClosestDays
 on Climbed  
 for insert
 as
 begin
 	if @@ROWCOUNT = 0
		return

	Declare myRes Cursor for
		SELECT  Top (1) i.trip_id, abs(datediff(day,c.when_climbed,i.when_climbed)) as "diff"
		from	inserted  as i,
				climbed as c
		where c.trip_id = i.trip_id and c.PEAK <> i.PEAK 
		order by abs(datediff(day,c.when_climbed,i.when_climbed));

	DECLARE @tripID INT;
	DECLARE @Diff INT;
		
	open myRes ;
	Fetch myRes into @tripID, @Diff
	close myRes;
	Deallocate myRes;

	if @Diff > 20
	begin
		print 'WARNING: The climb you inserted is days ' + cast(@Diff as varchar(10)) + ' from the closest existing climb in trip ' +  cast(@tripId as varchar(10))
		return 
	end

end