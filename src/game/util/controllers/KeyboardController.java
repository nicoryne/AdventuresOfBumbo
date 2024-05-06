package game.util.controllers;

import game.Game;
import game.util.ScreenStates;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardController implements KeyListener {

    private enum GameDirection {
        UP, DOWN, LEFT, RIGHT
    }

    private enum GameAction {
        ATTACK
    }

    private enum TitleAction {
        UP, DOWN, ENTER
    }

    private enum AllAction {
        FULLSCREEN
    }

    private final boolean[] keyGameDirectionStates = new boolean[4]; // Array to store key states for each direction

    private final boolean[] keyGameActionStates = new boolean[1];
    
    private final boolean[] keyTitleActionStates = new boolean[4];

    private final boolean[] keyAllActionStates = new boolean[4];

    private boolean isPauseToggled;

    private ScreenStates currentState;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        currentState = Game.getInstance().getGamePanel().getScreenState();

        switch(currentState) {
            case PLAYING, PAUSED -> updateGameKeyState(e.getKeyCode(), true);
            case TITLE_SCREEN, INTRO -> updateTitleKeyState(e.getKeyCode(), true);
        }

        updateAllKeyState(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        currentState = Game.getInstance().getGamePanel().getScreenState();

        switch(currentState) {
            case PLAYING, PAUSED -> updateGameKeyState(e.getKeyCode(), false);
            case TITLE_SCREEN -> updateTitleKeyState(e.getKeyCode(), false);
        }

        updateAllKeyState(e.getKeyCode(), false);
    }

    private void updateAllKeyState(int keyCode, boolean state) {
        switch (keyCode) {
            case KeyEvent.VK_O:
                keyAllActionStates[AllAction.FULLSCREEN.ordinal()] = state;
                break;
        }
    }

    private void updateGameKeyState(int keyCode, boolean state) {
        switch (keyCode) {
            case KeyEvent.VK_W:
                keyGameDirectionStates[GameDirection.UP.ordinal()] = state;
                break;
            case KeyEvent.VK_S:
                keyGameDirectionStates[GameDirection.DOWN.ordinal()] = state;
                break;
            case KeyEvent.VK_A:
                keyGameDirectionStates[GameDirection.LEFT.ordinal()] = state;
                break;
            case KeyEvent.VK_D:
                keyGameDirectionStates[GameDirection.RIGHT.ordinal()] = state;
                break;
            case KeyEvent.VK_F:
                keyGameActionStates[GameAction.ATTACK.ordinal()] = state;
                break;
            case KeyEvent.VK_P:
                if (state) {
                    isPauseToggled = !isPauseToggled;
                }
                break;
        }
    }

    private void updateTitleKeyState(int keyCode, boolean state) {
        switch (keyCode) {
            case KeyEvent.VK_W, KeyEvent.VK_UP, KeyEvent.VK_KP_UP:
                keyTitleActionStates[TitleAction.UP.ordinal()] = state;
                break;
            case KeyEvent.VK_S, KeyEvent.VK_DOWN, KeyEvent.VK_KP_DOWN:
                keyTitleActionStates[TitleAction.DOWN.ordinal()] = state;
                break;
            case KeyEvent.VK_ENTER:
                keyTitleActionStates[TitleAction.ENTER.ordinal()] = state;
                break;
        }
    }

    public boolean isUpPressed() {
        return keyGameDirectionStates[GameDirection.UP.ordinal()];
    }

    public boolean isDownPressed() {
        return keyGameDirectionStates[GameDirection.DOWN.ordinal()];
    }

    public boolean isLeftPressed() {
        return keyGameDirectionStates[GameDirection.LEFT.ordinal()];
    }

    public boolean isRightPressed() {
        return keyGameDirectionStates[GameDirection.RIGHT.ordinal()];
    }

    public boolean isAttacking() {
        return keyGameActionStates[GameAction.ATTACK.ordinal()];
    }

    public boolean isPaused() {
        return isPauseToggled;
    }

    public boolean isFullScreen() {
        return keyAllActionStates[AllAction.FULLSCREEN.ordinal()];
    }

    public boolean isMenuIncrement() {
        return keyTitleActionStates[TitleAction.DOWN.ordinal()];
    }

    public boolean isMenuDecrement() {
        return keyTitleActionStates[TitleAction.UP.ordinal()];
    }

    public boolean isMenuEntered() {
        boolean menuEntered = keyTitleActionStates[TitleAction.ENTER.ordinal()];
        keyTitleActionStates[TitleAction.ENTER.ordinal()] = false;
        return menuEntered;
    }
}
