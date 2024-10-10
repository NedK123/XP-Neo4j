MATCH path = (:Resource {id: $targetId})-[r:CREATED_UNDER*]-(b:BaseResource)
RETURN path