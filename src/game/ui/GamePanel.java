package game.ui;

import com.mysql.cj.log.Log;
import game.Game;
import game.ui.dialogs.LoginDialog;
import game.util.GameState;
import game.ui.forms.LoginFrame;
import game.ui.titlescreen.TitleScreen;
import game.util.GameLoopSingleton;
import game.util.handlers.SoundHandler;
import services.LoggerHelper;
import services.server.DBConnection;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;

/**
 * The GamePanel class represents a JPanel used for displaying a game screen.
 * It provides methods to set up the panel dimensions and appearance, as well
 * as functionality to run the game loop in a separate thread.
 */
public class GamePanel extends JPanel implements Runnable {

    private GameLoopSingleton gameLoopSingleton;
    private final Game game;
    private GameState currentState;
    private LoginDialog loginDialog;
    private JFrame window;
    private static final int MENU_UPDATE_COOLDOWN_MS = 200;
    private long lastMenuUpdateTime = 0;

    private boolean menuEntered = false;

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
        currentState = Game.getInstance().getGameState();

        if(currentState == GameState.TITLE_SCREEN) {
            menuSelector();
            checkEntered();
        }

        if(currentState == GameState.PLAYING) {
            Game.getInstance().updateEntities();
            toggleGameState();
        }

        if(currentState == GameState.PAUSED) {
            toggleGameState();
        }

        playAudio();
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
        switch(currentState) {
            case PLAYING:
                SoundHandler.playAudio("bgm-1-reincarnated", Clip.LOOP_CONTINUOUSLY, 0.5f);
                SoundHandler.stopAudio("bgm-2-vempair");
                break;
            case TITLE_SCREEN:
                SoundHandler.playAudio("bgm-2-vempair", Clip.LOOP_CONTINUOUSLY, 0.5f);
                SoundHandler.stopAudio("bgm-1-reincarnated");
                break;
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
        }
    }

    private void checkEntered() {
        if (Game.getInstance().getControllerComponents().getKeyboardController().isMenuEntered() && !menuEntered) {
            switch(TitleScreen.getTitleState()) {
                case BOARDING -> handleBoardingScreen();
                case MENU -> handleMenuScreen();
            }
        } else if (menuEntered) {
            menuEntered = false;
        }
    }

    private void handleMenuScreen() {
        int selected = TitleScreen.getMenuCounter();

        if (selected == 0) {
            TitleScreen.setTitleState(TitleScreen.TitleScreenState.PLAYING);
            Game.getInstance().setGameState(GameState.PLAYING);
        }

        if(selected == 2) {
            LoggerHelper.logInfo("Exiting program");
            System.exit(0);
        }
    }

    private void handleLoginScreen() {
        LoginDialog loginDialog = new LoginDialog(window);
        loginDialog.setVisible(true);
        menuEntered = true;
    }

    private void handleBoardingScreen() {
        int selected = TitleScreen.getMenuCounter();

        if (selected == 0) {
            handleLoginScreen();
        }

        if(selected == 2) {
            LoggerHelper.logInfo("Exiting program");
            System.exit(0);
        }
    }

    private void drawTitle(Graphics2D g2) {
        TitleScreen.draw(g2);
    }

    private void drawPlaying(Graphics2D g2) {
        Game.getInstance().renderEntities(g2);
    }

    private void drawPaused(Graphics2D g2) {
        PauseScreen.draw(g2);
    }

    private void drawDead(Graphics2D g2) {
        DeadScreen.draw(g2);
    }

}
