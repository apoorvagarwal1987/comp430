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
	begin
		return @lengthString2;
	end

  IF @lengthString2 = 0 
	BEGIN
		return @lengthString1;
	END

  DECLARE @i AS INT;
  DECLARE @j AS INT;

  SET @i = 0;
  SET @j = 0;
  SET @distance = 0;

  DECLARE @distanceTable table ( i INT,  j INT, value INT);

  WHILE (@i<= @lengthString1)
  BEGIN		
		INSERT INTO @distanceTable VALUES (@i,0,@i);
		set @i = @i + 1;
  END
  

  WHILE (@j<= @lengthString2)
  BEGIN		
		INSERT INTO @distanceTable VALUES (0,@j,@j);
		set @j = @j + 1;
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
					SET @tempValue = (	SELECT Top(1) value FROM @distanceTable WHERE i = @i-1 and j = @j-1 ); 
					INSERT INTO @distanceTable VALUES(@i,@j,@tempValue);				
				END
			ELSE
				BEGIN
					
					DECLARE @value1 INT;
					DECLARE @value2 INT;
					DECLARE @value3 INT;			
					
					SET @value1 = (	SELECT Top(1) value FROM @distanceTable WHERE i = @i-1 and j = @j-1 );
					SET @value2 = (	SELECT Top(1) value FROM @distanceTable WHERE i = @i and j = @j-1 );
					SET @value3 = (	SELECT Top(1) value FROM @distanceTable WHERE i = @i and j = @j-1 );

					--SET @tempValue = (	SELECT dbo.findMinimum(@value1,@value2,@value3)	);

					set @tempValue = case when @value1 < @value2 
										then
											case when @value1 < @value3
											   then 
													@value1
											   else
													@value3
											end									
										when @value2 < @value3 									
											then 
													@value2
											else
													@value3
										end

				   SET @tempValue = @tempValue  + 1 ;
				   INSERT INTO @distanceTable VALUES(@i,@j,@tempValue);
				END
			
			--print 'Min Values: ' + cast(@tempValue as varchar(10))
		set @j = @j + 1;
	  END
	  set @j = 1;
	  set @i = @i + 1;
	END

  SET @distance = (	SELECT value FROM @distanceTable WHERE i = @lengthString1 and j = @lengthString2 ); 
  RETURN @distance ;
END;
