 Alter  TRIGGER trigInsertClimbed
 on Climbed  
 instead of insert
 as
 begin
	
 	if @@ROWCOUNT = 0
		return
 
	Declare @insertedPeak VARCHAR(8000);
	Declare @oldPeak VARCHAR(8000);
	Declare @cutoffValue INT;
	Declare @distance INT;

	set @cutoffValue = ( select cutoff from ed_cutoff );

	set @insertedPeak = ( select PEAK from inserted );

	Declare peakNames Cursor for
		SELECT  name
			FROM peak;

	DECLARE @peakEditDistance table ( peakName VARCHAR(8000) unique, distance INT );

	open peakNames;
	Fetch peakNames into @oldPeak ;

	while (@@FETCH_STATUS = 0)
	BEGIN
		set @distance = (select dbo.levenshteinDistance(@oldPeak,@insertedPeak));
		insert into @peakEditDistance values (@oldPeak,@distance);
		Fetch peakNames into @oldPeak ;
	END

	close peakNames;
	deallocate peakNames;
	

	Declare resultSet Cursor for
		SELECT  Top(1) peakName , distance
			FROM @peakEditDistance
			where distance <= @cutoffValue
			order by distance;

	open resultSet;
	Fetch resultSet into @oldPeak , @distance;
	print 'Peak closest is ' + @oldPeak + '  its distance is : '+ cast(@distance as varchar(10));
	close resultSet
	deallocate resultset;

 end


