package classes.util.handlers;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardController implements KeyListener {

    public boolean UP, DOWN, LEFT, RIGHT;


    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        switch(code) {
            case KeyEvent.VK_W:
                UP = true;
                break;
            case KeyEvent.VK_S:
                DOWN = true;
                break;
            case KeyEvent.VK_A:
                LEFT = true;
                break;
            case KeyEvent.VK_D:
                RIGHT = true;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        switch(code) {
            case KeyEvent.VK_W:
                UP = false;
                break;
            case KeyEvent.VK_S:
                DOWN = false;
                break;
            case KeyEvent.VK_A:
                LEFT = false;
                break;
            case KeyEvent.VK_D:
                RIGHT = false;
                break;
        }
    }
}
