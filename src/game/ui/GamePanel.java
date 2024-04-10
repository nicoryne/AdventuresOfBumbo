package game.ui;

import game.Game;
import game.GameState;
import game.exceptions.GameInitializationException;
import game.ui.components.forms.LoginForm;
import game.ui.components.titlescreen.LoginScreen;
import game.ui.components.titlescreen.TitleScreen;
import game.util.GameLoopSingleton;
import game.util.handlers.SoundHandler;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 * The GamePanel class represents a JPanel used for displaying a game screen.
 * It provides methods to set up the panel dimensions and appearance, as well
 * as functionality to run the game loop in a separate thread.
 */
public class GamePanel extends JPanel implements Runnable {

    private GameLoopSingleton gameLoopSingleton;
    private final Game game;
    private GameState currentState;
    private JFrame window;
    private LoginForm loginForm;
    private static final int MENU_UPDATE_COOLDOWN_MS = 200;
    private long lastMenuUpdateTime = 0;

    public GamePanel(JFrame window){
        this.game = Game.getInstance();
        this.window = window;
        game.setupGame();
        this.setPreferredSize(new Dimension(game.getScreenWidth(), game.getScreenHeight()));
        this.setDoubleBuffered(true);
        this.addKeyListener(game.getControllerComponents().getKeyboardController());
        this.addMouseListener(game.getControllerComponents().getMouseController());
        this.addMouseMotionListener(game.getControllerComponents().getMouseController());
        this.setFocusable(true);
    }

    public void startGameThread() {
        Thread gameThread = new Thread(this);
        gameLoopSingleton = GameLoopSingleton.getInstance();

        double FPS = Double.parseDouble(game.getProperty("FPS"));
        gameLoopSingleton.setupGameLoop(FPS, gameThread, this);
        gameThread.start();
    }

    @Override
    public void run() {
        gameLoopSingleton.startGameLoop();
    }

    public void update() {
        playAudio();
        currentState = Game.getInstance().getGameState();

        if(currentState == GameState.TITLE_SCREEN) {
            menuSelector();
        }

        if(currentState == GameState.PLAYING) {
            Game.getInstance().updateEntities();
            toggleGameState();
        }

        if(currentState == GameState.PAUSED) {
            toggleGameState();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        switch(currentState) {
            case TITLE_SCREEN -> drawTitle(g2);
            case PLAYING -> drawPlaying(g2);
            case PAUSED -> drawPaused(g2);
            case DEAD -> drawDead(g2);
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

    private void playAudio() {
        if(currentState == GameState.PLAYING) {
            SoundHandler.playAudio("bgm-1-reincarnated", Clip.LOOP_CONTINUOUSLY, 0.5f);
        }

        if(currentState == GameState.TITLE_SCREEN) {
            SoundHandler.playAudio("bgm-2-vempair", Clip.LOOP_CONTINUOUSLY, 0.5f);
        }
    }

    private void drawTitle(Graphics2D g2) {
        try {
            TitleScreen.draw(g2);
        } catch (IOException | FontFormatException e) {
            throw new RuntimeException(e);
        }
    }

    private void drawPlaying(Graphics2D g2) {
        Game.getInstance().renderEntities(g2);
    }

    private void drawPaused(Graphics2D g2) {
        try {
            PauseScreen.draw(g2);
        } catch (IOException | FontFormatException e) {
            throw new RuntimeException(e);
        }
    }

    private void drawDead(Graphics2D g2) {
        try {
            DeadScreen.draw(g2);
        } catch (IOException | FontFormatException e) {
            throw new RuntimeException(e);
        }
    }

    private void menuSelector() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastMenuUpdateTime >= MENU_UPDATE_COOLDOWN_MS) {
            if (Game.getInstance().getControllerComponents().getKeyboardController().isMenuIncrement()) {
                TitleScreen.incrementMenuItem();
                lastMenuUpdateTime = currentTime;
            } else if (Game.getInstance().getControllerComponents().getKeyboardController().isMenuDecrement()) {
                TitleScreen.decrementMenuItem();
                lastMenuUpdateTime = currentTime;
            }
            if (Game.getInstance().getControllerComponents().getKeyboardController().isMenuEntered()) {
                if (TitleScreen.getTitleState() == TitleScreen.TitleScreenState.BOARDING) {
                    handleBoardingScreen();
                    lastMenuUpdateTime = currentTime;
                }
            }
        }
    }

    private void handleBoardingScreen() {
        int selected = TitleScreen.getMenuCounter();

        if (selected == 0) {
            TitleScreen.setTitleState(TitleScreen.TitleScreenState.LOGIN);
            loginForm = new LoginForm();
        }

        if(selected == 2) {
            System.exit(0);
        }
    }
}
