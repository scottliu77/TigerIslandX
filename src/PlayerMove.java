import java.awt.*;

/**
 * Created by Wylie on 3/22/2017.
 */
interface PlayerMove
{
    public void execute(Board board);
}

class TilePlacementMove implements PlayerMove
{
    private Player player;
    private Point targetPoint;

    public TilePlacementMove(Player player, Point targetPoint)
    {
        this.player = player;
        this.targetPoint = targetPoint;
    }

    public void execute(Board board)
    {
        board.placeTile(targetPoint);
    }
}

class BuildingPlacementMove implements PlayerMove
{
    private Player player;
    private Point targetPoint;
    private Building building;

    public BuildingPlacementMove(Player player, Point targetPoint, Building building)
    {
        this.player = player;
        this.targetPoint = targetPoint;
        this.building = building;
    }

    public void execute(Board board)
    {
        board.placeBuilding(targetPoint, building);
        int meepleCost = 1;
        if(building == Building.VILLAGER)
        {
            meepleCost = board.getHexButton(targetPoint).getHex().getLevel();
        }
        player.consumeMeeples(building, meepleCost);
    }
}
