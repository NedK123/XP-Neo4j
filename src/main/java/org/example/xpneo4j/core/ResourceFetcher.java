package org.example.xpneo4j.core;

public interface ResourceFetcher {
  ResourceLineage fetchLineage(FetchLineageRequest request);
}
