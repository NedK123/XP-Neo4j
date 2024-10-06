MATCH (t:TargetCustomLabels {id: $targetId})
MATCH (t)-[r*]-(n:NeighborCustomLabels)
MATCH (n)-[rel]-()
  WHERE rel.context IN $filterContexts
RETURN n