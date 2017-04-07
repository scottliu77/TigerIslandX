import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Wylie on 3/23/2017.
 */
public class TilePlacementTest 
{

    private static final int BOX_SIZE = 40;
    private static final int X_OFFSET = BOX_SIZE * 3 / 4;
    private static final int Y_OFFSET = BOX_SIZE / 2;

    private static final Point center = new Point(0, 0);

    private static final Point[] posPoints =
            {
                    new Point(center.x, center.y - BOX_SIZE),
                    new Point(center.x + X_OFFSET, center.y - Y_OFFSET),
                    new Point(center.x + X_OFFSET, center.y + Y_OFFSET),
                    new Point(center.x, center.y + BOX_SIZE),
                    new Point(center.x - X_OFFSET, center.y + Y_OFFSET),
                    new Point(center.x - X_OFFSET, center.y - Y_OFFSET)
            };

    @Test
    public void testTileConstruction() throws Exception
    {
        Random rand = new Random();
        Hex rockyHex = new Hex(Terrain.ROCKY);
        Hex lakeHex = new Hex(Terrain.LAKE);

        int tileId = rand.nextInt(100);

        Tile tile = new Tile(rockyHex, lakeHex, tileId, Orientation.N);

        assert(tile.getA() == rockyHex && tile.getB() == lakeHex && tile.getVolcano().getTerrain() == Terrain.VOLCANO && tile.getTileId() == tileId);
    }

    // This one's way too complicated for a unit test so I moved most of it to the acceptance test scenarios
    @Test
    public void testTilePlacement() throws Exception
    {
        Random rand = new Random();
        Orientation orient = Orientation.values()[3];

        Board board = new Board(null, true);
        Deck deck = board.getDeck();

        Tile tile = deck.getTopTile();
        tile.setOrientation(orient);

        Hex volcano = tile.getVolcano();
        Hex hexA = tile.getA();
        Hex hexB = tile.getB();

        HashMap<Point, HexButton> buttonMap = board.getButtonMap();
        ArrayList<Point> pointList = new ArrayList<Point>(buttonMap.keySet());

        Point initialPoint = pointList.get(0);
        // assuming orientation 3
        Point pointA = new Point(initialPoint.x + 20, initialPoint.y + 30);
        Point pointB = new Point(initialPoint.x - 20, initialPoint.y + 30);

        board.placeTile(initialPoint, orient);

        HexButton targetButton = buttonMap.get(initialPoint);
        HexButton buttonA = buttonMap.get(pointA);
        HexButton buttonB = buttonMap.get(pointB);

        Hex targetHex = targetButton.getHex();
        Hex hexExpectedA = buttonA.getHex();
        Hex hexExpectedB = buttonB.getHex();

        assert(volcano == targetHex && hexA == hexExpectedA && hexB == hexExpectedB);
    }
}
