Feature: Add a class name to a CRC card

  Scenario: Add a class name to a CRC card
    Given a default CRC card
    When I add class name Person to the CRC card
    Then The class name Person should be added to the default CRC card

  Scenario Outline: Invalid class name must not added to the CRC card
      Given a default CRC card
    When I add class name <className> to the CRC card
    Then the class name <className> should be rejected with the message <message>

    Examples:
      | className | message                              |
      | 1Invalid  | '1Invalid' is not a valid class name |
      | invalid   | 'invalid' is not a valid class name  |