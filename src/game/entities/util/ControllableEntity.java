package game.entities.util;

import game.util.controllers.KeyboardController;
import game.util.controllers.MouseController;

public interface ControllableEntity {

    void setKeyboardController(KeyboardController keyboardController);

    void setMouseController(MouseController mouseController);

}
