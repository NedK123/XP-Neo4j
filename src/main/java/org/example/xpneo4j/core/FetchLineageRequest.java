package org.example.xpneo4j.core;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FetchLineageRequest {
  @NonNull private String targetResourceId;
}
