# TigerIsland Client, Team O
CEN3031 Spring 2017

All game functionality has been implemented.

  + Meeples, Totoros, and Tigers are able to be placed
  + Settlements are able to be built and expanded
  + Nuking 

## Setup and Install
Clone repository using IntelliJ IDEA.

Add JUnit 1.2.5 and Cucumber Java8 1.2.5 to dependencies via the Project Structure menu.

Set TigerIsland.java as the main class in your configuration.

## GUI Mode
To deploy the GUI, use "UI" (without quotations) as the only command-line argument. 

Note: For performance reasons the GUI and networking functionality are mutually exclusive. 

Games can be reconstructed from logs in the GUI by feeding past-tense server messages into the Parser.

## Tournament Mode
To deploy the network client, use the command line arguments:
 + server's IP address
 + target port
 + tournament password
 + team login name
 + team password

If the server is running, the client will connect, automatically authenticate, and will wait for a challenge to begin.

##Tests

Have JUnit and Cucumber tests 

##AI Strategy 

Priority:
  1. Tigers/Totoros
  2. Nuking 
  3. Place a tile random tile
