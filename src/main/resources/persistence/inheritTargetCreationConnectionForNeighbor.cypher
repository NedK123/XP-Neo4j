MATCH (t {id: $targetId})-[r:CREATED_UNDER]->(p)
MATCH (n {id: $neighborId})
MERGE (n)-[:CREATED_UNDER {context : r.context}]->(p)