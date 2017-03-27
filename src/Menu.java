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
    private BoardDisplay boardDisplay;
    private DeckDisplay deckDisplay;
    private HexDetailDisplay hexDetailDisplay;
    private TurnStatusDisplay turnStatusDisplay;
    private PlayerStatusDisplay player1StatusDisplay;
    private PlayerStatusDisplay player2StatusDisplay;
    private SettlementsDisplay settlementsDisplay;
    private MoveAnalyzerDisplay moveAnalyzerDisplay;

    private GameManager gameManager;

    private HexButton hoverHex;

    public BaseMenu()
    {
        super();

        gameManager = new GameManager();
        Board board = gameManager.getBoard();
        Deck deck = board.getDeck();

        super.addDisplay(backgroundDisplay = new BackgroundDisplay());
        super.addDisplay(boardDisplay = new BoardDisplay(board));
        super.addDisplay(deckDisplay = new DeckDisplay(deck));
        super.addDisplay(hexDetailDisplay = new HexDetailDisplay());
        super.addDisplay(turnStatusDisplay = new TurnStatusDisplay(board));
        super.addDisplay(player1StatusDisplay = new PlayerStatusDisplay(board.getPlayer1(), new Point(256, 32)));
        super.addDisplay(player2StatusDisplay = new PlayerStatusDisplay(board.getPlayer2(), new Point(544, 32)));
        super.addDisplay(settlementsDisplay = new SettlementsDisplay(board.getSettlementManager()));
        super.addDisplay(moveAnalyzerDisplay = new MoveAnalyzerDisplay(board.getMoveAnalyzer()));

        // Note: should hold Board in Menu instead of at Display level...
        super.addButton(new RotateLeftButton((new Point(832, 256)), deck));
        super.addButton(new RotateRightButton((new Point(896, 256)), deck));

        super.addButton(new MeepleSelectButton(new Point(832, 512), gameManager, Building.VILLAGER));
        super.addButton(new MeepleSelectButton(new Point(896, 512), gameManager, Building.TIGER));
        super.addButton(new MeepleSelectButton(new Point(896, 576), gameManager, Building.TOTORO));
        super.addButton(new ExpansionSelectButton(new Point(832, 576), gameManager));

        super.addButton(new TerrainSelectButton(new Point(832, 640), gameManager, Terrain.LAKE));
        super.addButton(new TerrainSelectButton(new Point(896, 640), gameManager, Terrain.ROCKY));
        super.addButton(new TerrainSelectButton(new Point(832, 704), gameManager, Terrain.GRASS));
        super.addButton(new TerrainSelectButton(new Point(896, 704), gameManager, Terrain.JUNGLE));

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

    public void resetHexes()
    {
        super.removeHexButtons(gameManager.getButtonMap().values());
        gameManager.resetHexes();
        addHexButtons(gameManager.getButtonMap());
        updateDisplays();
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

    // Presses the button on which the point lies if applicable
    public void checkForPress(Point point)
    {
        for(Button button : super.getButtons())
        {
            if (button.pointIsOn(point))
            {
                button.press();
                addHexButtons(gameManager.getButtonMap());
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
}
