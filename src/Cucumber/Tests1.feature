Feature: Tile Placement

  Scenario: Place tile on board
    Given Hex and neighbors initialized
    When Player selects a target Hex
    Then Volcano placed at target Hex
      And Tile's Hex A placed at Volcano's neighbor 1
      And Tile's Hex B placed at Volcano's neighbor 2

  Scenario: Place tile legally
    Given Hex and neighbors initialized
      And target Hexes are not all part of the same Tile
      And target Hex is Volcano or Empty
    When Player selects a target Hex
    Then Volcano placed at target Hex
      And Tile's Hex A placed at Volcano's neighbor 1
      And Tile's Hex B placed at Volcano's neighbor 2

  Scenario: Place tile on non-Volcano, non-Empty target Hex
    Given Hex and neighbors initialized
      And target Hex is not Volcano
    When Player selects a target Hex
    Then target Hex and neighbors retain original Hexes

  Scenario: Place tile directly over another tile
    Given Hex and neighbors initialized
      And target Hex and neighbors share a tile
    When Player selects a target Hex
    Then target Hex and neighbors retain original Hexes

  Scenario: Place tile that creates a second island
    Given Hex and neighbors initialized
      And initial tile placed
    When Player selects a distant Hex
    Then distant Hex retains original Hex
