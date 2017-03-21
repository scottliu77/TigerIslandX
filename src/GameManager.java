import java.util.*;
import java.awt.*;

// GameManager is an intermediary class between the TigerPanel and the game Menu.
public class GameManager
{
    private TigerPanel panel;
    private ArrayList<Menu> menus;
    private Menu activeMenu;
    private BaseMenu baseMenu;

    GameManager(TigerPanel panel)
    {
        this.panel = panel;
        initializeMenus();
    }

    public void initializeMenus()
    {
        menus = new ArrayList<Menu>();
        baseMenu = new BaseMenu();
        menus.add(baseMenu);
        activeMenu = menus.get(0);
        activeMenu.drawMenu(panel.getG2D());
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

    public void drawMenu()
    {
        activeMenu.drawMenu(panel.getG2D());
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
