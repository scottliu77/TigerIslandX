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




        JMenu menuChangeMenu = new JMenu("Change Menu");

        JMenuItem setMenu0 = new JMenuItem("Menu 0");
        setMenu0.addActionListener(new ActionListener()
        {
           public void actionPerformed(ActionEvent event) { panel.setActiveMenu(0);}
        });

        JMenuItem setMenu1 = new JMenuItem("Menu 1");
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

        fileMenu.add(addGame);

        menuChangeMenu.add(setMenu0);
        menuChangeMenu.add(setMenu1);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(menuChangeMenu);
        frame.setJMenuBar(menuBar);

        frame.getContentPane().add(panel, BorderLayout.CENTER);

        frame.validate();
        frame.setVisible(true);
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
        baseMenu = new BaseMenu();
        menus.add(baseMenu);
        menus.add(new BaseMenu());
        activeMenu = menus.get(0);
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

    public void makeNewMenu()
    {
        menus.add(new BaseMenu());
    }
}
