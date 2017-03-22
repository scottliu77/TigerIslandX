import java.util.*;
import java.awt.*;

// GameManager is an intermediary class between the TigerPanel and the game Menu.
public class GameManager
{
    private Graphics2D panelG2D;
    private ArrayList<Menu> menus;
    private Menu activeMenu;
    private BaseMenu baseMenu;

    GameManager(Graphics2D panelG2D)
    {
        this.panelG2D = panelG2D;
        initializeMenus();
    }

    private void initializeMenus()
    {
        menus = new ArrayList<Menu>();
        baseMenu = new BaseMenu();
        menus.add(baseMenu);
        activeMenu = menus.get(0);
        activeMenu.drawMenu(panelG2D);
    }

    public void checkForPress(Point point)
    {
        activeMenu.checkForPress(point);
        updateMenu();
    }

    public void checkForHover(Point point)
    {
        activeMenu.checkForHover(point);
        updateMenu();
    }

    public void updateMenu()
    {
        activeMenu.updateDisplays();
        drawMenu();
    }

    private void drawMenu()
    {
        activeMenu.drawMenu(panelG2D);
    }

    public void emptyHexes()
    {
        baseMenu.clearHexes();
        updateMenu();
    }

    public void resetHexes()
    {
        baseMenu.resetHexes();
        updateMenu();
    }

    public void resetWithOneHex()
    {
        baseMenu.resetWithOneHex();
        updateMenu();
    }
}
