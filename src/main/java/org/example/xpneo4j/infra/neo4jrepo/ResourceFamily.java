package org.example.xpneo4j.infra.neo4jrepo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.xpneo4j.infra.neo4jtemplate.ResourceNode;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResourceFamily {
  private ResourceNode targetLineage;
  //  private Set<ResourceNode> relatives;
}
