CREATE FUNCTION findMinimum
(
    @num1 int,  
	@num2 int, 
	@num3 int
)
RETURNS int
AS
BEGIN
Declare @minValue int
set @minValue = case when @num1 < @num2 then
               case when @num1 < @num3 then 
                @num1
               else
                @num3
               end
        when @num2 < @num3 then 
                @num2
        else
                 @num3
    end
  return @minValue  
END 




