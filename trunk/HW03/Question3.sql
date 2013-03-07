-- *****************Assignmnet 3**********************------------
---Question 3---

alter procedure FindMostSimilar

	as
	begin
	
	declare climberPairList  Cursor for 
		select Top(1) c1.name , c2.name
			from  climber as c1 , CLIMBER as c2
			where c1.NAME < c2.name ;
	
	declare @fClimber varchar (8000);
	declare @sClimber varchar (8000);
	declare @maxSequence INT;
	set @maxSequence = 0;


	OPEN climberPairList;
	Fetch climberPairList into @fClimber , @sClimber;

	while (@@FETCH_STATUS = 0)

	begin
			
		declare peakListfClimber cursor for
			select  c.PEAK 
				from	PARTICIPATED as p,
						climbed  as c
				where p.TRIP_ID = c.TRIP_ID and p.NAME = @fClimber
				order by c.WHEN_CLIMBED	 ;

		
		declare peakListsClimber cursor for
				select  c.PEAK 
				from	PARTICIPATED as p,
						climbed  as c
				where p.TRIP_ID = c.TRIP_ID and p.NAME = @sClimber
				order by c.WHEN_CLIMBED	 ;

		Fetch climberPairList into @fClimber , @sClimber; 

	end
	
	close climberPairList
	deallocate climberPairList

end



exec FindMostSimilar