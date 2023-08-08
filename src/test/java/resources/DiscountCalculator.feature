#calculating percentage discount
Feature: Discount calculator

  Scenario: Calculate discount as per given amount and condition
    Given I have an amount of 1000
    And I have selected discount type "percentage"
    And The percentage is 10
    When I calculate the discount
    Then The calculated amount should be 900

  Scenario: Calculate discount as per given amount and condition
    Given I have an amount of 1000
    And I have selected discount type "flat"
    And The flat discount is 20
    When I calculate the discount
    Then The calculated amount should be 980

  Scenario: Calculate discount as per given amount and condition
    Given I have an amount of 1000
    And I have selected discount type "both"
    And The percentage is 10
    And The flat discount is 20
    And I have selected "lower" as applicable type
    When I calculate the discount
    Then The calculated amount should be 980

  Scenario Outline: Calculate discount as per given amount and condition
    Given I have an amount of <amount>
    And I have selected discount type "<discount_type>"
    And The percentage is <percentage>
    And The flat discount is <flat_discount>
    And I have selected "<applicable_type>" as applicable type
    When I calculate the discount
    Then The calculated amount should be <calculated_amount>

    Examples:
      | amount | discount_type | percentage | flat_discount | applicable_type | calculated_amount |
      | 1000   | percentage    | 10         | 0             | null            | 900               |
      | 1000   | flat          | 0          | 80            | null            | 920               |
      | 1000   | both          | 10         | 30            | lower           | 970               |
      | 1000   | both          | 10         | 25            | higher          | 900               |
      | 1000   | both          | 10         | 25            | combination     | 875               |
