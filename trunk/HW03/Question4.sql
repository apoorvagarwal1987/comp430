CREATE PROCEDURE Bacon
	@climber VARCHAR(8000)
	AS
	BEGIN
		Declare @fPeaks VARCHAR (8000);
		Declare @sPeaks VARCHAR (8000);

		DECLARE @baconTable table ( name VARCHAR (8000),number INT);
		--DECLARE @sPeaksTable table ( peakId int PRIMARY KEY IDENTITY, peak VARCHAR (8000));

		DECLARE @tripId INT;

		DECLARE @i INT;
		SET @i = 0 ;

		WHILE (@i <=6)
		BEGIN
			
			
			SET @i = @i + 1;
		END

	END