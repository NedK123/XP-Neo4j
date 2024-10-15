package org.example.xpneo4j.acceptance;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.example.xpneo4j.core.LineageResource;
import org.example.xpneo4j.core.LineageResponse;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonNode;

public class LineageStepDefinitions extends BaseAcceptanceTest {

  private MvcResult fetchedResult;

  @When("^the consumer asks for the lineage of resource with id (.+)$")
  public void consumer_asks_for_the_lineage_of_a_resource(String resourceId) throws Throwable {
    fetchedResult =
        mockMvc
            .perform(get("/lineage").queryParam("targetResourceId", resourceId))
            .andExpect(status().isOk())
            .andReturn();
  }

  @Then("the lineage response should be empty")
  public void theLineageResponseShouldBeEmpty() throws IOException {
    JsonNode jsonNode = objectMapper.readTree(fetchedResult.getResponse().getContentAsString());
    assertEquals(objectMapper.readTree(getEmptyLineageResponse()), jsonNode);
  }

  @Then("the lineage response should contain only the target resource:")
  public void theLineageResponseShouldContainOneResource(DataTable dataTable) throws Exception {
    assertEquals(1, dataTable.entries().size());
    LineageResponse expectedResponse =
        LineageResponse.builder().targetResource(buildExpectedLineageFor(dataTable)).build();
    assertEquals(expectedResponse, extractLineage(fetchedResult));
  }

  private LineageResponse extractLineage(MvcResult result) throws IOException {
    return objectMapper.readValue(result.getResponse().getContentAsString(), LineageResponse.class);
  }

  private static LineageResource buildExpectedLineageFor(DataTable dataTable) {
    var resources =
        dataTable.entries().stream()
            .map(LineageStepDefinitions::build)
            .collect(Collectors.toMap(LineageResource::getId, resource -> resource));

    dataTable
        .entries()
        .forEach(
            (properties) ->
                resources
                    .get(properties.get("id"))
                    .setParent(resources.get(properties.get("parent"))));

    String targetResourceId = dataTable.entries().get(0).get("id");
    return resources.get(targetResourceId);
  }

  private static LineageResource build(Map<String, String> properties) {
    return LineageResource.builder()
        .id(properties.get("id"))
        .name(properties.get("name"))
        .types(Set.of(properties.get("type")))
        .parent(null)
        .build();
  }

  private static String getEmptyLineageResponse() {
    return """
            {"targetResource": null}
            """;
  }
}
