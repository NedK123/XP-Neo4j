package org.example.xpneo4j.api;

import java.util.Objects;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.example.xpneo4j.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class Controller {

  @Autowired private ResourceCreator resourceCreator;
  @Autowired private ResourceFetcher resourceFetcher;

  @PostMapping("/register-detached")
  public ResponseEntity<Void> register(@RequestBody RegisterDetachedResourceRequest request) {
    resourceCreator.register(request);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/register-neighbor")
  public ResponseEntity<Void> register(@RequestBody RegisterNeighborRequest request) {
    resourceCreator.register(request);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/lineage")
  public ResponseEntity<ResourceLineage> fetchLineage(
      @RequestParam String targetResourceId,
      @RequestParam String projectId,
      @RequestParam(required = false) Set<String> filterResourceTypes,
      @RequestParam(required = false) Set<RelationshipType> filterRelationshipTypes,
      @RequestParam(required = false) Set<String> filterContexts) {
    FetchLineageRequest request =
        buildLineageRequest(
            targetResourceId,
            projectId,
            filterRelationshipTypes,
            filterResourceTypes,
            filterContexts);
    return ResponseEntity.ok(resourceFetcher.fetchLineage(request));
  }

  private static FetchLineageRequest buildLineageRequest(
      String targetResourceId,
      String projectId,
      Set<RelationshipType> filterRelationshipTypes,
      Set<String> filterResourceTypes,
      Set<String> filterContexts) {
    FetchLineageRequest.FetchLineageRequestBuilder requestBuilder =
        FetchLineageRequest.builder().targetResourceId(targetResourceId).projectId(projectId);
    if (Objects.nonNull(filterResourceTypes)) {
      requestBuilder.filterResourceTypes(filterResourceTypes);
    }
    if (Objects.nonNull(filterRelationshipTypes)) {
      requestBuilder.filterRelationshipTypes(filterRelationshipTypes);
    }
    if (Objects.nonNull(filterContexts)) {
      requestBuilder.filterContexts(filterContexts);
    }
    return requestBuilder.build();
  }
}
