package org.example.xpneo4j.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RelativeResource {
  private String id;
  private String name;
  private RelativeRelationship relationshipWithTargetResource;
}
