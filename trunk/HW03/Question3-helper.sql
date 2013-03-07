ALTER FUNCTION longestSequence
	 (	@fPerson VARCHAR(8000),
		@sPerson VARCHAR(8000)
	 )
RETURNS INT
WITH SCHEMABINDING AS
BEGIN
	Declare @fPeaks VARCHAR (8000);
	Declare @sPeaks VARCHAR (8000);

	DECLARE @fPeaksTable table ( peakId int PRIMARY KEY IDENTITY, peak VARCHAR (8000));
	DECLARE @sPeaksTable table ( peakId int PRIMARY KEY IDENTITY, peak VARCHAR (8000));

	---
			---   Copying peaks of the first person into the table  ---
	---
	insert into @fPeaksTable 
			select  c.PEAK 
			from	dbo.PARTICIPATED as p, dbo.CLIMBED  as c
			where   p.trip_id = c.trip_id and p.name = @fPerson
			order by c.WHEN_CLIMBED	 ;
		
	---
			---   Copying peaks of the Second person into the table  ---
	---
	insert into @sPeaksTable
			select  c.PEAK 
			from	dbo.PARTICIPATED as p, dbo.CLIMBED  as c
			where p.TRIP_ID = c.TRIP_ID and p.NAME = @sPerson
			order by c.WHEN_CLIMBED	;

	--
		-- Working on the peaks information of both the climber
	--
	DECLARE @fPeakClimbedCount INT;
	DECLARE @sPeakClimbedCount INT;
	DECLARE @longestSequencePeakTable table ( i INT,  j INT, value INT);
	DECLARE @longestSequence INT;

	DECLARE @i INT;
	DECLARE @j INT;
	SET @i = 0;
 	SET @j = 0;

	SET @fPeakClimbedCount = ( select COUNT(*) from @fPeaksTable);
	SET @sPeakClimbedCount = ( select COUNT(*) from @sPeaksTable);

	
	IF ( @fPeakClimbedCount = 0  ) or (@sPeakClimbedCount = 0)
		BEGIN
			RETURN 0
		END
	ELSE
		BEGIN
				--INSERT INTO ##ClimberInfo VALUES (@fPerson,@sPerson);
				WHILE (@i<= @fPeakClimbedCount)
				  BEGIN		
						INSERT INTO @longestSequencePeakTable VALUES (@i,0,0);
						SET @i = @i + 1;
				  END
  				WHILE (@j<= @sPeakClimbedCount)
				  BEGIN		
						INSERT INTO @longestSequencePeakTable VALUES (0,@j,0);
						SET @j = @j + 1;
				  END

				SET @i = 1;
				SET @j = 1;

				DECLARE @tempValue INT;
				DECLARE @tempPeak1 VARCHAR(8000);
				DECLARE @tempPeak2 VARCHAR(8000);
				DECLARE @value1 INT;
				DECLARE @value2 INT;
				DECLARE @value3 INT;

				WHILE (@i<= @fPeakClimbedCount)
				BEGIN	
				  SET @tempPeak1 = (SELECT peak from @fPeaksTable where peakId = @i);
				  WHILE (@j<= @sPeakClimbedCount)
					BEGIN
						SET @tempPeak2 = (SELECT peak from @sPeaksTable where peakId = @j);
						if (@tempPeak1 = @tempPeak2)
							BEGIN
								SET @value1 = (	SELECT Top(1) value FROM @longestSequencePeakTable WHERE i = @i-1 and j = @j-1 );
								SET @value1 = @value1 +1 ;
								INSERT INTO @longestSequencePeakTable VALUES (@i,@j,@value1);
							END
						ELSE
							BEGIN
								SET @value2 = (	SELECT Top(1) value FROM @longestSequencePeakTable WHERE i = @i and j = @j-1 );
								SET @value3 = (	SELECT Top(1) value FROM @longestSequencePeakTable WHERE i = @i-1 and j = @j );
								IF (@value2 < @value3)
									BEGIN
										INSERT INTO @longestSequencePeakTable VALUES (@i,@j,@value3);
										
									END
								ELSE
									BEGIN
										INSERT INTO @longestSequencePeakTable VALUES (@i,@j,@value2);
									END
							END
						SET @j = @j + 1;
					END
				  SET @j = 1;
				  SET @i = @i + 1;
				END

			SET @longestSequence = (SELECT Top (1) value FROM @longestSequencePeakTable WHERE i = @fPeakClimbedCount and j = @sPeakClimbedCount ); 
		END
	--EXECUTE dbo.CommonSequence @fPerson = @fPerson, @sPerson = @sPerson;
	return @longestSequence;
END

