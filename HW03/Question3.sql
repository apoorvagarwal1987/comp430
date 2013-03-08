-- *****************Assignmnet 3**********************------------
---Question 3---

alter procedure FindMostSimilar

	as
	BEGIN
	
	DECLARE climberPairList  Cursor for 
		SELECT c1.name , c2.name
			FROM  climber as c1 , CLIMBER as c2
			WHERE c1.NAME < c2.name ;
	
	DECLARE @fClimber varchar (8000);
	DECLARE @sClimber varchar (8000);

	DECLARE @maxSequence INT;
	SET @maxSequence = 0;

	DECLARE @SequencePeakTABLE TABLE ( fClimber VARCHAR(8000),sClimber VARCHAR(8000) ,value INT);

	--CREATE TABLE ##SequenceInfo (seqid INT PRIMARY KEY , peak VARCHAR(8000));
	--CREATE TABLE ##ClimberInfo (seqid INT PRIMARY KEY , fClimber VARCHAR(8000),sClimber VARCHAR(8000))

	OPEN climberPairList;
	FETCH climberPairList INTO @fClimber , @sClimber;
	WHILE (@@FETCH_STATUS = 0)
	BEGIN	
		SET @maxSequence = (SELECT dbo.longestSequence(@fClimber,@sClimber));
		INSERT INTO @SequencePeakTABLE values (@fClimber,@sClimber,@maxSequence);
		FETCH climberPairList INTO @fClimber , @sClimber;
	END
	CLOSE climberPairList
	DEALLOCATE climberPairList
	
	DECLARE @mfClimber VARCHAR(8000);
	DECLARE @msClimber VARCHAR(8000);
	DECLARE @peaksClimbed INT;

	--SET @mfClimber = (SELECT TOP(1) fClimber FROM @SequencePeakTABLE ORDER by value DESC);
	--SET @msClimber = (SELECT TOP(1) sClimber FROM @SequencePeakTABLE ORDER by value DESC);
	SET @peaksClimbed = (SELECT TOP(1) value FROM @SequencePeakTABLE ORDER by value DESC);

	DECLARE resultSet CURSOR FOR
		SELECT fClimber,sClimber
		FROM @SequencePeakTABLE
		WHERE value = @peaksClimbed;
	
	OPEN resultSet;
	FETCH resultSet INTO @mfClimber,@msClimber;

	WHILE (@@FETCH_STATUS = 0)
	BEGIN
		PRINT 'The two most similar climbers are ' + @mfClimber + ' and  ' + @msClimber + ' common peaks between them are:' + cast(@peaksClimbed as varchar(10));
		PRINT 'The longest sequence of peak ascents common to both is: '
		EXECUTE dbo.CommonSequence @fPerson = @mfClimber, @sPerson = @msClimber;
		FETCH resultSet INTO @mfClimber,@msClimber;
	END

	CLOSE resultSet;
	DEALLOCATE resultSet;
END

