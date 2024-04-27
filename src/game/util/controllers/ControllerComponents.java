package game.util.controllers;

public class ControllerComponents {

    private final KeyboardController keyboardController;

    private final MouseController mouseController;

    public ControllerComponents() {
        this.keyboardController = new KeyboardController();
        this.mouseController = new MouseController();
    }

    public KeyboardController getKeyboardController() {
        return keyboardController;
    }

    public MouseController getMouseController() {
        return mouseController;
    }
}
