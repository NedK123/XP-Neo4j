package org.example.xpneo4j.infra;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

public class QueryUtilities {

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
