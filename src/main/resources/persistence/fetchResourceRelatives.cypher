MATCH (t:Resource {id: $targetResourceId})-[:CREATED_UNDER*1..$ancestorsGenerationLimit]->(ancestor)
        <-[:CREATED_UNDER*1..$relativesGenerationLimit]-(relative)
  WHERE any(label IN labels(relative)
    WHERE label IN $relativeResourcesTypeFilter)
RETURN collect(DISTINCT ancestor) + collect(DISTINCT relative)
// This query can return the target node among the relatives
// This query will not return the ancestors if there were no relatives under it (I think this happened when I unified the MATCH clause)
// We can include a filter on the context of the relationship in relatives