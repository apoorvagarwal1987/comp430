ALTER FUNCTION levenshteinDistance
	 (	@newPeak Varchar(8000),
		@oldPeak Varchar(8000)	 
	 )
RETURNS INT
WITH SCHEMABINDING AS
BEGIN  
  DECLARE @distance AS INT ;
  DECLARE @lengthString1 AS INT;
  DECLARE @lengthString2 AS INT;

  SET @lengthString1 = LEN(@newPeak)
  SET @lengthString2 = LEN(@oldPeak)

  IF @lengthString1 = 0
	BEGIN
		RETURN @lengthString2;
	END

  IF @lengthString2 = 0 
	BEGIN
		RETURN @lengthString1;
	END

  DECLARE @i AS INT;
  DECLARE @j AS INT;

  SET @i = 0;
  SET @j = 0;
  SET @distance = 0;

  DECLARE @distanceTABLE TABLE ( i INT,  j INT, value INT);

  WHILE (@i<= @lengthString1)
  BEGIN		
		INSERT INTO @distanceTABLE VALUES (@i,0,@i);
		SET @i = @i + 1;
  END
  

  WHILE (@j<= @lengthString2)
  BEGIN		
		INSERT INTO @distanceTABLE VALUES (0,@j,@j);
		SET @j = @j + 1;
  END

  SET @i = 1;
  SET @j = 1;

  DECLARE @tempValue INT;
  
  -- OLD Peak is identified by j
  -- NEW PEAK is identified by i

  WHILE (@i<= @lengthString1)
	BEGIN	
	  WHILE (@j<= @lengthString2)
		BEGIN	
			IF CHAR(ASCII(SUBSTRING(@newPeak,@i,@i+1))) = CHAR(ASCII(SUBSTRING(@oldPeak,@j,@j+1)))
				BEGIN
					SET @tempValue = (	SELECT Top(1) value FROM @distanceTABLE WHERE i = @i-1 and j = @j-1 ); 
					INSERT INTO @distanceTABLE VALUES(@i,@j,@tempValue);				
				END
			ELSE
				BEGIN
					
					DECLARE @value1 INT;
					DECLARE @value2 INT;
					DECLARE @value3 INT;			
					
					SET @value1 = (	SELECT Top(1) value FROM @distanceTABLE WHERE i = @i-1 and j = @j-1 );
					SET @value2 = (	SELECT Top(1) value FROM @distanceTABLE WHERE i = @i and j = @j-1 );
					SET @value3 = (	SELECT Top(1) value FROM @distanceTABLE WHERE i = @i-1 and j = @j );

					--SET @tempValue = (	SELECT dbo.findMinimum(@value1,@value2,@value3)	);

					SET @tempValue = CASE WHEN @value1 < @value2 
										THEN
											CASE WHEN @value1 < @value3
											   THEN 
													@value1
											   ELSE
													@value3
											END									
										WHEN @value2 < @value3 									
											THEN 
													@value2
											ELSE
													@value3
										END

				   SET @tempValue = @tempValue  + 1 ;
				   INSERT INTO @distanceTABLE VALUES(@i,@j,@tempValue);
				END
			
			--PRINT 'Min Values: ' + cast(@tempValue as varchar(10))
		SET @j = @j + 1;
	  END
	  SET @j = 1;
	  SET @i = @i + 1;
	END

  SET @distance = (	SELECT value FROM @distanceTABLE WHERE i = @lengthString1 and j = @lengthString2 ); 
  RETURN @distance ;
END;
