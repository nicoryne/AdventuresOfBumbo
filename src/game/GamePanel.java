package game;

import game.equips.weapons.*;
import game.ui.*;
import game.ui.dialogs.LoginDialog;
import game.ui.dialogs.RegisterDialog;
import game.util.ScreenStates;
import game.ui.titlescreen.TitleScreen;
import game.util.GameLoopSingleton;
import game.util.handlers.SoundHandler;
import game.util.managers.SpritesManager;
import services.LoggerHelper;
import services.models.Score;
import services.server.DBConnection;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.sql.Time;
import java.util.logging.Logger;

/**
 * The GamePanel class represents a JPanel used for displaying a game screen.
 * It provides methods to set up the panel dimensions and appearance, as well
 * as functionality to run the game loop in a separate thread.
 */
public class GamePanel extends JPanel implements Runnable {

    private GameLoopSingleton gameLoopSingleton;
    private final Game game;
    private ScreenStates screenState;
    private LoginDialog loginDialog;
    private RegisterDialog registerDialog;
    private final JFrame window;
    private static final int MENU_UPDATE_COOLDOWN_MS = 200;
    private long lastMenuUpdateTime = 0;
    private boolean isPaused = false;
    private boolean isPickBuff = false;
    private int buffSelector = 0;
    private int introDialogueCounter = 1;
    private final int DIALOGUE_INCREMENT_DELAY = 5000;
    private long lastDialogueIncrementTime = 0;

    public GamePanel(JFrame window){
        this.game = Game.getInstance();
        this.screenState = ScreenStates.INTRO;
        this.window = window;
        game.setupGame(this);
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
        switch(screenState) {
            case INTRO:
                loadIntro();
                boolean isMenuEntered = Game.getInstance().getControllerComponents().getKeyboardController().isMenuEntered();
                if(isMenuEntered) {
                    handleIntroScreen();
                }
                timerDialogue(5000);
                timerPanelTransition(82000);
                break;
            case TITLE_SCREEN:
                menuSelector();
                checkTitleScreenEntered();
                break;
            case PLAYING:
                Game.getInstance().updateEntities();
                toggleGameState();
                if(isPlayerDead()) {
                    screenState = ScreenStates.DEAD;
                }

                if(isPickBuff) {
                    screenState = ScreenStates.BUFF_SELECTION;
                }

                break;
            case PAUSED:
                toggleGameState();
                break;
            case DEAD:
                timerPanelTransition(3000);
                break;
            case BUFF_SELECTION:
                if(buffSelector()) {
                    screenState = ScreenStates.PLAYING;
                    isPickBuff = false;
                }
                break;
        }
        playAudio();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        switch(screenState) {
            case TITLE_SCREEN -> drawTitle(g2);
            case PLAYING -> drawPlaying(g2);
            case PAUSED -> drawPaused(g2);
            case DEAD -> drawDead(g2);
            case RESULT -> drawResultScreen(g2, game.getLatestScore());
            case BUFF_SELECTION -> drawBuffSelection(g2);
            case INTRO -> drawIntroScreen(g2);
        }
    }

    private void toggleGameState() {
        if(Game.getInstance().getControllerComponents().getKeyboardController().isPaused()) {
            screenState = ScreenStates.PAUSED;
        } else {
            screenState = ScreenStates.PLAYING;
        }
    }

    private void playAudio() {
        switch(screenState) {
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

    private boolean buffSelector() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastMenuUpdateTime >= MENU_UPDATE_COOLDOWN_MS) {
            if (Game.getInstance().getControllerComponents().getKeyboardController().isMenuIncrement()) {
                if(buffSelector < 3) {
                    buffSelector++;
                }
                lastMenuUpdateTime = currentTime;
            } else if (Game.getInstance().getControllerComponents().getKeyboardController().isMenuDecrement()) {
                if(buffSelector > 0) {
                    buffSelector--;
                }
                lastMenuUpdateTime = currentTime;
            } else if(Game.getInstance().getControllerComponents().getKeyboardController().isMenuEntered()) {
                return applyBuff();
            }
        }
        return false;
    }

    private boolean applyBuff() {
        switch(buffSelector) {
            case 1: // HEAL
                int currentMaxHp = Game.getInstance().getPlayer().getStatComponent().getMaxHitPoints();
                Game.getInstance().getPlayer().getStatComponent().setCurrentHitPoints(currentMaxHp);
                return true;
            case 0: // SPEED
                int currentSpeed = Game.getInstance().getPlayer().getStatComponent().getSpeed();
                currentSpeed += (int) Math.ceil(currentSpeed * 0.2);
                Game.getInstance().getPlayer().getStatComponent().setSpeed(currentSpeed);
                return true;
            case 2: // DMG
                int currentDamage = Game.getInstance().getPlayer().getStatComponent().getDamage();
                currentDamage += (int) Math.ceil(currentDamage * 0.8);
                Game.getInstance().getPlayer().getStatComponent().setDamage(currentDamage);
                return true;
        }
        return false;
    }

    private void checkTitleScreenEntered() {
        boolean isMenuEntered = Game.getInstance().getControllerComponents().getKeyboardController().isMenuEntered();
        if(isMenuEntered) {
            switch(TitleScreen.getTitleState()) {
                case BOARDING -> handleBoardingScreen();
                case MENU -> handleMenuScreen();
                case WEAPON_SELECTION -> handleWeaponScreen();
                case LEADERBOARD -> handleLeaderboardScreen();
            }
        }
    }

    private void timerPanelTransition(int delay) {
        Timer timer = new Timer(delay, e -> {
            switch (screenState) {
                case DEAD -> handleDeadScreen();
                case RESULT -> handleResultScreen();
                case INTRO -> handleIntroScreen();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void timerDialogue(int delay) {
        Timer timer = new Timer(delay, e -> {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastDialogueIncrementTime >= DIALOGUE_INCREMENT_DELAY) {
                introDialogueCounter++;
                lastDialogueIncrementTime = currentTime;
                repaint();
            }
        });
        timer.setRepeats(true);
        timer.start();
    }

    private void handleResultScreen() {

    }

    private void loadIntro() {
        SoundHandler.playAudio("intro", 0, 0.8f);
    }

    private void handleDeadScreen() {
        int points = game.getPlayer().getPoints();
        Time time = game.getStopwatch().getTime();
        int userId = game.getUser().getUserId();
        String weaponUsed = game.getPlayer().getWeapon().getWeaponName().toString();
        Score score = new Score(
                points,
                time,
                userId,
                weaponUsed
        );

        if(addScore(score)) {
            LoggerHelper.logInfo("[GamePanel] Added new score for user id: " + score.getUserId());
        } else {
            LoggerHelper.logWarning("[GamePanel] Failed to add new score for user id: " + score.getScoreId());
        }

        screenState = ScreenStates.TITLE_SCREEN;
        TitleScreen.setTitleState(TitleScreen.TitleScreenState.MENU);
        Game.getInstance().restartGame();
    }

    private void handleIntroScreen() {
        SoundHandler.stopAudio("intro");
        screenState = ScreenStates.TITLE_SCREEN;
        TitleScreen.setTitleState(TitleScreen.TitleScreenState.BOARDING);
    }


    private boolean addScore(Score score) {
        DBConnection db = new DBConnection();
        try {
            return db.getScoreDML().addUScore(score);
        } finally {
            db.close();
        }
    }

    private void handleMenuScreen() {
        int selected = TitleScreen.getMenuCounter();
        switch(selected) {
            case 0:
                TitleScreen.setTitleState(TitleScreen.TitleScreenState.WEAPON_SELECTION);
                break;
            case 1:
                TitleScreen.setTitleState(TitleScreen.TitleScreenState.LEADERBOARD);
                break;
            case 2:
                exitProgram();
                break;
        }
    }

    private void handleWeaponScreen() {
        int selected = TitleScreen.getMenuCounter();
        switch(selected) {
            case 0:
                TitleScreen.setTitleState(TitleScreen.TitleScreenState.MENU);
                break;
            case 1:
                setPlayerWeapon(WeaponNames.SWORD);
                playGame();
                break;
            case 2:
                setPlayerWeapon(WeaponNames.STAFF);
                playGame();
                break;
            case 3:
                setPlayerWeapon(WeaponNames.BOW);
                playGame();
                break;
        }
    }

    private void handleLeaderboardScreen() {
        int selected = TitleScreen.getMenuCounter();
        switch(selected) {
            case 0:
                TitleScreen.setTitleState(TitleScreen.TitleScreenState.MENU);
                break;
        }
    }

    private void setPlayerWeapon(WeaponNames weapon) {
        LoggerHelper.logInfo(weapon.name());
        switch(weapon) {
            case SWORD:
                Sword sword = new Sword();
                Game.getInstance().getPlayer().setWeapon(sword);
                Game.getInstance().getPlayer().setAttackSpritesManager(new SpritesManager("worker/attack/sword", 4, 1));
                break;
            case STAFF:
                Staff staff = new Staff();
                Game.getInstance().getPlayer().setWeapon(staff);
                Game.getInstance().getPlayer().setAttackSpritesManager(new SpritesManager("worker/attack/staff", 4, 1));
                break;
            case BOW:
                Bow bow = new Bow();
                Game.getInstance().getPlayer().setWeapon(bow);
                Game.getInstance().getPlayer().setAttackSpritesManager(new SpritesManager("worker/attack/bow", 4, 1));
                break;
        }
    }

    private void playGame() {
        TitleScreen.setTitleState(TitleScreen.TitleScreenState.PLAYING);
        screenState = ScreenStates.PLAYING;
    }

    private void handleLoginScreen() {
            loginDialog = new LoginDialog(window);
            loginDialog.setVisible(true);
    }

    private void handleRegisterScreen() {
        registerDialog = new RegisterDialog(window);
        registerDialog.setVisible(true);
    }

    private void handleBoardingScreen() {
        int selected = TitleScreen.getMenuCounter();
        switch(selected) {
            case 0 -> handleLoginScreen();
            case 1 -> handleRegisterScreen();
            case 2 -> exitProgram();
        }
    }

    private void exitProgram() {
        LoggerHelper.logInfo("Exiting program");
        System.exit(0);
    }

    private void drawTitle(Graphics2D g2) {
        TitleScreen.draw(g2);
    }

    private void drawPlaying(Graphics2D g2) {
        Game.getInstance().renderEntities(g2);
        isPaused = false;
    }

    private void drawPaused(Graphics2D g2) {
        drawPlaying(g2);
        isPaused = true;
        PauseScreen.draw(g2);
    }

    private void drawDead(Graphics2D g2) {
        drawPlaying(g2);
        DeadScreen.draw(g2);
    }

    private void drawBuffSelection(Graphics2D g2) {
        drawPlaying(g2);
        BuffSelectionScreen.draw(g2, buffSelector);
    }

    private void drawResultScreen(Graphics2D g2, Score score) {
        ResultScreen.draw(g2, score);
    }

    private void drawIntroScreen(Graphics2D g2) {
        IntroScreen.draw(g2, introDialogueCounter);
    }

    private boolean isPlayerDead() {
        return !Game.getInstance().getPlayer().getRenderComponent().isAlive();
    }

    public ScreenStates getScreenState() {
        return screenState;
    }

    public void setScreenState(ScreenStates screenState) {
        this.screenState = screenState;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPickBuff(boolean state) {
        isPickBuff = state;
    }
}
