import org.junit.Test;
import java.awt.*;

public class GameManagerTest {
    @Test
    public void gameManagerTest() throws Exception{
        GameManager testGameManager = new GameManager(true);
        Point center = new Point(492,364);
        HexButton hexButton = testGameManager.getBoard().getHexButton(center);
        assert(hexButton.getHex().getTypeName().equals("VOLCANO"));
    }
    /*
    @Test
    public void GUITest() throws Exception{
        UI tigerIsland = new UI();
        tigerIsland.createAndShowGUI();

    }*/
}
