Feature: Fetching the lineage of a resource

  Scenario: Lineage of a resource that does not exist
    Given a resource with id "randomId" that does not exist
    When the consumer asks for the lineage of resource with id "randomId"
    Then the lineage response should be empty

  Scenario: Lineage of resource that is not connected to any other resource
    Given resources that are not connected to any other resources:
      | type | name | id                                   | projectId                            |
      | B    | B1   | 712428a2-fd27-4e4b-b183-7c6887e164d1 | 43882dec-d762-4dd9-a323-2fe3cf3f449e |
    When the consumer asks for the lineage of resource with id "712428a2-fd27-4e4b-b183-7c6887e164d1"
    Then the lineage response should be empty

#  Scenario: Lineage of resource that is not connected to any other resource
#    Given resources that are not connected to any other resources:
#      | type | name | id                                   | projectId                            |
#      | B    | B1   | 712428a2-fd27-4e4b-b183-7c6887e164d1 | 43882dec-d762-4dd9-a323-2fe3cf3f449e |
#    When the consumer asks for the lineage of resource with id "712428a2-fd27-4e4b-b183-7c6887e164d1"
#    Then the lineage response should contain only the target resource:
#      | type | name | id                                   | projectId                            | parent |
#      | B    | B1   | 712428a2-fd27-4e4b-b183-7c6887e164d1 | 43882dec-d762-4dd9-a323-2fe3cf3f449e |        |
