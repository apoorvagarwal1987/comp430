ALTER PROCEDURE CommonSequence
	@fPerson VARCHAR(8000),
	@sPerson VARCHAR(8000)	
	AS
	BEGIN
	DECLARE @fPeaks VARCHAR (8000);
	DECLARE @sPeaks VARCHAR (8000);

	DECLARE @fPeaksTABLE TABLE ( peakId int PRIMARY KEY IDENTITY, peak VARCHAR (8000));
	DECLARE @sPeaksTABLE TABLE ( peakId int PRIMARY KEY IDENTITY, peak VARCHAR (8000));
	DECLARE @sequenceTABLE TABLE (seqId int PRIMARY KEY IDENTITY, peak VARCHAR(8000), i INT, j INT);

	---
			---   Copying peaks of the first person INTO the TABLE  ---
	---
	INSERT INTO @fPeaksTABLE 
			SELECT  c.PEAK 
			FROM	dbo.PARTICIPATED as p, dbo.CLIMBED  as c
			WHERE   p.trip_id = c.trip_id and p.name = @fPerson
			ORDER by c.WHEN_CLIMBED	 ;
		
	---
			---   Copying peaks of the Second person INTO the TABLE  ---
	---
	INSERT INTO @sPeaksTABLE
			SELECT  c.PEAK 
			FROM	dbo.PARTICIPATED as p, dbo.CLIMBED  as c
			WHERE p.TRIP_ID = c.TRIP_ID and p.NAME = @sPerson
			ORDER by c.WHEN_CLIMBED	;

	--
		-- Working on the peaks information of both the climber
	--
	DECLARE @fPeakClimbedCount INT;
	DECLARE @sPeakClimbedCount INT;
	DECLARE @longestSequencePeakTABLE TABLE ( i INT,  j INT, value INT);
	DECLARE @longestSequence INT;

	DECLARE @i INT;
	DECLARE @j INT;
	SET @i = 0;
 	SET @j = 0;

	SET @fPeakClimbedCount = ( SELECT COUNT(*) FROM @fPeaksTABLE);
	SET @sPeakClimbedCount = ( SELECT COUNT(*) FROM @sPeaksTABLE);


	--INSERT INTO ##ClimberInfo VALUES (@fPerson,@sPerson);
	WHILE (@i<= @fPeakClimbedCount)
		BEGIN		
			INSERT INTO @longestSequencePeakTABLE VALUES (@i,0,0);
			SET @i = @i + 1;
		END
  	WHILE (@j<= @sPeakClimbedCount)
		BEGIN		
			INSERT INTO @longestSequencePeakTABLE VALUES (0,@j,0);
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
	DECLARE @tempPeak VARCHAR(8000);

	WHILE (@i<= @fPeakClimbedCount)
	BEGIN	
		SET @tempPeak1 = (SELECT peak FROM @fPeaksTABLE WHERE peakId = @i);
		WHILE (@j<= @sPeakClimbedCount)
		BEGIN
			SET @tempPeak2 = (SELECT peak FROM @sPeaksTABLE WHERE peakId = @j);
			IF (@tempPeak1 = @tempPeak2)
				BEGIN
					SET @value1 = (	SELECT Top(1) value FROM @longestSequencePeakTABLE WHERE i = @i-1 and j = @j-1 );
					SET @value1 = @value1 + 1 ;
					INSERT INTO @longestSequencePeakTABLE VALUES (@i,@j,@value1);

					DECLARE peakResult CURSOR FOR
						SELECT peak
						FROM @sequenceTABLE 
						WHERE i = @i - 1 and j = @j - 1;
											
					OPEN peakResult;
					FETCH peakResult INTO @tempPeak;
					WHILE (@@FETCH_STATUS = 0 )
						BEGIN
							--PRINT 'INSERTing  ' + @tempPeak + '  ' + cast(@i as varchar(10)) + '   '+ cast(@j as varchar(10)) ;
							INSERT INTO @sequenceTABLE VALUES (@tempPeak,@i,@j);
							FETCH peakResult INTO @tempPeak;
						END
					CLOSE peakResult;
					DEALLOCATE peakResult;

					INSERT INTO @sequenceTABLE VALUES (@tempPeak1,@i,@j);
					--PRINT 'Common Peak :  ' + @tempPeak1;
				END
			ELSE
				BEGIN
					SET @value2 = (	SELECT Top(1) value FROM @longestSequencePeakTABLE WHERE i = @i and j = @j-1 );
					SET @value3 = (	SELECT Top(1) value FROM @longestSequencePeakTABLE WHERE i = @i-1 and j = @j );
					IF (@value2 < @value3)
						BEGIN
							INSERT INTO @longestSequencePeakTABLE VALUES (@i,@j,@value3);	
							DECLARE peakResult CURSOR FOR
								SELECT peak
								FROM @sequenceTABLE 
								WHERE i = @i - 1 and j = @j;									
						END
					ELSE
						BEGIN
							INSERT INTO @longestSequencePeakTABLE VALUES (@i,@j,@value2);
							DECLARE peakResult CURSOR FOR
								SELECT peak
								FROM @sequenceTABLE 
								WHERE i = @i and j = @j - 1;									
						END

					OPEN peakResult;
					FETCH peakResult INTO @tempPeak;
					WHILE (@@FETCH_STATUS = 0 )
						BEGIN
							--PRINT 'INSERTing  ' + @tempPeak + '  ' + cast(@i as varchar(10)) + '   '+ cast(@j as varchar(10)) ;
							INSERT INTO @sequenceTABLE VALUES (@tempPeak,@i,@j);
							FETCH peakResult INTO @tempPeak;
						END
					CLOSE peakResult;
					DEALLOCATE peakResult;
				END
			SET @j = @j + 1;
		END
		SET @j = 1;
		SET @i = @i + 1;
	END
		
	--PRINT 'Common Peak :  ' +  cast(@fPeakClimbedCount as varchar(10)) + '     ' +  cast(@sPeakClimbedCount as varchar(10));
	DECLARE myResult CURSOR FOR
			SELECT peak
			FROM @sequenceTABLE 
			WHERE i = @fPeakClimbedCount and j = @sPeakClimbedCount
			ORDER by seqId;

	OPEN myResult;
	FETCH myResult INTO @tempPeak;
	WHILE (@@FETCH_STATUS =0 )
		BEGIN
			--INSERT INTO @sequenceTABLE VALUES (@tempPeak,@i,@j);
			--FETCH peakResult INTO @tempPeak;
			PRINT @tempPeak;
			FETCH myResult INTO @tempPeak;
		END
	CLOSE myResult;
	DEALLOCATE myResult;
	END
	