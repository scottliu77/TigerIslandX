import org.junit.*;
import sun.jvm.hotspot.oops.ExceptionTableElement;

import java.awt.*;

public class PlayerTest {


    @Test
    public void testVillagerResources() throws Exception{
        Player player1 = new Player("Player 1", Color.WHITE, Color.BLACK);
        int startVillagerPool = 19;
        assert (player1.getVillagers() == startVillagerPool);
    }

    @Test
    public void testShamanResources() throws Exception {
        Player player1 = new Player("Player 1", Color.WHITE, Color.BLACK);
        int startShamanPool = 1;
        assert (player1.getShaman() == startShamanPool);
    }

    @Test
    public void testTotoroResources() throws Exception{
        Player player1 = new Player("Player 1", Color.WHITE, Color.BLACK);
        int startTotoroPool = 3;
        assert (player1.getTotoros() == startTotoroPool);
    }

    @Test
    public void testTigerResources() throws Exception{
        Player player1 = new Player("Player 1", Color.WHITE, Color.BLACK);
        int startTigerPool = 2;
        assert (player1.getTigers() == startTigerPool);
    }
    @Test
    //This test could be split in 3 parts if desired
    public void testResourceDepletion() throws Exception {
        Player player1 = new Player("Player 1", Color.WHITE, Color.BLACK);
        int targetVillager = 11;
        int targetTotoro = 2;
        int targetTiger = 1;
        int targetShaman = 1;
        player1.decreaseTigers();
        player1.decreaseTotoros();
        player1.decreaseShaman();
        player1.decreaseVillagers(8);
        assert (player1.getTigers() == targetTiger && player1.getTotoros() == targetTotoro && player1.getVillagers() == targetVillager);
    }
    @Test
    public void testConsumeMeeple() throws Exception {
        Player player1 = new Player("Player 1", Color.WHITE, Color.BLACK);
        player1.consumeMeeples(Building.VILLAGER,13);
        player1.consumeMeeples(Building.TOTORO,2);
        player1.consumeMeeples(Building.TIGER,2);
        assert (player1.getVillagers()==6 && player1.getTotoros()==1 && player1.getTigers()==0);
    }
    @Test
    public void testResourceReset() throws Exception {
        Player player1 = new Player("Player 1", Color.WHITE, Color.BLACK);
        int targetVillager = 19;
        int targetTotoro = 3;
        int targetTiger = 2;
        int targetShaman = 1;
        player1.decreaseTigers();
        player1.decreaseTotoros();
        player1.decreaseVillagers(8);
        player1 .decreaseShaman();
        player1.resetResources();
        assert (player1.getTigers() == targetTiger && player1.getTotoros() == targetTotoro && player1.getVillagers() == targetVillager
        && player1.getShaman() == targetShaman);
    }
    @Test
    public void testOutOfResources() throws Exception {
        Player player1 = new Player("Player 1", Color.WHITE, Color.BLACK);
        player1.decreaseTigers();
        player1.decreaseTigers();
        player1.decreaseTotoros();
        player1.decreaseTotoros();
        player1.decreaseTotoros();
        player1.decreaseShaman();
        player1.decreaseVillagers(19);
        assert (player1.outOfResources() == true);
    }
    @Test
    public void testScore() throws Exception {
        Player player1 = new Player("Player 1", Color.WHITE, Color.BLACK);
        player1.increaseScore(123456);
        assert (player1.getScore()==123456);
    }
    @Test
    public void testScoreReset() throws Exception {
        Player player1 = new Player("Player 1", Color.WHITE, Color.BLACK);
        player1.increaseScore(123456);
        player1.resetScore();
        assert (player1.getScore()==0);
    }
    @Test
    public void testGetName() throws Exception {
        Player player1 = new Player("Player 1", Color.WHITE, Color.BLACK);
        assert(player1.getName()=="Player 1");
    }
}