package classes.util;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardController implements KeyListener {

    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    private final boolean[] keyStates = new boolean[4]; // Array to store key states for each direction

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        updateKeyState(e.getKeyCode(), true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        updateKeyState(e.getKeyCode(), false);
    }

    private void updateKeyState(int keyCode, boolean state) {
        switch(keyCode) {
            case KeyEvent.VK_W:
                keyStates[Direction.UP.ordinal()] = state;
                break;
            case KeyEvent.VK_S:
                keyStates[Direction.DOWN.ordinal()] = state;
                break;
            case KeyEvent.VK_A:
                keyStates[Direction.LEFT.ordinal()] = state;
                break;
            case KeyEvent.VK_D:
                keyStates[Direction.RIGHT.ordinal()] = state;
                break;
        }
    }

    public boolean isUpPressed() {
        return keyStates[Direction.UP.ordinal()];
    }

    public boolean isDownPressed() {
        return keyStates[Direction.DOWN.ordinal()];
    }

    public boolean isLeftPressed() {
        return keyStates[Direction.LEFT.ordinal()];
    }

    public boolean isRightPressed() {
        return keyStates[Direction.RIGHT.ordinal()];
    }
}
