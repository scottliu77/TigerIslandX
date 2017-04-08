import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Created by Wylie on 3/19/2017.
 */

// Main class contains JMenus (i.e., dropdown menus) and emplace a TigerPanel
public class TigerIsland
{
    static private final int WIDTH = 1024;
    static private final int HEIGHT = 768;

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                createAndShowGUI();
            }
        });
    }

    // createAndShowGUI assembles and configures the JFrame
    public static void createAndShowGUI()
    {
        int menuCount = 0;

        JFrame frame = new JFrame();

        frame.setTitle("Group O - TigerIsland v3.0 Development Build");
        frame.setSize(WIDTH + 16, HEIGHT + 62);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        TigerPanel panel = new TigerPanel(new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB));

        JMenu fileMenu = new JMenu("File");

        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                System.exit(0);
            }
        });

        fileMenu.add(exit);

        JMenu menuChangeMenu = new JMenu("Change View");

        JMenuItem setMenu0 = new JMenuItem("Game 0");
        setMenu0.addActionListener(new ActionListener()
        {
           public void actionPerformed(ActionEvent event) { panel.setActiveMenu(0);}
        });

        JMenuItem setMenu1 = new JMenuItem("Game 1");
        setMenu1.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event) { panel.setActiveMenu(1);}
        });

        JMenuItem addGame = new JMenuItem("Reset Games");
        addGame.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent event)
            {
                panel.initializeMenus();
            }
        });

        JMenu autoplayMenu = new JMenu("Autoplay");

        JMenuItem autoplayGames = new JMenuItem("Autoplay games");
        autoplayGames.addActionListener( new ActionListener()
        {
            public void actionPerformed(ActionEvent event) { autoplayGames(); }
        });

        autoplayMenu.add(autoplayGames);

        fileMenu.add(addGame);

        menuChangeMenu.add(setMenu0);
        menuChangeMenu.add(setMenu1);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(menuChangeMenu);
        menuBar.add(autoplayMenu);
        frame.setJMenuBar(menuBar);

        frame.getContentPane().add(panel, BorderLayout.CENTER);

        frame.validate();
        frame.setVisible(true);
    }

    public static void autoplayGames()
    {
        int numberOfGames = Integer.parseInt(JOptionPane.showInputDialog("How many games?"));
        Boolean playerFirst = true;

        int instawins[] = new int[2];
        int defaults[] = new int[2];
        int gameCount[] = new int[2];
        GameManager newGame;

        for(int i = 0; i < numberOfGames; i++)
        {
            System.out.println("Starting Game #" + i);
            newGame = new GameManager(playerFirst);
            // Toggle first player:
            playerFirst = !playerFirst;

            newGame.resolveGame();

            GameResult result = newGame.getBoard().getGameResult();

            // Right now I only bother keeping track of instawins and default because those are all that ever seem to happen

            int playerIndex = playerFirst ? 0 : 1;

            if(result == GameResult.INSTAWIN)
            {
                instawins[playerIndex]++;
            }
            else if (result == GameResult.DEFAULT)
            {
                defaults[playerIndex]++;
            }

            gameCount[playerIndex]++;

            System.out.println("First move Instawins: " + instawins[0] + ", rate: " + ((float) instawins[0] / (float) gameCount[0]) * 100 + "%");
            System.out.println("Second move Instawins: " + instawins[1] + ", rate: " + ((float) instawins[1] / (float) gameCount[1]) * 100 + "%");
            System.out.println("First move Defaults: " + defaults[0] + ", rate: " + ((float) defaults[0] / (float) gameCount[0]) * 100 + "%");
            System.out.println("Second move Defaults: " + defaults[1] + ", rate: " + ((float) defaults[1] / (float) gameCount[1]) * 100 + "%");

            newGame = null;
        }

        int instaTotal = instawins[0] + instawins[1];
        int defaultTotal = defaults[0] + defaults[1];

        int points = ((instaTotal * 20) - (defaultTotal)) * 10;

        System.out.println("Instawins: " + instaTotal + ", rate: " + ((float) instaTotal / (float) numberOfGames) * 100 + "%");
        System.out.println("Defaults: " + defaultTotal + ", rate: " + ((float) defaultTotal / (float) numberOfGames) * 100 + "%");
        System.out.println("Net points: " + points + ", average: " + ((float) points / (float) numberOfGames) + " points per game");
    }
}

// TigerPanel displays the output image and detects mouse inputs
// Used for human user input/output
class TigerPanel extends JPanel
{
    private BufferedImage image;
    private Graphics2D g2d;

    //private GameManager manager;

    private ArrayList<BaseMenu> menus;
    private BaseMenu baseMenu;
    private BaseMenu activeMenu;

    public TigerPanel(BufferedImage image) {
        this.image = image;
        g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        //manager = new GameManager(g2d);

        initializeMenus();

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent event) {
                activeMenu.checkForPress(event.getPoint());
                activeMenu.updateDisplays();
                activeMenu.drawMenu(g2d);
                repaint();
            }

            public void mouseClicked(MouseEvent event) {

            }

            public void mouseReleased(MouseEvent event) {

            }
        });

        addMouseMotionListener(new MouseMotionListener() {
            public void mouseMoved(MouseEvent event) {
                activeMenu.checkForHover(event.getPoint());
                activeMenu.updateDisplays();
                activeMenu.drawMenu(g2d);
                repaint();
            }

            public void mouseDragged(MouseEvent event) {
                activeMenu.checkForHover(event.getPoint());
                activeMenu.updateDisplays();
                activeMenu.drawMenu(g2d);
                repaint();
            }
        });


    }

    public void initializeMenus()
    {
        menus = new ArrayList<BaseMenu>();
        baseMenu = new BaseMenu(true);
        menus.add(baseMenu);
        menus.add(new BaseMenu(false));
        activeMenu = menus.get(0);
        activeMenu.drawMenu(g2d);
        repaint();
    }

    public void drawActiveMenu()
    {
        activeMenu.updateDisplays();
        activeMenu.drawMenu(g2d);
        repaint();
    }

    public Graphics2D getG2D() {
        return g2d;
    }

    public void drawImg(BufferedImage img) {
        g2d.drawImage(img, 0, 0, null);
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }

    public void setActiveMenu(int index) {
        this.activeMenu = menus.get(index);
        activeMenu.drawMenu(g2d);
        repaint();
    }
}
