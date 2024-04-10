package classes.util.controllers;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardController implements KeyListener {

    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    private enum Action {
        ATTACK
    }

    private final boolean[] keyDirectionStates = new boolean[4]; // Array to store key states for each direction

    private final boolean[] keyActionStates = new boolean[1];

    private boolean isPauseToggled;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        updateKeyState(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        updateKeyState(e.getKeyCode(), false);
    }

    private void updateKeyState(int keyCode, boolean state) {
        switch (keyCode) {
            case KeyEvent.VK_W:
                keyDirectionStates[Direction.UP.ordinal()] = state;
                break;
            case KeyEvent.VK_S:
                keyDirectionStates[Direction.DOWN.ordinal()] = state;
                break;
            case KeyEvent.VK_A:
                keyDirectionStates[Direction.LEFT.ordinal()] = state;
                break;
            case KeyEvent.VK_D:
                keyDirectionStates[Direction.RIGHT.ordinal()] = state;
                break;
            case KeyEvent.VK_F:
                keyActionStates[Action.ATTACK.ordinal()] = state;
                break;
            case KeyEvent.VK_P:
                if (state) { // If the key is pressed
                    isPauseToggled = !isPauseToggled; // Toggle the pause state
                }
                break;
        }
    }

    public boolean isUpPressed() {
        return keyDirectionStates[Direction.UP.ordinal()];
    }

    public boolean isDownPressed() {
        return keyDirectionStates[Direction.DOWN.ordinal()];
    }

    public boolean isLeftPressed() {
        return keyDirectionStates[Direction.LEFT.ordinal()];
    }

    public boolean isRightPressed() {
        return keyDirectionStates[Direction.RIGHT.ordinal()];
    }

    public boolean isAttacking() {
        return keyActionStates[Action.ATTACK.ordinal()];
    }

    public boolean isPaused() {
        return isPauseToggled;
    }
}
