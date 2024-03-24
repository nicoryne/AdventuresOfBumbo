package classes.ui.components;

import classes.entities.Player;
import classes.ui.tile.TileManager;
import classes.util.GameLoop;
import classes.util.KeyboardController;

import javax.swing.JPanel;
import java.awt.*;

/**
 * The GamePanel class represents a JPanel used for displaying a game screen.
 * It provides methods to set up the panel dimensions and appearance, as well
 * as functionality to run the game loop in a separate thread.
 */
public class GamePanel extends JPanel implements Runnable {

    // Constants for tile size and screen dimensions
    private static final int ORIGINAL_TILE_SIZE = 16; // Original size of a tile (in pixels)
    private static final int SCALE = 3; // Scale factor for resizing tiles
    private static final double FPS = 60.0;
    private static final int MAX_SCREEN_COL = 16; // Maximum number of columns to display
    private static final int MAX_SCREEN_ROW = 12; // Maximum number of rows to display
    private static final int MAX_WORLD_COL = 50;
    private static final int MAX_WORLD_ROW = 50;
    private static final int WORLD_WIDTH = ORIGINAL_TILE_SIZE * MAX_WORLD_COL;
    private static final int WORLD_LENGTH = ORIGINAL_TILE_SIZE * MAX_WORLD_ROW;
    private final KeyboardController keyboardController = new KeyboardController();
    private GameLoop gameLoop;
    private final Player player = new Player(this, keyboardController);
    private final TileManager tileManager = new TileManager(this);

    public GamePanel() {
        this.setPreferredSize(new Dimension(getScreenWidth(), getScreenHeight())); // Sets preferred size
        this.setBackground(Color.black); // Sets background color to black
        this.setDoubleBuffered(true); // Enables double buffering for smoother graphics
        this.addKeyListener(keyboardController);
        this.setFocusable(true);
    }

    public void startGameThread() {
        Thread gameThread = new Thread(this); // Create a new thread with this GamePanel as the target
        gameLoop = new GameLoop(FPS, gameThread, this);
        gameThread.start(); // Start the thread, which will execute the run() method
    }

    @Override
    public void run() {
        gameLoop.startGameLoop();
    }

    public void update() {
        player.update();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        tileManager.draw(g2);
        player.draw(g2);
        g2.dispose();
    }

    public int getTileSize() {
        return ORIGINAL_TILE_SIZE * SCALE; // Returns scaled tile size
    }

    public int getScreenWidth() {
        return getTileSize() * getMaxScreenCol(); // Returns screen width based on tile size and number of columns
    }

    public int getScreenHeight() {
        return getTileSize() * getMaxScreenRow(); // Returns screen height based on tile size and number of rows
    }

    public int getMaxScreenCol() {
        return MAX_SCREEN_COL;
    }

    public int getMaxScreenRow() {
        return MAX_SCREEN_ROW;
    }

    public int getMaxWorldCol() {
        return MAX_WORLD_COL;
    }

    public int getMaxWorldRow() {
        return MAX_WORLD_ROW;
    }

    public Player getPlayer() {
        return player;
    }

    public int getPlayerViewableMaxX() {
        return getPlayer().getWorldPositionX() + getPlayer().getScreenPositionX();
    }

    public int getPlayerViewableMaxY() {
        return getPlayer().getWorldPositionY() + getPlayer().getScreenPositionY();
    }

    public int getPlayerViewableMinX() {
        return getPlayer().getWorldPositionX() - getPlayer().getScreenPositionX();
    }

    public int getPlayerViewableMinY() {
        return getPlayer().getWorldPositionY() - getPlayer().getScreenPositionY();
    }


}
