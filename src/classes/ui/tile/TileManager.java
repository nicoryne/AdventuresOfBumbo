package classes.ui.tile;

import classes.ui.components.GamePanel;
import classes.util.ImageHandler;
import classes.util.MapHandler;

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
                boolean isSolid = checkIfCollidable(retrieveTileName(String.valueOf(child)));
                tiles.add(new Tile(tileImage, isSolid));
                System.out.println(child.getPath() + " (Collidable: " + isSolid + " )");
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

    public void draw(Graphics2D g2) {
        for (int worldRow = 0; worldRow < MAX_WORLD_ROW; worldRow++) {
            for (int worldCol = 0; worldCol < MAX_WORLD_COL; worldCol++) {
                drawTile(g2, worldCol, worldRow);
            }
        }
    }

    private void drawTile(Graphics2D g2, int worldCol, int worldRow) {
        int tileNum = mapTile2DArray[worldCol][worldRow];
        int worldX = worldCol * TILE_SIZE;
        int worldY = worldRow * TILE_SIZE;

        if (isTileInViewableArea(worldX, worldY)) {
            int screenX = calculateScreenX(worldX);
            int screenY = calculateScreenY(worldY);
            g2.drawImage(tileArrayList.get(tileNum).getImage(), screenX, screenY, TILE_SIZE, TILE_SIZE, null);
        }
    }

    private boolean isTileInViewableArea(int worldX, int worldY) {
        int playerViewableMinX = getGamePanel().getPlayerViewableMinX();
        int playerViewableMinY = getGamePanel().getPlayerViewableMinY();
        int playerViewableMaxX = getGamePanel().getPlayerViewableMaxX();
        int playerViewableMaxY = getGamePanel().getPlayerViewableMaxY();

        return worldX + TILE_SIZE > playerViewableMinX &&
                worldX - TILE_SIZE < playerViewableMaxX &&
                worldY + TILE_SIZE > playerViewableMinY &&
                worldY - TILE_SIZE < playerViewableMaxY;
    }

    private int calculateScreenX(int worldX) {
        int playerViewableMinX = getGamePanel().getPlayerViewableMinX();
        return worldX - playerViewableMinX;
    }

    private int calculateScreenY(int worldY) {
        int playerViewableMinY = getGamePanel().getPlayerViewableMinY();
        return worldY - playerViewableMinY;
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }
}
