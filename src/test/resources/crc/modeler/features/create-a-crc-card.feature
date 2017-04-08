Feature: Create a CRC card
"""
  A CRC card must created must create with a valid class name
  """

  Scenario: Create a new CRC card with a valid class name
    Given Book a valid class name
    When I create a new CRC card
    Then the new CRC card should created with the next CRC card id and the given class name

  Scenario Outline: Create a new CRC card with an invalid class name
    Given <className> an invalid class name
    When I create a new CRC card
    Then the new CRC card should rejected with the 'invalid class name' error containing the invalid class name <className>

    Examples:
      | className |
      | 1Invalid  |
      | invalid   |
