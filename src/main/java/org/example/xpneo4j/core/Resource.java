package org.example.xpneo4j.core;

import java.util.Set;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Resource {
  @NonNull private String id;
  @NonNull private String name;
  @Builder.Default private Set<String> labels = Set.of();
}
