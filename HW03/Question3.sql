-- *****************Assignmnet 3**********************------------
---Question 3---

alter procedure FindMostSimilar

	as
	begin
	
	declare climberPairList  Cursor for 
		select c1.name , c2.name
			from  climber as c1 , CLIMBER as c2
			where c1.NAME < c2.name ;
	
	declare @fClimber varchar (8000);
	declare @sClimber varchar (8000);

	declare @maxSequence INT;
	SET @maxSequence = 0;

	DECLARE @SequencePeakTable table ( fClimber VARCHAR(8000),sClimber VARCHAR(8000) ,value INT);

	--CREATE table ##SequenceInfo (seqid INT PRIMARY KEY , peak VARCHAR(8000));
	--CREATE table ##ClimberInfo (seqid INT PRIMARY KEY , fClimber VARCHAR(8000),sClimber VARCHAR(8000))

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
	
	Declare @mfClimber VARCHAR(8000);
	Declare @msClimber VARCHAR(8000);
	Declare @peaksClimbed INT;

	--SET @mfClimber = (select TOP(1) fClimber from @SequencePeakTable order by value DESC);
	--SET @msClimber = (select TOP(1) sClimber from @SequencePeakTable order by value DESC);
	SET @peaksClimbed = (select TOP(1) value from @SequencePeakTable order by value DESC);

	DECLARE resultSet CURSOR FOR
		SELECT fClimber,sClimber
		FROM @SequencePeakTable
		WHERE value = @peaksClimbed;
	
	OPEN resultSet;
	FETCH resultSet into @mfClimber,@msClimber;

	WHILE (@@FETCH_STATUS = 0)
	BEGIN
		PRINT 'The two most similar climbers are ' + @mfClimber + ' and  ' + @msClimber + ' common peaks between them are:' + cast(@peaksClimbed as varchar(10));
		PRINT 'The longest sequence of peak ascents common to both is: '
		EXECUTE dbo.CommonSequence @fPerson = @mfClimber, @sPerson = @msClimber;
		FETCH resultSet into @mfClimber,@msClimber;
	END

	CLOSE resultSet;
	DEALLOCATE resultSet;
END

