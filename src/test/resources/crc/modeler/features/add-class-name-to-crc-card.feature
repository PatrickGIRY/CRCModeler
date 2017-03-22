Feature: Add a class name to a CRC card

  Scenario: Add a class name to a CRC card
    Given a default CRC card
    When I add class name Person to the CRC card
    Then The class name Person should be added to the default CRC card

    @wip
    Scenario: Invalid class name must not added to the CRC card
      Given a default CRC card
      When I add class name 1Invalid to the CRC card
      Then the class name 1Invalid should be rejected