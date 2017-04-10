import javafx.geometry.Point3D;
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
        buttonMap.put(point, new HexButton(point, new Point3D(0,0,0), new Hex(Terrain.EMPTY), manager));

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


    /*
     @Test
    public void InstaWin() throws Exception {
        Object winner = null;
        this.winner = winner;
        GameResult gameResult = (GameResult) winner;
        assert (gameResult == this.winner);
    }
    
    
       @Test
    public void ProcesesTurn() throws Exception {


        Object gameResult = null;
        SwingWorker playerMove = null;
        if (playerMove != null && gameResult == null) {
            boolean tilePlaced = true;
            MoveAnalyzer activeAnalyzer = null;
            SettlementManager settlementManager = null;
            Player activePlayer = null;
            if (!tilePlaced) // if tilePlaced is false, Board expects a Tile Placement
            {

                tilePlaced = true;
                settlementManager.updateSettlements();
                activeAnalyzer.analyze();

                if (activeAnalyzer.noPossibleBuildActions()) {
                    boolean forfeitGame = true;
                }

            } else // if tilePlaced is true, Board expects a Build Action
            {

                tilePlaced = false;

                int deck = 0;
                settlementManager.updateSettlements();
                activeAnalyzer.analyze();

                if (activePlayer.instaWins()) {
                    boolean instaWin = true;
                    activePlayer = (Player) winner;
                } else if (deck > 48) {
                    boolean endGame = true;
                    assert (endGame == false);
                }

                Player player1 = null;
                Player player2 = null;
                activePlayer = (activePlayer == player1 ? player2 : player1);
                assert (activePlayer == player1);
            }
        }

    }

    */


}

