package org.example.xpneo4j.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.xpneo4j.api.NeighborRegistrationInfo;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterNeighborRequest {
  private String projectId;
  private String targetResourceId;
  private NeighborRegistrationInfo neighbor;
}
