package org.example.xpneo4j.infra.shared;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import org.example.xpneo4j.core.RegisterDetachedResourceRequest;
import org.example.xpneo4j.core.RegisterNeighborRequest;
import org.example.xpneo4j.core.RelationshipType;

public class QueryUtilities {

  public class RegisterDetachedResourceQuery {
    private static final String RESOURCE_LABELS = ":CustomLabels";
    public static final String RESOURCE_ID_FIELD = "id";
    public static final String RESOURCE_NAME_FIELD = "name";
    public static final String PROJECT_ID_FIELD = "projectId";

    public static String generate(RegisterDetachedResourceRequest request) {
      return fetchQuery()
          .replace(
              QueryUtilities.RegisterDetachedResourceQuery.RESOURCE_LABELS,
              constructResourceLabels(request.getProjectId(), request.getAdditionalLabels()));
    }

    private static String fetchQuery() {
      return QueryUtilities.fetchQuery("registerDetachedResource.cypher");
    }
  }

  public class RegisterNeighborResourceQuery {
    private static final String TARGET_RESOURCE_CUSTOM_LABELS = ":TargetCustomLabels";
    private static final String NEIGHBOR_CUSTOM_LABELS = ":NeighborCustomLabels";
    private static final String RELATION_CUSTOM_LABEL = ":RelationCustomLabel";

    public static String generate(RegisterNeighborRequest request) {
      return fetchQuery("registerNeighborResource.cypher")
          .replace(
              QueryUtilities.RegisterNeighborResourceQuery.TARGET_RESOURCE_CUSTOM_LABELS,
              constructResourceLabels(request.getProjectId(), Set.of()))
          .replace(
              QueryUtilities.RegisterNeighborResourceQuery.NEIGHBOR_CUSTOM_LABELS,
              constructResourceLabels(
                  request.getProjectId(), request.getNeighbor().getAdditionalLabels()))
          .replace(
              QueryUtilities.RegisterNeighborResourceQuery.RELATION_CUSTOM_LABEL,
              QueryUtilities.RegisterNeighborResourceQuery.constructrelationshipType(
                  request.getNeighbor().getRelationshipType()));
    }

    private static String constructrelationshipType(RelationshipType type) {
      return ":%s".formatted(type.name());
    }
  }

  public static String fetchQuery(String queryFileName) {
    try {
      return new String(
          Files.readAllBytes(Paths.get("src/main/resources/persistence/" + queryFileName)));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static String constructDisjunctionLabels(Set<String> labels) {
    StringBuilder labelBuilder = new StringBuilder();
    labelBuilder.append(":");
    for (String label : labels) {
      labelBuilder.append(label);
      labelBuilder.append("|");
    }
    labelBuilder.deleteCharAt(labelBuilder.length() - 1);
    return labelBuilder.toString();
  }

  public static String constructResourceLabels(String projectId, Set<String> additionalLabels) {
    StringBuilder labelBuilder =
        new StringBuilder(":Resource:Project_%s".formatted(projectId.replaceAll("-", "_")));
    for (String label : additionalLabels) {
      labelBuilder.append(":").append(label);
    }
    return labelBuilder.toString();
  }
}
