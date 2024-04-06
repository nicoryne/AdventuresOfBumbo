package classes.entities.util;

import classes.util.controllers.KeyboardController;
import classes.util.controllers.MouseController;

public interface ControllableEntity {

    void setKeyboardController(KeyboardController keyboardController);

    void setMouseController(MouseController mouseController);

}
