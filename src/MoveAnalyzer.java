import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Wylie on 3/26/2017.
 */
public class MoveAnalyzer
{
    private Board board;
    private ArrayList<HexButton> overallMoveset;
    private ArrayList<HexButton> legalMoves;
    private ArrayList<TilePlacementMove> legalTilePlacements;
    private ArrayList<TilePlacementMove> legalVolcanoPlacements;
    private ArrayList<TilePlacementMove> legalEmptyPlacements;
    private ArrayList<TilePlacementMove> expansionEnablers;
    private ArrayList<TilePlacementMove> singleNukes;
    private ArrayList<TilePlacementMove> doubleNukes;
    private ArrayList<BuildingPlacementMove> legalBuildingPlacements;
    private ArrayList<BuildingPlacementMove> legalTotoroPlacements;
    private ArrayList<BuildingPlacementMove> legalVillagerPlacements;
    private ArrayList<BuildingPlacementMove> legalTigerPlacements;
    private ArrayList<SettlementExpansionMove> legalSettlementExpansions;
    private Random rand;

    public MoveAnalyzer(Board board)
    {
        this.board = board;
        this.rand = new Random();
        updateMoveset();
        updateSettlementExpansions();
        updateTilePlacements();
        updateBuildingPlacements();

    }

    public void analyze()
    {
        updateMoveset();
        updateSettlementExpansions();
        updateTilePlacements();
        updateBuildingPlacements();

    }

    public void updateMoveset()
    {
        overallMoveset = new ArrayList<HexButton>(board.getButtonMap().values());
    }

    public void updateLegalMoves()
    {
        legalMoves = new ArrayList<HexButton>();
    }

    public void updateTilePlacements()
    {
        legalTilePlacements = new ArrayList<TilePlacementMove>();
        legalVolcanoPlacements = new ArrayList<TilePlacementMove>();
        legalEmptyPlacements = new ArrayList<TilePlacementMove>();
        expansionEnablers = new ArrayList<TilePlacementMove>();
        singleNukes = new ArrayList<TilePlacementMove>();
        doubleNukes = new ArrayList<TilePlacementMove>();

        HexButton targetA;
        HexButton targetB;

        Player activePlayer = board.getActivePlayer();

        Tile activeTile = board.getDeck().getTopTile();
        for(HexButton hex : overallMoveset)
        {
            for(int i = 0; i < 6; i++)
            {
                activeTile.setOrientation(Orientation.values()[i]);
                if(board.tilePlacementIsLegal(activeTile, hex))
                {
                    TilePlacementMove newMove = new TilePlacementMove(board.getActivePlayer(), hex.getOrigin(), activeTile.getOrientation());
                    legalTilePlacements.add(newMove);
                    if (hex.getHex().getTerrain() == Terrain.VOLCANO)
                    {
                        legalVolcanoPlacements.add(newMove);
                        targetA = board.getNeighborButton(hex, activeTile.getOrientation().ordinal());
                        targetB = board.getNeighborButton(hex, activeTile.getOrientationPlus(1).ordinal());
                        Hex targetHexA = targetA.getHex();
                        Hex targetHexB = targetB.getHex();
                        if(targetHexA.isOccupied() && targetHexB.isOccupied() && targetHexA.getOwner() != activePlayer && targetHexB.getOwner() != activePlayer)
                        {
                            doubleNukes.add(newMove);
                        }
                        else if ((targetHexA.isOccupied() && targetHexA.getOwner() != activePlayer) || (targetHexB.isOccupied() && targetHexB.getOwner() != activePlayer))
                        {
                            singleNukes.add(newMove);
                        }

                    }
                    else
                    {
                        legalEmptyPlacements.add(newMove);
                    }
                }
            }
        }


        /*

        for(Settlement settlement : board.getSettlements())
        {
            System.out.println("Checking new settlement");
            if(settlement.getOwner() == board.getActivePlayer())
            {
                System.out.println("Owned settlement found");
                if(!settlement.hasTotoro() && settlement.hasNoExpansions(legalSettlementExpansions))
                {
                    System.out.println("Bottle-necked settlement identified");
                    for(HexButton hex : settlement.getHexes())
                    {
                        for(int i = 0; i < 6; i++)
                        {
                            HexButton neighbor1 = board.getNeighborButton(hex, i);
                            for(int j = 0; j < 6; i++)
                            {
                                HexButton neighbor2 = board.getNeighborButton(neighbor1, j);
                                for(int k = 0; k < 6; k++)
                                {
                                    activeTile.setOrientation(Orientation.values()[k]);
                                    targetA = board.getNeighborButton(neighbor2, k);
                                    targetB = board.getNeighborButton(neighbor2, (k + 1) % 6);
                                    if(board.tilePlacementIsLegal(activeTile, neighbor2))
                                    {
                                        if(board.adjacentToSettlementMember(settlement, targetA) || board.adjacentToSettlementMember(settlement, targetB))
                                        {
                                            TilePlacementMove newMove = new TilePlacementMove(board.getActivePlayer(), hex.getOrigin(), activeTile.getOrientation());
                                            expansionEnablers.add(newMove);
                                            System.out.println("ExpansionEnabler added");
                                            activeTile.setOrientation(board.getDeck().getOrientation());
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        */


        activeTile.setOrientation(board.getDeck().getOrientation());
    }

    public ArrayList<TilePlacementMove> getTilePlacements()
    {
        return legalTilePlacements;
    }

    public ArrayList<BuildingPlacementMove> getLegalVillagerPlacements()
    {
        return legalVillagerPlacements;
    }

    public ArrayList<BuildingPlacementMove> getLegalTigerPlacements()
    {
        return legalTigerPlacements;
    }

    public ArrayList<BuildingPlacementMove> getLegalTotoroPlacements()
    {
        return legalTotoroPlacements;
    }

    public ArrayList<SettlementExpansionMove> getLegalSettlementExpansions()
    {
        return legalSettlementExpansions;
    }

    public ArrayList<BuildingPlacementMove> getLegalBuildingPlacements() { return legalBuildingPlacements; }

    public Board getBoard() { return board; }

    public void updateBuildingPlacements()
    {
        legalBuildingPlacements = new ArrayList<BuildingPlacementMove>();
        legalVillagerPlacements = new ArrayList<BuildingPlacementMove>();
        legalTotoroPlacements = new ArrayList<BuildingPlacementMove>();
        legalTigerPlacements = new ArrayList<BuildingPlacementMove>();

        for(HexButton hex : overallMoveset)
        {
            if(board.buildingPlacementIsLegal(Building.TIGER, hex))
            {
                BuildingPlacementMove move = new BuildingPlacementMove(board.getActivePlayer(), hex.getOrigin(), Building.TIGER);
                legalTigerPlacements.add(move);
                legalBuildingPlacements.add(move);
            }

            if(board.buildingPlacementIsLegal(Building.TOTORO, hex))
            {
                BuildingPlacementMove move = new BuildingPlacementMove(board.getActivePlayer(), hex.getOrigin(), Building.TOTORO);
                legalTotoroPlacements.add(move);
                legalBuildingPlacements.add(move);
            }

            if(board.buildingPlacementIsLegal(Building.VILLAGER, hex))
            {
                BuildingPlacementMove move = new BuildingPlacementMove(board.getActivePlayer(), hex.getOrigin(), Building.VILLAGER);
                legalVillagerPlacements.add(move);
                legalBuildingPlacements.add(move);
            }
        }


    }

    public void updateSettlementExpansions()
    {
        legalSettlementExpansions = new ArrayList<SettlementExpansionMove>();

        CopyOnWriteArrayList<Settlement> settlements = board.getSettlements();
        for(Settlement settlement : settlements)
        {
            if(settlement.getOwner() == board.getActivePlayer())
            {
                for (int i = 0; i < 4; i++)
                {
                    Terrain terrain = Terrain.values()[i];
                    Expansion expansion = board.getSettlementManager().getExpansion(settlement, terrain);
                    if (board.settlementExpansionIsLegal(expansion))
                    {
                        legalSettlementExpansions.add(new SettlementExpansionMove(board.getActivePlayer(), settlement, terrain));
                    }
                }
            }
        }

    }

    public boolean noPossibleBuildActions()
    {
        int moveCount = legalVillagerPlacements.size() + legalTigerPlacements.size() + legalTotoroPlacements.size() + legalSettlementExpansions.size();
        return (moveCount == 0);
    }

    public PlayerMove getNextBuildAction()
    {
        if (noPossibleBuildActions())
        {
            System.out.println("Error: no legal build actions detected");
            return null;
        }

        if(board.getActivePlayer().outOfTigersOrTotoros())
        {
            if(legalSettlementExpansions.size() > 0)
            {
                return legalSettlementExpansions.get(0);
            }
            else if (legalVillagerPlacements.size() > 0)
            {
                return legalVillagerPlacements.get(0);
            }
            else if (legalTigerPlacements.size() > 0)
            {
                return legalTigerPlacements.get(0);
            }
            else
            {
                return legalTotoroPlacements.get(0);
            }
        }
        else
        {
            if (legalTigerPlacements.size() > 0)
            {
                return legalTigerPlacements.get(0);
            }
            else if (legalTotoroPlacements.size() > 0)
            {
                return legalTotoroPlacements.get(rand.nextInt(legalTotoroPlacements.size()));
            }
            else if (legalSettlementExpansions.size() > 0)
            {
                for (SettlementExpansionMove expansionMove : legalSettlementExpansions)
                {
                    if (!expansionMove.getSettlement().hasTotoro())
                    {
                        return expansionMove;
                    }
                }
            }

            return legalVillagerPlacements.get(0);
        }
    }

    public TilePlacementMove getNextTilePlacement()
    {
        //return legalTilePlacements.get(rand.nextInt(legalTilePlacements.size()));

        /*
        if(expansionEnablers.size() > 0)
        {
            return expansionEnablers.get(rand.nextInt(expansionEnablers.size()));
        }
        */

        if(doubleNukes.size() > 0)
        {
            System.out.println("Double nuking!");
            return doubleNukes.get(0);
        }

        if(singleNukes.size() > 0)
        {
            System.out.println("Single nuking!");
            return singleNukes.get(0);
        }

        if(legalVolcanoPlacements.size() > 0)
        {
            return legalVolcanoPlacements.get(rand.nextInt(legalVolcanoPlacements.size()));
        }
        else
        {
            return legalEmptyPlacements.get(rand.nextInt(legalEmptyPlacements.size()));
        }
    }

    public void selectAndPlayMove()
    {
        if(!board.getTilePlaced())
        {
            board.processTurn(getNextTilePlacement());
        }
        else
        {
            board.processTurn(getNextBuildAction());
        }
    }
}

class PrematurePreston extends MoveAnalyzer
{
    public PrematurePreston(Board board)
    {
        super(board);
    }

    public TilePlacementMove getNextTilePlacement()
    {
        return super.getTilePlacements().get(0);
    }

    public PlayerMove getNextBuildAction()
    {
        ArrayList<BuildingPlacementMove> legalBuildingPlacements = super.getLegalBuildingPlacements();

        if (super.noPossibleBuildActions())
        {
            System.out.println("Error: no legal build actions detected");
            return null;
        }

        if(legalBuildingPlacements.size() > 0)
        {
            return legalBuildingPlacements.get(0);
        }
        else
        {
            return super.getLegalSettlementExpansions().get(0);
        }
    }

}

class RandomRandy extends MoveAnalyzer
{
    Random rand;

    public RandomRandy(Board board)
    {
        super(board);
        rand = new Random();
    }

    public TilePlacementMove getNextTilePlacement()
    {
        ArrayList<TilePlacementMove> legalTilePlacements = super.getTilePlacements();
        return legalTilePlacements.get(rand.nextInt(legalTilePlacements.size()));
    }

    public PlayerMove getNextBuildAction()
    {
        if (super.noPossibleBuildActions())
        {
            System.out.println("Error: no legal build actions detected");
            return null;
        }

        ArrayList<BuildingPlacementMove> legalTigerPlacements = super.getLegalTigerPlacements();
        ArrayList<BuildingPlacementMove> legalTotoroPlacements = super.getLegalTotoroPlacements();
        ArrayList<BuildingPlacementMove> legalVillagerPlacements = super.getLegalVillagerPlacements();

        if(legalTigerPlacements.size() > 0)
        {
            return legalTigerPlacements.get(0);
        }

        if(legalTotoroPlacements.size() > 0)
        {
            return legalTotoroPlacements.get(0);
        }

        if(legalVillagerPlacements.size() > 0)
        {
            return legalVillagerPlacements.get(0);
        }

        else
        {
            return super.getLegalSettlementExpansions().get(0);
        }
    }
}

