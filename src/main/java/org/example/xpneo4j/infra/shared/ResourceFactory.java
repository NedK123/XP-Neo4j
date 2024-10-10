package org.example.xpneo4j.infra.shared;

import java.util.HashSet;
import java.util.Set;
import org.example.xpneo4j.core.RegisterDetachedResourceRequest;
import org.example.xpneo4j.core.RegisterNeighborRequest;
import org.example.xpneo4j.core.RelationshipType;
import org.example.xpneo4j.infra.neo4jtemplate.ResourceNode;
import org.example.xpneo4j.infra.neo4jtemplate.ResourceRelationship;

public class ResourceFactory {

  public static ResourceNode generateResource(RegisterDetachedResourceRequest request) {
    return generateResource(
        request.getId(), request.getName(), request.getProjectId(), request.getAdditionalLabels());
  }

  public static ResourceNode generateNeighbor(
      ResourceNode targetResource, RegisterNeighborRequest request) {
    ResourceNode neighbor =
        generateResource(
            request.getNeighbor().getId(),
            request.getNeighbor().getName(),
            request.getProjectId(),
            request.getNeighbor().getAdditionalLabels());
    neighbor.addRelationshipWithNeighbor(
        generateRelationship(request, targetResource), request.getNeighbor().getRelationshipType());
    if (shouldInheritCreationContextOfTargetResource(request)) {
      neighbor.addRelationshipWithNeighbor(
          cloneRelation(targetResource.getCreatedUnderRelationship()),
          RelationshipType.CREATED_UNDER);
    }
    return neighbor;
  }

  private static boolean shouldInheritCreationContextOfTargetResource(
      RegisterNeighborRequest request) {
    return request.getNeighbor().isInheritCreationConnectionFromTarget();
  }

  private static ResourceRelationship cloneRelation(ResourceRelationship relationship) {
    return ResourceRelationship.builder()
        .context(relationship.getContext())
        .neighbor(relationship.getNeighbor())
        .build();
  }

  private static ResourceRelationship generateRelationship(
      RegisterNeighborRequest request, ResourceNode neighbor) {
    return ResourceRelationship.builder()
        .context(request.getNeighbor().getRelationshipContext())
        .neighbor(neighbor)
        .build();
  }

  private static ResourceNode generateResource(
      String id, String name, String projectId, Set<String> additionalLabels) {
    Set<String> labels = new HashSet<>();
    labels.add("Project_%s".formatted(projectId.replaceAll("-", "_")));
    labels.addAll(additionalLabels);
    return ResourceNode.builder().id(id).name(name).projectId(projectId).labels(labels).build();
  }
}
