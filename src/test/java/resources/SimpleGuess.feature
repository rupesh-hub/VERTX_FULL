Feature: SimpleGuess even or odd play
  Scenario: Play SimpleGuess to get Guess
    Given Create a SimpleGuess game play
    When I play with number 2
    Then The result is "EVEN"

  Scenario: Play SimpleGuess to get Guess
    Given Create a SimpleGuess game play
    When I play with number 1
    Then The result is "ODD"

  Scenario: Play SimpleGuess to get Guess
    Given Create a SimpleGuess game play
    When I play with number -1
    Then The result is "ODD"
