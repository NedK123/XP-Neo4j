package org.example.xpneo4j.api;

import lombok.AllArgsConstructor;
import org.example.xpneo4j.core.RegisterDetachedResourceRequest;
import org.example.xpneo4j.core.ResourceCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class Controller {

  @Autowired private ResourceCreator resourceCreator;

  @PostMapping("/register-detached")
  public ResponseEntity<Void> register(@RequestBody RegisterDetachedResourceRequest request) {
    resourceCreator.register(request);
    return ResponseEntity.ok().build();
  }
}
