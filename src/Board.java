import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Wylie on 3/14/2017.
 */
public class Board {
    private static final int WIDTH = 512;
    private static final int HEIGHT = 512;
    private static final int xOffset = 10;
    private static final int yOffset = 5;
    /*
    private static final Point[] neighborPts =
            {
                    new Point(0, -40),
                    new Point(30, -20),
                    new Point(30, 20),
                    new Point(0, 40),
                    new Point(-30, 20),
                    new Point(-30, -20)
            };

            */

    private static final Point[] neighborPts =
    {
        new Point(-20, -30),
        new Point(20, -30),
        new Point(40, 0),
        new Point(20, 30),
        new Point(-20, 30),
        new Point(-40, 0)
    };

    Player activePlayer;
    Player player1;
    Player player2;
    Player players[];
    Player winner;
    private GameManager manager;
    private HashMap<Point, HexButton> buttonMap;
    private SettlementManager settlementManager;
    private MoveAnalyzer analyzer1;
    private MoveAnalyzer analyzer2;
    private Deck deck;
    private boolean tilePlaced;
    private boolean playerTracker;
    private int settlementCount;
    private GameResult gameResult;

    // Constructor:
    public Board(GameManager manager) {
        this.manager = manager;

        deck = new Deck();

        player1 = new Player("Player 1", Color.WHITE, Color.BLACK);
        player2 = new Player("Player 2", Color.BLACK, Color.WHITE);
        activePlayer = player1;

        //g2d.drawImage(hexTemplate, 250, 200, null);

        //resetButtonMap();

        resetMapWithCenterHex();

        settlementManager = new SettlementManager(this);
        analyzer1 = new PrematurePreston(this);
        analyzer2 = new MoveAnalyzer(this);
    }

    // ====================================
    // Resetting and Initialization methods:


    public void resetButtonMap() {
        buttonMap = new HashMap<Point, HexButton>();

        int startX = 256;
        int startY = 128;
        int hexBoxSize = 40;
        int hexVertOffset = 20;
        int hexHoriOffset = 30;

        for (int i = startX + xOffset; i <= startX + WIDTH - hexBoxSize; i += hexHoriOffset * 2) {
            for (int j = startY + yOffset; j <= startY + HEIGHT - hexBoxSize; j += hexVertOffset * 2) {
                Point point = new Point(i, j);
                Hex hex = new Hex(Terrain.EMPTY);
                HexButton hexButton = new HexButton(point, hex, manager);
                buttonMap.put(point, hexButton);
                //System.out.println(point.toString());
                //g2d.drawImage(hexTemplate, i, j, null);
                //g2d.drawImage(hexTemplate, i + 30, j + 20, null);
            }
        }

        for (int i = startX + hexHoriOffset + xOffset; i <= startX + WIDTH - hexBoxSize; i += hexHoriOffset * 2) {
            for (int j = startY + hexVertOffset + yOffset; j <= startY + HEIGHT - hexBoxSize; j += hexVertOffset * 2) {
                Point point = new Point(i, j);
                Hex hex = new Hex(Terrain.EMPTY);
                HexButton hexButton = new HexButton(point, hex, manager);
                buttonMap.put(point, hexButton);
                //System.out.println(point.toString());
                //g2d.drawImage(hexTemplate, i, j, null);
            }
        }

        resetBoard();
    }

    public void clearHexes() {
        for (HexButton button : buttonMap.values()) {
            button.resetButton();
        }
        resetBoard();
    }

    private void resetMapWithCenterHex() {
        buttonMap = new HashMap<Point, HexButton>();

        Point point = new Point(256 + 236, 128 + 236);
        buttonMap.put(point, new HexButton(point, new Hex(Terrain.EMPTY), manager));
        placeStartingTile(point);
    }

    public void resetWithOneHex() {
        resetMapWithCenterHex();
        resetBoard();
    }

    public void resetDeck() {
        deck.resetTileCount();
    }

    public void resetBoard() {
        resetDeck();
        tilePlaced = false;
        settlementCount = 0;
        player1.resetResources();
        player1.resetScore();
        player2.resetResources();
        player2.resetScore();
        settlementManager.updateSettlements();
        analyzer1.analyze();
        analyzer2.analyze();
    }

    // ====================================
    // Game state management methods:

    public void processTurn(PlayerMove playerMove)
    {
        MoveAnalyzer activeAnalyzer = getActiveAnalyzer();
        if(playerMove != null && gameResult == null)
        {
            if (!tilePlaced) // if tilePlaced is false, Board expects a Tile Placement
            {
                playerMove.execute(this);
                tilePlaced = true;
                settlementManager.updateSettlements();
                activeAnalyzer.analyze();

                if (activeAnalyzer.noPossibleBuildActions())
                {
                    forfeitGame(activePlayer);
                }

            }
            else // if tilePlaced is true, Board expects a Build Action
            {
                playerMove.execute(this);
                tilePlaced = false;


                settlementManager.updateSettlements();
                activeAnalyzer.analyze();

                if (activePlayer.instaWins())
                {
                    instaWin(activePlayer);
                }

                else if (deck.getTopTile().getTileId() > 48)
                {
                    endGame(activePlayer);
                }

                // activePlayer is then switched from player1 to player2 or vice-versa:
                activePlayer = (activePlayer == player1 ? player2 : player1);
                getActiveAnalyzer().analyze();
            }
        }

    }

    public void instaWin(Player winner)
    {
        System.out.println("GAME OVER, instawin!");
        this.winner = winner;
        manager.sendInstaWinSignal(winner);
        gameResult = GameResult.INSTAWIN;
    }

    public void forfeitGame(Player loser) {
        System.out.println("GAME OVER, forfeit!");
        manager.sendForfeitSignal(loser);
        if (loser == player1)
        {
            winner = player2;
        }
        else
        {
            winner = player1;
        }
        gameResult = GameResult.DEFAULT;
    }

    public void endGame(Player tieVictor)
    {
        System.out.println("GAME OVER, scoring!");
        int score1 = player1.getScore();
        int score2 = player2.getScore();
        if (score1 > score2)
        {
            winner = player1;
            gameResult = GameResult.SCORE;
        }
        else if (score2 > score1)
        {
            winner = player2;
            gameResult = GameResult.SCORE;
        }
        else
        {
            winner = tieVictor;
            gameResult = GameResult.TIEBREAK;
        }
        manager.sendVictorySignal(winner);
    }

    // ====================================
    // Tile Placement methods:

    public void placeStartingTile(Point origin)
    {
        HexButton centerButton = buttonMap.get(origin);

        centerButton.changeHex(new Hex(Terrain.VOLCANO, 0));
        placePerimeterHexes(origin);
        placeHex(origin, new Hex(Terrain.JUNGLE, 0), 0);
        placeHex(origin, new Hex(Terrain.LAKE, 0), 1);
        placeHex(origin, new Hex(Terrain.GRASS, 0), 3);
        placeHex(origin, new Hex(Terrain.ROCKY, 0), 4);
    }

    public void placeTile(Point origin, Orientation orientation)
    {
        Tile tile = deck.getTopTile();
        tile.setOrientation(orientation);
        HexButton centerButton = buttonMap.get(origin);

        if (tilePlacementIsLegal(tile, centerButton))
        {
            centerButton.changeHex(tile.getVolcano());
            placePerimeterHexes(origin);

            placeHex(origin, tile.getA(), tile.getOrientation().ordinal());
            placeHex(origin, tile.getB(), tile.getOrientationPlus(1).ordinal());

            deck.nextTile();
        }
    }

    public void placeTile(Tile tile, Point origin) {
        HexButton centerButton = buttonMap.get(origin);

        if (tilePlacementIsLegal(tile, centerButton)) {
            centerButton.changeHex(tile.getVolcano());
            placePerimeterHexes(origin);

            placeHex(origin, tile.getA(), tile.getOrientation().ordinal());
            placeHex(origin, tile.getB(), tile.getOrientationPlus(1).ordinal());
        }
    }

    private void placeHex(Point center, Hex hex, int orientation) {
        Point delta = neighborPts[orientation];
        Point origin = new Point(center.x + delta.x, center.y + delta.y);
        if (buttonMap.containsKey(origin)) {
            HexButton button = buttonMap.get(origin);
            button.changeHex(hex);
        } else {
            HexButton button = new HexButton(origin, hex, manager);
            buttonMap.put(origin, button);
        }
        placePerimeterHexes(origin);
    }

    private void placePerimeterHexes(Point center) {
        for (int i = 0; i < 6; i++) {
            Point neighborPoint = neighborPts[i];
            Point buttonPoint = new Point(center.x + neighborPoint.x, center.y + neighborPoint.y);
            placeIfUnmapped(buttonPoint);
            place2ndLayerPerimeter(buttonPoint);
        }
    }

    private void place2ndLayerPerimeter(Point center) {
        for (int i = 0; i < 6; i++) {
            Point neighborPoint = neighborPts[i];
            Point buttonPoint = new Point(center.x + neighborPoint.x, center.y + neighborPoint.y);
            placeIfUnmapped(buttonPoint);
        }
    }

    private void placeIfUnmapped(Point buttonPoint) {
        if (!buttonMap.containsKey(buttonPoint)) {
            HexButton button = new HexButton(buttonPoint, new Hex(Terrain.EMPTY), manager);
            buttonMap.put(buttonPoint, button);
        }
    }

    public void placeBuilding(Point buttonPoint, Building building) {
        HexButton hexButton = buttonMap.get(buttonPoint);
        if (buildingPlacementIsLegal(building, hexButton)) {
            hexButton.placeBuilding(building, activePlayer);
            activePlayer.consumeMeeples(building, 1);
            activePlayer.increaseScore(building);
        }
        /*
        if(building == Building.VILLAGER)
        {
            settlements.add(new Settlement(hexButton, settlementCount));
            settlementCount++;
        }
        */
    }

    // Need to convert most of these if-conditions to separate methods for readability
    public boolean buildingPlacementIsLegal(Building building, HexButton hexButton) {
        if (!hexButton.getHex().getTerrain().isBuildable()) {
            //System.out.println("Illegal move: unbuildable terrain type");
            return false;
        }

        if (hexButton.getHex().getBuilding().occupiesHex()) {
            //System.out.println("Illegal move: hex is already occupied");
            return false;
        }

        if (activePlayer.getMeeples()[building.ordinal()] < 1) {
            //System.out.println("Illegal move: " + activePlayer.getName() + " has insufficient " + building.toString() + "s");
            return false;
        }

        if (building == Building.VILLAGER) {
            if (hexButton.getHex().getLevel() != 1) {
                //System.out.println("Illegal move: villager placement requires level = 1");
                return false;
            }
        }

        if (building == Building.TIGER) {
            if (hexButton.getHex().getLevel() < 3) {
                //System.out.println("Illegal move: tiger placement requires level >= 3");
                return false;
            }

            if (!isTigerlessSettlementAdjacent(hexButton)) {
                //System.out.println("Illegal move: tiger placement requires adjacent settlement");
                return false;
            }
        }

        if (building == Building.TOTORO) {
            if (!isTotorolessSize5SettlementAdjacent(hexButton)) {
                //System.out.println("Illegal move: totoro placement requires adjacent settlement size 5+ with no totoro");
                return false;
            }
        }

        return true;
    }

    public boolean isTotorolessSize5SettlementAdjacent(HexButton hexButton) {
        for (int i = 0; i < 6; i++) {
            HexButton neighbor = getNeighborButton(hexButton, i);
            Settlement settlement = settlementManager.getSettlement(neighbor);
            if (settlement != null && settlement.getOwner() == activePlayer) {
                if (!settlement.hasTotoro()) {
                    if (settlement.getSize() >= 5) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean isTigerlessSettlementAdjacent(HexButton hexButton) {
        for (int i = 0; i < 6; i++) {
            HexButton neighbor = getNeighborButton(hexButton, i);
            //if (settlementManager.isInSettlement(hexButton))
            //{
            //System.out.println("Neighbor " + i + " is a settlement member");
            Settlement settlement = settlementManager.getSettlement(neighbor);
            if (settlement != null && settlement.getOwner() == activePlayer) {
                //System.out.println("Neighbor's settlement is owned by activePlayer " + activePlayer.getName());
                if (!settlement.hasTiger()) {
                    //System.out.println("Settlement has no tiger, returning true");
                    return true;
                }
            }
            // }
        }

        return false;
    }
    // ====================================
    // Accessors for member data:

    public HashMap<Point, HexButton> getButtonMap() {
        return buttonMap;
    }

    public HexButton getNeighborButton(HexButton base, int index) {
        if (index < 0 || index > 5) {
            System.out.println("Error, invalid neighbor index = " + index);
            return null;
        } else {
            Point delta = neighborPts[index];
            Point neighborPt = new Point(base.getOrigin().x + delta.x, base.getOrigin().y + delta.y);
            if (!buttonMap.containsKey(neighborPt)) {
                return new HexButton(neighborPt, new Hex(Terrain.EMPTY), manager);
            } else {
                return buttonMap.get(neighborPt);
            }
        }
    }

    public Deck getDeck() {
        return deck;
    }

    public HexButton getHexButton(Point point) {
        return buttonMap.get(point);
    }

    public boolean getTilePlaced() {
        return tilePlaced;
    }

    public Player getActivePlayer() {
        return activePlayer;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Building getActiveBuilding() {
        return manager.getActiveBuilding();
    }

    public Terrain getActiveTerrain() {
        return manager.getActiveTerrain();
    }

    public boolean getExpandNext() {
        return manager.getExpandNext();
    }

    public CopyOnWriteArrayList<Settlement> getSettlements() {
        return settlementManager.getSettlements();
    }

    public GameResult getGameResult() {
        return gameResult;
    }

    public Player getWinner() {
        return winner;
    }

    public MoveAnalyzer getActiveAnalyzer()
    {
        if(activePlayer == player1)
        {
            return analyzer1;
        }
        else
        {
            return analyzer2;
        }
    }

    // ==============================================
    // Tile Placement legality checking functions:

    public boolean tilePlacementIsLegal(Tile tile, HexButton targetButton)
    {
        if (!targetIsEmptyOrVolcano(targetButton))
        {
            //System.out.println("Illegal move: target neither volcano nor empty");
            return false;
        }
        if (targetButton.getHex().getTerrain() == Terrain.VOLCANO)
        {
            if (destroysWholeSettlement(tile, targetButton))
            {
                //System.out.println("Illegal move: destroys at least one settlement");
                return false;
            }
            if (destroysPermanentBuilding(tile, targetButton))
            {
                //System.out.println("Illegal move: destroys tiger or totoro");
                return false;
            }
            if (hexesShareTile(tile, targetButton))
            {
                //System.out.println("Illegal move: hexes share tile");
                return false;
            }
            if (!hexesShareLevel(tile, targetButton))
            {
               // System.out.println("Illegal move: hexes not same height");
                return false;
            }
        }
        else if (targetButton.getHex().getTerrain() == Terrain.EMPTY)
        {

            if (!allHexesEmpty(tile, targetButton))
            {
              //  System.out.println("Illegal move: empty/nonempty tile placement");
                return false;
            }
            if (!adjacentToNonEmptyHex(tile, targetButton))
            {
               // System.out.println("Illegal move: attempted to make new island");
                return false;
            }
        }

        return true;
    }

    private boolean destroysWholeSettlement(Tile tile, HexButton targetButton)
    {
        int positionA = tile.getOrientation().ordinal();
        int positionB = tile.getOrientationPlus(1).ordinal();

        HexButton buttonA = getNeighborButton(targetButton, positionA);
        HexButton buttonB = getNeighborButton(targetButton, positionB);

        return (settlementManager.nukeWillDestroySettlement(buttonA, buttonB));
    }

    public boolean destroysPermanentBuilding(Tile tile, HexButton targetButton)
    {
        int positionA = tile.getOrientation().ordinal();
        int positionB = tile.getOrientationPlus(1).ordinal();

        HexButton buttonA = getNeighborButton(targetButton, positionA);
        HexButton buttonB = getNeighborButton(targetButton, positionB);

        return (buttonA.getHex().getBuilding().isPermanent() || buttonB.getHex().getBuilding().isPermanent());
    }

    // adjacentToNonEmptyHex returns true if any Fex of the Tile will be adjacent to a non-Empty Hex
    public boolean adjacentToNonEmptyHex(Tile tile, HexButton targetButton)
    {
        int positionA = tile.getOrientation().ordinal();
        int positionB = tile.getOrientationPlus(1).ordinal();

        HexButton buttonA = getNeighborButton(targetButton, positionA);
        HexButton buttonB = getNeighborButton(targetButton, positionB);

        return hasNonEmptyNeighbor(targetButton) || hasNonEmptyNeighbor(buttonA) || hasNonEmptyNeighbor(buttonB);

    }

    public boolean adjacentToSettlementMember(Settlement settlement, HexButton targetButton)
    {
        for(int i = 0; i < 6; i++)
        {
            HexButton neighbor = getNeighborButton(targetButton, i);
            if(settlement.getHexes().contains(neighbor))
            {
                return true;
            }
        }

        return false;
    }

    // hasNonEmptyNeighbor returns true if any of a Hex's neighbors are not Empty
    public boolean hasNonEmptyNeighbor(HexButton targetButton)
    {
        for (int i = 0; i < 6; i++)
        {
            HexButton neighborButton = getNeighborButton(targetButton, i);
            // If neighborButton's hex is not Empty, return true
            Terrain terrain = neighborButton.getHex().getTerrain();
            if (terrain != Terrain.EMPTY)
            {
                return true;
            }
        }
        // When all neighborButtons have been checked and none were Empty, return false
        return false;
    }

    // allHexesEmpty returns true if you are attempting to place a Tile on three Empty hexes
    public boolean allHexesEmpty(Tile tile, HexButton targetButton)
    {
        int positionA = tile.getOrientation().ordinal();
        int positionB = tile.getOrientationPlus(1).ordinal();

        HexButton buttonA = getNeighborButton(targetButton, positionA);
        HexButton buttonB = getNeighborButton(targetButton, positionB);

        Hex hexV = targetButton.getHex();
        Hex hexA = buttonA.getHex();
        Hex hexB = buttonB.getHex();

        return hexV.getTerrain() == Terrain.EMPTY && hexA.getTerrain() == Terrain.EMPTY && hexB.getTerrain() == Terrain.EMPTY;
    }

    // hexesShareLevel returns true if you are attempting to place a Tile on three hexes of the same level
    public boolean hexesShareLevel(Tile tile, HexButton targetButton)
    {
        int positionA = tile.getOrientation().ordinal();
        int positionB = tile.getOrientationPlus(1).ordinal();

        HexButton buttonA = getNeighborButton(targetButton, positionA);
        HexButton buttonB = getNeighborButton(targetButton, positionB);

        int levelV = targetButton.getHex().getLevel();
        int levelA = buttonA.getHex().getLevel();
        int levelB = buttonB.getHex().getLevel();

        return levelV == levelA && levelA == levelB;
    }

    // hexesShareTile returns true if you are attempting to place a Tile directly over another
    public boolean hexesShareTile(Tile tile, HexButton targetButton)
    {
        int positionA = tile.getOrientation().ordinal();
        int positionB = tile.getOrientationPlus(1).ordinal();

        HexButton buttonA = getNeighborButton(targetButton, positionA);
        HexButton buttonB = getNeighborButton(targetButton, positionB);

        int tileV = targetButton.getHex().getTileId();
        int tileA = buttonA.getHex().getTileId();
        int tileB = buttonB.getHex().getTileId();

        return tileV == tileA && tileV == tileB && tileV != -1;
    }

    // targetIsEmptyOrVolcano returns true if the target hex is Empty or a Volcano
    public boolean targetIsEmptyOrVolcano(HexButton targetButton)
    {
        Hex hex = targetButton.getHex();

        return hex.getTerrain() == Terrain.EMPTY || hex.getTerrain() == Terrain.VOLCANO;
    }

    public SettlementManager getSettlementManager() {
        return settlementManager;
    }

    public void expandSettlement(Settlement settlement, Terrain terrain)
    {
        Expansion expansion = settlementManager.getExpansion(settlement, terrain);
        if (settlementExpansionIsLegal(expansion))
        {
            for (HexButton button : expansion.getHexes())
            {
                button.placeBuilding(Building.VILLAGER, activePlayer);
                int level = button.getHex().getLevel();
                activePlayer.increaseScore(level * level);
            }
            activePlayer.decreaseVillagers(expansion.getCost());
        }
    }

    public boolean settlementExpansionIsLegal(Expansion expansion)
    {
        if (expansion.getHexes().isEmpty())
        {
            //System.out.println("Illegal move: no expandable hexes");
            return false;
        }

        if (activePlayer.getVillagers() < expansion.getCost())
        {
            //System.out.println("Illegal move: insufficient resources");
            return false;
        }

        return true;
    }

    public boolean hexIsTotorolessSettlementAdjacent(HexButton hex)
    {
        for(int i = 0; i < 6; i++)
        {
            HexButton neighbor = getNeighborButton(hex, i);
            Hex neighborHex = neighbor.getHex();
            if(neighborHex.isOccupied() && neighborHex.getOwner() == activePlayer && !getSettlementManager().getSettlement(neighbor).hasTotoro())
            {
                return true;
            }
        }
        return false;
    }

    public boolean hexIsTigerEligibleAdjacent(HexButton hex)
    {
        boolean status = false;
        for(int i = 0; i < 6; i++)
        {
            HexButton neighbor = getNeighborButton(hex, i);
            Hex neighborHex = neighbor.getHex();
            if(!neighborHex.isOccupied() && neighborHex.getTerrain().isBuildable() && neighborHex.getLevel() >= 3)
            {
                status = true;
            }
            else if (neighborHex.isOccupied() && neighborHex.getBuilding() == Building.TIGER)
            {
                return false;
            }
        }
        return status;
    }



}
