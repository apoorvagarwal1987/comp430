-- *****************Assignmnet 3**********************------------
---Question 1---

--Trigger 1



create  trigger trigClosestDays
 on Climbed  
 after insert
 as
 begin
 	if @@ROWCOUNT = 0
		return

	declare @minDiff INT;
	/*
	set @minDiff = (	
					select min(temp.closest)
					from 
							(select abs(DATEDIFF(day,d.WHEN_CLIMBED,i.WHEN_CLIMBED)) as "closest"
								from		
									inserted as i,
								    climbed as d
								where 
									i.TRIP_ID = d.TRIP_ID 
							) as temp
				);*/

	set @minDiff = (	
					select temp.diff
					from (
						select top(1) abs(datediff(day,c.when_climbed,i.when_climbed)) as "diff"
						from Climbed as c , inserted as i
						where c.trip_id = i.trip_id
						order by abs(datediff(day,c.when_climbed,i.when_climbed))	
						) as temp
				);

	print 'WARNING: The climb you inserted is days ' + cast(@minDiff as varchar(10)) + ' from the closest existing climb in trip '
	print getdate()

	if @minDiff < 20
	begin
		print 'you are cool'
		return 
	end

end