# TigerIsland Client, Team O
CEN3031 Spring 2017

All game functionality has been implemented.

  -Meeples, Totoros, & Tigers are able to be placed.
  
  -Settlements are able to be built. 
  
  -Nuking works. 

## Setup and Install
Clone repository using IntelliJ IDEA. 

Add JUnit 1.2.5 and Cucumber Java8 1.2.5 to dependencies via the Project Structure menu.

Set TigerIsland.java as the main class in your configuration.

## GUI Mode
To deploy the GUI, use "UI" (without quotations) as the only command-line argument. 

Note: For performance reasons the GUI and networking functionality are mutually exclusive. 

Games can be reconstructed from logs in the GUI by feeding past-tense server messages into the Parser.

GUI: You can play as the user, AI can play or you can autoplay an entire game. AI successfully plays against itself.

## Tournament Mode
To deploy the network client, use the server's IP address, target port, tournament password, team login name and team password as arguments.

If the server is running, the client will connect and begin resolving signals automatically.

Can successfully see the move being placed and result of the games played.
