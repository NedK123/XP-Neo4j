package org.example.xpneo4j.acceptance.data.creation;

import static org.example.xpneo4j.core.RelationshipType.CREATED_UNDER;
import static org.example.xpneo4j.core.RelationshipType.REPRODUCTION_OF;

import java.util.Set;
import java.util.UUID;
import lombok.SneakyThrows;
import org.example.xpneo4j.core.NeighborRegistrationInfo;
import org.example.xpneo4j.core.RegisterDetachedResourceRequest;
import org.example.xpneo4j.core.RegisterNeighborRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.test.web.servlet.MockMvc;

public class FirstUseCase extends AbstractDataCase {

  private final String PROJECT_ID = UUID.randomUUID().toString();
  private final String B1 = UUID.randomUUID().toString();
  private final String S1 = UUID.randomUUID().toString();
  private final String S1R = UUID.randomUUID().toString();
  private final String S1RR = UUID.randomUUID().toString();
  private final String S1RRR = UUID.randomUUID().toString();
  private final String S1RRRR = UUID.randomUUID().toString();
  private final String S2 = UUID.randomUUID().toString();
  private final String S2R = UUID.randomUUID().toString();
  private final String S2RR = UUID.randomUUID().toString();
  private final String S2RRR = UUID.randomUUID().toString();
  private final String S2RRRR = UUID.randomUUID().toString();
  private final String S3 = UUID.randomUUID().toString();
  private final String S3R = UUID.randomUUID().toString();
  private final String S3RR = UUID.randomUUID().toString();
  private final String S3RRR = UUID.randomUUID().toString();
  private final String S3RRRR = UUID.randomUUID().toString();
  private final String S4 = UUID.randomUUID().toString();
  private final String S4R = UUID.randomUUID().toString();
  private final String S4RR = UUID.randomUUID().toString();
  private final String S4RRR = UUID.randomUUID().toString();
  private final String S4RRRR = UUID.randomUUID().toString();
  private final String S5 = UUID.randomUUID().toString();
  private final String S5R = UUID.randomUUID().toString();
  private final String S5RR = UUID.randomUUID().toString();
  private final String S5RRR = UUID.randomUUID().toString();
  private final String S5RRRR = UUID.randomUUID().toString();
  private final String S6 = UUID.randomUUID().toString();
  private final String S7 = UUID.randomUUID().toString();
  private final String S8 = UUID.randomUUID().toString();
  private final String E1 = UUID.randomUUID().toString();
  private final String E1R = UUID.randomUUID().toString();
  private final String E1RR = UUID.randomUUID().toString();
  private final String E1RRR = UUID.randomUUID().toString();
  private final String E1RRRR = UUID.randomUUID().toString();
  private final String E2 = UUID.randomUUID().toString();
  private final String E2R = UUID.randomUUID().toString();
  private final String E2RR = UUID.randomUUID().toString();
  private final String E2RRR = UUID.randomUUID().toString();
  private final String E2RRRR = UUID.randomUUID().toString();
  private final String E3 = UUID.randomUUID().toString();
  private final String E3R = UUID.randomUUID().toString();
  private final String E3RR = UUID.randomUUID().toString();
  private final String E3RRR = UUID.randomUUID().toString();
  private final String E3RRRR = UUID.randomUUID().toString();
  private final String E4 = UUID.randomUUID().toString();
  private final String E4R = UUID.randomUUID().toString();
  private final String E4RR = UUID.randomUUID().toString();
  private final String E4RRR = UUID.randomUUID().toString();
  private final String E4RRRR = UUID.randomUUID().toString();
  private final String E5 = UUID.randomUUID().toString();
  private final String E5R = UUID.randomUUID().toString();
  private final String E5RR = UUID.randomUUID().toString();
  private final String E5RRR = UUID.randomUUID().toString();
  private final String E5RRRR = UUID.randomUUID().toString();
  private final String E6 = UUID.randomUUID().toString();
  private final String E7 = UUID.randomUUID().toString();
  private final String E8 = UUID.randomUUID().toString();
  private final String E9 = UUID.randomUUID().toString();
  private final String E10 = UUID.randomUUID().toString();
  private final String E11 = UUID.randomUUID().toString();
  private final String E12 = UUID.randomUUID().toString();
  private final String E13 = UUID.randomUUID().toString();
  private final String M1 = UUID.randomUUID().toString();
  private final String M2 = UUID.randomUUID().toString();
  private final String M3 = UUID.randomUUID().toString();

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
            .additionalLabels(TypeB())
            .build());
  }

  @Override
  protected Set<RegisterNeighborRequest> getRegisterNeighborRequest() {
    return Set.of(
        // S
        builder().targetResourceId(B1).neighbor(CR_S(S1, "S1")).build(),
        builder().targetResourceId(S1).neighbor(RO_S(S1R, "S1R")).build(),
        builder().targetResourceId(S1R).neighbor(RO_S(S1RR, "S1RR")).build(),
        builder().targetResourceId(S1RR).neighbor(RO_S(S1RRR, "S1RRR")).build(),
        builder().targetResourceId(S1RRR).neighbor(RO_S(S1RRRR, "S1RRRR")).build(),
        builder().targetResourceId(B1).neighbor(CR_S(S2, "S2")).build(),
        builder().targetResourceId(S2).neighbor(RO_S(S2R, "S2R")).build(),
        builder().targetResourceId(S2R).neighbor(RO_S(S2RR, "S2RR")).build(),
        builder().targetResourceId(S2RR).neighbor(RO_S(S2RRR, "S2RRR")).build(),
        builder().targetResourceId(S2RRR).neighbor(RO_S(S2RRRR, "S2RRRR")).build(),
        builder().targetResourceId(B1).neighbor(CR_S(S3, "S3")).build(),
        builder().targetResourceId(S3).neighbor(RO_S(S3R, "S3R")).build(),
        builder().targetResourceId(S3R).neighbor(RO_S(S3RR, "S3RR")).build(),
        builder().targetResourceId(S3RR).neighbor(RO_S(S3RRR, "S3RRR")).build(),
        builder().targetResourceId(S3RRR).neighbor(RO_S(S3RRRR, "S3RRRR")).build(),
        builder().targetResourceId(B1).neighbor(CR_S(S4, "S4")).build(),
        builder().targetResourceId(S4).neighbor(RO_S(S4R, "S4R")).build(),
        builder().targetResourceId(S4R).neighbor(RO_S(S4RR, "S4RR")).build(),
        builder().targetResourceId(S4RR).neighbor(RO_S(S4RRR, "S4RRR")).build(),
        builder().targetResourceId(S4RRR).neighbor(RO_S(S4RRRR, "S4RRRR")).build(),
        builder().targetResourceId(B1).neighbor(CR_S(S5, "S5")).build(),
        builder().targetResourceId(S5).neighbor(RO_S(S5R, "S5R")).build(),
        builder().targetResourceId(S5R).neighbor(RO_S(S5RR, "S5RR")).build(),
        builder().targetResourceId(S5RR).neighbor(RO_S(S5RRR, "S5RRR")).build(),
        builder().targetResourceId(S5RRR).neighbor(RO_S(S5RRRR, "S5RRRR")).build(),

        // E
        builder().targetResourceId(S1).neighbor(CR_E(E1, "E1")).build(),
        builder().targetResourceId(S1R).neighbor(CR_E(E1R, "E1R")).build(),
        builder().targetResourceId(S1RR).neighbor(CR_E(E1RR, "E1RR")).build(),
        builder().targetResourceId(S1RRR).neighbor(CR_E(E1RRR, "E1RRR")).build(),
        builder().targetResourceId(S1RRRR).neighbor(CR_E(E1RRRR, "E1RRRR")).build(),
        builder().targetResourceId(S2).neighbor(CR_E(E2, "E2")).build(),
        builder().targetResourceId(S2R).neighbor(CR_E(E2R, "E2R")).build(),
        builder().targetResourceId(S2RR).neighbor(CR_E(E2RR, "E2RR")).build(),
        builder().targetResourceId(S2RRR).neighbor(CR_E(E2RRR, "E2RRR")).build(),
        builder().targetResourceId(S2RRRR).neighbor(CR_E(E2RRRR, "E2RRRR")).build(),
        builder().targetResourceId(S3).neighbor(CR_E(E3, "E3")).build(),
        builder().targetResourceId(S3R).neighbor(CR_E(E3R, "E3R")).build(),
        builder().targetResourceId(S3RR).neighbor(CR_E(E3RR, "E3RR")).build(),
        builder().targetResourceId(S3RRR).neighbor(CR_E(E3RRR, "E3RRR")).build(),
        builder().targetResourceId(S3RRRR).neighbor(CR_E(E3RRRR, "E3RRRR")).build(),
        builder().targetResourceId(S4).neighbor(CR_E(E4, "E4")).build(),
        builder().targetResourceId(S4R).neighbor(CR_E(E4R, "E4R")).build(),
        builder().targetResourceId(S4RR).neighbor(CR_E(E4RR, "E4RR")).build(),
        builder().targetResourceId(S4RRR).neighbor(CR_E(E4RRR, "E4RRR")).build(),
        builder().targetResourceId(S4RRRR).neighbor(CR_E(E4RRRR, "E4RRRR")).build(),
        builder().targetResourceId(S5).neighbor(CR_E(E5, "E5")).build(),
        builder().targetResourceId(S5R).neighbor(CR_E(E5R, "E5R")).build(),
        builder().targetResourceId(S5RR).neighbor(CR_E(E5RR, "E5RR")).build(),
        builder().targetResourceId(S5RRR).neighbor(CR_E(E5RRR, "E5RRR")).build(),
        builder().targetResourceId(S5RRRR).neighbor(CR_E(E5RRRR, "E5RRRR")).build(),
        builder().targetResourceId(B1).neighbor(CR_E(E6, "E6", "ref")).build(),
        builder().targetResourceId(B1).neighbor(CR_E(E7, "E7", "ref")).build(),
        builder().targetResourceId(B1).neighbor(CR_E(E8, "E8", "ref")).build(),
        builder().targetResourceId(B1).neighbor(CR_E(E9, "E9", "ref")).build(),
        builder().targetResourceId(B1).neighbor(CR_E(E10, "E10", "ref")).build(),

        // M
        builder().targetResourceId(B1).neighbor(CR_M(M1, "M1")).build(),
        builder().targetResourceId(B1).neighbor(CR_M(M2, "M2")).build(),
        builder().targetResourceId(B1).neighbor(CR_M(M3, "M3")).build(),
        builder().targetResourceId(M1).neighbor(CR_S(S6, "S6")).build(),
        builder().targetResourceId(M2).neighbor(CR_S(S7, "S7")).build(),
        builder().targetResourceId(M3).neighbor(CR_S(S8, "S8")).build(),
        builder().targetResourceId(S6).neighbor(CR_E(E11, "E11")).build(),
        builder().targetResourceId(S7).neighbor(CR_E(E12, "E12")).build(),
        builder().targetResourceId(S8).neighbor(CR_E(E13, "E13")).build());
  }

  private RegisterNeighborRequest.RegisterNeighborRequestBuilder builder() {
    return RegisterNeighborRequest.builder().projectId(PROJECT_ID);
  }

  @SneakyThrows
  private static NeighborRegistrationInfo RO_S(String id, String name) {
    return NeighborRegistrationInfo.builder()
        .id(id)
        .name(name)
        .relationshipType(REPRODUCTION_OF)
        .additionalLabels(TypeS())
        .inheritCreationConnectionFromTarget(true)
        .build();
  }

  @SneakyThrows
  private static NeighborRegistrationInfo CR_S(String id, String name) {
    return NeighborRegistrationInfo.builder()
        .id(id)
        .name(name)
        .relationshipType(CREATED_UNDER)
        .additionalLabels(TypeS())
        .build();
  }

  @SneakyThrows
  private static NeighborRegistrationInfo CR_E(String id, String name) {
    return NeighborRegistrationInfo.builder()
        .id(id)
        .name(name)
        .relationshipType(CREATED_UNDER)
        .additionalLabels(TypeE())
        .build();
  }

  @SneakyThrows
  private static NeighborRegistrationInfo CR_E(String id, String name, String context) {
    return NeighborRegistrationInfo.builder()
        .id(id)
        .name(name)
        .relationshipType(CREATED_UNDER)
        .relationshipContext(context)
        .additionalLabels(TypeE())
        .build();
  }

  @SneakyThrows
  private static NeighborRegistrationInfo CR_M(String id, String name) {
    return NeighborRegistrationInfo.builder()
        .id(id)
        .name(name)
        .relationshipType(CREATED_UNDER)
        .additionalLabels(TypeM())
        .build();
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
