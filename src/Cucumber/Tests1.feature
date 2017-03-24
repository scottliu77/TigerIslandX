Feature: Tile Placement

  Scenario: Place tile on board
    Given Hex and neighbors initialized
    When Player selects a target Hex
    Then Volcano placed at target Hex
      And Tile's Hex A placed at Volcano's neighbor 1
      And Tile's Hex B placed at Volcano's neighbor 2