import java.util.*;
import java.awt.*;

// GameManager is an intermediary class between the TigerPanel and the game Menu.
public class GameManager
{
    private Board board;
    private Building activeBuilding;
    private Terrain activeTerrain;
    private boolean expandNext;

    GameManager()
    {
        board = new Board(this);
        activeBuilding = Building.VILLAGER;
        activeTerrain = Terrain.GRASS;
        expandNext = false;
    }

    public PlayerMove generateMove(Point targetPoint)
    {
        if(!board.getTilePlaced())
        {
            return new TilePlacementMove(board.getActivePlayer(), targetPoint);
        }
        else if (!expandNext)
        {
            return new BuildingPlacementMove(board.getActivePlayer(), targetPoint, activeBuilding);
        }
        else
        {
            return new SettlementExpansionMove(board.getActivePlayer(), targetPoint, activeTerrain);
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

    public void processTurn(PlayerMove playerMove)
    {
        board.processTurn(playerMove);
    }

    public void emptyHexes()
    {
        board.clearHexes();
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
        expandNext = false;
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
}
