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
	SET @maxSequence = 0;

	DECLARE @SequencePeakTable table ( fClimber VARCHAR(8000),sClimber VARCHAR(8000) ,value INT);

	CREATE table ##SequenceInfo (seqid INT PRIMARY KEY , peak VARCHAR(8000));
	CREATE table ##ClimberInfo (seqid INT PRIMARY KEY , fClimber VARCHAR(8000),sClimber VARCHAR(8000))

	OPEN climberPairList;
	Fetch climberPairList into @fClimber , @sClimber;
	WHILE (@@FETCH_STATUS = 0)
	BEGIN	
		SET @maxSequence = (select dbo.longestSequence(@fClimber,@sClimber));
		insert into @SequencePeakTable values (@fClimber,@sClimber,@maxSequence);
		Fetch climberPairList into @fClimber , @sClimber;
	END



	close climberPairList
	deallocate climberPairList

end



--exec FindMostSimilar