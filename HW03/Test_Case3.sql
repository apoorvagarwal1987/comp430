SELECT  c.PEAK , c.when_climbed
FROM	dbo.PARTICIPATED as p, dbo.CLIMBED  as c
WHERE p.TRIP_ID = c.TRIP_ID and p.NAME = 'MARY'
ORDER by c.WHEN_CLIMBED


SELECT  c.PEAK ,c.when_climbed
FROM	dbo.PARTICIPATED as p, dbo.CLIMBED  as c
WHERE p.TRIP_ID = c.TRIP_ID and p.NAME = 'PATRICIA'
ORDER by c.WHEN_CLIMBED

SET NOCOUNT ON
SELECT dbo.longestSequence('DONALD','EDWARD')

SET NOCOUNT ON
EXECUTE dbo.CommonSequence @fPerson = 'DONALD', @sPerson = 'EDWARD';

SET NOCOUNT ON
SELECT dbo.longestSequence('JENNIFER','BETTY')

SET NOCOUNT ON
EXECUTE dbo.CommonSequence @fPerson = 'JENNIFER', @sPerson = 'BETTY';

SET NOCOUNT ON
EXECUTE dbo.CommonSequence @fPerson = 'MARY', @sPerson = 'PATRICIA';

SET NOCOUNT ON
exec FindMostSimilar

SELECT * FROM climber



-- Most common climber are MARY and  PATRICIA common peaks are:54
