import org.junit.Test;

import java.awt.*;
import java.util.HashMap;

/**
 * Created by rahee on 3/26/2017.
 */
public class BoardTest {
    @Test
    public void BoardResetWithCenterHex() throws Exception{
        HashMap<Point, HexButton> buttonMap = new HashMap<Point, HexButton>();
        Point point = new Point(256 + 236, 128 + 236);
        GameManager manager = null;
        buttonMap.put(point, new HexButton(point, new Hex(Terrain.EMPTY), manager));
       
    }
}
