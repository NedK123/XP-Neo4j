MATCH path = (:Resource {id: $TargetResourceId})-[*]-()
WITH nodes(path) AS nodes
UNWIND nodes AS node
WITH node
  WHERE ANY(label IN labels(node) WHERE label IN $RelativeNodeLabels)
MATCH (node)-[r:CREATED_UNDER {context:$RelationContext}]-()
RETURN node