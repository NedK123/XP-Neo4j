package org.example.xpneo4j.infra;

import java.util.Set;

public interface LineageProjection {

  Set<NodeProjection> getNodes(); // List of all nodes in the path

  Set<RelationshipProjection> getRelationships();

  interface NodeProjection {
    Long getResourceId();

    Set<String> getLabels(); // Add any other node properties you need
  }

  interface RelationshipProjection {
    String getType(); // Type of relationship
  }
}
