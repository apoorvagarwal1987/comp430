CREATE FUNCTION findMinimum
(
    @num1 int,  
	@num2 int, 
	@num3 int
)
RETURNS int
AS
BEGIN
DECLARE @minValue int
SET @minValue = CASE WHEN @num1 < @num2 THEN
               CASE WHEN @num1 < @num3 THEN 
                @num1
               ELSE
                @num3
               END
        WHEN @num2 < @num3 THEN 
                @num2
        ELSE
                 @num3
    END
  RETURN @minValue  
END 




