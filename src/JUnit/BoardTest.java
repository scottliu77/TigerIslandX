import org.junit.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import static java.awt.image.ImageObserver.HEIGHT;
import static java.awt.image.ImageObserver.WIDTH;


public class BoardTest {
    private Object manager;

    @Test
    public void BoardResetWithCenterHex() throws Exception {
        HashMap<Point, HexButton> buttonMap = new HashMap<Point, HexButton>();
        Point point = new Point(256 + 236, 128 + 236);
        GameManager manager = null;
        buttonMap.put(point, new HexButton(point, new Hex(Terrain.EMPTY), manager));

    }

    @Test
    public void EndGame() throws Exception {
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
                Player tieVictor = null;
                winner = tieVictor;

            }
            assert (winner == player1);

        }
    }

    @Test
    public void ForfeitGame() throws Exception{
        Object player1 = null;
        Object player2 = null;
        Object loser = player1;
        Object winner;

        if (loser == player1) {

            winner = player2;
        } else {
            winner = player1;
        }
        assert (winner == player2);
    }

    @Test
    public void HexTemplate() throws Exception {


        int hexagonX[] = {10, 30, 40, 30, 10, 0};
        int hexagonY[] = {0, 0, 20, 40, 40, 20};
        Polygon hexagon = new Polygon(hexagonX, hexagonY, 6);


        BufferedImage hexTemplate = new BufferedImage(41, 41, BufferedImage.TYPE_INT_ARGB);
        Graphics2D templateG2 = hexTemplate.createGraphics();
        templateG2.setColor(Color.BLACK);
        templateG2.draw(hexagon);

    }

      @Test
    public void ResetButtonMap() throws Exception {
        HashMap<Point, HexButton> buttonMap = new HashMap<Point, HexButton>();

        int startX = 256;
        int startY = 128;
        int hexBoxSize = 40;
        int hexVertOffset = 20;
        int hexHoriOffset = 30;

        int xOffset = 0;
        int yOffset = 0;
        for (int i = startX + xOffset; i <= ((startX + WIDTH) - hexBoxSize); i += hexHoriOffset * 2) {
            for (int j = startY + yOffset; j <= startY + HEIGHT - hexBoxSize; j += hexVertOffset * 2) {
                Point point = new Point(i, j);
                Hex hex = new Hex(Terrain.EMPTY);
                HexButton hexButton = new HexButton(point, hex, (GameManager) manager);
               assert(buttonMap.put(point, hexButton)!=null);
            }
        }

        for (int i = startX + hexHoriOffset + xOffset; i <= ((startX + WIDTH) - hexBoxSize); i += hexHoriOffset * 2) {
            for (int j = startY + hexVertOffset + yOffset; j <= ((startY + HEIGHT) - hexBoxSize); j += hexVertOffset * 2) {
                Point point = new Point(i, j);
                Hex hex = new Hex(Terrain.EMPTY);
                HexButton hexButton = new HexButton(point, hex, (GameManager) manager);
              assert(  buttonMap.put(point, hexButton)!=null);

            }
        }
    }
}

