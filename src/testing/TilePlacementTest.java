import org.junit.Test;

import java.awt.*;
import java.util.Random;

/**
 * Created by Wylie on 3/23/2017.
 */
public class TilePlacementTest 
{
    @Test
    public void testTileConstruction() throws Exception
    {
        Random rand = new Random();
        Hex rockyHex = new Hex(Terrain.ROCKY);
        Hex lakeHex = new Hex(Terrain.LAKE);

        int tileId = rand.nextInt(100);

        Tile tile = new Tile(rockyHex, lakeHex, tileId, Orientation.N);

        assert(tile.getA() == rockyHex && tile.getB() == lakeHex && tile.getVolcano().getTerrain() == Terrain.VOLCANO);
    }
}
