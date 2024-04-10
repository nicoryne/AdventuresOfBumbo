package game.exceptions;

import java.io.IOException;

public class GameInitializationException extends IOException {
    public GameInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

}
