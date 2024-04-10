package game.util.controllers;

import game.Game;
import game.GameState;

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

    private final boolean[] keyGameDirectionStates = new boolean[4]; // Array to store key states for each direction

    private final boolean[] keyGameActionStates = new boolean[1];
    
    private final boolean[] keyTitleActionStates = new boolean[3];

    private boolean isPauseToggled;

    private GameState currentState;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        currentState = Game.getInstance().getGameState();

        if(currentState == GameState.PLAYING || currentState == GameState.PAUSED) {
            updateGameKeyState(e.getKeyCode(), true);
        }

        if(currentState == GameState.TITLE_SCREEN) {
            updateTitleKeyState(e.getKeyCode(), true);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        currentState = Game.getInstance().getGameState();

        if(currentState == GameState.PLAYING || currentState == GameState.PAUSED) {
            updateGameKeyState(e.getKeyCode(), false);
        }

        if(currentState == GameState.TITLE_SCREEN) {
            updateTitleKeyState(e.getKeyCode(), false);
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

    public boolean isMenuIncrement() {
        return keyTitleActionStates[TitleAction.DOWN.ordinal()];
    }

    public boolean isMenuDecrement() {
        return keyTitleActionStates[TitleAction.UP.ordinal()];
    }

    public boolean isMenuEntered() {
        return keyTitleActionStates[TitleAction.ENTER.ordinal()];
    }
}
