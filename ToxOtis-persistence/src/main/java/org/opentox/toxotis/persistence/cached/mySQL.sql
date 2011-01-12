/* All Comments */
SELECT MiComments.idx AS `id`, Literal.VALUE as `comment`
FROM MiComments INNER JOIN Literal ON MiComments.litId=Literal.hash;

/* Parameters with their Comments*/
SELECT Parameter.uri AS `Prm URI`, Literal.VALUE AS `comment` FROM Parameter
INNER JOIN OTComponent ON Parameter.uri = OTComponent.uri
INNER JOIN MetaInfo ON OTComponent.meta = MetaInfo.idx
INNER JOIN MiComments on MetaInfo.idx = MiComments.idx
INNER JOIN Literal ON Literal.hash=MiComments.litId;