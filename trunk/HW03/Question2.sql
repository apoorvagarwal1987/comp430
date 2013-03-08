 CREATE  TRIGGER trigINSERTClimbed
 on Climbed  
 instead of INSERT
 as
 BEGIN
	
 	IF @@ROWCOUNT = 0
		RETURN
 
	DECLARE @INSERTedPeak VARCHAR(8000);
	DECLARE @oldPeak VARCHAR(8000);
	DECLARE @cutoffValue INT;
	DECLARE @distance INT;

	SET @cutoffValue = ( SELECT cutoff FROM ed_cutoff );

	SET @INSERTedPeak = ( SELECT PEAK FROM INSERTed );

	DECLARE peakNames Cursor for
		SELECT  name
			FROM peak;

	DECLARE @peakEditDistance TABLE ( peakName VARCHAR(8000) unique, distance INT );

	OPEN peakNames;
	FETCH peakNames INTO @oldPeak ;

	while (@@FETCH_STATUS = 0)
	BEGIN
		SET @distance = (SELECT dbo.levenshteinDistance(@INSERTedPeak,@oldPeak));
		INSERT INTO @peakEditDistance values (@oldPeak,@distance);
		FETCH peakNames INTO @oldPeak ;
	END

	CLOSE peakNames;
	DEALLOCATE peakNames;
	

	DECLARE resultSet Cursor for
		SELECT  Top(1) peakName , distance
			FROM @peakEditDistance
			ORDER by distance;

	OPEN resultSet;
	FETCH resultSet INTO @oldPeak , @distance;

	IF (@distance = 0)
	BEGIN
		--PRINT 'Success: INSERTed peak name'  + @INSERTedPeak + ' does not match any in the database.' + @oldPeak + 'is used instead'
		INSERT INTO climbed 
			SELECT * FROM INSERTed

		PRINT 'Successfully INSERTed'
		--PRINT 'Peak closest is ' + @oldPeak + '  its distance is : '+ cast(@distance as varchar(10));
	END

	ELSE 
	BEGIN
		IF ( @distance <= @cutoffValue)
			BEGIN
				PRINT 'ERROR: INSERTed peak name '  + @INSERTedPeak + ' does not match any in the database. ' + @oldPeak + ' is used instead'
				DECLARE @date DATE;
				DECLARE @tripId INT;

				SET @date = (	SELECT when_climbed FROM INSERTed );
				SET @tripId = (	SELECT trip_id FROM INSERTed );

				INSERT INTO climbed values (@tripId,@oldPeak,@date)
			END
		ELSE
			BEGIN
				PRINT 'ERROR: INSERTed peak name '  + @INSERTedPeak + ' does not closely match any in the database and so the INSERT is rejected'
			END
	END
	PRINT 'Peak closest is ' + @oldPeak + '  its distance is : '+ cast(@distance as varchar(10));	
	CLOSE resultSet
	DEALLOCATE resultset;

 END


