package org.example.xpneo4j.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.xpneo4j.api.ResourceInfo;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterNeighborRequest {
  private String targetResourceId;
  private ResourceInfo neighbor;
}
