package classes.ui.components;

import classes.entity.Player;
import classes.util.GameLoop;
import classes.util.controllers.KeyboardController;

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
    private static final int MAX_SCREEN_COL = 16; // Maximum number of columns to display
    private static final int MAX_SCREEN_ROW = 12; // Maximum number of rows to display
    private static final int SCALE = 3; // Scale factor for resizing tiles
    private static final double FPS = 60.0;
    private static final KeyboardController KEYBOARD_CONTROLLER = new KeyboardController();
    private static GameLoop gameLoop;
    private final Player player = new Player(this, KEYBOARD_CONTROLLER);


    public GamePanel() {
        this.setPreferredSize(new Dimension(getScreenWidth(), getScreenHeight())); // Sets preferred size
        this.setBackground(Color.black); // Sets background color to black
        this.setDoubleBuffered(true); // Enables double buffering for smoother graphics
        this.addKeyListener(KEYBOARD_CONTROLLER);
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
        player.draw(g2);
        g2.dispose();
    }

    public int getTileSize() {
        return ORIGINAL_TILE_SIZE * SCALE; // Returns scaled tile size
    }

    private int getScreenWidth() {
        return getTileSize() * MAX_SCREEN_COL; // Returns screen width based on tile size and number of columns
    }

    private int getScreenHeight() {
        return getTileSize() * MAX_SCREEN_ROW; // Returns screen height based on tile size and number of rows
    }
}
