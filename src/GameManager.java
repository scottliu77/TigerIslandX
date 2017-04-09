import java.util.*;
import java.awt.*;

// GameManager is an intermediary class between the TigerPanel and the game Menu.
public class GameManager
{
    private Board board;
    private Building activeBuilding;
    private Terrain activeTerrain;
    private boolean expandNext;
    private Parser parser;

    private TilePlacementMove storedTilePlacement;

    public static final Point hexOffsetPoint = new Point( 256 + 236,  128 + 236);

    GameManager(boolean playerFirst, Parser p)
    {
        board = new Board(this, playerFirst);
        parser = p;
        activeBuilding = Building.VILLAGER;
        activeTerrain = Terrain.GRASS;
        expandNext = false;
    }
    GameManager(boolean playerFirst)
    {
        board = new Board(this, playerFirst);
        activeBuilding = Building.VILLAGER;
        activeTerrain = Terrain.GRASS;
        expandNext = false;
        parser = new Parser(this);
    }

    public PlayerMove generateMove(Point targetPoint)
    {
        if(!board.getTilePlaced())
        {
            return new TilePlacementMove(board.getActivePlayer(), board.getHexButton(targetPoint), board.getDeck().getOrientation());
        }
        else if (!expandNext)
        {
            return new BuildingPlacementMove(board.getActivePlayer(), board.getHexButton(targetPoint), activeBuilding);
        }
        else
        {
            return new SettlementExpansionMove(board.getActivePlayer(), board.getSettlementManager().getSettlement(targetPoint), activeTerrain);
        }
    }

    public boolean moveIsLegal(Point targetPoint)
    {
        HexButton hexButton = board.getHexButton(targetPoint);
        if(!board.getTilePlaced())
        {
            Tile tile = board.getDeck().getTopTile();
            return board.tilePlacementIsLegal(tile, hexButton);
        }
        else if(!expandNext)
        {
            return board.buildingPlacementIsLegal(activeBuilding, hexButton);
        }
        else
        {
            SettlementManager settlementManager = board.getSettlementManager();
            Settlement targetSettlement = settlementManager.getSettlement(board.getHexButton(targetPoint));
            if(targetSettlement == null)
            {
                return false;
            }

            if(targetSettlement.getOwner() != board.getActivePlayer())
            {
                System.out.println("Illegal move: settlement owner not active player");
                return false;
            }

            Expansion expansion = settlementManager.getExpansion(targetSettlement, activeTerrain);
            return board.settlementExpansionIsLegal(expansion);
        }
    }

    public void sendForfeitSignal(Player loser)
    {
        parser.extractAndSendAction(board.getStoredTilePlacement());
    }

    public void sendVictorySignal(Player winner)
    {

    }

    public void processTurn(PlayerMove playerMove)
    {
        board.processTurn(playerMove);
    }


    public void resetHexes()
    {
        board.resetButtonMap();
    }

    public void resetWithOneHex()
    {
        board.resetWithOneHex();
    }

    public Board getBoard()
    {
        return board;
    }

    public Building getActiveBuilding()
    {
        return activeBuilding;
    }

    public void setActiveBuilding(Building building)
    {
        activeBuilding = building;
    }

    public void setActiveTerrain(Terrain terrain)
    {
        activeTerrain = terrain;
    }

    public HashMap<Point, HexButton> getButtonMap()
    {
        return board.getButtonMap();
    }

    public void resetDeck()
    {
        board.resetDeck();
    }

    public void setExpandNext(boolean state)
    {
        expandNext = state;
    }

    public boolean getExpandNext()
    {
        return expandNext;
    }

    public Terrain getActiveTerrain()
    {
        return activeTerrain;
    }

    public void sendInstaWinSignal(Player winner) {

    }

    public void selectAndPlayMove()
    {
        board.getActiveAnalyzer().selectAndPlayMove();
    }

    public void resolveGame()
    {
        while(board.getGameResult() == null)
        {
            long startTime = System.currentTimeMillis();
            board.getActiveAnalyzer().selectAndPlayMove(); // Tile Placement Action
            board.getActiveAnalyzer().selectAndPlayMove(); // Build Action
            long endTime = System.currentTimeMillis();
            System.out.println("Time elapsed during turn: " + (endTime - startTime) + " ms");
        }
    }

    public Point getHexOffsetPoint()
    {
        return hexOffsetPoint;
    }

    public void parseMoves(TilePlacementMove storedTilePlacement, PlayerMove playerMove)
    {
        parser.extractAndSendAction(storedTilePlacement, playerMove);
    }

    public void sendMessageToParser(String s)
    {
        parser.receiveMessage(s);
    }
}
