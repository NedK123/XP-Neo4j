package org.example.xpneo4j.core;

public interface ResourceCreator {
  void register(RegisterDetachedResourceRequest request);

  void register(RegisterNeighborRequest request);
}
