package org.example.xpneo4j.acceptance;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.example.xpneo4j.core.NeighborRegistrationInfo;
import org.example.xpneo4j.core.RegisterDetachedResourceRequest;
import org.example.xpneo4j.core.RegisterNeighborRequest;
import org.example.xpneo4j.core.RelationshipType;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.MediaType;

public class RegistrationStepDefinitions extends BaseAcceptanceTest {

  @Given("a resource with id {string} that does not exist")
  public void aResourceWithIdXxxThatDoesNotExist(String resourceId) {}

  @Given("resources that are not connected to any other resources:")
  public void resources_that_are_not_connected_to_other_resources(DataTable dataTable) {
    dataTable.entries().forEach(this::registerDetachedResource);
  }

  @Given("resources that are connected to each other:")
  public void resourcesThatConnectedToEachOther(DataTable dataTable) {
    dataTable.entries().forEach(this::registerResource);
  }

  private void registerDetachedResource(Map<String, String> row) {
    try {
      mockMvc
          .perform(
              post("/register-detached")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(map(row))))
          .andExpect(status().isCreated());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void registerResource(Map<String, String> row) {
    try {
      mockMvc
          .perform(
              post("/register-neighbor")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(objectMapper.writeValueAsString(mapo(row))))
          .andExpect(status().isCreated());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private RegisterDetachedResourceRequest map(Map<String, String> row) {
    return RegisterDetachedResourceRequest.builder()
        .id(row.get("id"))
        .name(row.get("name"))
        .projectId(row.get("projectId"))
        .additionalLabels(mapDetachedResourceLabels(row))
        .build();
  }

  private static @NotNull Set<String> mapDetachedResourceLabels(Map<String, String> row) {
    var labels = new HashSet<String>();
    labels.add(row.get("type"));
    if (Boolean.parseBoolean(row.get("base"))) {
      labels.add("BaseResource");
    }
    return labels;
  }

  private RegisterNeighborRequest mapo(Map<String, String> row) {
    return RegisterNeighborRequest.builder()
        .targetResourceId(row.get("parent"))
        .projectId("43882dec-d762-4dd9-a323-2fe3cf3f449e")
        .neighbor(
            NeighborRegistrationInfo.builder()
                .id(row.get("id"))
                .name(row.get("name"))
                .relationshipContext(row.get("relationContext"))
                .inheritCreationConnectionFromTarget(
                    Boolean.getBoolean(row.get("inheritCreationContext")))
                .additionalLabels(Set.of(row.get("type")))
                .relationshipType(RelationshipType.valueOf(row.get("relationType")))
                .build())
        .build();
  }
}
