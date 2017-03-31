
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
    
        @Test
    public void endGame() throws Exception {
        {
            Player player1 = null;
            int score1 = 1;
            Player player2 = null;
            int score2 = 0;
            Player winner;

            if (score1 > score2) {
                winner = player1;

            } else if (score2 > score1) {
                winner = player2;

            } else {
                Player tieVictor =null;
                winner = tieVictor;

            }
           assert (winner ==player1);

        }
    }
}

    
}
