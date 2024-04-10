package game.util.controllers;

public class ControllerComponents {

    private final KeyboardController keyboardController;

    private final MouseController mouseController;

    public ControllerComponents(KeyboardController keyboardController, MouseController mouseController) {
        this.keyboardController = keyboardController;
        this.mouseController = mouseController;
    }

    public KeyboardController getKeyboardController() {
        return keyboardController;
    }

    public MouseController getMouseController() {
        return mouseController;
    }
}
