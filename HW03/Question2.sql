 CREATE  TRIGGER trigInsertClimbed
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
		set @distance = (select dbo.levenshteinDistance(@insertedPeak,@oldPeak));
		insert into @peakEditDistance values (@oldPeak,@distance);
		Fetch peakNames into @oldPeak ;
	END

	close peakNames;
	deallocate peakNames;
	

	Declare resultSet Cursor for
		SELECT  Top(1) peakName , distance
			FROM @peakEditDistance
			order by distance;

	open resultSet;
	Fetch resultSet into @oldPeak , @distance;

	if (@distance = 0)
	BEGIN
		--print 'Success: Inserted peak name'  + @insertedPeak + ' does not match any in the database.' + @oldPeak + 'is used instead'
		insert into climbed 
			select * from inserted

		print 'Successfully Inserted'
		--print 'Peak closest is ' + @oldPeak + '  its distance is : '+ cast(@distance as varchar(10));
	END

	ELSE 
	BEGIN
		if ( @distance <= @cutoffValue)
			BEGIN
				print 'ERROR: Inserted peak name '  + @insertedPeak + ' does not match any in the database. ' + @oldPeak + ' is used instead'
				declare @date DATE;
				declare @tripId INT;

				set @date = (	select when_climbed from inserted );
				set @tripId = (	select trip_id from inserted );

				insert into climbed values (@tripId,@oldPeak,@date)
			END
		else
			BEGIN
				print 'ERROR: Inserted peak name '  + @insertedPeak + ' does not closely match any in the database and so the insert is rejected'
			END
	END
	print 'Peak closest is ' + @oldPeak + '  its distance is : '+ cast(@distance as varchar(10));	
	close resultSet
	deallocate resultset;

 end


