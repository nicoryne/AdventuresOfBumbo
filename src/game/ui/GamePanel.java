package game.ui;

import game.Game;
import game.GameState;
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
    private JFrame window;
    private LoginFrame loginFrame;
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

                if (TitleScreen.getTitleState() == TitleScreen.TitleScreenState.LOGIN) {
                    handleLoginScreen();
                }

                if(TitleScreen.getTitleState() == TitleScreen.TitleScreenState.MENU) {
                    handleMenuScreen();
                    lastMenuUpdateTime = currentTime;
                }
            }
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

    private void handleBoardingScreen() {
        int selected = TitleScreen.getMenuCounter();

        if (selected == 0) {
            TitleScreen.setTitleState(TitleScreen.TitleScreenState.LOGIN);
        }

        if(selected == 2) {
            LoggerHelper.logInfo("Exiting program");
            System.exit(0);
        }
    }

    private void handleLoginScreen() {
        if(loginFrame == null) {
            loginFrame = new LoginFrame();
        }

        loginFrame.getBackBtn().addActionListener(v -> {
            loginFrame.destroy();
            loginFrame = null;
            TitleScreen.setTitleState(TitleScreen.TitleScreenState.BOARDING);
        });

        loginFrame.getSubmitBtn().addActionListener(v -> {
            String username = loginFrame.getUnameField().getText();
            String password = "admin";

            if(loginUser(username, password)) {
                loginFrame.destroy();
                TitleScreen.setTitleState(TitleScreen.TitleScreenState.MENU);
                LoggerHelper.logInfo("Welcome " + username + "!");
            } else {
                LoggerHelper.logWarning("Failed to login user.");
            }
        });
    }

    private boolean loginUser(String username, String password) {
        DBConnection db = new DBConnection();
        try {
            return db.getUserDML().loginUser(username, password);
        } finally {
            db.close();
        }
    }
}
