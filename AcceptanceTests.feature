  Increment Acceptance Tests

  Increment 1: Boards, Hexes, Tiles, Players and Turns

  Feature: Place Tile on Board
      Player places a drawn Tile on the Board
     
      Scenario: Player performs initial Tile Placement
  Given Player is performing a Tile Placement
  When no existing Tiles have been placed on the Board
  Then Tile is placed in the center of the Board

      Scenario: Player performs Tile Placement in empty Board space
  Given Player is performing a Tile Placement
  When Player targets an empty space
  When there are no existing Tiles placed on the Board
  Then Tile is placed on the previously empty Board

  Scenario: Player performs Tile Placement in occupied Board space
    Given Player is performing a Tile Placement
    When Player specifies Tile location and orientation on the Board
    Then the Tile is placed onto the Board at the chosen location and orientation
     
Feature: Draw Tile from Deck
      Player draws a Tile from Deck for Tile Placement Action
     
      Scenario: Player draws Tile from Deck
  Given Player begins a Tile Placement
  When Player draws a Tile from Deck
  Then Player may place Tile from top of Deck
  And top of Deck replaced by new Tile


  Increment 2: Legal Tile Placement

  Feature: Tile Placement with Rules
      Player draws a Tile and places it legally on the Board

      Scenario: Player performs legal Tile Placement
  Given Player is performing a Tile Placement
  When target (i.e., lower) Hexes are of same level
  And target Hexes are not all part of the same Tile
  And new Tile’s Volcano Hex is directly over an existing Volcano Hex or empty board
  Then Tile is placed at specified location and orientation
         
      Scenario: Player performs illegal Tile Placement (different Levels)
  Given Player is performing a Tile Placement
  When target Hexes are not of same level
  Then Tile is not placed
  And Player is prompted for a legal move

  Scenario: Player performs illegal Tile Placement (Volcanos not overlapping)
    Given Player is performing a Tile Placement
    When new Tile’s Volcano Hex is not directly over an existing Volcano Hex or empty board
    Then Tile is not placed
    And Player is prompted for a legal move

      Scenario: Player performs illegal Tile Placement (all same Tile)
    Given a Player is performing a Tile Placement
    When target Hexes are all part of the same Tile
    Then Tile is not placed
    And Player is prompted for a legal move

      Scenario: Player performs illegal Tile Placement (not adjacent to existing Tiles)
    Given Player is performing a Tile Placement
    When there is at least one existing Tile placed on the Board
    And target Hexes are not adjacent to any existing Tiles on the Board
    Then Tile is not placed
    And Player is prompted for a legal move

     


  Increment 3: Villager Placement

Feature: Villager Placement with Rules
      Player performs a Build Action by placing a Villager, founding new Settlement

      Scenario: Player founds new Settlement on legal Hex
  Given a Player is performing a Build Action
  When Player attempts to found a new Settlement on target Hex
  And target Hex is neither a Volcano nor an empty Hex
  And target Hex has Level = 1
  And target Hex is not part of an existing Settlement
  Then Player places a Villager to found a new Settlement
  And Player’s score increases by 1
     
      Scenario: Player founds new Settlement on a Volcano or Empty Space
  Given a Player is performing a Build Action
  When Player attempts to found a new Settlement on target Hex
  And target Hex is either a Volcano or an empty Hex
  Then Player cannot place Villager and Settlement is not founded
  And Player is prompted for a legal move

      Scenario: Player founds new Settlement on Villager-occupied Hex
  Given a Player is performing a Build Action
  When Player attempts to found a new Settlement on target Hex
  And target Hex is already part of an existing Settlement
  Then Player cannot place Villager and Settlement is not founded
  And Player is prompted for a legal move

      Scenario: Player founds new Settlement on Hex of Level > 1
  Given a Player is performing a Build Action
  When Player attempts to found a new Settlement on target Hex
  And target Hex has Level > 1
  Then Player cannot place Villager and Settlement is not founded
  And Player is prompted for a legal move



  Feature: Nuking Villagers
      Player performs a Tile Placement that covers up a Hex that is Villager-occupied

      Scenario: Player performs a legal Tile Placement that overlaps with an existing Settlement
  Given a Player is performing a legal Tile Placement Action
  When a target Hex covers up a Hex with one or more Villagers
  Then those Villagers are not carried over to the new Hex
  And the Settlement is checked for division

     
  Increment 4: Settlements and Expansion
  Feature: Players can now expand existing Settlements

      Scenario: Player performs a legal Settlement expansion
  Given Player’s turn to perform a Build Action
  When Player targets a Hex for expansion
  And target Hex is adjacent to existing Settlement belonging to Player
  And target Hex is not a Volcano or empty Hex
  And target Hex does not already belong to an existing Settlement
  And Player has sufficient Villagers to expand onto all Hexes of like type with the target with which the target forms a contiguous group
  Then Player expands Settlement by placing Villagers on target Hexes
  And Player’s score increases by 1 per Villager per Villager’s Hex’s Level.

      Scenario: Player performs an illegal Settlement expansion (Volcano or non-Hex target)
  Given Player’s turn to perform a Build Action
  When Player targets a Hex for expansion
  And target Hex is a Volcano or empty Hex
  Then Player cannot place Villagers and Settlement is not expanded
  And Player is prompted for a legal move
     
     




           Scenario: Player performs an illegal Settlement expansion (insufficient Villagers)
  Given Player’s turn to perform a Build Action
  When Player targets a Hex for expansions
  And Player has insufficient Villagers to expand onto all Hexes of like type with which the target Hex forms a contiguous group
  Then Player cannot place Villagers and Settlement is not expanded
  And Player is prompted for a legal move

      Scenario: Player performs an illegal Settlement expansion (occupied target)
  Given Player’s turn to perform a Build Action
  When Player targets a Hex for expansion
  And target Hex already belongs to an existing Settlement
  Then Player cannot place Villagers and Settlement is not expanded
  And Player is prompted for a legal move

     

  Scenario: Player performs an illegal Settlement expansion (non-adjacent target)
    Given Player’s turn to perform a Build Action
    When Player targets a Hex for expansion
    And Player has insufficient Villagers to expand onto all Hexes of like type with which the target Hex forms a contiguous group
    Then Player cannot place Villagers and Settlement is not expanded
    And Player is prompted for a legal move

  Increment 5: Totoro Placement
Feature: Players can now place a Totoro next to a sufficiently large Settlement

      Scenario: Player places a Totoro legally
  Given Player is performing a Build Action
  And Player has at least one remaining Totoro
  When Player attempts to place a Totoro on a Settlement-adjacent target Hex
  And Settlement is of Size >= 5
  And Settlement does not already have a Totoro
  And target Hex is not a Volcano or empty
  And target Hex is not already part of an existing Settlement
  Then the Settlement is expanded by placing Totoro on target Hex
  And Player’s score increased by 200

      Scenario: Player places a Totoro next to a Settlement of Size < 5
  Given Player is performing a Build Action
  And Player has at least one remaining Totoro
  When Player attempts to place a Totoro on a Settlement-adjacent target Hex
  And Settlement is of Size < 5
  Then Totoro is not placed
  And Player is prompted for a legal move
     
      Scenario: Player places a Totoro next to a Settlement with an existing Totoro
  Given Player is performing a Build Action
  And Player has at least one remaining Totoro
  When Player attempts to place a Totoro on a Settlement-adjacent target Hex
  And Settlement has an existing Totoro
  Then Totoro is not placed
  And Player is prompted for a legal move

      Scenario: Player places a Totoro on an occupied Hex
  Given Player is performing a Build Action
  And Player has at least one remaining Totoro
  When Player attempts to place a Totoro on a Settlement-adjacent target Hex
  And target Hex is occupied by a Villager(s) or Totoro
  Then Totoro is not placed
  And Player is prompted for a legal move

      Scenario: Player places a Totoro on a Volcano or non-Hex
  Given Player is performing a Build Action
  And Player has at least one remaining Totoro
  When Player attempts to place a Totoro on a Settlement-adjacent target Hex
  And target Hex is a Volcano or empty
  Then Totoro is not placed
  And Player is prompted for a legal move

      Scenario: Player places a Totoro on a Hex that is not Settlement-adjacent
  Given Player is performing a Build Action
  And Player has at least one remaining Totoro
  When Player attempts to place Totoro on Settlement-unadjacent target Hex
  Then Totoro is not placed
  And Player is prompted for a legal move
     
  Scenario: Player places a Totoro with no unplaced Totoros remaining
    Given Player is performing a Build Action
    And Player has no remaining unplaced Totoros
    When Player attempts to place a Totoro on a Settlement-adjacent target Hex
    Then Totoro is not placed
    And Player is prompted for a legal move

  Scenario: Player performs Tile Placement on a Totoro-occupied Hex
    Given Player is performing a Build Action
    And Player has at least one remaining Totoro
    When Player attempts to place a Totoro on a Settlement-adjacent target Hex
    And target Hex is already occupied by a Totoro
    Then Totoro is not placed
    And Player is prompted for a legal move



  Increment 6: Resource Pool and Game End Condition
Feature: Player can now end the game by placing their last unplaced Villager.

      Scenario: Player exhausts Build Resources, ending game
  Given Player’s turn to perform a Build Action
  When Player performs a legal Build Action
  And that Build Action exhausts the Player’s remaining Build Resources
  Then game ends and highest scoring Player wins
         
  Increment 7: Game Loss Condition
  Feature: The game now evaluates possible Build Actions and ends if a Player has no legal Build Action available

      Scenario: Player has no legal Build Action available
  Given Player’s turn to perform a Build Action
  When no legal Build Action exists that can be performed
  Then game ends and current Player loses
             
      Scenario: Player performs an illegal Build Action with a legal move available
  Given Player’s turn to perform a Build Action
  And a legal Build Action exists that can be performed
  When Player attempts to perform an illegal Build Action
  Then move is rejected
  And Player is prompted for a legal move
