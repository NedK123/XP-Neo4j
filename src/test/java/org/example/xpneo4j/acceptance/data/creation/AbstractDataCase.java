package org.example.xpneo4j.acceptance.data.creation;

import java.util.Set;
import org.example.xpneo4j.acceptance.resource.registration.ResourceRegistrationUtilities;
import org.example.xpneo4j.core.RegisterDetachedResourceRequest;
import org.example.xpneo4j.core.RegisterNeighborRequest;
import org.springframework.test.web.servlet.MockMvc;

public abstract class AbstractDataCase implements DataUseCase {

  protected MockMvc mockMvc;

  public AbstractDataCase(MockMvc mockMvc) {
    this.mockMvc = mockMvc;
  }

  @Override
  public void importData() {
    getDetachedResourceRequests().forEach(this::registerDetachedResource);
    getRegisterNeighborRequest().forEach(this::registerNeighbor);
    System.out.println("Hellooooo");
  }

  private void registerDetachedResource(RegisterDetachedResourceRequest request) {
    ResourceRegistrationUtilities.registerDetachedResource(mockMvc, request);
  }

  private void registerNeighbor(RegisterNeighborRequest request) {
    ResourceRegistrationUtilities.registerNeighborResource(mockMvc, request);
  }

  protected abstract Set<RegisterDetachedResourceRequest> getDetachedResourceRequests();

  protected abstract Set<RegisterNeighborRequest> getRegisterNeighborRequest();
}
