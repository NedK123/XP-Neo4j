package org.example.xpneo4j.core;

public interface ResourceFetcher {
  LineageResponse fetchLineage(FetchLineageRequest request);

  RelativesResponse fetchRelatives(FetchRelativesRequest request);
}
