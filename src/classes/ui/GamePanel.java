package classes.ui;

import classes.Game;
import classes.GameState;
import classes.exceptions.GameInitializationException;
import classes.util.GameLoopSingleton;
import classes.util.handlers.SoundHandler;

import javax.sound.sampled.Clip;
import javax.swing.JPanel;
import java.awt.*;
import java.io.IOException;

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

    public void startGameThread() {
        Thread gameThread = new Thread(this); // Create a new thread with this GamePanel as the target
        gameLoopSingleton = GameLoopSingleton.getInstance();

        double FPS = Double.parseDouble(game.getProperty("FPS"));
        gameLoopSingleton.setupGameLoop(FPS, gameThread, this);
        gameThread.start(); // Start the thread, which will execute the run() method
    }

    @Override
    public void run() {
        SoundHandler.playAudio("bgm-1-reincarnated", Clip.LOOP_CONTINUOUSLY, 0.5f);
        gameLoopSingleton.startGameLoop();
    }

    public void update() {
        toggleGameState();

        if(Game.getInstance().getGameState() == GameState.PLAYING) {
            Game.getInstance().updateEntities();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        Game.getInstance().renderEntities(g2);

        if(Game.getInstance().getGameState() == GameState.PAUSED) {
            try {
                PauseScreen.draw(g2);
            } catch (IOException | FontFormatException e) {
                throw new RuntimeException(e);
            }
        } else if (Game.getInstance().getGameState() == GameState.DEAD) {
            try {
                DeadScreen.draw(g2);
            } catch (IOException | FontFormatException e) {
                throw new RuntimeException(e);
            }
        }

        g2.dispose();
    }

    private void toggleGameState() {
        if(Game.getInstance().getControllerComponents().getKeyboardController().isPaused()) {
            Game.getInstance().setGameState(GameState.PAUSED);
        } else {
            Game.getInstance().setGameState(GameState.PLAYING);
        }
    }
}
