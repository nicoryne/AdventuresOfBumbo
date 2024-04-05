package classes.ui;

import classes.Game;
import classes.exceptions.GameInitializationException;
import classes.util.GameLoopSingleton;
import classes.util.handlers.SoundHandler;

import javax.sound.sampled.Clip;
import javax.swing.JPanel;
import java.awt.*;

/**
 * The GamePanel class represents a JPanel used for displaying a game screen.
 * It provides methods to set up the panel dimensions and appearance, as well
 * as functionality to run the game loop in a separate thread.
 */
public class GamePanel extends JPanel implements Runnable {

    private GameLoopSingleton gameLoopSingleton;
    private final Game game;

    public GamePanel() throws GameInitializationException {
        this.game = Game.getInstance();
        game.setupGame();
        this.setPreferredSize(new Dimension(game.getScreenWidth(), game.getScreenHeight())); // Sets preferred size
        this.setBackground(Color.black); // Sets background color to black
        this.setDoubleBuffered(true); // Enables double buffering for smoother graphics
        this.addKeyListener(game.getControllerComponents().getKeyboardController());
        this.addMouseListener(game.getControllerComponents().getMouseController());
        this.addMouseMotionListener(game.getControllerComponents().getMouseController());
        this.setFocusable(true);
    }

    public void setupGame() {

    }

    public void startGameThread() {
        Thread gameThread = new Thread(this); // Create a new thread with this GamePanel as the target
        gameLoopSingleton = GameLoopSingleton.getInstance();

        double FPS = Double.parseDouble(game.getProperty("FPS"));
        gameLoopSingleton.setupGameLoop(FPS, gameThread, this);
        gameThread.start(); // Start the thread, which will execute the run() method
    }

    @Override
    public void run() {
        SoundHandler.playAudio("bgm-1-reincarnated", Clip.LOOP_CONTINUOUSLY, 0.8f);
        gameLoopSingleton.startGameLoop();
    }

    public void update() {
        Game.getInstance().updateEntities();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        double drawStart = 0;
        drawStart = System.nanoTime();

        Game.getInstance().renderEntities(g2);

        double drawEnd = System.nanoTime();
        double passed = (drawEnd - drawStart);

        g2.setColor(Color.white);
        g2.drawString("Draw time: " + passed, 10, 400);

        g2.dispose();
    }
}
