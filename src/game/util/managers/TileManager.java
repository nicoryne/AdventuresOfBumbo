package game.util.managers;

import game.Game;
import game.entities.tile.CollidableTiles;
import game.entities.tile.Tile;
import game.util.handlers.FileHandler;
import game.util.handlers.ImageHandler;
import game.util.handlers.MapHandler;
import game.util.handlers.RenderHandler;
import services.LoggerHelper;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class TileManager {

    private final ArrayList<Tile> tileArrayList;
    private final int[][] mapTile2DArray;
    private static int MAX_WORLD_COL;
    private static int MAX_WORLD_ROW;
    private static int TILE_SIZE;

    public TileManager() {
        MAX_WORLD_COL = Integer.parseInt(Game.getInstance().getProperty("MAX_WORLD_COL"));
        MAX_WORLD_ROW = Integer.parseInt(Game.getInstance().getProperty("MAX_WORLD_ROW"));
        TILE_SIZE = Integer.parseInt(Game.getInstance().getProperty("TILE_SIZE"));
        this.tileArrayList = loadTilesFromFolder();
        this.mapTile2DArray = MapHandler.loadMap("world02.txt", MAX_WORLD_COL, MAX_WORLD_ROW);
    }

    private ArrayList<Tile> loadTilesFromFolder() {
        ArrayList<Tile> tiles = new ArrayList<>();
        File[] directoryListing = FileHandler.getListingsFromRes("tiles");

        if (directoryListing != null) {
            for (File child : directoryListing) {
                BufferedImage tileImage = ImageHandler.getBufferedImage(child);

                if(tileImage == null) {
                    continue;
                }

                BufferedImage scaledImage = ImageHandler.scaleImageBasedOnTileSize(tileImage, 1);

                boolean isCollidable = checkIfCollidable(retrieveTileName(String.valueOf(child)));
                tiles.add(new Tile(scaledImage, isCollidable));
                LoggerHelper.logInfo("[TileManager] Loaded tile image file: " + child.getName());
            }
        }

        return tiles;
    }

    private String retrieveTileName(String filename) {
        String nameWithoutExtension = filename.substring(0, filename.lastIndexOf('.'));

        String[] parts = nameWithoutExtension.split("_");

        try {
            if (parts.length >= 2) {
                String tileName = String.join("_", Arrays.copyOfRange(parts, 1, parts.length));
                return tileName.toUpperCase();
            } else {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            LoggerHelper.logError("Invalid tile filename format: " + filename, e);
        }

        return null;
    }

    private boolean checkIfCollidable(String tileName) {
        for(CollidableTiles tile : CollidableTiles.values()) {
            if(Objects.equals(tile.toString(), tileName)) {
                return true;
            }
        }
        return false;
    }

    public void render(Graphics2D g2) {
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

        if (RenderHandler.isViewableOnScreen(worldX, worldY)) {
            BufferedImage image = tileArrayList.get(tileNum).getRenderComponent().getSprite();
            RenderHandler.renderOnScreen(worldX, worldY, image, g2, null);
        }
    }


    public int[][] getMapTile2DArray() {
        return mapTile2DArray;
    }

    public ArrayList<Tile> getTileArrayList() {
        return tileArrayList;
    }
}
