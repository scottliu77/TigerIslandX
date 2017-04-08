import java.awt.*;

/**
 * Created by Wylie on 3/22/2017.
 */
interface PlayerMove
{
    void execute(Board board);
}

class TilePlacementMove implements PlayerMove
{
    private Player player;
    private HexButton targetHex;
    private Orientation orientation;

    public TilePlacementMove(Player player, HexButton targetHex, Orientation orientation)
    {
        this.player = player;
        this.targetHex = targetHex;
        this.orientation = orientation;
    }

    public void execute(Board board)
    {
        board.placeTile(targetHex.getOrigin(), orientation);
    }

    public HexButton getTargetHex()
    {
        return targetHex;
    }

    public Orientation getOrientation()
    {
        return orientation;
    }
}

class BuildingPlacementMove implements PlayerMove
{
    //private Point targetPoint;
    private HexButton targetHex;
    private Building building;

    public BuildingPlacementMove(Player player, HexButton targetHex, Building building)
    {
        this.targetHex = targetHex;
        this.building = building;
    }

    public Building getBuilding()
    {
        return building;
    }

    public HexButton getTargetHex()
    {
        return targetHex;
    }

    public void execute(Board board)
    {
        board.placeBuilding(targetHex.getOrigin(), building);
    }
}

class SettlementExpansionMove implements PlayerMove
{
    //private Point targetPoint;
    private Settlement settlement;
    private Terrain terrain;

    public SettlementExpansionMove(Player player, Settlement settlement, Terrain terrain)
    {
        this.settlement = settlement;
        this.terrain = terrain;
    }

    public Terrain getTerrain() { return terrain; }

    public Settlement getSettlement()
    {
        return settlement;
    }

    public void execute(Board board) { board.expandSettlement(settlement, terrain);}
}
