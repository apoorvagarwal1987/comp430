ALTER PROCEDURE Bacon
	@climber VARCHAR(8000)
	AS
	BEGIN
		DECLARE @baconTABLE TABLE ( name VARCHAR (8000),number INT);
		INSERT INTO @baconTABLE VALUES (@climber,0);

		DECLARE @tripId INT;
		DECLARE @personName VARCHAR (8000);
		DECLARE @bDistance INT;

		DECLARE @i INT;
		SET @i = 0 ;

		WHILE (@i <=6)
		BEGIN
			INSERT INTO @baconTABLE
			SELECT name , @i+1
			FROM PARTICIPATED 
			WHERE TRIP_ID in (
							SELECT TRIP_ID
							FROM PARTICIPATED 
							WHERE name in (
												SELECT name 
												FROM @baconTABLE 
												WHERE number = @i
											)	
						)
			and name not in (SELECT name FROM @baconTABLE) ;

			SET @i = @i + 1;
		END

		DECLARE resultSet CURSOR FOR 
			SELECT DISTINCT name , number
			FROM @baconTABLE
			ORDER BY number;

		OPEN resultSet ;
		FETCH resultSet INTO @personName , @bDistance;
		PRINT 'PERSON NAME '+ ' DISTANCE ' ;
		WHILE (@@FETCH_STATUS = 0)
		BEGIN
			--PRINT ' PERSON NAME: ' + @personName + ' DISTANCE FROM ' + @climber + ' IS ' + cast(@bDistance as varchar(10));
			PRINT @personName + '        '  + cast(@bDistance as varchar(10));
			FETCH resultSet INTO @personName , @bDistance;
		END
		CLOSE resultSet;
		DEALLOCATE resultSet;
	END