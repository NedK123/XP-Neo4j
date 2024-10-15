Feature: Fetching the lineage of a resource

  Scenario: Lineage of a resource that does not exist
    Given a resource with id "randomId" that does not exist
    When the consumer asks for the lineage of resource with id randomId
    Then the lineage response should be empty

  Scenario: Lineage of a resource that is not connected to any other resource
    Given resources that are not connected to any other resources:
      | type | name | id     | projectId                            | base |
      | B    | B1   | B1-100 | 43882dec-d762-4dd9-a323-2fe3cf3f449e | true |
    When the consumer asks for the lineage of resource with id B1-100
    Then the lineage response should be empty

  Scenario: Lineage of a resource that is connected to only one other resource
    Given resources that are not connected to any other resources:
      | type | name | id     | projectId                            | base |
      | B    | B1   | B1-100 | 43882dec-d762-4dd9-a323-2fe3cf3f449e | true |
    And resources that are connected to each other:
      | parent | relationType  | relationContext | type | name | id     | inheritCreationContext |
      | B1-100 | CREATED_UNDER |                 | E    | E1   | E1-100 |                        |
    When the consumer asks for the lineage of resource with id E1-100
    Then the lineage response should contain:
      | type | name | id     | parent |
      | E    | E1   | E1-100 | B1-100 |
      | B    | B1   | B1-100 |        |
