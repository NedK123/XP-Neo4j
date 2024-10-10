MATCH (t:TargetCustomLabels {id: $targetId})
MERGE (n:NeighborCustomLabels {id: $neighborId, name: $neighborName, projectId: $projectId})
MERGE (n)-[:RelationCustomLabel {context : $context}]->(t)