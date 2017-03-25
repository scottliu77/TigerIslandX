import org.junit.*;

import java.awt.*;

public class HexAndTileTest {


    @Test
    public void testHexTerrainGeneration() throws Exception {
        Hex genericHex = new Hex(Terrain.GRASS, 1);
        assert (genericHex.getTypeName() == "GRASS");
        assert (genericHex.getTerrain() == Terrain.GRASS);
    }
    @Test
    public void testHexLevel() throws Exception {
        Hex genericHex = new Hex(Terrain.GRASS,1);
        int arbitraryHeight = 123;
        genericHex.setLevel(arbitraryHeight);
        assert (genericHex.getLevel()==arbitraryHeight);
    }
    @Test
    public void testHexID() throws Exception {
        int expectedID = 456;
        Hex genericHex = new Hex(Terrain.GRASS,expectedID);
        assert (genericHex.getTileId()==expectedID);
    }
    @Test
    public void testHexUnoccupied() throws Exception {
        Hex genericHex = new Hex(Terrain.GRASS,1);
        assert (genericHex.isOccupied()==false);
    }
    public void testHexOccupied() throws Exception {
        Hex genericHex = new Hex(Terrain.GRASS,1);
        genericHex.placeBuilding(Building.TOTORO, new Player("Generic Player", Color.BLACK, Color.WHITE));
        assert (genericHex.isOccupied()==true);
    }
    @Test
    public void testHexPlaceBuilding() throws Exception {
        Hex genericHex = new Hex(Terrain.GRASS,1);
        genericHex.placeBuilding(Building.TOTORO, new Player("Generic Player", Color.BLACK, Color.WHITE));
        assert (genericHex.getBuilding()==Building.TOTORO);
    }

    @Test
    public void testTileVolcanoGeneration() throws Exception {
        int genericTileID = 123;
        Hex genericHexA = new Hex(Terrain.GRASS,genericTileID);
        Hex genericHexB = new Hex(Terrain.JUNGLE,genericTileID);
        Orientation genericOrientation = Orientation.SW;
        int expectedTileID = genericTileID;
        Hex expectedVolcano = new Hex(Terrain.VOLCANO, expectedTileID);
        Tile genericTile = new Tile(genericHexA, genericHexB, genericTileID, genericOrientation);
        assert (genericTile.getVolcano().getTileId()==expectedVolcano.getTileId() && genericTile.getVolcano().getLevel()==expectedVolcano.getLevel() && genericTile.getVolcano().getTypeName()==expectedVolcano.getTypeName());
    }
    @Test
    public void testTileHexA() throws Exception {
        int genericTileID = 123;
        Hex genericHexA = new Hex(Terrain.GRASS,genericTileID);
        Hex genericHexB = new Hex(Terrain.JUNGLE,genericTileID);
        Orientation genericOrientation = Orientation.SW;
        Tile genericTile = new Tile(genericHexA, genericHexB, genericTileID, genericOrientation);
        assert (genericTile.getA().getTypeName()=="GRASS");
    }
    @Test
    public void testTileHexB() throws Exception {
        int genericTileID = 123;
        Hex genericHexA = new Hex(Terrain.GRASS,genericTileID);
        Hex genericHexB = new Hex(Terrain.JUNGLE,genericTileID);
        Orientation genericOrientation = Orientation.SW;
        Tile genericTile = new Tile(genericHexA, genericHexB, genericTileID, genericOrientation);
        assert (genericTile.getB().getTypeName()=="JUNGLE");
    }
    @Test
    public void testTileRotRight() throws Exception {
        int genericTileID = 123;
        Hex genericHexA = new Hex(Terrain.GRASS, genericTileID);
        Hex genericHexB = new Hex(Terrain.JUNGLE, genericTileID);
        Orientation genericOrientation = Orientation.SW;
        Tile genericTile = new Tile(genericHexA, genericHexB, genericTileID, genericOrientation);
        genericTile.rotRight();
        assert (genericTile.getOrientation()==5);
    }
    @Test
    public void testTileRotLeft() throws Exception {
        int genericTileID = 123;
        Hex genericHexA = new Hex(Terrain.GRASS, genericTileID);
        Hex genericHexB = new Hex(Terrain.JUNGLE, genericTileID);
        Orientation genericOrientation = Orientation.SW;
        Tile genericTile = new Tile(genericHexA, genericHexB, genericTileID, genericOrientation);
        genericTile.rotLeft();
        assert (genericTile.getOrientation()==3);
    }
    @Test
    public void testTileSetOrientation() throws Exception {
        int genericTileID = 123;
        Hex genericHexA = new Hex(Terrain.GRASS, genericTileID);
        Hex genericHexB = new Hex(Terrain.JUNGLE, genericTileID);
        Orientation genericOrientation = Orientation.SW;
        Tile genericTile = new Tile(genericHexA, genericHexB, genericTileID, genericOrientation);
        genericTile.setOrientation(Orientation.NE);
        assert (genericTile.getOrientation()==1);
    }
    @Test
    public void testTileOrientationPlus() throws Exception {
        int genericTileID = 123;
        Hex genericHexA = new Hex(Terrain.GRASS, genericTileID);
        Hex genericHexB = new Hex(Terrain.JUNGLE, genericTileID);
        Orientation genericOrientation = Orientation.SW;
        Tile genericTile = new Tile(genericHexA, genericHexB, genericTileID, genericOrientation);
        assert (genericTile.getTileId()==genericTileID);
    }
}