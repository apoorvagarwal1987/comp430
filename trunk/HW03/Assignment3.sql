-- *****************Assignmnet 3**********************------------
---Question 1---

--Trigger 1

 alter  trigger trigClosestDays
 on Climbed  
 for insert
 as
 begin
 	if @@ROWCOUNT = 0
		return

	Declare myRes Cursor for
		select  i.trip_id, abs(datediff(day,c.when_climbed,i.when_climbed)) as "diff"
		from	inserted  as i,
				climbed as c
		where c.trip_id = i.trip_id and c.PEAK <> i.PEAK 
		order by abs(datediff(day,c.when_climbed,i.when_climbed));

	declare @minDiff INT;
	declare @tripID INT;
	declare @Diff INT;
	declare @minTripId INT;
	set @minDiff = 2147483647;
	
	open myRes ;
	Fetch myRes into @tripID, @Diff
	while (@@FETCH_STATUS = 0)
		begin
			if(@Diff < @minDiff)
				begin
					set @minDiff = @Diff;
					set @minTripId = @tripID;
				end
			print 'Current: ' + cast(@Diff as varchar(10)) + ' Minimum: ' + cast(@minDiff as varchar(10))
			Fetch myRes into @tripID, @Diff
		end

	close myRes;
	Deallocate myRes;

	print 'WARNING: The climb you inserted is days ' + cast(@minDiff as varchar(10)) + ' from the closest existing climb in trip '
	print getdate()

	if @minDiff < 20
	begin
		print 'you are cool'
		return 
	end

end