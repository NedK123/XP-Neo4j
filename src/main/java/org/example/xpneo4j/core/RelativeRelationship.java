package org.example.xpneo4j.core;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RelativeRelationship {
  @NonNull private String type;
  @NonNull private String context;
}
