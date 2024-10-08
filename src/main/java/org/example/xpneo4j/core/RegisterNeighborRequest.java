package org.example.xpneo4j.core;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterNeighborRequest {
  @NonNull private String projectId;
  @NonNull private String targetResourceId;
  @NonNull private NeighborRegistrationInfo neighbor;
}
