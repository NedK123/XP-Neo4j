MATCH (t:TargetCustomLabels {id: $targetId})
MATCH (t)-[r*]-(n:NeighborCustomLabels)
//MATCH (n)-[rel]-()
//  WHERE rel.context = 'ref'
RETURN n


