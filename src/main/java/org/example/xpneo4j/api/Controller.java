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
    return ResponseEntity.created(null).build();
  }

  @PostMapping("/register-neighbor")
  public ResponseEntity<Void> register(@RequestBody RegisterNeighborRequest request) {
    resourceCreator.register(request);
    return ResponseEntity.created(null).build();
  }

  @GetMapping("/lineage")
  public ResponseEntity<LineageResponse> fetchLineage(@RequestParam String targetResourceId) {
    return ResponseEntity.ok(
        resourceFetcher.fetchLineage(
            FetchLineageRequest.builder().targetResourceId(targetResourceId).build()));
  }

  @GetMapping("/relatives")
  public ResponseEntity<RelativesResponse> fetchRelatives(
      @RequestParam String targetResourceId,
      @RequestParam(required = false) Set<String> relativeResourcesTypeFilter,
      @RequestParam int ancestorsGenerationLimit,
      @RequestParam int relativesGenerationLimit) {
    return ResponseEntity.ok(
        resourceFetcher.fetchRelatives(
            FetchRelativesRequest.builder()
                .targetResourceId(targetResourceId)
                .relativeResourcesTypeFilter(relativeResourcesTypeFilter)
                .ancestorsGenerationLimit(ancestorsGenerationLimit)
                .relativesGenerationLimit(relativesGenerationLimit)
                .build()));
  }
}
