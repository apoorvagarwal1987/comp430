 Create  TRIGGER trigInsertClimbed
 on Climbed  
 instead of insert
 as
 begin
 	if @@ROWCOUNT = 0
		return
 
	Declare peakNames Cursor for
		SELECT  name
			FROM peak;



 end
