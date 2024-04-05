package classes.entities.tile;

import classes.ui.components.GamePanel;
import classes.util.handlers.ImageHandler;
import classes.util.handlers.MapHandler;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class TileManager {

    private final ArrayList<Tile> tileArrayList;
    private final int[][] mapTile2DArray;
    private final GamePanel gamePanel;
    private static int MAX_WORLD_COL;
    private static int MAX_WORLD_ROW;
    private static int TILE_SIZE;

    public TileManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        MAX_WORLD_COL = gamePanel.getMaxWorldCol();
        MAX_WORLD_ROW = gamePanel.getMaxWorldRow();
        TILE_SIZE = gamePanel.getTileSize();
        this.tileArrayList = loadTilesFromFolder();
        this.mapTile2DArray = MapHandler.loadMap("world01.txt", MAX_WORLD_COL, MAX_WORLD_ROW);
    }

    private ArrayList<Tile> loadTilesFromFolder() {
        ArrayList<Tile> tiles = new ArrayList<>();
        File[] directoryListing = ImageHandler.getListingsFromRes("tiles");

        System.out.println("TILES LOADED:");

        if (directoryListing != null) {
            for (File child : directoryListing) {
                BufferedImage tileImage = ImageHandler.getBufferedImage(child);
                assert tileImage != null;

                BufferedImage scaledImage = new BufferedImage(TILE_SIZE, TILE_SIZE, tileImage.getType());
                Graphics2D g = scaledImage.createGraphics();
                g.drawImage(tileImage, 0, 0, TILE_SIZE, TILE_SIZE, null);
                g.dispose();

                boolean isCollidable = checkIfCollidable(retrieveTileName(String.valueOf(child)));
                tiles.add(new Tile(scaledImage, isCollidable));
                System.out.println(child.getPath() + " (Collidable: " + isCollidable + ")");
            }
        }

        return tiles;
    }

    private String retrieveTileName(String filename) {
        String nameWithoutExtension = filename.substring(0, filename.lastIndexOf('.'));

        String[] parts = nameWithoutExtension.split("_");

        if (parts.length >= 2) {
            String tileName = String.join("_", Arrays.copyOfRange(parts, 1, parts.length));
            return tileName.toUpperCase();
        } else {
            throw new IllegalArgumentException("Invalid filename format: " + filename);
        }
    }

    private boolean checkIfCollidable(String tileName) {
        for(CollidableTiles tile : CollidableTiles.values()) {
            if(Objects.equals(tile.toString(), tileName)) {
                return true;
            }
        }
        return false;
    }

    public void render(Graphics2D g) {
        for (int worldRow = 0; worldRow < MAX_WORLD_ROW; worldRow++) {
            for (int worldCol = 0; worldCol < MAX_WORLD_COL; worldCol++) {
                drawTile(g, worldCol, worldRow);
            }
        }
    }

    private void drawTile(Graphics2D g, int worldCol, int worldRow) {
        int tileNum = mapTile2DArray[worldCol][worldRow];
        int worldX = worldCol * TILE_SIZE;
        int worldY = worldRow * TILE_SIZE;

        if (gamePanel.getRenderHelper().isViewableOnScreen(worldX, worldY)) {
            BufferedImage image = tileArrayList.get(tileNum).getImage();
            gamePanel.getRenderHelper().renderOnScreen(worldX, worldY, image, g);
        }
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public int[][] getMapTile2DArray() {
        return mapTile2DArray;
    }

    public ArrayList<Tile> getTileArrayList() {
        return tileArrayList;
    }
}
