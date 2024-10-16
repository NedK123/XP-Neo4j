package org.example.xpneo4j.acceptance.data.creation;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.UUID;

import lombok.SneakyThrows;
import org.example.xpneo4j.core.NeighborRegistrationInfo;
import org.example.xpneo4j.core.RegisterDetachedResourceRequest;
import org.example.xpneo4j.core.RegisterNeighborRequest;
import org.example.xpneo4j.core.RelationshipType;
import org.jetbrains.annotations.NotNull;
import org.springframework.test.web.servlet.MockMvc;

import static org.example.xpneo4j.core.RelationshipType.*;

public class FirstUseCase extends AbstractDataCase {

  private static final String PROJECT_ID = UUID.randomUUID().toString();
  private static final String B1 = UUID.randomUUID().toString();
  private static final String S1 = UUID.randomUUID().toString();
  private static final String S1R = UUID.randomUUID().toString();
  private static final String S1RR = UUID.randomUUID().toString();
  private static final String S1RRR = UUID.randomUUID().toString();
  private static final String S1RRRR = UUID.randomUUID().toString();
  private static final String S2 = UUID.randomUUID().toString();
  private static final String S2R = UUID.randomUUID().toString();
  private static final String S2RR = UUID.randomUUID().toString();
  private static final String S2RRR =UUID.randomUUID().toString();
  private static final String S2RRRR =UUID.randomUUID().toString();
  private static final String S3 = UUID.randomUUID().toString();
  private static final String S3R = UUID.randomUUID().toString();
  private static final String S3RR = UUID.randomUUID().toString();
  private static final String S3RRR =UUID.randomUUID().toString();
  private static final String S3RRRR =UUID.randomUUID().toString();
  private static final String S4 = UUID.randomUUID().toString();
  private static final String S4R = UUID.randomUUID().toString();
  private static final String S4RR = UUID.randomUUID().toString();
  private static final String S4RRR =UUID.randomUUID().toString();
  private static final String S4RRRR =UUID.randomUUID().toString();
  private static final String S5 = UUID.randomUUID().toString();
  private static final String S5R = UUID.randomUUID().toString();
  private static final String S5RR = UUID.randomUUID().toString();
  private static final String S5RRR =UUID.randomUUID().toString();
  private static final String S5RRRR =UUID.randomUUID().toString();

  private static final String E1 = UUID.randomUUID().toString();
  private static final String E1R = UUID.randomUUID().toString();
  private static final String E1RR = UUID.randomUUID().toString();
  private static final String E1RRR = UUID.randomUUID().toString();
  private static final String E1RRRR = UUID.randomUUID().toString();
  private static final String E2 = UUID.randomUUID().toString();
  private static final String E2R = UUID.randomUUID().toString();
  private static final String E2RR = UUID.randomUUID().toString();
  private static final String E2RRR =UUID.randomUUID().toString();
  private static final String E2RRRR =UUID.randomUUID().toString();
  private static final String E3 = UUID.randomUUID().toString();
  private static final String E3R = UUID.randomUUID().toString();
  private static final String E3RR = UUID.randomUUID().toString();
  private static final String E3RRR =UUID.randomUUID().toString();
  private static final String E3RRRR =UUID.randomUUID().toString();
  private static final String E4 = UUID.randomUUID().toString();
  private static final String E4R = UUID.randomUUID().toString();
  private static final String E4RR = UUID.randomUUID().toString();
  private static final String E4RRR =UUID.randomUUID().toString();
  private static final String E4RRRR =UUID.randomUUID().toString();
  private static final String E5 = UUID.randomUUID().toString();
  private static final String E5R = UUID.randomUUID().toString();
  private static final String E5RR = UUID.randomUUID().toString();
  private static final String E5RRR =UUID.randomUUID().toString();
  private static final String E5RRRR =UUID.randomUUID().toString();


  public FirstUseCase(MockMvc mockMvc) {
    super(mockMvc);
  }

  @Override
  protected Set<RegisterDetachedResourceRequest> getDetachedResourceRequests() {
    return Set.of(
        RegisterDetachedResourceRequest.builder()
            .id(B1)
            .name("B1")
            .projectId(PROJECT_ID)
            .additionalLabels(Set.of("B"))
            .build());
  }

  @Override
  protected Set<RegisterNeighborRequest> getRegisterNeighborRequest() {
    return Set.of(
            // S
            builder().targetResourceId(B1).neighbor(build(S1, "S1", CREATED_UNDER, TypeS(), false)).projectId(PROJECT_ID).build(),
            builder().targetResourceId(S1).neighbor(build(S1R, "S1R", REPRODUCTION_OF, TypeS(), true)).projectId(PROJECT_ID).build(),
            builder().targetResourceId(S1R).neighbor(build(S1RR, "S1RR", REPRODUCTION_OF, TypeS(), true)).projectId(PROJECT_ID).build(),
            builder().targetResourceId(S1RR).neighbor(build(S1RRR, "S1RRR", REPRODUCTION_OF, TypeS(), true)).projectId(PROJECT_ID).build(),
            builder().targetResourceId(S1RRR).neighbor(build(S1RRRR, "S1RRRR", REPRODUCTION_OF, TypeS(), true)).projectId(PROJECT_ID).build(),

            builder().targetResourceId(B1).neighbor(build(S2, "S2", CREATED_UNDER, TypeS(), false)).projectId(PROJECT_ID).build(),
            builder().targetResourceId(S2).neighbor(build(S2R, "S2R", REPRODUCTION_OF, TypeS(), true)).projectId(PROJECT_ID).build(),
            builder().targetResourceId(S2R).neighbor(build(S2RR, "S2RR", REPRODUCTION_OF, TypeS(), true)).projectId(PROJECT_ID).build(),
            builder().targetResourceId(S2RR).neighbor(build(S2RRR, "S2RRR", REPRODUCTION_OF, TypeS(), true)).projectId(PROJECT_ID).build(),
            builder().targetResourceId(S2RRR).neighbor(build(S2RRRR, "S2RRRR", REPRODUCTION_OF, TypeS(), true)).projectId(PROJECT_ID).build(),

            builder().targetResourceId(B1).neighbor(build(S3, "S3", CREATED_UNDER, TypeS(), false)).projectId(PROJECT_ID).build(),
            builder().targetResourceId(S3).neighbor(build(S3R, "S3R", REPRODUCTION_OF, TypeS(), true)).projectId(PROJECT_ID).build(),
            builder().targetResourceId(S3R).neighbor(build(S3RR, "S3RR", REPRODUCTION_OF, TypeS(), true)).projectId(PROJECT_ID).build(),
            builder().targetResourceId(S3RR).neighbor(build(S3RRR, "S3RRR", REPRODUCTION_OF, TypeS(), true)).projectId(PROJECT_ID).build(),
            builder().targetResourceId(S3RRR).neighbor(build(S3RRRR, "S3RRRR", REPRODUCTION_OF, TypeS(), true)).projectId(PROJECT_ID).build(),

            builder().targetResourceId(B1).neighbor(build(S4, "S4", CREATED_UNDER, TypeS(), false)).projectId(PROJECT_ID).build(),
            builder().targetResourceId(S4).neighbor(build(S4R, "S4R", REPRODUCTION_OF, TypeS(), true)).projectId(PROJECT_ID).build(),
            builder().targetResourceId(S4R).neighbor(build(S4RR, "S4RR", REPRODUCTION_OF, TypeS(), true)).projectId(PROJECT_ID).build(),
            builder().targetResourceId(S4RR).neighbor(build(S4RRR, "S4RRR", REPRODUCTION_OF, TypeS(), true)).projectId(PROJECT_ID).build(),
            builder().targetResourceId(S4RRR).neighbor(build(S4RRRR, "S4RRRR", REPRODUCTION_OF, TypeS(), true)).projectId(PROJECT_ID).build(),

            builder().targetResourceId(B1).neighbor(build(S5, "S5", CREATED_UNDER, TypeS(), false)).projectId(PROJECT_ID).build(),
            builder().targetResourceId(S5).neighbor(build(S5R, "S5R", REPRODUCTION_OF, TypeS(), true)).projectId(PROJECT_ID).build(),
            builder().targetResourceId(S5R).neighbor(build(S5RR, "S5RR", REPRODUCTION_OF, TypeS(), true)).projectId(PROJECT_ID).build(),
            builder().targetResourceId(S5RR).neighbor(build(S5RRR, "S5RRR", REPRODUCTION_OF, TypeS(), true)).projectId(PROJECT_ID).build(),
            builder().targetResourceId(S5RRR).neighbor(build(S5RRRR, "S5RRRR", REPRODUCTION_OF, TypeS(), true)).projectId(PROJECT_ID).build()


            // E
            builder().targetResourceId(S1).neighbor(build(E1, "E1", REPRODUCTION_OF, TypeS(), true)).projectId(PROJECT_ID).build(),
            builder().targetResourceId(S1R).neighbor(build(E1R, "E1R", REPRODUCTION_OF, TypeS(), true)).projectId(PROJECT_ID).build(),
            builder().targetResourceId(S1RR).neighbor(build(E1RR, "E1RR", REPRODUCTION_OF, TypeS(), true)).projectId(PROJECT_ID).build(),
            builder().targetResourceId(S1RRR).neighbor(build(E1RRR, "E1RRR", REPRODUCTION_OF, TypeS(), true)).projectId(PROJECT_ID).build(),
            builder().targetResourceId(S1RRRR).neighbor(build(E1RRRR, "E1RRRR", REPRODUCTION_OF, TypeS(), true)).projectId(PROJECT_ID).build(),

            builder().targetResourceId(S2).neighbor(build(E2R, "E2R", REPRODUCTION_OF, TypeS(), true)).projectId(PROJECT_ID).build(),
            builder().targetResourceId(S2R).neighbor(build(E2RR, "E2RR", REPRODUCTION_OF, TypeS(), true)).projectId(PROJECT_ID).build(),
            builder().targetResourceId(S2RR).neighbor(build(E2RRR, "E2RRR", REPRODUCTION_OF, TypeS(), true)).projectId(PROJECT_ID).build(),
            builder().targetResourceId(S2RRR).neighbor(build(E2RRRR, "E2RRRR", REPRODUCTION_OF, TypeS(), true)).projectId(PROJECT_ID).build(),

            builder().targetResourceId(S3).neighbor(build(E3R, "E3R", REPRODUCTION_OF, TypeS(), true)).projectId(PROJECT_ID).build(),
            builder().targetResourceId(S3R).neighbor(build(E3RR, "E3RR", REPRODUCTION_OF, TypeS(), true)).projectId(PROJECT_ID).build(),
            builder().targetResourceId(S3RR).neighbor(build(E3RRR, "E3RRR", REPRODUCTION_OF, TypeS(), true)).projectId(PROJECT_ID).build(),
            builder().targetResourceId(S3RRR).neighbor(build(E3RRRR, "E3RRRR", REPRODUCTION_OF, TypeS(), true)).projectId(PROJECT_ID).build(),

            builder().targetResourceId(S4).neighbor(build(E4R, "E4R", REPRODUCTION_OF, TypeS(), true)).projectId(PROJECT_ID).build(),
            builder().targetResourceId(S4R).neighbor(build(E4RR, "E4RR", REPRODUCTION_OF, TypeS(), true)).projectId(PROJECT_ID).build(),
            builder().targetResourceId(S4RR).neighbor(build(E4RRR, "E4RRR", REPRODUCTION_OF, TypeS(), true)).projectId(PROJECT_ID).build(),
            builder().targetResourceId(S4RRR).neighbor(build(E4RRRR, "E4RRRR", REPRODUCTION_OF, TypeS(), true)).projectId(PROJECT_ID).build(),

            builder().targetResourceId(S5).neighbor(build(E5R, "E5R", REPRODUCTION_OF, TypeS(), true)).projectId(PROJECT_ID).build(),
            builder().targetResourceId(S5R).neighbor(build(E5RR, "E5RR", REPRODUCTION_OF, TypeS(), true)).projectId(PROJECT_ID).build(),
            builder().targetResourceId(S5RR).neighbor(build(E5RRR, "E5RRR", REPRODUCTION_OF, TypeS(), true)).projectId(PROJECT_ID).build(),
            builder().targetResourceId(S5RRR).neighbor(build(E5RRRR, "E5RRRR", REPRODUCTION_OF, TypeS(), true)).projectId(PROJECT_ID).build()

    );
  }

  private static RegisterNeighborRequest.RegisterNeighborRequestBuilder builder() {
    return RegisterNeighborRequest.builder();
  }

  @SneakyThrows
  private static NeighborRegistrationInfo build(String id, String name, RelationshipType relationshipType, @NotNull Set<String> additionalLabels, boolean sibling) {
    return NeighborRegistrationInfo.builder().id(id).name(name).relationshipType(relationshipType).additionalLabels(additionalLabels).inheritCreationConnectionFromTarget(sibling).build();
  }

  private static @NotNull Set<String> TypeB() {
    return Set.of("B");
  }

  private static @NotNull Set<String> TypeS() {
    return Set.of("S");
  }

  private static @NotNull Set<String> TypeE() {
    return Set.of("E");
  }

  private static @NotNull Set<String> TypeM() {
    return Set.of("M");
  }
}
