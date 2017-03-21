Feature: Add a class name to a CRC card

  Scenario: Add a class name to a CRC card
    Given a default CRC card
    When I add class name Person to the CRC card
    Then The class name Person should be added to the default CRC card