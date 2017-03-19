import java.awt.image.BufferedImage;
import java.util.*;
import java.awt.*;

public class Menu
{
    // Member data:

    private ArrayList<Button> buttons;
    private ArrayList<Display> displays;

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

    //

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
        }
    }

    public void updateDisplays()
    {
        for(Display display : displays)
        {
            display.update();
        }
    }

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
    }

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
}

class BaseMenu extends Menu
{
    BoardDisplay boardDisplay;

    Board board;

    public BaseMenu(GameManager manager)
    {
        super();

        super.addDisplay(new BackgroundDisplay());

        board = new Board(manager);

        boardDisplay = new BoardDisplay(board);
        super.addDisplay(boardDisplay);
        super.addDisplay(new DeckDisplay(board.getDeck()));
        super.addDisplay(new HexDetailDisplay(board));

        // Note: should hold Board in Menu instead of at Display level...
        super.addButton(makeRotateLeftButton(new Point(832, 256)));
        super.addButton(makeRotateRightButton(new Point(896, 256)));

        addHexButtons(board.getButtonMap());
    }

    public void clearHexes()
    {
        HashMap<Point, HexButton> buttonMap = board.getButtonMap();
        for( HexButton button : buttonMap.values())
        {
            button.changeHex(new EmptyHex());
        }
        /*
        super.clearButtons();
        addHexButtons(board.getButtonMap());
        */

    }

    public void updateHexButtons()
    {
        HashMap<Point, HexButton> buttonMap = board.getButtonMap();
        addHexButtons(board.getButtonMap());
    }

    public void resetHexes()
    {
        super.removeHexButtons(board.getButtonMap().values());
        board.resetButtonMap();
        addHexButtons(board.getButtonMap());
    }

    public void resetWithOneHex()
    {
        super.removeHexButtons(board.getButtonMap().values());
        board.resetWithOneHex();
        addHexButtons(board.getButtonMap());
    }

    public void addHexButtons(HashMap<Point, HexButton> buttonMap)
    {
        for (HexButton button : buttonMap.values())
        {
            super.addButton(button);
        }
    }

    public RotateRightButton makeRotateRightButton(Point origin)
    {
        BufferedImage baseImg = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) baseImg.createGraphics();
        g2d.setColor(Color.RED);
        g2d.fillRect(0, 0, 64, 64);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, 0, 63, 63);
        g2d.drawString("Rotate R", 4, 16);

        BufferedImage hoverImg = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        g2d = (Graphics2D) hoverImg.createGraphics();
        g2d.setColor(Color.RED);
        g2d.fillRect(0, 0, 64, 64);
        g2d.setColor(Color.BLUE);
        g2d.fillRect(16, 16, 32, 32);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, 0, 63, 63);
        g2d.drawString("Rotate R", 4, 16);

        return new RotateRightButton(origin, board, baseImg, hoverImg);
    }

    public RotateLeftButton makeRotateLeftButton(Point origin)
    {
        BufferedImage baseImg = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) baseImg.createGraphics();
        g2d.setColor(Color.RED);
        g2d.fillRect(0, 0, 63, 63);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, 0, 63, 63);
        g2d.drawString("Rotate L", 4, 16);

        BufferedImage hoverImg = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
        g2d = (Graphics2D) hoverImg.createGraphics();
        g2d.setColor(Color.RED);
        g2d.fillRect(0, 0, 63, 63);
        g2d.setColor(Color.BLUE);
        g2d.fillRect(16, 16, 32, 32);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, 0, 63, 63);
        g2d.drawString("Rotate L", 4, 16);

        return new RotateLeftButton(origin, board, baseImg, hoverImg);
    }
}
