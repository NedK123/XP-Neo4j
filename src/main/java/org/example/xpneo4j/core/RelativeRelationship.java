package org.example.xpneo4j.core;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RelativeRelationship {
  private String type;
  private String context;
}
