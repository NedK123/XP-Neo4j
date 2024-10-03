package org.example.xpneo4j.api;

import lombok.AllArgsConstructor;
import org.example.xpneo4j.infra.LineageProjection;
import org.example.xpneo4j.infra.ResourceEntity;
import org.example.xpneo4j.infra.ResourceRelationshipEntity;
import org.example.xpneo4j.infra.ResourceRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@AllArgsConstructor
public class Controller {

    private final ResourceRepo repo;

    @PostMapping("/register")
    public ResponseEntity<ResourceEntity> register(@RequestParam String resourceId, @RequestParam Set<String> labels) {
        ResourceEntity resource = ResourceEntity.builder()
                .resourceId(resourceId)
                .labels(labels)
                .build();
        return ResponseEntity.ok(repo.save(resource));
    }

    @PostMapping("/register-children")
    public ResponseEntity<ResourceEntity> registerChildren(@RequestParam String parentId,
                                                           @RequestParam String childId,
                                                           @RequestParam String context,
                                                           @RequestParam Set<String> labels) {
        ResourceEntity resourceEntity = repo.findById(parentId).get();
        resourceEntity.getResourceRelationshipEntity().add(ResourceRelationshipEntity.builder()
                .target(ResourceEntity.builder().resourceId(childId).labels(labels).build())
                .context(context)
                .build());
        return ResponseEntity.ok(repo.save(resourceEntity));
    }

    @GetMapping("/resources/{resourceId}")
    public ResponseEntity<ResourceEntity> getResource(@RequestParam String resourceId) {
        return ResponseEntity.ok(repo.findById(resourceId).get());
    }

    @GetMapping("/resources/{resourceId}/lineage")
    public ResponseEntity<Object> getResourceLineage(@RequestParam String resourceId) {
        return ResponseEntity.ok(repo.findLineageByNodeId(resourceId));
    }
}
