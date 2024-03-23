package classes.ui.components;

import classes.entity.Player;
import classes.util.handlers.KeyboardController;

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
    private static Thread GAME_THREAD;
    private static final KeyboardController KEYBOARD_CONTROLLER = new KeyboardController();
    private final Player player = new Player(this, KEYBOARD_CONTROLLER);
    private static final int PLAYER_SPEED = 1;
    private static final double FPS = 60.0;


    public GamePanel() {
        this.setPreferredSize(new Dimension(getScreenWidth(), getScreenHeight())); // Sets preferred size
        this.setBackground(Color.black); // Sets background color to black
        this.setDoubleBuffered(true); // Enables double buffering for smoother graphics
        this.addKeyListener(KEYBOARD_CONTROLLER);
        this.setFocusable(true);
    }

    public void startGameThread() {
        GAME_THREAD = new Thread(this); // Create a new thread with this GamePanel as the target
        GAME_THREAD.start(); // Start the thread, which will execute the run() method
    }

    @Override
    public void run() {
        // This method will continuously run until the game thread is stopped
        // Variable Delta Time game loop
        double drawInterval = 1000000000 / FPS;
        double delta = 0.0;
        long prevTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while(GAME_THREAD != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - prevTime) / drawInterval;
            timer += (currentTime - prevTime);
            prevTime = currentTime;

            if(delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if(timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
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
