MATCH (t:TargetCustomLabels {id: $targetId})
MERGE (n:NeighborCustomLabels {id: $id, name: $name, projectId: $projectId})
MERGE (t)-[:RelationCustomLabel {context : $context}]->(n)