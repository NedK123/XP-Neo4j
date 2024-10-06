package org.example.xpneo4j.api;

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
      @RequestParam Set<String> filterResourceTypes,
      @RequestParam Set<String> filterContexts) {
    FetchLineageRequest request =
        FetchLineageRequest.builder()
            .targetResourceId(targetResourceId)
            .projectId(projectId)
            .filterResourceTypes(filterResourceTypes)
            .filterContexts(filterContexts)
            .build();
    return ResponseEntity.ok(resourceFetcher.fetchLineage(request));
  }
}
