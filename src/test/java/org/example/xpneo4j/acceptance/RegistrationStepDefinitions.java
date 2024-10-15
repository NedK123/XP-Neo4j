package org.example.xpneo4j.acceptance;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import java.util.Map;
import java.util.Set;
import org.example.xpneo4j.core.RegisterDetachedResourceRequest;
import org.springframework.http.MediaType;

public class RegistrationStepDefinitions extends BaseAcceptanceTest {

  @Given("a resource with id {string} that does not exist")
  public void aResourceWithIdXxxThatDoesNotExist(String resourceId) {}

  @Given("resources that are not connected to any other resources:")
  public void resources_that_are_not_connected_to_other_resources(DataTable dataTable) {
    dataTable.entries().forEach(this::registerResource);
  }

  private void registerResource(Map<String, String> row) {
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

  private RegisterDetachedResourceRequest map(Map<String, String> row) {
    return RegisterDetachedResourceRequest.builder()
        .id(row.get("id"))
        .name(row.get("name"))
        .projectId(row.get("projectId"))
        .additionalLabels(Set.of(row.get("type")))
        .build();
  }
}
