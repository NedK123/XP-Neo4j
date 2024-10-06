MATCH (t:TargetCustomLabels {id: $targetId})
MATCH (t)-[r:RelationCustomLabels*]-(n:NeighborCustomLabels)
MATCH (n)-[rel]-()
  WHERE rel.context IN $filterContexts
RETURN n