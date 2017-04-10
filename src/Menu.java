import java.util.*;
import java.awt.*;

// A menu manages its button and display objects.
class Menu
{
    // Member data:

    // Buttons allow the user to interact with the mouse
    private ArrayList<Button> buttons;

    // Displays aren't directly interactive, but can still update in response to actions
    private ArrayList<Display> displays;

    // The menu keeps track of which button is being hovered over
    private Button hoverButton;

    // Constructors:

    public Menu(ArrayList<Button> buttons, ArrayList<Display> displays)
    {
        this.buttons = buttons;
        this.displays = displays;
    }

    public Menu(ArrayList<Button> buttons)
    {
        this(buttons, new ArrayList<Display>());
    }

    public Menu()
    {
        this(new ArrayList<Button>(), new ArrayList<Display>());
    }

    // drawMenu draws the current states of the menu's displays and buttons
    public void drawMenu(Graphics2D g2d)
    {
        drawDisplays(g2d);
        drawButtons(g2d);
    }

    public void drawDisplays(Graphics2D g2d)
    {
        for(Display display : displays)
        {
            display.drawImg(g2d);
        }
    }

    public void drawButtons(Graphics2D g2d)
    {
        for(Button button : buttons)
        {
            button.drawButton(g2d);
            if(hoverButton != null)
            {
                hoverButton.drawButton(g2d);
            }
        }
    }

    // updateDisplays causes each member display to execute its update function
    public void updateDisplays()
    {
        for(Display display : displays)
        {
            display.update();
        }
    }

    // Updates the current hoverButton based on mouse movement input
    public void checkForHover(Point point)
    {
        // Most likely case is that the button previously hovered over is still being hovered over, so check that first:
        if(hoverButton != null)
        {
            if(hoverButton.hoverCheck(point))
            {
                return;
            }
        }

        // If the previous button isn't still hovered over, check all the other buttons
        for(Button button : buttons)
        {
            if (button.hoverCheck(point))
            {
                hoverButton = button;
                return;
            }
        }

        updateDisplays();
        // hoverButton = null; // Reset hoverButton if no hover is detected
    }

    // Presses the button on which the point lies if applicable
    public void checkForPress(Point point)
    {
        for(Button button : buttons)
        {
            if (button.pointIsOn(point))
            {
                button.press();
                return;
            }
        }
    }

    public void setHoverButton(Button button)
    {
        this.hoverButton = button;
    }

    public void addDisplays(Collection<Display> newDisplays)
    {
        displays.addAll(newDisplays);
    }

    public void addDisplay(Display newDisplay)
    {
        displays.add(newDisplay);
    }

    public void addButtons(Collection<Button> newButtons)
    {
        buttons.addAll(newButtons);
    }

    public void addButton(Button newButton)
    {
        buttons.add(newButton);
    }

    public void clearButtons()
    {
        buttons.clear();
    }

    public void removeHexButtons(Collection<HexButton> oldButtons)
    {
        buttons.removeAll(oldButtons);
    }

    public ArrayList<Button> getButtons()
    {
        return buttons;
    }

    public Button getHoverButton()
    {
        return hoverButton;
    }

}

// The baseMenu holds the board and most game logic.
// It's currently the only menu, but I might implement some more menus for settings configuration etc.
class BaseMenu extends Menu
{
    //Board board;

    private BackgroundDisplay backgroundDisplay;
    private DeckDisplay deckDisplay;
    private HexDetailDisplay hexDetailDisplay;
    private TurnStatusDisplay turnStatusDisplay;
    private PlayerStatusDisplay player1StatusDisplay;
    private PlayerStatusDisplay player2StatusDisplay;
    private SettlementsDisplay settlementsDisplay;
    private MoveAnalyzerDisplay moveAnalyzerDisplay;

    private GameManager gameManager;

    private ArrayList<RadialButton> buildRadials;
    private RadialButton depressedBuildRadial;

    public BaseMenu(boolean playerFirst)
    {
        super();

        gameManager = new GameManager(playerFirst);
        Board board = gameManager.getBoard();
        Deck deck = board.getDeck();

        buildRadials = new ArrayList<RadialButton>();
        MeepleSelectButton villagerButton;
        MeepleSelectButton tigerButton;
        MeepleSelectButton totoroButton;
        TerrainSelectButton lakeButton;
        TerrainSelectButton rockyButton;
        TerrainSelectButton grassButton;
        TerrainSelectButton jungleButton;

        super.addDisplay(backgroundDisplay = new BackgroundDisplay());
        super.addDisplay(deckDisplay = new DeckDisplay(deck));
        super.addDisplay(hexDetailDisplay = new HexDetailDisplay());
        super.addDisplay(turnStatusDisplay = new TurnStatusDisplay(board));
        super.addDisplay(player1StatusDisplay = new PlayerStatusDisplay(board.getPlayer1(), new Point(256, 16)));
        super.addDisplay(player2StatusDisplay = new PlayerStatusDisplay(board.getPlayer2(), new Point(544, 16)));
        super.addDisplay(settlementsDisplay = new SettlementsDisplay(board.getSettlementManager()));
        super.addDisplay(moveAnalyzerDisplay = new MoveAnalyzerDisplay(board));

        // Note: should hold Board in Menu instead of at Display level...
        super.addButton(new RotateLeftButton((new Point(832, 256)), deck));
        super.addButton(new RotateRightButton((new Point(896, 256)), deck));

        super.addButton(villagerButton = new MeepleSelectButton(new Point(800, 496), gameManager, Building.VILLAGER));
        super.addButton(tigerButton = new MeepleSelectButton(new Point(864, 496), gameManager, Building.TIGER));
        super.addButton(totoroButton = new MeepleSelectButton(new Point(928, 496), gameManager, Building.TOTORO));

        super.addButton(lakeButton = new TerrainSelectButton(new Point(832, 544), gameManager, Terrain.LAKE));
        super.addButton(rockyButton = new TerrainSelectButton(new Point(896, 544), gameManager, Terrain.ROCKY));
        super.addButton(grassButton = new TerrainSelectButton(new Point(832, 576), gameManager, Terrain.GRASS));
        super.addButton(jungleButton = new TerrainSelectButton(new Point(896, 576), gameManager, Terrain.JUNGLE));

        super.addButton(new PlayAnalyzerMoveButton(board));
        super.addButton(new AutoResolveButton(board));

        buildRadials.add(villagerButton);
        buildRadials.add(tigerButton);
        buildRadials.add(totoroButton);
        buildRadials.add(lakeButton);
        buildRadials.add(rockyButton);
        buildRadials.add(grassButton);
        buildRadials.add(jungleButton);

        depressedBuildRadial = villagerButton;
        villagerButton.setDepressed(true);

        addHexButtons(board.getButtonMap());
    }

    public void clearHexes()
    {
        HashMap<Point, HexButton> buttonMap = gameManager.getButtonMap();
        for( HexButton button : buttonMap.values())
        {
            button.resetButton();
        }
        gameManager.resetDeck();
        updateDisplays();
        /*
        super.clearButtons();
        addHexButtons(board.getButtonMap());
        */
    }

    public void updateHexButtons(Graphics2D G2D)
    {
        HashMap<Point, HexButton> buttonMap = gameManager.getButtonMap();
        addHexButtons(gameManager.getButtonMap());
        //updateDisplays();
        //drawMenu(G2D);
    }

    public void resetWithOneHex()
    {
        super.removeHexButtons(gameManager.getButtonMap().values());
        gameManager.resetWithOneHex();
        addHexButtons(gameManager.getButtonMap());
        updateDisplays();
    }

    private void addHexButtons(HashMap<Point, HexButton> buttonMap)
    {
        for (HexButton button : buttonMap.values())
        {
            super.addButton(button);
        }
    }

    public void updateDisplays()
    {
        //super.updateDisplays();
        deckDisplay.update();
        hexDetailDisplay.update(getHoverHexButton());
        turnStatusDisplay.update();
        player1StatusDisplay.update();
        player2StatusDisplay.update();
        settlementsDisplay.update();
        moveAnalyzerDisplay.update();
    }

    // This override has marginally better performance on a large board than default Menu.checkForHover:
    public void checkForHover(Point point)
    {
        // Most likely case is that the button previously hovered over is still being hovered over, so check that first:
        Button hoverButton = super.getHoverButton();

        if(hoverButton != null)
        {
            if(hoverButton.hoverCheck(point))
            {
                return;
            }
        }

        // If the previous button isn't still hovered over, check all the other buttons
        for(Button button : super.getButtons())
        {
            if (button.hoverCheck(point))
            {
                super.setHoverButton(button);
                break;
            }
        }

        // Update only displays that are dependent on the currently hovered button:
        hexDetailDisplay.update(getHoverHexButton());
    }

    // Presses the button on which the point lies if applicable
    public void checkForPress(Point point)
    {
        for(Button button : super.getButtons())
        {
            if (button.pointIsOn(point))
            {
                button.press();
                //if(RadialButton.class.isAssignableFrom(button.getClass()))
                if(button instanceof RadialButton && buildRadials.contains(button) && depressedBuildRadial != button)
                {
                    depressedBuildRadial.setDepressed(false);
                    depressedBuildRadial = (RadialButton) button;
                    depressedBuildRadial.setDepressed(true);
                }
                else
                {
                    addHexButtons(gameManager.getButtonMap());
                }
                return;
            }
        }
    }

    private HexButton getHoverHexButton()
    {
        HexButton hexButton = null;
        Button hoverButton = super.getHoverButton();
        if(hoverButton instanceof HexButton)
        {
            hexButton = (HexButton) hoverButton;
        }
        return hexButton;
    }

    public void sendMessageToParser(String s)
    {
        gameManager.sendMessageToParser(s);
        addHexButtons(gameManager.getButtonMap());
    }

    public void sendServerMessageToParser(String s)
    {

    }
}
